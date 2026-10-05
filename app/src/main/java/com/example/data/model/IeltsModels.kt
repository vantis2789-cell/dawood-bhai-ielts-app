package com.example.data.model

enum class UserRole {
    STUDENT,
    TEACHER,
    ADMIN
}

enum class IeltsType(val displayName: String) {
    ACADEMIC("IELTS Academic"),
    GENERAL_TRAINING("IELTS General Training")
}

data class UserProfile(
    val id: String = "usr_01",
    val name: String = "Haris Mahmood",
    val email: String = "haris.ielts@academy.db",
    val role: UserRole = UserRole.STUDENT,
    val ieltsType: IeltsType = IeltsType.ACADEMIC,
    val currentBand: Double = 6.5,
    val targetBand: Double = 7.5,
    val examDateDaysLeft: Int = 42,
    val dailyGoalMinutes: Int = 60,
    val streakDays: Int = 7,
    val studyHoursTotal: Double = 24.5,
    val xp: Int = 1850,
    val level: Int = 4,
    val listeningBand: Double = 7.5,
    val readingBand: Double = 7.0,
    val writingBand: Double = 6.5,
    val speakingBand: Double = 6.5
)

enum class IeltsModuleType(val displayName: String, val iconName: String) {
    LISTENING("Listening", "Headphones"),
    READING("Reading", "MenuBook"),
    WRITING("Writing", "EditNote"),
    SPEAKING("Speaking", "Mic"),
    VOCABULARY("Vocabulary", "Style"),
    GRAMMAR("Grammar", "AutoStories")
}

enum class QuestionType {
    MULTIPLE_CHOICE,
    FORM_COMPLETION,
    TRUE_FALSE_NOT_GIVEN,
    MATCHING_HEADINGS,
    SUMMARY_COMPLETION,
    NOTE_COMPLETION
}

data class QuestionOption(
    val id: String,
    val label: String,
    val text: String
)

data class TestQuestion(
    val id: String,
    val questionNumber: Int,
    val questionType: QuestionType,
    val prompt: String,
    val options: List<QuestionOption> = emptyList(),
    val correctAnswer: String,
    val explanation: String,
    val studentAnswer: String? = null
)

data class TestSection(
    val sectionNumber: Int,
    val title: String,
    val instructions: String,
    val audioTitle: String? = null,
    val audioScript: String? = null,
    val passageTitle: String? = null,
    val passageText: String? = null,
    val questions: List<TestQuestion>
)

data class MockTest(
    val id: String,
    val title: String,
    val ieltsType: IeltsType,
    val difficulty: String, // "Standard", "Challenging", "Foundation"
    val durationMinutes: Int,
    val totalQuestions: Int,
    val moduleType: IeltsModuleType,
    val sections: List<TestSection>,
    val contentSource: String = "Dawood Bhai IELTS Studio Original Curriculum",
    val copyrightOwner: String = "Dawood Bhai IELTS Studio & Sahil VANTIS",
    val licenseStatus: String = "Authorized Academy License",
    val isCompleted: Boolean = false,
    val bestEstimatedBand: Double? = null
)

data class WritingPrompt(
    val id: String,
    val taskType: String, // "Task 1" or "Task 2"
    val title: String,
    val instructions: String,
    val prompt: String,
    val minWords: Int,
    val suggestedTimeMinutes: Int,
    val sampleHighBandEssay: String
)

data class WritingSubmission(
    val id: String,
    val studentName: String,
    val promptId: String,
    val taskType: String,
    val promptTitle: String,
    val essayText: String,
    val wordCount: Int,
    val submittedAt: String,
    val isEvaluated: Boolean,
    val taskResponseScore: Double? = null,
    val coherenceCohesionScore: Double? = null,
    val lexicalResourceScore: Double? = null,
    val grammarScore: Double? = null,
    val overallBand: Double? = null,
    val teacherFeedback: String? = null,
    val teacherStrengths: String? = null,
    val teacherWeaknesses: String? = null,
    val teacherCorrections: String? = null
)

data class SpeakingCueCard(
    val id: String,
    val part: Int, // 1, 2, or 3
    val title: String,
    val prompt: String,
    val bulletPoints: List<String>,
    val prepTimeSeconds: Int = 60,
    val speakTimeSeconds: Int = 120,
    val tips: String
)

data class SpeakingSubmission(
    val id: String,
    val studentName: String,
    val cueCardTitle: String,
    val part: Int,
    val durationSeconds: Int,
    val recordedDate: String,
    val isEvaluated: Boolean,
    val fluencyScore: Double? = null,
    val lexicalScore: Double? = null,
    val grammarScore: Double? = null,
    val pronunciationScore: Double? = null,
    val overallBand: Double? = null,
    val teacherFeedback: String? = null
)

data class VocabWord(
    val id: String,
    val word: String,
    val phonetic: String,
    val partOfSpeech: String,
    val definition: String,
    val sampleSentence: String,
    val synonyms: List<String>,
    val collocations: List<String>,
    val ieltsBand: Double,
    val topic: String,
    val isFavorite: Boolean = false,
    val isMastered: Boolean = false
)

data class GrammarQuestion(
    val id: String,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class GrammarTopic(
    val id: String,
    val title: String,
    val tag: String,
    val bandImpact: String,
    val explanation: String,
    val keyRules: List<String>,
    val examples: List<Pair<String, String>>, // Bad/Good or Example/Explanation
    val quiz: List<GrammarQuestion>
)

data class DailyMissionItem(
    val id: String,
    val title: String,
    val module: IeltsModuleType,
    val durationMinutes: Int,
    val isCompleted: Boolean
)

data class IntelligentRecommendation(
    val skill: String,
    val taskName: String,
    val focusArea: String,
    val estimatedMinutes: Int,
    val reason: String,
    val urgencyLevel: String // "High", "Medium", "Review"
)
