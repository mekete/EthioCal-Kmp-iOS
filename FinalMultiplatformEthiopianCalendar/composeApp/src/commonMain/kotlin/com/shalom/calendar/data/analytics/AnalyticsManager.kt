package com.shalom.calendar.data.analytics

/**
 * KMP interface for analytics tracking.
 * Platform-specific implementations will use Firebase Analytics (Android/iOS).
 */
interface AnalyticsManager {
    fun logEvent(event: AnalyticsEvent)
    fun setUserProperty(key: String, value: String)
}

/**
 * Analytics events for tracking user behavior
 */
sealed class AnalyticsEvent(val name: String, val params: Map<String, Any> = emptyMap()) {
    // Screen views
    data object MonthCalendarScreenViewed : AnalyticsEvent("month_calendar_viewed")
    data object HolidayListScreenViewed : AnalyticsEvent("holiday_list_viewed")
    data object EventScreenViewed : AnalyticsEvent("event_screen_viewed")
    data object SettingsScreenViewed : AnalyticsEvent("settings_screen_viewed")
    data object DateConverterScreenViewed : AnalyticsEvent("date_converter_viewed")

    // User actions
    data class DateCellClicked(val hasEvents: Boolean, val hasHolidays: Boolean) :
        AnalyticsEvent("date_cell_clicked", mapOf("has_events" to hasEvents, "has_holidays" to hasHolidays))

    data object DateDetailsDialogOpened : AnalyticsEvent("date_details_dialog_opened")

    // Month navigation
    data class MonthNavigated(val direction: String, val offset: Int) :
        AnalyticsEvent("month_navigated", mapOf("direction" to direction, "offset" to offset))

    data object TodayButtonClicked : AnalyticsEvent("today_button_clicked")

    // Event CRUD operations with detailed tracking
    data class EventCreated(
        val isAllDay: Boolean = false,
        val hasReminder: Boolean = false,
        val reminderMinutes: Int = 0,
        val isRecurring: Boolean = false,
        val recurrenceType: String? = null,
        val hasDescription: Boolean = false,
        val category: String = "PERSONAL"
    ) : AnalyticsEvent("event_created", buildMap {
        put("is_all_day", isAllDay)
        put("has_reminder", hasReminder)
        put("reminder_minutes", reminderMinutes)
        put("is_recurring", isRecurring)
        put("has_description", hasDescription)
        put("category", category)
        recurrenceType?.let { put("recurrence_type", it) }
    })

    data class EventUpdated(val category: String) :
        AnalyticsEvent("event_updated", mapOf("category" to category))

    data class EventEdited(val eventId: String, val changedFields: List<String>) :
        AnalyticsEvent("event_edited", mapOf("event_id" to eventId, "changed_fields" to changedFields.joinToString(",")))

    data class EventDeleted(val eventId: String, val wasRecurring: Boolean) :
        AnalyticsEvent("event_deleted", mapOf("event_id" to eventId, "was_recurring" to wasRecurring))

    // Onboarding events
    data object OnboardingStart : AnalyticsEvent("onboarding_start")

    data class OnboardingPageView(val page: Int) :
        AnalyticsEvent("onboarding_page_view", mapOf("page" to page))

    data class OnboardingLanguageSelected(val language: String) :
        AnalyticsEvent("onboarding_language_selected", mapOf("language" to language))

    data class OnboardingThemeSelected(val theme: String) :
        AnalyticsEvent("onboarding_theme_selected", mapOf("theme" to theme))

    data class OnboardingHolidaysConfigured(
        val showOrthodox: Boolean,
        val showMuslim: Boolean,
        val showDayOff: Boolean
    ) : AnalyticsEvent("onboarding_holidays_configured", mapOf(
        "show_orthodox" to showOrthodox,
        "show_muslim" to showMuslim,
        "show_day_off" to showDayOff
    ))

    data class OnboardingCalendarSelected(val calendarType: String) :
        AnalyticsEvent("onboarding_calendar_selected", mapOf("calendar_type" to calendarType))

    data object OnboardingCompleted : AnalyticsEvent("onboarding_completed")

    data object OnboardingSkipped : AnalyticsEvent("onboarding_skipped")

    // Settings events
    data class SettingsCalendarPreferencesChanged(val setting: String, val value: String) :
        AnalyticsEvent("settings_calendar_preferences_changed", mapOf("setting" to setting, "value" to value))

    data class SettingsOrthodoxNamesToggled(val enabled: Boolean) :
        AnalyticsEvent("settings_orthodox_names_toggled", mapOf("enabled" to enabled))

    data class SettingsHolidayToggled(val holidayType: String, val enabled: Boolean) :
        AnalyticsEvent("settings_holiday_toggled", mapOf("holiday_type" to holidayType, "enabled" to enabled))

    data class SettingsWidgetConfigured(val setting: String, val value: String) :
        AnalyticsEvent("settings_widget_configured", mapOf("setting" to setting, "value" to value))

    data class SettingsLanguageChanged(val language: String) :
        AnalyticsEvent("settings_language_changed", mapOf("language" to language))

    data class SettingsThemeChanged(val theme: String) :
        AnalyticsEvent("settings_theme_changed", mapOf("theme" to theme))

    data class SettingsThemeModeChanged(val isDarkMode: Boolean) :
        AnalyticsEvent("settings_theme_mode_changed", mapOf("is_dark_mode" to isDarkMode))

    data class HolidayFilterChanged(val filterType: String, val enabled: Boolean) :
        AnalyticsEvent("holiday_filter_changed", mapOf("filter_type" to filterType, "enabled" to enabled))

    data class CalendarTypeChanged(val calendarType: String) :
        AnalyticsEvent("calendar_type_changed", mapOf("calendar_type" to calendarType))

    data class ThemeChanged(val isDarkMode: Boolean, val color: String) :
        AnalyticsEvent("theme_changed", mapOf("is_dark_mode" to isDarkMode, "color" to color))

    // Custom events
    data class CustomEvent(val eventName: String, val parameters: Map<String, Any> = emptyMap()) :
        AnalyticsEvent(eventName, parameters)
}

/**
 * No-op implementation for when analytics is disabled
 */
class NoOpAnalyticsManager : AnalyticsManager {
    override fun logEvent(event: AnalyticsEvent) {
        // No-op
    }

    override fun setUserProperty(key: String, value: String) {
        // No-op
    }
}
