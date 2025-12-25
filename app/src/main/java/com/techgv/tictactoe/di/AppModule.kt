package com.techgv.tictactoe.di

import com.techgv.tictactoe.data.model.AIDifficulty
import com.techgv.tictactoe.data.model.FirstPlayer
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

    // ViewModels - new instance per request
    viewModel { (gameMode: GameMode, aiDifficulty: AIDifficulty?, firstPlayer: FirstPlayer) ->
        GameViewModel(
            settingsRepository = get(),
            aiDifficulty = aiDifficulty,
            gameMode = gameMode,
            firstPlayer = firstPlayer
        )
    }
    viewModel { SettingsViewModel(get()) }
}
