package com.shalom.calendar.domain.model

/**
 * Represents a holiday in the Ethiopian calendar
 */
data class Holiday(
    override val id: String,
    override val title: String,
    val nameAmharic: String = "",
    val type: HolidayType,
    override val ethiopianYear: Int = 2020,
    override val ethiopianMonth: Int,
    override val ethiopianDay: Int,
    val isDayOff: Boolean,
    val isVerified: Boolean = false,
    val isFixedDate: Boolean,
    override val description: String = "",
    val celebration: String = "",
    val mediaUrl: String? = null
) : CalendarItem(), Comparable<Holiday> {

    override fun compareTo(other: Holiday): Int {
        return when {
            ethiopianMonth != other.ethiopianMonth -> ethiopianMonth.compareTo(other.ethiopianMonth)
            else -> ethiopianDay.compareTo(other.ethiopianDay)
        }
    }
}
