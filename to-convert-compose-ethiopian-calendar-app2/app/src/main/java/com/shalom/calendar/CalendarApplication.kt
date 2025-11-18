package com.shalom.calendar

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.shalom.calendar.alarm.NotificationHelper
import com.shalom.calendar.data.initialization.AppInitializationManager
import com.shalom.calendar.data.initialization.ReminderReregistrationManager
import com.shalom.calendar.data.permissions.PermissionManager
import com.shalom.calendar.data.preferences.Language
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.data.remote.RemoteConfigManager
import com.shalom.calendar.util.LocaleHelper
import com.shalom.calendar.widget.CalendarWidgetWorker
import com.shalom.calendar.widget.DateChangeWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class CalendarApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var remoteConfigManager: RemoteConfigManager

    @Inject
    lateinit var appInitializationManager: AppInitializationManager

    @Inject
    lateinit var reminderReregistrationManager: ReminderReregistrationManager

    @Inject
    lateinit var permissionManager: PermissionManager

    @Inject
    lateinit var userPropertiesManager: com.shalom.calendar.data.analytics.UserPropertiesManager

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

    override fun attachBaseContext(base: Context) { // Get the saved language preference and wrap the context with it
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

        // Check and log initial permission states
        permissionManager.refreshPermissionState()

        // Register permission change listener to monitor SCHEDULE_EXACT_ALARM changes
        permissionManager.registerPermissionChangeListener(reminderReregistrationManager)

        // Schedule periodic widget updates (events refresh every 30 minutes)
        CalendarWidgetWorker.schedule(this)

        // Schedule hourly date updates (date labels refresh every hour)
        DateChangeWorker.scheduleDaily(this)

        // 1.4 Initialize Firebase Remote Config for Muslim holiday offsets
        // This is called in AppInitializationManager but we keep it here for immediate config
        remoteConfigManager.initialize(isDebug = BuildConfig.DEBUG)

        // Execute comprehensive app initialization:
        // 1.1 Locale & Chronology Initialization
        // 1.2 First-Time Setup (if needed)
        // 1.3 Version-Upgrade Setup (if needed)
        // 1.4 Firebase Remote Config (refresh)
        // 1.6 Analytics and Usage Logging
        appInitializationManager.initialize()

        // 1.5 Re-register event reminders
        // Ensures alarms survive app updates and device reboots
        reminderReregistrationManager.reregisterReminders()

        // Initialize user properties for analytics segmentation
        userPropertiesManager.initializeUserProperties()
    }
}
