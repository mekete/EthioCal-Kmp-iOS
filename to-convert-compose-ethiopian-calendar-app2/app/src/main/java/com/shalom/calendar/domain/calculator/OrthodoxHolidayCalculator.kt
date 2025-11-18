package com.shalom.calendar.domain.calculator

import com.shalom.calendar.R
import com.shalom.calendar.data.provider.ResourceProvider
import com.shalom.calendar.domain.model.Holiday
import com.shalom.calendar.domain.model.HolidayType
import com.shalom.ethiopicchrono.EthiopicDate
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Calculates Ethiopian Orthodox moveable holidays using the traditional Ethiopian method
 * based on Metqi, Tewusak, and Nineveh calculations (matching OrthodoxHolidayManager.java)
 */
@Singleton
class OrthodoxHolidayCalculator @Inject constructor(
    private val resourceProvider: ResourceProvider
) {

    companion object {
        private const val BEFORE_COMMON_ERA = 5500 // God created the world 5500 years before the birth of Our Lord
        private const val MAX_DAYS_IN_LUNAR_MONTH = 30
        private const val NIOUS_QEMER = 19
        private const val MONTH_MESKEREM = 1
        private const val MONTH_TIKIMIT = 2
        private const val MONTH_TIR = 5
        private const val MONTH_YEKATIT = 6
    }

    fun getOrthodoxHolidaysForYear(ethiopianYear: Int, includeDayOffHolidays: Boolean, includeWorkingHolidays: Boolean): List<Holiday> {
        val fixed = getFixedOrthodoxHolidays(ethiopianYear, includeDayOffHolidays, includeWorkingHolidays)
        val movable = getRunningHolidaysForYear(ethiopianYear, includeDayOffHolidays, includeWorkingHolidays)
        return fixed + movable
    }


    fun getRunningHolidaysForYear(ethiopianYear: Int, includeDayOffHolidays: Boolean, includeWorkingHolidays: Boolean): List<Holiday> { // Calculate Nineveh first, as it's the reference point for all movable holidays
        val nineveh = calculateNineveh(ethiopianYear)

        // Calculate derived dates using ChronoUnit days
        val abiyTsom = nineveh.plus(14, ChronoUnit.DAYS) as EthiopicDate
        val debreZeit = nineveh.plus(41, ChronoUnit.DAYS) as EthiopicDate
        val hosanna = nineveh.plus(62, ChronoUnit.DAYS) as EthiopicDate
        val goodFriday = nineveh.plus(67, ChronoUnit.DAYS) as EthiopicDate
        val easter = nineveh.plus(69, ChronoUnit.DAYS) as EthiopicDate
        val rikbeKahinat = nineveh.plus(93, ChronoUnit.DAYS) as EthiopicDate
        val ascension = nineveh.plus(108, ChronoUnit.DAYS) as EthiopicDate
        val pentecost = nineveh.plus(118, ChronoUnit.DAYS) as EthiopicDate
        val tsomeHawariat = nineveh.plus(119, ChronoUnit.DAYS) as EthiopicDate
        val tsomeDihnet = nineveh.plus(121, ChronoUnit.DAYS) as EthiopicDate

        val movableWorkingHolidays = if (includeWorkingHolidays) {
            listOf(

                Holiday(id = "orthodox_nineveh_$ethiopianYear",
                    title = resourceProvider.getString(R.string.holiday_orthodox_nineveh),
                    nameAmharic = "ጾመ ነነዌ",
                    type = HolidayType.ORTHODOX_WORKING,
                    ethiopianMonth = nineveh.get(ChronoField.MONTH_OF_YEAR),
                    ethiopianDay = nineveh.get(ChronoField.DAY_OF_MONTH),
                    isDayOff = false,
                    isFixedDate = false,
                    description = resourceProvider.getString(R.string.holiday_orthodox_nineveh_history),
                    celebration = resourceProvider.getString(R.string.holiday_orthodox_nineveh_celebration)),
                Holiday(id = "orthodox_abiy_tsom_$ethiopianYear",
                    title = resourceProvider.getString(R.string.holiday_orthodox_abiy_tsom),
                    nameAmharic = "አብይ ጾም",
                    type = HolidayType.ORTHODOX_WORKING,
                    ethiopianMonth = abiyTsom.get(ChronoField.MONTH_OF_YEAR),
                    ethiopianDay = abiyTsom.get(ChronoField.DAY_OF_MONTH),
                    isDayOff = false,
                    isFixedDate = false,
                    description = resourceProvider.getString(R.string.holiday_orthodox_abiy_tsom_history),
                    celebration = resourceProvider.getString(R.string.holiday_orthodox_abiy_tsom_celebration)),
                Holiday(id = "orthodox_debre_zeit_$ethiopianYear",
                    title = resourceProvider.getString(R.string.holiday_orthodox_debre_zeit),
                    nameAmharic = "ደብረ ዘይት",
                    type = HolidayType.ORTHODOX_WORKING,
                    ethiopianMonth = debreZeit.get(ChronoField.MONTH_OF_YEAR),
                    ethiopianDay = debreZeit.get(ChronoField.DAY_OF_MONTH),
                    isDayOff = false,
                    isFixedDate = false,
                    description = resourceProvider.getString(R.string.holiday_orthodox_debre_zeit_history),
                    celebration = resourceProvider.getString(R.string.holiday_orthodox_debre_zeit_celebration)),
                Holiday(id = "orthodox_hosanna_$ethiopianYear",
                    title = resourceProvider.getString(R.string.holiday_orthodox_hosanna),
                    nameAmharic = "ሆሣእና",
                    type = HolidayType.ORTHODOX_WORKING,
                    ethiopianMonth = hosanna.get(ChronoField.MONTH_OF_YEAR),
                    ethiopianDay = hosanna.get(ChronoField.DAY_OF_MONTH),
                    isDayOff = false,
                    isFixedDate = false,
                    description = resourceProvider.getString(R.string.holiday_orthodox_hosanna_history),
                    celebration = resourceProvider.getString(R.string.holiday_orthodox_hosanna_celebration)),
                Holiday(id = "orthodox_rikbe_kahinat_$ethiopianYear",
                    title = resourceProvider.getString(R.string.holiday_orthodox_rikbe_kahinat),
                    nameAmharic = "ርክበ ካህናት",
                    type = HolidayType.ORTHODOX_WORKING,
                    ethiopianMonth = rikbeKahinat.get(ChronoField.MONTH_OF_YEAR),
                    ethiopianDay = rikbeKahinat.get(ChronoField.DAY_OF_MONTH),
                    isDayOff = false,
                    isFixedDate = false,
                    description = resourceProvider.getString(R.string.holiday_orthodox_rikbe_kahinat_history),
                    celebration = resourceProvider.getString(R.string.holiday_orthodox_rikbe_kahinat_celebration)),
                Holiday(id = "orthodox_erget_$ethiopianYear", title = resourceProvider.getString(R.string.holiday_orthodox_erget), nameAmharic = "እርገት", type = HolidayType.ORTHODOX_WORKING, ethiopianMonth = ascension.get(ChronoField.MONTH_OF_YEAR), ethiopianDay = ascension.get(ChronoField.DAY_OF_MONTH), isDayOff = false, isFixedDate = false, description = resourceProvider.getString(R.string.holiday_orthodox_erget_history), celebration = resourceProvider.getString(R.string.holiday_orthodox_erget_celebration)),
                Holiday(id = "orthodox_peraklitos_$ethiopianYear",
                    title = resourceProvider.getString(R.string.holiday_orthodox_peraklitos),
                    nameAmharic = "ጴራቅሊጦስ",
                    type = HolidayType.ORTHODOX_WORKING,
                    ethiopianMonth = pentecost.get(ChronoField.MONTH_OF_YEAR),
                    ethiopianDay = pentecost.get(ChronoField.DAY_OF_MONTH),
                    isDayOff = false,
                    isFixedDate = false,
                    description = resourceProvider.getString(R.string.holiday_orthodox_peraklitos_history),
                    celebration = resourceProvider.getString(R.string.holiday_orthodox_peraklitos_celebration)),
                Holiday(id = "orthodox_tsome_hawariat_$ethiopianYear",
                    title = resourceProvider.getString(R.string.holiday_orthodox_tsome_hawariat),
                    nameAmharic = "ጾመ ሐዋርያት",
                    type = HolidayType.ORTHODOX_WORKING,
                    ethiopianMonth = tsomeHawariat.get(ChronoField.MONTH_OF_YEAR),
                    ethiopianDay = tsomeHawariat.get(ChronoField.DAY_OF_MONTH),
                    isDayOff = false,
                    isFixedDate = false,
                    description = resourceProvider.getString(R.string.holiday_orthodox_tsome_hawariat_history),
                    celebration = resourceProvider.getString(R.string.holiday_orthodox_tsome_hawariat_celebration)),
                Holiday(id = "orthodox_tsome_dihnet_$ethiopianYear",
                    title = resourceProvider.getString(R.string.holiday_orthodox_tsome_dihnet),
                    nameAmharic = "ጾመ ድኅነት",
                    type = HolidayType.ORTHODOX_WORKING,
                    ethiopianMonth = tsomeDihnet.get(ChronoField.MONTH_OF_YEAR),
                    ethiopianDay = tsomeDihnet.get(ChronoField.DAY_OF_MONTH),
                    isDayOff = false,
                    isFixedDate = false,
                    description = resourceProvider.getString(R.string.holiday_orthodox_tsome_dihnet_history),
                    celebration = resourceProvider.getString(R.string.holiday_orthodox_tsome_dihnet_celebration)))
        } else {
            listOf()
        }

        val movableDayOffHolidays = if (includeDayOffHolidays) {
            listOf(Holiday(id = "orthodox_siklet_$ethiopianYear", title = resourceProvider.getString(R.string.holiday_orthodox_siklet), nameAmharic = "ስቅለት", type = HolidayType.ORTHODOX_DAY_OFF, ethiopianMonth = goodFriday.get(ChronoField.MONTH_OF_YEAR), ethiopianDay = goodFriday.get(ChronoField.DAY_OF_MONTH), isDayOff = true, isFixedDate = false, description = resourceProvider.getString(R.string.holiday_orthodox_siklet_history), celebration = resourceProvider.getString(R.string.holiday_orthodox_siklet_celebration)),
                Holiday(id = "orthodox_fasika_$ethiopianYear", title = resourceProvider.getString(R.string.holiday_orthodox_fasika), nameAmharic = "ፋሲካ", type = HolidayType.ORTHODOX_DAY_OFF, ethiopianMonth = easter.get(ChronoField.MONTH_OF_YEAR), ethiopianDay = easter.get(ChronoField.DAY_OF_MONTH), isDayOff = true, isFixedDate = false, description = resourceProvider.getString(R.string.holiday_orthodox_fasika_history), celebration = resourceProvider.getString(R.string.holiday_orthodox_fasika_celebration)))
        } else {
            listOf()
        }

        return movableWorkingHolidays + movableDayOffHolidays
    }

    private fun getFixedOrthodoxHolidays(ethiopianYear: Int, includeDayOffHolidays: Boolean, includeWorkingHolidays: Boolean): List<Holiday> {


        val fixedDayOffHolidays = if (includeDayOffHolidays) {
            listOf(Holiday(id = "orthodox_day_off_meskel_$ethiopianYear", title = resourceProvider.getString(R.string.holiday_public_christian_meskel), nameAmharic = "መስቀል", type = HolidayType.ORTHODOX_DAY_OFF, ethiopianMonth = 1, ethiopianDay = 17, isDayOff = true, isFixedDate = true, description = resourceProvider.getString(R.string.holiday_public_christian_meskel_history), celebration = resourceProvider.getString(R.string.holiday_public_christian_meskel_celebration)),
                Holiday(id = "orthodox_day_off_christmas_$ethiopianYear", title = resourceProvider.getString(R.string.holiday_public_christian_genna), nameAmharic = "ገና", type = HolidayType.ORTHODOX_DAY_OFF, ethiopianMonth = 4, ethiopianDay = getChristmasDay(ethiopianYear), isDayOff = true, isFixedDate = false, description = resourceProvider.getString(R.string.holiday_public_christian_genna_history), celebration = resourceProvider.getString(R.string.holiday_public_christian_genna_celebration)),
                Holiday(id = "orthodox_day_off_timkat_$ethiopianYear", title = resourceProvider.getString(R.string.holiday_public_christian_timket), nameAmharic = "ጥምቀት", type = HolidayType.ORTHODOX_DAY_OFF, ethiopianMonth = 5, ethiopianDay = 11, isDayOff = true, isFixedDate = true, description = resourceProvider.getString(R.string.holiday_public_christian_timket_history), celebration = resourceProvider.getString(R.string.holiday_public_christian_timket_celebration)))
        } else {
            listOf()
        }


        val fixedWorkingHolidays = if (includeWorkingHolidays) {
            listOf(Holiday(id = "orthodox_ghad_$ethiopianYear", title = resourceProvider.getString(R.string.holiday_orthodox_ghad), nameAmharic = "ገሃድ", type = HolidayType.ORTHODOX_WORKING, ethiopianMonth = 5, ethiopianDay = 10, isDayOff = false, isFixedDate = true, description = resourceProvider.getString(R.string.holiday_orthodox_ghad_history), celebration = resourceProvider.getString(R.string.holiday_orthodox_ghad_celebration)),
                Holiday(id = "orthodox_kana_zegelila_$ethiopianYear", title = resourceProvider.getString(R.string.holiday_orthodox_kana_zegelila), nameAmharic = "ቃና ዘገሊላ", type = HolidayType.ORTHODOX_WORKING, ethiopianMonth = 5, ethiopianDay = 12, isDayOff = false, isFixedDate = true, description = resourceProvider.getString(R.string.holiday_orthodox_kana_zegelila_history), celebration = resourceProvider.getString(R.string.holiday_orthodox_kana_zegelila_celebration)),
                Holiday(id = "orthodox_lideta_mariam_$ethiopianYear", title = resourceProvider.getString(R.string.holiday_orthodox_lideta_mariam), nameAmharic = "ልደተ ማርያም", type = HolidayType.ORTHODOX_WORKING, ethiopianMonth = 9, ethiopianDay = 1, isDayOff = false, isFixedDate = true, description = resourceProvider.getString(R.string.holiday_orthodox_lideta_mariam_history), celebration = resourceProvider.getString(R.string.holiday_orthodox_lideta_mariam_celebration)),
                Holiday(id = "orthodox_filseta_$ethiopianYear", title = resourceProvider.getString(R.string.holiday_orthodox_filseta), nameAmharic = "ፍልሰታ", type = HolidayType.ORTHODOX_WORKING, ethiopianMonth = 12, ethiopianDay = 1, isDayOff = false, isFixedDate = true, description = resourceProvider.getString(R.string.holiday_orthodox_filseta_history), celebration = resourceProvider.getString(R.string.holiday_orthodox_filseta_celebration)),
                Holiday(id = "orthodox_debre_tabor_$ethiopianYear", title = resourceProvider.getString(R.string.holiday_orthodox_debre_tabor), nameAmharic = "ደብረ ታቦር", type = HolidayType.ORTHODOX_WORKING, ethiopianMonth = 12, ethiopianDay = 13, isDayOff = false, isFixedDate = true, description = resourceProvider.getString(R.string.holiday_orthodox_debre_tabor_history), celebration = resourceProvider.getString(R.string.holiday_orthodox_debre_tabor_celebration)))
        } else {
            listOf()
        }

        return fixedDayOffHolidays + fixedWorkingHolidays

    }

    private fun getChristmasDay(ethiopianYear: Int): Int {
        return if (ethiopianYear % 4 == 3) 29 else 28
    }

    /**
     * Calculate Nineveh (Fast of Nineveh) - the reference point for all movable Ethiopian Orthodox holidays
     * This uses the traditional Ethiopian calculation based on Metqi and Tewusak
     */
    private fun calculateNineveh(ethiopianYear: Int): EthiopicDate {
        val metqi = getMetqiForEthiopianYear(ethiopianYear)

        // Metqi can be 8 or less (occurs in Tikimit/October) or greater than 8 (occurs in Meskerem/September)
        val metiqMonth = if (metqi <= 8) MONTH_TIKIMIT else MONTH_MESKEREM
        val metqiDate = metqi

        // Calculate Mebaja Hamer (Metqi date + Tewusak)
        val tewusak = getTewusakFromMetiq(ethiopianYear, metiqMonth, metqiDate)
        val mebajaHamer = metqiDate + tewusak

        // Determine Nineveh month and date
        // If mebajaHamer > 30 or metiqMonth is Tikimit, Nineveh is in Yekatit
        // Otherwise it's in Tir
        val ninevehMonth = if (mebajaHamer > 30 || metiqMonth == MONTH_TIKIMIT) {
            MONTH_YEKATIT
        } else {
            MONTH_TIR
        }

        val ninevehDate = (mebajaHamer % 30).let { if (it == 0) 30 else it }

        return EthiopicDate.of(ethiopianYear, ninevehMonth, ninevehDate)
    }

    /**
     * Calculate Metqi for a given Ethiopian year
     *
     * Metqi (መጥቅ) is the number of days it takes for Lunar month to complete
     * when it is overlapped with the beginning month of new year.
     *
     * Abekitee (አበቅቴ) refers to the difference or overlapping days between
     * Lunar and Solar calendar. Lunar calendar year is shorter than Solar by 11
     * days and this number increases every year.
     */
    private fun getMetqiForEthiopianYear(ethiopianYear: Int): Int {
        val yearsSinceCreation = BEFORE_COMMON_ERA + ethiopianYear

        // Medeb (መደብ) is the remainder of yearsSinceCreation divided by 19 (Nuskemer)
        val medeb = yearsSinceCreation % NIOUS_QEMER

        // Wenber (ወንበር) is obtained by subtracting 1 from Medeb
        // If remainder is 0, Medeb of the year is 0 and Wenber is 18
        val wember = (medeb - 1 + NIOUS_QEMER) % NIOUS_QEMER

        // Abikete (አበቅቴ) the difference or overlapping days between Lunar and Solar calendar
        val abikete = (wember * 11) % MAX_DAYS_IN_LUNAR_MONTH

        // Metqi: the number of days it takes for Lunar month to complete
        val metqi = MAX_DAYS_IN_LUNAR_MONTH - abikete

        // Note: Metqi cannot be 1, 3, 6, 9, 11, 14, 17, 20, 22, 25 and 28
        // Therefore it can be any of the other 19 numbers
        return metqi
    }

    /**
     * Calculate Tewusak from Metiq
     *
     * Tewsak is the number of days from end of the Nineveh Fast to the starting date of the Lent Fast.
     * If the number of days is greater than 30, it's divided by 30 and the remainder will be Tewsak.
     *
     * Example: If Beale Metqi is on Saturday, the number of days between the next day of
     * Beale Metqi and Nineveh is 128. So 128 divided by 30 gives a remainder of 8.
     * This remainder of 8 is called Tewsak which always fall on Tuesday.
     */
    private fun getTewusakFromMetiq(ethiopianYear: Int, bealeMetiqMonth: Int, bealeMetiqDate: Int): Int {
        val metiqDate = EthiopicDate.of(ethiopianYear, bealeMetiqMonth, bealeMetiqDate)

        // Get day of week (1 = Monday, 7 = Sunday in ISO standard)
        val dayOfWeekValue = metiqDate.get(ChronoField.DAY_OF_WEEK)

        // Calculate Tewusak based on the day of week
        // Formula: ((128 - dayOfWeek - 2) % 7) with adjustment if result <= 1
        val tewusak = ((128 - dayOfWeekValue - 2) % 7).let {
            if (it <= 1) it + 7 else it
        }

        return tewusak
    }

}