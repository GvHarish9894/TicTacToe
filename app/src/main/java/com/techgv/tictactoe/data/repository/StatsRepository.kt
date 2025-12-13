package com.techgv.tictactoe.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.techgv.tictactoe.data.model.GameStats
import com.techgv.tictactoe.data.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "game_stats")

class StatsRepository(private val context: Context) {

    private object PreferencesKeys {
        val TOTAL_GAMES = intPreferencesKey("total_games")
        val PLAYER_X_WINS = intPreferencesKey("player_x_wins")
        val PLAYER_O_WINS = intPreferencesKey("player_o_wins")
        val DRAWS = intPreferencesKey("draws")
        val FASTEST_WIN_SECONDS = longPreferencesKey("fastest_win_seconds")
        val CURRENT_WIN_STREAK = intPreferencesKey("current_win_streak")
        val LAST_WINNER = stringPreferencesKey("last_winner")
    }

    val gameStats: Flow<GameStats> = context.dataStore.data.map { preferences ->
        GameStats(
            totalGames = preferences[PreferencesKeys.TOTAL_GAMES] ?: 0,
            playerXWins = preferences[PreferencesKeys.PLAYER_X_WINS] ?: 0,
            playerOWins = preferences[PreferencesKeys.PLAYER_O_WINS] ?: 0,
            draws = preferences[PreferencesKeys.DRAWS] ?: 0,
            fastestWinSeconds = preferences[PreferencesKeys.FASTEST_WIN_SECONDS],
            currentWinStreak = preferences[PreferencesKeys.CURRENT_WIN_STREAK] ?: 0,
            lastWinner = preferences[PreferencesKeys.LAST_WINNER]?.let {
                if (it.isNotEmpty()) Player.valueOf(it) else null
            }
        )
    }

    suspend fun recordWin(winner: Player, durationSeconds: Long, currentStats: GameStats) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TOTAL_GAMES] = currentStats.totalGames + 1

            when (winner) {
                Player.X -> {
                    preferences[PreferencesKeys.PLAYER_X_WINS] = currentStats.playerXWins + 1
                }
                Player.O -> {
                    preferences[PreferencesKeys.PLAYER_O_WINS] = currentStats.playerOWins + 1
                }
                Player.NONE -> { /* Should not happen */ }
            }

            // Update fastest win if this is faster
            val currentFastest = currentStats.fastestWinSeconds
            if (currentFastest == null || durationSeconds < currentFastest) {
                preferences[PreferencesKeys.FASTEST_WIN_SECONDS] = durationSeconds
            }

            // Update win streak
            val newStreak = if (currentStats.lastWinner == winner) {
                currentStats.currentWinStreak + 1
            } else {
                1
            }
            preferences[PreferencesKeys.CURRENT_WIN_STREAK] = newStreak
            preferences[PreferencesKeys.LAST_WINNER] = winner.name
        }
    }

    suspend fun recordDraw(currentStats: GameStats) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TOTAL_GAMES] = currentStats.totalGames + 1
            preferences[PreferencesKeys.DRAWS] = currentStats.draws + 1
            preferences[PreferencesKeys.CURRENT_WIN_STREAK] = 0
            preferences[PreferencesKeys.LAST_WINNER] = ""
        }
    }

    suspend fun resetStats() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
