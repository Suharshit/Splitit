package com.example.splitit.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = Green40,
    onPrimary = Neutral95,
    primaryContainer = Green80,
    onPrimaryContainer = Green40,
    secondary = Teal40,
    onSecondary = Neutral95,
    secondaryContainer = Teal80,
    onSecondaryContainer = Teal40,
    background = Neutral95,
    surface = Neutral95,
    onBackground = Neutral10,
    onSurface = Neutral10,
)

private val DarkColors = darkColorScheme(
    primary = Green80,
    onPrimary = Green40,
    primaryContainer = Green40,
    onPrimaryContainer = Green80,
    secondary = Teal80,
    onSecondary = Teal40,
    secondaryContainer = Teal40,
    onSecondaryContainer = Teal80,
)

@Composable
fun SplitItTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}