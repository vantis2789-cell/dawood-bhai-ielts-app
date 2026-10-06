package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val passwordHash: String,
    val fullName: String,
    val photoUrl: String? = null,
    val role: String = "STUDENT", // "STUDENT", "TEACHER", "ADMIN"
    val ieltsType: String = "ACADEMIC", // "ACADEMIC", "GENERAL_TRAINING"
    val currentBand: Double = 6.5,
    val targetBand: Double = 7.5,
    val dailyGoalMinutes: Int = 60,
    val streakDays: Int = 0,
    val studyHoursTotal: Double = 0.0,
    val xp: Int = 0,
    val level: Int = 1,
    val listeningBand: Double = 6.5,
    val readingBand: Double = 6.5,
    val writingBand: Double = 6.0,
    val speakingBand: Double = 6.0,
    val registrationDate: Long = System.currentTimeMillis(),
    val lastActive: Long = System.currentTimeMillis()
)

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val edition: String,
    val publisher: String,
    val coverUrl: String? = null,
    val testCount: Int = 4,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "tests")
data class TestEntity(
    @PrimaryKey val id: String,
    val bookId: String,
    val testNumber: Int,
    val title: String,
    val ieltsType: String = "ACADEMIC",
    val difficulty: String = "Standard",
    val durationMinutes: Int = 160,
    val totalQuestions: Int = 40,
    val moduleType: String = "READING", // "LISTENING", "READING", "WRITING", "SPEAKING", "FULL_MOCK"
    val contentSource: String = "Dawood Bhai IELTS Studio Original Curriculum",
    val copyrightOwner: String = "Dawood Bhai IELTS Studio & Sahil VANTIS",
    val licenseStatus: String = "Authorized Academy License",
    val isPublished: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "test_sections")
data class SectionEntity(
    @PrimaryKey val id: String,
    val testId: String,
    val moduleType: String,
    val sectionNumber: Int,
    val title: String,
    val instructions: String,
    val passageTitle: String? = null,
    val passageText: String? = null,
    val audioUrl: String? = null,
    val audioScript: String? = null
)

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val id: String,
    val testId: String,
    val sectionId: String,
    val questionNumber: Int,
    val questionType: String, // "MULTIPLE_CHOICE", "TRUE_FALSE_NOT_GIVEN", "FORM_COMPLETION", etc.
    val prompt: String,
    val optionsJson: String, // JSON serialized List<QuestionOption>
    val correctAnswer: String,
    val explanation: String,
    val difficulty: String = "Standard",
    val orderIndex: Int = 0
)

@Entity(tableName = "test_attempts")
data class AttemptEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val testId: String,
    val testTitle: String,
    val moduleType: String,
    val rawScore: Int,
    val totalQuestions: Int,
    val bandScore: Double,
    val timeTakenSeconds: Int,
    val isCompleted: Boolean,
    val startedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

@Entity(tableName = "attempt_answers")
data class AttemptAnswerEntity(
    @PrimaryKey val id: String, // e.g. "$attemptId-$questionId"
    val attemptId: String,
    val questionId: String,
    val questionNumber: Int,
    val prompt: String,
    val userAnswer: String?,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val isMarkedForReview: Boolean = false,
    val explanation: String
)

@Entity(tableName = "writing_submissions")
data class WritingSubmissionEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val studentName: String,
    val promptId: String,
    val taskType: String,
    val promptTitle: String,
    val essayText: String,
    val wordCount: Int,
    val trScore: Double? = null,
    val ccScore: Double? = null,
    val lrScore: Double? = null,
    val graScore: Double? = null,
    val overallBand: Double? = null,
    val teacherFeedback: String? = null,
    val teacherStrengths: String? = null,
    val teacherWeaknesses: String? = null,
    val teacherCorrections: String? = null,
    val submittedAt: Long = System.currentTimeMillis(),
    val isEvaluated: Boolean = false
)

@Entity(tableName = "speaking_submissions")
data class SpeakingSubmissionEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val studentName: String,
    val cueCardTitle: String,
    val part: Int,
    val durationSeconds: Int,
    val audioUrl: String? = null,
    val fluencyScore: Double? = null,
    val vocabScore: Double? = null,
    val grammarScore: Double? = null,
    val pronunciationScore: Double? = null,
    val overallBand: Double? = null,
    val teacherFeedback: String? = null,
    val recordedDate: Long = System.currentTimeMillis(),
    val isEvaluated: Boolean = false
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: String,
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
