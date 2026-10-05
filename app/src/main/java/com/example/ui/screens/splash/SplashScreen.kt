package com.example.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.theme.*

@Composable
fun SplashScreen(
    onNavigateNext: () -> Unit
) {
    val scale = remember { Animatable(0.7f) }
    val alpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )
        textAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMesh)
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Academy Brand Emblem
            Box(
                modifier = Modifier
                    .scale(scale.value)
                    .alpha(alpha.value)
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                SurfaceElevated,
                                Color(0xFF0F172A),
                                BackgroundDark
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.sweepGradient(
                            colors = listOf(NeonCyan, ElectricViolet, NeonCyan)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_app_launcher),
                    contentDescription = "Dawood Bhai IELTS Studio Logo",
                    modifier = Modifier
                        .size(105.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Main Brand Title
            Text(
                text = "DAWOOD BHAI",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 28.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha.value)
            )

            Text(
                text = "IELTS STUDIO",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 32.sp,
                    color = NeonCyan,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha.value)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Your Journey to IELTS Success",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.5.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha.value)
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Creator Credit Badge (Subtle & Elegant as requested)
            Box(
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF1E293B).copy(alpha = 0.7f))
                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Designed & Developed by Sahil VANTIS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF94A3B8),
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.8.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Enter Command Center Action
            FuturisticGlowButton(
                text = "ENTER COMMAND CENTER",
                onClick = onNavigateNext,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .alpha(textAlpha.value),
                testTag = "splash_enter_button",
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = BackgroundDark
                    )
                }
            )
        }
    }
}
