package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.SpeakingSubmissionEntity
import com.example.data.local.entity.WritingSubmissionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubmissionDao {
    // ================= Writing =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWritingSubmission(submission: WritingSubmissionEntity)

    @Query("SELECT * FROM writing_submissions WHERE userId = :userId ORDER BY submittedAt DESC")
    fun getWritingSubmissionsForUser(userId: String): Flow<List<WritingSubmissionEntity>>

    @Query("SELECT * FROM writing_submissions ORDER BY submittedAt DESC")
    fun getAllWritingSubmissions(): Flow<List<WritingSubmissionEntity>>

    @Query("""
        UPDATE writing_submissions SET 
            isEvaluated = 1,
            trScore = :tr,
            ccScore = :cc,
            lrScore = :lr,
            graScore = :gra,
            overallBand = :overall,
            teacherFeedback = :feedback,
            teacherStrengths = :strengths,
            teacherWeaknesses = :weaknesses,
            teacherCorrections = :corrections
        WHERE id = :submissionId
    """)
    suspend fun gradeWritingSubmission(
        submissionId: String,
        tr: Double,
        cc: Double,
        lr: Double,
        gra: Double,
        overall: Double,
        feedback: String,
        strengths: String,
        weaknesses: String,
        corrections: String
    )

    // ================= Speaking =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpeakingSubmission(submission: SpeakingSubmissionEntity)

    @Query("SELECT * FROM speaking_submissions WHERE userId = :userId ORDER BY recordedDate DESC")
    fun getSpeakingSubmissionsForUser(userId: String): Flow<List<SpeakingSubmissionEntity>>

    @Query("SELECT * FROM speaking_submissions ORDER BY recordedDate DESC")
    fun getAllSpeakingSubmissions(): Flow<List<SpeakingSubmissionEntity>>

    @Query("""
        UPDATE speaking_submissions SET 
            isEvaluated = 1,
            fluencyScore = :fluency,
            vocabScore = :vocab,
            grammarScore = :grammar,
            pronunciationScore = :pron,
            overallBand = :overall,
            teacherFeedback = :feedback
        WHERE id = :submissionId
    """)
    suspend fun gradeSpeakingSubmission(
        submissionId: String,
        fluency: Double,
        vocab: Double,
        grammar: Double,
        pron: Double,
        overall: Double,
        feedback: String
    )
}
