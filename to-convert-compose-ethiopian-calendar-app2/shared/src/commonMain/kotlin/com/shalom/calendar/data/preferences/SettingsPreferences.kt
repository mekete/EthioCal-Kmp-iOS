package com.shalom.calendar.data.preferences

import kotlinx.coroutines.flow.Flow

/**
 * KMP interface for app settings and preferences.
 * Platform-specific implementations will use DataStore (Android) or UserDefaults (iOS).
 */
interface SettingsPreferences {
    // Calendar display settings
    val primaryCalendar: Flow<CalendarType>
    val secondaryCalendar: Flow<CalendarType>
    val displayDualCalendar: Flow<Boolean>

    // Holiday display settings
    val includeAllDayOffHolidays: Flow<Boolean>
    val includeWorkingOrthodoxHolidays: Flow<Boolean>
    val includeWorkingMuslimHolidays: Flow<Boolean>
    val includeWorkingCulturalHolidays: Flow<Boolean>
    val includeWorkingWesternHolidays: Flow<Boolean>

    // UI preferences
    val hideHolidayInfoDialog: Flow<Boolean>
    val useAmharicDayNames: Flow<Boolean>
    val showOrthodoxDayNames: Flow<Boolean>
    val showLunarPhases: Flow<Boolean>
    val useGeezNumbers: Flow<Boolean>
    val use24HourFormat: Flow<Boolean>

    // Widget settings
    val displayTwoClocks: Flow<Boolean>
    val primaryWidgetTimezone: Flow<String>
    val secondaryWidgetTimezone: Flow<String>
    val useTransparentBackground: Flow<Boolean>

    // Language
    val language: Flow<Language>

    // Theme settings
    val isDarkMode: Flow<Boolean>
    val themeColor: Flow<String>

    // Onboarding
    val hasCompletedOnboarding: Flow<Boolean>

    // Setters - Calendar
    suspend fun setPrimaryCalendar(type: CalendarType)
    suspend fun setSecondaryCalendar(type: CalendarType)
    suspend fun setDisplayDualCalendar(display: Boolean)

    // Setters - Holidays
    suspend fun setIncludeAllDayOffHolidays(include: Boolean)
    suspend fun setIncludeWorkingOrthodoxHolidays(include: Boolean)
    suspend fun setIncludeWorkingMuslimHolidays(include: Boolean)
    suspend fun setIncludeWorkingCulturalHolidays(include: Boolean)
    suspend fun setIncludeWorkingWesternHolidays(include: Boolean)

    // Setters - UI
    suspend fun setHideHolidayInfoDialog(hide: Boolean)
    suspend fun setUseAmharicDayNames(use: Boolean)
    suspend fun setShowOrthodoxDayNames(show: Boolean)
    suspend fun setShowLunarPhases(show: Boolean)
    suspend fun setUseGeezNumbers(use: Boolean)
    suspend fun setUse24HourFormat(use: Boolean)

    // Setters - Widget
    suspend fun setDisplayTwoClocks(display: Boolean)
    suspend fun setPrimaryWidgetTimezone(timezone: String)
    suspend fun setSecondaryWidgetTimezone(timezone: String)
    suspend fun setUseTransparentBackground(use: Boolean)

    // Setters - Language & Theme
    suspend fun setLanguage(language: Language)
    suspend fun setIsDarkMode(isDark: Boolean)
    suspend fun setThemeColor(color: String)

    // Setters - Onboarding
    suspend fun setHasCompletedOnboarding(completed: Boolean)
}

/**
 * Calendar type enumeration
 */
enum class CalendarType {
    ETHIOPIAN,
    GREGORIAN
}

/**
 * Language enumeration
 */
enum class Language(val displayName: String) {
    ENGLISH("English"),
    AMHARIC("አማርኛ (Amharic)"),
    OROMIFFA("Afan Oromoo"),
    TIGRIGNA("ትግርኛ (Tigrigna)"),
    FRENCH("French")
}
