package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun FuturisticGlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = NeonCyan.copy(alpha = 0.25f),
    backgroundColor: Color = SurfaceCard.copy(alpha = 0.85f),
    shape: RoundedCornerShape = RoundedCornerShape(20.dp),
    testTag: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
            .shadow(
                elevation = 12.dp,
                shape = shape,
                ambientColor = NeonCyan.copy(alpha = 0.15f),
                spotColor = ElectricViolet.copy(alpha = 0.2f)
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    borderColor,
                    ElectricViolet.copy(alpha = 0.15f),
                    borderColor.copy(alpha = 0.05f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun FuturisticScoreRing(
    currentScore: Double,
    targetScore: Double,
    modifier: Modifier = Modifier,
    ringSize: Dp = 150.dp,
    strokeWidth: Dp = 12.dp
) {
    val progress = ((currentScore / targetScore).coerceIn(0.0, 1.0)).toFloat()
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(currentScore, targetScore) {
        animatedProgress.animateTo(
            targetValue = progress,
            animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = modifier.size(ringSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            val diameter = size.minDimension - strokePx
            val topLeft = Offset(strokePx / 2f, strokePx / 2f)
            val arcSize = Size(diameter, diameter)

            // Background track
            drawArc(
                color = Color(0xFF1E293B),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Foreground glowing gradient arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        CyanAccent,
                        NeonCyan,
                        ElectricViolet,
                        NeonCyan
                    )
                ),
                startAngle = 135f,
                sweepAngle = 270f * animatedProgress.value,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = String.format("%.1f", currentScore),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 42.sp,
                    color = NeonCyan,
                    fontWeight = FontWeight.Black
                )
            )
            Text(
                text = "TARGET ${String.format("%.1f", targetScore)}",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFF94A3B8),
                    letterSpacing = 1.2.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun FuturisticGlowButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "futuristic_button",
    containerColor: Color = NeonCyan,
    contentColor: Color = BackgroundDark,
    icon: (@Composable () -> Unit)? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .testTag(testTag)
            .heightIn(min = 48.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
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
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}

@Composable
fun FuturisticAudioPlayerWidget(
    title: String,
    durationSeconds: Int = 180,
    modifier: Modifier = Modifier,
    onAudioCompleted: () -> Unit = {}
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentPositionSeconds by remember { mutableFloatStateOf(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    val waveAnim1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w1"
    )
    val waveAnim2 by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w2"
    )

    LaunchedEffect(isPlaying) {
        while (isPlaying && currentPositionSeconds < durationSeconds) {
            kotlinx.coroutines.delay(1000)
            currentPositionSeconds += 1f
            if (currentPositionSeconds >= durationSeconds) {
                isPlaying = false
                onAudioCompleted()
            }
        }
    }

    FuturisticGlassCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = NeonCyan.copy(alpha = 0.35f),
        testTag = "audio_player_widget"
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "IELTS AUDIO STREAM",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonCyan,
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

            // Waveform animation preview
            if (isPlaying) {
                Row(
                    modifier = Modifier.height(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val heights = listOf(waveAnim1, waveAnim2, waveAnim1 * 0.7f, waveAnim2 * 1.1f, waveAnim1 * 0.5f)
                    heights.forEach { h ->
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .fillMaxHeight(h.coerceIn(0.15f, 1f))
                                .background(NeonCyan, CircleShape)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Scrubber Slider
        Slider(
            value = currentPositionSeconds,
            onValueChange = { currentPositionSeconds = it },
            valueRange = 0f..durationSeconds.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = NeonCyan,
                activeTrackColor = NeonCyan,
                inactiveTrackColor = Color(0xFF1E293B)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("audio_scrubber")
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val currMin = (currentPositionSeconds / 60).toInt()
            val currSec = (currentPositionSeconds % 60).toInt()
            val totalMin = durationSeconds / 60
            val totalSec = durationSeconds % 60

            Text(
                text = String.format("%02d:%02d", currMin, currSec),
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
            )
            Text(
                text = String.format("%02d:%02d", totalMin, totalSec),
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Audio Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { currentPositionSeconds = 0f },
                modifier = Modifier
                    .size(44.dp)
                    .testTag("audio_replay_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Replay,
                    contentDescription = "Replay audio",
                    tint = TextSecondary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(PrimaryGradient)
                    .clickable { isPlaying = !isPlaying }
                    .testTag("audio_play_pause_btn"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause audio" else "Play audio",
                    tint = BackgroundDark,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
