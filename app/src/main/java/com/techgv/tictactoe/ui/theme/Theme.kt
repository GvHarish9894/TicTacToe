package com.techgv.tictactoe.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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

@Suppress("UNUSED_PARAMETER")
@Composable
fun TicTacToeTheme(
    darkTheme: Boolean = true, // Always use dark theme
    dynamicColor: Boolean = false, // Disable dynamic colors for consistent branding
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TicTacToeColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
