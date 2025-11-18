package com.shalom.calendar.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.shalom.calendar.data.local.converter.DateConverter
import kotlinx.datetime.Instant

/**
 * Room entity for storing events and reminders.
 * KMP-compatible version using kotlinx.datetime.
 *
 * This structure is designed to be compatible with Google Calendar API's Event resource.
 */
@Entity(tableName = "events")
@TypeConverters(DateConverter::class)
data class EventEntity(
    @PrimaryKey val id: String,

    // Core event fields
    val summary: String,                              // Event title
    val description: String? = null,                   // Event description
    val startTime: Instant,                            // Start date/time
    val endTime: Instant? = null,                      // End date/time (null for reminders)
    val isAllDay: Boolean = false,                     // All-day event flag
    val timeZone: String = "Africa/Addis_Ababa",      // IANA timezone identifier

    // Recurrence fields
    val recurrenceRule: String? = null,                // RRULE string (e.g., "RRULE:FREQ=WEEKLY;BYDAY=TU,TH")
    val recurrenceEndDate: Instant? = null,            // When recurrence ends (null = never ends)

    // Category and visual
    val category: String = "PERSONAL",                 // PERSONAL, WORK, RELIGIOUS, etc.

    // Reminder/Notification settings
    val reminderMinutesBefore: Int? = null,            // Minutes before event to remind (null = no reminder)
    val notificationChannelId: String = "event_reminders", // For Android notification channels

    // Ethiopian calendar fields (for local display)
    val ethiopianYear: Int,                            // Ethiopian calendar year
    val ethiopianMonth: Int,                           // Ethiopian calendar month (1-13)
    val ethiopianDay: Int,                             // Ethiopian calendar day

    // Google Calendar sync fields
    val googleCalendarEventId: String? = null,         // Google Calendar event ID
    val googleCalendarId: String? = null,              // Google Calendar ID (e.g., "primary")
    val isSynced: Boolean = false,                     // Whether synced with Google Calendar
    val syncedAt: Long? = null,                        // Last sync timestamp

    // Metadata
    val createdAt: Long,                               // Creation timestamp
    val updatedAt: Long                                // Last update timestamp
)

/**
 * Represents a single instance of an event (useful for recurring events).
 * This is a computed model, not stored in database directly.
 */
data class EventInstance(
    val eventId: String,
    val summary: String,
    val description: String?,
    val instanceStart: Instant,
    val instanceEnd: Instant?,
    val isAllDay: Boolean,
    val category: String,
    val reminderMinutesBefore: Int?,
    val ethiopianYear: Int,
    val ethiopianMonth: Int,
    val ethiopianDay: Int,
    val isRecurring: Boolean,
    val originalEvent: EventEntity? = null
)
