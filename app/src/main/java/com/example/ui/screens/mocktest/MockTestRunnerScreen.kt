package com.example.ui.screens.mocktest

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.example.data.model.MockTest
import com.example.data.model.TestQuestion
import com.example.data.repository.IeltsRepository
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.screens.listening.QuestionItemCard
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun MockTestRunnerScreen(
    testId: String,
    onNavigateBack: () -> Unit,
    onTestSubmitted: (Double) -> Unit
) {
    val repository = remember { IeltsRepository.getInstance() }
    val mockTests by repository.mockTests.collectAsState()
    val activeTest = mockTests.find { it.id == testId } ?: mockTests.first()

    val attemptId = remember(testId) {
        repository.startAttempt(testId, activeTest.moduleType)
    }

    val allQuestions = remember(activeTest) {
        activeTest.sections.flatMap { it.questions }
    }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val currentQuestion = allQuestions.getOrNull(currentQuestionIndex) ?: allQuestions.first()

    val userAnswers = remember { mutableStateMapOf<String, String>() }
    val markedForReview = remember { mutableStateMapOf<String, Boolean>() }
    var secondsRemaining by remember { mutableIntStateOf(activeTest.durationMinutes * 60) }
    var isSubmitting by remember { mutableStateOf(false) }
    var showConfirmSubmitDialog by remember { mutableStateOf(false) }

    // Live Test Timer
    LaunchedEffect(Unit) {
        while (secondsRemaining > 0 && !isSubmitting) {
            delay(1000)
            secondsRemaining--
        }
        if (secondsRemaining == 0 && !isSubmitting) {
            isSubmitting = true
            val timeTaken = (activeTest.durationMinutes * 60) - secondsRemaining
            val band = repository.submitAttempt(attemptId, timeTaken)
            onTestSubmitted(band)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .testTag("mock_test_runner_screen")
    ) {
        // Sticky Exam Header with Timer & Submit Action
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = SurfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { showConfirmSubmitDialog = true },
                    modifier = Modifier.testTag("runner_exit_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Exit Test",
                        tint = TextPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = activeTest.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = "Question ${currentQuestionIndex + 1} of ${allQuestions.size}",
                        style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan)
                    )
                }

                // Countdown Timer Pill
                val hours = secondsRemaining / 3600
                val mins = (secondsRemaining % 3600) / 60
                val secs = secondsRemaining % 60
                val timeString = if (hours > 0) {
                    String.format("%02d:%02d:%02d", hours, mins, secs)
                } else {
                    String.format("%02d:%02d", mins, secs)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (secondsRemaining < 300) NeonRed.copy(alpha = 0.2f) else SurfaceElevated)
                        .border(1.dp, if (secondsRemaining < 300) NeonRed else NeonCyan, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = if (secondsRemaining < 300) NeonRed else NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = timeString,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (secondsRemaining < 300) NeonRed else NeonCyan
                            )
                        )
                    }
                }
            }
        }

        // Horizontal Question Matrix Navigator Bar
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceDark)
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(allQuestions) { idx, q ->
                val isAnswered = userAnswers[q.id] != null
                val isCurrent = idx == currentQuestionIndex

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (isCurrent) NeonCyan
                            else if (isAnswered) ElectricViolet.copy(alpha = 0.35f)
                            else SurfaceElevated
                        )
                        .border(
                            1.dp,
                            if (isCurrent) NeonCyan
                            else if (isAnswered) ElectricViolet
                            else SurfaceBorder,
                            CircleShape
                        )
                        .clickable { currentQuestionIndex = idx }
                        .testTag("matrix_btn_$idx"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${idx + 1}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrent) BackgroundDark else if (isAnswered) Color.White else TextSecondary
                        )
                    )
                }
            }
        }

        // Active Question View
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 18.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                val currentSection = activeTest.sections.find { sec ->
                    sec.questions.any { it.id == currentQuestion.id }
                } ?: activeTest.sections.first()

                Text(
                    text = currentSection.title.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currentSection.instructions,
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                )

                if (currentSection.passageText != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    FuturisticGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = ElectricViolet.copy(alpha = 0.35f)
                    ) {
                        Text(
                            text = currentSection.passageTitle ?: "Reading Passage Excerpt",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentSection.passageText ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary,
                                lineHeight = 20.sp
                            ),
                            maxLines = 8
                        )
                    }
                }
            }

            item {
                QuestionItemCard(
                    question = currentQuestion,
                    selectedAnswer = userAnswers[currentQuestion.id],
                    isSubmitted = false,
                    onSelectAnswer = { ans ->
                        userAnswers[currentQuestion.id] = ans
                        repository.saveAnswer(
                            attemptId = attemptId,
                            questionId = currentQuestion.id,
                            questionNumber = currentQuestion.questionNumber,
                            prompt = currentQuestion.prompt,
                            userAnswer = ans,
                            correctAnswer = currentQuestion.correctAnswer,
                            explanation = currentQuestion.explanation,
                            isMarkedForReview = markedForReview[currentQuestion.id] ?: false
                        )
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    val isMarked = markedForReview[currentQuestion.id] ?: false
                    OutlinedButton(
                        onClick = {
                            val newMarked = !isMarked
                            markedForReview[currentQuestion.id] = newMarked
                            if (userAnswers[currentQuestion.id] != null) {
                                repository.saveAnswer(
                                    attemptId = attemptId,
                                    questionId = currentQuestion.id,
                                    questionNumber = currentQuestion.questionNumber,
                                    prompt = currentQuestion.prompt,
                                    userAnswer = userAnswers[currentQuestion.id],
                                    correctAnswer = currentQuestion.correctAnswer,
                                    explanation = currentQuestion.explanation,
                                    isMarkedForReview = newMarked
                                )
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isMarked) NeonAmber else TextSecondary
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isMarked) NeonAmber else SurfaceBorder
                        )
                    ) {
                        Icon(
                            imageVector = if (isMarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isMarked) "Marked for Review" else "Mark for Review", fontSize = 12.sp)
                    }
                }
            }
        }

        // Bottom Previous / Next / Submit Controls
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            color = SurfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        if (currentQuestionIndex > 0) currentQuestionIndex--
                    },
                    enabled = currentQuestionIndex > 0,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("runner_prev_btn")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Previous")
                }

                if (currentQuestionIndex < allQuestions.size - 1) {
                    FuturisticGlowButton(
                        text = "NEXT QUESTION",
                        onClick = { currentQuestionIndex++ },
                        icon = {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = BackgroundDark, modifier = Modifier.size(16.dp))
                        },
                        testTag = "runner_next_btn"
                    )
                } else {
                    FuturisticGlowButton(
                        text = "FINISH & SUBMIT TEST",
                        onClick = { showConfirmSubmitDialog = true },
                        containerColor = NeonGreen,
                        contentColor = BackgroundDark,
                        testTag = "runner_finish_btn"
                    )
                }
            }
        }
    }

    if (showConfirmSubmitDialog) {
        val answeredCount = userAnswers.size
        AlertDialog(
            onDismissRequest = { showConfirmSubmitDialog = false },
            title = { Text("Submit Mock Test?", color = TextPrimary) },
            text = {
                Text(
                    "You have answered $answeredCount of ${allQuestions.size} questions. Once submitted, your answers will be evaluated according to official IELTS band score tables.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmSubmitDialog = false
                        isSubmitting = true
                        val timeTaken = (activeTest.durationMinutes * 60) - secondsRemaining
                        val band = repository.submitAttempt(attemptId, timeTaken)
                        onTestSubmitted(band)
                    }
                ) {
                    Text("Submit Now", color = NeonCyan, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmSubmitDialog = false }) {
                    Text("Continue Test", color = TextSecondary)
                }
            },
            containerColor = SurfaceElevated
        )
    }
}
