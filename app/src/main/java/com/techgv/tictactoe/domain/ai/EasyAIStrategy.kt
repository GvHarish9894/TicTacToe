package com.techgv.tictactoe.domain.ai

import com.techgv.tictactoe.data.model.Player
import kotlin.random.Random

/**
 * Easy AI: Picks a random valid move.
 */
class EasyAIStrategy : AIStrategy {
    override fun findBestMove(board: List<Player>, aiPlayer: Player): Int {
        val availableMoves = board.indices.filter { board[it] == Player.NONE }
        return if (availableMoves.isNotEmpty()) {
            availableMoves.random(Random)
        } else {
            -1 // No valid moves
        }
    }
}
