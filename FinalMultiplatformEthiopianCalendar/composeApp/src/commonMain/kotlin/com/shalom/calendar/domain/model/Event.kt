package com.shalom.calendar.domain.model

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.Clock

/**
 * Represents a user event in the Ethiopian calendar
 */
data class Event(
    override val id: String,
    override val title: String,
    override val description: String? = null,
    override val ethiopianYear: Int,
    override val ethiopianMonth: Int,
    override val ethiopianDay: Int,
    val startTime: String? = null,  // HH:mm format
    val endTime: String? = null,
    val isAllDay: Boolean = true,
    val category: EventCategory = EventCategory.PERSONAL,
    val googleCalendarEventId: String? = null,
    val isSynced: Boolean = false,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds()
) : CalendarItem()

/**
 * Categories for events
 */
enum class EventCategory {
    PERSONAL, WORK, RELIGIOUS, NATIONAL, BIRTHDAY, ANNIVERSARY, CUSTOM;

    fun getColor(): Color {
        return when (this) {
            PERSONAL -> Color(0xFF1976D2)      // Blue
            WORK -> Color(0xFFFF9800)          // Orange
            RELIGIOUS -> Color(0xFFFFEB3B)    // Golden
            NATIONAL -> Color(0xFF388E3C)     // Green
            BIRTHDAY -> Color(0xFFE91E63)     // Pink
            ANNIVERSARY -> Color(0xFFFF5722) // Deep Orange
            CUSTOM -> Color(0xFF607D8B)       // Blue Grey
        }
    }
}
