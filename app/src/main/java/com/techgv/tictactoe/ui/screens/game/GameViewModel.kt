package com.techgv.tictactoe.ui.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.perf.metrics.Trace
import com.techgv.tictactoe.analytics.AnalyticsHelper
import com.techgv.tictactoe.analytics.PerformanceHelper
import com.techgv.tictactoe.data.model.AIDifficulty
import com.techgv.tictactoe.data.model.FirstPlayer
import com.techgv.tictactoe.data.model.GameResult
import com.techgv.tictactoe.data.model.GameSettings
import com.techgv.tictactoe.data.model.HumanSymbol
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
    private val analyticsHelper: AnalyticsHelper,
    private val performanceHelper: PerformanceHelper,
    private val aiDifficulty: AIDifficulty? = null,
    private val gameMode: GameMode = GameMode.PLAYER_VS_PLAYER,
    private val firstPlayer: FirstPlayer = FirstPlayer.HUMAN,
    private val humanSymbol: HumanSymbol = HumanSymbol.X
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

    // Compute human and AI players based on symbol choice
    private val humanPlayer: Player = humanSymbol.symbol
    private val aiPlayer: Player = humanSymbol.symbol.opposite()

    // Performance trace for game duration
    private var gameTrace: Trace? = null

    init {
        // Determine starting player based on who goes first and their symbols
        val startingPlayer = when {
            gameMode == GameMode.PLAYER_VS_PLAYER -> Player.X
            firstPlayer == FirstPlayer.HUMAN -> humanPlayer
            else -> aiPlayer
        }

        // Initialize game state with correct starting player
        _uiState.update { state ->
            state.copy(
                gameState = state.gameState.copy(
                    currentPlayer = startingPlayer
                )
            )
        }

        // Log game started event
        analyticsHelper.logGameStarted(gameMode, aiDifficulty, firstPlayer)

        // Start performance trace
        gameTrace = performanceHelper.startGameTrace()

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

        // Log invalid move attempt
        if (!currentGameState.isGameOver &&
            !GameLogic.isValidMove(currentGameState.board, index)
        ) {
            analyticsHelper.logInvalidMoveAttempt(index, currentGameState.currentPlayer)
        }

        // Ignore clicks if:
        // - Game is over
        // - Cell is occupied
        // - AI is thinking
        // - It's AI's turn (in AI mode)
        if (isClickIgnored || _uiState.value.isAIThinking || isAITurn) {
            return
        }

        // Make the human move
        makeMove(index, currentGameState.currentPlayer, isAIMove = false)

        // Trigger AI response if game is still in progress
        val newGameState = _uiState.value.gameState
        if (gameMode == GameMode.PLAYER_VS_AI && newGameState.gameResult is GameResult.InProgress) {
            triggerAIMove()
        }
    }

    private fun makeMove(index: Int, player: Player, isAIMove: Boolean = false) {
        val currentGameState = _uiState.value.gameState
        val moveNumber = currentGameState.board.count { it != Player.NONE } + 1

        // Log player move
        if (!isAIMove) {
            analyticsHelper.logPlayerMove(
                cellIndex = index,
                player = player,
                moveNumber = moveNumber
            )
        }

        val newBoard = GameLogic.makeMove(currentGameState.board, index, player)
        val gameResult = GameLogic.checkGameResult(newBoard)

        // Calculate duration if game is over
        val duration = if (gameResult is GameResult.Win || gameResult is GameResult.Draw) {
            (System.currentTimeMillis() - currentGameState.gameStartTime) / 1000
        } else {
            null
        }

        // Log game result
        when (gameResult) {
            is GameResult.Win -> {
                analyticsHelper.logGameWon(
                    winner = gameResult.winner,
                    durationSeconds = duration ?: 0,
                    winningLine = gameResult.winningLine,
                    moveCount = moveNumber,
                    scoreX = if (gameResult.winner == Player.X) {
                        currentGameState.scoreX + 1
                    } else {
                        currentGameState.scoreX
                    },
                    scoreO = if (gameResult.winner == Player.O) {
                        currentGameState.scoreO + 1
                    } else {
                        currentGameState.scoreO
                    }
                )
                // Stop performance trace
                gameTrace?.let { trace ->
                    performanceHelper.stopGameTrace(
                        trace = trace,
                        winner = gameResult.winner.name,
                        moveCount = moveNumber
                    )
                }
                gameTrace = null
            }

            is GameResult.Draw -> {
                analyticsHelper.logGameDraw(
                    durationSeconds = duration ?: 0,
                    moveCount = moveNumber,
                    scoreX = currentGameState.scoreX,
                    scoreO = currentGameState.scoreO
                )
                // Stop performance trace
                gameTrace?.stop()
                gameTrace = null
            }

            else -> { /* Game in progress */
            }
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

            val startTime = System.currentTimeMillis()

            // Add delay for natural feel (300-500ms)
            delay(Random.nextLong(300, 500))

            val currentGameState = _uiState.value.gameState
            if (currentGameState.gameResult is GameResult.InProgress) {
                aiStrategy?.let { strategy ->
                    val moveNumber = currentGameState.board.count { it != Player.NONE } + 1

                    // Trace AI move calculation with performance monitoring
                    val aiMove = if (aiDifficulty != null) {
                        performanceHelper.traceAIMove(aiDifficulty.name.lowercase()) {
                            strategy.findBestMove(currentGameState.board, aiPlayer)
                        }
                    } else {
                        strategy.findBestMove(currentGameState.board, aiPlayer)
                    }

                    if (aiMove >= 0) {
                        val thinkingTime = System.currentTimeMillis() - startTime

                        // Log AI move
                        aiDifficulty?.let { difficulty ->
                            analyticsHelper.logAIMove(
                                difficulty = difficulty,
                                cellIndex = aiMove,
                                thinkingTimeMs = thinkingTime,
                                moveNumber = moveNumber
                            )
                        }

                        makeMove(aiMove, aiPlayer, isAIMove = true)
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
        val currentState = _uiState.value.gameState

        // Log reset event
        analyticsHelper.logGameReset(
            scoreX = currentState.scoreX,
            scoreO = currentState.scoreO
        )

        // Stop any active trace
        gameTrace?.stop()
        gameTrace = null

        // Start new game trace
        gameTrace = performanceHelper.startGameTrace()

        // Determine starting player based on who goes first and their symbols
        val startingPlayer = when {
            gameMode == GameMode.PLAYER_VS_PLAYER -> Player.X
            firstPlayer == FirstPlayer.HUMAN -> humanPlayer
            else -> aiPlayer
        }

        _uiState.update { state ->
            state.copy(
                gameState = state.gameState.copy(
                    board = GameLogic.emptyBoard(),
                    currentPlayer = startingPlayer,
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

    override fun onCleared() {
        super.onCleared()
        // Clean up any active traces
        gameTrace?.stop()
        gameTrace = null
    }
}
