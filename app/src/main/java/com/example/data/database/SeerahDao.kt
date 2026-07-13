package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.Question
import com.example.data.model.UserStats
import com.example.data.model.QuizHistory
import com.example.data.model.ChatMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface SeerahDao {
    // --- Questions ---
    @Query("SELECT * FROM questions ORDER BY id ASC")
    fun getAllQuestions(): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE module = :module ORDER BY id ASC")
    fun getQuestionsByModule(module: String): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE category = :category ORDER BY id ASC")
    fun getQuestionsByCategory(category: String): Flow<List<Question>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Query("DELETE FROM questions WHERE id = :id")
    suspend fun deleteQuestion(id: Int)

    // --- User Stats ---
    @Query("SELECT * FROM user_stats WHERE userId = :userId LIMIT 1")
    fun getUserStats(userId: String = "guest"): Flow<UserStats?>

    @Query("SELECT * FROM user_stats WHERE userId = :userId LIMIT 1")
    suspend fun getUserStatsDirect(userId: String = "guest"): UserStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserStats(userStats: UserStats)

    // --- Quiz History ---
    @Query("SELECT * FROM quiz_history ORDER BY date DESC")
    fun getQuizHistory(): Flow<List<QuizHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizHistory(history: QuizHistory)

    // --- Chat Messages ---
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllChatMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatMessages()
}
