package com.techgv.tictactoe.data.model

data class GameSettings(
    val soundEnabled: Boolean = true,
    val hapticEnabled: Boolean = false,
    val playerXName: String = "Player X",
    val playerOName: String = "Player O"
)
