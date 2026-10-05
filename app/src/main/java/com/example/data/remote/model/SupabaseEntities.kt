package com.example.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data model representing the PostgreSQL/Supabase `profiles` table schema.
 * Supports Row-Level Security (RLS) and auth.users reference.
 */
@JsonClass(generateAdapter = true)
data class ProfileDto(
    @Json(name = "id")
    val id: String, // UUID referencing auth.users.id

    @Json(name = "full_name")
    val fullName: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "role")
    val role: String = "student", // "student", "teacher", "admin"

    @Json(name = "ielts_type")
    val ieltsType: String = "academic", // "academic", "general_training"

    @Json(name = "current_band")
    val currentBand: Double = 6.5,

    @Json(name = "target_band")
    val targetBand: Double = 7.5,

    @Json(name = "exam_date")
    val examDate: String? = null,

    @Json(name = "daily_goal_minutes")
    val dailyGoalMinutes: Int = 60,

    @Json(name = "streak_days")
    val streakDays: Int = 0,

    @Json(name = "study_hours_total")
    val studyHoursTotal: Double = 0.0,

    @Json(name = "xp")
    val xp: Int = 0,

    @Json(name = "level")
    val level: Int = 1,

    @Json(name = "created_at")
    val createdAt: String? = null,

    @Json(name = "updated_at")
    val updatedAt: String? = null
)

/**
 * Data model representing the PostgreSQL/Supabase `test_sets` table schema.
 * Reusable database-driven exam definition.
 */
@JsonClass(generateAdapter = true)
data class TestSetDto(
    @Json(name = "id")
    val id: String, // UUID primary key

    @Json(name = "title")
    val title: String,

    @Json(name = "ielts_type")
    val ieltsType: String, // "academic", "general_training"

    @Json(name = "difficulty")
    val difficulty: String = "Standard", // "Standard", "Challenging", "Foundation"

    @Json(name = "duration_minutes")
    val durationMinutes: Int = 160,

    @Json(name = "total_questions")
    val totalQuestions: Int = 40,

    @Json(name = "module_type")
    val moduleType: String = "full_mock", // "listening", "reading", "writing", "speaking", "full_mock"

    @Json(name = "content_source")
    val contentSource: String = "Dawood Bhai IELTS Studio Original Curriculum",

    @Json(name = "copyright_owner")
    val copyrightOwner: String = "Dawood Bhai IELTS Studio & Sahil VANTIS",

    @Json(name = "license_status")
    val licenseStatus: String = "Authorized Academy License",

    @Json(name = "is_active")
    val isActive: Boolean = true,

    @Json(name = "created_at")
    val createdAt: String? = null,

    @Json(name = "updated_at")
    val updatedAt: String? = null
)

/**
 * Data model representing `test_sections` child table linked to `test_sets.id`.
 */
@JsonClass(generateAdapter = true)
data class TestSectionDto(
    @Json(name = "id")
    val id: String,

    @Json(name = "test_set_id")
    val testSetId: String,

    @Json(name = "section_number")
    val sectionNumber: Int,

    @Json(name = "title")
    val title: String,

    @Json(name = "instructions")
    val instructions: String,

    @Json(name = "audio_url")
    val audioUrl: String? = null,

    @Json(name = "audio_script")
    val audioScript: String? = null,

    @Json(name = "passage_title")
    val passageTitle: String? = null,

    @Json(name = "passage_text")
    val passageText: String? = null
)

/**
 * Data model representing `questions` schema for database-driven IELTS tests.
 */
@JsonClass(generateAdapter = true)
data class QuestionDto(
    @Json(name = "id")
    val id: String,

    @Json(name = "section_id")
    val sectionId: String,

    @Json(name = "question_number")
    val questionNumber: Int,

    @Json(name = "question_type")
    val questionType: String,

    @Json(name = "prompt")
    val prompt: String,

    @Json(name = "options_json")
    val optionsJson: String? = null,

    @Json(name = "correct_answer")
    val correctAnswer: String,

    @Json(name = "explanation")
    val explanation: String
)
