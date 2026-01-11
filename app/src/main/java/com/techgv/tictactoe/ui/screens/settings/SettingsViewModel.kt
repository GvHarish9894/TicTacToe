package com.techgv.tictactoe.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techgv.tictactoe.analytics.AnalyticsHelper
import com.techgv.tictactoe.analytics.AnalyticsValues
import com.techgv.tictactoe.data.model.GameSettings
import com.techgv.tictactoe.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {
    val settings: StateFlow<GameSettings> =
        settingsRepository.settings.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GameSettings(),
        )

    fun updateSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            analyticsHelper.logSoundToggled(enabled)
            settingsRepository.updateSoundEnabled(enabled)
        }
    }

    fun updateHapticEnabled(enabled: Boolean) {
        viewModelScope.launch {
            analyticsHelper.logHapticToggled(enabled)
            settingsRepository.updateHapticEnabled(enabled)
        }
    }

    fun updatePlayerXName(name: String) {
        val oldName = settings.value.playerXName
        if (oldName != name) {
            viewModelScope.launch {
                analyticsHelper.logPlayerNameChanged(
                    playerType = AnalyticsValues.SETTING_PLAYER_X_NAME,
                    oldName = oldName,
                    newName = name
                )
                settingsRepository.updatePlayerXName(name)
            }
        }
    }

    fun updatePlayerOName(name: String) {
        val oldName = settings.value.playerOName
        if (oldName != name) {
            viewModelScope.launch {
                analyticsHelper.logPlayerNameChanged(
                    playerType = AnalyticsValues.SETTING_PLAYER_O_NAME,
                    oldName = oldName,
                    newName = name
                )
                settingsRepository.updatePlayerOName(name)
            }
        }
    }
}
