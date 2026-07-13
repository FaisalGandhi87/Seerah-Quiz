package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val options: String, // Pipe-separated options: "Option A|Option B|Option C|Option D"
    val correctAnswer: String,
    val explanation: String,
    val category: String, // e.g., "Birth", "Badr", "Taif", etc.
    val module: String, // e.g., "Module 1", "Module 2", "Battle"
    val difficulty: String = "Medium", // Easy, Medium, Hard
    val isCustom: Boolean = false
) : Serializable {
    fun getOptionsList(): List<String> = options.split("|").map { it.trim() }
}

@Entity(tableName = "user_stats")
data class UserStats(
    @PrimaryKey val userId: String = "guest",
    val points: Int = 0,
    val level: String = "Level 1: Student", // Student, Talib-e-Ilm, Da'i, Scholar, Muhaddith, Seerah Expert, Seerah Master
    val currentStreak: Int = 0,
    val maxStreak: Int = 0,
    val completedQuizzesCount: Int = 0,
    val battlesUnlockedCount: Int = 1, // Battle levels unlock progressively
    val lastQuizDate: String = "",
    val bronzeCertificateEarned: Boolean = false,
    val silverCertificateEarned: Boolean = false,
    val goldCertificateEarned: Boolean = false,
    val masterCertificateEarned: Boolean = false
)

@Entity(tableName = "quiz_history")
data class QuizHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // e.g., "Module 1", "Badr", etc.
    val mode: String, // Practice, Exam, Battle, Daily, Survival
    val score: Int,
    val totalQuestions: Int,
    val percentage: Double,
    val timeTakenSeconds: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val weakAreas: String, // Comma-separated topics
    val suggestedRevision: String, // Comma-separated revision topics
    val date: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String, // "user" or "tutor"
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
