package com.shalom.calendar.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.shalom.calendar.data.local.entity.EventEntity
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

/**
 * Android implementation of AlarmScheduler using AlarmManager.
 */
class AlarmSchedulerImpl(private val context: Context) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleAlarm(event: EventEntity): Boolean {
        // Check if reminder is enabled
        val reminderMinutes = event.reminderMinutesBefore ?: return false

        // Calculate alarm trigger time
        val alarmTime = event.startTime.minus(reminderMinutes.minutes)

        // Don't schedule alarms for past times
        if (alarmTime < Clock.System.now()) {
            // For repeating events, try to find the next occurrence
            if (event.recurrenceRule != null) {
                return scheduleNextRecurringAlarm(event)
            }
            return false
        }

        // Create pending intent for the alarm
        val pendingIntent = createAlarmPendingIntent(event)

        return try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTime.toEpochMilliseconds(),
                pendingIntent
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun scheduleNextRecurringAlarm(event: EventEntity): Boolean {
        val rrule = event.recurrenceRule ?: return false
        val reminderMinutes = event.reminderMinutesBefore ?: return false

        // Check if recurrence has ended
        val recurrenceEnd = event.recurrenceEndDate
        if (recurrenceEnd != null && recurrenceEnd < Clock.System.now()) {
            return false
        }

        // Parse RRULE for weekly recurrence
        if (!rrule.contains("FREQ=WEEKLY")) {
            return false
        }

        // Extract weekdays from BYDAY parameter
        val weekDays = extractWeekDaysFromRRule(rrule)
        if (weekDays.isEmpty()) {
            return false
        }

        // Find the next occurrence
        val nextOccurrence = findNextOccurrence(event.startTime, weekDays, recurrenceEnd)
            ?: return false

        // Calculate alarm time (next occurrence - reminder offset)
        val alarmTime = nextOccurrence.minus(reminderMinutes.minutes)

        // Don't schedule if alarm time is in the past
        if (alarmTime < Clock.System.now()) {
            return false
        }

        // Create pending intent
        val pendingIntent = createAlarmPendingIntent(event)

        return try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTime.toEpochMilliseconds(),
                pendingIntent
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun findNextOccurrence(
        startTime: Instant,
        weekDays: Set<DayOfWeek>,
        endDate: Instant?
    ): Instant? {
        val timeZone = TimeZone.currentSystemDefault()
        val now = Clock.System.now()
        val startDateTime = startTime.toLocalDateTime(timeZone)

        var currentInstant = if (startTime > now) startTime else now
        var currentDateTime = currentInstant.toLocalDateTime(timeZone)

        val maxSearchDays = 365
        var daysSearched = 0

        while (daysSearched < maxSearchDays) {
            if (endDate != null && currentInstant > endDate) {
                return null
            }

            if (weekDays.contains(currentDateTime.dayOfWeek)) {
                // Create occurrence at the same time as startTime but on current date
                val occurrenceDateTime = kotlinx.datetime.LocalDateTime(
                    currentDateTime.date,
                    kotlinx.datetime.LocalTime(
                        startDateTime.hour,
                        startDateTime.minute,
                        startDateTime.second,
                        0
                    )
                )
                val occurrence = occurrenceDateTime.toInstant(timeZone)

                if (occurrence > now) {
                    return occurrence
                }
            }

            currentInstant = currentInstant.plus(1.days)
            currentDateTime = currentInstant.toLocalDateTime(timeZone)
            daysSearched++
        }

        return null
    }

    private fun extractWeekDaysFromRRule(rrule: String): Set<DayOfWeek> {
        val byDayMatch = Regex("BYDAY=([A-Z,]+)").find(rrule) ?: return emptySet()
        val days = byDayMatch.groupValues[1].split(",")

        return days.mapNotNull { day ->
            when (day) {
                "MO" -> DayOfWeek.MONDAY
                "TU" -> DayOfWeek.TUESDAY
                "WE" -> DayOfWeek.WEDNESDAY
                "TH" -> DayOfWeek.THURSDAY
                "FR" -> DayOfWeek.FRIDAY
                "SA" -> DayOfWeek.SATURDAY
                "SU" -> DayOfWeek.SUNDAY
                else -> null
            }
        }.toSet()
    }

    override fun cancelAlarm(eventId: String) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_EVENT_ALARM
            putExtra(AlarmReceiver.EXTRA_EVENT_ID, eventId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    override fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    private fun createAlarmPendingIntent(event: EventEntity): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_EVENT_ALARM
            putExtra(AlarmReceiver.EXTRA_EVENT_ID, event.id)
            putExtra(AlarmReceiver.EXTRA_EVENT_TITLE, event.summary)
            putExtra(AlarmReceiver.EXTRA_EVENT_DESCRIPTION, event.description)
            putExtra(AlarmReceiver.EXTRA_EVENT_TIME, event.startTime.toEpochMilliseconds())
            putExtra(AlarmReceiver.EXTRA_IS_RECURRING, event.recurrenceRule != null)
        }

        return PendingIntent.getBroadcast(
            context,
            event.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}

/**
 * BroadcastReceiver for handling alarm events.
 * This is referenced by the Android implementation.
 */
object AlarmReceiver {
    const val ACTION_EVENT_ALARM = "com.shalom.calendar.ACTION_EVENT_ALARM"
    const val EXTRA_EVENT_ID = "event_id"
    const val EXTRA_EVENT_TITLE = "event_title"
    const val EXTRA_EVENT_DESCRIPTION = "event_description"
    const val EXTRA_EVENT_TIME = "event_time"
    const val EXTRA_IS_RECURRING = "is_recurring"
}
