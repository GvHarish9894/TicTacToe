package com.techgv.tictactoe.di

import com.techgv.tictactoe.data.repository.SettingsRepository
import com.techgv.tictactoe.ui.screens.game.GameViewModel
import com.techgv.tictactoe.ui.screens.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Repositories - single instance
    single { SettingsRepository(androidContext()) }

    // ViewModels - new instance per request
    viewModel { GameViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
}
