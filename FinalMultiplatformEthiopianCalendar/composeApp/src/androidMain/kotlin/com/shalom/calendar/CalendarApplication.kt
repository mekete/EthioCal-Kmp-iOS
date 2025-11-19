package com.shalom.calendar

import android.app.Application
import android.content.Context
import com.shalom.calendar.alarm.NotificationHelper
import com.shalom.calendar.data.preferences.Language
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.util.LocaleHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CalendarApplication : Application() {

    override fun attachBaseContext(base: Context) {
        // Get the saved language preference and wrap the context with it
        val language = runBlocking {
            try {
                SettingsPreferences(base).language.first()
            } catch (e: Exception) {
                Language.AMHARIC
            }
        }
        val wrappedContext = LocaleHelper.wrapContext(base, language)
        super.attachBaseContext(wrappedContext)
    }

    override fun onCreate() {
        super.onCreate()

        // Create notification channels for event reminders
        NotificationHelper.createNotificationChannels(this)

        // TODO: Add back dependency injection and initialization when Hilt/Koin is configured
        // The following features are disabled until DI is set up:
        // - WorkManager configuration
        // - Permission state management
        // - Widget scheduling
        // - Remote config initialization
        // - App initialization manager
        // - Reminder re-registration
        // - Analytics user properties
    }
}
