package com.shalom.calendar.presentation.month

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shalom.calendar.data.analytics.AnalyticsEvent
import com.shalom.calendar.data.analytics.AnalyticsManager
import com.shalom.calendar.data.preferences.CalendarType
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.data.repository.EventRepository
import com.shalom.calendar.data.repository.HolidayRepository
import com.shalom.calendar.domain.model.HolidayOccurrence
import com.shalom.calendar.domain.model.HolidayType
import com.shalom.calendar.util.compareTo
import com.shalom.calendar.util.lengthOfMonth
import com.shalom.calendar.util.today
import com.shalom.ethiopicchrono.ChronoField
import com.shalom.ethiopicchrono.ChronoUnit
import com.shalom.ethiopicchrono.EthiopicDate
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlin.math.abs

/**
 * ViewModel for MonthCalendar screen.
 * Manages calendar grid generation, date selection, and holiday/event display.
 */
class MonthCalendarViewModel(
    private val holidayRepository: HolidayRepository,
    private val eventRepository: EventRepository,
    private val settingsPreferences: SettingsPreferences,
    private val analyticsManager: AnalyticsManager
) : ViewModel() {

    companion object {
        // Paging range: ±60 months (5 years)
        const val MONTHS_BEFORE = 60
        const val MONTHS_AFTER = 60
        const val TOTAL_PAGES = MONTHS_BEFORE + 1 + MONTHS_AFTER // 121 pages
    }

    // Reference date: current Ethiopian date
    private val referenceDate = EthiopicDate.now() as EthiopicDate
    private val referenceYear = referenceDate.get(ChronoField.YEAR_OF_ERA)
    private val referenceMonth = referenceDate.get(ChronoField.MONTH_OF_YEAR)

    // Calculate initial page (center of range)
    val initialPage: Int by lazy {
        val primary = runBlocking { settingsPreferences.primaryCalendar.first() }
        if (primary == CalendarType.GREGORIAN) {
            getPageForCurrentGregorianMonth()
        } else {
            MONTHS_BEFORE
        }
    }

    // Calendar display preferences
    val primaryCalendar: StateFlow<CalendarType> = settingsPreferences.primaryCalendar.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalendarType.ETHIOPIAN
    )

    val displayDualCalendar: StateFlow<Boolean> = settingsPreferences.displayDualCalendar.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val secondaryCalendar: StateFlow<CalendarType> = settingsPreferences.secondaryCalendar.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalendarType.GREGORIAN
    )

    private val _selectedDate = MutableStateFlow<EthiopicDate?>(null)
    val selectedDate: StateFlow<EthiopicDate?> = _selectedDate.asStateFlow()

    // Dialog state for date details popup
    private val _showDateDetailsDialog = MutableStateFlow(false)
    val showDateDetailsDialog: StateFlow<Boolean> = _showDateDetailsDialog.asStateFlow()

    private val _selectedDateForDialog = MutableStateFlow<EthiopicDate?>(null)
    val selectedDateForDialog: StateFlow<EthiopicDate?> = _selectedDateForDialog.asStateFlow()

    private val _selectedDateHasEvents = MutableStateFlow(false)
    val selectedDateHasEvents: StateFlow<Boolean> = _selectedDateHasEvents.asStateFlow()

    /**
     * Show date details dialog for a specific date
     */
    fun showDateDetailsDialog(date: EthiopicDate, hasEvents: Boolean) {
        _selectedDateForDialog.value = date
        _selectedDateHasEvents.value = hasEvents
        _showDateDetailsDialog.value = true

        // Track date cell click
        analyticsManager.logEvent(
            AnalyticsEvent.DateCellClicked(
                hasEvents = hasEvents,
                hasHolidays = false
            )
        )

        // Track dialog open
        analyticsManager.logEvent(AnalyticsEvent.DateDetailsDialogOpened)
    }

    /**
     * Hide date details dialog
     */
    fun hideDateDetailsDialog() {
        _showDateDetailsDialog.value = false
    }

    /**
     * Track month navigation
     */
    fun trackMonthNavigation(newPage: Int, oldPage: Int) {
        val direction = if (newPage > oldPage) "forward" else "back"
        val offset = abs(newPage - oldPage)

        analyticsManager.logEvent(
            AnalyticsEvent.MonthNavigated(
                direction = direction,
                offset = offset
            )
        )
    }

    /**
     * Track "Today" button click
     */
    fun trackTodayButtonClick() {
        analyticsManager.logEvent(AnalyticsEvent.TodayButtonClicked)
    }

    /**
     * Get Ethiopian date for a specific page index
     */
    fun getEthiopicDateForPage(page: Int): EthiopicDate {
        val monthOffset = page - MONTHS_BEFORE
        var targetYear = referenceYear
        var targetMonth = referenceMonth + monthOffset

        // Handle year wrapping
        while (targetMonth > 13) {
            targetMonth -= 13
            targetYear++
        }
        while (targetMonth < 1) {
            targetMonth += 13
            targetYear--
        }

        return EthiopicDate.of(targetYear, targetMonth, 1)
    }

    /**
     * Get page index for a specific Ethiopian date
     */
    fun getPageForEthiopicDate(date: EthiopicDate): Int {
        val year = date.get(ChronoField.YEAR_OF_ERA)
        val month = date.get(ChronoField.MONTH_OF_YEAR)

        val yearDiff = year - referenceYear
        val monthDiff = month - referenceMonth
        val totalMonthDiff = yearDiff * 13 + monthDiff

        return MONTHS_BEFORE + totalMonthDiff
    }

    /**
     * Find the page that displays the current Gregorian month.
     */
    private fun getPageForCurrentGregorianMonth(): Int {
        val todayDate = today()
        val currentGregorianYear = todayDate.year
        val currentGregorianMonth = todayDate.monthNumber

        // Search Ethiopian months near the current one
        for (offset in -2..2) {
            val testPage = MONTHS_BEFORE + offset
            val ethiopianMonth = getEthiopicDateForPage(testPage)
            val (gregYear, gregMonth) = calculateGregorianMonthForDisplay(ethiopianMonth)

            if (gregYear == currentGregorianYear && gregMonth == currentGregorianMonth) {
                return testPage
            }
        }

        // Fallback to current Ethiopian month if no match found
        return MONTHS_BEFORE
    }

    /**
     * Load month data for a specific page
     */
    fun loadMonthDataForPage(page: Int): Flow<MonthCalendarUiState> {
        return flow {
            try {
                val currentMonth = getEthiopicDateForPage(page)

                // Get all Ethiopian months needed for this view
                val ethiopianMonthsToFetch = getEthiopianMonthsForView(currentMonth)

                // Fetch holidays from all required Ethiopian months
                val holidayFlows = ethiopianMonthsToFetch.map { (etYear, etMonth) ->
                    holidayRepository.getHolidaysForMonth(etYear, etMonth)
                }
                val eventFlows = ethiopianMonthsToFetch.map { (etYear, etMonth) ->
                    eventRepository.getEventsForMonth(etYear, etMonth)
                }

                // Combine preferences with calendar item data and events
                combine(
                    combine(*holidayFlows.toTypedArray()) { it.flatMap { list -> list } },
                    combine(*eventFlows.toTypedArray()) { it.flatMap { list -> list } },
                    primaryCalendar,
                    displayDualCalendar,
                    secondaryCalendar,
                    _selectedDate,
                    settingsPreferences.includeAllDayOffHolidays,
                    settingsPreferences.includeWorkingOrthodoxHolidays,
                    settingsPreferences.includeWorkingMuslimHolidays
                ) { values ->
                    @Suppress("UNCHECKED_CAST")
                    val calendarItems = values[0] as List<HolidayOccurrence>
                    @Suppress("UNCHECKED_CAST")
                    val events = values[1] as List<com.shalom.calendar.data.local.entity.EventInstance>
                    val primary = values[2] as CalendarType
                    val displayDual = values[3] as Boolean
                    val secondary = values[4] as CalendarType
                    val selected = values[5] as EthiopicDate?
                    val showAllDayOff = values[6] as Boolean
                    val showWorkingOrthodox = values[7] as Boolean
                    val showWorkingMuslim = values[8] as Boolean

                    val dateList = generateDateListForMonth(currentMonth, primary)

                    // Calculate Gregorian month/year when Gregorian is primary
                    val (gregorianYear, gregorianMonth) = if (primary == CalendarType.GREGORIAN) {
                        calculateGregorianMonthForDisplay(currentMonth)
                    } else {
                        Pair(null, null)
                    }

                    // Apply holiday filters based on settings
                    val settingsFilteredItems = applyHolidayFilters(
                        calendarItems,
                        showAllDayOff,
                        showWorkingOrthodox,
                        showWorkingMuslim
                    )

                    // Filter holidays by the actual month range
                    val filteredCalendarItems = filterHolidaysByMonthRange(
                        settingsFilteredItems,
                        currentMonth,
                        primary,
                        gregorianYear,
                        gregorianMonth
                    )

                    MonthCalendarUiState.Success(
                        currentMonth = currentMonth,
                        dateList = dateList,
                        calendarItems = filteredCalendarItems,
                        events = events,
                        selectedDate = selected,
                        primaryCalendar = primary,
                        displayDualCalendar = displayDual,
                        secondaryCalendar = secondary,
                        currentGregorianYear = gregorianYear,
                        currentGregorianMonth = gregorianMonth
                    )
                }.collect { state ->
                    emit(state)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val errorState = MonthCalendarUiState.Error(e.message ?: "Unknown error")
                emit(errorState)
            }
        }
    }

    /**
     * Get all Ethiopian year/month pairs needed to fetch holidays for this view.
     */
    private fun getEthiopianMonthsForView(ethiopianMonth: EthiopicDate): List<Pair<Int, Int>> {
        val ethiopianGrid = generateEthiopianMonthGrid(ethiopianMonth)
        val gregorianGrid = generateGregorianMonthGrid(ethiopianMonth)

        // Combine both grids to get all possible dates we might show
        val allDates = (ethiopianGrid + gregorianGrid).distinct()

        // Extract all unique Ethiopian year/month combinations from these dates
        val ethiopianMonths = allDates.map { date ->
            Pair(date.get(ChronoField.YEAR_OF_ERA), date.get(ChronoField.MONTH_OF_YEAR))
        }.distinct()

        return ethiopianMonths
    }

    /**
     * Apply holiday filters based on user settings.
     */
    private fun applyHolidayFilters(
        holidays: List<HolidayOccurrence>,
        showAllDayOff: Boolean,
        showWorkingOrthodox: Boolean,
        showWorkingMuslim: Boolean
    ): List<HolidayOccurrence> {
        val selectedFilters = mutableSetOf<HolidayType>()

        if (showAllDayOff) {
            selectedFilters.add(HolidayType.NATIONAL_DAY_OFF)
            selectedFilters.add(HolidayType.ORTHODOX_DAY_OFF)
            selectedFilters.add(HolidayType.MUSLIM_DAY_OFF)
        }
        if (showWorkingOrthodox) {
            selectedFilters.add(HolidayType.ORTHODOX_WORKING)
            selectedFilters.add(HolidayType.ORTHODOX_DAY_OFF)
        }
        if (showWorkingMuslim) {
            selectedFilters.add(HolidayType.MUSLIM_WORKING)
            selectedFilters.add(HolidayType.MUSLIM_DAY_OFF)
        }

        return holidays.filter { selectedFilters.contains(it.holiday.type) }
    }

    /**
     * Filter holidays to only include those that fall within the actual month range.
     */
    private fun filterHolidaysByMonthRange(
        holidays: List<HolidayOccurrence>,
        ethiopianMonth: EthiopicDate,
        primaryCalendar: CalendarType,
        gregorianYear: Int?,
        gregorianMonth: Int?
    ): List<HolidayOccurrence> {
        return if (primaryCalendar == CalendarType.GREGORIAN && gregorianYear != null && gregorianMonth != null) {
            val firstDayOfMonth = LocalDate(gregorianYear, gregorianMonth, 1)
            val lastDayOfMonth = LocalDate(gregorianYear, gregorianMonth, firstDayOfMonth.lengthOfMonth())

            // Convert to Ethiopian dates for comparison
            val firstEthiopianDate = EthiopicDate.from(firstDayOfMonth)
            val lastEthiopianDate = EthiopicDate.from(lastDayOfMonth)

            holidays.filter { holiday ->
                val holidayDate = holiday.actualEthiopicDate
                holidayDate >= firstEthiopianDate && holidayDate <= lastEthiopianDate
            }
        } else {
            val year = ethiopianMonth.get(ChronoField.YEAR_OF_ERA)
            val month = ethiopianMonth.get(ChronoField.MONTH_OF_YEAR)

            holidays.filter { holiday ->
                holiday.actualEthiopicDate.get(ChronoField.YEAR_OF_ERA) == year &&
                        holiday.actualEthiopicDate.get(ChronoField.MONTH_OF_YEAR) == month
            }
        }
    }

    /**
     * Generate 42 date cells for calendar grid (6 weeks × 7 days)
     */
    private fun generateDateListForMonth(month: EthiopicDate, primaryCalendar: CalendarType): List<EthiopicDate> {
        return when (primaryCalendar) {
            CalendarType.ETHIOPIAN -> generateEthiopianMonthGrid(month)
            CalendarType.GREGORIAN -> generateGregorianMonthGrid(month)
            CalendarType.HIRJI -> generateEthiopianMonthGrid(month) // Fallback to Ethiopian for now
        }
    }

    /**
     * Generate calendar grid based on Ethiopian month
     */
    private fun generateEthiopianMonthGrid(month: EthiopicDate): List<EthiopicDate> {
        val year = month.get(ChronoField.YEAR_OF_ERA)
        val monthValue = month.get(ChronoField.MONTH_OF_YEAR)

        val firstDayOfMonth = EthiopicDate.of(year, monthValue, 1)
        val daysInMonth = getDaysInMonth(year, monthValue)

        val dateList = mutableListOf<EthiopicDate>()

        // Add days from previous month to fill first week
        val gregorianFirstDay = firstDayOfMonth.toLocalDate()
        val firstDayWeekday = gregorianFirstDay.dayOfWeek.ordinal + 1
        val dayOffset = (firstDayWeekday - 1) % 7

        if (dayOffset > 0) {
            var prevDate = firstDayOfMonth.plus(-1, ChronoUnit.DAYS) as EthiopicDate
            val prevDatesToAdd = mutableListOf<EthiopicDate>()

            for (i in 0 until dayOffset) {
                prevDatesToAdd.add(prevDate)
                if (i < dayOffset - 1) {
                    prevDate = prevDate.plus(-1, ChronoUnit.DAYS) as EthiopicDate
                }
            }

            dateList.addAll(prevDatesToAdd.reversed())
        }

        // Add days of current month
        for (day in 1..daysInMonth) {
            dateList.add(EthiopicDate.of(year, monthValue, day))
        }

        // Add days from next month only if needed to complete the last week
        val currentSize = dateList.size
        val weeksNeeded = (currentSize + 6) / 7
        val targetCells = weeksNeeded * 7
        val remainingCells = targetCells - currentSize

        if (remainingCells > 0) {
            var nextDate = EthiopicDate.of(year, monthValue, daysInMonth).plus(1, ChronoUnit.DAYS) as EthiopicDate

            for (i in 0 until remainingCells) {
                dateList.add(nextDate)
                if (i < remainingCells - 1) {
                    nextDate = nextDate.plus(1, ChronoUnit.DAYS) as EthiopicDate
                }
            }
        }

        return dateList
    }

    /**
     * Generate calendar grid based on Gregorian month
     */
    private fun generateGregorianMonthGrid(ethiopianMonth: EthiopicDate): List<EthiopicDate> {
        val ethiopianYear = ethiopianMonth.get(ChronoField.YEAR_OF_ERA)
        val ethiopianMonthValue = ethiopianMonth.get(ChronoField.MONTH_OF_YEAR)
        val daysInEthiopianMonth = getDaysInMonth(ethiopianYear, ethiopianMonthValue)

        // Use the last day of the Ethiopian month
        val referenceDay = daysInEthiopianMonth
        val referenceEthiopianDate = EthiopicDate.of(ethiopianYear, ethiopianMonthValue, referenceDay)

        // Convert to Gregorian to find the Gregorian month to display
        val gregorianDate = referenceEthiopianDate.toLocalDate()
        val year = gregorianDate.year
        val month = gregorianDate.monthNumber

        // Get first day of the Gregorian month
        val firstDayOfMonthGreg = LocalDate(year, month, 1)
        val daysInMonth = firstDayOfMonthGreg.lengthOfMonth()

        val dateList = mutableListOf<EthiopicDate>()

        // Add days from previous month to fill first week
        val firstDayWeekday = firstDayOfMonthGreg.dayOfWeek.ordinal + 1
        val dayOffset = (firstDayWeekday - 1) % 7

        if (dayOffset > 0) {
            var prevEpochDay = firstDayOfMonthGreg.toEpochDays() - 1
            val prevDatesToAdd = mutableListOf<LocalDate>()

            for (i in 0 until dayOffset) {
                prevDatesToAdd.add(LocalDate.fromEpochDays(prevEpochDay))
                if (i < dayOffset - 1) {
                    prevEpochDay -= 1
                }
            }

            dateList.addAll(prevDatesToAdd.reversed().map { EthiopicDate.from(it) })
        }

        // Add days of current Gregorian month
        for (day in 1..daysInMonth) {
            val gregorianDay = LocalDate(year, month, day)
            dateList.add(EthiopicDate.from(gregorianDay))
        }

        // Add days from next month only if needed to complete the last week
        val currentSize = dateList.size
        val weeksNeeded = (currentSize + 6) / 7
        val targetCells = weeksNeeded * 7
        val remainingCells = targetCells - currentSize

        if (remainingCells > 0) {
            var nextEpochDay = LocalDate(year, month, daysInMonth).toEpochDays() + 1

            for (i in 0 until remainingCells) {
                dateList.add(EthiopicDate.from(LocalDate.fromEpochDays(nextEpochDay)))
                if (i < remainingCells - 1) {
                    nextEpochDay += 1
                }
            }
        }

        return dateList
    }

    private fun getDaysInMonth(year: Int, month: Int): Int {
        return when (month) {
            13 -> if (year % 4 == 3) 6 else 5  // Pagume
            else -> 30
        }
    }

    /**
     * Calculate which Gregorian month to display for a given Ethiopian month
     */
    private fun calculateGregorianMonthForDisplay(ethiopianMonth: EthiopicDate): Pair<Int, Int> {
        val ethiopianYear = ethiopianMonth.get(ChronoField.YEAR_OF_ERA)
        val ethiopianMonthValue = ethiopianMonth.get(ChronoField.MONTH_OF_YEAR)
        val daysInEthiopianMonth = getDaysInMonth(ethiopianYear, ethiopianMonthValue)

        // Use the last day of the Ethiopian month
        val referenceDay = daysInEthiopianMonth
        val referenceEthiopianDate = EthiopicDate.of(ethiopianYear, ethiopianMonthValue, referenceDay)

        // Convert to Gregorian to find the Gregorian month to display
        val gregorianDate = referenceEthiopianDate.toLocalDate()
        val year = gregorianDate.year
        val month = gregorianDate.monthNumber

        return Pair(year, month)
    }

    // User actions
    fun selectDate(date: EthiopicDate) {
        _selectedDate.value = date
    }

    fun getTodayPage(): Int {
        return if (primaryCalendar.value == CalendarType.GREGORIAN) {
            getPageForCurrentGregorianMonth()
        } else {
            val today = EthiopicDate.now() as EthiopicDate
            getPageForEthiopicDate(today)
        }
    }
}
