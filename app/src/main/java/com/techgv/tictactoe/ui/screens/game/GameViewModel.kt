package com.techgv.tictactoe.ui.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techgv.tictactoe.data.model.AIDifficulty
import com.techgv.tictactoe.data.model.FirstPlayer
import com.techgv.tictactoe.data.model.GameResult
import com.techgv.tictactoe.data.model.GameSettings
import com.techgv.tictactoe.data.model.Player
import com.techgv.tictactoe.data.repository.SettingsRepository
import com.techgv.tictactoe.domain.GameLogic
import com.techgv.tictactoe.domain.ai.AIStrategy
import com.techgv.tictactoe.domain.ai.AIStrategyFactory
import com.techgv.tictactoe.ui.screens.gamemode.GameMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(
    settingsRepository: SettingsRepository,
    aiDifficulty: AIDifficulty? = null,
    private val gameMode: GameMode = GameMode.PLAYER_VS_PLAYER,
    private val firstPlayer: FirstPlayer = FirstPlayer.HUMAN
) : ViewModel() {

    // Combined UI state
    private val _uiState = MutableStateFlow(GameScreenViewState())
    val uiState: StateFlow<GameScreenViewState> = _uiState.asStateFlow()

    // Event to signal AI made a move (for sound/haptic feedback)
    private val _aiMoveEvent = MutableSharedFlow<Unit>()
    val aiMoveEvent: SharedFlow<Unit> = _aiMoveEvent.asSharedFlow()

    val settings: StateFlow<GameSettings> =
        settingsRepository.settings.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GameSettings(),
        )

    // AI configuration
    private val aiStrategy: AIStrategy? = aiDifficulty?.let { AIStrategyFactory.create(it) }
    private val aiPlayer = Player.O // AI always plays as O

    init {
        // If AI goes first, trigger AI move
        if (gameMode == GameMode.PLAYER_VS_AI && firstPlayer == FirstPlayer.AI) {
            triggerAIMove()
        }
    }

    fun onCellClick(index: Int) {
        val currentGameState = _uiState.value.gameState
        val isAITurn =
            gameMode == GameMode.PLAYER_VS_AI && currentGameState.currentPlayer == aiPlayer
        val isClickIgnored =
            currentGameState.isGameOver ||
                !GameLogic.isValidMove(currentGameState.board, index)

        // Ignore clicks if:
        // - Game is over
        // - Cell is occupied
        // - AI is thinking
        // - It's AI's turn (in AI mode)
        if (isClickIgnored || _uiState.value.isAIThinking || isAITurn) {
            return
        }

        // Make the human move
        makeMove(index, currentGameState.currentPlayer)

        // Trigger AI response if game is still in progress
        val newGameState = _uiState.value.gameState
        if (gameMode == GameMode.PLAYER_VS_AI && newGameState.gameResult is GameResult.InProgress) {
            triggerAIMove()
        }
    }

    private fun makeMove(index: Int, player: Player) {
        val currentGameState = _uiState.value.gameState
        val newBoard = GameLogic.makeMove(currentGameState.board, index, player)
        val gameResult = GameLogic.checkGameResult(newBoard)

        // Calculate duration if game is won
        val duration = if (gameResult is GameResult.Win) {
            (System.currentTimeMillis() - currentGameState.gameStartTime) / 1000
        } else {
            null
        }

        // Update scores if there's a winner
        val (newScoreX, newScoreO) = when (gameResult) {
            is GameResult.Win -> when (gameResult.winner) {
                Player.X -> currentGameState.scoreX + 1 to currentGameState.scoreO
                Player.O -> currentGameState.scoreX to currentGameState.scoreO + 1
                Player.NONE -> currentGameState.scoreX to currentGameState.scoreO
            }
            else -> currentGameState.scoreX to currentGameState.scoreO
        }

        _uiState.update { state ->
            state.copy(
                gameState = state.gameState.copy(
                    board = newBoard,
                    currentPlayer = if (gameResult is GameResult.InProgress) {
                        player.opposite()
                    } else {
                        player
                    },
                    scoreX = newScoreX,
                    scoreO = newScoreO,
                    gameResult = gameResult,
                    lastWinDuration = duration
                )
            )
        }
    }

    private fun triggerAIMove() {
        viewModelScope.launch {
            _uiState.update { it.copy(isAIThinking = true) }

            // Add delay for natural feel (300-500ms)
            delay(Random.nextLong(300, 500))

            val currentGameState = _uiState.value.gameState
            if (currentGameState.gameResult is GameResult.InProgress) {
                aiStrategy?.let { strategy ->
                    val aiMove = strategy.findBestMove(currentGameState.board, aiPlayer)
                    if (aiMove >= 0) {
                        makeMove(aiMove, aiPlayer)
                        // Emit event for sound/haptic feedback
                        _aiMoveEvent.emit(Unit)
                    }
                }
            }

            _uiState.update { it.copy(isAIThinking = false) }
        }
    }

    fun setShowResultDialog(show: Boolean) {
        _uiState.update { it.copy(showResultDialog = show) }
    }

    fun resetGame() {
        _uiState.update { state ->
            state.copy(
                gameState = state.gameState.copy(
                    board = GameLogic.emptyBoard(),
                    currentPlayer = Player.X,
                    gameResult = GameResult.InProgress,
                    gameStartTime = System.currentTimeMillis(),
                    lastWinDuration = null
                ),
                showResultDialog = false
            )
        }

        // If AI goes first, trigger AI move after reset
        if (gameMode == GameMode.PLAYER_VS_AI && firstPlayer == FirstPlayer.AI) {
            triggerAIMove()
        }
    }
}
