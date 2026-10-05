package com.example.ui.screens.speaking

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.repository.IeltsRepository
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.theme.*
import kotlinx.coroutines.delay

enum class SpeakingState {
    IDLE,
    PREPARING,
    RECORDING,
    COMPLETED
}

@Composable
fun SpeakingPracticeScreen(
    onNavigateBack: () -> Unit
) {
    val repository = remember { IeltsRepository.getInstance() }
    val cueCard = repository.currentSpeakingCueCard

    var speakingState by remember { mutableStateOf(SpeakingState.IDLE) }
    var prepSecondsLeft by remember { mutableIntStateOf(60) }
    var speakingSecondsLeft by remember { mutableIntStateOf(120) }
    var isSubmitted by remember { mutableStateOf(false) }

    // Waveform animation
    val infiniteTransition = rememberInfiniteTransition(label = "speaking_wave")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Preparation countdown timer
    LaunchedEffect(speakingState) {
        if (speakingState == SpeakingState.PREPARING) {
            while (prepSecondsLeft > 0 && speakingState == SpeakingState.PREPARING) {
                delay(1000)
                prepSecondsLeft--
            }
            if (prepSecondsLeft == 0) {
                speakingState = SpeakingState.RECORDING
            }
        } else if (speakingState == SpeakingState.RECORDING) {
            while (speakingSecondsLeft > 0 && speakingState == SpeakingState.RECORDING) {
                delay(1000)
                speakingSecondsLeft--
            }
            if (speakingSecondsLeft == 0) {
                speakingState = SpeakingState.COMPLETED
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .padding(horizontal = 18.dp)
            .testTag("speaking_practice_screen"),
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
                    modifier = Modifier.testTag("speaking_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Text(
                    text = "IELTS SPEAKING LAB",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NeonGreen,
                        letterSpacing = 1.sp
                    )
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceElevated)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Part ${cueCard.part} Cue Card",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Cue Card Display
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonGreen.copy(alpha = 0.4f),
                testTag = "speaking_cue_card"
            ) {
                Text(
                    text = "CUE CARD CANDIDATE TASK",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = cueCard.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "You will have 1 minute to take notes, and then you should speak for 1 to 2 minutes.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )

                Spacer(modifier = Modifier.height(12.dp))

                cueCard.bulletPoints.forEach { pt ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text("•", color = NeonCyan, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = pt,
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
                        )
                    }
                }
            }
        }

        // Interactive Audio Recorder & Timers Card
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = when (speakingState) {
                    SpeakingState.RECORDING -> NeonRed
                    SpeakingState.PREPARING -> NeonAmber
                    SpeakingState.COMPLETED -> NeonGreen
                    else -> NeonCyan.copy(alpha = 0.4f)
                },
                testTag = "speaking_recorder_card"
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (speakingState) {
                        SpeakingState.IDLE -> {
                            Text(
                                text = "READY FOR SPEAKING SIMULATION",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier
                                    .size(76.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceElevated)
                                    .border(2.dp, NeonGreen, CircleShape)
                                    .clickable {
                                        prepSecondsLeft = 60
                                        speakingState = SpeakingState.PREPARING
                                    }
                                    .testTag("start_prep_timer_btn"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Start Speaking Practice",
                                    tint = NeonGreen,
                                    modifier = Modifier.size(38.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Tap to Begin 1-Minute Preparation",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                        }

                        SpeakingState.PREPARING -> {
                            Text(
                                text = "PREPARATION COUNTDOWN",
                                style = MaterialTheme.typography.labelSmall.copy(color = NeonAmber)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${prepSecondsLeft}s",
                                style = MaterialTheme.typography.displayLarge.copy(
                                    color = NeonAmber,
                                    fontWeight = FontWeight.Black
                                )
                            )
                            Text(
                                text = "Organize ideas into Introduction, Details, and Reflection",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            FuturisticGlowButton(
                                text = "START SPEAKING NOW",
                                onClick = {
                                    speakingSecondsLeft = 120
                                    speakingState = SpeakingState.RECORDING
                                },
                                containerColor = NeonRed,
                                contentColor = Color.White,
                                testTag = "skip_prep_btn"
                            )
                        }

                        SpeakingState.RECORDING -> {
                            Text(
                                text = "RECORDING IN PROGRESS...",
                                style = MaterialTheme.typography.labelSmall.copy(color = NeonRed)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Dynamic Pulsing Recording Hub
                            Box(
                                modifier = Modifier
                                    .size((80 * pulseScale).dp)
                                    .clip(CircleShape)
                                    .background(NeonRed.copy(alpha = 0.2f))
                                    .border(2.dp, NeonRed, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = null,
                                    tint = NeonRed,
                                    modifier = Modifier.size(42.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            val min = speakingSecondsLeft / 60
                            val sec = speakingSecondsLeft % 60
                            Text(
                                text = String.format("%02d:%02d", min, sec),
                                style = MaterialTheme.typography.displayMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            FuturisticGlowButton(
                                text = "STOP & SAVE RECORDING",
                                onClick = {
                                    speakingState = SpeakingState.COMPLETED
                                },
                                containerColor = NeonAmber,
                                testTag = "stop_speaking_btn"
                            )
                        }

                        SpeakingState.COMPLETED -> {
                            Text(
                                text = "AUDIO RECORDING CAPTURED",
                                style = MaterialTheme.typography.labelSmall.copy(color = NeonGreen)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                IconButton(
                                    onClick = { /* Playback */ },
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(SurfaceElevated)
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = "Play back", tint = NeonGreen)
                                }

                                Column {
                                    Text(
                                        text = "Duration: ${120 - speakingSecondsLeft}s",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "High fidelity stereo speech sample",
                                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        prepSecondsLeft = 60
                                        speakingSecondsLeft = 120
                                        speakingState = SpeakingState.IDLE
                                        isSubmitted = false
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Retry", color = TextSecondary)
                                }

                                FuturisticGlowButton(
                                    text = if (isSubmitted) "SUBMITTED ✓" else "SUBMIT TO TEACHER",
                                    onClick = {
                                        repository.submitSpeakingRecording(120 - speakingSecondsLeft)
                                        isSubmitted = true
                                    },
                                    modifier = Modifier.weight(1.5f),
                                    containerColor = NeonGreen,
                                    testTag = "submit_speaking_btn"
                                )
                            }
                        }
                    }
                }
            }
        }

        // Examiner Rubric Breakdown Guidance
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = SurfaceBorder
            ) {
                Text(
                    text = "SPEAKING RUBRIC BREAKDOWN (4 CRITERIA)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                listOf(
                    "Fluency & Coherence" to "Smooth rhythm with natural discourse markers and minimal pauses.",
                    "Lexical Resource" to "Accurate academic idioms and flexible collocations.",
                    "Grammatical Range" to "Balanced compound and complex structures without systemic errors.",
                    "Pronunciation" to "Intelligible intonation, word stress, and phonetic clarity."
                ).forEach { (criteria, desc) ->
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(
                            text = "• $criteria",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }
            }
        }
    }
}
