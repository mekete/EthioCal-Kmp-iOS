package com.shalom.calendar.data.initialization

import android.content.Context
import com.shalom.calendar.alarm.AlarmScheduler
import com.shalom.calendar.data.local.dao.EventDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages re-registration of event reminders on app launch.
 *
 * This ensures that alarms persist across:
 * - App updates
 * - Device reboots (in addition to BootCompleteReceiver)
 * - System alarm clearing
 *
 * 1.5 User Preferences and Reminders:
 * - Load reminder/alarm schedules from database
 * - Re-register them if necessary to ensure persisted alarms survive reboot or version upgrade
 */
@Singleton
class ReminderReregistrationManager @Inject constructor(
    @ApplicationContext private val context: Context, private val eventDao: EventDao
) {
    private val alarmScheduler = AlarmScheduler(context)
    private val applicationScope = CoroutineScope(Dispatchers.IO)

    /**
     * Re-register all event reminders that have future occurrences.
     * Should be called during app initialization.
     */
    fun reregisterReminders() {
        applicationScope.launch {
            try { // Get all events that have reminders enabled
                val eventsWithReminders = eventDao.getAllEvents().filter { it.reminderMinutesBefore != null }

                var rescheduledCount = 0
                var skippedCount = 0

                eventsWithReminders.forEach { event ->
                    try { // Check if event is in the future or is recurring
                        val shouldReschedule = when { // Recurring events - always reschedule
                            event.recurrenceRule != null -> true

                            // One-time events - only if in the future
                            event.startTime.isAfter(ZonedDateTime.now()) -> true

                            // Past one-time events - skip
                            else -> false
                        }

                        if (shouldReschedule) {
                            val scheduled = alarmScheduler.scheduleAlarm(event)
                            if (scheduled) {
                                rescheduledCount++
                            } else {
                                skippedCount++
                            }
                        } else {
                            skippedCount++
                        }
                    } catch (e: Exception) {
                        skippedCount++
                    }
                }

            } catch (e: Exception) {
            }
        }
    }

    /**
     * Check if exact alarms can be scheduled.
     * On Android 12+, this requires SCHEDULE_EXACT_ALARM permission.
     */
    fun canScheduleExactAlarms(): Boolean {
        return alarmScheduler.canScheduleExactAlarms()
    }
}
