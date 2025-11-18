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

    // UI preferences
    val hideHolidayInfoDialog: Flow<Boolean>
    val useAmharicDayNames: Flow<Boolean>
    val showLunarPhases: Flow<Boolean>

    // Theme settings
    val isDarkMode: Flow<Boolean>
    val themeColor: Flow<String>

    // Onboarding
    val hasCompletedOnboarding: Flow<Boolean>

    // Setters
    suspend fun setPrimaryCalendar(type: CalendarType)
    suspend fun setSecondaryCalendar(type: CalendarType)
    suspend fun setDisplayDualCalendar(display: Boolean)
    suspend fun setIncludeAllDayOffHolidays(include: Boolean)
    suspend fun setIncludeWorkingOrthodoxHolidays(include: Boolean)
    suspend fun setIncludeWorkingMuslimHolidays(include: Boolean)
    suspend fun setHideHolidayInfoDialog(hide: Boolean)
    suspend fun setUseAmharicDayNames(use: Boolean)
    suspend fun setShowLunarPhases(show: Boolean)
    suspend fun setIsDarkMode(isDark: Boolean)
    suspend fun setThemeColor(color: String)
    suspend fun setHasCompletedOnboarding(completed: Boolean)
}

/**
 * Calendar type enumeration
 */
enum class CalendarType {
    ETHIOPIAN,
    GREGORIAN
}
