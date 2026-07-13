package com.example.ui

import android.app.Application
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ChatMessage
import com.example.data.model.Question
import com.example.data.model.QuizHistory
import com.example.data.model.UserStats
import com.example.data.repository.SeerahRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class SeerahViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {
    private val repository = SeerahRepository(application)

    // --- Database flows ---
    val allQuestions: StateFlow<List<Question>> = repository.allQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userStats: StateFlow<UserStats?> = repository.userStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserStats())

    val quizHistory: StateFlow<List<QuizHistory>> = repository.quizHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Active Quiz State ---
    private val _quizQuestions = MutableStateFlow<List<Question>>(emptyList())
    val quizQuestions = _quizQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex = _currentQuestionIndex.asStateFlow()

    private val _selectedAnswer = MutableStateFlow<String?>(null)
    val selectedAnswer = _selectedAnswer.asStateFlow()

    private val _isAnswerChecked = MutableStateFlow(false)
    val isAnswerChecked = _isAnswerChecked.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore = _quizScore.asStateFlow()

    private val _correctAnswersCount = MutableStateFlow(0)
    val correctAnswersCount = _correctAnswersCount.asStateFlow()

    private val _wrongAnswersCount = MutableStateFlow(0)
    val wrongAnswersCount = _wrongAnswersCount.asStateFlow()

    private val _quizTimeSeconds = MutableStateFlow(0)
    val quizTimeSeconds = _quizTimeSeconds.asStateFlow()

    private val _quizMode = MutableStateFlow("Practice") // Practice, Exam, Battle, Daily, Survival
    val quizMode = _quizMode.asStateFlow()

    private val _quizCategory = MutableStateFlow("General")
    val quizCategory = _quizCategory.asStateFlow()

    private val _quizModule = MutableStateFlow("Module 1")
    val quizModule = _quizModule.asStateFlow()

    private val _isQuizFinished = MutableStateFlow(false)
    val isQuizFinished = _isQuizFinished.asStateFlow()

    private val _quizStreak = MutableStateFlow(0)
    val quizStreak = _quizStreak.asStateFlow()

    private val _weakAreas = MutableStateFlow<List<String>>(emptyList())
    val weakAreas = _weakAreas.asStateFlow()

    private val _suggestedRevisions = MutableStateFlow<List<String>>(emptyList())
    val suggestedRevisions = _suggestedRevisions.asStateFlow()

    // Timer Job
    private var timerJob: Job? = null

    // --- AI Tutor State ---
    private val _isTutorLoading = MutableStateFlow(false)
    val isTutorLoading = _isTutorLoading.asStateFlow()

    private val _tutorError = MutableStateFlow<String?>(null)
    val tutorError = _tutorError.asStateFlow()

    // --- AI Generator State ---
    private val _isGeneratingMcq = MutableStateFlow(false)
    val isGeneratingMcq = _isGeneratingMcq.asStateFlow()

    private val _generatedMcq = MutableStateFlow<Question?>(null)
    val generatedMcq = _generatedMcq.asStateFlow()

    // --- TextToSpeech State ---
    private var textToSpeech: TextToSpeech? = null
    private val _isTtsInitialized = MutableStateFlow(false)
    val isTtsInitialized = _isTtsInitialized.asStateFlow()

    init {
        // Initialize Database Seeding
        viewModelScope.launch {
            repository.seedDatabaseIfNeeded()
        }

        // Initialize Native Text to Speech
        textToSpeech = TextToSpeech(application, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.language = Locale.ENGLISH
            _isTtsInitialized.value = true
        } else {
            Log.e("SeerahViewModel", "TextToSpeech Initialization Failed.")
        }
    }

    // --- TextToSpeech Voice Reading ---
    fun speakText(text: String, language: String = "en") {
        if (!_isTtsInitialized.value) return
        viewModelScope.launch {
            val locale = when (language) {
                "ur" -> Locale("ur", "PK") // Urdu
                "ar" -> Locale("ar", "SA") // Arabic
                else -> Locale.ENGLISH     // English Default
            }
            textToSpeech?.language = locale
            textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun stopSpeaking() {
        textToSpeech?.stop()
    }

    // --- Start Quiz Session ---
    fun startQuiz(module: String, category: String, mode: String) {
        viewModelScope.launch {
            _quizMode.value = mode
            _quizCategory.value = category
            _quizModule.value = module
            _currentQuestionIndex.value = 0
            _selectedAnswer.value = null
            _isAnswerChecked.value = false
            _quizScore.value = 0
            _correctAnswersCount.value = 0
            _wrongAnswersCount.value = 0
            _quizTimeSeconds.value = 0
            _isQuizFinished.value = false
            _quizStreak.value = 0
            _weakAreas.value = emptyList()
            _suggestedRevisions.value = emptyList()

            // Fetch questions from Room Database
            val allDbQuestions = allQuestions.value
            val filtered = if (module == "Battle") {
                allDbQuestions.filter { it.module == "Battle" && it.category == category }
            } else if (module == "Daily") {
                allDbQuestions.shuffled().take(30)
            } else if (module == "Survival") {
                allDbQuestions.shuffled() // Infinite pool
            } else {
                allDbQuestions.filter { it.module == module && (category == "All" || it.category == category) }
            }

            // Shuffle questions and their answer choices
            val shuffledQuestions = filtered.map { q ->
                val opts = q.getOptionsList().shuffled()
                q.copy(options = opts.joinToString("|"))
            }.shuffled()

            _quizQuestions.value = shuffledQuestions

            // Start Timer
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _quizTimeSeconds.value += 1
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    // --- Answer Operations ---
    fun selectAnswer(answer: String) {
        if (_isAnswerChecked.value && _quizMode.value == "Practice") return
        _selectedAnswer.value = answer
    }

    fun submitAnswer() {
        val currentQ = _quizQuestions.value.getOrNull(_currentQuestionIndex.value) ?: return
        val selectedAns = _selectedAnswer.value ?: return

        val isCorrect = selectedAns == currentQ.correctAnswer

        if (_quizMode.value == "Practice") {
            // In Practice Mode, we reveal correct answer instantly and do not proceed automatically
            _isAnswerChecked.value = true
            if (isCorrect) {
                _quizScore.value += 10
                _correctAnswersCount.value += 1
                _quizStreak.value += 1
            } else {
                _wrongAnswersCount.value += 1
                _quizStreak.value = 0
                // Track weaknesses
                if (!weakAreas.value.contains(currentQ.category)) {
                    _weakAreas.value = _weakAreas.value + currentQ.category
                    _suggestedRevisions.value = _suggestedRevisions.value + "Review details of ${currentQ.category} in Seerah history."
                }
            }
        } else if (_quizMode.value == "Survival") {
            // In Survival Mode, wrong answer is instant Game Over
            if (isCorrect) {
                _quizScore.value += 10
                _correctAnswersCount.value += 1
                _quizStreak.value += 1
                moveToNextQuestion()
            } else {
                _wrongAnswersCount.value += 1
                _quizStreak.value = 0
                finishQuiz(isSurvivalGameOver = true)
            }
        } else {
            // Exam Mode / Battle Challenge / Daily Mode
            if (isCorrect) {
                _quizScore.value += 10
                _correctAnswersCount.value += 1
                _quizStreak.value += 1
            } else {
                _wrongAnswersCount.value += 1
                _quizStreak.value = 0
                if (!weakAreas.value.contains(currentQ.category)) {
                    _weakAreas.value = _weakAreas.value + currentQ.category
                    _suggestedRevisions.value = _suggestedRevisions.value + "Review details of ${currentQ.category} in Seerah history."
                }
            }
            moveToNextQuestion()
        }
    }

    fun moveToNextQuestion() {
        _selectedAnswer.value = null
        _isAnswerChecked.value = false
        val nextIdx = _currentQuestionIndex.value + 1
        if (nextIdx < _quizQuestions.value.size) {
            _currentQuestionIndex.value = nextIdx
        } else {
            finishQuiz(isSurvivalGameOver = false)
        }
    }

    private fun finishQuiz(isSurvivalGameOver: Boolean = false) {
        stopTimer()
        _isQuizFinished.value = true

        viewModelScope.launch {
            val totalQ = if (_quizMode.value == "Survival") _correctAnswersCount.value + _wrongAnswersCount.value else _quizQuestions.value.size
            val correct = _correctAnswersCount.value
            val pct = if (totalQ > 0) (correct.toDouble() / totalQ.toDouble()) * 100.0 else 0.0

            // Save History to local DB
            val history = QuizHistory(
                category = "${_quizModule.value} - ${_quizCategory.value}",
                mode = _quizMode.value,
                score = _quizScore.value,
                totalQuestions = totalQ,
                percentage = pct,
                timeTakenSeconds = _quizTimeSeconds.value,
                correctAnswers = correct,
                wrongAnswers = _wrongAnswersCount.value,
                weakAreas = _weakAreas.value.joinToString(", ").ifEmpty { "None! All facts mastered!" },
                suggestedRevision = _suggestedRevisions.value.joinToString(", ").ifEmpty { "Keep reviewing advanced Seerah modules." }
            )
            repository.saveQuizHistory(history)

            // Update global user stats, levels, streaks and certificates
            repository.updateStatsAfterQuiz(
                score = _quizScore.value,
                correctCount = correct,
                totalQuestions = totalQ,
                isSurvivalGameOver = isSurvivalGameOver,
                mode = _quizMode.value,
                isCompleted = !isSurvivalGameOver
            )
        }
    }

    // --- AI Tutor Operations ---
    fun askTutor(messageText: String) {
        if (messageText.isBlank()) return
        viewModelScope.launch {
            _isTutorLoading.value = true
            _tutorError.value = null
            try {
                repository.askAiTutor(messageText)
            } catch (e: Exception) {
                _tutorError.value = e.localizedMessage
            } finally {
                _isTutorLoading.value = false
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChatHistory()
        }
    }

    // --- AI Question Generator ---
    fun generateQuestionWithAi(category: String, difficulty: String) {
        viewModelScope.launch {
            _isGeneratingMcq.value = true
            _generatedMcq.value = null
            try {
                val q = repository.generateAiMcq(category, difficulty)
                _generatedMcq.value = q
            } catch (e: Exception) {
                Log.e("SeerahViewModel", "Error generating AI question", e)
            } finally {
                _isGeneratingMcq.value = false
            }
        }
    }

    fun saveGeneratedQuestionToDb() {
        val q = _generatedMcq.value ?: return
        viewModelScope.launch {
            repository.insertQuestion(q)
            _generatedMcq.value = null
        }
    }

    // --- Admin Operations ---
    fun addCustomQuestion(
        text: String,
        options: List<String>,
        correctAnswer: String,
        explanation: String,
        category: String,
        module: String,
        difficulty: String
    ) {
        viewModelScope.launch {
            val q = Question(
                text = text,
                options = options.joinToString("|"),
                correctAnswer = correctAnswer,
                explanation = explanation,
                category = category,
                module = module,
                difficulty = difficulty,
                isCustom = true
            )
            repository.insertQuestion(q)
        }
    }

    fun deleteQuestion(id: Int) {
        viewModelScope.launch {
            repository.deleteQuestion(id)
        }
    }

    // Clear resources when ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }
}
