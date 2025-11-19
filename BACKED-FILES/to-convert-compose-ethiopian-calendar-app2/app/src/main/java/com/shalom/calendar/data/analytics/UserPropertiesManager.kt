package com.shalom.calendar.data.analytics

import com.shalom.calendar.data.permissions.PermissionManager
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.data.preferences.ThemePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages user properties for analytics.
 * Automatically syncs key user preferences as user properties
 * to enable segmentation and analysis in Firebase Analytics.
 */
@Singleton
class UserPropertiesManager @Inject constructor(
    private val analyticsManager: AnalyticsManager,
    private val settingsPreferences: SettingsPreferences,
    private val themePreferences: ThemePreferences,
    private val permissionManager: PermissionManager
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    /**
     * Initialize and sync all user properties.
     * Call this once during app initialization.
     */
    fun initializeUserProperties() {
        scope.launch {
            syncAllProperties()
            observePropertyChanges()
        }
    }

    /**
     * Sync all user properties immediately
     */
    private suspend fun syncAllProperties() {
        // Language
        val language = settingsPreferences.language.first()
        analyticsManager.setUserProperty(AnalyticsParams.USER_LANGUAGE, language.name)

        // Theme
        val appTheme = themePreferences.appTheme.first()
        analyticsManager.setUserProperty(AnalyticsParams.USER_THEME, appTheme.name)

        // Theme Mode (Light/Dark/System)
        val themeMode = themePreferences.themeMode.first()
        analyticsManager.setUserProperty(AnalyticsParams.USER_THEME_MODE, themeMode.name)

        // Primary Calendar
        val primaryCalendar = settingsPreferences.primaryCalendar.first()
        analyticsManager.setUserProperty(AnalyticsParams.PRIMARY_CALENDAR, primaryCalendar.name)

        // Dual Calendar
        val dualCalendar = settingsPreferences.displayDualCalendar.first()
        analyticsManager.setUserProperty(AnalyticsParams.DUAL_CALENDAR_ENABLED, dualCalendar.toString())

        // Holiday Preferences
        val publicHolidays = settingsPreferences.includeAllDayOffHolidays.first()
        analyticsManager.setUserProperty(AnalyticsParams.PUBLIC_HOLIDAYS_ENABLED, publicHolidays.toString())

        val orthodoxHolidays = settingsPreferences.includeWorkingOrthodoxHolidays.first()
        analyticsManager.setUserProperty(AnalyticsParams.ORTHODOX_HOLIDAYS_ENABLED, orthodoxHolidays.toString())

        val muslimHolidays = settingsPreferences.includeWorkingMuslimHolidays.first()
        analyticsManager.setUserProperty(AnalyticsParams.MUSLIM_HOLIDAYS_ENABLED, muslimHolidays.toString())

        // Device Country
        val countryCode = settingsPreferences.deviceCountryCode.first()
        analyticsManager.setUserProperty(AnalyticsParams.DEVICE_COUNTRY_CODE, countryCode)

        // Permissions
        val permissionState = permissionManager.permissionState.first()
        analyticsManager.setUserProperty(
            AnalyticsParams.HAS_NOTIFICATION_PERMISSION,
            permissionState.notificationsPermission.isGranted.toString()
        )
        analyticsManager.setUserProperty(
            AnalyticsParams.HAS_EXACT_ALARM_PERMISSION,
            permissionState.exactAlarmPermission.isGranted.toString()
        )

        // Orthodox Day Names
        val orthodoxDayNames = settingsPreferences.showOrthodoxDayNames.first()
        analyticsManager.setUserProperty("orthodox_day_names_enabled", orthodoxDayNames.toString())

        // Widget settings
        val displayTwoClocks = settingsPreferences.displayTwoClocks.first()
        analyticsManager.setUserProperty(AnalyticsParams.WIDGET_INSTALLED, displayTwoClocks.toString())
    }

    /**
     * Observe key preference changes and update user properties in real-time
     */
    private fun observePropertyChanges() {
        // Observe language changes
        scope.launch {
            settingsPreferences.language.collect { language ->
                analyticsManager.setUserProperty(AnalyticsParams.USER_LANGUAGE, language.name)
            }
        }

        // Observe theme changes
        scope.launch {
            themePreferences.appTheme.collect { theme ->
                analyticsManager.setUserProperty(AnalyticsParams.USER_THEME, theme.name)
            }
        }

        // Observe theme mode changes
        scope.launch {
            themePreferences.themeMode.collect { mode ->
                analyticsManager.setUserProperty(AnalyticsParams.USER_THEME_MODE, mode.name)
            }
        }

        // Observe primary calendar changes
        scope.launch {
            settingsPreferences.primaryCalendar.collect { calendar ->
                analyticsManager.setUserProperty(AnalyticsParams.PRIMARY_CALENDAR, calendar.name)
            }
        }

        // Observe dual calendar toggle
        scope.launch {
            settingsPreferences.displayDualCalendar.collect { enabled ->
                analyticsManager.setUserProperty(AnalyticsParams.DUAL_CALENDAR_ENABLED, enabled.toString())
            }
        }

        // Observe permission changes
        scope.launch {
            permissionManager.permissionState.collect { state ->
                analyticsManager.setUserProperty(
                    AnalyticsParams.HAS_NOTIFICATION_PERMISSION,
                    state.notificationsPermission.isGranted.toString()
                )
                analyticsManager.setUserProperty(
                    AnalyticsParams.HAS_EXACT_ALARM_PERMISSION,
                    state.exactAlarmPermission.isGranted.toString()
                )
            }
        }
    }

    /**
     * Track total events created (increment counter)
     */
    fun incrementEventsCreated() {
        scope.launch {
            // You could maintain a counter in preferences if needed
            // For now, we track via individual event creation events
        }
    }
}
