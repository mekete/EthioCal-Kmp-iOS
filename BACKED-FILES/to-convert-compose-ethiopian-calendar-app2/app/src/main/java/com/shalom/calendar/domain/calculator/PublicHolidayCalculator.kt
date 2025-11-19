package com.shalom.calendar.domain.calculator

import com.shalom.calendar.R
import com.shalom.calendar.data.provider.ResourceProvider
import com.shalom.calendar.domain.model.Holiday
import com.shalom.calendar.domain.model.HolidayType
import javax.inject.Inject


class PublicHolidayCalculator @Inject constructor(
    private val resourceProvider: ResourceProvider
) {

    fun getPublicHolidaysForYear(ethiopianYear: Int): List<Holiday> {
        return listOf(
            Holiday(id = "public_new_year_$ethiopianYear", title = resourceProvider.getString(R.string.holiday_public_enkutatash), nameAmharic = "እንቁጣጣሽ", type = HolidayType.NATIONAL_DAY_OFF, ethiopianMonth = 1, ethiopianDay = 1, isDayOff = true, isFixedDate = true, description = resourceProvider.getString(R.string.holiday_public_enkutatash_history), celebration = resourceProvider.getString(R.string.holiday_public_enkutatash_celebration)),

            Holiday(id = "public_adwa_$ethiopianYear", title = resourceProvider.getString(R.string.holiday_public_adwa), nameAmharic = "የዓድዋ ድል", type = HolidayType.NATIONAL_DAY_OFF, ethiopianMonth = 6, ethiopianDay = 23, isDayOff = true, isFixedDate = true, description = resourceProvider.getString(R.string.holiday_public_adwa_history), celebration = resourceProvider.getString(R.string.holiday_public_adwa_celebration)),

            Holiday(id = "public_mayday_$ethiopianYear", title = resourceProvider.getString(R.string.holiday_public_labour_day), nameAmharic = "የሰራተኞች ቀን", type = HolidayType.NATIONAL_DAY_OFF, ethiopianMonth = 8, ethiopianDay = 23, isDayOff = true, isFixedDate = true, description = resourceProvider.getString(R.string.holiday_public_labour_day_history), celebration = resourceProvider.getString(R.string.holiday_public_labour_day_celebration)),

            Holiday(id = "public_patriot_day_$ethiopianYear", title = resourceProvider.getString(R.string.holiday_public_patriots_day), nameAmharic = "የአርበኞች ቀን", type = HolidayType.NATIONAL_DAY_OFF, ethiopianMonth = 8, ethiopianDay = 27, isDayOff = true, isFixedDate = true, description = resourceProvider.getString(R.string.holiday_public_patriots_day_history), celebration = resourceProvider.getString(R.string.holiday_public_patriots_day_celebration)),

            )
    }

}
