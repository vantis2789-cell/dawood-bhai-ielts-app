package com.example.ui.screens.teacher

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
import com.example.data.model.WritingSubmission
import com.example.data.repository.IeltsRepository
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.theme.*

@Composable
fun TeacherDashboardScreen(
    onNavigateBack: () -> Unit
) {
    val repository = remember { IeltsRepository.getInstance() }
    val writingSubmissions by repository.writingSubmissions.collectAsState()
    val speakingSubmissions by repository.speakingSubmissions.collectAsState()

    var selectedSubmission by remember { mutableStateOf<WritingSubmission?>(null) }
    var trScore by remember { mutableDoubleStateOf(7.0) }
    var ccScore by remember { mutableDoubleStateOf(6.5) }
    var lrScore by remember { mutableDoubleStateOf(7.5) }
    var graScore by remember { mutableDoubleStateOf(6.5) }

    var feedbackText by remember { mutableStateOf("Cohesive and clear overall structure; tighten transitional clauses.") }
    var strengthsText by remember { mutableStateOf("Sophisticated academic lexical density and sustained thesis.") }
    var weaknessesText by remember { mutableStateOf("Punctuation slips in complex conditional sentences.") }
    var correctionsText by remember { mutableStateOf("Use 'mitigates disparities' instead of 'bridges the achievement divide'.") }

    val calculatedBand = BandScoreCalculator.calculateRubricBand(trScore, ccScore, lrScore, graScore)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .padding(horizontal = 18.dp)
            .testTag("teacher_dashboard_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("teacher_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Text(
                    text = "TEACHER EVALUATION HUB",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ElectricViolet,
                        letterSpacing = 1.sp
                    )
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceElevated)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Instructor Mode",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ElectricViolet,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Active Submission Evaluation Modal/Section
        if (selectedSubmission != null) {
            val sub = selectedSubmission!!
            item {
                FuturisticGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonCyan,
                    testTag = "evaluation_editor_card"
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "GRADING: ${sub.studentName}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan
                            )
                        )
                        IconButton(onClick = { selectedSubmission = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                        }
                    }

                    Text(
                        text = "${sub.taskType} • ${sub.promptTitle} (${sub.wordCount} Words)",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 140.dp)
                            .background(SurfaceDark, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = sub.essayText,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary),
                            maxLines = 6
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "ASSIGN IELTS 4-CRITERIA RUBRICS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    RubricSliderRow("Task Response / Achievement", trScore) { trScore = it }
                    RubricSliderRow("Coherence & Cohesion", ccScore) { ccScore = it }
                    RubricSliderRow("Lexical Resource", lrScore) { lrScore = it }
                    RubricSliderRow("Grammatical Range & Accuracy", graScore) { graScore = it }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Calculated Overall Band:", style = MaterialTheme.typography.titleSmall.copy(color = TextSecondary))
                        Text(
                            text = "Band ${String.format("%.1f", calculatedBand)}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = NeonGreen
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = feedbackText,
                        onValueChange = { feedbackText = it },
                        label = { Text("Examiner Feedback", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = strengthsText,
                        onValueChange = { strengthsText = it },
                        label = { Text("Strengths", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = weaknessesText,
                        onValueChange = { weaknessesText = it },
                        label = { Text("Weaknesses to Address", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FuturisticGlowButton(
                        text = "SAVE & DISPATCH EVALUATION",
                        onClick = {
                            repository.evaluateWritingSubmission(
                                submissionId = sub.id,
                                tr = trScore,
                                cc = ccScore,
                                lr = lrScore,
                                gra = graScore,
                                feedback = feedbackText,
                                strengths = strengthsText,
                                weaknesses = weaknessesText,
                                corrections = correctionsText
                            )
                            selectedSubmission = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = NeonGreen,
                        contentColor = BackgroundDark,
                        testTag = "dispatch_evaluation_btn"
                    )
                }
            }
        }

        // Submissions Queue
        item {
            Text(
                text = "PENDING & RECENT STUDENT WRITING SUBMISSIONS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
        }

        items(writingSubmissions) { sub ->
            FuturisticGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedSubmission = sub },
                borderColor = if (sub.isEvaluated) NeonGreen.copy(alpha = 0.4f) else NeonAmber.copy(alpha = 0.5f),
                testTag = "submission_item_${sub.id}"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = sub.studentName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (sub.isEvaluated) NeonGreen.copy(alpha = 0.15f) else NeonAmber.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (sub.isEvaluated) "Band ${String.format("%.1f", sub.overallBand)}" else "Needs Review",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (sub.isEvaluated) NeonGreen else NeonAmber,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${sub.taskType}: ${sub.promptTitle}",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                        Text(
                            text = "Submitted: ${sub.submittedAt} • ${sub.wordCount} words",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                        )
                    }

                    Button(
                        onClick = { selectedSubmission = sub },
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevated),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (sub.isEvaluated) "Edit Grade" else "Grade Now", color = NeonCyan)
                    }
                }
            }
        }

        // Speaking Submissions
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "STUDENT SPEAKING RECORDINGS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
        }

        items(speakingSubmissions) { spk ->
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonGreen.copy(alpha = 0.35f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = spk.studentName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Part ${spk.part}: ${spk.cueCardTitle}",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                        Text(
                            text = "Duration: ${spk.durationSeconds}s • ${spk.recordedDate}",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonGreen.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Band ${String.format("%.1f", spk.overallBand)}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonGreen,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RubricSliderRow(
    title: String,
    currentScore: Double,
    onScoreChange: (Double) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
            Text(
                text = String.format("%.1f", currentScore),
                style = MaterialTheme.typography.titleSmall.copy(color = NeonCyan, fontWeight = FontWeight.Bold)
            )
        }
        Slider(
            value = currentScore.toFloat(),
            onValueChange = {
                val rounded = (Math.round(it * 2) / 2.0).coerceIn(4.0, 9.0)
                onScoreChange(rounded)
            },
            valueRange = 4f..9f,
            steps = 9,
            colors = SliderDefaults.colors(
                thumbColor = NeonCyan,
                activeTrackColor = NeonCyan,
                inactiveTrackColor = Color(0xFF1E293B)
            )
        )
    }
}
