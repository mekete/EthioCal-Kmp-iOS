package com.shalom.calendar

import android.app.Application
import android.content.Context
import com.shalom.calendar.alarm.NotificationHelper
import com.shalom.calendar.data.analytics.UserPropertiesManager
import com.shalom.calendar.data.initialization.AppInitializationManager
import com.shalom.calendar.data.initialization.ReminderReregistrationManager
import com.shalom.calendar.data.permissions.PermissionManager
import com.shalom.calendar.data.preferences.Language
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.di.appModules
import com.shalom.calendar.util.LocaleHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CalendarApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val appInitializationManager: AppInitializationManager by inject()
    private val reminderReregistrationManager: ReminderReregistrationManager by inject()
    private val permissionManager: PermissionManager by inject()
    private val userPropertiesManager: UserPropertiesManager by inject()

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

        // Initialize Koin
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@CalendarApplication)
            modules(appModules)
        }

        // Create notification channels for event reminders
        NotificationHelper.createNotificationChannels(this)

        // Initialize app components
        applicationScope.launch {
            // Initialize remote config and other app-wide setup
            appInitializationManager.initialize()

            // Re-register any existing reminders after app update/restart
            reminderReregistrationManager.reregisterAllReminders()

            // Refresh permission state
            permissionManager.refreshPermissionState()

            // Update analytics user properties
            userPropertiesManager.updateAllUserProperties()
        }
    }
}
