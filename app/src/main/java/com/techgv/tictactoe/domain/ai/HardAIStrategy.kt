package com.techgv.tictactoe.domain.ai

import com.techgv.tictactoe.data.model.GameResult
import com.techgv.tictactoe.data.model.Player
import com.techgv.tictactoe.domain.GameLogic

/**
 * Hard AI: Uses Minimax algorithm for perfect play.
 * Never loses - will always win or draw.
 */
class HardAIStrategy : AIStrategy {

    override fun findBestMove(board: List<Player>, aiPlayer: Player): Int {
        val humanPlayer = aiPlayer.opposite()
        var bestScore = Int.MIN_VALUE
        var bestMove = -1

        for (i in board.indices) {
            if (board[i] == Player.NONE) {
                val newBoard = GameLogic.makeMove(board, i, aiPlayer)
                val score = minimax(
                    board = newBoard,
                    depth = 0,
                    isMaximizing = false,
                    aiPlayer = aiPlayer,
                    humanPlayer = humanPlayer
                )
                if (score > bestScore) {
                    bestScore = score
                    bestMove = i
                }
            }
        }
        return bestMove
    }

    private fun minimax(
        board: List<Player>,
        depth: Int,
        isMaximizing: Boolean,
        aiPlayer: Player,
        humanPlayer: Player
    ): Int {
        val result = GameLogic.checkGameResult(board)

        // Terminal states
        when (result) {
            is GameResult.Win -> {
                return if (result.winner == aiPlayer) {
                    10 - depth // AI wins - prefer faster wins
                } else {
                    depth - 10 // Human wins - prefer slower losses
                }
            }
            is GameResult.Draw -> return 0
            is GameResult.InProgress -> { /* continue recursion */ }
        }

        if (isMaximizing) {
            // AI's turn - maximize score
            var bestScore = Int.MIN_VALUE
            for (i in board.indices) {
                if (board[i] == Player.NONE) {
                    val newBoard = GameLogic.makeMove(board, i, aiPlayer)
                    val score = minimax(newBoard, depth + 1, false, aiPlayer, humanPlayer)
                    bestScore = maxOf(bestScore, score)
                }
            }
            return bestScore
        } else {
            // Human's turn - minimize score
            var bestScore = Int.MAX_VALUE
            for (i in board.indices) {
                if (board[i] == Player.NONE) {
                    val newBoard = GameLogic.makeMove(board, i, humanPlayer)
                    val score = minimax(newBoard, depth + 1, true, aiPlayer, humanPlayer)
                    bestScore = minOf(bestScore, score)
                }
            }
            return bestScore
        }
    }
}
