package com.battlebucks.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val GameDarkColorScheme = darkColorScheme(
    primary = OrangePrimary,
    onPrimary = TextPrimary,
    secondary = Gold,
    onSecondary = BackgroundDark,
    tertiary = OrangeLight,
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = BackgroundCard,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundCardElevated,
    onSurfaceVariant = TextSecondary,
    error = RedAccent
)

@Composable
fun LeaderboardAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GameDarkColorScheme,
        typography = Typography,
        content = content
    )
}
