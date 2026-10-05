package com.example.ui.screens.listening

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.BandScoreCalculator
import com.example.data.model.TestQuestion
import com.example.data.repository.IeltsRepository
import com.example.ui.components.FuturisticAudioPlayerWidget
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.theme.*

@Composable
fun ListeningPracticeScreen(
    onNavigateBack: () -> Unit,
    onNavigateToResult: (Double) -> Unit
) {
    val repository = remember { IeltsRepository.getInstance() }
    val mockTests by repository.mockTests.collectAsState()
    val listeningTest = mockTests.firstOrNull() ?: return

    var currentSectionIndex by remember { mutableIntStateOf(0) }
    val currentSection = listeningTest.sections.getOrNull(currentSectionIndex) ?: listeningTest.sections[0]

    val userAnswers = remember { mutableStateMapOf<String, String>() }
    var isSubmitted by remember { mutableStateOf(false) }
    var showTranscript by remember { mutableStateOf(false) }

    val allQuestions = listeningTest.sections.flatMap { it.questions }
    val correctCount = allQuestions.count { q ->
        userAnswers[q.id]?.trim()?.equals(q.correctAnswer.trim(), ignoreCase = true) == true
    }
    val estimatedBand = BandScoreCalculator.calculateListeningBand(correctCount * 10) // Scale to 40 questions equivalent

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .padding(horizontal = 18.dp)
            .testTag("listening_practice_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Navigation Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("listening_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Text(
                    text = "IELTS LISTENING PRACTICE",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )
                )

                // Transcript toggle
                IconButton(
                    onClick = { showTranscript = !showTranscript },
                    modifier = Modifier.testTag("transcript_toggle_btn")
                ) {
                    Icon(
                        imageVector = if (showTranscript) Icons.Default.Description else Icons.AutoMirrored.Filled.Article,
                        contentDescription = "Toggle Transcript",
                        tint = if (showTranscript) NeonCyan else TextSecondary
                    )
                }
            }
        }

        // Section Tabs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listeningTest.sections.forEachIndexed { index, section ->
                    val isSelected = currentSectionIndex == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) NeonCyan else SurfaceCard)
                            .border(1.dp, if (isSelected) NeonCyan else SurfaceBorder, RoundedCornerShape(10.dp))
                            .clickable { currentSectionIndex = index }
                            .testTag("section_tab_$index"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Section ${section.sectionNumber}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) BackgroundDark else TextSecondary
                            )
                        )
                    }
                }
            }
        }

        // Futuristic Audio Player Widget
        item {
            FuturisticAudioPlayerWidget(
                title = currentSection.audioTitle ?: "IELTS Section Audio Track",
                durationSeconds = 150
            )
        }

        // Audio Transcript (Optional Drawer/Card)
        if (showTranscript && currentSection.audioScript != null) {
            item {
                FuturisticGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = ElectricViolet.copy(alpha = 0.4f),
                    testTag = "audio_transcript_card"
                ) {
                    Text(
                        text = "AUDIO TRANSCRIPT / SCRIPT",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ElectricViolet,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentSection.audioScript ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextPrimary,
                            lineHeight = 22.sp
                        )
                    )
                }
            }
        }

        // Section Header & Instructions
        item {
            Column {
                Text(
                    text = currentSection.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currentSection.instructions,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                )
            }
        }

        // Questions List
        items(currentSection.questions) { question ->
            QuestionItemCard(
                question = question,
                selectedAnswer = userAnswers[question.id],
                isSubmitted = isSubmitted,
                onSelectAnswer = { ans ->
                    if (!isSubmitted) {
                        userAnswers[question.id] = ans
                    }
                }
            )
        }

        // Submit & Score Panel
        item {
            Spacer(modifier = Modifier.height(10.dp))
            if (!isSubmitted) {
                FuturisticGlowButton(
                    text = "CHECK ANSWERS & ESTIMATE BAND",
                    onClick = { isSubmitted = true },
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "submit_listening_btn"
                )
            } else {
                FuturisticGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonGreen.copy(alpha = 0.5f),
                    testTag = "listening_result_summary_card"
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "PRACTICE RESULT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = NeonGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Band ${String.format("%.1f", estimatedBand)}",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = NeonCyan
                                )
                            )
                            Text(
                                text = "Score: $correctCount / ${allQuestions.size} correct in sample",
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                            )
                        }

                        FuturisticGlowButton(
                            text = "FULL ANALYSIS",
                            onClick = { onNavigateToResult(estimatedBand) },
                            containerColor = NeonGreen,
                            contentColor = BackgroundDark,
                            testTag = "view_full_analysis_btn"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionItemCard(
    question: TestQuestion,
    selectedAnswer: String?,
    isSubmitted: Boolean,
    onSelectAnswer: (String) -> Unit
) {
    val isCorrect = selectedAnswer?.trim()?.equals(question.correctAnswer.trim(), ignoreCase = true) == true

    FuturisticGlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (isSubmitted) {
            if (isCorrect) NeonGreen.copy(alpha = 0.6f) else NeonRed.copy(alpha = 0.6f)
        } else SurfaceBorder,
        testTag = "question_card_${question.questionNumber}"
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "QUESTION ${question.questionNumber}",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )

            if (isSubmitted) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = null,
                        tint = if (isCorrect) NeonGreen else NeonRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isCorrect) "Correct" else "Incorrect",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isCorrect) NeonGreen else NeonRed,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = question.prompt,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                lineHeight = 22.sp
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Options
        question.options.forEach { opt ->
            val isOptionSelected = selectedAnswer == opt.text
            val isThisCorrect = opt.text == question.correctAnswer

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSubmitted && isThisCorrect) NeonGreen.copy(alpha = 0.15f)
                        else if (isOptionSelected) NeonCyan.copy(alpha = 0.12f)
                        else SurfaceElevated
                    )
                    .clickable { onSelectAnswer(opt.text) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isOptionSelected,
                    onClick = { onSelectAnswer(opt.text) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = NeonCyan,
                        unselectedColor = Color(0xFF64748B)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${opt.label}. ${opt.text}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (isSubmitted && isThisCorrect) NeonGreen
                        else if (isOptionSelected) NeonCyan
                        else TextPrimary,
                        fontWeight = if (isOptionSelected || (isSubmitted && isThisCorrect)) FontWeight.Bold else FontWeight.Normal
                    )
                )
            }
        }

        // Explanation on submit
        if (isSubmitted) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceDark)
                    .padding(10.dp)
            ) {
                Column {
                    Text(
                        text = "Correct Answer: ${question.correctAnswer}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonGreen,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = question.explanation,
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }
        }
    }
}
