package com.shalom.calendar.alarm

import com.shalom.calendar.data.local.entity.EventEntity

/**
 * iOS implementation of AlarmScheduler.
 *
 * Note: iOS handles notifications differently through UNUserNotificationCenter.
 * This is a placeholder implementation that can be enhanced later with iOS-specific
 * notification scheduling using UNUserNotificationCenter.
 *
 * TODO: Implement actual iOS notification scheduling using:
 * - UNUserNotificationCenter for scheduling local notifications
 * - UNTimeIntervalNotificationTrigger for time-based triggers
 * - UNCalendarNotificationTrigger for date-based triggers
 */
class AlarmSchedulerImpl : AlarmScheduler {

    override fun scheduleAlarm(event: EventEntity): Boolean {
        // TODO: Implement iOS notification scheduling
        // Use UNUserNotificationCenter.current().add(request) { error in ... }
        return false
    }

    override fun cancelAlarm(eventId: String) {
        // TODO: Implement iOS notification cancellation
        // Use UNUserNotificationCenter.current().removePendingNotificationRequests(withIdentifiers:)
    }

    override fun canScheduleExactAlarms(): Boolean {
        // iOS always allows local notifications once permission is granted
        // The permission check should be done at app startup
        return true
    }
}
