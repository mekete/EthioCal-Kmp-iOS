package com.shalom.calendar.data.preferences

import android.content.Context
import android.util.Log
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
        private val INCLUDE_WORKING_CULTURAL_HOLIDAYS_KEY = booleanPreferencesKey("show_cultural_holidays")
        private val INCLUDE_WORKING_WESTERN_HOLIDAYS_KEY = booleanPreferencesKey("show_working_western_holidays")

        private val HIDE_HOLIDAY_INFO_DIALOG_KEY = booleanPreferencesKey("hide_holiday_info_dialog")
        private val USE_AMHARIC_DAY_NAMES_KEY = booleanPreferencesKey("show_orthodox_day_names")
        private val SHOW_ORTHODOX_DAY_NAMES_KEY = booleanPreferencesKey("show_orthodox_day_names")
        private val SHOW_LUNAR_PHASES_KEY = booleanPreferencesKey("show_lunar_phases")
        private val USE_GEEZ_NUMBERS_KEY = booleanPreferencesKey("use_geez_numbers")
        private val USE_24_HOUR_FORMAT_KEY = booleanPreferencesKey("use_24_hour_format_in_widgets")

        private val DISPLAY_TWO_CLOCKS_KEY = booleanPreferencesKey("display_two_clocks")
        private val PRIMARY_WIDGET_TIMEZONE_KEY = stringPreferencesKey("primary_widget_timezone")
        private val SECONDARY_WIDGET_TIMEZONE_KEY = stringPreferencesKey("secondary_widget_timezone")
        private val USE_TRANSPARENT_BACKGROUND_KEY = booleanPreferencesKey("use_transparent_background")

        private val LANGUAGE_KEY = stringPreferencesKey("app_language")

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

    override val includeWorkingCulturalHolidays: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[INCLUDE_WORKING_CULTURAL_HOLIDAYS_KEY] ?: false
    }

    override val includeWorkingWesternHolidays: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[INCLUDE_WORKING_WESTERN_HOLIDAYS_KEY] ?: false
    }

    // UI preferences
    override val hideHolidayInfoDialog: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[HIDE_HOLIDAY_INFO_DIALOG_KEY] ?: false
    }

    override val useAmharicDayNames: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[USE_AMHARIC_DAY_NAMES_KEY] ?: false
    }

    override val showOrthodoxDayNames: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[SHOW_ORTHODOX_DAY_NAMES_KEY] ?: false
    }

    override val showLunarPhases: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[SHOW_LUNAR_PHASES_KEY] ?: false
    }

    override val useGeezNumbers: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[USE_GEEZ_NUMBERS_KEY] ?: false
    }

    override val use24HourFormat: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[USE_24_HOUR_FORMAT_KEY] ?: false
    }

    // Widget settings
    override val displayTwoClocks: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[DISPLAY_TWO_CLOCKS_KEY] ?: true
    }

    override val primaryWidgetTimezone: Flow<String> = context.settingsDataStore.data.map { preferences ->
        preferences[PRIMARY_WIDGET_TIMEZONE_KEY] ?: ""
    }

    override val secondaryWidgetTimezone: Flow<String> = context.settingsDataStore.data.map { preferences ->
        preferences[SECONDARY_WIDGET_TIMEZONE_KEY] ?: ""
    }

    override val useTransparentBackground: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[USE_TRANSPARENT_BACKGROUND_KEY] ?: false
    }

    // Language
    override val language: Flow<Language> = context.settingsDataStore.data.map { preferences ->
        val languageString = preferences[LANGUAGE_KEY] ?: Language.AMHARIC.name
        val result = try {
            Language.valueOf(languageString)
        } catch (e: IllegalArgumentException) {
            Language.AMHARIC
        }
        Log.d("CHECK-LANG-ONBOARDING", "SettingsPreferencesImpl.language Flow emitting: ${result.name}")
        result
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

    override suspend fun setIncludeWorkingCulturalHolidays(include: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[INCLUDE_WORKING_CULTURAL_HOLIDAYS_KEY] = include
        }
    }

    override suspend fun setIncludeWorkingWesternHolidays(include: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[INCLUDE_WORKING_WESTERN_HOLIDAYS_KEY] = include
        }
    }

    // UI Setters
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

    override suspend fun setShowOrthodoxDayNames(show: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SHOW_ORTHODOX_DAY_NAMES_KEY] = show
        }
    }

    override suspend fun setShowLunarPhases(show: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SHOW_LUNAR_PHASES_KEY] = show
        }
    }

    override suspend fun setUseGeezNumbers(use: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[USE_GEEZ_NUMBERS_KEY] = use
        }
    }

    override suspend fun setUse24HourFormat(use: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[USE_24_HOUR_FORMAT_KEY] = use
        }
    }

    // Widget Setters
    override suspend fun setDisplayTwoClocks(display: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[DISPLAY_TWO_CLOCKS_KEY] = display
        }
    }

    override suspend fun setPrimaryWidgetTimezone(timezone: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[PRIMARY_WIDGET_TIMEZONE_KEY] = timezone
        }
    }

    override suspend fun setSecondaryWidgetTimezone(timezone: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[SECONDARY_WIDGET_TIMEZONE_KEY] = timezone
        }
    }

    override suspend fun setUseTransparentBackground(use: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[USE_TRANSPARENT_BACKGROUND_KEY] = use
        }
    }

    // Language & Theme Setters
    override suspend fun setLanguage(language: Language) {
        Log.d("CHECK-LANG-ONBOARDING", "SettingsPreferencesImpl.setLanguage() called with: ${language.name}")
        context.settingsDataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language.name
            Log.d("CHECK-LANG-ONBOARDING", "SettingsPreferencesImpl.setLanguage() saved to DataStore: ${language.name}")
        }

        // Also save to SharedPreferences for MainActivity.attachBaseContext to read
        context.getSharedPreferences("language_prefs", android.content.Context.MODE_PRIVATE)
            .edit()
            .putString("language_code", language.localeTag)
            .apply()
        Log.d("CHECK-LANG-ONBOARDING", "SettingsPreferencesImpl.setLanguage() saved to SharedPrefs: ${language.localeTag}")
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
