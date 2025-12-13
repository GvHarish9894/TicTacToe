package com.techgv.tictactoe.ui.screens.settings

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.techgv.tictactoe.ui.components.BottomNavBar
import com.techgv.tictactoe.ui.components.NavItem
import com.techgv.tictactoe.ui.theme.ButtonShape
import com.techgv.tictactoe.ui.theme.CardBackground
import com.techgv.tictactoe.ui.theme.CellBackground
import com.techgv.tictactoe.ui.theme.CoralAccent
import com.techgv.tictactoe.ui.theme.DarkGreen500
import com.techgv.tictactoe.ui.theme.DarkGreen600
import com.techgv.tictactoe.ui.theme.DarkGreen800
import com.techgv.tictactoe.ui.theme.DarkGreen900
import com.techgv.tictactoe.ui.theme.ErrorRed
import com.techgv.tictactoe.ui.theme.GreenAccent
import com.techgv.tictactoe.ui.theme.TextPrimary
import com.techgv.tictactoe.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToStats: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val settings by viewModel.settings.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateToHome) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            BottomNavBar(
                selectedItem = NavItem.SETTINGS,
                onItemSelected = { item ->
                    when (item) {
                        NavItem.HOME -> onNavigateToHome()
                        NavItem.STATS -> onNavigateToStats()
                        NavItem.SETTINGS -> { /* Already here */ }
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // GAME EXPERIENCE Section
            SectionHeader(title = "GAME EXPERIENCE")

            Spacer(modifier = Modifier.height(12.dp))

            // Settings Card for Game Experience
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardBackground)
                    .border(
                        width = 1.dp,
                        color = DarkGreen500.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                // Sound Effects Toggle
                SettingsToggleRow(
                    icon = Icons.Filled.PlayArrow,
                    title = "Sound Effects",
                    subtitle = "In-game audio",
                    isChecked = settings.soundEnabled,
                    onCheckedChange = { viewModel.updateSoundEnabled(it) }
                )

                HorizontalDivider(
                    color = DarkGreen600.copy(alpha = 0.5f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // Haptic Feedback Toggle
                SettingsToggleRow(
                    icon = Icons.Filled.Notifications,
                    title = "Haptic Feedback",
                    subtitle = "Vibrate on moves",
                    isChecked = settings.hapticEnabled,
                    onCheckedChange = { viewModel.updateHapticEnabled(it) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // PLAYER CUSTOMIZATION Section
            SectionHeader(title = "PLAYER CUSTOMIZATION")

            Spacer(modifier = Modifier.height(12.dp))

            // Player Customization Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardBackground)
                    .border(
                        width = 1.dp,
                        color = DarkGreen500.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Player 1 (X)
                PlayerNameInput(
                    label = "Player 1 (X)",
                    value = settings.playerXName,
                    onValueChange = { viewModel.updatePlayerXName(it) },
                    playerSymbol = "X",
                    symbolColor = GreenAccent
                )

                // Player 2 (O)
                PlayerNameInput(
                    label = "Player 2 (O)",
                    value = settings.playerOName,
                    onValueChange = { viewModel.updatePlayerOName(it) },
                    playerSymbol = "O",
                    symbolColor = CoralAccent
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Reset Game Data Button
            OutlinedButton(
                onClick = { showResetDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = ButtonShape,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = ErrorRed
                ),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                    brush = SolidColor(ErrorRed.copy(alpha = 0.5f))
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = ErrorRed
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Reset Game Data",
                    style = MaterialTheme.typography.labelLarge,
                    color = ErrorRed
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Version info
            Text(
                text = "Tic Tac Toe v1.2.0 • Build 842",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Reset Confirmation Dialog
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = {
                    Text(
                        text = "Reset Game Data?",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                },
                text = {
                    Text(
                        text = "This will clear all your statistics and reset settings to default. This action cannot be undone.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.resetAllData()
                            showResetDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ErrorRed
                        )
                    ) {
                        Text("Reset")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                },
                containerColor = CardBackground,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium.copy(
            letterSpacing = 1.sp,
            fontWeight = FontWeight.SemiBold
        ),
        color = GreenAccent,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp)
    )
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon background
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(DarkGreen600),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = GreenAccent,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = DarkGreen600,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun PlayerNameInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    playerSymbol: String,
    symbolColor: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CellBackground)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Player symbol
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(symbolColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = playerSymbol,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = symbolColor
                )
            }

            // Text input
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    color = TextPrimary,
                    fontSize = 16.sp
                ),
                singleLine = true,
                cursorBrush = SolidColor(GreenAccent),
                modifier = Modifier.weight(1f)
            )
        }
    }
}
