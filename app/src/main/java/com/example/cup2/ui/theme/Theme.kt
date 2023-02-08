package com.example.cup2.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val DarkColorScheme = darkColorScheme(
    primary = DeepOrangeLight,
    onPrimary = Color.Black,
    secondary = BlueGreyLight,
    secondaryContainer = DeepOrange,
    tertiary = Green,
    onTertiary = Color.Black,
    error = Red,
    onError = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = DeepOrange,
    onPrimary = Color.White,
    secondary = BlueGrey,
    secondaryContainer = DeepOrangeLight,
    tertiary = GreenDark,
    onTertiary = Color.White,
    error = RedDark,
    onError = Color.White,
)

@Composable
fun Cup2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}