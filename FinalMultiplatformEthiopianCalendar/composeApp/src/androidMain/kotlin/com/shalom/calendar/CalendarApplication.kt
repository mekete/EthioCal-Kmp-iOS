package com.shalom.calendar

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CalendarApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Koin
        // Note: Koin modules are currently incomplete - full initialization will be available
        // once the data layer classes are migrated to the KMP project
        try {
            startKoin {
                androidLogger(Level.ERROR)
                androidContext(this@CalendarApplication)
                // TODO: Add appModules once data layer is fully migrated
                // modules(appModules)
            }
        } catch (e: Exception) {
            // Log initialization error but don't crash the app
            android.util.Log.e("CalendarApplication", "Koin initialization failed", e)
        }
    }
}
