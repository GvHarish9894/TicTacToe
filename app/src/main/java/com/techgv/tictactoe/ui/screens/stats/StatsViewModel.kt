package com.techgv.tictactoe.ui.screens.stats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.techgv.tictactoe.data.model.GameStats
import com.techgv.tictactoe.data.repository.StatsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StatsViewModel(application: Application) : AndroidViewModel(application) {

    private val statsRepository = StatsRepository(application)

    val stats: StateFlow<GameStats> = statsRepository.gameStats.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GameStats()
    )

    fun resetStats() {
        viewModelScope.launch {
            statsRepository.resetStats()
        }
    }
}
