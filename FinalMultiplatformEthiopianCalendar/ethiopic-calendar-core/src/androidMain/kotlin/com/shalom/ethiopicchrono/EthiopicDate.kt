package com.shalom.ethiopicchrono

import java.io.Serializable
import java.time.Clock
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.chrono.ChronoLocalDate
import java.time.chrono.ChronoLocalDateTime
import java.time.chrono.ChronoPeriod
import java.time.temporal.ChronoField as JavaChronoField
import java.time.temporal.ChronoUnit as JavaChronoUnit
import java.time.temporal.Temporal
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAmount
import java.time.temporal.TemporalField
import java.time.temporal.TemporalUnit
import java.time.temporal.UnsupportedTemporalTypeException
import java.time.temporal.ValueRange
import kotlinx.datetime.LocalDate as KotlinxLocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate

actual class EthiopicDate private constructor(
    private val prolepticYear: Int,
    private val month: Short,
    private val day: Short
) : ChronoLocalDate, Serializable {

    actual companion object {
        private const val serialVersionUID = -268768729L
        private const val EPOCH_DAY_DIFFERENCE = 716367

        @JvmStatic
        actual fun now(): EthiopicDate = now(Clock.systemDefaultZone())

        @JvmStatic
        fun now(zone: ZoneId): EthiopicDate = now(Clock.system(zone))

        @JvmStatic
        fun now(clock: Clock): EthiopicDate {
            val now = LocalDate.now(clock)
            return ofEpochDay(now.toEpochDay())
        }

        @JvmStatic
        actual fun of(prolepticYear: Int, month: Int, dayOfMonth: Int): EthiopicDate {
            return create(prolepticYear, month, dayOfMonth)
        }

        @JvmStatic
        fun from(temporal: TemporalAccessor): EthiopicDate {
            if (temporal is EthiopicDate) {
                return temporal
            }
            return ofEpochDay(temporal.getLong(JavaChronoField.EPOCH_DAY))
        }

        /**
         * Creates an Ethiopian date from a kotlinx.datetime LocalDate
         */
        @JvmStatic
        actual fun from(gregorianDate: KotlinxLocalDate): EthiopicDate {
            return ofEpochDay(gregorianDate.toEpochDays().toLong())
        }

        @JvmStatic
        actual fun ofYearDay(prolepticYear: Int, dayOfYear: Int): EthiopicDate {
            EthiopicChronology.YEAR_RANGE.checkValidValue(prolepticYear.toLong(), JavaChronoField.YEAR)
            JavaChronoField.DAY_OF_YEAR.range().checkValidValue(dayOfYear.toLong(), JavaChronoField.DAY_OF_YEAR)
            if (dayOfYear == 366 && !EthiopicChronology.INSTANCE.isLeapYear(prolepticYear.toLong())) {
                throw DateTimeException("Invalid date 'Pagumen 6' as '$prolepticYear' is not a leap year")
            }
            return EthiopicDate(prolepticYear, ((dayOfYear - 1) / 30 + 1).toShort(), ((dayOfYear - 1) % 30 + 1).toShort())
        }

        @JvmStatic
        actual fun ofEpochDay(epochDay: Long): EthiopicDate {
            JavaChronoField.EPOCH_DAY.range().checkValidValue(epochDay, JavaChronoField.EPOCH_DAY)
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

        private fun resolvePreviousValid(prolepticYear: Int, month: Int, day: Int): EthiopicDate {
            var adjustedDay = day
            if (month == 13 && day > 5) {
                adjustedDay = if (EthiopicChronology.INSTANCE.isLeapYear(prolepticYear.toLong())) 6 else 5
            }
            return EthiopicDate(prolepticYear, month.toShort(), adjustedDay.toShort())
        }

        private fun create(prolepticYear: Int, month: Int, dayOfMonth: Int): EthiopicDate {
            EthiopicChronology.YEAR_RANGE.checkValidValue(prolepticYear.toLong(), JavaChronoField.YEAR)
            EthiopicChronology.MOY_RANGE.checkValidValue(month.toLong(), JavaChronoField.MONTH_OF_YEAR)
            EthiopicChronology.DOM_RANGE.checkValidValue(dayOfMonth.toLong(), JavaChronoField.DAY_OF_MONTH)

            if (month == 13 && dayOfMonth > 5) {
                if (EthiopicChronology.INSTANCE.isLeapYear(prolepticYear.toLong())) {
                    if (dayOfMonth > 6) {
                        throw DateTimeException("Invalid date 'Pagumen $dayOfMonth', valid range from 1 to 5, or 1 to 6 in a leap year")
                    }
                } else {
                    if (dayOfMonth == 6) {
                        throw DateTimeException("Invalid date 'Pagumen 6' as '$prolepticYear' is not a leap year")
                    } else {
                        throw DateTimeException("Invalid date 'Pagumen $dayOfMonth', valid range from 1 to 5, or 1 to 6 in a leap year")
                    }
                }
            }
            return EthiopicDate(prolepticYear, month.toShort(), dayOfMonth.toShort())
        }
    }

    private fun readResolve(): Any = create(prolepticYear, month.toInt(), day.toInt())

    actual val chronology: EthiopicChronology
        get() = getChronology()

    actual val era: EthiopicEra
        get() = getEra()

    actual val isLeapYear: Boolean
        get() = EthiopicChronology.INSTANCE.isLeapYear(prolepticYear.toLong())

    override fun getChronology(): EthiopicChronology = EthiopicChronology.INSTANCE

    override fun getEra(): EthiopicEra =
        if (prolepticYear >= 1) EthiopicEra.INCARNATION else EthiopicEra.BEFORE_INCARNATION

    actual override fun lengthOfMonth(): Int {
        return if (month.toInt() == 13) {
            if (isLeapYear) 6 else 5
        } else {
            30
        }
    }

    actual override fun lengthOfYear(): Int =
        if (EthiopicChronology.INSTANCE.isLeapYear(prolepticYear.toLong())) 366 else 365

    /**
     * Gets the value of the specified common ChronoField
     */
    actual fun get(field: ChronoField): Int {
        return getLong(field.toJavaChronoField()).toInt()
    }

    /**
     * Converts this Ethiopian date to a kotlinx.datetime LocalDate
     */
    actual fun toLocalDate(): KotlinxLocalDate {
        val epochDay = toEpochDay()
        return LocalDate.ofEpochDay(epochDay).toKotlinLocalDate()
    }

    /**
     * Adds the specified amount using common ChronoUnit
     */
    actual fun plus(amountToAdd: Long, unit: ChronoUnit): EthiopicDate {
        return plus(amountToAdd, unit.toJavaChronoUnit())
    }

    /**
     * Adds the specified amount using common ChronoUnit (Int version)
     */
    actual fun plus(amountToAdd: Int, unit: ChronoUnit): EthiopicDate {
        return plus(amountToAdd.toLong(), unit.toJavaChronoUnit())
    }

    /**
     * Subtracts the specified amount using common ChronoUnit
     */
    actual fun minus(amountToSubtract: Long, unit: ChronoUnit): EthiopicDate {
        return minus(amountToSubtract, unit.toJavaChronoUnit())
    }

    /**
     * Subtracts the specified amount using common ChronoUnit (Int version)
     */
    actual fun minus(amountToSubtract: Int, unit: ChronoUnit): EthiopicDate {
        return minus(amountToSubtract.toLong(), unit.toJavaChronoUnit())
    }

    private val dayOfYear: Int
        get() = (month - 1) * 30 + day

    private val dayOfWeek: Int
        get() = (Math.floorMod(toEpochDay() + 3, 7) + 1)

    private val prolepticMonth: Long
        get() = prolepticYear * 13L + month - 1

    private val yearOfEra: Int
        get() = if (prolepticYear >= 1) prolepticYear else 1 - prolepticYear

    private val alignedDayOfWeekInMonth: Int
        get() = ((day - 1) % 7) + 1

    private val alignedDayOfWeekInYear: Int
        get() = ((dayOfYear - 1) % 7) + 1

    private val alignedWeekOfMonth: Int
        get() = ((day - 1) / 7) + 1

    private val alignedWeekOfYear: Int
        get() = ((dayOfYear - 1) / 7) + 1

    override fun range(field: TemporalField): ValueRange {
        if (field is JavaChronoField) {
            if (isSupported(field)) {
                return when (field) {
                    JavaChronoField.DAY_OF_MONTH -> ValueRange.of(1, lengthOfMonth().toLong())
                    JavaChronoField.DAY_OF_YEAR -> ValueRange.of(1, lengthOfYear().toLong())
                    JavaChronoField.ALIGNED_WEEK_OF_MONTH ->
                        ValueRange.of(1, if (month.toInt() == 13) 1 else 5)
                    else -> getChronology().range(field)
                }
            }
            throw UnsupportedTemporalTypeException("Unsupported field: $field")
        }
        return field.rangeRefinedBy(this)
    }

    override fun getLong(field: TemporalField): Long {
        if (field is JavaChronoField) {
            return when (field) {
                JavaChronoField.DAY_OF_WEEK -> dayOfWeek.toLong()
                JavaChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH -> alignedDayOfWeekInMonth.toLong()
                JavaChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR -> alignedDayOfWeekInYear.toLong()
                JavaChronoField.DAY_OF_MONTH -> day.toLong()
                JavaChronoField.DAY_OF_YEAR -> dayOfYear.toLong()
                JavaChronoField.EPOCH_DAY -> toEpochDay()
                JavaChronoField.ALIGNED_WEEK_OF_MONTH -> alignedWeekOfMonth.toLong()
                JavaChronoField.ALIGNED_WEEK_OF_YEAR -> alignedWeekOfYear.toLong()
                JavaChronoField.MONTH_OF_YEAR -> month.toLong()
                JavaChronoField.PROLEPTIC_MONTH -> prolepticMonth
                JavaChronoField.YEAR_OF_ERA -> yearOfEra.toLong()
                JavaChronoField.YEAR -> prolepticYear.toLong()
                JavaChronoField.ERA -> if (prolepticYear >= 1) 1L else 0L
                else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
            }
        }
        return field.getFrom(this)
    }

    actual override fun toEpochDay(): Long {
        val year = prolepticYear.toLong()
        val calendarEpochDay = ((year - 1) * 365) + Math.floorDiv(year, 4) + (dayOfYear - 1)
        return calendarEpochDay - EPOCH_DAY_DIFFERENCE
    }

    override fun with(adjuster: TemporalAdjuster): EthiopicDate {
        return adjuster.adjustInto(this) as EthiopicDate
    }

    override fun with(field: TemporalField, newValue: Long): EthiopicDate {
        if (field is JavaChronoField) {
            getChronology().range(field).checkValidValue(newValue, field)
            val nvalue = newValue.toInt()
            return when (field) {
                JavaChronoField.DAY_OF_WEEK -> plusDays(newValue - dayOfWeek)
                JavaChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH ->
                    plusDays(newValue - getLong(JavaChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH))
                JavaChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR ->
                    plusDays(newValue - getLong(JavaChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR))
                JavaChronoField.DAY_OF_MONTH -> resolvePreviousValid(prolepticYear, month.toInt(), nvalue)
                JavaChronoField.DAY_OF_YEAR -> withDayOfYear(nvalue)
                JavaChronoField.EPOCH_DAY -> ofEpochDay(newValue)
                JavaChronoField.ALIGNED_WEEK_OF_MONTH ->
                    plusDays((newValue - getLong(JavaChronoField.ALIGNED_WEEK_OF_MONTH)) * 7)
                JavaChronoField.ALIGNED_WEEK_OF_YEAR ->
                    plusDays((newValue - getLong(JavaChronoField.ALIGNED_WEEK_OF_YEAR)) * 7)
                JavaChronoField.MONTH_OF_YEAR -> resolvePreviousValid(prolepticYear, nvalue, day.toInt())
                JavaChronoField.PROLEPTIC_MONTH -> plusMonths(newValue - prolepticMonth)
                JavaChronoField.YEAR_OF_ERA ->
                    resolvePreviousValid(if (prolepticYear >= 1) nvalue else 1 - nvalue, month.toInt(), day.toInt())
                JavaChronoField.YEAR -> resolvePreviousValid(nvalue, month.toInt(), day.toInt())
                JavaChronoField.ERA ->
                    if (newValue == getLong(JavaChronoField.ERA)) this
                    else resolvePreviousValid(1 - prolepticYear, month.toInt(), day.toInt())
                else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
            }
        }
        return field.adjustInto(this, newValue) as EthiopicDate
    }

    private fun withDayOfYear(value: Int): EthiopicDate {
        return resolvePreviousValid(prolepticYear, ((value - 1) / 30) + 1, ((value - 1) % 30) + 1)
    }

    override fun plus(amountToAdd: TemporalAmount): EthiopicDate {
        return amountToAdd.addTo(this) as EthiopicDate
    }

    override fun plus(amountToAdd: Long, unit: TemporalUnit): EthiopicDate {
        if (unit is JavaChronoUnit) {
            return when (unit) {
                JavaChronoUnit.DAYS -> plusDays(amountToAdd)
                JavaChronoUnit.WEEKS -> plusWeeks(amountToAdd)
                JavaChronoUnit.MONTHS -> plusMonths(amountToAdd)
                JavaChronoUnit.YEARS -> plusYears(amountToAdd)
                JavaChronoUnit.DECADES -> plusYears(Math.multiplyExact(amountToAdd, 10))
                JavaChronoUnit.CENTURIES -> plusYears(Math.multiplyExact(amountToAdd, 100))
                JavaChronoUnit.MILLENNIA -> plusYears(Math.multiplyExact(amountToAdd, 1000))
                JavaChronoUnit.ERAS -> with(JavaChronoField.ERA, Math.addExact(getLong(JavaChronoField.ERA), amountToAdd))
                else -> throw UnsupportedTemporalTypeException("Unsupported unit: $unit")
            }
        }
        return unit.addTo(this, amountToAdd) as EthiopicDate
    }

    override fun minus(amountToSubtract: TemporalAmount): EthiopicDate {
        return amountToSubtract.subtractFrom(this) as EthiopicDate
    }

    override fun minus(amountToSubtract: Long, unit: TemporalUnit): EthiopicDate {
        return if (amountToSubtract == Long.MIN_VALUE) {
            plus(Long.MAX_VALUE, unit).plus(1, unit)
        } else {
            plus(-amountToSubtract, unit)
        }
    }

    private fun plusYears(yearsToAdd: Long): EthiopicDate {
        if (yearsToAdd == 0L) return this
        val newYear = JavaChronoField.YEAR.checkValidIntValue(Math.addExact(prolepticYear.toLong(), yearsToAdd))
        return resolvePreviousValid(newYear, month.toInt(), day.toInt())
    }

    private fun plusMonths(months: Long): EthiopicDate {
        if (months == 0L) return this
        val curEm = prolepticMonth
        val calcEm = Math.addExact(curEm, months)
        val newYear = Math.toIntExact(Math.floorDiv(calcEm, 13))
        val newMonth = (Math.floorMod(calcEm, 13) + 1).toInt()
        return resolvePreviousValid(newYear, newMonth, day.toInt())
    }

    private fun plusWeeks(amountToAdd: Long): EthiopicDate {
        return plusDays(Math.multiplyExact(amountToAdd, 7))
    }

    private fun plusDays(days: Long): EthiopicDate {
        if (days == 0L) return this
        return ofEpochDay(Math.addExact(toEpochDay(), days))
    }

    @Suppress("UNCHECKED_CAST")
    override fun atTime(localTime: LocalTime): ChronoLocalDateTime<EthiopicDate> {
        return super.atTime(localTime) as ChronoLocalDateTime<EthiopicDate>
    }

    override fun until(endExclusive: Temporal, unit: TemporalUnit): Long {
        val end = from(endExclusive)
        if (unit is JavaChronoUnit) {
            return when (unit) {
                JavaChronoUnit.DAYS -> daysUntil(end)
                JavaChronoUnit.WEEKS -> daysUntil(end) / 7
                JavaChronoUnit.MONTHS -> monthsUntil(end)
                JavaChronoUnit.YEARS -> monthsUntil(end) / 13
                JavaChronoUnit.DECADES -> monthsUntil(end) / 130
                JavaChronoUnit.CENTURIES -> monthsUntil(end) / 1300
                JavaChronoUnit.MILLENNIA -> monthsUntil(end) / 13000
                JavaChronoUnit.ERAS -> end.getLong(JavaChronoField.ERA) - getLong(JavaChronoField.ERA)
                else -> throw UnsupportedTemporalTypeException("Unsupported unit: $unit")
            }
        }
        return unit.between(this, end)
    }

    override fun until(endDateExclusive: ChronoLocalDate): ChronoPeriod {
        val end = from(endDateExclusive)
        var totalMonths = end.prolepticMonth - this.prolepticMonth
        var days = end.day - this.day
        if (totalMonths > 0 && days < 0) {
            totalMonths--
            val calcDate = this.plusMonths(totalMonths)
            days = (end.toEpochDay() - calcDate.toEpochDay()).toInt()
        } else if (totalMonths < 0 && days > 0) {
            totalMonths++
            days -= end.lengthOfMonth()
        }
        val years = totalMonths / 13
        val months = (totalMonths % 13).toInt()
        return chronology.period(Math.toIntExact(years), months, days.toInt())
    }

    private fun daysUntil(end: ChronoLocalDate): Long {
        return end.toEpochDay() - toEpochDay()
    }

    private fun monthsUntil(end: EthiopicDate): Long {
        val packed1 = prolepticMonth * 256L + day
        val packed2 = end.prolepticMonth * 256L + end.day
        return (packed2 - packed1) / 256L
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
