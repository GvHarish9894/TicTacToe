package com.techgv.tictactoe.data.model

sealed class GameResult {
    data object InProgress : GameResult()
    data class Win(
        val winner: Player,
        val winningLine: List<Int>
    ) : GameResult()
    data object Draw : GameResult()
}
