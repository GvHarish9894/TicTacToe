package com.techgv.tictactoe.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val TicTacToeColorScheme = darkColorScheme(
    primary = GreenAccent,
    onPrimary = DarkGreen900,
    primaryContainer = DarkGreen700,
    onPrimaryContainer = GreenAccent,
    secondary = CoralAccent,
    onSecondary = DarkGreen900,
    secondaryContainer = DarkGreen700,
    onSecondaryContainer = CoralAccent,
    tertiary = GreenAccentDark,
    onTertiary = DarkGreen900,
    background = DarkGreen900,
    onBackground = TextPrimary,
    surface = DarkGreen800,
    onSurface = TextPrimary,
    surfaceVariant = CardBackground,
    onSurfaceVariant = TextSecondary,
    outline = DarkGreen500,
    outlineVariant = CellBorder,
    error = ErrorRed,
    onError = Color.White,
    inverseSurface = TextPrimary,
    inverseOnSurface = DarkGreen900,
    inversePrimary = DarkGreen700,
    surfaceTint = GreenAccent
)

@Composable
fun TicTacToeTheme(
    darkTheme: Boolean = true, // Always use dark theme
    dynamicColor: Boolean = false, // Disable dynamic colors for consistent branding
    content: @Composable () -> Unit
) {
    val colorScheme = TicTacToeColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkGreen900.toArgb()
            window.navigationBarColor = DarkGreen900.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
