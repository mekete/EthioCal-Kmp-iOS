package com.shalom.calendar.domain.calculator

import com.shalom.calendar.domain.model.Holiday
import com.shalom.calendar.domain.model.HolidayType
import com.shalom.ethiopicchrono.ChronoField
import com.shalom.ethiopicchrono.ChronoUnit
import com.shalom.ethiopicchrono.EthiopicDate

/**
 * Calculates Ethiopian Orthodox moveable holidays using the traditional Ethiopian method
 * based on Metqi, Tewusak, and Nineveh calculations.
 * KMP-compatible version using constructor injection.
 */
class OrthodoxHolidayCalculator(
    private val resourceProvider: ResourceProvider
) {

    companion object {
        private const val BEFORE_COMMON_ERA = 5500
        private const val MAX_DAYS_IN_LUNAR_MONTH = 30
        private const val NIOUS_QEMER = 19
        private const val MONTH_MESKEREM = 1
        private const val MONTH_TIKIMIT = 2
        private const val MONTH_TIR = 5
        private const val MONTH_YEKATIT = 6
    }

    fun getOrthodoxHolidaysForYear(
        ethiopianYear: Int,
        includeDayOffHolidays: Boolean = true,
        includeWorkingHolidays: Boolean = true
    ): List<Holiday> {
        val fixed = getFixedOrthodoxHolidays(ethiopianYear, includeDayOffHolidays, includeWorkingHolidays)
        val movable = getRunningHolidaysForYear(ethiopianYear, includeDayOffHolidays, includeWorkingHolidays)
        return fixed + movable
    }

    fun getRunningHolidaysForYear(
        ethiopianYear: Int,
        includeDayOffHolidays: Boolean = true,
        includeWorkingHolidays: Boolean = true
    ): List<Holiday> {
        // Calculate Nineveh first, as it's the reference point for all movable holidays
        val nineveh = calculateNineveh(ethiopianYear)

        // Calculate derived dates using ChronoUnit days
        val abiyTsom = nineveh.plus(14, ChronoUnit.DAYS)
        val debreZeit = nineveh.plus(41, ChronoUnit.DAYS)
        val hosanna = nineveh.plus(62, ChronoUnit.DAYS)
        val goodFriday = nineveh.plus(67, ChronoUnit.DAYS)
        val easter = nineveh.plus(69, ChronoUnit.DAYS)
        val rikbeKahinat = nineveh.plus(93, ChronoUnit.DAYS)
        val ascension = nineveh.plus(108, ChronoUnit.DAYS)
        val pentecost = nineveh.plus(118, ChronoUnit.DAYS)
        val tsomeHawariat = nineveh.plus(119, ChronoUnit.DAYS)
        val tsomeDihnet = nineveh.plus(121, ChronoUnit.DAYS)

        val movableWorkingHolidays = if (includeWorkingHolidays) {
            listOf(
                Holiday(
                    id = "orthodox_nineveh_$ethiopianYear",
                    title = resourceProvider.getString("holiday_orthodox_nineveh"),
                    nameAmharic = "ጾመ ነነዌ",
                    type = HolidayType.ORTHODOX_WORKING,
                    ethiopianMonth = nineveh.get(ChronoField.MONTH_OF_YEAR),
                    ethiopianDay = nineveh.get(ChronoField.DAY_OF_MONTH),
                    isDayOff = false,
                    isFixedDate = false,
                    description = resourceProvider.getString("holiday_default_description")
                ),
                Holiday(
                    id = "orthodox_abiy_tsom_$ethiopianYear",
                    title = resourceProvider.getString("holiday_orthodox_abiy_tsom"),
                    nameAmharic = "አብይ ጾም",
                    type = HolidayType.ORTHODOX_WORKING,
                    ethiopianMonth = abiyTsom.get(ChronoField.MONTH_OF_YEAR),
                    ethiopianDay = abiyTsom.get(ChronoField.DAY_OF_MONTH),
                    isDayOff = false,
                    isFixedDate = false,
                    description = resourceProvider.getString("holiday_default_description")
                ),
                Holiday(
                    id = "orthodox_debre_zeit_$ethiopianYear",
                    title = resourceProvider.getString("holiday_orthodox_debre_zeit"),
                    nameAmharic = "ደብረ ዘይት",
                    type = HolidayType.ORTHODOX_WORKING,
                    ethiopianMonth = debreZeit.get(ChronoField.MONTH_OF_YEAR),
                    ethiopianDay = debreZeit.get(ChronoField.DAY_OF_MONTH),
                    isDayOff = false,
                    isFixedDate = false,
                    description = resourceProvider.getString("holiday_default_description")
                ),
                Holiday(
                    id = "orthodox_hosanna_$ethiopianYear",
                    title = resourceProvider.getString("holiday_orthodox_hosanna"),
                    nameAmharic = "ሆሣእና",
                    type = HolidayType.ORTHODOX_WORKING,
                    ethiopianMonth = hosanna.get(ChronoField.MONTH_OF_YEAR),
                    ethiopianDay = hosanna.get(ChronoField.DAY_OF_MONTH),
                    isDayOff = false,
                    isFixedDate = false,
                    description = resourceProvider.getString("holiday_default_description")
                )
            )
        } else {
            emptyList()
        }

        val movableDayOffHolidays = if (includeDayOffHolidays) {
            listOf(
                Holiday(
                    id = "orthodox_siklet_$ethiopianYear",
                    title = resourceProvider.getString("holiday_orthodox_siklet"),
                    nameAmharic = "ስቅለት",
                    type = HolidayType.ORTHODOX_DAY_OFF,
                    ethiopianMonth = goodFriday.get(ChronoField.MONTH_OF_YEAR),
                    ethiopianDay = goodFriday.get(ChronoField.DAY_OF_MONTH),
                    isDayOff = true,
                    isFixedDate = false,
                    description = resourceProvider.getString("holiday_default_description")
                ),
                Holiday(
                    id = "orthodox_fasika_$ethiopianYear",
                    title = resourceProvider.getString("holiday_orthodox_fasika"),
                    nameAmharic = "ፋሲካ",
                    type = HolidayType.ORTHODOX_DAY_OFF,
                    ethiopianMonth = easter.get(ChronoField.MONTH_OF_YEAR),
                    ethiopianDay = easter.get(ChronoField.DAY_OF_MONTH),
                    isDayOff = true,
                    isFixedDate = false,
                    description = resourceProvider.getString("holiday_default_description")
                )
            )
        } else {
            emptyList()
        }

        return movableWorkingHolidays + movableDayOffHolidays
    }

    private fun getFixedOrthodoxHolidays(
        ethiopianYear: Int,
        includeDayOffHolidays: Boolean,
        includeWorkingHolidays: Boolean
    ): List<Holiday> {
        val fixedDayOffHolidays = if (includeDayOffHolidays) {
            listOf(
                Holiday(
                    id = "orthodox_day_off_meskel_$ethiopianYear",
                    title = resourceProvider.getString("holiday_public_christian_meskel"),
                    nameAmharic = "መስቀል",
                    type = HolidayType.ORTHODOX_DAY_OFF,
                    ethiopianMonth = 1,
                    ethiopianDay = 17,
                    isDayOff = true,
                    isFixedDate = true,
                    description = resourceProvider.getString("holiday_default_description")
                ),
                Holiday(
                    id = "orthodox_day_off_christmas_$ethiopianYear",
                    title = resourceProvider.getString("holiday_public_christian_genna"),
                    nameAmharic = "ገና",
                    type = HolidayType.ORTHODOX_DAY_OFF,
                    ethiopianMonth = 4,
                    ethiopianDay = getChristmasDay(ethiopianYear),
                    isDayOff = true,
                    isFixedDate = false,
                    description = resourceProvider.getString("holiday_default_description")
                ),
                Holiday(
                    id = "orthodox_day_off_timkat_$ethiopianYear",
                    title = resourceProvider.getString("holiday_public_christian_timket"),
                    nameAmharic = "ጥምቀት",
                    type = HolidayType.ORTHODOX_DAY_OFF,
                    ethiopianMonth = 5,
                    ethiopianDay = 11,
                    isDayOff = true,
                    isFixedDate = true,
                    description = resourceProvider.getString("holiday_default_description")
                )
            )
        } else {
            emptyList()
        }

        return fixedDayOffHolidays
    }

    private fun getChristmasDay(ethiopianYear: Int): Int {
        return if (ethiopianYear % 4 == 3) 29 else 28
    }

    private fun calculateNineveh(ethiopianYear: Int): EthiopicDate {
        val metqi = getMetqiForEthiopianYear(ethiopianYear)
        val metiqMonth = if (metqi <= 8) MONTH_TIKIMIT else MONTH_MESKEREM
        val metqiDate = metqi
        val tewusak = getTewusakFromMetiq(ethiopianYear, metiqMonth, metqiDate)
        val mebajaHamer = metqiDate + tewusak

        val ninevehMonth = if (mebajaHamer > 30 || metiqMonth == MONTH_TIKIMIT) {
            MONTH_YEKATIT
        } else {
            MONTH_TIR
        }

        val ninevehDate = (mebajaHamer % 30).let { if (it == 0) 30 else it }
        return EthiopicDate.of(ethiopianYear, ninevehMonth, ninevehDate)
    }

    private fun getMetqiForEthiopianYear(ethiopianYear: Int): Int {
        val yearsSinceCreation = BEFORE_COMMON_ERA + ethiopianYear
        val medeb = yearsSinceCreation % NIOUS_QEMER
        val wember = (medeb - 1 + NIOUS_QEMER) % NIOUS_QEMER
        val abikete = (wember * 11) % MAX_DAYS_IN_LUNAR_MONTH
        return MAX_DAYS_IN_LUNAR_MONTH - abikete
    }

    private fun getTewusakFromMetiq(ethiopianYear: Int, bealeMetiqMonth: Int, bealeMetiqDate: Int): Int {
        val metiqDate = EthiopicDate.of(ethiopianYear, bealeMetiqMonth, bealeMetiqDate)
        val dayOfWeekValue = metiqDate.get(ChronoField.DAY_OF_WEEK)
        val tewusak = ((128 - dayOfWeekValue - 2) % 7).let {
            if (it <= 1) it + 7 else it
        }
        return tewusak
    }
}
