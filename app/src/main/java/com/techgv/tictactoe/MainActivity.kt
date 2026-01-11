package com.techgv.tictactoe

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.rememberNavController
import com.techgv.tictactoe.analytics.AnalyticsHelper
import com.techgv.tictactoe.ui.navigation.TicTacToeNavGraph
import com.techgv.tictactoe.ui.theme.DarkGreen900
import com.techgv.tictactoe.ui.theme.TicTacToeTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val analyticsHelper: AnalyticsHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Log app open
        analyticsHelper.logAppOpen()

        // Lock phones to portrait, allow tablets to rotate
        val isTablet = resources.configuration.smallestScreenWidthDp >= 600
        if (!isTablet) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(DarkGreen900.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(DarkGreen900.toArgb())
        )
        setContent {
            TicTacToeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    TicTacToeNavGraph(navController = navController)
                }
            }
        }
    }
}
