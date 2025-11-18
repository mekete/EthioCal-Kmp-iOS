package com.shalom.calendar.alarm

import com.shalom.calendar.data.local.entity.EventEntity

/**
 * Interface for scheduling event reminder alarms.
 * Platform-specific implementations handle the actual alarm/notification scheduling.
 */
interface AlarmScheduler {
    /**
     * Schedule an alarm for an event.
     *
     * @param event The event to schedule an alarm for
     * @return true if alarm was scheduled successfully, false otherwise
     */
    fun scheduleAlarm(event: EventEntity): Boolean

    /**
     * Cancel an alarm for an event.
     *
     * @param eventId The ID of the event
     */
    fun cancelAlarm(eventId: String)

    /**
     * Check if the app can schedule exact alarms.
     *
     * @return true if exact alarms can be scheduled
     */
    fun canScheduleExactAlarms(): Boolean
}
