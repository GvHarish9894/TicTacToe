package com.techgv.tictactoe.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.techgv.tictactoe.domain.GameLogic
import com.techgv.tictactoe.domain.WinLineType

@Composable
fun WinLine(
    winningLine: List<Int>,
    color: Color,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(winningLine) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500)
        )
    }

    val winLineType = GameLogic.getWinLineType(winningLine)

    Canvas(modifier = modifier) {
        val cellWidth = size.width / 3
        val cellHeight = size.height / 3
        val padding = cellWidth * 0.15f
        val strokeWidth = 8f

        val (startOffset, endOffset) = when (winLineType) {
            WinLineType.ROW_TOP -> {
                Offset(padding, cellHeight / 2) to
                        Offset(size.width - padding, cellHeight / 2)
            }
            WinLineType.ROW_MIDDLE -> {
                Offset(padding, size.height / 2) to
                        Offset(size.width - padding, size.height / 2)
            }
            WinLineType.ROW_BOTTOM -> {
                Offset(padding, size.height - cellHeight / 2) to
                        Offset(size.width - padding, size.height - cellHeight / 2)
            }
            WinLineType.COLUMN_LEFT -> {
                Offset(cellWidth / 2, padding) to
                        Offset(cellWidth / 2, size.height - padding)
            }
            WinLineType.COLUMN_CENTER -> {
                Offset(size.width / 2, padding) to
                        Offset(size.width / 2, size.height - padding)
            }
            WinLineType.COLUMN_RIGHT -> {
                Offset(size.width - cellWidth / 2, padding) to
                        Offset(size.width - cellWidth / 2, size.height - padding)
            }
            WinLineType.DIAGONAL_MAIN -> {
                Offset(padding, padding) to
                        Offset(size.width - padding, size.height - padding)
            }
            WinLineType.DIAGONAL_ANTI -> {
                Offset(size.width - padding, padding) to
                        Offset(padding, size.height - padding)
            }
        }

        // Calculate current end position based on animation progress
        val currentEnd = Offset(
            x = startOffset.x + (endOffset.x - startOffset.x) * progress.value,
            y = startOffset.y + (endOffset.y - startOffset.y) * progress.value
        )

        drawLine(
            color = color.copy(alpha = 0.8f),
            start = startOffset,
            end = currentEnd,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}
