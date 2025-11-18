package com.shalom.calendar.domain.model

import kotlin.random.Random

/**
 * Represents a user event in the Ethiopian calendar
 */
data class Event(
    override val id: String = generateUUID(),
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
    val createdAt: Long = currentTimeMillis(),
    val updatedAt: Long = currentTimeMillis()
) : CalendarItem()

/**
 * Categories for events
 */
enum class EventCategory {
    PERSONAL,
    WORK,
    RELIGIOUS,
    NATIONAL,
    BIRTHDAY,
    ANNIVERSARY,
    CUSTOM;

    /**
     * Get color value as hex for cross-platform compatibility
     * UI layer can convert this to platform-specific Color objects
     */
    fun getColorHex(): Long {
        return when (this) {
            PERSONAL -> 0xFF1976D2      // Blue
            WORK -> 0xFFFF9800          // Orange
            RELIGIOUS -> 0xFFFFEB3B     // Golden
            NATIONAL -> 0xFF388E3C      // Green
            BIRTHDAY -> 0xFFE91E63      // Pink
            ANNIVERSARY -> 0xFFFF5722   // Deep Orange
            CUSTOM -> 0xFF607D8B        // Blue Grey
        }
    }
}

/**
 * Simple UUID generation for KMP
 */
private fun generateUUID(): String {
    val random = Random.Default
    return buildString {
        repeat(8) { append(random.nextInt(16).toString(16)) }
        append('-')
        repeat(4) { append(random.nextInt(16).toString(16)) }
        append('-')
        append('4')
        repeat(3) { append(random.nextInt(16).toString(16)) }
        append('-')
        append(listOf('8', '9', 'a', 'b').random())
        repeat(3) { append(random.nextInt(16).toString(16)) }
        append('-')
        repeat(12) { append(random.nextInt(16).toString(16)) }
    }
}

/**
 * Cross-platform current time in milliseconds
 */
expect fun currentTimeMillis(): Long
