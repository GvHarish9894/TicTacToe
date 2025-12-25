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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.techgv.tictactoe.domain.GameLogic
import com.techgv.tictactoe.domain.WinLineType

@Composable
fun WinLine(
    winningLine: List<Int>,
    color: Color,
    modifier: Modifier = Modifier,
    spacing: Dp = 8.dp
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
        val spacingPx = spacing.toPx()
        val cellWidth = (size.width - 2 * spacingPx) / 3
        val cellHeight = (size.height - 2 * spacingPx) / 3
        val linePadding = cellWidth * 0.15f
        val strokeWidth = 8f

        // Cell center positions accounting for spacing
        val col0Center = cellWidth / 2
        val col1Center = cellWidth + spacingPx + cellWidth / 2
        val col2Center = 2 * (cellWidth + spacingPx) + cellWidth / 2

        val row0Center = cellHeight / 2
        val row1Center = cellHeight + spacingPx + cellHeight / 2
        val row2Center = 2 * (cellHeight + spacingPx) + cellHeight / 2

        val (startOffset, endOffset) = when (winLineType) {
            WinLineType.ROW_TOP -> {
                Offset(linePadding, row0Center) to
                    Offset(size.width - linePadding, row0Center)
            }
            WinLineType.ROW_MIDDLE -> {
                Offset(linePadding, row1Center) to
                    Offset(size.width - linePadding, row1Center)
            }
            WinLineType.ROW_BOTTOM -> {
                Offset(linePadding, row2Center) to
                    Offset(size.width - linePadding, row2Center)
            }
            WinLineType.COLUMN_LEFT -> {
                Offset(col0Center, linePadding) to
                    Offset(col0Center, size.height - linePadding)
            }
            WinLineType.COLUMN_CENTER -> {
                Offset(col1Center, linePadding) to
                    Offset(col1Center, size.height - linePadding)
            }
            WinLineType.COLUMN_RIGHT -> {
                Offset(col2Center, linePadding) to
                    Offset(col2Center, size.height - linePadding)
            }
            WinLineType.DIAGONAL_MAIN -> {
                Offset(linePadding, linePadding) to
                    Offset(size.width - linePadding, size.height - linePadding)
            }
            WinLineType.DIAGONAL_ANTI -> {
                Offset(size.width - linePadding, linePadding) to
                    Offset(linePadding, size.height - linePadding)
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
