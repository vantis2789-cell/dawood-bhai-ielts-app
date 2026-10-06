package com.example

import com.example.data.ai.DefaultIeltsAiServiceImpl
import com.example.data.engine.BandScoreCalculator
import com.example.data.model.DailyMissionItem
import com.example.data.model.IeltsModuleType
import com.example.data.model.IeltsType
import com.example.data.model.QuestionType
import com.example.data.model.TestQuestion
import com.example.data.model.UserRole
import com.example.data.repository.IeltsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testListeningBandCalculations() {
        assertEquals(9.0, BandScoreCalculator.calculateListeningBand(40), 0.01)
        assertEquals(9.0, BandScoreCalculator.calculateListeningBand(39), 0.01)
        assertEquals(8.5, BandScoreCalculator.calculateListeningBand(38), 0.01)
        assertEquals(8.0, BandScoreCalculator.calculateListeningBand(35), 0.01)
        assertEquals(7.5, BandScoreCalculator.calculateListeningBand(33), 0.01)
        assertEquals(7.0, BandScoreCalculator.calculateListeningBand(30), 0.01)
        assertEquals(6.5, BandScoreCalculator.calculateListeningBand(27), 0.01)
        assertEquals(6.0, BandScoreCalculator.calculateListeningBand(24), 0.01)
    }

    @Test
    fun testReadingAcademicBandCalculations() {
        assertEquals(9.0, BandScoreCalculator.calculateReadingBand(39, IeltsType.ACADEMIC), 0.01)
        assertEquals(8.0, BandScoreCalculator.calculateReadingBand(35, IeltsType.ACADEMIC), 0.01)
        assertEquals(7.5, BandScoreCalculator.calculateReadingBand(33, IeltsType.ACADEMIC), 0.01)
        assertEquals(7.0, BandScoreCalculator.calculateReadingBand(31, IeltsType.ACADEMIC), 0.01)
        assertEquals(6.5, BandScoreCalculator.calculateReadingBand(28, IeltsType.ACADEMIC), 0.01)
    }

    @Test
    fun testReadingGeneralTrainingBandCalculations() {
        assertEquals(9.0, BandScoreCalculator.calculateReadingBand(40, IeltsType.GENERAL_TRAINING), 0.01)
        assertEquals(8.5, BandScoreCalculator.calculateReadingBand(39, IeltsType.GENERAL_TRAINING), 0.01)
        assertEquals(8.0, BandScoreCalculator.calculateReadingBand(37, IeltsType.GENERAL_TRAINING), 0.01)
        assertEquals(7.5, BandScoreCalculator.calculateReadingBand(36, IeltsType.GENERAL_TRAINING), 0.01)
        assertEquals(7.0, BandScoreCalculator.calculateReadingBand(34, IeltsType.GENERAL_TRAINING), 0.01)
    }

    @Test
    fun testOfficialIeltsRoundingRules() {
        // 6.25 rounds up to 6.5
        assertEquals(6.5, BandScoreCalculator.roundToIeltsBand(6.25), 0.01)
        // 6.75 rounds up to 7.0
        assertEquals(7.0, BandScoreCalculator.roundToIeltsBand(6.75), 0.01)
        // 6.1 rounds down to 6.0
        assertEquals(6.0, BandScoreCalculator.roundToIeltsBand(6.1), 0.01)
        // 6.6 rounds down to 6.5
        assertEquals(6.5, BandScoreCalculator.roundToIeltsBand(6.6), 0.01)
    }

    @Test
    fun testOverallBandCalculation() {
        // Listening: 7.5, Reading: 7.0, Writing: 6.5, Speaking: 6.5 -> Avg: 6.875 -> 7.0
        val overall = BandScoreCalculator.calculateOverallBand(7.5, 7.0, 6.5, 6.5)
        assertEquals(7.0, overall, 0.01)
    }

    @Test
    fun testQuestionScoringEvaluation() {
        val q1 = TestQuestion(
            id = "q_test_1",
            questionNumber = 1,
            questionType = QuestionType.FORM_COMPLETION,
            prompt = "Address: 42 _______ Crescent",
            correctAnswer = "Highfield",
            explanation = "Alex confirmed Highfield Crescent."
        )

        // Case-insensitive trimmed match
        val studentAnswer1 = "  highfield "
        assertTrue(studentAnswer1.trim().equals(q1.correctAnswer.trim(), ignoreCase = true))

        val studentAnswer2 = "Highland"
        assertFalse(studentAnswer2.trim().equals(q1.correctAnswer.trim(), ignoreCase = true))
    }

    @Test
    fun testDailyProgressCalculation() {
        val missions = listOf(
            DailyMissionItem("1", "Listening", IeltsModuleType.LISTENING, 15, true),
            DailyMissionItem("2", "Reading", IeltsModuleType.READING, 20, true),
            DailyMissionItem("3", "Writing", IeltsModuleType.WRITING, 40, false),
            DailyMissionItem("4", "Speaking", IeltsModuleType.SPEAKING, 15, false)
        )
        val completed = missions.count { it.isCompleted }
        val total = missions.size
        val progress = completed.toFloat() / total

        assertEquals(2, completed)
        assertEquals(4, total)
        assertEquals(0.5f, progress, 0.001f)
    }

    @Test
    fun testStudyPlanGeneration() = runBlocking {
        val aiService = DefaultIeltsAiServiceImpl()
        val result = aiService.generatePersonalizedStudyPlan(
            currentBand = 6.5,
            targetBand = 7.5,
            weakAreas = listOf("Writing Task 2 Cohesion"),
            daysRemaining = 42
        )

        assertTrue(result.isSuccess)
        val plan = result.getOrNull()
        assertEquals(7.5, plan?.candidateTargetBand ?: 0.0, 0.01)
        assertEquals(42, plan?.estimatedDaysToReachTarget)
        assertTrue(plan?.weeklyMilestones?.isNotEmpty() == true)
    }

    @Test
    fun testAuthenticationValidation() {
        // Valid email check
        val validEmail = "haris.ielts@academy.db"
        assertTrue(validEmail.contains("@") && validEmail.contains("."))

        val invalidEmail = "candidateWithoutAtSymbol"
        assertFalse(invalidEmail.contains("@") && invalidEmail.contains("."))

        // Password length check
        val shortPassword = "123"
        val validPassword = "password2026"
        assertFalse(shortPassword.length >= 6)
        assertTrue(validPassword.length >= 6)
    }

    @Test
    fun testRepositoryAttemptLifecycleAndScoring() = runBlocking {
        val repository = IeltsRepository.getInstance()

        // 1. Start Attempt
        val attemptId = repository.startAttempt("test_ac_01", IeltsModuleType.READING)
        assertNotNull(attemptId)
        assertTrue(attemptId.startsWith("att_"))

        // 2. Save Answers
        repository.saveAnswer(
            attemptId = attemptId,
            questionId = "qr1_1",
            questionNumber = 1,
            prompt = "Subterranean ecosystems contain roughly one-third of all living biomass on Earth.",
            userAnswer = "TRUE",
            correctAnswer = "TRUE",
            explanation = "Passage confirms statement.",
            isMarkedForReview = false
        )
        repository.saveAnswer(
            attemptId = attemptId,
            questionId = "qr1_2",
            questionNumber = 2,
            prompt = "Microbial life relies primarily on solar photosynthesis.",
            userAnswer = "FALSE",
            correctAnswer = "FALSE",
            explanation = "Lithotrophic organisms rely on radiolytic decay.",
            isMarkedForReview = true
        )

        // 3. Submit Attempt
        val calculatedBand = repository.submitAttempt(attemptId, timeTakenSeconds = 450)
        assertTrue(calculatedBand >= 2.5 && calculatedBand <= 9.0)

        // 4. Retrieve Question Review
        val reviewAttempt = repository.getAttemptReview(attemptId)
        assertNotNull(reviewAttempt)
        assertEquals(attemptId, reviewAttempt?.id)
        assertEquals(2, reviewAttempt?.answers?.size)
        assertTrue(reviewAttempt?.answers?.all { it.isCorrect } == true)
    }

    @Test
    fun testRepositoryAuthenticationWorkflow() = runBlocking {
        val repository = IeltsRepository.getInstance()

        // Register new actual candidate
        val regResult = repository.registerUser(
            email = "candidate.real@academy.db",
            password = "securePassword2026",
            fullName = "Candidate Alpha",
            role = UserRole.STUDENT
        )
        assertTrue(regResult.isSuccess)
        assertEquals("Candidate Alpha", regResult.getOrNull()?.name)

        // Login with actual registered credentials
        val loginResult = repository.loginUser("candidate.real@academy.db", "securePassword2026")
        assertTrue(loginResult.isSuccess)
        assertEquals("Candidate Alpha", loginResult.getOrNull()?.name)
        assertEquals(UserRole.STUDENT, loginResult.getOrNull()?.role)

        // Login with admin credentials
        val adminResult = repository.loginUser("admin@dawoodbhai-ielts.com", "admin2026")
        assertTrue(adminResult.isSuccess)
        assertEquals(UserRole.ADMIN, adminResult.getOrNull()?.role)

        // Logout
        repository.logoutUser()
        assertEquals("", repository.userProfile.value.name)
    }

    @Test
    fun testSupabaseClientConfigurationAndUrls() {
        com.example.data.remote.SupabaseClient.initialize(
            url = "https://custom-project.supabase.co",
            anonKey = "custom_test_anon_key_123"
        )
        assertEquals("https://custom-project.supabase.co", com.example.data.remote.SupabaseClient.supabaseUrl)
        assertEquals("custom_test_anon_key_123", com.example.data.remote.SupabaseClient.supabaseAnonKey)

        val headers = com.example.data.remote.SupabaseClient.getHeaders()
        assertEquals("custom_test_anon_key_123", headers["apikey"])
        assertEquals("Bearer custom_test_anon_key_123", headers["Authorization"])

        // Test Profiles and TestSets URL generation
        val profileUrl = com.example.data.remote.SupabaseClient.getProfilesUrl("usr_test_99")
        assertEquals("https://custom-project.supabase.co/rest/v1/profiles?id=eq.usr_test_99&select=*", profileUrl)

        val testSetsUrl = com.example.data.remote.SupabaseClient.getTestSetsUrl()
        assertEquals("https://custom-project.supabase.co/rest/v1/test_sets?select=*", testSetsUrl)
    }
}
