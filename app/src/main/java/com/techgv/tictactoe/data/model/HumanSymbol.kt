package com.techgv.tictactoe.data.model

/**
 * Represents the symbol choice for the human player in AI mode
 */
enum class HumanSymbol(val displayName: String, val symbol: Player) {
    X("X", Player.X),
    O("O", Player.O)
}
