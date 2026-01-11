package com.techgv.tictactoe.util

import com.techgv.tictactoe.data.model.HumanSymbol
import com.techgv.tictactoe.data.model.Player
import com.techgv.tictactoe.ui.screens.gamemode.GameMode

/**
 * Utility object for determining player display names based on game mode
 */
object PlayerDisplayNames {
    /**
     * Returns the appropriate display name for a player based on the game mode
     *
     * @param player The player to get the display name for
     * @param gameMode The current game mode (PLAYER_VS_PLAYER or PLAYER_VS_AI)
     * @param humanSymbol The symbol chosen by the human player in AI mode
     * @param playerXName The custom name for Player X (used in 2-player mode)
     * @param playerOName The custom name for Player O (used in 2-player mode)
     * @return The display name to show in the UI
     */
    fun getDisplayName(
        player: Player,
        gameMode: GameMode,
        humanSymbol: HumanSymbol,
        playerXName: String,
        playerOName: String
    ): String {
        return when (gameMode) {
            GameMode.PLAYER_VS_AI -> {
                // In AI mode: show "You" for human, "AI" for computer
                if (player == humanSymbol.symbol) "You" else "AI"
            }

            GameMode.PLAYER_VS_PLAYER -> {
                // In 2-player mode: use custom names
                when (player) {
                    Player.X -> playerXName
                    Player.O -> playerOName
                    Player.NONE -> ""
                }
            }
        }
    }
}
