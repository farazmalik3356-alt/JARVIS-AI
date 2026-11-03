package com.jarvis.ai.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val JarvisColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = VoidBlack,
    primaryContainer = NeonCyanDim,
    onPrimaryContainer = VoidBlack,
    secondary = ElectricViolet,
    onSecondary = TextPrimary,
    secondaryContainer = ElectricVioletDim,
    onSecondaryContainer = TextPrimary,
    tertiary = NeonCyan,
    background = VoidBlack,
    onBackground = TextPrimary,
    surface = VoidBlack,
    onSurface = TextPrimary,
    surfaceVariant = DeepNavy,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = BorderSubtle,
    error = ErrorRed,
    onError = VoidBlack,
    scrim = VoidBlack
)

@Composable
fun JarvisTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = JarvisColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = VoidBlack.toArgb()
            window.navigationBarColor = VoidBlack.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = JarvisTypography,
        shapes = JarvisShapes,
        content = content
    )
}