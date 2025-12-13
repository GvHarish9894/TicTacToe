package com.techgv.tictactoe.ui.screens.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.techgv.tictactoe.data.model.GameResult
import com.techgv.tictactoe.data.model.GameSettings
import com.techgv.tictactoe.data.model.GameState
import com.techgv.tictactoe.data.model.GameStats
import com.techgv.tictactoe.data.model.Player
import com.techgv.tictactoe.data.repository.SettingsRepository
import com.techgv.tictactoe.data.repository.StatsRepository
import com.techgv.tictactoe.domain.GameLogic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val statsRepository = StatsRepository(application)
    private val settingsRepository = SettingsRepository(application)

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    val stats: StateFlow<GameStats> = statsRepository.gameStats.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GameStats()
    )

    val settings: StateFlow<GameSettings> = settingsRepository.settings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GameSettings()
    )

    fun onCellClick(index: Int) {
        val currentState = _gameState.value

        // Ignore clicks if game is over or cell is occupied
        if (currentState.isGameOver || !GameLogic.isValidMove(currentState.board, index)) {
            return
        }

        // Make the move
        val newBoard = GameLogic.makeMove(currentState.board, index, currentState.currentPlayer)
        val gameResult = GameLogic.checkGameResult(newBoard)

        // Calculate duration if game is won
        val duration = if (gameResult is GameResult.Win) {
            (System.currentTimeMillis() - currentState.gameStartTime) / 1000
        } else {
            null
        }

        // Update scores if there's a winner
        val (newScoreX, newScoreO) = when (gameResult) {
            is GameResult.Win -> when (gameResult.winner) {
                Player.X -> currentState.scoreX + 1 to currentState.scoreO
                Player.O -> currentState.scoreX to currentState.scoreO + 1
                Player.NONE -> currentState.scoreX to currentState.scoreO
            }
            else -> currentState.scoreX to currentState.scoreO
        }

        _gameState.update {
            it.copy(
                board = newBoard,
                currentPlayer = if (gameResult is GameResult.InProgress) {
                    currentState.currentPlayer.opposite()
                } else {
                    currentState.currentPlayer
                },
                scoreX = newScoreX,
                scoreO = newScoreO,
                gameResult = gameResult,
                lastWinDuration = duration
            )
        }

        // Record stats
        when (gameResult) {
            is GameResult.Win -> {
                viewModelScope.launch {
                    statsRepository.recordWin(
                        winner = gameResult.winner,
                        durationSeconds = duration ?: 0,
                        currentStats = stats.value
                    )
                }
            }
            is GameResult.Draw -> {
                viewModelScope.launch {
                    statsRepository.recordDraw(stats.value)
                }
            }
            else -> { /* Game still in progress */ }
        }
    }

    fun resetGame() {
        _gameState.update {
            it.copy(
                board = GameLogic.emptyBoard(),
                currentPlayer = Player.X,
                gameResult = GameResult.InProgress,
                gameStartTime = System.currentTimeMillis(),
                lastWinDuration = null
            )
        }
    }

    fun resetAll() {
        _gameState.value = GameState()
    }
}
