package com.shalom.calendar.ui.holidaylist

import com.shalom.calendar.domain.model.HolidayOccurrence
import com.shalom.calendar.domain.model.HolidayType

sealed class CalendarItemListUiState {
    data object Loading : CalendarItemListUiState()

    data class Success(
        val currentYear: Int, val allCalendarItems: List<HolidayOccurrence>, val filteredCalendarItems: List<HolidayOccurrence>, val selectedFilters: Set<HolidayType>
    ) : CalendarItemListUiState()

    data class Error(val message: String) : CalendarItemListUiState()
}
