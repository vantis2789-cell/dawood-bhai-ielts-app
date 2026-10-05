package com.example.ui.screens.result

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.BandScoreCalculator
import com.example.data.model.IeltsModuleType
import com.example.data.repository.IeltsRepository
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.theme.*

@Composable
fun ResultScreen(
    customBand: Double? = null,
    onNavigateBack: () -> Unit,
    onStartRecommendedPractice: (IeltsModuleType) -> Unit
) {
    val repository = remember { IeltsRepository.getInstance() }
    val userProfile by repository.userProfile.collectAsState()

    val listeningBand = userProfile.listeningBand
    val readingBand = userProfile.readingBand
    val writingBand = userProfile.writingBand
    val speakingBand = userProfile.speakingBand

    val overallBand = customBand ?: BandScoreCalculator.calculateOverallBand(
        listeningBand,
        readingBand,
        writingBand,
        speakingBand
    )
    val bandGap = (userProfile.targetBand - overallBand).coerceAtLeast(0.0)

    val scaleAnim = remember { Animatable(0.75f) }
    LaunchedEffect(Unit) {
        scaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .padding(horizontal = 18.dp)
            .testTag("result_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("result_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "MOCK TEST PERFORMANCE REPORT",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )
                )
            }
        }

        // Hero Score Reveal Card
        item {
            FuturisticGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(scaleAnim.value),
                borderColor = NeonCyan.copy(alpha = 0.5f),
                testTag = "score_reveal_card"
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ESTIMATED PRACTICE BAND",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        NeonCyan.copy(alpha = 0.25f),
                                        SurfaceCard,
                                        BackgroundDark
                                    )
                                )
                            )
                            .border(3.dp, PrimaryGradient, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = String.format("%.1f", overallBand),
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = 54.sp,
                                fontWeight = FontWeight.Black,
                                color = NeonCyan
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Target Band", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                            Text(
                                text = String.format("%.1f", userProfile.targetBand),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricViolet
                                )
                            )
                        }

                        Box(modifier = Modifier.size(1.dp, 28.dp).background(SurfaceBorder))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Band Gap", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                            Text(
                                text = String.format("%.1f", bandGap),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (bandGap <= 0.0) NeonGreen else NeonAmber
                                )
                            )
                        }
                    }
                }
            }
        }

        // Four Module Breakdown Cards
        item {
            Text(
                text = "FOUR MODULE SCORE BREAKDOWN",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ScoreMiniCard("Listening", listeningBand, NeonCyan, Modifier.weight(1f))
                ScoreMiniCard("Reading", readingBand, ElectricViolet, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ScoreMiniCard("Writing", writingBand, NeonAmber, Modifier.weight(1f))
                ScoreMiniCard("Speaking", speakingBand, NeonGreen, Modifier.weight(1f))
            }
        }

        // Your Next Step / Weakness Analysis Card
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonAmber.copy(alpha = 0.5f),
                testTag = "weakness_analysis_card"
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = null,
                        tint = NeonAmber,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "DIAGNOSTIC WEAKNESS & NEXT STEP",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonAmber,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Writing Task 2 Cohesion Deficit",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Your writing rubric score currently stands at 6.5. Addressing coherence transitions and complex sentence structure in Task 2 will bridge your 0.5 gap toward Band 7.5.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        lineHeight = 22.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                FuturisticGlowButton(
                    text = "START RECOMMENDED PRACTICE",
                    onClick = { onStartRecommendedPractice(IeltsModuleType.WRITING) },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = NeonAmber,
                    contentColor = BackgroundDark,
                    testTag = "start_recommended_practice_btn"
                )
            }
        }

        // Question Review Section
        item {
            Text(
                text = "SAMPLE QUESTION REVIEW",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ReviewItem(
                    qNum = 1,
                    qType = "Form Completion",
                    userAns = "Highfield",
                    correctAns = "Highfield",
                    isCorrect = true,
                    explanation = "Alex confirmed address: '42 Highfield Crescent'."
                )
                ReviewItem(
                    qNum = 2,
                    qType = "Multiple Choice",
                    userAns = "Heated pool",
                    correctAns = "Heated pool",
                    isCorrect = true,
                    explanation = "Primary weekend sporting facility is heated pool."
                )
                ReviewItem(
                    qNum = 3,
                    qType = "True / False / Not Given",
                    userAns = "FALSE",
                    correctAns = "NOT GIVEN",
                    isCorrect = false,
                    explanation = "The passage does not state how many nations adopted deep basalt storage."
                )
            }
        }

        // Official Disclaimer
        item {
            Text(
                text = "Disclaimer: Band scores calculated in this application represent practice diagnostics calibrated against standard IELTS rubric criteria. Dawood Bhai IELTS Studio is an independent preparation academy and does not issue official certificates.",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextMuted,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            )
        }
    }
}

@Composable
private fun ScoreMiniCard(
    title: String,
    band: Double,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    FuturisticGlassCard(
        modifier = modifier,
        borderColor = accentColor.copy(alpha = 0.35f)
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                color = accentColor,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = String.format("%.1f", band),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Black,
                color = TextPrimary
            )
        )
    }
}

@Composable
private fun ReviewItem(
    qNum: Int,
    qType: String,
    userAns: String,
    correctAns: String,
    isCorrect: Boolean,
    explanation: String
) {
    FuturisticGlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (isCorrect) NeonGreen.copy(alpha = 0.4f) else NeonRed.copy(alpha = 0.4f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Question $qNum • $qType",
                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
            )
            Text(
                text = if (isCorrect) "CORRECT ✓" else "INCORRECT ✕",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if (isCorrect) NeonGreen else NeonRed,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = "Your Answer: $userAns", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary))
            if (!isCorrect) {
                Text(text = "Key: $correctAns", style = MaterialTheme.typography.bodySmall.copy(color = NeonGreen, fontWeight = FontWeight.Bold))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = explanation, style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
    }
}
