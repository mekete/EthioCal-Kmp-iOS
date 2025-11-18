package com.shalom.calendar.domain.model

import org.threeten.extra.chrono.EthiopicDate
import java.time.LocalDate
import java.time.temporal.ChronoField

/**
 * Represents a holiday in the Ethiopian calendar
 */
data class Holiday(
    override val id: String, override val title: String, val nameAmharic: String = "", val type: HolidayType, override val ethiopianYear: Int = 2020, override val ethiopianMonth: Int, override val ethiopianDay: Int, val isDayOff: Boolean, val isVerified: Boolean = false, val isFixedDate: Boolean, override val description: String = "", val celebration: String = "", val mediaUrl: String? = null
) : CalendarItem(), Comparable<Holiday> {

    override fun compareTo(other: Holiday): Int {
        return when {
            ethiopianMonth != other.ethiopianMonth -> ethiopianMonth.compareTo(other.ethiopianMonth)
            else -> ethiopianDay.compareTo(other.ethiopianDay)
        }
    }
}

/**
 * Holiday occurrence for a specific year with actual date
 */
data class HolidayOccurrence(
    val holiday: Holiday, val ethiopicDate: EthiopicDate, val adjustment: Int = 0  // Days adjusted via Firebase
) {
    /**
     * Get the actual Ethiopic date after applying any adjustment
     * Converts through Gregorian to ensure correct date arithmetic
     */
    val actualEthiopicDate: EthiopicDate
        get() = if (adjustment == 0) {
            ethiopicDate
        } else { // Convert to Gregorian, add days, convert back to Ethiopic
            val gregorian = LocalDate.from(ethiopicDate)
            val adjustedGregorian = gregorian.plusDays(adjustment.toLong())
            EthiopicDate.from(adjustedGregorian)
        }

    /**
     * Get the Gregorian date for this holiday occurrence
     */
    fun toGregorian(): LocalDate = LocalDate.from(actualEthiopicDate)

    /**
     * Get Ethiopian month number (1-13)
     */
    fun getEthiopianMonth(): Int = actualEthiopicDate.get(ChronoField.MONTH_OF_YEAR)

    /**
     * Get Ethiopian day of month (1-30)
     */
    fun getEthiopianDay(): Int = actualEthiopicDate.get(ChronoField.DAY_OF_MONTH)

    /**
     * Get Ethiopian year
     */
    fun getEthiopianYear(): Int = actualEthiopicDate.get(ChronoField.YEAR_OF_ERA)
}