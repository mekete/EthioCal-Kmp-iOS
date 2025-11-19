package com.shalom.calendar.presentation.holidaylist

import com.shalom.calendar.domain.model.HolidayOccurrence
import com.shalom.calendar.domain.model.HolidayType

/**
 * UI state for the Holiday List screen
 */
sealed class CalendarItemListUiState {
    data object Loading : CalendarItemListUiState()

    data class Success(
        val currentYear: Int,
        val allCalendarItems: List<HolidayOccurrence>,
        val filteredCalendarItems: List<HolidayOccurrence>,
        val selectedFilters: Set<HolidayType>
    ) : CalendarItemListUiState()

    data class Error(val message: String) : CalendarItemListUiState()
}
