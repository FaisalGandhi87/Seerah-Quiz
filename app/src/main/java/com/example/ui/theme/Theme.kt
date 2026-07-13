package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldTeal,
    onPrimary = Color.White,
    secondary = RadiantGold,
    onSecondary = DeepNavy,
    tertiary = AmberGold,
    onTertiary = DeepNavy,
    background = DarkBackground,
    onBackground = Color(0xFFECEFF4),
    surface = DarkSurface,
    onSurface = Color(0xFFECEFF4),
    error = CoralRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldTeal,
    onPrimary = Color.White,
    secondary = RadiantGold,
    onSecondary = DeepNavy,
    tertiary = AmberGold,
    onTertiary = DeepNavy,
    background = LightBackground,
    onBackground = DeepNavy,
    surface = LightSurface,
    onSurface = DeepNavy,
    error = CoralRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
