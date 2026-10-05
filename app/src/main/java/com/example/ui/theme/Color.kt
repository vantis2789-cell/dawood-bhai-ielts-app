package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Futuristic Dark Palette for DAWOOD BHAI IELTS STUDIO
val BackgroundDark = Color(0xFF080C14)
val SurfaceDark = Color(0xFF0F172A)
val SurfaceCard = Color(0xFF131D33)
val SurfaceElevated = Color(0xFF1A2642)
val SurfaceBorder = Color(0xFF233554)
val SurfaceGlass = Color(0x99131D33)

// Neon & Brand Accents
val NeonCyan = Color(0xFF00F2FE)
val CyanAccent = Color(0xFF4FACFE)
val ElectricViolet = Color(0xFF8B5CF6)
val NeonPurple = Color(0xFFA855F7)
val DeepViolet = Color(0xFF6366F1)

// Status & Indicators
val NeonGreen = Color(0xFF10B981)
val NeonAmber = Color(0xFFF59E0B)
val NeonRed = Color(0xFFEF4444)

// Text Colors
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

// Gradients
val PrimaryGradient = Brush.horizontalGradient(
    colors = listOf(CyanAccent, NeonCyan)
)

val VioletCyanGradient = Brush.horizontalGradient(
    colors = listOf(ElectricViolet, NeonCyan)
)

val CardGlowBorder = Brush.linearGradient(
    colors = listOf(
        Color(0xFF00F2FE).copy(alpha = 0.4f),
        Color(0xFF8B5CF6).copy(alpha = 0.3f),
        Color(0xFF00F2FE).copy(alpha = 0.1f)
    )
)

val BackgroundMesh = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF070A12),
        Color(0xFF0B101E),
        Color(0xFF080C14)
    )
)
