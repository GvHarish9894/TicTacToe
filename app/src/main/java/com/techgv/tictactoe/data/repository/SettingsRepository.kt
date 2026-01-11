package com.techgv.tictactoe.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.techgv.tictactoe.data.model.GameSettings
import com.techgv.tictactoe.data.model.HumanSymbol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "game_settings")

class SettingsRepository(private val context: Context) {

    private object PreferencesKeys {
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val HAPTIC_ENABLED = booleanPreferencesKey("haptic_enabled")
        val PLAYER_X_NAME = stringPreferencesKey("player_x_name")
        val PLAYER_O_NAME = stringPreferencesKey("player_o_name")
        val HUMAN_SYMBOL = stringPreferencesKey("human_symbol")
    }

    val settings: Flow<GameSettings> = context.settingsDataStore.data.map { preferences ->
        val humanSymbolStr = preferences[PreferencesKeys.HUMAN_SYMBOL] ?: "X"
        val humanSymbol = when (humanSymbolStr) {
            "O" -> HumanSymbol.O
            else -> HumanSymbol.X
        }

        GameSettings(
            soundEnabled = preferences[PreferencesKeys.SOUND_ENABLED] ?: true,
            hapticEnabled = preferences[PreferencesKeys.HAPTIC_ENABLED] ?: false,
            playerXName = preferences[PreferencesKeys.PLAYER_X_NAME] ?: "Player X",
            playerOName = preferences[PreferencesKeys.PLAYER_O_NAME] ?: "Player O",
            humanSymbol = humanSymbol
        )
    }

    suspend fun updateSoundEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_ENABLED] = enabled
        }
    }

    suspend fun updateHapticEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.HAPTIC_ENABLED] = enabled
        }
    }

    suspend fun updatePlayerXName(name: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.PLAYER_X_NAME] = name.ifBlank { "Player X" }
        }
    }

    suspend fun updatePlayerOName(name: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.PLAYER_O_NAME] = name.ifBlank { "Player O" }
        }
    }

    suspend fun updateHumanSymbol(symbol: HumanSymbol) {
        context.settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.HUMAN_SYMBOL] = symbol.displayName
        }
    }

    suspend fun resetSettings() {
        context.settingsDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
