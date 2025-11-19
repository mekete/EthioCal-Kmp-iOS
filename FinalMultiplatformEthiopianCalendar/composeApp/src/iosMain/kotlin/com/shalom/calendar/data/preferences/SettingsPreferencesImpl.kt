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
        // Preference keys - matching Android keys for consistency
        private const val PRIMARY_CALENDAR_KEY = "primary_calendar"
        private const val SECONDARY_CALENDAR_KEY = "secondary_calendar"
        private const val DISPLAY_DUAL_CALENDAR_KEY = "display_dual_calendar"

        private const val INCLUDE_ALL_DAY_OFF_HOLIDAYS_KEY = "show_public_holidays"
        private const val INCLUDE_WORKING_ORTHODOX_HOLIDAYS_KEY = "show_orthodox_fasting_holidays"
        private const val INCLUDE_WORKING_MUSLIM_HOLIDAYS_KEY = "show_muslim_holidays"
        private const val INCLUDE_WORKING_CULTURAL_HOLIDAYS_KEY = "show_cultural_holidays"
        private const val INCLUDE_WORKING_WESTERN_HOLIDAYS_KEY = "show_working_western_holidays"

        private const val HIDE_HOLIDAY_INFO_DIALOG_KEY = "hide_holiday_info_dialog"
        private const val USE_AMHARIC_DAY_NAMES_KEY = "show_orthodox_day_names"
        private const val SHOW_ORTHODOX_DAY_NAMES_KEY = "show_orthodox_day_names"
        private const val SHOW_LUNAR_PHASES_KEY = "show_lunar_phases"
        private const val USE_GEEZ_NUMBERS_KEY = "use_geez_numbers"
        private const val USE_24_HOUR_FORMAT_KEY = "use_24_hour_format_in_widgets"

        private const val DISPLAY_TWO_CLOCKS_KEY = "display_two_clocks"
        private const val PRIMARY_WIDGET_TIMEZONE_KEY = "primary_widget_timezone"
        private const val SECONDARY_WIDGET_TIMEZONE_KEY = "secondary_widget_timezone"
        private const val USE_TRANSPARENT_BACKGROUND_KEY = "use_transparent_background"

        private const val LANGUAGE_KEY = "app_language"
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
    private val _includeWorkingCulturalHolidays = MutableStateFlow(getBoolean(INCLUDE_WORKING_CULTURAL_HOLIDAYS_KEY, false))
    private val _includeWorkingWesternHolidays = MutableStateFlow(getBoolean(INCLUDE_WORKING_WESTERN_HOLIDAYS_KEY, false))

    private val _hideHolidayInfoDialog = MutableStateFlow(getBoolean(HIDE_HOLIDAY_INFO_DIALOG_KEY, false))
    private val _useAmharicDayNames = MutableStateFlow(getBoolean(USE_AMHARIC_DAY_NAMES_KEY, false))
    private val _showOrthodoxDayNames = MutableStateFlow(getBoolean(SHOW_ORTHODOX_DAY_NAMES_KEY, false))
    private val _showLunarPhases = MutableStateFlow(getBoolean(SHOW_LUNAR_PHASES_KEY, false))
    private val _useGeezNumbers = MutableStateFlow(getBoolean(USE_GEEZ_NUMBERS_KEY, false))
    private val _use24HourFormat = MutableStateFlow(getBoolean(USE_24_HOUR_FORMAT_KEY, false))

    private val _displayTwoClocks = MutableStateFlow(getBoolean(DISPLAY_TWO_CLOCKS_KEY, true))
    private val _primaryWidgetTimezone = MutableStateFlow(getString(PRIMARY_WIDGET_TIMEZONE_KEY, ""))
    private val _secondaryWidgetTimezone = MutableStateFlow(getString(SECONDARY_WIDGET_TIMEZONE_KEY, ""))
    private val _useTransparentBackground = MutableStateFlow(getBoolean(USE_TRANSPARENT_BACKGROUND_KEY, false))

    private val _language = MutableStateFlow(getLanguage(LANGUAGE_KEY, Language.AMHARIC))
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
    override val includeWorkingCulturalHolidays: Flow<Boolean> = _includeWorkingCulturalHolidays.asStateFlow()
    override val includeWorkingWesternHolidays: Flow<Boolean> = _includeWorkingWesternHolidays.asStateFlow()

    // UI preferences
    override val hideHolidayInfoDialog: Flow<Boolean> = _hideHolidayInfoDialog.asStateFlow()
    override val useAmharicDayNames: Flow<Boolean> = _useAmharicDayNames.asStateFlow()
    override val showOrthodoxDayNames: Flow<Boolean> = _showOrthodoxDayNames.asStateFlow()
    override val showLunarPhases: Flow<Boolean> = _showLunarPhases.asStateFlow()
    override val useGeezNumbers: Flow<Boolean> = _useGeezNumbers.asStateFlow()
    override val use24HourFormat: Flow<Boolean> = _use24HourFormat.asStateFlow()

    // Widget settings
    override val displayTwoClocks: Flow<Boolean> = _displayTwoClocks.asStateFlow()
    override val primaryWidgetTimezone: Flow<String> = _primaryWidgetTimezone.asStateFlow()
    override val secondaryWidgetTimezone: Flow<String> = _secondaryWidgetTimezone.asStateFlow()
    override val useTransparentBackground: Flow<Boolean> = _useTransparentBackground.asStateFlow()

    // Language
    override val language: Flow<Language> = _language.asStateFlow()

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

    private fun getLanguage(key: String, defaultValue: Language): Language {
        val stringValue = userDefaults.stringForKey(key) ?: return defaultValue
        return try {
            Language.valueOf(stringValue)
        } catch (e: IllegalArgumentException) {
            defaultValue
        }
    }

    // Setters - Calendar
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

    // Setters - Holidays
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

    override suspend fun setIncludeWorkingCulturalHolidays(include: Boolean) {
        userDefaults.setBool(include, INCLUDE_WORKING_CULTURAL_HOLIDAYS_KEY)
        userDefaults.synchronize()
        _includeWorkingCulturalHolidays.value = include
    }

    override suspend fun setIncludeWorkingWesternHolidays(include: Boolean) {
        userDefaults.setBool(include, INCLUDE_WORKING_WESTERN_HOLIDAYS_KEY)
        userDefaults.synchronize()
        _includeWorkingWesternHolidays.value = include
    }

    // Setters - UI
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

    override suspend fun setShowOrthodoxDayNames(show: Boolean) {
        userDefaults.setBool(show, SHOW_ORTHODOX_DAY_NAMES_KEY)
        userDefaults.synchronize()
        _showOrthodoxDayNames.value = show
    }

    override suspend fun setShowLunarPhases(show: Boolean) {
        userDefaults.setBool(show, SHOW_LUNAR_PHASES_KEY)
        userDefaults.synchronize()
        _showLunarPhases.value = show
    }

    override suspend fun setUseGeezNumbers(use: Boolean) {
        userDefaults.setBool(use, USE_GEEZ_NUMBERS_KEY)
        userDefaults.synchronize()
        _useGeezNumbers.value = use
    }

    override suspend fun setUse24HourFormat(use: Boolean) {
        userDefaults.setBool(use, USE_24_HOUR_FORMAT_KEY)
        userDefaults.synchronize()
        _use24HourFormat.value = use
    }

    // Setters - Widget
    override suspend fun setDisplayTwoClocks(display: Boolean) {
        userDefaults.setBool(display, DISPLAY_TWO_CLOCKS_KEY)
        userDefaults.synchronize()
        _displayTwoClocks.value = display
    }

    override suspend fun setPrimaryWidgetTimezone(timezone: String) {
        userDefaults.setObject(timezone, PRIMARY_WIDGET_TIMEZONE_KEY)
        userDefaults.synchronize()
        _primaryWidgetTimezone.value = timezone
    }

    override suspend fun setSecondaryWidgetTimezone(timezone: String) {
        userDefaults.setObject(timezone, SECONDARY_WIDGET_TIMEZONE_KEY)
        userDefaults.synchronize()
        _secondaryWidgetTimezone.value = timezone
    }

    override suspend fun setUseTransparentBackground(use: Boolean) {
        userDefaults.setBool(use, USE_TRANSPARENT_BACKGROUND_KEY)
        userDefaults.synchronize()
        _useTransparentBackground.value = use
    }

    // Setters - Language & Theme
    override suspend fun setLanguage(language: Language) {
        userDefaults.setObject(language.name, LANGUAGE_KEY)
        userDefaults.synchronize()
        _language.value = language
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
