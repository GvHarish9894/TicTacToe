package com.techgv.tictactoe

import android.app.Application
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.techgv.tictactoe.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TicTacToeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Koin DI
        startKoin {
            androidLogger()
            androidContext(this@TicTacToeApplication)
            modules(appModule)
        }

        // Initialize Firebase services
        initializeFirebase()
    }

    private fun initializeFirebase() {
        // Enable analytics collection in all builds
        // Note: When using DebugView, events are separated from production data
        Firebase.analytics.setAnalyticsCollectionEnabled(true)

        // Configure Crashlytics (disabled in debug builds)
        FirebaseCrashlytics.getInstance().apply {
            isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
            // Add custom keys for debugging
            setCustomKey("app_version", BuildConfig.VERSION_NAME)
            setCustomKey("version_code", BuildConfig.VERSION_CODE)
        }
    }
}
