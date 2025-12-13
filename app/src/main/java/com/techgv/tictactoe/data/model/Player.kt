package com.techgv.tictactoe.data.model

import androidx.compose.ui.graphics.Color
import com.techgv.tictactoe.ui.theme.CoralAccent
import com.techgv.tictactoe.ui.theme.GreenAccent

enum class Player(
    val symbol: String,
    val displayName: String
) {
    X("X", "Player X"),
    O("O", "Player O"),
    NONE("", "");

    val color: Color
        get() = when (this) {
            X -> GreenAccent
            O -> CoralAccent
            NONE -> Color.Transparent
        }

    fun opposite(): Player = when (this) {
        X -> O
        O -> X
        NONE -> NONE
    }
}
