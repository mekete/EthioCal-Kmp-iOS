package com.shalom.ethiopicchrono

import kotlinx.datetime.LocalDate
import kotlin.math.floor

/**
 * iOS-compatible EthiopicDate implementation
 * Represents a date in the Ethiopian calendar system
 */
class EthiopicDate private constructor(
    private val prolepticYear: Int,
    private val month: Short,
    private val day: Short
) {
    companion object {
        // Epoch day difference between Gregorian and Ethiopian calendars
        private const val EPOCH_DAY_DIFFERENCE = 716367

        /**
         * Creates an Ethiopian date from year, month, and day
         */
        fun of(prolepticYear: Int, month: Int, dayOfMonth: Int): EthiopicDate {
            return create(prolepticYear, month, dayOfMonth)
        }

        /**
         * Creates an Ethiopian date from a Gregorian LocalDate
         */
        fun from(gregorianDate: LocalDate): EthiopicDate {
            val epochDay = gregorianDate.toEpochDays().toLong()
            return ofEpochDay(epochDay)
        }

        /**
         * Creates an Ethiopian date from year and day of year
         */
        fun ofYearDay(prolepticYear: Int, dayOfYear: Int): EthiopicDate {
            validateYear(prolepticYear)
            if (dayOfYear < 1 || dayOfYear > 366) {
                throw IllegalArgumentException("Invalid day of year: $dayOfYear")
            }
            if (dayOfYear == 366 && !EthiopicChronology.INSTANCE.isLeapYear(prolepticYear)) {
                throw IllegalArgumentException("Invalid date 'Pagumen 6' as '$prolepticYear' is not a leap year")
            }
            val monthValue = ((dayOfYear - 1) / 30 + 1)
            val dayValue = ((dayOfYear - 1) % 30 + 1)
            return EthiopicDate(prolepticYear, monthValue.toShort(), dayValue.toShort())
        }

        /**
         * Creates an Ethiopian date from an epoch day
         */
        fun ofEpochDay(epochDay: Long): EthiopicDate {
            var ethiopicED = epochDay + EPOCH_DAY_DIFFERENCE
            var adjustment = 0
            if (ethiopicED < 0) {
                ethiopicED += (1461L * (1_000_000L / 4))
                adjustment = -1_000_000
            }
            val prolepticYear = (((ethiopicED * 4) + 1463) / 1461).toInt()
            val startYearEpochDay = (prolepticYear - 1) * 365 + (prolepticYear / 4)
            val doy0 = (ethiopicED - startYearEpochDay).toInt()
            val month = doy0 / 30 + 1
            val dom = doy0 % 30 + 1
            return EthiopicDate(prolepticYear + adjustment, month.toShort(), dom.toShort())
        }

        private fun create(prolepticYear: Int, month: Int, dayOfMonth: Int): EthiopicDate {
            validateYear(prolepticYear)
            validateMonth(month)
            validateDay(dayOfMonth)

            if (month == 13 && dayOfMonth > 5) {
                if (EthiopicChronology.INSTANCE.isLeapYear(prolepticYear)) {
                    if (dayOfMonth > 6) {
                        throw IllegalArgumentException(
                            "Invalid date 'Pagumen $dayOfMonth', valid range from 1 to 5, or 1 to 6 in a leap year"
                        )
                    }
                } else {
                    if (dayOfMonth == 6) {
                        throw IllegalArgumentException(
                            "Invalid date 'Pagumen 6' as '$prolepticYear' is not a leap year"
                        )
                    } else {
                        throw IllegalArgumentException(
                            "Invalid date 'Pagumen $dayOfMonth', valid range from 1 to 5, or 1 to 6 in a leap year"
                        )
                    }
                }
            }
            return EthiopicDate(prolepticYear, month.toShort(), dayOfMonth.toShort())
        }

        private fun resolvePreviousValid(prolepticYear: Int, month: Int, day: Int): EthiopicDate {
            var adjustedDay = day
            if (month == 13 && day > 5) {
                adjustedDay = if (EthiopicChronology.INSTANCE.isLeapYear(prolepticYear)) 6 else 5
            }
            return EthiopicDate(prolepticYear, month.toShort(), adjustedDay.toShort())
        }

        private fun validateYear(year: Int) {
            if (year < EthiopicChronology.MIN_YEAR || year > EthiopicChronology.MAX_YEAR) {
                throw IllegalArgumentException(
                    "Invalid year: $year (must be between ${EthiopicChronology.MIN_YEAR} and ${EthiopicChronology.MAX_YEAR})"
                )
            }
        }

        private fun validateMonth(month: Int) {
            if (month < EthiopicChronology.MIN_MONTH || month > EthiopicChronology.MAX_MONTH) {
                throw IllegalArgumentException(
                    "Invalid month: $month (must be between ${EthiopicChronology.MIN_MONTH} and ${EthiopicChronology.MAX_MONTH})"
                )
            }
        }

        private fun validateDay(day: Int) {
            if (day < EthiopicChronology.MIN_DAY || day > EthiopicChronology.MAX_DAY) {
                throw IllegalArgumentException(
                    "Invalid day: $day (must be between ${EthiopicChronology.MIN_DAY} and ${EthiopicChronology.MAX_DAY})"
                )
            }
        }
    }

    /**
     * Gets the chronology of this date
     */
    val chronology: EthiopicChronology
        get() = EthiopicChronology.INSTANCE

    /**
     * Gets the era of this date
     */
    val era: EthiopicEra
        get() = if (prolepticYear >= 1) EthiopicEra.INCARNATION else EthiopicEra.BEFORE_INCARNATION

    /**
     * Gets whether this year is a leap year
     */
    val isLeapYear: Boolean
        get() = EthiopicChronology.INSTANCE.isLeapYear(prolepticYear)

    /**
     * Gets the length of this month
     */
    fun lengthOfMonth(): Int {
        return if (month.toInt() == 13) {
            if (isLeapYear) 6 else 5
        } else {
            30
        }
    }

    /**
     * Gets the length of this year
     */
    fun lengthOfYear(): Int {
        return if (isLeapYear) 366 else 365
    }

    /**
     * Gets the day of year (1-366)
     */
    private val dayOfYear: Int
        get() = (month - 1) * 30 + day

    /**
     * Gets the day of week (1=Monday, 7=Sunday)
     */
    private val dayOfWeek: Int
        get() {
            val epochDay = toEpochDay()
            // Epoch day 0 (1970-01-01) was a Thursday (4)
            // So we add 3 to align: (0 + 3) % 7 + 1 = 4 (Thursday)
            return ((epochDay + 3).mod(7) + 1).toInt()
        }

    /**
     * Gets the proleptic month (year * 13 + month - 1)
     */
    private val prolepticMonth: Long
        get() = prolepticYear * 13L + month - 1

    /**
     * Gets the year of era (always positive)
     */
    private val yearOfEra: Int
        get() = if (prolepticYear >= 1) prolepticYear else 1 - prolepticYear

    /**
     * Gets aligned day of week in month (1-7)
     */
    private val alignedDayOfWeekInMonth: Int
        get() = ((day - 1) % 7) + 1

    /**
     * Gets aligned day of week in year (1-7)
     */
    private val alignedDayOfWeekInYear: Int
        get() = ((dayOfYear - 1) % 7) + 1

    /**
     * Gets aligned week of month (1-5)
     */
    private val alignedWeekOfMonth: Int
        get() = ((day - 1) / 7) + 1

    /**
     * Gets aligned week of year (1-53)
     */
    private val alignedWeekOfYear: Int
        get() = ((dayOfYear - 1) / 7) + 1

    /**
     * Gets the value of the specified field
     */
    fun get(field: ChronoField): Int {
        return when (field) {
            ChronoField.DAY_OF_WEEK -> dayOfWeek
            ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH -> alignedDayOfWeekInMonth
            ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR -> alignedDayOfWeekInYear
            ChronoField.DAY_OF_MONTH -> day.toInt()
            ChronoField.DAY_OF_YEAR -> dayOfYear
            ChronoField.EPOCH_DAY -> toEpochDay().toInt()
            ChronoField.ALIGNED_WEEK_OF_MONTH -> alignedWeekOfMonth
            ChronoField.ALIGNED_WEEK_OF_YEAR -> alignedWeekOfYear
            ChronoField.MONTH_OF_YEAR -> month.toInt()
            ChronoField.PROLEPTIC_MONTH -> prolepticMonth.toInt()
            ChronoField.YEAR_OF_ERA -> yearOfEra
            ChronoField.YEAR -> prolepticYear
            ChronoField.ERA -> if (prolepticYear >= 1) 1 else 0
        }
    }

    /**
     * Converts this date to an epoch day
     */
    fun toEpochDay(): Long {
        val year = prolepticYear.toLong()
        val calendarEpochDay = ((year - 1) * 365) + floor(year / 4.0).toLong() + (dayOfYear - 1)
        return calendarEpochDay - EPOCH_DAY_DIFFERENCE
    }

    /**
     * Converts this Ethiopian date to a Gregorian LocalDate
     */
    fun toLocalDate(): LocalDate {
        val epochDay = toEpochDay()
        return LocalDate.fromEpochDays(epochDay.toInt())
    }

    /**
     * Adds the specified amount to this date
     */
    fun plus(amountToAdd: Long, unit: ChronoUnit): EthiopicDate {
        return when (unit) {
            ChronoUnit.DAYS -> plusDays(amountToAdd)
            ChronoUnit.WEEKS -> plusWeeks(amountToAdd)
            ChronoUnit.MONTHS -> plusMonths(amountToAdd)
            ChronoUnit.YEARS -> plusYears(amountToAdd)
            ChronoUnit.DECADES -> plusYears(amountToAdd * 10)
            ChronoUnit.CENTURIES -> plusYears(amountToAdd * 100)
            ChronoUnit.MILLENNIA -> plusYears(amountToAdd * 1000)
            ChronoUnit.ERAS -> {
                val currentEra = if (prolepticYear >= 1) 1 else 0
                val newEra = currentEra + amountToAdd.toInt()
                val newYear = if (newEra == currentEra) {
                    prolepticYear
                } else {
                    1 - prolepticYear
                }
                resolvePreviousValid(newYear, month.toInt(), day.toInt())
            }
        }
    }

    /**
     * Adds the specified amount to this date (Int version for convenience)
     */
    fun plus(amountToAdd: Int, unit: ChronoUnit): EthiopicDate {
        return plus(amountToAdd.toLong(), unit)
    }

    /**
     * Subtracts the specified amount from this date
     */
    fun minus(amountToSubtract: Long, unit: ChronoUnit): EthiopicDate {
        return if (amountToSubtract == Long.MIN_VALUE) {
            plus(Long.MAX_VALUE, unit).plus(1, unit)
        } else {
            plus(-amountToSubtract, unit)
        }
    }

    /**
     * Subtracts the specified amount from this date (Int version for convenience)
     */
    fun minus(amountToSubtract: Int, unit: ChronoUnit): EthiopicDate {
        return minus(amountToSubtract.toLong(), unit)
    }

    private fun plusYears(yearsToAdd: Long): EthiopicDate {
        if (yearsToAdd == 0L) return this
        val newYear = (prolepticYear.toLong() + yearsToAdd).toInt()
        validateYear(newYear)
        return resolvePreviousValid(newYear, month.toInt(), day.toInt())
    }

    private fun plusMonths(months: Long): EthiopicDate {
        if (months == 0L) return this
        val curEm = prolepticMonth
        val calcEm = curEm + months
        val newYear = (calcEm / 13).toInt()
        val newMonth = ((calcEm % 13) + 1).toInt()
        return resolvePreviousValid(newYear, newMonth, day.toInt())
    }

    private fun plusWeeks(amountToAdd: Long): EthiopicDate {
        return plusDays(amountToAdd * 7)
    }

    private fun plusDays(days: Long): EthiopicDate {
        if (days == 0L) return this
        return ofEpochDay(toEpochDay() + days)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EthiopicDate) return false
        return prolepticYear == other.prolepticYear &&
                month == other.month &&
                day == other.day
    }

    override fun hashCode(): Int {
        val yearValue = prolepticYear
        val monthValue = month.toInt()
        val dayValue = day.toInt()
        return chronology.id.hashCode() xor
                ((yearValue and 0xFFFFF800.toInt()) xor ((yearValue shl 11) +
                        (monthValue shl 6) + dayValue))
    }

    override fun toString(): String {
        val buf = StringBuilder(30)
        buf.append(chronology.toString())
            .append(" ")
            .append(era)
            .append(" ")
            .append(yearOfEra)
        if (month < 10) buf.append("-0") else buf.append("-")
        buf.append(month)
        if (day < 10) buf.append("-0") else buf.append("-")
        buf.append(day)
        return buf.toString()
    }
}
