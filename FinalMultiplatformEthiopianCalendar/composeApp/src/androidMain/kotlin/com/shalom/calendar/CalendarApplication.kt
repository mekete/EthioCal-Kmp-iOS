package com.shalom.calendar

import android.app.Application
import com.shalom.calendar.di.androidPlatformModule
import com.shalom.calendar.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CalendarApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Koin with all app modules
        try {
            startKoin {
                androidLogger(Level.ERROR)
                androidContext(this@CalendarApplication)
                modules(appModules + androidPlatformModule(this@CalendarApplication))
            }
        } catch (e: Exception) {
            // Log initialization error but don't crash the app
            android.util.Log.e("CalendarApplication", "Koin initialization failed", e)
        }
    }
}
