package com.techgv.tictactoe.ui.components

import android.content.Context
import android.os.Build
import android.os.Vibrator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.techgv.tictactoe.data.model.Player
import com.techgv.tictactoe.ui.theme.CellBackground
import com.techgv.tictactoe.ui.theme.CellBorder
import com.techgv.tictactoe.ui.theme.CoralAccent
import com.techgv.tictactoe.ui.theme.GameCellShape
import com.techgv.tictactoe.ui.theme.GreenAccent

@Composable
fun GameCell(
    player: Player,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isWinningCell: Boolean = false,
    hapticEnabled: Boolean = false,
    soundEnabled: Boolean = false,
    onPlaySound: () -> Unit = {},
) {
    val scale by animateFloatAsState(
        targetValue = if (isWinningCell) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "cell_scale"
    )

    val interactionSource = remember { MutableInteractionSource() }
    val hapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
            .clip(GameCellShape)
            .background(CellBackground)
            .border(
                width = 1.dp,
                color = if (isWinningCell) player.color.copy(alpha = 0.5f) else CellBorder,
                shape = GameCellShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled && player == Player.NONE,
                onClick = {
                    if (soundEnabled) {
                        onPlaySound()
                    }
                    if (hapticEnabled) {
                        performHapticFeedback(context, hapticFeedback)
                    }
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = player != Player.NONE,
            enter = scaleIn(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn()
        ) {
            when (player) {
                Player.X -> XMark(
                    color = GreenAccent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                )
                Player.O -> OMark(
                    color = CoralAccent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                )
                Player.NONE -> { /* Empty cell */ }
            }
        }
    }
}

@Composable
fun XMark(
    color: Color,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 12f
) {
    Box(
        modifier = modifier.drawBehind {
            val padding = size.width * 0.1f
            // Draw first line of X (top-left to bottom-right)
            drawLine(
                color = color,
                start = Offset(padding, padding),
                end = Offset(size.width - padding, size.height - padding),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            // Draw second line of X (top-right to bottom-left)
            drawLine(
                color = color,
                start = Offset(size.width - padding, padding),
                end = Offset(padding, size.height - padding),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    )
}

@Composable
fun OMark(
    color: Color,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 12f
) {
    Box(
        modifier = modifier.drawBehind {
            val padding = size.width * 0.1f
            val radius = (size.width - padding * 2) / 2
            drawCircle(
                color = color,
                radius = radius,
                center = Offset(size.width / 2, size.height / 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    )
}

/**
 * Performs haptic feedback with version-specific fallbacks.
 * - API 31+: Uses VibratorManager with VibrationEffect
 * - API 26-30: Uses Vibrator with VibrationEffect
 * - API 24-25: Uses deprecated Vibrator.vibrate(long)
 */
private fun performHapticFeedback(
    context: Context,
    hapticFeedback: androidx.compose.ui.hapticfeedback.HapticFeedback
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // API 26+ - Use Compose's HapticFeedback (most reliable on modern devices)
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
    } else {
        // API 24-25 fallback - Use Vibrator directly
        @Suppress("DEPRECATION")
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.let {
            if (it.hasVibrator()) {
                @Suppress("DEPRECATION")
                it.vibrate(50L) // 50ms vibration
            }
        }
    }
}
