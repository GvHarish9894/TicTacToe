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
import com.techgv.tictactoe.ui.screens.stats.StatsScreen

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
            GameModeScreen(
                onStartGame = {
                    navController.navigate(Screen.Game.route)
                },
                onNavigateToStats = {
                    navController.navigate(Screen.Stats.route) {
                        popUpTo(Screen.GameMode.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route) {
                        popUpTo(Screen.GameMode.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
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
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            }
        ) {
            GameScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToStats = {
                    navController.navigate(Screen.Stats.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToHome = {
                    navController.navigate(Screen.GameMode.route) {
                        popUpTo(Screen.GameMode.route) { inclusive = true }
                    }
                }
            )
        }

        // Stats Screen
        composable(
            route = Screen.Stats.route,
            enterTransition = {
                fadeIn(animationSpec = tween(ANIMATION_DURATION))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(ANIMATION_DURATION))
            }
        ) {
            StatsScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.GameMode.route) {
                        popUpTo(Screen.GameMode.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route) {
                        popUpTo(Screen.GameMode.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // Settings Screen
        composable(
            route = Screen.Settings.route,
            enterTransition = {
                fadeIn(animationSpec = tween(ANIMATION_DURATION))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(ANIMATION_DURATION))
            }
        ) {
            SettingsScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.GameMode.route) {
                        popUpTo(Screen.GameMode.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToStats = {
                    navController.navigate(Screen.Stats.route) {
                        popUpTo(Screen.GameMode.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
