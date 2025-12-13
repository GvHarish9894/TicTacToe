package com.techgv.tictactoe.ui.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.techgv.tictactoe.data.model.Player
import com.techgv.tictactoe.ui.components.BottomNavBar
import com.techgv.tictactoe.ui.components.NavItem
import com.techgv.tictactoe.ui.theme.CardBackground
import com.techgv.tictactoe.ui.theme.CoralAccent
import com.techgv.tictactoe.ui.theme.DarkGreen500
import com.techgv.tictactoe.ui.theme.DarkGreen800
import com.techgv.tictactoe.ui.theme.DarkGreen900
import com.techgv.tictactoe.ui.theme.GreenAccent
import com.techgv.tictactoe.ui.theme.TextSecondary
import com.techgv.tictactoe.ui.theme.WarningYellow

@Composable
fun StatsScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: StatsViewModel = viewModel()
) {
    val stats by viewModel.stats.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = NavItem.STATS,
                onItemSelected = { item ->
                    when (item) {
                        NavItem.HOME -> onNavigateToHome()
                        NavItem.STATS -> { /* Already here */ }
                        NavItem.SETTINGS -> onNavigateToSettings()
                    }
                }
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(DarkGreen900, DarkGreen800)
                    )
                )
                .padding(paddingValues)
                .statusBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Your game performance",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Total Games Card
            StatsCard(
                title = "Total Games",
                value = stats.totalGames.toString(),
                icon = "🎮"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Win Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PlayerStatsCard(
                    player = Player.X,
                    wins = stats.playerXWins,
                    winRate = stats.winRateX,
                    modifier = Modifier.weight(1f)
                )
                PlayerStatsCard(
                    player = Player.O,
                    wins = stats.playerOWins,
                    winRate = stats.winRateO,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Draws Card
            StatsCard(
                title = "Draws",
                value = stats.draws.toString(),
                icon = "🤝",
                color = WarningYellow
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Additional Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Fastest Win
                SmallStatsCard(
                    title = "Fastest Win",
                    value = stats.fastestWinSeconds?.let { "${it}s" } ?: "-",
                    icon = "⚡",
                    modifier = Modifier.weight(1f)
                )
                // Win Streak
                SmallStatsCard(
                    title = "Win Streak",
                    value = stats.currentWinStreak.toString(),
                    icon = "🔥",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatsCard(
    title: String,
    value: String,
    icon: String,
    color: Color = GreenAccent,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .border(
                width = 1.dp,
                color = DarkGreen500.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun PlayerStatsCard(
    player: Player,
    wins: Int,
    winRate: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .border(
                width = 1.dp,
                color = DarkGreen500.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Player indicator
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(player.color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = player.symbol,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = player.color
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = player.displayName,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = wins.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = player.color
        )

        Text(
            text = "wins",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Win rate
        Text(
            text = "${(winRate * 100).toInt()}%",
            style = MaterialTheme.typography.titleSmall,
            color = TextSecondary
        )
    }
}

@Composable
private fun SmallStatsCard(
    title: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .border(
                width = 1.dp,
                color = DarkGreen500.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = icon, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = GreenAccent
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}
