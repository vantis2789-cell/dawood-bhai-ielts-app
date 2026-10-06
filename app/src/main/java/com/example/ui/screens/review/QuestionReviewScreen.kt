package com.example.ui.screens.review

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.model.TestAttempt
import com.example.data.repository.IeltsRepository
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticScoreRing
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.*

@Composable
fun QuestionReviewScreen(
    attemptId: String?,
    onNavigateBack: () -> Unit
) {
    val repository = remember { IeltsRepository.getInstance() }
    val completedAttempts by repository.completedAttempts.collectAsState()

    val attempt: TestAttempt? = remember(attemptId, completedAttempts) {
        if (attemptId != null) {
            completedAttempts.find { it.id == attemptId } ?: completedAttempts.firstOrNull()
        } else {
            completedAttempts.firstOrNull()
        }
    }

    if (attempt == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark)
                .statusBarsPadding()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "No Attempt Recorded Yet",
                    style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Complete a practice module or full mock test to generate an analytical question review.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
                Spacer(modifier = Modifier.height(16.dp))
                PrimaryButton(text = "Return to Dashboard", onClick = onNavigateBack)
            }
        }
        return
    }

    val answers = attempt.answers
    val total = if (answers.isNotEmpty()) answers.size else attempt.totalQuestions
    val correctCount = answers.count { it.isCorrect }
    val unansweredCount = answers.count { it.userAnswer.isNullOrBlank() }
    val incorrectCount = total - correctCount - unansweredCount

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .padding(horizontal = 18.dp)
            .testTag("question_review_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("review_back_btn")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "EXAM QUESTION REVIEW",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )
                )
            }
        }

        // Summary Card with Score & Ring
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonCyan.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = attempt.testTitle,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Time Spent: ${attempt.timeTakenSeconds / 60}m ${attempt.timeTakenSeconds % 60}s",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "OFFICIAL BAND ESTIMATE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = String.format("Band %.1f", attempt.bandScore),
                            style = MaterialTheme.typography.displaySmall.copy(
                                color = NeonGreen,
                                fontWeight = FontWeight.Black
                            )
                        )
                    }

                    FuturisticScoreRing(
                        currentScore = attempt.bandScore,
                        targetScore = 9.0,
                        ringSize = 90.dp,
                        strokeWidth = 7.dp
                    )
                }
            }
        }

        // Analytical Metrics Pill Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReviewMetricPill(
                    label = "Correct",
                    value = "$correctCount",
                    color = NeonGreen,
                    modifier = Modifier.weight(1f)
                )
                ReviewMetricPill(
                    label = "Incorrect",
                    value = "$incorrectCount",
                    color = NeonRed,
                    modifier = Modifier.weight(1f)
                )
                ReviewMetricPill(
                    label = "Unanswered",
                    value = "$unansweredCount",
                    color = NeonAmber,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Questions Section Title
        item {
            Text(
                text = "DETAILED ITEM-BY-ITEM DIAGNOSTIC",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
        }

        // Each question from the real attempt
        items(answers) { ans ->
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = if (ans.isCorrect) NeonGreen.copy(alpha = 0.5f) else NeonRed.copy(alpha = 0.5f),
                testTag = "review_item_${ans.questionId}"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Question ${ans.questionNumber}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (ans.isCorrect) NeonGreen.copy(alpha = 0.2f) else NeonRed.copy(alpha = 0.2f))
                            .border(1.dp, if (ans.isCorrect) NeonGreen else NeonRed, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (ans.isCorrect) "CORRECT (+1)" else if (ans.userAnswer.isNullOrBlank()) "UNANSWERED (0)" else "INCORRECT (0)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (ans.isCorrect) NeonGreen else NeonRed,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = ans.prompt,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // User's answer vs Correct Answer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Candidate Answer:", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                        Text(
                            text = ans.userAnswer ?: "(Unanswered)",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (ans.isCorrect) NeonGreen else NeonRed,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Correct Official Key:", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                        Text(
                            text = ans.correctAnswer,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Explanation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceElevated)
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "ACADEMY EXPLANATION:",
                            style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = ans.explanation,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, lineHeight = 18.sp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewMetricPill(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleLarge.copy(color = color, fontWeight = FontWeight.Black))
            Text(label, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
        }
    }
}
