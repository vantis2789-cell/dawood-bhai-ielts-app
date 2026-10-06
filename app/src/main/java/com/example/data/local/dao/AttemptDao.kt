package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.AttemptAnswerEntity
import com.example.data.local.entity.AttemptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AttemptDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: AttemptEntity)

    @Update
    suspend fun updateAttempt(attempt: AttemptEntity)

    @Query("SELECT * FROM test_attempts WHERE id = :attemptId LIMIT 1")
    suspend fun getAttemptById(attemptId: String): AttemptEntity?

    @Query("SELECT * FROM test_attempts WHERE userId = :userId AND testId = :testId AND isCompleted = 0 LIMIT 1")
    suspend fun getUnfinishedAttempt(userId: String, testId: String): AttemptEntity?

    @Query("SELECT * FROM test_attempts WHERE userId = :userId AND isCompleted = 1 ORDER BY completedAt DESC")
    fun getUserCompletedAttempts(userId: String): Flow<List<AttemptEntity>>

    @Query("SELECT * FROM test_attempts WHERE isCompleted = 1 ORDER BY completedAt DESC")
    fun getAllAttemptsAdmin(): Flow<List<AttemptEntity>>

    // ================= Attempt Answers =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAnswer(answer: AttemptAnswerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAnswers(answers: List<AttemptAnswerEntity>)

    @Query("SELECT * FROM attempt_answers WHERE attemptId = :attemptId ORDER BY questionNumber ASC")
    suspend fun getAnswersForAttempt(attemptId: String): List<AttemptAnswerEntity>

    @Query("SELECT * FROM attempt_answers WHERE attemptId = :attemptId ORDER BY questionNumber ASC")
    fun getAnswersForAttemptFlow(attemptId: String): Flow<List<AttemptAnswerEntity>>

    @Query("UPDATE attempt_answers SET isMarkedForReview = :isMarked WHERE id = :answerId")
    suspend fun toggleMarkForReview(answerId: String, isMarked: Boolean)

    @Query("DELETE FROM test_attempts WHERE id = :attemptId")
    suspend fun deleteAttempt(attemptId: String)
}
