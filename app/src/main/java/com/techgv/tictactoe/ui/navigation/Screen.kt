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
    data object Stats : Screen("stats")
    data object Settings : Screen("settings")
}

// Bottom navigation items
enum class BottomNavItem(
    val screen: Screen,
    val title: String,
    val iconSelected: String,
    val iconUnselected: String
) {
    Home(Screen.GameMode, "HOME", "home_filled", "home_outlined"),
    Stats(Screen.Stats, "STATS", "stats_filled", "stats_outlined"),
    Settings(Screen.Settings, "SETTINGS", "settings_filled", "settings_outlined")
}
