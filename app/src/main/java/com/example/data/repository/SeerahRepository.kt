package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.data.database.AppDatabase
import com.example.data.database.SeerahDao
import com.example.data.model.Question
import com.example.data.model.UserStats
import com.example.data.model.QuizHistory
import com.example.data.model.ChatMessage
import com.example.data.service.RetrofitClient
import com.example.data.service.GeminiRequest
import com.example.data.service.Content
import com.example.data.service.Part
import com.example.data.service.GenerationConfig
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.BuildConfig

@JsonClass(generateAdapter = true)
data class AiQuestionJson(
    val text: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String
)

class SeerahRepository(context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val dao: SeerahDao = db.seerahDao()

    // --- Exposed Database Streams ---
    val allQuestions: Flow<List<Question>> = dao.getAllQuestions()
    val userStats: Flow<UserStats?> = dao.getUserStats()
    val quizHistory: Flow<List<QuizHistory>> = dao.getQuizHistory()
    val chatMessages: Flow<List<ChatMessage>> = dao.getAllChatMessages()

    // --- Initialize Database with Seed Questions if Empty ---
    suspend fun seedDatabaseIfNeeded() = withContext(Dispatchers.IO) {
        val currentQuestions = dao.getAllQuestions().firstOrNull() ?: emptyList()
        if (currentQuestions.isEmpty()) {
            dao.insertQuestions(InitialQuestions.questions)
            Log.d("SeerahRepository", "Database successfully seeded with ${InitialQuestions.questions.size} authentic questions.")
        }
        
        // Initialize user stats if they don't exist
        val stats = dao.getUserStatsDirect()
        if (stats == null) {
            dao.insertOrUpdateUserStats(UserStats())
        }
    }

    // --- Questions CRUD ---
    suspend fun insertQuestion(question: Question) = withContext(Dispatchers.IO) {
        dao.insertQuestion(question)
    }

    suspend fun deleteQuestion(id: Int) = withContext(Dispatchers.IO) {
        dao.deleteQuestion(id)
    }

    suspend fun getQuestionsByModule(module: String): Flow<List<Question>> {
        return dao.getQuestionsByModule(module)
    }

    suspend fun getQuestionsByCategory(category: String): Flow<List<Question>> {
        return dao.getQuestionsByCategory(category)
    }

    // --- Stats & Scoring Logic ---
    suspend fun updateStatsAfterQuiz(
        score: Int,
        correctCount: Int,
        totalQuestions: Int,
        isSurvivalGameOver: Boolean,
        mode: String,
        isCompleted: Boolean
    ) = withContext(Dispatchers.IO) {
        val currentStats = dao.getUserStatsDirect() ?: UserStats()
        
        // Compute base points: Correct answer = +10, wrong = 0
        var addedPoints = correctCount * 10
        
        // Streaks and streak bonuses
        // We calculate if the user had streak bonuses: Streak of 5 = +25, Streak of 10 = +50
        // (In ViewModel we track active streak. We add bonus points to the final result)
        var newStreak = currentStats.currentStreak
        if (correctCount == totalQuestions && totalQuestions >= 5) {
            newStreak += 1
            if (newStreak % 10 == 0) {
                addedPoints += 50 // Streak of 10 bonus
            } else if (newStreak % 5 == 0) {
                addedPoints += 25 // Streak of 5 bonus
            }
        } else if (correctCount < totalQuestions / 2) {
            newStreak = 0 // Break streak if performance is low
        } else {
            newStreak += 1
        }

        if (isCompleted && mode == "Survival") {
            addedPoints += 100 // Survival Completion bonus
        }

        val finalPoints = currentStats.points + addedPoints
        val maxStr = maxOf(currentStats.maxStreak, newStreak)

        // Determine Level based on points
        // Level 1: Student (0-200)
        // Level 2: Talib-e-Ilm (201-500)
        // Level 3: Da'i (501-1000)
        // Level 4: Scholar (1001-2000)
        // Level 5: Muhaddith (2011-4000)
        // Level 6: Seerah Expert (4001-7000)
        // Level 7: Seerah Master (7001+)
        val finalLevel = when {
            finalPoints >= 7000 -> "Level 7: Seerah Master"
            finalPoints >= 4000 -> "Level 6: Seerah Expert"
            finalPoints >= 2000 -> "Level 5: Muhaddith"
            finalPoints >= 1000 -> "Level 4: Scholar"
            finalPoints >= 500  -> "Level 3: Da'i"
            finalPoints >= 200  -> "Level 2: Talib-e-Ilm"
            else -> "Level 1: Student"
        }

        // Percentage for certificates
        val pct = if (totalQuestions > 0) (correctCount.toDouble() / totalQuestions.toDouble()) * 100.0 else 0.0
        val isBronze = currentStats.bronzeCertificateEarned || pct >= 60.0
        val isSilver = currentStats.silverCertificateEarned || pct >= 75.0
        val isGold = currentStats.goldCertificateEarned || pct >= 90.0
        val isMaster = currentStats.masterCertificateEarned || pct >= 100.0

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayStr = dateFormat.format(Date())

        val updatedStats = currentStats.copy(
            points = finalPoints,
            level = finalLevel,
            currentStreak = newStreak,
            maxStreak = maxStr,
            completedQuizzesCount = currentStats.completedQuizzesCount + 1,
            battlesUnlockedCount = if (mode == "Battle" && pct >= 70.0) {
                minOf(10, currentStats.battlesUnlockedCount + 1)
            } else currentStats.battlesUnlockedCount,
            lastQuizDate = todayStr,
            bronzeCertificateEarned = isBronze,
            silverCertificateEarned = isSilver,
            goldCertificateEarned = isGold,
            masterCertificateEarned = isMaster
        )

        dao.insertOrUpdateUserStats(updatedStats)
    }

    suspend fun saveQuizHistory(history: QuizHistory) = withContext(Dispatchers.IO) {
        dao.insertQuizHistory(history)
    }

    // --- AI Tutor: Ask Seerah questions ---
    suspend fun askAiTutor(userMessage: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "API Key is missing or default. Please add your GEMINI_API_KEY in the AI Studio Secrets panel."
        }

        // Record User Message
        dao.insertChatMessage(ChatMessage(sender = "user", content = userMessage))

        val prompt = "You are an expert Islamic Scholar and AI Seerah Tutor. " +
                "Answer the following question about the life and history of Prophet Muhammad ﷺ in an informative, " +
                "highly respectful, and accurate manner. Quote verified classical sources (e.g., Sahih al-Bukhari, " +
                "Sahih Muslim, Ibn Hisham, Ar-Raheeq Al-Makhtum) wherever possible. Keep the explanation engaging " +
                "and educational.\n\nUser Question: $userMessage"

        val request = GeminiRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            systemInstruction = Content(parts = listOf(Part(text = "You are a reliable, peaceful, and extremely accurate AI Seerah Tutor.")))
        )

        val tutorResponse = try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "I was unable to retrieve a response from the Seerah Tutor network."
        } catch (e: Exception) {
            "Error communicating with AI Tutor: ${e.localizedMessage ?: "Unknown connection error"}"
        }

        // Record Tutor Response
        dao.insertChatMessage(ChatMessage(sender = "tutor", content = tutorResponse))

        return@withContext tutorResponse
    }

    suspend fun clearChatHistory() = withContext(Dispatchers.IO) {
        dao.clearChatMessages()
    }

    // --- AI MCQ Generator: Generate interactive questions on the fly ---
    suspend fun generateAiMcq(category: String, difficulty: String): Question? = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext null
        }

        val prompt = "Generate one unique, historically authentic multiple-choice question (MCQ) about " +
                "the life of Prophet Muhammad ﷺ specifically for the topic: '$category' and difficulty level: '$difficulty'.\n" +
                "You MUST return ONLY a raw JSON object matching this exact schema, and absolutely nothing else. No markdown wraps, no extra characters:\n" +
                "{\n" +
                "  \"text\": \"The text of the question?\",\n" +
                "  \"options\": [\"Option 1\", \"Option 2\", \"Option 3\", \"Option 4\"],\n" +
                "  \"correctAnswer\": \"Option 1 (must be an exact string equal to one of the options)\",\n" +
                "  \"explanation\": \"A clean explanation citing historical references.\"\n" +
                "}"

        val request = GeminiRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            generationConfig = GenerationConfig(temperature = 0.7f)
        )

        try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            val jsonText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: return@withContext null
            
            // Clean the JSON string from markdown backticks if any
            val cleanJson = jsonText.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            val adapter = RetrofitClient.moshi.adapter(AiQuestionJson::class.java)
            val parsed = adapter.fromJson(cleanJson) ?: return@withContext null

            // Construct and return Question
            Question(
                text = parsed.text,
                options = parsed.options.joinToString("|"),
                correctAnswer = parsed.correctAnswer,
                explanation = parsed.explanation,
                category = category,
                module = if (category in listOf("Badr", "Uhud", "Khandaq (Ahzab)", "Banu Qaynuqa", "Banu Nadir", "Banu Qurayzah", "Khaybar", "Mu'tah", "Hunayn", "Tabuk")) "Battle" else "Dynamic AI Generator",
                difficulty = difficulty,
                isCustom = false
            )
        } catch (e: Exception) {
            Log.e("SeerahRepository", "Failed to generate AI question: ${e.message}", e)
            null
        }
    }
}
