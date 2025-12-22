package com.techgv.tictactoe.domain.ai

import com.techgv.tictactoe.data.model.Player

/**
 * Interface for AI strategies in Tic Tac Toe.
 */
interface AIStrategy {
    /**
     * Calculate the best move for the AI player.
     * @param board Current board state (List of 9 Players)
     * @param aiPlayer The player the AI controls
     * @return Index (0-8) of the cell to play
     */
    fun findBestMove(board: List<Player>, aiPlayer: Player): Int
}
