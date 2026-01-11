package com.techgv.tictactoe.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techgv.tictactoe.R
import com.techgv.tictactoe.data.model.HumanSymbol
import com.techgv.tictactoe.data.model.Player
import com.techgv.tictactoe.ui.screens.gamemode.GameMode
import com.techgv.tictactoe.ui.theme.CardBackground
import com.techgv.tictactoe.ui.theme.DarkGreen600
import com.techgv.tictactoe.ui.theme.TurnIndicatorShape
import com.techgv.tictactoe.util.PlayerDisplayNames

@Composable
fun TurnIndicator(
    currentPlayer: Player,
    modifier: Modifier = Modifier,
    gameMode: GameMode = GameMode.PLAYER_VS_PLAYER,
    humanSymbol: HumanSymbol = HumanSymbol.X,
    playerXName: String = "Player X",
    playerOName: String = "Player O"
) {
    val dotColor by animateColorAsState(
        targetValue = currentPlayer.color,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "dot_color"
    )

    // Get display name based on game mode
    val displayName = PlayerDisplayNames.getDisplayName(
        currentPlayer, gameMode, humanSymbol, playerXName, playerOName
    )

    Row(
        modifier = modifier
            .clip(TurnIndicatorShape)
            .background(CardBackground)
            .border(
                width = 1.dp,
                color = DarkGreen600,
                shape = TurnIndicatorShape
            )
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Animated dot
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(dotColor)
        )

        // Turn text
        Text(
            text = stringResource(R.string.turn_indicator, displayName.uppercase()),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = currentPlayer.color
        )
    }
}
