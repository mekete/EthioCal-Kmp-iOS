package com.shalom.calendar.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Android implementation of SettingsPreferences using DataStore.
 * This provides persistent storage for app settings on Android.
 */
class SettingsPreferencesImpl(private val context: Context) : SettingsPreferences {

    companion object {
        private val Context.settingsDataStore by preferencesDataStore(name = "settings_preferences")

        // Preference keys
        private val PRIMARY_CALENDAR_KEY = stringPreferencesKey("primary_calendar")
        private val SECONDARY_CALENDAR_KEY = stringPreferencesKey("secondary_calendar")
        private val DISPLAY_DUAL_CALENDAR_KEY = booleanPreferencesKey("display_dual_calendar")

        private val INCLUDE_ALL_DAY_OFF_HOLIDAYS_KEY = booleanPreferencesKey("show_public_holidays")
        private val INCLUDE_WORKING_ORTHODOX_HOLIDAYS_KEY = booleanPreferencesKey("show_orthodox_fasting_holidays")
        private val INCLUDE_WORKING_MUSLIM_HOLIDAYS_KEY = booleanPreferencesKey("show_muslim_holidays")

        private val HIDE_HOLIDAY_INFO_DIALOG_KEY = booleanPreferencesKey("hide_holiday_info_dialog")
        private val USE_AMHARIC_DAY_NAMES_KEY = booleanPreferencesKey("show_orthodox_day_names")
        private val SHOW_LUNAR_PHASES_KEY = booleanPreferencesKey("show_lunar_phases")

        private val IS_DARK_MODE_KEY = booleanPreferencesKey("is_dark_mode")
        private val THEME_COLOR_KEY = stringPreferencesKey("theme_color")

        private val HAS_COMPLETED_ONBOARDING_KEY = booleanPreferencesKey("has_completed_onboarding")
    }

    // Calendar display settings
    override val primaryCalendar: Flow<CalendarType> = context.settingsDataStore.data.map { preferences ->
        val calendarString = preferences[PRIMARY_CALENDAR_KEY] ?: CalendarType.ETHIOPIAN.name
        try {
            CalendarType.valueOf(calendarString)
        } catch (e: IllegalArgumentException) {
            CalendarType.ETHIOPIAN
        }
    }

    override val secondaryCalendar: Flow<CalendarType> = context.settingsDataStore.data.map { preferences ->
        val calendarString = preferences[SECONDARY_CALENDAR_KEY] ?: CalendarType.GREGORIAN.name
        try {
            CalendarType.valueOf(calendarString)
        } catch (e: IllegalArgumentException) {
            CalendarType.GREGORIAN
        }
    }

    override val displayDualCalendar: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[DISPLAY_DUAL_CALENDAR_KEY] ?: false
    }

    // Holiday display settings
    override val includeAllDayOffHolidays: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[INCLUDE_ALL_DAY_OFF_HOLIDAYS_KEY] ?: true
    }

    override val includeWorkingOrthodoxHolidays: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[INCLUDE_WORKING_ORTHODOX_HOLIDAYS_KEY] ?: false
    }

    override val includeWorkingMuslimHolidays: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[INCLUDE_WORKING_MUSLIM_HOLIDAYS_KEY] ?: false
    }

    // UI preferences
    override val hideHolidayInfoDialog: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[HIDE_HOLIDAY_INFO_DIALOG_KEY] ?: false
    }

    override val useAmharicDayNames: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[USE_AMHARIC_DAY_NAMES_KEY] ?: false
    }

    override val showLunarPhases: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[SHOW_LUNAR_PHASES_KEY] ?: false
    }

    // Theme settings
    override val isDarkMode: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[IS_DARK_MODE_KEY] ?: false
    }

    override val themeColor: Flow<String> = context.settingsDataStore.data.map { preferences ->
        preferences[THEME_COLOR_KEY] ?: "#1976D2" // Default blue color
    }

    // Onboarding
    override val hasCompletedOnboarding: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[HAS_COMPLETED_ONBOARDING_KEY] ?: false
    }

    // Setters
    override suspend fun setPrimaryCalendar(type: CalendarType) {
        context.settingsDataStore.edit { preferences ->
            preferences[PRIMARY_CALENDAR_KEY] = type.name
        }
    }

    override suspend fun setSecondaryCalendar(type: CalendarType) {
        context.settingsDataStore.edit { preferences ->
            preferences[SECONDARY_CALENDAR_KEY] = type.name
        }
    }

    override suspend fun setDisplayDualCalendar(display: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[DISPLAY_DUAL_CALENDAR_KEY] = display
        }
    }

    override suspend fun setIncludeAllDayOffHolidays(include: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[INCLUDE_ALL_DAY_OFF_HOLIDAYS_KEY] = include
        }
    }

    override suspend fun setIncludeWorkingOrthodoxHolidays(include: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[INCLUDE_WORKING_ORTHODOX_HOLIDAYS_KEY] = include
        }
    }

    override suspend fun setIncludeWorkingMuslimHolidays(include: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[INCLUDE_WORKING_MUSLIM_HOLIDAYS_KEY] = include
        }
    }

    override suspend fun setHideHolidayInfoDialog(hide: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[HIDE_HOLIDAY_INFO_DIALOG_KEY] = hide
        }
    }

    override suspend fun setUseAmharicDayNames(use: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[USE_AMHARIC_DAY_NAMES_KEY] = use
        }
    }

    override suspend fun setShowLunarPhases(show: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SHOW_LUNAR_PHASES_KEY] = show
        }
    }

    override suspend fun setIsDarkMode(isDark: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[IS_DARK_MODE_KEY] = isDark
        }
    }

    override suspend fun setThemeColor(color: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[THEME_COLOR_KEY] = color
        }
    }

    override suspend fun setHasCompletedOnboarding(completed: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[HAS_COMPLETED_ONBOARDING_KEY] = completed
        }
    }
}
