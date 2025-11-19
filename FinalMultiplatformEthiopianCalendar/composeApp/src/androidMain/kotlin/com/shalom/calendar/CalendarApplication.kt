package com.shalom.calendar

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CalendarApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Koin with empty modules for now
        // Modules will be added once the data layer is fully migrated to KMP
        try {
            startKoin {
                androidLogger(Level.ERROR)
                androidContext(this@CalendarApplication)
                modules(emptyList())
            }
        } catch (e: Exception) {
            // Log initialization error but don't crash the app
            android.util.Log.e("CalendarApplication", "Koin initialization failed", e)
        }
    }
}
