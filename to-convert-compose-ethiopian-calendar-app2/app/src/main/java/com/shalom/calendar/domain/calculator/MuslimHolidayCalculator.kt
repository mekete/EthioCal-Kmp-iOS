package com.shalom.calendar.domain.calculator

import com.shalom.calendar.R
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.data.provider.ResourceProvider
import com.shalom.calendar.data.remote.ConfigHolidayOffset
import com.shalom.calendar.domain.model.Holiday
import com.shalom.calendar.domain.model.HolidayType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.threeten.extra.chrono.EthiopicDate
import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Calculates Islamic holidays using the Hijrah calendar.
 * All dates are calculated astronomically and may vary ±1-2 days based on moon sighting.
 *
 * This implementation:
 *  - Converts the Ethiopian-year interval to Gregorian LocalDate range
 *  - Determines the overlapping Hijrah years (via HijrahDate.from(LocalDate))
 *  - Generates Hijrah dates for standard Muslim holidays and converts them to EthiopicDate
 *  - Filters to only return holidays that fall inside the requested Ethiopian year
 */
@Singleton
class MuslimHolidayCalculator @Inject constructor(
    private val settingsPreferences: SettingsPreferences, private val resourceProvider: ResourceProvider
) {

    // JSON parser with lenient configuration
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    /**
     * Get Muslim holidays for a specific Ethiopian year
     */
    fun getMuslimHolidaysForEthiopianYear(
        ethiopianYear: Int, includeDayOffHolidays: Boolean = true, includeWorkingHolidays: Boolean = true
    ): List<Holiday> {
        val holidays = mutableListOf<Holiday>()

        // Retrieve holiday offsets once for the Ethiopian year
        val offsets = getHolidayOffsets(ethiopianYear) // Ethiopian start (Meskerem 1) and next-year start (exclusive)
        val startEthiopian = EthiopicDate.of(ethiopianYear, 1, 1)
        val endEthiopian = EthiopicDate.of(ethiopianYear + 1, 1, 1)

        // Convert Ethiopic boundaries to Gregorian LocalDate for comparison
        val startGregorian = LocalDate.from(startEthiopian) // throws if conversion not supported
        val endGregorian = LocalDate.from(endEthiopian)

        // Determine Hijri years overlapping this Gregorian interval
        val startHijrah = HijrahDate.from(startGregorian)
        val endHijrah = HijrahDate.from(endGregorian.minusDays(1)) // make end inclusive for overlap calculation

        for (hijriYear in startHijrah.get(ChronoField.YEAR_OF_ERA)..endHijrah.get(ChronoField.YEAR_OF_ERA)) {

            if (includeDayOffHolidays) {
                holidays.addAll(getDayOffMuslimHolidays(hijriYear, ethiopianYear, offsets))
            }

            if (includeWorkingHolidays) {
                holidays.addAll(getWorkingMuslimHolidays(hijriYear, ethiopianYear, offsets))
            }
        }

        // Filter to only holidays whose Gregorian date falls inside the Ethiopian year range
        return holidays.filter { holiday ->
            val muslimHolidayInEthiopicDate = EthiopicDate.of(holiday.ethiopianYear, holiday.ethiopianMonth, holiday.ethiopianDay)
            !muslimHolidayInEthiopicDate.isBefore(startEthiopian) && muslimHolidayInEthiopicDate.isBefore(endEthiopian)
        }
    }

    /**
     * Retrieve holiday offset configuration for the specified Ethiopian year
     */
    private fun getHolidayOffsets(ethiopianYear: Int): ConfigHolidayOffset {
        return runBlocking {
            val jsonString = settingsPreferences.holidayOffsetConfigJson.first()

            if (jsonString.isNotEmpty()) {
                try {
                    val configList = json.decodeFromString<List<ConfigHolidayOffset>>(jsonString)

                    // Find config for the current Ethiopian year
                    configList.find { it.offsetEthioYear == ethiopianYear } ?: configList.firstOrNull() // Fallback to first config if exact year not found
                    ?: ConfigHolidayOffset() // Return default if list is empty
                } catch (e: Exception) { // Return default values if parsing fails
                    ConfigHolidayOffset()
                }
            } else { // Return default values if no config is stored
                ConfigHolidayOffset()
            }
        }
    }

    private fun getDayOffMuslimHolidays(
        hijriYear: Int, ethiopianYear: Int, offsets: ConfigHolidayOffset
    ): List<Holiday> {
        val holidays = mutableListOf<Holiday>()

        val shouldApplyOffsets = offsets.offsetEthioYear == ethiopianYear && hijriYear == offsets.offsetHirjiYear

        // Eid al-Fitr (Shawwal 1) — Hijrah month 10, day 1
        var eidFitrDate = HijrahDate.of(hijriYear, 10, 1)
        if (shouldApplyOffsets && offsets.offsetEidAlFitr != 0) {
            eidFitrDate = eidFitrDate.plus(offsets.offsetEidAlFitr.toLong(), ChronoUnit.DAYS)
        }




        holidays.add(createMuslimHoliday(id = "muslim_eid_fitr_$hijriYear", title = resourceProvider.getString(R.string.holiday_muslim_eid_fitr), nameAmharic = "ኢድ አል-ፈጥር", hijriDate = eidFitrDate, isDayOff = true, isVerified = isHolidayVerified(eidFitrDate, offsets, shouldApplyOffsets), type = HolidayType.MUSLIM_DAY_OFF, description = resourceProvider.getString(R.string.holiday_muslim_eid_fitr_history), celebration = resourceProvider.getString(R.string.holiday_muslim_eid_fitr_celebration)))

        // Eid al-Adha (Dhu al-Hijjah 10) — Hijrah month 12, day 10
        var eidAdhaDate = HijrahDate.of(hijriYear, 12, 10)
        if (shouldApplyOffsets && offsets.offsetEidAlAdha != 0) {
            eidAdhaDate = eidAdhaDate.plus(offsets.offsetEidAlAdha.toLong(), ChronoUnit.DAYS)
        }
        holidays.add(createMuslimHoliday(id = "muslim_eid_adha_$hijriYear", title = resourceProvider.getString(R.string.holiday_muslim_eid_adha), nameAmharic = "ኢድ አል-አድሃ", hijriDate = eidAdhaDate, isDayOff = true, isVerified = isHolidayVerified(eidAdhaDate, offsets, shouldApplyOffsets), type = HolidayType.MUSLIM_DAY_OFF, description = resourceProvider.getString(R.string.holiday_muslim_eid_adha_history), celebration = resourceProvider.getString(R.string.holiday_muslim_eid_adha_celebration)))

        // Mawlid al-Nabi (Rabi' al-Awwal 12) — Hijrah month 3, day 12
        var mawlidDate = HijrahDate.of(hijriYear, 3, 12)
        if (shouldApplyOffsets && offsets.offsetMawlid != 0) {
            mawlidDate = mawlidDate.plus(offsets.offsetMawlid.toLong(), ChronoUnit.DAYS)
        }
        holidays.add(createMuslimHoliday(id = "muslim_mawlid_$hijriYear", title = resourceProvider.getString(R.string.holiday_muslim_mawlid), nameAmharic = "መውሊድ", hijriDate = mawlidDate, isDayOff = true, isVerified = isHolidayVerified(mawlidDate, offsets, shouldApplyOffsets), type = HolidayType.MUSLIM_DAY_OFF, description = resourceProvider.getString(R.string.holiday_muslim_mawlid_history), celebration = resourceProvider.getString(R.string.holiday_muslim_mawlid_celebration)))

        return holidays
    }

    fun isHolidayVerified(holiday: HijrahDate, offsets: ConfigHolidayOffset, shouldApplyOffsets: Boolean): Boolean {
        val afterTwoDays = HijrahDate.now().plus(2, ChronoUnit.DAYS)
        val isEidInTwoDaysTimeOrPast = !holiday.isAfter(afterTwoDays) //future holidays can be known in 2 days
        val isVerified = offsets.offsetUpdateTimestamp > 0 && shouldApplyOffsets && isEidInTwoDaysTimeOrPast
        return isVerified
    }

    private fun getWorkingMuslimHolidays(
        hijriYear: Int, ethiopianYear: Int, offsets: ConfigHolidayOffset
    ): List<Holiday> {
        val holidays = mutableListOf<Holiday>()

        // Only apply offsets if the config year matches the Ethiopian year being calculated
        val shouldApplyOffsets = offsets.offsetEthioYear == ethiopianYear

        // Determine if holidays are verified based on remote config timestamp
        val isVerified = offsets.offsetUpdateTimestamp > 0

        // Islamic New Year (Muharram 1) — Hijrah month 1, day 1
        val newYearDate = HijrahDate.of(hijriYear, 1, 1)
        holidays.add(createMuslimHoliday(id = "muslim_new_year_$hijriYear", title = resourceProvider.getString(R.string.holiday_muslim_hijri_new_year), nameAmharic = "የሙስሊም አዲስ ዓመት", hijriDate = newYearDate, type = HolidayType.MUSLIM_WORKING, isDayOff = false, isVerified = isVerified, description = resourceProvider.getString(R.string.holiday_muslim_hijri_new_year_history), celebration = resourceProvider.getString(R.string.holiday_muslim_hijri_new_year_celebration)))

        // Ashura (Muharram 10)
        val ashuraDate = HijrahDate.of(hijriYear, 1, 10)
        holidays.add(createMuslimHoliday(id = "muslim_ashura_$hijriYear", title = resourceProvider.getString(R.string.holiday_muslim_ashura), nameAmharic = "አሹራ", hijriDate = ashuraDate, isDayOff = false, isVerified = isVerified, type = HolidayType.MUSLIM_WORKING, description = resourceProvider.getString(R.string.holiday_muslim_ashura_history), celebration = resourceProvider.getString(R.string.holiday_muslim_ashura_celebration)))

        // Start of Ramadan (Ramadan 1) — Hijrah month 9, day 1
        var ramadanDate = HijrahDate.of(hijriYear, 9, 1)
        if (shouldApplyOffsets && offsets.offsetRamadanStart != 0) {
            ramadanDate = ramadanDate.plus(offsets.offsetRamadanStart.toLong(), ChronoUnit.DAYS)
        }
        holidays.add(createMuslimHoliday(id = "muslim_ramadan_$hijriYear", title = resourceProvider.getString(R.string.holiday_muslim_ramadan_start), nameAmharic = "የረመዳን መጀመሪያ", hijriDate = ramadanDate, isDayOff = false, isVerified = isHolidayVerified(ramadanDate, offsets, shouldApplyOffsets), type = HolidayType.MUSLIM_WORKING, description = resourceProvider.getString(R.string.holiday_muslim_ramadan_start_history), celebration = resourceProvider.getString(R.string.holiday_muslim_ramadan_start_celebration)))

        // Mid-Sha'ban (Sha'ban 15) — Hijrah month 8, day 15
        val midShabanDate = HijrahDate.of(hijriYear, 8, 15)
        holidays.add(createMuslimHoliday(id = "muslim_mid_shaban_$hijriYear", title = resourceProvider.getString(R.string.holiday_muslim_mid_shaban), nameAmharic = "መካከለኛ ሻዕባን", hijriDate = midShabanDate, isDayOff = false, isVerified = isVerified, type = HolidayType.MUSLIM_WORKING, description = resourceProvider.getString(R.string.holiday_muslim_mid_shaban_history), celebration = resourceProvider.getString(R.string.holiday_muslim_mid_shaban_celebration)))

        return holidays
    }

    /**
     * Create a Holiday from a HijrahDate by converting to Gregorian then to EthiopicDate.
     *
     * The ThreeTen-Extra EthiopicDate is a ChronoLocalDate; conversion path:
     *   HijrahDate -> LocalDate -> EthiopicDate
     *
     * We extract month/day using ChronoField to be robust against chronology implementations.
     */
    private fun createMuslimHoliday(
        id: String, title: String, nameAmharic: String, hijriDate: HijrahDate, isDayOff: Boolean, isVerified: Boolean = false, type: HolidayType, description: String, celebration: String

    ): Holiday {
        val ethiopic: EthiopicDate = EthiopicDate.from(LocalDate.from(hijriDate))
        val ethiopianYear = ethiopic.get(ChronoField.YEAR)
        val ethiopianMonth = ethiopic.get(ChronoField.MONTH_OF_YEAR)
        val ethiopianDay = ethiopic.get(ChronoField.DAY_OF_MONTH)

        return Holiday(id = id, title = title, nameAmharic = nameAmharic, type = type, ethiopianYear = ethiopianYear, ethiopianMonth = ethiopianMonth, ethiopianDay = ethiopianDay, isDayOff = isDayOff, isVerified = isVerified, isFixedDate = false, description = description, celebration = celebration)
    }
}
