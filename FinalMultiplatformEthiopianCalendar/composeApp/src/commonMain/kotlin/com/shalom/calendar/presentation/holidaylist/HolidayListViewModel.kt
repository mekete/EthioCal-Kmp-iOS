package com.shalom.calendar.presentation.holidaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.data.repository.HolidayRepository
import com.shalom.calendar.domain.model.Holiday
import com.shalom.calendar.domain.model.HolidayOccurrence
import com.shalom.calendar.domain.model.HolidayType
import com.shalom.ethiopicchrono.ChronoField
import com.shalom.ethiopicchrono.EthiopicDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * ViewModel for Holiday List screen.
 * KMP-compatible version using Koin for DI.
 */
class CalendarItemListViewModel(
    private val holidayRepository: HolidayRepository,
    private val settingsPreferences: SettingsPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<CalendarItemListUiState>(CalendarItemListUiState.Loading)
    val uiState: StateFlow<CalendarItemListUiState> = _uiState.asStateFlow()

    private var currentYear: Int = EthiopicDate.now().get(ChronoField.YEAR)
    private var allCalendarItems: List<HolidayOccurrence> = emptyList()

    val hideHolidayInfoDialog = settingsPreferences.hideHolidayInfoDialog

    init {
        loadHolidaysForYear()
        observeSettingsChanges()
    }

    private fun observeSettingsChanges() {
        viewModelScope.launch {
            combine(
                settingsPreferences.includeAllDayOffHolidays,
                settingsPreferences.includeWorkingOrthodoxHolidays,
                settingsPreferences.includeWorkingMuslimHolidays
            ) { showAllDayOff, showWorkingOrthodox, showWorkingMuslim ->
                Triple(showAllDayOff, showWorkingOrthodox, showWorkingMuslim)
            }.collect { (showAllDayOff, showWorkingOrthodox, showWorkingMuslim) ->
                applyFilters(showAllDayOff, showWorkingOrthodox, showWorkingMuslim)
            }
        }
    }

    fun incrementYear() {
        currentYear++
        loadHolidaysForYear()
    }

    fun decrementYear() {
        currentYear--
        loadHolidaysForYear()
    }

    fun setHideHolidayInfoDialog(hide: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setHideHolidayInfoDialog(hide)
        }
    }

    private fun loadHolidaysForYear() {
        viewModelScope.launch {
            _uiState.value = CalendarItemListUiState.Loading
            holidayRepository.getHolidaysForYear(ethiopianYear = currentYear)
                .catch { e ->
                    _uiState.value = CalendarItemListUiState.Error(
                        message = e.message ?: "Failed to load calendar items"
                    )
                }
                .collect { holidays ->
                    // Convert Holiday to HolidayOccurrence
                    allCalendarItems = holidays.map { holiday ->
                        HolidayOccurrence(
                            holiday = holiday,
                            ethiopicDate = EthiopicDate.of(
                                holiday.ethiopianYear,
                                holiday.ethiopianMonth,
                                holiday.ethiopianDay
                            )
                        )
                    }.sortedWith(compareBy { it.actualEthiopicDate })

                    // Get current settings values to apply initial filter
                    val showAllDayOffHolidays = settingsPreferences.includeAllDayOffHolidays
                    val showWorkingOrthodoxHolidays = settingsPreferences.includeWorkingOrthodoxHolidays
                    val showWorkingMuslimHolidays = settingsPreferences.includeWorkingMuslimHolidays

                    combine(
                        showAllDayOffHolidays,
                        showWorkingOrthodoxHolidays,
                        showWorkingMuslimHolidays
                    ) { publicAndDayOff, orthodoxWorking, muslimWorking ->
                        Triple(publicAndDayOff, orthodoxWorking, muslimWorking)
                    }.collect { (public, orthodox, muslim) ->
                        applyFilters(public, orthodox, muslim)
                    }
                }
        }
    }

    private fun applyFilters(
        showAllDayOffPublic: Boolean,
        showWorkingOrthodoxHolidays: Boolean,
        showWorkingMuslimHolidays: Boolean
    ) {
        val selectedFilters = mutableSetOf<HolidayType>()

        if (showAllDayOffPublic) {
            selectedFilters.add(HolidayType.NATIONAL_DAY_OFF)
            selectedFilters.add(HolidayType.ORTHODOX_DAY_OFF)
            selectedFilters.add(HolidayType.MUSLIM_DAY_OFF)
        }

        if (showWorkingOrthodoxHolidays) {
            selectedFilters.add(HolidayType.ORTHODOX_WORKING)
            // If show-all-day-off holidays is false, include orthodox day-off ones here
            // Safe to add again as we're using a set
            selectedFilters.add(HolidayType.ORTHODOX_DAY_OFF)
        }

        if (showWorkingMuslimHolidays) {
            selectedFilters.add(HolidayType.MUSLIM_WORKING)
            // If show-all-day-off holidays is false, include muslim day-off ones here
            // Safe to add again as we're using a set
            selectedFilters.add(HolidayType.MUSLIM_DAY_OFF)
        }

        val filteredItems = allCalendarItems.filter {
            selectedFilters.contains(it.holiday.type)
        }

        _uiState.value = CalendarItemListUiState.Success(
            currentYear = currentYear,
            allCalendarItems = allCalendarItems,
            filteredCalendarItems = filteredItems,
            selectedFilters = selectedFilters
        )
    }
}
