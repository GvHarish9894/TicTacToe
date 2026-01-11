package com.techgv.tictactoe.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techgv.tictactoe.data.model.HumanSymbol
import com.techgv.tictactoe.data.model.Player
import com.techgv.tictactoe.ui.screens.gamemode.GameMode
import com.techgv.tictactoe.ui.theme.TextSecondary
import com.techgv.tictactoe.util.PlayerDisplayNames

@Composable
fun ScoreBoard(
    scoreX: Int,
    scoreO: Int,
    modifier: Modifier = Modifier,
    gameMode: GameMode = GameMode.PLAYER_VS_PLAYER,
    humanSymbol: HumanSymbol = HumanSymbol.X,
    playerXName: String = "Player X",
    playerOName: String = "Player O"
) {
    // Get display names based on game mode
    val displayNameX = PlayerDisplayNames.getDisplayName(
        Player.X, gameMode, humanSymbol, playerXName, playerOName
    )
    val displayNameO = PlayerDisplayNames.getDisplayName(
        Player.O, gameMode, humanSymbol, playerXName, playerOName
    )

    Row(
        modifier = modifier.padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Player X score
        ScoreColumn(
            score = scoreX,
            label = displayNameX.uppercase(),
            player = Player.X
        )

        // Divider
        VerticalDivider(
            modifier = Modifier
                .height(40.dp)
                .padding(horizontal = 24.dp),
            thickness = 1.dp,
            color = TextSecondary.copy(alpha = 0.3f)
        )

        // Player O score
        ScoreColumn(
            score = scoreO,
            label = displayNameO.uppercase(),
            player = Player.O
        )
    }
}

@Composable
private fun ScoreColumn(
    score: Int,
    label: String,
    player: Player,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = score.toString(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            color = player.color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = TextSecondary
        )
    }
}
