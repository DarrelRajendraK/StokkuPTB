package com.example.stokkuptb.ui.theme // Ganti 'stokkuapp' jika nama paket Anda beda

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
import androidx.core.view.WindowCompat

// Skema warna untuk Light Mode
private val LightColorScheme = lightColorScheme(
    primary = StokkuRed,
    onPrimary = StokkuWhite,
    secondary = StokkuRedDark,
    onSecondary = StokkuWhite,
    background = StokkuWhite,
    onBackground = StokkuBlack,
    surface = StokkuGrey, // Warna untuk Card
    onSurface = StokkuBlack,
    primaryContainer = StokkuRed,
    onPrimaryContainer = StokkuWhite
)

// Skema warna untuk Dark Mode
private val DarkColorScheme = darkColorScheme(
    primary = StokkuRedDark,
    onPrimary = StokkuWhite,
    secondary = StokkuRed,
    onSecondary = StokkuWhite,
    background = Color(0xFF121212),
    onBackground = StokkuWhite,
    surface = Color(0xFF1E1E1E),
    onSurface = StokkuWhite,
    primaryContainer = StokkuRedDark,
    onPrimaryContainer = StokkuWhite
)

@Composable
fun StokkuAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set warna status bar agar sama dengan TopAppBar
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Pastikan Anda punya file Type.kt
        content = content
    )
}