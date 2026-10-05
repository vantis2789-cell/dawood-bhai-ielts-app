package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = BackgroundDark,
    primaryContainer = SurfaceElevated,
    onPrimaryContainer = NeonCyan,
    secondary = ElectricViolet,
    onSecondary = Color.White,
    secondaryContainer = SurfaceCard,
    onSecondaryContainer = Color(0xFFDDD6FE),
    tertiary = NeonGreen,
    onTertiary = BackgroundDark,
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceBorder,
    outlineVariant = Color(0xFF334155),
    error = NeonRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to futuristic dark mode
    dynamicColor: Boolean = false, // Keep intentional dark futuristic aesthetics
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
