package com.techgv.tictactoe.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.techgv.tictactoe.ui.screens.game.GameScreen
import com.techgv.tictactoe.ui.screens.gamemode.GameModeScreen
import com.techgv.tictactoe.ui.screens.settings.SettingsScreen
import com.techgv.tictactoe.ui.screens.splash.SplashScreen

private const val ANIMATION_DURATION = 300

@Composable
fun TicTacToeNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash Screen
        composable(
            route = Screen.Splash.route,
            exitTransition = {
                fadeOut(animationSpec = tween(ANIMATION_DURATION))
            }
        ) {
            SplashScreen(
                onNavigateToGameMode = {
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
                fadeIn(animationSpec = tween(ANIMATION_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            }
        ) {
            GameModeScreen(onStartGame = { navController.navigate(Screen.Game.route) })
        }

        // Game Screen
        composable(
            route = Screen.Game.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            }
        ) {
            GameScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        // Settings Screen
        composable(
            route = Screen.Settings.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            }
        ) {
            SettingsScreen(onBackPress = { navController.popBackStack() })
        }
    }
}
