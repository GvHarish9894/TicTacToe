package com.techgv.tictactoe.ui.screens.game

import com.techgv.tictactoe.data.model.GameState

data class GameScreenViewState(
    val gameState: GameState = GameState(),
    val isAIThinking: Boolean = false,
    val showResultDialog: Boolean = false
)
