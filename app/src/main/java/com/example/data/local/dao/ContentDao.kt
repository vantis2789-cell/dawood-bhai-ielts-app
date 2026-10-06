package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.BookEntity
import com.example.data.local.entity.QuestionEntity
import com.example.data.local.entity.SectionEntity
import com.example.data.local.entity.TestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {
    // ================= Books =================
    @Query("SELECT * FROM books WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    // ================= Tests =================
    @Query("SELECT * FROM tests WHERE isPublished = 1 ORDER BY createdAt DESC")
    fun getPublishedTests(): Flow<List<TestEntity>>

    @Query("SELECT * FROM tests ORDER BY createdAt DESC")
    fun getAllTestsAdmin(): Flow<List<TestEntity>>

    @Query("SELECT * FROM tests WHERE id = :testId LIMIT 1")
    suspend fun getTestById(testId: String): TestEntity?

    @Query("SELECT * FROM tests WHERE bookId = :bookId AND isPublished = 1 ORDER BY testNumber ASC")
    fun getTestsByBook(bookId: String): Flow<List<TestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTest(test: TestEntity)

    @Query("UPDATE tests SET isPublished = :isPublished WHERE id = :testId")
    suspend fun setTestPublished(testId: String, isPublished: Boolean)

    @Delete
    suspend fun deleteTest(test: TestEntity)

    // ================= Sections =================
    @Query("SELECT * FROM test_sections WHERE testId = :testId ORDER BY sectionNumber ASC")
    suspend fun getSectionsForTest(testId: String): List<SectionEntity>

    @Query("SELECT * FROM test_sections WHERE testId = :testId ORDER BY sectionNumber ASC")
    fun getSectionsForTestFlow(testId: String): Flow<List<SectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSection(section: SectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSections(sections: List<SectionEntity>)

    @Query("DELETE FROM test_sections WHERE id = :sectionId")
    suspend fun deleteSection(sectionId: String)

    // ================= Questions =================
    @Query("SELECT * FROM questions WHERE sectionId = :sectionId ORDER BY questionNumber ASC")
    suspend fun getQuestionsForSection(sectionId: String): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE testId = :testId ORDER BY questionNumber ASC")
    suspend fun getQuestionsForTest(testId: String): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE testId = :testId ORDER BY questionNumber ASC")
    fun getQuestionsForTestFlow(testId: String): Flow<List<QuestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Query("DELETE FROM questions WHERE id = :questionId")
    suspend fun deleteQuestion(questionId: String)
}
