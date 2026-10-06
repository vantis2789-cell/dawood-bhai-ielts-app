package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.*
import com.example.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest

@Database(
    entities = [
        UserEntity::class,
        BookEntity::class,
        TestEntity::class,
        SectionEntity::class,
        QuestionEntity::class,
        AttemptEntity::class,
        AttemptAnswerEntity::class,
        WritingSubmissionEntity::class,
        SpeakingSubmissionEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class IeltsDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun contentDao(): ContentDao
    abstract fun attemptDao(): AttemptDao
    abstract fun submissionDao(): SubmissionDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: IeltsDatabase? = null

        fun getDatabase(context: Context): IeltsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IeltsDatabase::class.java,
                    "ielts_studio_production.db"
                )
                    .addCallback(DatabasePrepopulationCallback())
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun hashPassword(password: String): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }
    }

    private class DatabasePrepopulationCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                INSTANCE?.let { database ->
                    seedInitialData(database)
                }
            }
        }

        private suspend fun seedInitialData(database: IeltsDatabase) {
            val userDao = database.userDao()
            val contentDao = database.contentDao()

            // 1. Seed Super Administrator User (sahilwaqar50@gmail.com)
            val superAdmin = UserEntity(
                id = "usr_admin_sahil",
                email = "sahilwaqar50@gmail.com",
                passwordHash = hashPassword("sahil2026"),
                fullName = "Sahil Waqar (Super Admin)",
                role = "ADMIN",
                ieltsType = "ACADEMIC",
                currentBand = 9.0,
                targetBand = 9.0,
                dailyGoalMinutes = 120,
                streakDays = 30,
                studyHoursTotal = 150.0,
                xp = 9999,
                level = 10,
                listeningBand = 9.0,
                readingBand = 9.0,
                writingBand = 9.0,
                speakingBand = 9.0
            )
            userDao.insertUser(superAdmin)

            // 2. Seed Books
            val book19 = BookEntity(
                id = "book_cambridge_19",
                title = "Cambridge IELTS 19 Academic",
                edition = "Official Examination Papers 2024",
                publisher = "Cambridge University Press & Assessment",
                coverUrl = null,
                testCount = 4,
                isActive = true
            )
            val book18 = BookEntity(
                id = "book_cambridge_18_gt",
                title = "Cambridge IELTS 18 General Training",
                edition = "Official Examination Papers 2023",
                publisher = "Cambridge University Press & Assessment",
                coverUrl = null,
                testCount = 4,
                isActive = true
            )
            contentDao.insertBook(book19)
            contentDao.insertBook(book18)

            // 4. Seed Tests
            val test1 = TestEntity(
                id = "test_ac_01",
                bookId = "book_cambridge_19",
                testNumber = 1,
                title = "Cambridge 19 Academic Test 1 (Full Mock)",
                ieltsType = "ACADEMIC",
                difficulty = "Standard Exam Standard",
                durationMinutes = 160,
                totalQuestions = 40,
                moduleType = "READING",
                contentSource = "Dawood Bhai IELTS Studio Authorized Curriculum",
                copyrightOwner = "Dawood Bhai IELTS Studio & Sahil VANTIS",
                licenseStatus = "Authorized Academy License",
                isPublished = true
            )
            val test2 = TestEntity(
                id = "test_ac_02",
                bookId = "book_cambridge_19",
                testNumber = 2,
                title = "Cambridge 19 Academic Test 2 (Science & Ecology)",
                ieltsType = "ACADEMIC",
                difficulty = "Challenging",
                durationMinutes = 160,
                totalQuestions = 40,
                moduleType = "READING",
                contentSource = "Dawood Bhai IELTS Studio Curriculum",
                copyrightOwner = "Dawood Bhai IELTS Studio & Sahil VANTIS",
                licenseStatus = "Authorized Academy License",
                isPublished = true
            )
            contentDao.insertTest(test1)
            contentDao.insertTest(test2)

            // 5. Seed Sections
            val sec1 = SectionEntity(
                id = "sec_ac_01_1",
                testId = "test_ac_01",
                moduleType = "READING",
                sectionNumber = 1,
                title = "Reading Passage 1: Subterranean Biomass and Carbon Sinks",
                instructions = "Do the following statements agree with the information given in Reading Passage 1? Write TRUE, FALSE, or NOT GIVEN.",
                passageTitle = "The Hidden Reservoirs of Terrestrial Carbon",
                passageText = "While atmospheric and oceanic carbon cycles receive dominant scientific scrutiny, subterranean ecosystems harbor approximately one-third of the planet's total living biomass. Deep underground microbial colonies, often termed the deep biosphere, subsist under extreme hydrostatic pressures and elevated thermal gradients.\n\nRecent genomic surveys conducted across deep borehole excavations reveal that these lithotrophic microorganisms do not rely on photosynthetic byproducts. Instead, they derive metabolic sustenance directly from the radiolytic decomposition of subterranean groundwater and geochemical reactions involving ferric basalt minerals. Consequently, geobiologists hypothesize that these subterranean biomes represent an exceptionally resilient biological repository, insulated from catastrophic surface climate perturbations.\n\nMoreover, the capacity of deep basaltic formations to chemically sequester injected carbon dioxide into permanent carbonate minerals within mere months presents a promising avenue for industrialized carbon capture initiatives. Pilot installations in volcanic basalt zones have demonstrated an efficiency rate exceeding 95% within two years of initial subterranean injection."
            )
            val sec2 = SectionEntity(
                id = "sec_ac_01_2",
                testId = "test_ac_01",
                moduleType = "LISTENING",
                sectionNumber = 2,
                title = "Listening Section 1: Community Leisure Club Registration",
                instructions = "Complete the form below. Write ONE WORD AND/OR A NUMBER for each answer.",
                audioUrl = "https://actions.google.com/sounds/v1/ambiences/coffee_shop.ogg",
                audioScript = "Receptionist: Good morning! Welcome to Northfield Leisure Pavilion. How can I help you today?\nApplicant: Hi there. I'd like to enroll in your badminton and squash club membership.\nReceptionist: Excellent. Let me capture your registration particulars. What is your full legal name?\nApplicant: Alex Henderson.\nReceptionist: And your current residential address?\nApplicant: 42 Highfield Crescent, Northfield.\nReceptionist: Perfect. Which membership tier are you looking to join?\nApplicant: The Gold Tier membership."
            )
            contentDao.insertSection(sec1)
            contentDao.insertSection(sec2)

            // 6. Seed Questions
            val q1 = QuestionEntity(
                id = "qr1_1",
                testId = "test_ac_01",
                sectionId = "sec_ac_01_1",
                questionNumber = 1,
                questionType = "TRUE_FALSE_NOT_GIVEN",
                prompt = "Subterranean ecosystems contain roughly one-third of all living biomass on Earth.",
                optionsJson = """[{"id":"t","label":"TRUE","text":"TRUE"},{"id":"f","label":"FALSE","text":"FALSE"},{"id":"ng","label":"NOT GIVEN","text":"NOT GIVEN"}]""",
                correctAnswer = "TRUE",
                explanation = "The passage confirms: 'subterranean ecosystems harbor approximately one-third of the planet's total living biomass.'",
                difficulty = "Standard",
                orderIndex = 1
            )
            val q2 = QuestionEntity(
                id = "qr1_2",
                testId = "test_ac_01",
                sectionId = "sec_ac_01_1",
                questionNumber = 2,
                questionType = "TRUE_FALSE_NOT_GIVEN",
                prompt = "Microbial life in the deep biosphere relies primarily on solar photosynthesis for energy.",
                optionsJson = """[{"id":"t","label":"TRUE","text":"TRUE"},{"id":"f","label":"FALSE","text":"FALSE"},{"id":"ng","label":"NOT GIVEN","text":"NOT GIVEN"}]""",
                correctAnswer = "FALSE",
                explanation = "The text says they 'do not rely on photosynthetic byproducts. Instead, they derive metabolic sustenance directly from the radiolytic decomposition...'",
                difficulty = "Standard",
                orderIndex = 2
            )
            val q3 = QuestionEntity(
                id = "qr1_3",
                testId = "test_ac_01",
                sectionId = "sec_ac_01_1",
                questionNumber = 3,
                questionType = "TRUE_FALSE_NOT_GIVEN",
                prompt = "Deep basalt carbon capture has been adopted in more than fifty nations worldwide.",
                optionsJson = """[{"id":"t","label":"TRUE","text":"TRUE"},{"id":"f","label":"FALSE","text":"FALSE"},{"id":"ng","label":"NOT GIVEN","text":"NOT GIVEN"}]""",
                correctAnswer = "NOT GIVEN",
                explanation = "The passage mentions 95% efficiency in pilot installations, but does not disclose the total number of nations adopting it.",
                difficulty = "Standard",
                orderIndex = 3
            )
            val q4 = QuestionEntity(
                id = "ql1_1",
                testId = "test_ac_01",
                sectionId = "sec_ac_01_2",
                questionNumber = 4,
                questionType = "FORM_COMPLETION",
                prompt = "Address: 42 _______ Crescent",
                optionsJson = """[{"id":"a","label":"A","text":"Highfield"},{"id":"b","label":"B","text":"Highland"},{"id":"c","label":"C","text":"Hartsfield"}]""",
                correctAnswer = "Highfield",
                explanation = "Applicant explicitly states: '42 Highfield Crescent, Northfield.'",
                difficulty = "Standard",
                orderIndex = 4
            )
            contentDao.insertQuestions(listOf(q1, q2, q3, q4))
        }
    }
}
