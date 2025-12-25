package com.techgv.tictactoe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.techgv.tictactoe.data.model.Player
import com.techgv.tictactoe.ui.theme.DarkGreen700

@Composable
fun GameBoard(
    board: List<Player>,
    winningLine: List<Int>?,
    onCellClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    hapticEnabled: Boolean = false,
    soundEnabled: Boolean = false,
    onPlaySound: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(DarkGreen700.copy(alpha = 0.5f))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (row in 0..2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (col in 0..2) {
                        val index = row * 3 + col
                        val isWinningCell = winningLine?.contains(index) == true

                        GameCell(
                            player = board[index],
                            onClick = { onCellClick(index) },
                            enabled = enabled,
                            isWinningCell = isWinningCell,
                            hapticEnabled = hapticEnabled,
                            soundEnabled = soundEnabled,
                            onPlaySound = onPlaySound,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Win line overlay
        if (winningLine != null && board[winningLine[0]] != Player.NONE) {
            WinLine(
                winningLine = winningLine,
                color = board[winningLine[0]].color,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
