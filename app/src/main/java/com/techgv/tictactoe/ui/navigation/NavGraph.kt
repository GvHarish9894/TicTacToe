package com.techgv.tictactoe.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.techgv.tictactoe.analytics.AnalyticsHelper
import com.techgv.tictactoe.analytics.AnalyticsValues
import com.techgv.tictactoe.data.model.AIDifficulty
import com.techgv.tictactoe.data.model.FirstPlayer
import com.techgv.tictactoe.data.model.HumanSymbol
import com.techgv.tictactoe.ui.screens.game.GameScreen
import com.techgv.tictactoe.ui.screens.gamemode.GameMode
import com.techgv.tictactoe.ui.screens.gamemode.GameModeScreen
import com.techgv.tictactoe.ui.screens.settings.SettingsScreen
import com.techgv.tictactoe.ui.screens.splash.SplashScreen
import org.koin.compose.koinInject

@Composable
fun TicTacToeNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route,
    analyticsHelper: AnalyticsHelper = koinInject()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash Screen
        composable(
            route = Screen.Splash.route,
            exitTransition = {
                fadeOut(animationSpec = tween(AnimationDuration))
            }
        ) {
            LaunchedEffect(Unit) {
                analyticsHelper.logScreenView(
                    screenName = AnalyticsValues.SCREEN_SPLASH,
                    screenClass = "SplashScreen"
                )
            }

            SplashScreen(
                onNavigateToGameMode = {
                    analyticsHelper.logNavigateForward(
                        fromScreen = AnalyticsValues.SCREEN_SPLASH,
                        toScreen = AnalyticsValues.SCREEN_GAME_MODE
                    )
                    navController.navigate(Screen.GameMode.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Game Mode Selection Screen
        composable(
            route = Screen.GameMode.route,
            enterTransition = {
                fadeIn(animationSpec = tween(AnimationDuration))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(AnimationDuration)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(AnimationDuration)
                )
            }
        ) {
            LaunchedEffect(Unit) {
                analyticsHelper.logScreenView(
                    screenName = AnalyticsValues.SCREEN_GAME_MODE,
                    screenClass = "GameModeScreen"
                )
            }

            GameModeScreen(
                onStartGame = { gameMode, difficulty, firstPlayer, humanSymbol ->
                    // Log game mode selection
                    analyticsHelper.logGameModeSelected(gameMode)
                    difficulty?.let { analyticsHelper.logAIDifficultySelected(it) }
                    firstPlayer?.let { analyticsHelper.logFirstPlayerSelected(it) }

                    analyticsHelper.logNavigateForward(
                        fromScreen = AnalyticsValues.SCREEN_GAME_MODE,
                        toScreen = AnalyticsValues.SCREEN_GAME
                    )

                    navController.navigate(
                        Screen.Game.createRoute(
                            gameMode = gameMode.name,
                            difficulty = difficulty?.name ?: "NONE",
                            firstPlayer = firstPlayer?.name ?: "HUMAN",
                            humanSymbol = humanSymbol?.displayName ?: "X"
                        )
                    )
                }
            )
        }

        // Game Screen
        composable(
            route = Screen.Game.route,
            arguments = listOf(
                navArgument("gameMode") { type = NavType.StringType },
                navArgument("difficulty") { type = NavType.StringType },
                navArgument("firstPlayer") { type = NavType.StringType },
                navArgument("humanSymbol") { type = NavType.StringType }
            ),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(AnimationDuration)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(AnimationDuration)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(AnimationDuration)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(AnimationDuration)
                )
            }
        ) { backStackEntry ->
            LaunchedEffect(Unit) {
                analyticsHelper.logScreenView(
                    screenName = AnalyticsValues.SCREEN_GAME,
                    screenClass = "GameScreen"
                )
            }

            val gameMode = GameMode.valueOf(
                backStackEntry.arguments?.getString("gameMode") ?: "PLAYER_VS_PLAYER"
            )
            val difficulty = backStackEntry.arguments?.getString("difficulty")?.let {
                if (it != "NONE") AIDifficulty.valueOf(it) else null
            }
            val firstPlayer = backStackEntry.arguments?.getString("firstPlayer")?.let {
                FirstPlayer.valueOf(it)
            } ?: FirstPlayer.HUMAN
            val humanSymbol = backStackEntry.arguments?.getString("humanSymbol")?.let {
                when (it) {
                    "O" -> HumanSymbol.O
                    else -> HumanSymbol.X
                }
            } ?: HumanSymbol.X

            GameScreen(
                gameMode = gameMode,
                aiDifficulty = difficulty,
                firstPlayer = firstPlayer,
                humanSymbol = humanSymbol,
                onNavigateBack = {
                    analyticsHelper.logNavigateBack(
                        fromScreen = AnalyticsValues.SCREEN_GAME,
                        toScreen = AnalyticsValues.SCREEN_GAME_MODE
                    )
                    navController.popBackStack()
                },
                onNavigateToSettings = {
                    analyticsHelper.logNavigateForward(
                        fromScreen = AnalyticsValues.SCREEN_GAME,
                        toScreen = AnalyticsValues.SCREEN_SETTINGS
                    )
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        // Settings Screen
        composable(
            route = Screen.Settings.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(AnimationDuration)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(AnimationDuration)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(AnimationDuration)
                )
            }
        ) {
            LaunchedEffect(Unit) {
                analyticsHelper.logScreenView(
                    screenName = AnalyticsValues.SCREEN_SETTINGS,
                    screenClass = "SettingsScreen"
                )
            }

            SettingsScreen(
                onBackPress = {
                    analyticsHelper.logNavigateBack(
                        fromScreen = AnalyticsValues.SCREEN_SETTINGS,
                        toScreen = AnalyticsValues.SCREEN_GAME
                    )
                    navController.popBackStack()
                }
            )
        }
    }
}

private const val AnimationDuration = 300
