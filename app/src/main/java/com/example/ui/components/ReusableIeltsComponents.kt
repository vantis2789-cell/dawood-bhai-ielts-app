package com.example.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.MockTest
import com.example.data.model.TestQuestion
import com.example.data.model.VocabWord
import com.example.ui.theme.*
import kotlinx.coroutines.delay

// ==========================================
// 1. Primary & Secondary Buttons
// ==========================================

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: (@Composable () -> Unit)? = null,
    testTag: String = "primary_button"
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .testTag(testTag)
            .heightIn(min = 48.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = NeonCyan,
            contentColor = BackgroundDark,
            disabledContainerColor = SurfaceElevated,
            disabledContentColor = TextMuted
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = BackgroundDark,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    icon()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    testTag: String = "secondary_button"
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .testTag(testTag)
            .heightIn(min = 48.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = TextPrimary,
            disabledContentColor = TextMuted
        ),
        border = BorderStroke(1.dp, SurfaceBorder),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

// ==========================================
// 2. GlassCard
// ==========================================

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = NeonCyan.copy(alpha = 0.25f),
    backgroundColor: Color = SurfaceCard.copy(alpha = 0.88f),
    shape: RoundedCornerShape = RoundedCornerShape(20.dp),
    testTag: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
            .shadow(
                elevation = 8.dp,
                shape = shape,
                ambientColor = NeonCyan.copy(alpha = 0.12f),
                spotColor = ElectricViolet.copy(alpha = 0.15f)
            ),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

// ==========================================
// 3. SkillCard & BandScoreCard
// ==========================================

@Composable
fun SkillCard(
    title: String,
    currentScore: Double,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "skill_card"
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        borderColor = accentColor.copy(alpha = 0.35f),
        testTag = testTag
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = title.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = accentColor,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = String.format("%.1f", currentScore),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Band",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                )
            }
        }
    }
}

@Composable
fun BandScoreCard(
    currentBand: Double,
    targetBand: Double,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = NeonCyan.copy(alpha = 0.45f),
        testTag = "band_score_card"
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "OVERALL ESTIMATED BAND",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = String.format("%.1f", currentBand),
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Black
                    )
                )
                Text(
                    text = "Target Goal: Band ${String.format("%.1f", targetBand)}",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }

            FuturisticScoreRing(
                currentScore = currentBand,
                targetScore = targetBand,
                ringSize = 100.dp,
                strokeWidth = 8.dp
            )
        }
    }
}

// ==========================================
// 4. ProgressCard, MockTestCard & QuestionCard
// ==========================================

@Composable
fun ProgressCard(
    title: String,
    completed: Int,
    total: Int,
    modifier: Modifier = Modifier,
    progressColor: Color = NeonCyan
) {
    val progress = if (total > 0) completed.toFloat() / total else 0f
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = progressColor.copy(alpha = 0.35f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Text(
                text = "$completed / $total",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = progressColor,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = progressColor,
            trackColor = Color(0xFF1E293B)
        )
    }
}

@Composable
fun MockTestCard(
    mockTest: MockTest,
    onStartTest: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = if (mockTest.isCompleted) NeonGreen.copy(alpha = 0.4f) else NeonCyan.copy(alpha = 0.35f),
        testTag = "mock_test_card_${mockTest.id}"
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(SurfaceElevated)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = mockTest.ieltsType.displayName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Text(
                text = "${mockTest.durationMinutes} Mins • ${mockTest.totalQuestions} Questions",
                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = mockTest.title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Licensing: ${mockTest.copyrightOwner} • ${mockTest.licenseStatus}",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
        )

        Spacer(modifier = Modifier.height(12.dp))

        PrimaryButton(
            text = if (mockTest.isCompleted) "RE-TAKE TEST" else "START TEST",
            onClick = onStartTest,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun QuestionCard(
    question: TestQuestion,
    selectedAnswer: String?,
    onAnswerSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isSubmitted: Boolean = false
) {
    val isCorrect = selectedAnswer?.trim()?.equals(question.correctAnswer.trim(), ignoreCase = true) == true
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = if (isSubmitted) {
            if (isCorrect) NeonGreen.copy(alpha = 0.6f) else NeonRed.copy(alpha = 0.6f)
        } else SurfaceBorder,
        testTag = "question_card_${question.id}"
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Question ${question.questionNumber} (${question.questionType.name.replace("_", " ")})",
                style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontWeight = FontWeight.Bold)
            )
            if (isSubmitted) {
                Text(
                    text = if (isCorrect) "✓ Correct" else "✕ Incorrect",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (isCorrect) NeonGreen else NeonRed,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = question.prompt,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        question.options.forEach { opt ->
            val isSelected = selectedAnswer == opt.text
            val isOptionCorrect = opt.text == question.correctAnswer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSubmitted && isOptionCorrect) NeonGreen.copy(alpha = 0.15f)
                        else if (isSelected) NeonCyan.copy(alpha = 0.12f)
                        else SurfaceElevated
                    )
                    .clickable { if (!isSubmitted) onAnswerSelected(opt.text) }
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = { if (!isSubmitted) onAnswerSelected(opt.text) },
                    colors = RadioButtonDefaults.colors(selectedColor = NeonCyan)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${opt.label}. ${opt.text}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (isSubmitted && isOptionCorrect) NeonGreen else TextPrimary,
                        fontWeight = if (isSelected || (isSubmitted && isOptionCorrect)) FontWeight.Bold else FontWeight.Normal
                    )
                )
            }
        }
    }
}

// ==========================================
// 5. TimerCard & StatCard
// ==========================================

@Composable
fun TimerCard(
    secondsRemaining: Int,
    label: String = "Time Remaining",
    modifier: Modifier = Modifier
) {
    val mins = secondsRemaining / 60
    val secs = secondsRemaining % 60
    val timeFormatted = String.format("%02d:%02d", mins, secs)

    GlassCard(
        modifier = modifier,
        borderColor = if (secondsRemaining < 300) NeonRed.copy(alpha = 0.6f) else NeonCyan.copy(alpha = 0.4f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                )
                Text(
                    text = timeFormatted,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = if (secondsRemaining < 300) NeonRed else NeonCyan,
                        fontWeight = FontWeight.Black
                    )
                )
            }
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                tint = if (secondsRemaining < 300) NeonRed else NeonCyan,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        borderColor = accentColor.copy(alpha = 0.35f)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
            }
        }
    }
}

// ==========================================
// 6. Configurable AudioPlayer
// ==========================================

@Composable
fun AudioPlayer(
    title: String,
    durationSeconds: Int = 180,
    allowSeeking: Boolean = true, // Configurable for test mode
    onAudioCompleted: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentSeconds by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isPlaying) {
        while (isPlaying && currentSeconds < durationSeconds) {
            delay(1000)
            currentSeconds += 1f
            if (currentSeconds >= durationSeconds) {
                isPlaying = false
                onAudioCompleted()
            }
        }
    }

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = NeonCyan.copy(alpha = 0.35f),
        testTag = "configurable_audio_player"
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (allowSeeking) "PRACTICE AUDIO PLAYER" else "TEST MODE (SEEKING LOCKED)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (allowSeeking) NeonCyan else NeonAmber,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
            }

            IconButton(
                onClick = { isPlaying = !isPlaying },
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(PrimaryGradient)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = BackgroundDark
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Scrubber
        Slider(
            value = currentSeconds,
            onValueChange = { if (allowSeeking) currentSeconds = it },
            valueRange = 0f..durationSeconds.toFloat(),
            enabled = allowSeeking,
            colors = SliderDefaults.colors(
                thumbColor = NeonCyan,
                activeTrackColor = NeonCyan,
                inactiveTrackColor = Color(0xFF1E293B)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = String.format("%02d:%02d", (currentSeconds / 60).toInt(), (currentSeconds % 60).toInt()),
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )
            Text(
                text = String.format("%02d:%02d", durationSeconds / 60, durationSeconds % 60),
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )
        }
    }
}

// ==========================================
// 7. RecordingButton with Permission Handling Flow
// ==========================================

@Composable
fun RecordingButton(
    onRecordingCompleted: (durationSeconds: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var hasMicPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var showPermissionRationale by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }
    var recordedSeconds by remember { mutableIntStateOf(0) }
    var isRecorded by remember { mutableStateOf(false) }
    var isSubmitted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasMicPermission = isGranted
        if (!isGranted) {
            showPermissionRationale = true
        }
    }

    // Recording timer
    LaunchedEffect(isRecording) {
        while (isRecording) {
            delay(1000)
            recordedSeconds++
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!hasMicPermission) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonAmber.copy(alpha = 0.5f)
            ) {
                Text(
                    text = "MICROPHONE ACCESS REQUIRED",
                    style = MaterialTheme.typography.labelSmall.copy(color = NeonAmber, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "To record your speaking exam response and enable pronunciation assessment, grant microphone permission.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
                Spacer(modifier = Modifier.height(10.dp))
                PrimaryButton(
                    text = "GRANT MICROPHONE ACCESS",
                    onClick = { permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else if (!isRecorded) {
            // Recording trigger
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(if (isRecording) NeonRed else NeonGreen)
                        .clickable {
                            if (isRecording) {
                                isRecording = false
                                isRecorded = true
                            } else {
                                recordedSeconds = 0
                                isRecording = true
                            }
                        }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                        contentDescription = if (isRecording) "Stop" else "Record",
                        tint = BackgroundDark,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Column {
                    Text(
                        text = if (isRecording) "Recording Speech..." else "Tap Mic to Start Speaking",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = if (isRecording) "Elapsed: ${recordedSeconds}s" else "1-2 minutes target speaking duration",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isRecording) NeonRed else TextSecondary
                        )
                    )
                }
            }
        } else {
            // Playback, Retry, and Submit Flow
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonGreen.copy(alpha = 0.5f)
            ) {
                Text(
                    text = "AUDIO SAMPLE READY (${recordedSeconds}s)",
                    style = MaterialTheme.typography.labelSmall.copy(color = NeonGreen, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SecondaryButton(
                        text = "Retry / Delete",
                        onClick = {
                            isRecorded = false
                            recordedSeconds = 0
                            isSubmitted = false
                        },
                        modifier = Modifier.weight(1f)
                    )
                    PrimaryButton(
                        text = if (isSubmitted) "SUBMITTED ✓" else "SUBMIT AUDIO",
                        onClick = {
                            isSubmitted = true
                            onRecordingCompleted(recordedSeconds)
                        },
                        modifier = Modifier.weight(1.2f)
                    )
                }
            }
        }
    }

    if (showPermissionRationale) {
        AlertDialog(
            onDismissRequest = { showPermissionRationale = false },
            title = { Text("Audio Permission", color = TextPrimary) },
            text = {
                Text(
                    "Speaking practice requires microphone recording. You can also grant this at any time in Android Settings.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionRationale = false
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }) {
                    Text("Retry", color = NeonCyan)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionRationale = false }) {
                    Text("Dismiss", color = TextSecondary)
                }
            },
            containerColor = SurfaceElevated
        )
    }
}

// ==========================================
// 8. VocabularyCard, LoadingView, ErrorView & EmptyState
// ==========================================

@Composable
fun VocabularyCard(
    vocab: VocabWord,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = vocab.word,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "${vocab.phonetic} • ${vocab.partOfSpeech}",
                    style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan)
                )
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (vocab.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (vocab.isFavorite) NeonRed else TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = vocab.definition,
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
        )
    }
}

@Composable
fun LoadingView(
    message: String = "Loading IELTS Command Data...",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = NeonCyan, strokeWidth = 3.dp)
            Spacer(modifier = Modifier.height(14.dp))
            Text(text = message, style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
        }
    }
}

@Composable
fun ErrorView(
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = NeonRed.copy(alpha = 0.5f)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = NeonRed, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text("Connection or Loading Error", style = MaterialTheme.typography.titleSmall.copy(color = NeonRed, fontWeight = FontWeight.Bold))
                Text(errorMessage, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        SecondaryButton(text = "Retry Action", onClick = onRetry, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun EmptyState(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actionButton: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Inbox,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                textAlign = TextAlign.Center
            )
            if (actionButton != null) {
                Spacer(modifier = Modifier.height(16.dp))
                actionButton()
            }
        }
    }
}
