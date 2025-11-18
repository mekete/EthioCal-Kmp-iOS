package com.shalom.calendar.data.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Foundation.NSUserDefaults

/**
 * iOS implementation of SettingsPreferences using NSUserDefaults.
 * This provides persistent storage for app settings on iOS.
 */
class SettingsPreferencesImpl : SettingsPreferences {

    private val userDefaults = NSUserDefaults.standardUserDefaults

    companion object {
        // Preference keys
        private const val PRIMARY_CALENDAR_KEY = "primary_calendar"
        private const val SECONDARY_CALENDAR_KEY = "secondary_calendar"
        private const val DISPLAY_DUAL_CALENDAR_KEY = "display_dual_calendar"

        private const val INCLUDE_ALL_DAY_OFF_HOLIDAYS_KEY = "show_public_holidays"
        private const val INCLUDE_WORKING_ORTHODOX_HOLIDAYS_KEY = "show_orthodox_fasting_holidays"
        private const val INCLUDE_WORKING_MUSLIM_HOLIDAYS_KEY = "show_muslim_holidays"

        private const val HIDE_HOLIDAY_INFO_DIALOG_KEY = "hide_holiday_info_dialog"
        private const val USE_AMHARIC_DAY_NAMES_KEY = "show_orthodox_day_names"
        private const val SHOW_LUNAR_PHASES_KEY = "show_lunar_phases"

        private const val IS_DARK_MODE_KEY = "is_dark_mode"
        private const val THEME_COLOR_KEY = "theme_color"

        private const val HAS_COMPLETED_ONBOARDING_KEY = "has_completed_onboarding"
    }

    // State flows for reactive updates
    private val _primaryCalendar = MutableStateFlow(getCalendarType(PRIMARY_CALENDAR_KEY, CalendarType.ETHIOPIAN))
    private val _secondaryCalendar = MutableStateFlow(getCalendarType(SECONDARY_CALENDAR_KEY, CalendarType.GREGORIAN))
    private val _displayDualCalendar = MutableStateFlow(getBoolean(DISPLAY_DUAL_CALENDAR_KEY, false))

    private val _includeAllDayOffHolidays = MutableStateFlow(getBoolean(INCLUDE_ALL_DAY_OFF_HOLIDAYS_KEY, true))
    private val _includeWorkingOrthodoxHolidays = MutableStateFlow(getBoolean(INCLUDE_WORKING_ORTHODOX_HOLIDAYS_KEY, false))
    private val _includeWorkingMuslimHolidays = MutableStateFlow(getBoolean(INCLUDE_WORKING_MUSLIM_HOLIDAYS_KEY, false))

    private val _hideHolidayInfoDialog = MutableStateFlow(getBoolean(HIDE_HOLIDAY_INFO_DIALOG_KEY, false))
    private val _useAmharicDayNames = MutableStateFlow(getBoolean(USE_AMHARIC_DAY_NAMES_KEY, false))
    private val _showLunarPhases = MutableStateFlow(getBoolean(SHOW_LUNAR_PHASES_KEY, false))

    private val _isDarkMode = MutableStateFlow(getBoolean(IS_DARK_MODE_KEY, false))
    private val _themeColor = MutableStateFlow(getString(THEME_COLOR_KEY, "#1976D2"))

    private val _hasCompletedOnboarding = MutableStateFlow(getBoolean(HAS_COMPLETED_ONBOARDING_KEY, false))

    // Calendar display settings
    override val primaryCalendar: Flow<CalendarType> = _primaryCalendar.asStateFlow()
    override val secondaryCalendar: Flow<CalendarType> = _secondaryCalendar.asStateFlow()
    override val displayDualCalendar: Flow<Boolean> = _displayDualCalendar.asStateFlow()

    // Holiday display settings
    override val includeAllDayOffHolidays: Flow<Boolean> = _includeAllDayOffHolidays.asStateFlow()
    override val includeWorkingOrthodoxHolidays: Flow<Boolean> = _includeWorkingOrthodoxHolidays.asStateFlow()
    override val includeWorkingMuslimHolidays: Flow<Boolean> = _includeWorkingMuslimHolidays.asStateFlow()

    // UI preferences
    override val hideHolidayInfoDialog: Flow<Boolean> = _hideHolidayInfoDialog.asStateFlow()
    override val useAmharicDayNames: Flow<Boolean> = _useAmharicDayNames.asStateFlow()
    override val showLunarPhases: Flow<Boolean> = _showLunarPhases.asStateFlow()

    // Theme settings
    override val isDarkMode: Flow<Boolean> = _isDarkMode.asStateFlow()
    override val themeColor: Flow<String> = _themeColor.asStateFlow()

    // Onboarding
    override val hasCompletedOnboarding: Flow<Boolean> = _hasCompletedOnboarding.asStateFlow()

    // Helper functions
    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return userDefaults.boolForKey(key).takeIf { userDefaults.objectForKey(key) != null } ?: defaultValue
    }

    private fun getString(key: String, defaultValue: String): String {
        return userDefaults.stringForKey(key) ?: defaultValue
    }

    private fun getCalendarType(key: String, defaultValue: CalendarType): CalendarType {
        val stringValue = userDefaults.stringForKey(key) ?: return defaultValue
        return try {
            CalendarType.valueOf(stringValue)
        } catch (e: IllegalArgumentException) {
            defaultValue
        }
    }

    // Setters
    override suspend fun setPrimaryCalendar(type: CalendarType) {
        userDefaults.setObject(type.name, PRIMARY_CALENDAR_KEY)
        userDefaults.synchronize()
        _primaryCalendar.value = type
    }

    override suspend fun setSecondaryCalendar(type: CalendarType) {
        userDefaults.setObject(type.name, SECONDARY_CALENDAR_KEY)
        userDefaults.synchronize()
        _secondaryCalendar.value = type
    }

    override suspend fun setDisplayDualCalendar(display: Boolean) {
        userDefaults.setBool(display, DISPLAY_DUAL_CALENDAR_KEY)
        userDefaults.synchronize()
        _displayDualCalendar.value = display
    }

    override suspend fun setIncludeAllDayOffHolidays(include: Boolean) {
        userDefaults.setBool(include, INCLUDE_ALL_DAY_OFF_HOLIDAYS_KEY)
        userDefaults.synchronize()
        _includeAllDayOffHolidays.value = include
    }

    override suspend fun setIncludeWorkingOrthodoxHolidays(include: Boolean) {
        userDefaults.setBool(include, INCLUDE_WORKING_ORTHODOX_HOLIDAYS_KEY)
        userDefaults.synchronize()
        _includeWorkingOrthodoxHolidays.value = include
    }

    override suspend fun setIncludeWorkingMuslimHolidays(include: Boolean) {
        userDefaults.setBool(include, INCLUDE_WORKING_MUSLIM_HOLIDAYS_KEY)
        userDefaults.synchronize()
        _includeWorkingMuslimHolidays.value = include
    }

    override suspend fun setHideHolidayInfoDialog(hide: Boolean) {
        userDefaults.setBool(hide, HIDE_HOLIDAY_INFO_DIALOG_KEY)
        userDefaults.synchronize()
        _hideHolidayInfoDialog.value = hide
    }

    override suspend fun setUseAmharicDayNames(use: Boolean) {
        userDefaults.setBool(use, USE_AMHARIC_DAY_NAMES_KEY)
        userDefaults.synchronize()
        _useAmharicDayNames.value = use
    }

    override suspend fun setShowLunarPhases(show: Boolean) {
        userDefaults.setBool(show, SHOW_LUNAR_PHASES_KEY)
        userDefaults.synchronize()
        _showLunarPhases.value = show
    }

    override suspend fun setIsDarkMode(isDark: Boolean) {
        userDefaults.setBool(isDark, IS_DARK_MODE_KEY)
        userDefaults.synchronize()
        _isDarkMode.value = isDark
    }

    override suspend fun setThemeColor(color: String) {
        userDefaults.setObject(color, THEME_COLOR_KEY)
        userDefaults.synchronize()
        _themeColor.value = color
    }

    override suspend fun setHasCompletedOnboarding(completed: Boolean) {
        userDefaults.setBool(completed, HAS_COMPLETED_ONBOARDING_KEY)
        userDefaults.synchronize()
        _hasCompletedOnboarding.value = completed
    }
}
