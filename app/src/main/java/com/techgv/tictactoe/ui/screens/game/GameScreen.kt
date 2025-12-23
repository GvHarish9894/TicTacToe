package com.techgv.tictactoe.ui.screens.game

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.techgv.tictactoe.R
import com.techgv.tictactoe.data.model.AIDifficulty
import com.techgv.tictactoe.data.model.FirstPlayer
import com.techgv.tictactoe.data.model.GameResult
import com.techgv.tictactoe.ui.components.GameBoard
import com.techgv.tictactoe.ui.components.ResultDialog
import com.techgv.tictactoe.ui.components.ScoreBoard
import com.techgv.tictactoe.ui.components.TurnIndicator
import com.techgv.tictactoe.ui.screens.gamemode.GameMode
import com.techgv.tictactoe.ui.theme.ButtonShape
import com.techgv.tictactoe.ui.theme.DarkGreen800
import com.techgv.tictactoe.ui.theme.DarkGreen900
import com.techgv.tictactoe.ui.theme.GreenAccent
import com.techgv.tictactoe.ui.theme.TextSecondary
import com.techgv.tictactoe.util.SoundManager
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    gameMode: GameMode = GameMode.PLAYER_VS_PLAYER,
    aiDifficulty: AIDifficulty? = null,
    firstPlayer: FirstPlayer = FirstPlayer.HUMAN,
    onNavigateBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: GameViewModel = koinViewModel { parametersOf(gameMode, aiDifficulty, firstPlayer) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val settings by viewModel.settings.collectAsState()

    // Destructure for convenience
    val gameState = uiState.gameState
    val isAIThinking = uiState.isAIThinking
    val showResultDialog = uiState.showResultDialog

    // Initialize SoundManager for click sounds
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }
    val hapticFeedback = LocalHapticFeedback.current

    // Clean up SoundManager when composable leaves composition
    DisposableEffect(Unit) {
        onDispose {
            soundManager.release()
        }
    }

    // Listen for AI move events to trigger sound/haptic feedback
    LaunchedEffect(Unit) {
        viewModel.aiMoveEvent.collect {
            if (settings.soundEnabled) {
                soundManager.playClickSound()
            }
            if (settings.hapticEnabled) {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    }

    // Delay showing dialog for wins (wait for win line animation)
    LaunchedEffect(gameState.isGameOver, gameState.gameResult) {
        if (gameState.isGameOver) {
            when (gameState.gameResult) {
                is GameResult.Win -> {
                    delay(550) // Wait for 500ms win line animation + small buffer
                    viewModel.setShowResultDialog(true)
                }

                is GameResult.Draw -> {
                    viewModel.setShowResultDialog(true) // Show immediately for draws
                }

                else -> {
                    viewModel.setShowResultDialog(false)
                }
            }
        } else {
            viewModel.setShowResultDialog(false)
        }
    }

    // Handle system back button when dialog is not showing
    // (Dialog handles its own back press via onDismissRequest)
    BackHandler(enabled = !showResultDialog) {
        onNavigateBack()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = TextSecondary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings),
                            tint = TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.statusBarsPadding()
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(DarkGreen900, DarkGreen800)
                    )
                )
        ) {
            if (isLandscape) {
                // Landscape layout for tablets: Row with controls on left, game board on right
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left side: Turn indicator, Score board, Reset button
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TurnIndicator(
                            currentPlayer = gameState.currentPlayer,
                            playerXName = settings.playerXName,
                            playerOName = settings.playerOName
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        ScoreBoard(
                            scoreX = gameState.scoreX,
                            scoreO = gameState.scoreO,
                            playerXName = settings.playerXName,
                            playerOName = settings.playerOName
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = { viewModel.resetGame() },
                            modifier = Modifier
                                .widthIn(max = 200.dp)
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = ButtonShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GreenAccent,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = stringResource(R.string.reset_game),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    // Right side: Game board (constrained size)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        GameBoard(
                            board = gameState.board,
                            winningLine = gameState.winningLine,
                            onCellClick = { index -> viewModel.onCellClick(index) },
                            enabled = !gameState.isGameOver && !isAIThinking,
                            hapticEnabled = settings.hapticEnabled,
                            soundEnabled = settings.soundEnabled,
                            onPlaySound = { soundManager.playClickSound() },
                            modifier = Modifier.widthIn(max = 400.dp)
                        )
                    }
                }
            } else {
                // Portrait layout: Current vertical Column layout
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TurnIndicator(
                        currentPlayer = gameState.currentPlayer,
                        playerXName = settings.playerXName,
                        playerOName = settings.playerOName
                    )

                    ScoreBoard(
                        scoreX = gameState.scoreX,
                        scoreO = gameState.scoreO,
                        playerXName = settings.playerXName,
                        playerOName = settings.playerOName
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    GameBoard(
                        board = gameState.board,
                        winningLine = gameState.winningLine,
                        onCellClick = { index -> viewModel.onCellClick(index) },
                        enabled = !gameState.isGameOver && !isAIThinking,
                        hapticEnabled = settings.hapticEnabled,
                        soundEnabled = settings.soundEnabled,
                        onPlaySound = { soundManager.playClickSound() },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { viewModel.resetGame() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = ButtonShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenAccent,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = stringResource(R.string.reset_game),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Result dialog overlay
            ResultDialog(
                visible = showResultDialog,
                gameResult = gameState.gameResult,
                winDurationSeconds = gameState.lastWinDuration,
                onDismiss = { viewModel.setShowResultDialog(false) },
                onPlayAgain = { viewModel.resetGame() },
                onExitGame = {
                    viewModel.setShowResultDialog(false)
                    onNavigateBack()
                },
                playerXName = settings.playerXName,
                playerOName = settings.playerOName
            )
        }
    }
}
