package com.techgv.tictactoe.data.model

data class GameState(
    val board: List<Player> = List(9) { Player.NONE },
    val currentPlayer: Player = Player.X,
    val scoreX: Int = 0,
    val scoreO: Int = 0,
    val gameResult: GameResult = GameResult.InProgress,
    val gameStartTime: Long = System.currentTimeMillis(),
    val lastWinDuration: Long? = null
) {
    val isGameOver: Boolean
        get() = gameResult !is GameResult.InProgress

    val winningLine: List<Int>?
        get() = (gameResult as? GameResult.Win)?.winningLine
}
