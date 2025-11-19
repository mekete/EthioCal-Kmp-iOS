package com.shalom.ethiopicchrono

/**
 * iOS-compatible EthiopicChronology
 * Represents the Ethiopian calendar system
 */
actual class EthiopicChronology private constructor() {

    actual companion object {
        actual val INSTANCE = EthiopicChronology()

        // Value ranges for validation
        actual const val MIN_YEAR = -999_998
        actual const val MAX_YEAR = 999_999
        actual const val MIN_MONTH = 1
        actual const val MAX_MONTH = 13
        actual const val MIN_DAY = 1
        actual const val MAX_DAY = 30
    }

    actual val id: String = "Ethiopic"
    actual val calendarType: String = "ethiopic"

    /**
     * Checks if the given year is a leap year in the Ethiopian calendar
     * A year is a leap year if (year mod 4) == 3
     */
    actual fun isLeapYear(prolepticYear: Long): Boolean {
        return (prolepticYear.mod(4)) == 3
    }

    actual fun isLeapYear(prolepticYear: Int): Boolean {
        return isLeapYear(prolepticYear.toLong())
    }

    /**
     * Gets the length of the specified month in days
     */
    fun lengthOfMonth(prolepticYear: Int, month: Int): Int {
        return when (month) {
            in 1..12 -> 30
            13 -> if (isLeapYear(prolepticYear)) 6 else 5
            else -> throw IllegalArgumentException("Invalid month: $month")
        }
    }

    /**
     * Gets the length of the year in days
     */
    fun lengthOfYear(prolepticYear: Int): Int {
        return if (isLeapYear(prolepticYear)) 366 else 365
    }

    override fun toString(): String = "Ethiopic"
}
