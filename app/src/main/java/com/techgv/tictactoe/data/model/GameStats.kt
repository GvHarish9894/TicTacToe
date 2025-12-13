package com.techgv.tictactoe.data.model

data class GameStats(
    val totalGames: Int = 0,
    val playerXWins: Int = 0,
    val playerOWins: Int = 0,
    val draws: Int = 0,
    val fastestWinSeconds: Long? = null,
    val currentWinStreak: Int = 0,
    val lastWinner: Player? = null
) {
    val winRateX: Float
        get() = if (totalGames > 0) playerXWins.toFloat() / totalGames else 0f

    val winRateO: Float
        get() = if (totalGames > 0) playerOWins.toFloat() / totalGames else 0f

    val drawRate: Float
        get() = if (totalGames > 0) draws.toFloat() / totalGames else 0f
}
