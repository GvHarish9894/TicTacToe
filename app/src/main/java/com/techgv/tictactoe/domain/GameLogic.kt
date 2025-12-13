package com.techgv.tictactoe.domain

import com.techgv.tictactoe.data.model.GameResult
import com.techgv.tictactoe.data.model.Player

object GameLogic {
    // All possible winning combinations (indices)
    private val winningLines = listOf(
        // Rows
        listOf(0, 1, 2),
        listOf(3, 4, 5),
        listOf(6, 7, 8),
        // Columns
        listOf(0, 3, 6),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        // Diagonals
        listOf(0, 4, 8),
        listOf(2, 4, 6)
    )

    /**
     * Check the current game state and return the result.
     * @param board List of 9 Players representing the game board
     * @return GameResult - Win with winner and winning line, Draw, or InProgress
     */
    fun checkGameResult(board: List<Player>): GameResult {
        // Check for a winner
        for (line in winningLines) {
            val (a, b, c) = line
            if (board[a] != Player.NONE &&
                board[a] == board[b] &&
                board[b] == board[c]
            ) {
                return GameResult.Win(
                    winner = board[a],
                    winningLine = line
                )
            }
        }

        // Check for draw (all cells filled with no winner)
        return if (board.none { it == Player.NONE }) {
            GameResult.Draw
        } else {
            GameResult.InProgress
        }
    }

    /**
     * Check if a move is valid at the given index.
     */
    fun isValidMove(board: List<Player>, index: Int): Boolean {
        return index in 0..8 && board[index] == Player.NONE
    }

    /**
     * Make a move and return the new board state.
     */
    fun makeMove(board: List<Player>, index: Int, player: Player): List<Player> {
        if (!isValidMove(board, index)) return board

        return board.toMutableList().apply {
            this[index] = player
        }
    }

    /**
     * Get an empty board.
     */
    fun emptyBoard(): List<Player> = List(9) { Player.NONE }

    /**
     * Get the cell position from index.
     * Returns Pair(row, column) where both are 0-indexed.
     */
    fun getCellPosition(index: Int): Pair<Int, Int> {
        return Pair(index / 3, index % 3)
    }

    /**
     * Get line type for win line drawing.
     */
    fun getWinLineType(winningLine: List<Int>): WinLineType {
        return when (winningLine) {
            listOf(0, 1, 2) -> WinLineType.ROW_TOP
            listOf(3, 4, 5) -> WinLineType.ROW_MIDDLE
            listOf(6, 7, 8) -> WinLineType.ROW_BOTTOM
            listOf(0, 3, 6) -> WinLineType.COLUMN_LEFT
            listOf(1, 4, 7) -> WinLineType.COLUMN_CENTER
            listOf(2, 5, 8) -> WinLineType.COLUMN_RIGHT
            listOf(0, 4, 8) -> WinLineType.DIAGONAL_MAIN
            listOf(2, 4, 6) -> WinLineType.DIAGONAL_ANTI
            else -> WinLineType.ROW_TOP
        }
    }
}

enum class WinLineType {
    ROW_TOP,
    ROW_MIDDLE,
    ROW_BOTTOM,
    COLUMN_LEFT,
    COLUMN_CENTER,
    COLUMN_RIGHT,
    DIAGONAL_MAIN,
    DIAGONAL_ANTI
}
