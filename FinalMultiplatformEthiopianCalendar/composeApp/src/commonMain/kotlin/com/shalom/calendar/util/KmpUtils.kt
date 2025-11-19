package com.shalom.calendar.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Generate a random UUID string.
 * Platform-specific implementation.
 */
expect fun randomUUID(): String

/**
 * Get current time in milliseconds since epoch.
 */
fun currentTimeMillis(): Long = Clock.System.now().toEpochMilliseconds()

/**
 * Get today's date in the system timezone.
 */
fun today(): LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

/**
 * Get the first day of the month for a given date.
 */
fun LocalDate.firstDayOfMonth(): LocalDate = LocalDate(year, month, 1)

/**
 * Get the last day of the month for a given date.
 */
fun LocalDate.lastDayOfMonth(): LocalDate {
    val nextMonth = if (monthNumber == 12) {
        LocalDate(year + 1, 1, 1)
    } else {
        LocalDate(year, monthNumber + 1, 1)
    }
    return LocalDate.fromEpochDays(nextMonth.toEpochDays() - 1)
}

/**
 * Get the length of the month.
 */
fun LocalDate.lengthOfMonth(): Int {
    return lastDayOfMonth().dayOfMonth
}

/**
 * Create a LocalDate with a specific day of month.
 */
fun LocalDate.withDayOfMonth(day: Int): LocalDate = LocalDate(year, month, day)
