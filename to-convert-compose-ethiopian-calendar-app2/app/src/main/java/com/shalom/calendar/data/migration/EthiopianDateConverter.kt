package com.shalom.calendar.data.migration


import com.shalom.ethiopicchrono.EthiopicDate
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoField

/**
 * Utilities for converting between Gregorian and Ethiopian dates using ThreeTen-Extra.
 *
 * This is used specifically for migrating old app data that was stored with Joda-Time
 * EthiopicChronology timestamps.
 */
object EthiopianDateConverter {


    fun convertGregorianMillisToEthiopianDateTime(gregorianMillis: Long): EthiopianDateTime {

        val zonedDateTime = Instant.ofEpochMilli(gregorianMillis).atZone(ZoneId.systemDefault())

        val gregorianDate = zonedDateTime.toLocalDate()

        val ethiopianDate = EthiopicDate.from(gregorianDate)

        val result = EthiopianDateTime(year = ethiopianDate.get(ChronoField.YEAR), month = ethiopianDate.get(ChronoField.MONTH_OF_YEAR), day = ethiopianDate.get(ChronoField.DAY_OF_MONTH), hour = zonedDateTime.hour, minute = zonedDateTime.minute, second = zonedDateTime.second)


        return result
    }


    /**
     * Converts Gregorian millis to ZonedDateTime in Ethiopian timezone.
     *
     * @param gregorianMillis Timestamp in milliseconds
     * @return ZonedDateTime in Africa/Addis_Ababa timezone
     */
    fun convertMillisToZonedDateTime(gregorianMillis: Long): ZonedDateTime {
        return Instant.ofEpochMilli(gregorianMillis).atZone(ZoneId.systemDefault())
    }


}

/**
 * Represents an Ethiopian date with time components.
 */
data class EthiopianDateTime(
    val year: Int, val month: Int, val day: Int, val hour: Int, val minute: Int, val second: Int = 0
) {

    override fun toString(): String {
        return "$year-$month-$day $hour:$minute:$second"
    }
}
