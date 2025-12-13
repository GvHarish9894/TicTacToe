package com.techgv.tictactoe.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.techgv.tictactoe.ui.theme.CardBackground
import com.techgv.tictactoe.ui.theme.GreenAccent
import com.techgv.tictactoe.ui.theme.TextSecondary

enum class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("HOME", Icons.Filled.Home, Icons.Outlined.Home),
    STATS("STATS", Icons.Filled.Home, Icons.Outlined.Home), // Using home icon as placeholder
    SETTINGS("SETTINGS", Icons.Filled.Settings, Icons.Outlined.Settings)
}

@Composable
fun BottomNavBar(
    selectedItem: NavItem,
    onItemSelected: (NavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(CardBackground)
            .padding(vertical = 12.dp, horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem.entries.forEach { item ->
            BottomNavItem(
                item = item,
                isSelected = item == selectedItem,
                onClick = { onItemSelected(item) }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) GreenAccent else CardBackground
                ),
            contentAlignment = Alignment.Center
        ) {
            // Custom icon for Stats (bar chart)
            if (item == NavItem.STATS) {
                StatsIcon(
                    isSelected = isSelected,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                    contentDescription = item.label,
                    tint = if (isSelected) CardBackground else TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.label,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) GreenAccent else TextSecondary
        )
    }
}

@Composable
private fun StatsIcon(
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        // Three bars representing a chart
        val barColor = if (isSelected) CardBackground else TextSecondary
        Box(
            modifier = Modifier
                .size(width = 4.dp, height = 8.dp)
                .background(barColor, RoundedCornerShape(1.dp))
        )
        Box(
            modifier = Modifier
                .size(width = 4.dp, height = 16.dp)
                .background(barColor, RoundedCornerShape(1.dp))
        )
        Box(
            modifier = Modifier
                .size(width = 4.dp, height = 12.dp)
                .background(barColor, RoundedCornerShape(1.dp))
        )
    }
}
