package com.techgv.tictactoe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.techgv.tictactoe.R
import com.techgv.tictactoe.data.model.AIDifficulty
import com.techgv.tictactoe.data.model.FirstPlayer
import com.techgv.tictactoe.data.model.HumanSymbol
import com.techgv.tictactoe.ui.theme.ButtonShape
import com.techgv.tictactoe.ui.theme.CardBackground
import com.techgv.tictactoe.ui.theme.DarkGreen500
import com.techgv.tictactoe.ui.theme.DialogShape
import com.techgv.tictactoe.ui.theme.GreenAccent
import com.techgv.tictactoe.ui.theme.TextSecondary

@Composable
fun AIConfigDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onStartGame: (difficulty: AIDifficulty, firstPlayer: FirstPlayer, humanSymbol: HumanSymbol) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDifficulty by remember { mutableStateOf(AIDifficulty.MEDIUM) }
    var selectedFirstPlayer by remember { mutableStateOf(FirstPlayer.HUMAN) }
    var selectedSymbol by remember { mutableStateOf(HumanSymbol.X) }

    if (visible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                modifier = modifier
                    .widthIn(max = 400.dp)
                    .fillMaxWidth(0.9f)
                    .clip(DialogShape)
                    .background(CardBackground)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with close button
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.ai_config_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close),
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Difficulty Section
                Text(
                    text = stringResource(R.string.select_difficulty),
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AIDifficulty.entries.forEach { difficulty ->
                        SelectionChip(
                            text = difficulty.displayName,
                            isSelected = selectedDifficulty == difficulty,
                            onClick = { selectedDifficulty = difficulty },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // First Player Section
                Text(
                    text = stringResource(R.string.who_goes_first),
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FirstPlayer.entries.forEach { player ->
                        SelectionChip(
                            text = player.displayName,
                            isSelected = selectedFirstPlayer == player,
                            onClick = { selectedFirstPlayer = player },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Symbol Selection Section
                Text(
                    text = stringResource(R.string.choose_your_symbol),
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HumanSymbol.entries.forEach { symbol ->
                        SelectionChip(
                            text = symbol.displayName,
                            isSelected = selectedSymbol == symbol,
                            onClick = { selectedSymbol = symbol },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Start Button
                Button(
                    onClick = {
                        onStartGame(
                            selectedDifficulty,
                            selectedFirstPlayer,
                            selectedSymbol
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = ButtonShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenAccent,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = stringResource(R.string.start_game),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectionChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) GreenAccent else CardBackground)
            .border(
                width = 1.dp,
                color = if (isSelected) GreenAccent else DarkGreen500,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) Color.Black else Color.White,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
