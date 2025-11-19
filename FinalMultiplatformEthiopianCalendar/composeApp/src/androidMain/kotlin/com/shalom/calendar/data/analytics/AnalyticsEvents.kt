package com.shalom.calendar.data.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Sealed class representing all trackable analytics events in the app.
 * Each event has a name and parameters bundle for type-safe event logging.
 */
sealed class AnalyticsEvent {
    abstract val eventName: String
    abstract val parameters: Bundle

    // ==================== Application Lifecycle ====================

    data class AppOpen(val source: String) : AnalyticsEvent() {
        override val eventName = "app_open"
        override val parameters = bundleOf("source" to source)
    }

    data class AppBackground(val sessionDuration: Long) : AnalyticsEvent() {
        override val eventName = "app_background"
        override val parameters = bundleOf("session_duration" to sessionDuration)
    }

    data class AppForeground(val source: String) : AnalyticsEvent() {
        override val eventName = "app_foreground"
        override val parameters = bundleOf("source" to source)
    }

    data class SessionStart(val timestamp: Long) : AnalyticsEvent() {
        override val eventName = "session_start"
        override val parameters = bundleOf("timestamp" to timestamp)
    }

    data class SessionEnd(val duration: Long) : AnalyticsEvent() {
        override val eventName = "session_end"
        override val parameters = bundleOf("duration" to duration)
    }

    // ==================== Screen Navigation ====================

    data class ScreenView(
        val screenName: String,
        val screenClass: String,
        val previousScreen: String? = null
    ) : AnalyticsEvent() {
        override val eventName = FirebaseAnalytics.Event.SCREEN_VIEW
        override val parameters = bundleOf(
            FirebaseAnalytics.Param.SCREEN_NAME to screenName,
            FirebaseAnalytics.Param.SCREEN_CLASS to screenClass,
            "previous_screen" to previousScreen
        )
    }

    // ==================== Onboarding ====================

    object OnboardingStart : AnalyticsEvent() {
        override val eventName = "onboarding_start"
        override val parameters = Bundle()
    }

    data class OnboardingPageView(
        val pageNumber: Int,
        val pageName: String
    ) : AnalyticsEvent() {
        override val eventName = "onboarding_page_view"
        override val parameters = bundleOf(
            "page_number" to pageNumber,
            "page_name" to pageName
        )
    }

    data class OnboardingLanguageSelected(val language: String) : AnalyticsEvent() {
        override val eventName = "onboarding_language_selected"
        override val parameters = bundleOf("language" to language)
    }

    data class OnboardingThemeSelected(
        val theme: String,
        val mode: String
    ) : AnalyticsEvent() {
        override val eventName = "onboarding_theme_selected"
        override val parameters = bundleOf(
            "theme" to theme,
            "mode" to mode
        )
    }

    data class OnboardingHolidaysConfigured(
        val publicEnabled: Boolean,
        val orthodoxEnabled: Boolean,
        val muslimEnabled: Boolean
    ) : AnalyticsEvent() {
        override val eventName = "onboarding_holidays_configured"
        override val parameters = bundleOf(
            "public_enabled" to publicEnabled,
            "orthodox_enabled" to orthodoxEnabled,
            "muslim_enabled" to muslimEnabled
        )
    }

    data class OnboardingCalendarSelected(
        val primaryCalendar: String,
        val dualEnabled: Boolean
    ) : AnalyticsEvent() {
        override val eventName = "onboarding_calendar_selected"
        override val parameters = bundleOf(
            "primary_calendar" to primaryCalendar,
            "dual_enabled" to dualEnabled
        )
    }

    data class OnboardingCompleted(val duration: Long) : AnalyticsEvent() {
        override val eventName = "onboarding_completed"
        override val parameters = bundleOf("duration" to duration)
    }

    data class OnboardingSkipped(val lastPage: Int) : AnalyticsEvent() {
        override val eventName = "onboarding_skipped"
        override val parameters = bundleOf("last_page" to lastPage)
    }

    // ==================== Month Calendar ====================

    data class CalendarViewMode(
        val calendarType: String,
        val isDual: Boolean
    ) : AnalyticsEvent() {
        override val eventName = "calendar_view_mode"
        override val parameters = bundleOf(
            "calendar_type" to calendarType,
            "is_dual" to isDual
        )
    }

    data class DateCellClicked(
        val hasEvents: Boolean,
        val hasHolidays: Boolean
    ) : AnalyticsEvent() {
        override val eventName = "date_cell_clicked"
        override val parameters = bundleOf(
            "has_events" to hasEvents,
            "has_holidays" to hasHolidays
        )
    }

    object DateDetailsDialogOpened : AnalyticsEvent() {
        override val eventName = "date_details_dialog_opened"
        override val parameters = Bundle()
    }

    data class MonthNavigated(
        val direction: String, // "forward" or "back"
        val offset: Int
    ) : AnalyticsEvent() {
        override val eventName = "month_navigated"
        override val parameters = bundleOf(
            "direction" to direction,
            "offset" to offset
        )
    }

    object TodayButtonClicked : AnalyticsEvent() {
        override val eventName = "today_button_clicked"
        override val parameters = Bundle()
    }

    data class NavigateToEventsFromCalendar(val date: String) : AnalyticsEvent() {
        override val eventName = "navigate_to_events_from_calendar"
        override val parameters = bundleOf("date" to date)
    }

    data class HolidayViewedFromCalendar(val holidayType: String) : AnalyticsEvent() {
        override val eventName = "holiday_viewed_from_calendar"
        override val parameters = bundleOf("holiday_type" to holidayType)
    }

    // ==================== Events ====================

    data class EventCreateOpened(val source: String) : AnalyticsEvent() {
        override val eventName = "event_create_opened"
        override val parameters = bundleOf("source" to source)
    }

    data class EventCreated(
        val isAllDay: Boolean,
        val hasReminder: Boolean,
        val reminderMinutes: Int,
        val isRecurring: Boolean,
        val recurrenceType: String?,
        val hasDescription: Boolean
    ) : AnalyticsEvent() {
        override val eventName = "event_created"
        override val parameters = bundleOf(
            "is_all_day" to isAllDay,
            "has_reminder" to hasReminder,
            "reminder_minutes" to reminderMinutes,
            "is_recurring" to isRecurring,
            "recurrence_type" to recurrenceType,
            "has_description" to hasDescription
        )
    }

    data class EventEdited(
        val eventId: String,
        val changedFields: List<String>
    ) : AnalyticsEvent() {
        override val eventName = "event_edited"
        override val parameters = bundleOf(
            "event_id" to eventId,
            "changed_fields" to changedFields.joinToString(",")
        )
    }

    data class EventDeleted(
        val eventId: String,
        val wasRecurring: Boolean
    ) : AnalyticsEvent() {
        override val eventName = "event_deleted"
        override val parameters = bundleOf(
            "event_id" to eventId,
            "was_recurring" to wasRecurring
        )
    }

    data class EventFilterApplied(
        val hasStart: Boolean,
        val hasEnd: Boolean
    ) : AnalyticsEvent() {
        override val eventName = "event_filter_applied"
        override val parameters = bundleOf(
            "has_start" to hasStart,
            "has_end" to hasEnd
        )
    }

    object EventFilterCleared : AnalyticsEvent() {
        override val eventName = "event_filter_cleared"
        override val parameters = Bundle()
    }

    data class EventReminderSet(val minutesBefore: Int) : AnalyticsEvent() {
        override val eventName = "event_reminder_set"
        override val parameters = bundleOf("minutes_before" to minutesBefore)
    }

    // ==================== Date Converter ====================

    data class ConversionPerformed(
        val fromCalendar: String,
        val toCalendar: String
    ) : AnalyticsEvent() {
        override val eventName = "conversion_performed"
        override val parameters = bundleOf(
            "from_calendar" to fromCalendar,
            "to_calendar" to toCalendar
        )
    }

    data class DateDifferenceCalculated(val days: Int) : AnalyticsEvent() {
        override val eventName = "date_difference_calculated"
        override val parameters = bundleOf("days" to days)
    }

    data class ConversionResultViewed(val resultType: String) : AnalyticsEvent() {
        override val eventName = "conversion_result_viewed"
        override val parameters = bundleOf("result_type" to resultType)
    }

    // ==================== Holiday List ====================

    data class HolidayListViewed(val year: Int) : AnalyticsEvent() {
        override val eventName = "holiday_list_viewed"
        override val parameters = bundleOf("year" to year)
    }

    data class HolidayYearChanged(val direction: String) : AnalyticsEvent() {
        override val eventName = "holiday_year_changed"
        override val parameters = bundleOf("direction" to direction)
    }

    data class HolidayDetailsViewed(
        val holidayType: String,
        val holidayName: String
    ) : AnalyticsEvent() {
        override val eventName = "holiday_details_viewed"
        override val parameters = bundleOf(
            "holiday_type" to holidayType,
            "holiday_name" to holidayName
        )
    }

    object HolidayInfoDialogOpened : AnalyticsEvent() {
        override val eventName = "holiday_info_dialog_opened"
        override val parameters = Bundle()
    }

    // ==================== Settings ====================

    data class SettingsLanguageChanged(
        val fromLanguage: String,
        val toLanguage: String
    ) : AnalyticsEvent() {
        override val eventName = "settings_language_changed"
        override val parameters = bundleOf(
            "from_language" to fromLanguage,
            "to_language" to toLanguage
        )
    }

    data class SettingsThemeChanged(val theme: String) : AnalyticsEvent() {
        override val eventName = "settings_theme_changed"
        override val parameters = bundleOf("theme" to theme)
    }

    data class SettingsThemeModeChanged(val mode: String) : AnalyticsEvent() {
        override val eventName = "settings_theme_mode_changed"
        override val parameters = bundleOf("mode" to mode)
    }

    data class SettingsCalendarPreferencesChanged(val changedField: String) : AnalyticsEvent() {
        override val eventName = "settings_calendar_preferences_changed"
        override val parameters = bundleOf("changed_field" to changedField)
    }

    data class SettingsHolidayToggled(
        val type: String,
        val enabled: Boolean
    ) : AnalyticsEvent() {
        override val eventName = "settings_holiday_toggled"
        override val parameters = bundleOf(
            "type" to type,
            "enabled" to enabled
        )
    }

    data class SettingsOrthodoxNamesToggled(val enabled: Boolean) : AnalyticsEvent() {
        override val eventName = "settings_orthodox_names_toggled"
        override val parameters = bundleOf("enabled" to enabled)
    }

    data class SettingsWidgetConfigured(val field: String) : AnalyticsEvent() {
        override val eventName = "settings_widget_configured"
        override val parameters = bundleOf("field" to field)
    }

    object SettingsNotificationsOpened : AnalyticsEvent() {
        override val eventName = "settings_notifications_opened"
        override val parameters = Bundle()
    }

    object SettingsShareClicked : AnalyticsEvent() {
        override val eventName = "settings_share_clicked"
        override val parameters = Bundle()
    }

    object SettingsAboutViewed : AnalyticsEvent() {
        override val eventName = "settings_about_viewed"
        override val parameters = Bundle()
    }

    // ==================== Permissions ====================

    data class PermissionRequested(
        val permission: String,
        val source: String
    ) : AnalyticsEvent() {
        override val eventName = "permission_requested"
        override val parameters = bundleOf(
            "permission" to permission,
            "source" to source
        )
    }

    data class PermissionGranted(val permission: String) : AnalyticsEvent() {
        override val eventName = "permission_granted"
        override val parameters = bundleOf("permission" to permission)
    }

    data class PermissionDenied(
        val permission: String,
        val permanently: Boolean
    ) : AnalyticsEvent() {
        override val eventName = "permission_denied"
        override val parameters = bundleOf(
            "permission" to permission,
            "permanently" to permanently
        )
    }

    data class PermissionRationaleShown(val permission: String) : AnalyticsEvent() {
        override val eventName = "permission_rationale_shown"
        override val parameters = bundleOf("permission" to permission)
    }

    data class PermissionDontAskSelected(
        val permission: String,
        val days: Int
    ) : AnalyticsEvent() {
        override val eventName = "permission_dont_ask_selected"
        override val parameters = bundleOf(
            "permission" to permission,
            "days" to days
        )
    }

    data class PermissionSettingsOpened(val permission: String) : AnalyticsEvent() {
        override val eventName = "permission_settings_opened"
        override val parameters = bundleOf("permission" to permission)
    }

    // ==================== Widget ====================

    object WidgetAdded : AnalyticsEvent() {
        override val eventName = "widget_added"
        override val parameters = Bundle()
    }

    object WidgetRemoved : AnalyticsEvent() {
        override val eventName = "widget_removed"
        override val parameters = Bundle()
    }

    data class WidgetRefreshed(val source: String) : AnalyticsEvent() {
        override val eventName = "widget_refreshed"
        override val parameters = bundleOf("source" to source)
    }

    data class WidgetClicked(val target: String) : AnalyticsEvent() {
        override val eventName = "widget_clicked"
        override val parameters = bundleOf("target" to target)
    }

    data class WidgetEventViewed(val fromWidget: Boolean) : AnalyticsEvent() {
        override val eventName = "widget_event_viewed"
        override val parameters = bundleOf("from_widget" to fromWidget)
    }

    // ==================== Alarms & Notifications ====================

    data class EventReminderScheduled(
        val eventId: String,
        val minutesBefore: Int
    ) : AnalyticsEvent() {
        override val eventName = "event_reminder_scheduled"
        override val parameters = bundleOf(
            "event_id" to eventId,
            "minutes_before" to minutesBefore
        )
    }

    data class EventReminderTriggered(
        val eventId: String,
        val isRecurring: Boolean
    ) : AnalyticsEvent() {
        override val eventName = "event_reminder_triggered"
        override val parameters = bundleOf(
            "event_id" to eventId,
            "is_recurring" to isRecurring
        )
    }

    data class EventReminderClicked(val eventId: String) : AnalyticsEvent() {
        override val eventName = "event_reminder_clicked"
        override val parameters = bundleOf("event_id" to eventId)
    }

    data class EventReminderDismissed(val eventId: String) : AnalyticsEvent() {
        override val eventName = "event_reminder_dismissed"
        override val parameters = bundleOf("event_id" to eventId)
    }

    data class ReminderReregistrationCompleted(val count: Int) : AnalyticsEvent() {
        override val eventName = "reminder_reregistration_completed"
        override val parameters = bundleOf("count" to count)
    }

    // ==================== Errors ====================

    data class ErrorOccurred(
        val screen: String,
        val errorType: String,
        val message: String
    ) : AnalyticsEvent() {
        override val eventName = "error_occurred"
        override val parameters = bundleOf(
            "screen" to screen,
            "error_type" to errorType,
            "message" to message
        )
    }

    data class MigrationCompleted(
        val eventCount: Int,
        val success: Boolean
    ) : AnalyticsEvent() {
        override val eventName = "migration_completed"
        override val parameters = bundleOf(
            "event_count" to eventCount,
            "success" to success
        )
    }

    data class RemoteConfigFetched(val success: Boolean) : AnalyticsEvent() {
        override val eventName = "remote_config_fetched"
        override val parameters = bundleOf("success" to success)
    }

    data class CalendarSyncAttempted(val success: Boolean) : AnalyticsEvent() {
        override val eventName = "calendar_sync_attempted"
        override val parameters = bundleOf("success" to success)
    }
}

/**
 * Analytics parameter constants
 */
object AnalyticsParams {
    // User Properties
    const val USER_LANGUAGE = "user_language"
    const val USER_THEME = "user_theme"
    const val USER_THEME_MODE = "user_theme_mode"
    const val PRIMARY_CALENDAR = "primary_calendar"
    const val DUAL_CALENDAR_ENABLED = "dual_calendar_enabled"
    const val PUBLIC_HOLIDAYS_ENABLED = "public_holidays_enabled"
    const val ORTHODOX_HOLIDAYS_ENABLED = "orthodox_holidays_enabled"
    const val MUSLIM_HOLIDAYS_ENABLED = "muslim_holidays_enabled"
    const val DEVICE_COUNTRY_CODE = "device_country_code"
    const val HAS_NOTIFICATION_PERMISSION = "has_notification_permission"
    const val HAS_EXACT_ALARM_PERMISSION = "has_exact_alarm_permission"
    const val WIDGET_INSTALLED = "widget_installed"
    const val TOTAL_EVENTS_CREATED = "total_events_created"

    // Event sources
    const val SOURCE_FAB = "fab"
    const val SOURCE_DATE_DIALOG = "date_dialog"
    const val SOURCE_CALENDAR = "calendar"
    const val SOURCE_MANUAL = "manual"
    const val SOURCE_AUTOMATIC = "automatic"
}
