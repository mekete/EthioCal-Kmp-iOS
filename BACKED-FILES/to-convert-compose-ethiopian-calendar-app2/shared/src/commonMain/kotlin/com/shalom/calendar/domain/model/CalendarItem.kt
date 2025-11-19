package com.shalom.calendar.domain.model

/**
 * Base sealed class for calendar items (Events and Holidays)
 * Provides polymorphic treatment and exhaustive when expressions
 */
sealed class CalendarItem {
    abstract val id: String
    abstract val title: String
    abstract val description: String?
    abstract val ethiopianYear: Int
    abstract val ethiopianMonth: Int
    abstract val ethiopianDay: Int
}
