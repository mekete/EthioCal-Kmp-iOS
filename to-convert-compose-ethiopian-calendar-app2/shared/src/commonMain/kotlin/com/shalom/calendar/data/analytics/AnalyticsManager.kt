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

    data class EventCreated(val category: String) :
        AnalyticsEvent("event_created", mapOf("category" to category))

    data class EventUpdated(val category: String) :
        AnalyticsEvent("event_updated", mapOf("category" to category))

    data class EventDeleted(val category: String) :
        AnalyticsEvent("event_deleted", mapOf("category" to category))

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
