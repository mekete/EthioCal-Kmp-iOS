package com.shalom.ethiopicchrono

import java.io.Serializable
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.chrono.AbstractChronology
import java.time.chrono.ChronoLocalDateTime
import java.time.chrono.ChronoZonedDateTime
import java.time.chrono.Era
import java.time.format.ResolverStyle
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalField
import java.time.temporal.ValueRange

class EthiopicChronology private constructor() : AbstractChronology(), Serializable {

    companion object {
        @JvmField
        val INSTANCE = EthiopicChronology()

        private const val serialVersionUID = 53287687268768L

        @JvmField
        val YEAR_RANGE: ValueRange = ValueRange.of(-999_998, 999_999)

        @JvmField
        val YOE_RANGE: ValueRange = ValueRange.of(1, 999_999)

        @JvmField
        val PROLEPTIC_MONTH_RANGE: ValueRange = ValueRange.of(-999_998 * 13L, 999_999 * 13L + 12)

        @JvmField
        val MOY_RANGE: ValueRange = ValueRange.of(1, 13)

        @JvmField
        val ALIGNED_WOM_RANGE: ValueRange = ValueRange.of(1, 1, 5)

        @JvmField
        val DOM_RANGE: ValueRange = ValueRange.of(1, 5, 30)

        @JvmField
        val DOM_RANGE_NONLEAP: ValueRange = ValueRange.of(1, 5)

        @JvmField
        val DOM_RANGE_LEAP: ValueRange = ValueRange.of(1, 6)
    }

    private fun readResolve(): Any = INSTANCE

    override fun getId(): String = "Ethiopic"

    override fun getCalendarType(): String = "ethiopic"

    override fun isLeapYear(prolepticYear: Long): Boolean =
        Math.floorMod(prolepticYear, 4) == 3

    override fun date(era: Era, yearOfEra: Int, month: Int, dayOfMonth: Int): EthiopicDate {
        return date(prolepticYear(era, yearOfEra), month, dayOfMonth)
    }

    override fun date(prolepticYear: Int, month: Int, dayOfMonth: Int): EthiopicDate {
        return EthiopicDate.of(prolepticYear, month, dayOfMonth)
    }

    override fun dateYearDay(era: Era, yearOfEra: Int, dayOfYear: Int): EthiopicDate {
        return dateYearDay(prolepticYear(era, yearOfEra), dayOfYear)
    }

    override fun dateYearDay(prolepticYear: Int, dayOfYear: Int): EthiopicDate {
        return EthiopicDate.ofYearDay(prolepticYear, dayOfYear)
    }

    override fun dateEpochDay(epochDay: Long): EthiopicDate {
        return EthiopicDate.ofEpochDay(epochDay)
    }

    override fun dateNow(): EthiopicDate {
        return EthiopicDate.now()
    }

    override fun dateNow(zone: ZoneId): EthiopicDate {
        return EthiopicDate.now(zone)
    }

    override fun dateNow(clock: Clock): EthiopicDate {
        return EthiopicDate.now(clock)
    }

    override fun date(temporal: TemporalAccessor): EthiopicDate {
        return EthiopicDate.from(temporal)
    }

    @Suppress("UNCHECKED_CAST")
    override fun localDateTime(temporal: TemporalAccessor): ChronoLocalDateTime<EthiopicDate> {
        return super.localDateTime(temporal) as ChronoLocalDateTime<EthiopicDate>
    }

    @Suppress("UNCHECKED_CAST")
    override fun zonedDateTime(temporal: TemporalAccessor): ChronoZonedDateTime<EthiopicDate> {
        return super.zonedDateTime(temporal) as ChronoZonedDateTime<EthiopicDate>
    }

    @Suppress("UNCHECKED_CAST")
    override fun zonedDateTime(instant: Instant, zone: ZoneId): ChronoZonedDateTime<EthiopicDate> {
        return super.zonedDateTime(instant, zone) as ChronoZonedDateTime<EthiopicDate>
    }

    override fun prolepticYear(era: Era, yearOfEra: Int): Int {
        if (era !is EthiopicEra) {
            throw ClassCastException("Era must be EthiopicEra")
        }
        return if (era == EthiopicEra.INCARNATION) yearOfEra else 1 - yearOfEra
    }

    override fun eraOf(eraValue: Int): EthiopicEra {
        return EthiopicEra.of(eraValue)
    }

    override fun eras(): List<Era> {
        return listOf(EthiopicEra.BEFORE_INCARNATION, EthiopicEra.INCARNATION)
    }

    override fun range(field: ChronoField): ValueRange {
        return when (field) {
            ChronoField.DAY_OF_MONTH -> DOM_RANGE
            ChronoField.ALIGNED_WEEK_OF_MONTH -> ALIGNED_WOM_RANGE
            ChronoField.MONTH_OF_YEAR -> MOY_RANGE
            ChronoField.PROLEPTIC_MONTH -> PROLEPTIC_MONTH_RANGE
            ChronoField.YEAR_OF_ERA -> YOE_RANGE
            ChronoField.YEAR -> YEAR_RANGE
            else -> field.range()
        }
    }

    override fun resolveDate(
        fieldValues: MutableMap<TemporalField, Long>,
        resolverStyle: ResolverStyle
    ): EthiopicDate {
        return super.resolveDate(fieldValues, resolverStyle) as EthiopicDate
    }
}
