package com.techgv.tictactoe.analytics

/**
 * Analytics parameter keys following Firebase conventions
 * - Use lowercase with underscores
 * - Max 40 characters
 * - Descriptive and consistent
 */
object AnalyticsParams {
    // Standard Firebase parameters
    const val SCREEN_NAME = "screen_name"
    const val SCREEN_CLASS = "screen_class"

    // Game Mode parameters
    const val GAME_MODE = "game_mode"
    const val AI_DIFFICULTY = "ai_difficulty"
    const val FIRST_PLAYER = "first_player"

    // Game State parameters
    const val PLAYER = "player"
    const val CELL_INDEX = "cell_index"
    const val MOVE_NUMBER = "move_number"
    const val WINNER = "winner"
    const val GAME_DURATION = "game_duration" // in seconds
    const val MOVE_COUNT = "move_count"
    const val WINNING_PATTERN = "winning_pattern" // e.g., "row_0", "col_1", "diag_main"
    const val AI_THINKING_TIME = "ai_thinking_time" // in milliseconds

    // Score parameters
    const val SCORE_X = "score_x"
    const val SCORE_O = "score_o"

    // Settings parameters
    const val SETTING_NAME = "setting_name"
    const val OLD_VALUE = "old_value"
    const val NEW_VALUE = "new_value"
    const val PLAYER_TYPE = "player_type" // "player_x" or "player_o"

    // Navigation parameters
    const val FROM_SCREEN = "from_screen"
    const val TO_SCREEN = "to_screen"
    const val NAVIGATION_TYPE = "navigation_type" // "forward" or "back"
}

/**
 * Common parameter values for consistency
 */
object AnalyticsValues {
    // Game Modes
    const val MODE_PVP = "player_vs_player"
    const val MODE_PVA = "player_vs_ai"

    // AI Difficulties
    const val DIFFICULTY_EASY = "easy"
    const val DIFFICULTY_MEDIUM = "medium"
    const val DIFFICULTY_HARD = "hard"

    // First Player
    const val FIRST_HUMAN = "human"
    const val FIRST_AI = "ai"

    // Players
    const val PLAYER_X = "player_x"
    const val PLAYER_O = "player_o"

    // Settings
    const val SETTING_SOUND = "sound_effects"
    const val SETTING_HAPTIC = "haptic_feedback"
    const val SETTING_PLAYER_X_NAME = "player_x_name"
    const val SETTING_PLAYER_O_NAME = "player_o_name"

    // Screens
    const val SCREEN_SPLASH = "splash"
    const val SCREEN_GAME_MODE = "game_mode"
    const val SCREEN_GAME = "game"
    const val SCREEN_SETTINGS = "settings"

    // Boolean values
    const val VALUE_TRUE = "true"
    const val VALUE_FALSE = "false"
}
