package com.techgv.tictactoe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techgv.tictactoe.ui.theme.BadgeShape
import com.techgv.tictactoe.ui.theme.CardBackground
import com.techgv.tictactoe.ui.theme.CardShape
import com.techgv.tictactoe.ui.theme.DarkGreen500
import com.techgv.tictactoe.ui.theme.GreenAccent
import com.techgv.tictactoe.ui.theme.TextMuted
import com.techgv.tictactoe.ui.theme.TextSecondary

@Composable
fun ModeSelectionCard(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    isSelected: Boolean,
    isEnabled: Boolean,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardShape)
            .background(CardBackground)
            .then(
                if (isSelected && isEnabled) {
                    Modifier.border(
                        width = 2.dp,
                        color = GreenAccent,
                        shape = CardShape
                    )
                } else {
                    Modifier.border(
                        width = 1.dp,
                        color = DarkGreen500.copy(alpha = 0.5f),
                        shape = CardShape
                    )
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = isEnabled,
                onClick = onStartClick
            )
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(if (isEnabled) GreenAccent else DarkGreen500.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }

                // Badge or Start button
                if (!isEnabled) {
                    ComingSoonBadge()
                } else if (isSelected) {
                    StartButton(onClick = onStartClick)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (isEnabled) Color.White else TextMuted
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isEnabled) TextSecondary else TextMuted
            )
        }
    }
}

@Composable
private fun ComingSoonBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(BadgeShape)
            .background(TextMuted.copy(alpha = 0.3f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = "COMING SOON",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = TextMuted
        )
    }
}

@Composable
private fun StartButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = GreenAccent,
            contentColor = Color.Black
        )
    ) {
        Text(
            text = "START",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun PlayerVsPlayerIcon(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy((-8).dp)
    ) {
        // Two person icons overlapping
        PersonIcon(tint = CardBackground)
        PersonIcon(tint = CardBackground)
    }
}

@Composable
fun PlayerVsAIIcon(modifier: Modifier = Modifier) {
    // Robot-like icon
    Box(
        modifier = modifier.size(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "🤖",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun PersonIcon(
    tint: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Head
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(tint)
        )
        Spacer(modifier = Modifier.height(2.dp))
        // Body
        Box(
            modifier = Modifier
                .size(width = 14.dp, height = 8.dp)
                .clip(RoundedCornerShape(topStart = 7.dp, topEnd = 7.dp))
                .background(tint)
        )
    }
}
