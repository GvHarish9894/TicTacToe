package com.techgv.tictactoe.di

import com.techgv.tictactoe.analytics.AnalyticsHelper
import com.techgv.tictactoe.analytics.PerformanceHelper
import com.techgv.tictactoe.data.model.AIDifficulty
import com.techgv.tictactoe.data.model.FirstPlayer
import com.techgv.tictactoe.data.model.HumanSymbol
import com.techgv.tictactoe.data.repository.SettingsRepository
import com.techgv.tictactoe.ui.screens.game.GameViewModel
import com.techgv.tictactoe.ui.screens.gamemode.GameMode
import com.techgv.tictactoe.ui.screens.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Repositories - single instance
    single { SettingsRepository(androidContext()) }

    // Analytics - single instance
    single { AnalyticsHelper() }
    single { PerformanceHelper() }

    // ViewModels - new instance per request
    viewModel { (gameMode: GameMode, aiDifficulty: AIDifficulty?, firstPlayer: FirstPlayer, humanSymbol: HumanSymbol) ->
        GameViewModel(
            settingsRepository = get(),
            analyticsHelper = get(),
            performanceHelper = get(),
            aiDifficulty = aiDifficulty,
            gameMode = gameMode,
            firstPlayer = firstPlayer,
            humanSymbol = humanSymbol
        )
    }
    viewModel { SettingsViewModel(get(), get()) }
}
