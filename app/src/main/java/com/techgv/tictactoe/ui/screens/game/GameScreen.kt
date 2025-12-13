package com.techgv.tictactoe.ui.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.techgv.tictactoe.data.model.GameResult
import com.techgv.tictactoe.ui.components.BottomNavBar
import com.techgv.tictactoe.ui.components.GameBoard
import com.techgv.tictactoe.ui.components.NavItem
import com.techgv.tictactoe.ui.components.ResultDialog
import com.techgv.tictactoe.ui.components.ScoreBoard
import com.techgv.tictactoe.ui.components.TurnIndicator
import com.techgv.tictactoe.ui.theme.ButtonShape
import com.techgv.tictactoe.ui.theme.DarkGreen800
import com.techgv.tictactoe.ui.theme.DarkGreen900
import com.techgv.tictactoe.ui.theme.GreenAccent
import com.techgv.tictactoe.ui.theme.TextSecondary
import com.techgv.tictactoe.util.SoundManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    onNavigateBack: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: GameViewModel = viewModel()
) {
    val gameState by viewModel.gameState.collectAsState()
    val settings by viewModel.settings.collectAsState()

    // Initialize SoundManager for click sounds
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }

    // Clean up SoundManager when composable leaves composition
    DisposableEffect(Unit) {
        onDispose {
            soundManager.release()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Tic Tac Toe",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextSecondary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Menu options */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
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
        bottomBar = {
            BottomNavBar(
                selectedItem = NavItem.HOME,
                onItemSelected = { item ->
                    when (item) {
                        NavItem.HOME -> onNavigateToHome()
                        NavItem.STATS -> onNavigateToStats()
                        NavItem.SETTINGS -> onNavigateToSettings()
                    }
                }
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(DarkGreen900, DarkGreen800)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Turn indicator
                TurnIndicator(
                    currentPlayer = gameState.currentPlayer,
                    playerXName = settings.playerXName,
                    playerOName = settings.playerOName
                )

                // Score board
                ScoreBoard(
                    scoreX = gameState.scoreX,
                    scoreO = gameState.scoreO,
                    playerXName = settings.playerXName,
                    playerOName = settings.playerOName
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Game board
                GameBoard(
                    board = gameState.board,
                    winningLine = gameState.winningLine,
                    onCellClick = { index -> viewModel.onCellClick(index) },
                    enabled = !gameState.isGameOver,
                    hapticEnabled = settings.hapticEnabled,
                    soundEnabled = settings.soundEnabled,
                    onPlaySound = { soundManager.playClickSound() },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))

                // Reset button
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
                        text = "Reset Game",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Result dialog overlay
            ResultDialog(
                visible = gameState.isGameOver,
                gameResult = gameState.gameResult,
                winDurationSeconds = gameState.lastWinDuration,
                onPlayAgain = { viewModel.resetGame() },
                onExitGame = onNavigateBack,
                playerXName = settings.playerXName,
                playerOName = settings.playerOName
            )
        }
    }
}
