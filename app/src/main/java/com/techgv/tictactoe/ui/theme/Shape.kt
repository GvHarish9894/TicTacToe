package com.techgv.tictactoe.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    // Small elements like badges, chips
    extraSmall = RoundedCornerShape(4.dp),
    // Buttons, small cards
    small = RoundedCornerShape(8.dp),
    // Cards, dialogs
    medium = RoundedCornerShape(16.dp),
    // Large cards, game board cells
    large = RoundedCornerShape(20.dp),
    // Full-width bottom sheets, large containers
    extraLarge = RoundedCornerShape(28.dp)
)

// Custom shapes for specific UI elements
val GameCellShape = RoundedCornerShape(16.dp)
val ButtonShape = RoundedCornerShape(24.dp)
val CardShape = RoundedCornerShape(20.dp)
val DialogShape = RoundedCornerShape(28.dp)
val TurnIndicatorShape = RoundedCornerShape(50) // Pill shape
val BadgeShape = RoundedCornerShape(8.dp)
