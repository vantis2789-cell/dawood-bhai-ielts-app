package com.example.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.IeltsRepository
import com.example.ui.components.EmptyState
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TestHistoryScreen(
    onNavigateBack: () -> Unit,
    onOpenAttemptReview: (String) -> Unit,
    onStartNewTest: () -> Unit
) {
    val repository = remember { IeltsRepository.getInstance() }
    val completedAttempts by repository.completedAttempts.collectAsState()

    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .padding(horizontal = 18.dp)
            .testTag("test_history_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("history_back_btn")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "EXAM ATTEMPT HISTORY",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )
                )
            }
        }

        if (completedAttempts.isEmpty()) {
            item {
                EmptyState(
                    title = "No Tests Completed Yet",
                    description = "Take your first timed mock exam or module drill. All answers, timings, and band diagnostics will be preserved in your database profile.",
                    actionButton = {
                        PrimaryButton(
                            text = "START A MOCK TEST",
                            onClick = onStartNewTest
                        )
                    }
                )
            }
        } else {
            item {
                Text(
                    text = "${completedAttempts.size} COMPLETED EXAM SESSIONS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
            }

            items(completedAttempts) { attempt ->
                FuturisticGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenAttemptReview(attempt.id) },
                    borderColor = NeonCyan.copy(alpha = 0.35f),
                    testTag = "attempt_card_${attempt.id}"
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = attempt.testTitle,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            val dateStr = if (attempt.completedAt != null) {
                                dateFormat.format(Date(attempt.completedAt))
                            } else {
                                "Recent Attempt"
                            }
                            Text(
                                text = "$dateStr • ${attempt.timeTakenSeconds / 60} mins",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = String.format("Band %.1f", attempt.bandScore),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = NeonGreen
                                )
                            )
                            Text(
                                text = "${attempt.rawScore} / ${attempt.totalQuestions} Correct",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Review Question Answers",
                            style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
