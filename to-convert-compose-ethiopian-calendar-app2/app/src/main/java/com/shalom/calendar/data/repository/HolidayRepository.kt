package com.shalom.calendar.data.repository

import com.shalom.calendar.data.preferences.HolidayPreferences
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.domain.calculator.MuslimHolidayCalculator
import com.shalom.calendar.domain.calculator.OrthodoxHolidayCalculator
import com.shalom.calendar.domain.calculator.PublicHolidayCalculator
import com.shalom.calendar.domain.model.HolidayOccurrence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.threeten.extra.chrono.EthiopicDate
import java.time.temporal.ChronoField
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HolidayRepository @Inject constructor(
    private val publicHolidayCalculator: PublicHolidayCalculator, private val orthodoxHolidayCalculator: OrthodoxHolidayCalculator, private val muslimHolidayCalculator: MuslimHolidayCalculator, private val settingsPreferences: SettingsPreferences
) {

    fun getHolidaysForYear(
        ethiopianYear: Int, preferences: HolidayPreferences? = null
    ): Flow<List<HolidayOccurrence>> {
        return flow { // Use provided preferences or fetch from settings
            val holidays = mutableListOf<HolidayOccurrence>()
            val nonReligiousDayOffHolidays = publicHolidayCalculator.getPublicHolidaysForYear(ethiopianYear)
            val allOrthodoxHolidays = orthodoxHolidayCalculator.getOrthodoxHolidaysForYear(ethiopianYear = ethiopianYear, includeDayOffHolidays = true, includeWorkingHolidays = true)
            val allMuslimHolidays = muslimHolidayCalculator.getMuslimHolidaysForEthiopianYear(ethiopianYear = ethiopianYear, includeDayOffHolidays = true, includeWorkingHolidays = true)

            holidays.addAll(nonReligiousDayOffHolidays.map { holiday ->
                HolidayOccurrence(holiday = holiday, ethiopicDate = EthiopicDate.of(ethiopianYear, holiday.ethiopianMonth, holiday.ethiopianDay), adjustment = 0)
            })
            holidays.addAll(allOrthodoxHolidays.map { holiday ->
                HolidayOccurrence(holiday = holiday, ethiopicDate = EthiopicDate.of(ethiopianYear, holiday.ethiopianMonth, holiday.ethiopianDay), adjustment = 0)
            })
            holidays.addAll(allMuslimHolidays.map { holiday ->
                HolidayOccurrence(holiday = holiday, ethiopicDate = EthiopicDate.of(ethiopianYear, holiday.ethiopianMonth, holiday.ethiopianDay), adjustment = 0)
            })

            val sortedHolidays = holidays.sortedWith(compareBy({ it.ethiopicDate }))

            emit(sortedHolidays)
        }
    }

    fun getHolidaysForMonth(
        ethiopianYear: Int, ethiopianMonth: Int, preferences: HolidayPreferences? = null
    ): Flow<List<HolidayOccurrence>> {
        return flow {
            val allHolidays = mutableListOf<HolidayOccurrence>()
            getHolidaysForYear(ethiopianYear = ethiopianYear, preferences = preferences).collect { yearHolidays ->
                allHolidays.addAll(yearHolidays)
            }
            val monthHolidays = allHolidays.filter {
                it.ethiopicDate.get(ChronoField.MONTH_OF_YEAR) == ethiopianMonth
            }
            emit(monthHolidays)
        }
    }


}
