package com.techgv.tictactoe.ui.screens.gamemode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.techgv.tictactoe.R
import com.techgv.tictactoe.ui.components.ModeSelectionCard
import com.techgv.tictactoe.ui.components.PlayerVsAIIcon
import com.techgv.tictactoe.ui.components.PlayerVsPlayerIcon
import com.techgv.tictactoe.ui.theme.DarkGreen800
import com.techgv.tictactoe.ui.theme.DarkGreen900
import com.techgv.tictactoe.ui.theme.TextSecondary

enum class GameMode {
    PLAYER_VS_PLAYER,
    PLAYER_VS_AI
}

@Composable
fun GameModeScreen(
    onStartGame: () -> Unit
) {
    var selectedMode by remember { mutableStateOf(GameMode.PLAYER_VS_PLAYER) }

    Scaffold(containerColor = Color.Transparent) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(DarkGreen900, DarkGreen800)
                    )
                )
                .padding(paddingValues)
                .statusBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = stringResource(R.string.choose_game_mode),
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = stringResource(R.string.select_opponent),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Mode selection cards
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Player vs Player
                ModeSelectionCard(
                    title = stringResource(R.string.pvp_title),
                    subtitle = stringResource(R.string.pvp_subtitle),
                    icon = { PlayerVsPlayerIcon() },
                    isSelected = selectedMode == GameMode.PLAYER_VS_PLAYER,
                    isEnabled = true,
                    onStartClick = {
                        selectedMode = GameMode.PLAYER_VS_PLAYER
                        onStartGame()
                    }
                )

                // Player vs AI
                ModeSelectionCard(
                    title = stringResource(R.string.pva_title),
                    subtitle = stringResource(R.string.pva_subtitle),
                    icon = { PlayerVsAIIcon() },
                    isSelected = selectedMode == GameMode.PLAYER_VS_AI,
                    isEnabled = false,
                    onStartClick = {
                        // Coming soon - disabled
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
