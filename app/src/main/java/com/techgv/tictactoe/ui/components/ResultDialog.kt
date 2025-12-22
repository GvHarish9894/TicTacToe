package com.techgv.tictactoe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.techgv.tictactoe.R
import com.techgv.tictactoe.data.model.GameResult
import com.techgv.tictactoe.data.model.Player
import com.techgv.tictactoe.ui.theme.ButtonShape
import com.techgv.tictactoe.ui.theme.CardBackground
import com.techgv.tictactoe.ui.theme.DialogShape
import com.techgv.tictactoe.ui.theme.GreenAccent
import com.techgv.tictactoe.ui.theme.TextSecondary

@Composable
fun ResultDialog(
    visible: Boolean,
    gameResult: GameResult,
    winDurationSeconds: Long?,
    onDismiss: () -> Unit,
    onPlayAgain: () -> Unit,
    onExitGame: () -> Unit,
    modifier: Modifier = Modifier,
    playerXName: String = "Player X",
    playerOName: String = "Player O"
) {
    if (visible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            ResultCard(
                gameResult = gameResult,
                winDurationSeconds = winDurationSeconds,
                onPlayAgain = onPlayAgain,
                onExitGame = onExitGame,
                playerXName = playerXName,
                playerOName = playerOName,
                modifier = modifier
                    .widthIn(max = 400.dp)
                    .fillMaxWidth(0.9f)
            )
        }
    }
}

@Composable
private fun ResultCard(
    gameResult: GameResult,
    winDurationSeconds: Long?,
    onPlayAgain: () -> Unit,
    onExitGame: () -> Unit,
    playerXName: String,
    playerOName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(DialogShape)
            .background(CardBackground)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Trophy icon with glow background
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(GreenAccent.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (gameResult) {
                    is GameResult.Win -> "🏆"
                    is GameResult.Draw -> "🤝"
                    else -> ""
                },
                style = MaterialTheme.typography.displayMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Result title
        val winnerName = when (gameResult) {
            is GameResult.Win -> when (gameResult.winner) {
                Player.X -> playerXName
                Player.O -> playerOName
                Player.NONE -> ""
            }
            else -> ""
        }
        val title = when (gameResult) {
            is GameResult.Win -> stringResource(R.string.winner_title, winnerName)
            is GameResult.Draw -> stringResource(R.string.draw_title)
            else -> ""
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle with duration
        val subtitle = when (gameResult) {
            is GameResult.Win -> {
                if (winDurationSeconds != null) {
                    stringResource(R.string.win_subtitle_with_time, winDurationSeconds)
                } else {
                    stringResource(R.string.win_subtitle)
                }
            }
            is GameResult.Draw -> stringResource(R.string.draw_subtitle)
            else -> ""
        }
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Play Again button
        Button(
            onClick = onPlayAgain,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
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
                text = stringResource(R.string.play_again),
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Exit Game button
        OutlinedButton(
            onClick = onExitGame,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = ButtonShape,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(R.string.exit_game),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
