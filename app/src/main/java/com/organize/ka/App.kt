package com.organize.ka

import android.app.Application
import com.organize.ka.di.dataModule
import com.organize.ka.di.screenModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidDebugLogger()
            applicationContext()
            appModules()
        }
    }

    private fun KoinApplication.androidDebugLogger() {
        androidLogger(level = if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
    }

    private fun KoinApplication.applicationContext() {
        androidContext(androidContext = this@App)
    }

    private fun KoinApplication.appModules() {
        modules(
            modules = listOf(
                dataModule,
                screenModule,
            )
        )
    }
}