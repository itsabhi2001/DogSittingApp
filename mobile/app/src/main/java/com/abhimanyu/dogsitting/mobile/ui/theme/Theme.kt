package com.abhimanyu.dogsitting.mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light color scheme
private val LightColorScheme = lightColorScheme(
    primary = PawOrange,
    onPrimary = White,
    secondary = PawTeal,
    onSecondary = White,
    background = PawGrayLight,
    onBackground = PawGrayDark,
    surface = White,
    onSurface = PawGrayDark,
    error = Color(0xFFB00020),
    onError = White,
)

// Dark color scheme
private val DarkColorScheme = darkColorScheme(
    primary = PawOrangeDark,
    onPrimary = Black,
    secondary = PawTealDark,
    onSecondary = White,
    background = PawGrayDark,
    onBackground = PawGrayLight,
    surface = PawGrayDark,
    onSurface = PawGrayLight,
    error = Color(0xFFCF6679),
    onError = Black,
)

@Composable
fun DogSittingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
