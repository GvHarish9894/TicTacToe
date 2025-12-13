package com.techgv.tictactoe.ui.screens.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.techgv.tictactoe.data.model.GameSettings
import com.techgv.tictactoe.data.repository.SettingsRepository
import com.techgv.tictactoe.data.repository.StatsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository = SettingsRepository(application)
    private val statsRepository = StatsRepository(application)

    val settings: StateFlow<GameSettings> = settingsRepository.settings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GameSettings()
    )

    fun updateSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateSoundEnabled(enabled)
        }
    }

    fun updateHapticEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateHapticEnabled(enabled)
        }
    }

    fun updatePlayerXName(name: String) {
        viewModelScope.launch {
            settingsRepository.updatePlayerXName(name)
        }
    }

    fun updatePlayerOName(name: String) {
        viewModelScope.launch {
            settingsRepository.updatePlayerOName(name)
        }
    }

    fun resetAllData() {
        viewModelScope.launch {
            statsRepository.resetStats()
            settingsRepository.resetSettings()
        }
    }
}
