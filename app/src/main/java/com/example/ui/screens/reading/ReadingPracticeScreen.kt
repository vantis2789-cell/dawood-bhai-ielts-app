package com.example.ui.screens.reading

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.data.model.IeltsType
import com.example.data.repository.IeltsRepository
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.screens.listening.QuestionItemCard
import com.example.ui.theme.*

@Composable
fun ReadingPracticeScreen(
    onNavigateBack: () -> Unit,
    onNavigateToResult: (Double) -> Unit
) {
    val repository = remember { IeltsRepository.getInstance() }
    val mockTests by repository.mockTests.collectAsState()
    val readingTest = mockTests.getOrNull(1) ?: mockTests.first()
    val section = readingTest.sections.first()

    var activeTab by remember { mutableIntStateOf(0) } // 0 = Passage, 1 = Questions
    val userAnswers = remember { mutableStateMapOf<String, String>() }
    var isSubmitted by remember { mutableStateOf(false) }

    val correctCount = section.questions.count { q ->
        userAnswers[q.id]?.trim()?.equals(q.correctAnswer.trim(), ignoreCase = true) == true
    }
    val estimatedBand = BandScoreCalculator.calculateReadingBand(correctCount * 13, IeltsType.ACADEMIC)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .testTag("reading_practice_screen")
    ) {
        // Top Bar with Timer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.testTag("reading_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "IELTS ACADEMIC READING",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ElectricViolet,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Passage 1 • 20 Mins",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
            }

            // Timer Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceElevated)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "18:45",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Tab Selector (Passage vs Questions)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (activeTab == 0) ElectricViolet else SurfaceCard)
                    .clickable { activeTab = 0 }
                    .testTag("tab_passage"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Reading Passage",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (activeTab == 0) Color.White else TextSecondary
                    )
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (activeTab == 1) ElectricViolet else SurfaceCard)
                    .clickable { activeTab = 1 }
                    .testTag("tab_questions"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Questions (${section.questions.size})",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (activeTab == 1) Color.White else TextSecondary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Content
        if (activeTab == 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp)
                    .testTag("reading_passage_view"),
                contentPadding = PaddingValues(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(
                        text = section.passageTitle ?: "Academic Passage",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Source: Scientific Journal of Geochemical Geobiology • Dawood Bhai IELTS Studio Content Engine",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                    )
                }

                item {
                    FuturisticGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = ElectricViolet.copy(alpha = 0.3f)
                    ) {
                        Text(
                            text = section.passageText ?: "",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextPrimary,
                                lineHeight = 26.sp
                            )
                        )
                    }
                }

                item {
                    FuturisticGlowButton(
                        text = "PROCEED TO QUESTIONS →",
                        onClick = { activeTab = 1 },
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "reading_goto_questions_btn"
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp)
                    .testTag("reading_questions_view"),
                contentPadding = PaddingValues(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Instructions: Questions 1–3",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = section.instructions,
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                }

                items(section.questions) { q ->
                    QuestionItemCard(
                        question = q,
                        selectedAnswer = userAnswers[q.id],
                        isSubmitted = isSubmitted,
                        onSelectAnswer = { ans ->
                            if (!isSubmitted) userAnswers[q.id] = ans
                        }
                    )
                }

                item {
                    if (!isSubmitted) {
                        FuturisticGlowButton(
                            text = "CHECK READING SCORES",
                            onClick = { isSubmitted = true },
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "submit_reading_btn"
                        )
                    } else {
                        FuturisticGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            borderColor = NeonGreen.copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = "READING ESTIMATED BAND: ${String.format("%.1f", estimatedBand)}",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Correct: $correctCount / ${section.questions.size}",
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            FuturisticGlowButton(
                                text = "VIEW FULL ANALYSIS",
                                onClick = { onNavigateToResult(estimatedBand) },
                                containerColor = NeonGreen,
                                testTag = "reading_view_analysis_btn"
                            )
                        }
                    }
                }
            }
        }
    }
}
