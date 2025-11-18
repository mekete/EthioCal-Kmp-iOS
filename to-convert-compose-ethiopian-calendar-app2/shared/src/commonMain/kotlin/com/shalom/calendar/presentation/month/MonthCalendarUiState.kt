package com.shalom.calendar.presentation.month

import com.shalom.calendar.data.local.entity.EventInstance
import com.shalom.calendar.data.preferences.CalendarType
import com.shalom.calendar.domain.model.HolidayOccurrence
import com.shalom.ethiopicchrono.EthiopicDate

/**
 * UI state for month calendar screen
 */
sealed class MonthCalendarUiState {
    object Loading : MonthCalendarUiState()

    data class Success(
        val currentMonth: EthiopicDate,
        val dateList: List<EthiopicDate>,
        val calendarItems: List<HolidayOccurrence>,
        val events: List<EventInstance>,
        val selectedDate: EthiopicDate?,
        val primaryCalendar: CalendarType,
        val displayDualCalendar: Boolean,
        val secondaryCalendar: CalendarType,
        val currentGregorianYear: Int?,
        val currentGregorianMonth: Int?
    ) : MonthCalendarUiState()

    data class Error(val message: String) : MonthCalendarUiState()
}
