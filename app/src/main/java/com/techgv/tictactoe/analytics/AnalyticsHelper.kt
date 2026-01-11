package com.techgv.tictactoe.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import com.techgv.tictactoe.data.model.AIDifficulty
import com.techgv.tictactoe.data.model.FirstPlayer
import com.techgv.tictactoe.data.model.Player
import com.techgv.tictactoe.ui.screens.gamemode.GameMode

/**
 * Central helper class for Firebase Analytics integration.
 * Provides type-safe, convenient methods for logging analytics events.
 */
class AnalyticsHelper {

    private val analytics: FirebaseAnalytics = Firebase.analytics

    // ==================== App Lifecycle ====================

    fun logAppOpen() {
        analytics.logEvent(AnalyticsEvent.APP_OPEN, null)
    }

    // ==================== Screen Tracking ====================

    fun logScreenView(screenName: String, screenClass: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        }
    }

    // ==================== Game Mode Selection ====================

    fun logGameModeSelected(gameMode: GameMode) {
        analytics.logEvent(AnalyticsEvent.GAME_MODE_SELECTED) {
            param(AnalyticsParams.GAME_MODE, gameMode.toAnalyticsValue())
        }
    }

    fun logAIDifficultySelected(difficulty: AIDifficulty) {
        analytics.logEvent(AnalyticsEvent.AI_DIFFICULTY_SELECTED) {
            param(AnalyticsParams.AI_DIFFICULTY, difficulty.toAnalyticsValue())
        }
    }

    fun logFirstPlayerSelected(firstPlayer: FirstPlayer) {
        analytics.logEvent(AnalyticsEvent.FIRST_PLAYER_SELECTED) {
            param(AnalyticsParams.FIRST_PLAYER, firstPlayer.toAnalyticsValue())
        }
    }

    // ==================== Game Events ====================

    fun logGameStarted(
        gameMode: GameMode,
        difficulty: AIDifficulty?,
        firstPlayer: FirstPlayer?
    ) {
        analytics.logEvent(AnalyticsEvent.GAME_STARTED) {
            param(AnalyticsParams.GAME_MODE, gameMode.toAnalyticsValue())
            difficulty?.let {
                param(AnalyticsParams.AI_DIFFICULTY, it.toAnalyticsValue())
            }
            firstPlayer?.let {
                param(AnalyticsParams.FIRST_PLAYER, it.toAnalyticsValue())
            }
        }
    }

    fun logPlayerMove(
        cellIndex: Int,
        player: Player,
        moveNumber: Int
    ) {
        analytics.logEvent(AnalyticsEvent.PLAYER_MOVE) {
            param(AnalyticsParams.CELL_INDEX, cellIndex.toLong())
            param(AnalyticsParams.PLAYER, player.toAnalyticsValue())
            param(AnalyticsParams.MOVE_NUMBER, moveNumber.toLong())
        }
    }

    fun logAIMove(
        difficulty: AIDifficulty,
        cellIndex: Int,
        thinkingTimeMs: Long,
        moveNumber: Int
    ) {
        analytics.logEvent(AnalyticsEvent.AI_MOVE) {
            param(AnalyticsParams.AI_DIFFICULTY, difficulty.toAnalyticsValue())
            param(AnalyticsParams.CELL_INDEX, cellIndex.toLong())
            param(AnalyticsParams.AI_THINKING_TIME, thinkingTimeMs)
            param(AnalyticsParams.MOVE_NUMBER, moveNumber.toLong())
        }
    }

    fun logGameWon(
        winner: Player,
        durationSeconds: Long,
        winningLine: List<Int>,
        moveCount: Int,
        scoreX: Int,
        scoreO: Int
    ) {
        analytics.logEvent(AnalyticsEvent.GAME_WON) {
            param(AnalyticsParams.WINNER, winner.toAnalyticsValue())
            param(AnalyticsParams.GAME_DURATION, durationSeconds)
            param(AnalyticsParams.WINNING_PATTERN, getWinningPattern(winningLine))
            param(AnalyticsParams.MOVE_COUNT, moveCount.toLong())
            param(AnalyticsParams.SCORE_X, scoreX.toLong())
            param(AnalyticsParams.SCORE_O, scoreO.toLong())
        }
    }

    fun logGameDraw(
        durationSeconds: Long,
        moveCount: Int,
        scoreX: Int,
        scoreO: Int
    ) {
        analytics.logEvent(AnalyticsEvent.GAME_DRAW) {
            param(AnalyticsParams.GAME_DURATION, durationSeconds)
            param(AnalyticsParams.MOVE_COUNT, moveCount.toLong())
            param(AnalyticsParams.SCORE_X, scoreX.toLong())
            param(AnalyticsParams.SCORE_O, scoreO.toLong())
        }
    }

    fun logGameReset(scoreX: Int, scoreO: Int) {
        analytics.logEvent(AnalyticsEvent.GAME_RESET) {
            param(AnalyticsParams.SCORE_X, scoreX.toLong())
            param(AnalyticsParams.SCORE_O, scoreO.toLong())
        }
    }

    fun logInvalidMoveAttempt(cellIndex: Int, player: Player) {
        analytics.logEvent(AnalyticsEvent.INVALID_MOVE_ATTEMPT) {
            param(AnalyticsParams.CELL_INDEX, cellIndex.toLong())
            param(AnalyticsParams.PLAYER, player.toAnalyticsValue())
        }
    }

    // ==================== Settings Events ====================

    fun logSoundToggled(enabled: Boolean) {
        analytics.logEvent(AnalyticsEvent.SOUND_TOGGLED) {
            param(AnalyticsParams.SETTING_NAME, AnalyticsValues.SETTING_SOUND)
            param(
                AnalyticsParams.NEW_VALUE,
                if (enabled) AnalyticsValues.VALUE_TRUE else AnalyticsValues.VALUE_FALSE
            )
        }
    }

    fun logHapticToggled(enabled: Boolean) {
        analytics.logEvent(AnalyticsEvent.HAPTIC_TOGGLED) {
            param(AnalyticsParams.SETTING_NAME, AnalyticsValues.SETTING_HAPTIC)
            param(
                AnalyticsParams.NEW_VALUE,
                if (enabled) AnalyticsValues.VALUE_TRUE else AnalyticsValues.VALUE_FALSE
            )
        }
    }

    fun logPlayerNameChanged(playerType: String, oldName: String, newName: String) {
        analytics.logEvent(AnalyticsEvent.PLAYER_NAME_CHANGED) {
            param(AnalyticsParams.PLAYER_TYPE, playerType)
            param(AnalyticsParams.OLD_VALUE, oldName)
            param(AnalyticsParams.NEW_VALUE, newName)
        }
    }

    // ==================== Navigation Events ====================

    fun logNavigateForward(fromScreen: String, toScreen: String) {
        analytics.logEvent(AnalyticsEvent.NAVIGATE_FORWARD) {
            param(AnalyticsParams.FROM_SCREEN, fromScreen)
            param(AnalyticsParams.TO_SCREEN, toScreen)
        }
    }

    fun logNavigateBack(fromScreen: String, toScreen: String) {
        analytics.logEvent(AnalyticsEvent.NAVIGATE_BACK) {
            param(AnalyticsParams.FROM_SCREEN, fromScreen)
            param(AnalyticsParams.TO_SCREEN, toScreen)
        }
    }

    // ==================== Helper Methods ====================

    private fun getWinningPattern(winningLine: List<Int>): String {
        return when (winningLine) {
            listOf(0, 1, 2) -> "row_0"
            listOf(3, 4, 5) -> "row_1"
            listOf(6, 7, 8) -> "row_2"
            listOf(0, 3, 6) -> "col_0"
            listOf(1, 4, 7) -> "col_1"
            listOf(2, 5, 8) -> "col_2"
            listOf(0, 4, 8) -> "diag_main"
            listOf(2, 4, 6) -> "diag_anti"
            else -> "unknown"
        }
    }

    // Extension functions for enum conversions
    private fun GameMode.toAnalyticsValue(): String = when (this) {
        GameMode.PLAYER_VS_PLAYER -> AnalyticsValues.MODE_PVP
        GameMode.PLAYER_VS_AI -> AnalyticsValues.MODE_PVA
    }

    private fun AIDifficulty.toAnalyticsValue(): String = when (this) {
        AIDifficulty.EASY -> AnalyticsValues.DIFFICULTY_EASY
        AIDifficulty.MEDIUM -> AnalyticsValues.DIFFICULTY_MEDIUM
        AIDifficulty.HARD -> AnalyticsValues.DIFFICULTY_HARD
    }

    private fun FirstPlayer.toAnalyticsValue(): String = when (this) {
        FirstPlayer.HUMAN -> AnalyticsValues.FIRST_HUMAN
        FirstPlayer.AI -> AnalyticsValues.FIRST_AI
    }

    private fun Player.toAnalyticsValue(): String = when (this) {
        Player.X -> AnalyticsValues.PLAYER_X
        Player.O -> AnalyticsValues.PLAYER_O
        Player.NONE -> "none"
    }
}
