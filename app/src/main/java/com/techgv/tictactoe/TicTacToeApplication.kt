package com.techgv.tictactoe

import android.app.Application
import com.techgv.tictactoe.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TicTacToeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TicTacToeApplication)
            modules(appModule)
        }
    }
}
