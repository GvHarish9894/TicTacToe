package com.techgv.tictactoe.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")

    data object GameMode : Screen("game_mode")

    data object Game : Screen("game/{gameMode}/{difficulty}/{firstPlayer}") {
        fun createRoute(
            gameMode: String,
            difficulty: String = "NONE",
            firstPlayer: String = "HUMAN"
        ): String = "game/$gameMode/$difficulty/$firstPlayer"
    }

    data object Settings : Screen("settings")
}
