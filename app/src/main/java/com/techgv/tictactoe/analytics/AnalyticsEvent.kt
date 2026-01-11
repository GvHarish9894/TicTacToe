package com.techgv.tictactoe.analytics

/**
 * Analytics event names following Firebase naming conventions
 * - Use lowercase with underscores
 * - Max 40 characters
 * - Start with letter
 */
object AnalyticsEvent {
    // App Lifecycle
    const val APP_OPEN = "app_open"

    // Screen Views (Firebase auto-tracks, but we add custom params)
    const val SCREEN_VIEW = "screen_view"

    // Game Mode Selection
    const val GAME_MODE_SELECTED = "game_mode_selected"
    const val AI_DIFFICULTY_SELECTED = "ai_difficulty_selected"
    const val FIRST_PLAYER_SELECTED = "first_player_selected"

    // Game Events
    const val GAME_STARTED = "game_started"
    const val PLAYER_MOVE = "player_move"
    const val AI_MOVE = "ai_move"
    const val GAME_WON = "game_won"
    const val GAME_DRAW = "game_draw"
    const val GAME_RESET = "game_reset"
    const val INVALID_MOVE_ATTEMPT = "invalid_move_attempt"

    // Settings Events
    const val SOUND_TOGGLED = "sound_toggled"
    const val HAPTIC_TOGGLED = "haptic_toggled"
    const val PLAYER_NAME_CHANGED = "player_name_changed"

    // Navigation Events
    const val NAVIGATE_FORWARD = "navigate_forward"
    const val NAVIGATE_BACK = "navigate_back"
}
