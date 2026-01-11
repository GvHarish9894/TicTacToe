package com.techgv.tictactoe.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")

    data object GameMode : Screen("game_mode")

    data object Game : Screen("game/{gameMode}/{difficulty}/{firstPlayer}/{humanSymbol}") {
        fun createRoute(
            gameMode: String,
            difficulty: String = "NONE",
            firstPlayer: String = "HUMAN",
            humanSymbol: String = "X"
        ): String = "game/$gameMode/$difficulty/$firstPlayer/$humanSymbol"
    }

    data object Settings : Screen("settings")
}
