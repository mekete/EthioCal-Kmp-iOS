package com.shalom.calendar.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shalom.calendar.data.analytics.AnalyticsEvent
import com.shalom.calendar.data.analytics.AnalyticsManager
import com.shalom.calendar.data.preferences.CalendarType
import com.shalom.calendar.data.preferences.Language
import com.shalom.calendar.data.preferences.SettingsPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for Settings screen.
 * Exposes settings preferences as StateFlows and provides setter methods.
 */
class SettingsViewModel(
    private val settingsPreferences: SettingsPreferences,
    private val analyticsManager: AnalyticsManager
) : ViewModel() {

    // Calendar Display Settings
    val primaryCalendar: StateFlow<CalendarType> = settingsPreferences.primaryCalendar.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalendarType.ETHIOPIAN
    )

    val displayDualCalendar: StateFlow<Boolean> = settingsPreferences.displayDualCalendar.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val secondaryCalendar: StateFlow<CalendarType> = settingsPreferences.secondaryCalendar.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalendarType.GREGORIAN
    )

    val showOrthodoxDayNames: StateFlow<Boolean> = settingsPreferences.showOrthodoxDayNames.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val showPublicHolidays: StateFlow<Boolean> = settingsPreferences.includeAllDayOffHolidays.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    val showOrthodoxFastingHolidays: StateFlow<Boolean> = settingsPreferences.includeWorkingOrthodoxHolidays.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val showMuslimHolidays: StateFlow<Boolean> = settingsPreferences.includeWorkingMuslimHolidays.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val showCulturalHolidays: StateFlow<Boolean> = settingsPreferences.includeWorkingCulturalHolidays.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val showUsHolidays: StateFlow<Boolean> = settingsPreferences.includeWorkingWesternHolidays.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val useGeezNumbers: StateFlow<Boolean> = settingsPreferences.useGeezNumbers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val use24HourFormat: StateFlow<Boolean> = settingsPreferences.use24HourFormat.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    // Widget Settings
    val displayTwoClocks: StateFlow<Boolean> = settingsPreferences.displayTwoClocks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val primaryWidgetTimezone: StateFlow<String> = settingsPreferences.primaryWidgetTimezone.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val secondaryWidgetTimezone: StateFlow<String> = settingsPreferences.secondaryWidgetTimezone.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val useTransparentBackground: StateFlow<Boolean> = settingsPreferences.useTransparentBackground.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val language: StateFlow<Language> = settingsPreferences.language
        .onEach { lang ->
            println("CHECK-LANG-ONBOARDING: SettingsViewModel.language Flow emitted: ${lang.name}")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Language.AMHARIC
        )

    init {
        println("CHECK-LANG-ONBOARDING: SettingsViewModel created, instance: ${this.hashCode()}")
    }

    // Setter functions for Calendar Display Settings
    fun setPrimaryCalendar(calendar: CalendarType) {
        viewModelScope.launch {
            settingsPreferences.setPrimaryCalendar(calendar)

            // Track calendar preference change
            analyticsManager.logEvent(
                AnalyticsEvent.SettingsCalendarPreferencesChanged(
                    changedField = "primary_calendar_${calendar.name}"
                )
            )
        }
    }

    fun setDisplayDualCalendar(value: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setDisplayDualCalendar(value)

            // Track dual calendar toggle
            analyticsManager.logEvent(
                AnalyticsEvent.SettingsCalendarPreferencesChanged(
                    changedField = "dual_calendar_${value}"
                )
            )
        }
    }

    fun setSecondaryCalendar(calendar: CalendarType) {
        viewModelScope.launch {
            settingsPreferences.setSecondaryCalendar(calendar)

            // Track secondary calendar change
            analyticsManager.logEvent(
                AnalyticsEvent.SettingsCalendarPreferencesChanged(
                    changedField = "secondary_calendar_${calendar.name}"
                )
            )
        }
    }

    fun setShowOrthodoxDayNames(value: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setShowOrthodoxDayNames(value)

            // Track Orthodox day names toggle
            analyticsManager.logEvent(
                AnalyticsEvent.SettingsOrthodoxNamesToggled(enabled = value)
            )
        }
    }

    fun setShowPublicHolidays(value: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setIncludeAllDayOffHolidays(value)

            // Track holiday toggle
            analyticsManager.logEvent(
                AnalyticsEvent.SettingsHolidayToggled(
                    type = "public",
                    enabled = value
                )
            )
        }
    }

    fun setShowOrthodoxFastingHolidays(value: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setIncludeWorkingOrthodoxHolidays(value)

            // Track holiday toggle
            analyticsManager.logEvent(
                AnalyticsEvent.SettingsHolidayToggled(
                    type = "orthodox",
                    enabled = value
                )
            )
        }
    }

    fun setShowMuslimHolidays(value: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setIncludeWorkingMuslimHolidays(value)

            // Track holiday toggle
            analyticsManager.logEvent(
                AnalyticsEvent.SettingsHolidayToggled(
                    type = "muslim",
                    enabled = value
                )
            )
        }
    }

    fun setShowCulturalHolidays(value: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setIncludeWorkingCulturalHolidays(value)
        }
    }

    fun setShowUsHolidays(value: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setIncludeWorkingWesternHolidays(value)
        }
    }

    fun setUseGeezNumbers(value: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setUseGeezNumbers(value)
        }
    }

    fun setUse24HourFormat(value: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setUse24HourFormat(value)
        }
    }

    // Setter functions for Widget Settings
    fun setDisplayTwoClocks(value: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setDisplayTwoClocks(value)

            // Track widget setting
            analyticsManager.logEvent(
                AnalyticsEvent.SettingsWidgetConfigured(field = "display_two_clocks_$value")
            )
        }
    }

    fun setPrimaryWidgetTimezone(value: String) {
        viewModelScope.launch {
            settingsPreferences.setPrimaryWidgetTimezone(value)

            // Track widget timezone setting
            analyticsManager.logEvent(
                AnalyticsEvent.SettingsWidgetConfigured(field = "primary_timezone")
            )
        }
    }

    fun setSecondaryWidgetTimezone(value: String) {
        viewModelScope.launch {
            settingsPreferences.setSecondaryWidgetTimezone(value)

            // Track widget timezone setting
            analyticsManager.logEvent(
                AnalyticsEvent.SettingsWidgetConfigured(field = "secondary_timezone")
            )
        }
    }

    fun setUseTransparentBackground(value: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setUseTransparentBackground(value)

            // Track widget setting
            analyticsManager.logEvent(
                AnalyticsEvent.SettingsWidgetConfigured(field = "transparent_background_$value")
            )
        }
    }

    fun setLanguage(language: Language) {
        viewModelScope.launch {
            val oldLanguage = settingsPreferences.language.first()
            settingsPreferences.setLanguage(language)

            // Track language change
            analyticsManager.logEvent(
                AnalyticsEvent.SettingsLanguageChanged(
                    fromLanguage = oldLanguage.name,
                    toLanguage = language.name
                )
            )
        }
    }
}
