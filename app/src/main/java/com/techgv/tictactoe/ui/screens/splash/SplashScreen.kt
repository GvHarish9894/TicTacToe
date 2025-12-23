package com.techgv.tictactoe.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.techgv.tictactoe.BuildConfig
import com.techgv.tictactoe.R
import com.techgv.tictactoe.data.model.Player
import com.techgv.tictactoe.ui.components.OMark
import com.techgv.tictactoe.ui.components.XMark
import com.techgv.tictactoe.ui.theme.CellBackground
import com.techgv.tictactoe.ui.theme.CellBorder
import com.techgv.tictactoe.ui.theme.CoralAccent
import com.techgv.tictactoe.ui.theme.DarkGreen700
import com.techgv.tictactoe.ui.theme.DarkGreen800
import com.techgv.tictactoe.ui.theme.DarkGreen900
import com.techgv.tictactoe.ui.theme.GameCellShape
import com.techgv.tictactoe.ui.theme.GreenAccent
import com.techgv.tictactoe.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun SplashScreen(onNavigateToGameMode: () -> Unit) {
    // Animation states
    val titleAlpha = remember { Animatable(0f) }
    val boardAlpha = remember { Animatable(0f) }
    val loadingAlpha = remember { Animatable(0f) }
    val progress = remember { Animatable(0f) }

    // Launch animations
    LaunchedEffect(Unit) {
        // Fade in title
        titleAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(500, easing = FastOutSlowInEasing)
        )

        // Fade in board
        boardAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(500, easing = FastOutSlowInEasing)
        )

        // Fade in loading
        loadingAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(300)
        )

        // Animate progress
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(2000, easing = LinearEasing)
        )

        // Small delay before navigation
        delay(300)

        // Navigate to game mode
        onNavigateToGameMode()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkGreen900, DarkGreen800)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Title
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.alpha(titleAlpha.value)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Mini game board preview
            MiniGameBoard(
                modifier = Modifier
                    .width(220.dp)
                    .alpha(boardAlpha.value)
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Loading progress bar
            Column(
                modifier = Modifier
                    .alpha(loadingAlpha.value)
                    .width(220.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = { progress.value },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = GreenAccent,
                    trackColor = DarkGreen700,
                    strokeCap = StrokeCap.Round
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.loading),
                    style = MaterialTheme.typography.labelMedium,
                    color = GreenAccent,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Version number
            Text(
                text = "v${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MiniGameBoard(modifier: Modifier = Modifier) {
    // Mutable board state - starts empty
    val boardState =
        remember { mutableStateListOf<Player>().apply { addAll(List(9) { Player.NONE }) } }

    // Animation to randomly fill cells one by one
    LaunchedEffect(Unit) {
        val availableIndices = (0..8).toMutableList()

        // Fill cells one by one with delays
        while (availableIndices.isNotEmpty()) {
            delay(250L) // Delay between each cell animation

            // Pick random empty cell
            val randomIndex = availableIndices.random()
            availableIndices.remove(randomIndex)

            // Randomly choose X or O
            boardState[randomIndex] = if (Random.nextBoolean()) Player.X else Player.O
        }
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(DarkGreen700.copy(alpha = 0.5f))
            .border(
                width = 1.dp,
                color = CellBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (row in 0..2) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (col in 0..2) {
                        val index = row * 3 + col
                        MiniCell(
                            player = boardState[index],
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniCell(
    player: Player,
    modifier: Modifier = Modifier
) {
    // Scale animation for pop effect
    val scale by animateFloatAsState(
        targetValue = if (player != Player.NONE) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "cell_pop"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(GameCellShape)
            .background(CellBackground)
            .border(
                width = 1.dp,
                color = CellBorder,
                shape = GameCellShape
            ),
        contentAlignment = Alignment.Center
    ) {
        when (player) {
            Player.X -> XMark(
                color = GreenAccent,
                strokeWidth = 6f,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
            )
            Player.O -> OMark(
                color = CoralAccent,
                strokeWidth = 6f,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
            )
            Player.NONE -> { /* Empty */ }
        }
    }
}

private val Int.sp: androidx.compose.ui.unit.TextUnit
    get() =
        androidx.compose.ui.unit
            .TextUnit(this.toFloat(), androidx.compose.ui.unit.TextUnitType.Sp)
