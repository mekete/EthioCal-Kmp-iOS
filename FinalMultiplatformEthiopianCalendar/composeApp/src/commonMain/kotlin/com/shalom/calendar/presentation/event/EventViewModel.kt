package com.shalom.calendar.presentation.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shalom.calendar.alarm.AlarmScheduler
import com.shalom.calendar.data.analytics.AnalyticsEvent
import com.shalom.calendar.data.analytics.AnalyticsManager
import com.shalom.calendar.data.local.entity.EventEntity
import com.shalom.calendar.data.local.entity.RecurrenceRule
import com.shalom.calendar.data.local.entity.toRRuleString
import com.shalom.calendar.data.preferences.CalendarType
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.data.repository.EventRepository
import com.shalom.calendar.util.randomUUID
import com.shalom.calendar.util.today
import com.shalom.calendar.util.firstDayOfMonth
import com.shalom.calendar.util.lastDayOfMonth
import com.shalom.calendar.util.lengthOfMonth
import com.shalom.calendar.util.withDayOfMonth
import com.shalom.ethiopicchrono.ChronoField
import com.shalom.ethiopicchrono.ChronoUnit
import com.shalom.ethiopicchrono.EthiopicDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

/**
 * ViewModel for EventScreen.
 * Manages event CRUD operations, filtering, and alarm scheduling.
 */
class EventViewModel(
    private val eventRepository: EventRepository,
    private val settingsPreferences: SettingsPreferences,
    private val analyticsManager: AnalyticsManager,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow<EventUiState>(EventUiState.Loading)
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    // Store all events to apply filtering
    private var allEvents: List<com.shalom.calendar.data.local.entity.EventInstance> = emptyList()

    // Track if we've initialized filters with defaults
    private var hasInitializedFilters = false

    init {
        loadEvents()
    }

    /**
     * Load all events from repository.
     */
    private fun loadEvents() {
        viewModelScope.launch {
            eventRepository.getAllEvents()
                .catch { exception ->
                    _uiState.value = EventUiState.Error(
                        message = exception.message ?: "Failed to load events"
                    )
                }
                .collect { events ->
                    // Convert EventEntity to EventInstance
                    val instances = events.map { event ->
                        event.toEventInstance()
                    }
                    allEvents = instances

                    val currentState = _uiState.value as? EventUiState.Success

                    // Determine filter dates
                    val startDate: LocalDate?
                    val endDate: LocalDate?

                    if (!hasInitializedFilters) {
                        // First load: Initialize with default dates based on calendar preference
                        val defaultDates = getDefaultFilterDates()
                        startDate = defaultDates.first
                        endDate = defaultDates.second
                        hasInitializedFilters = true
                    } else {
                        // Subsequent loads: Preserve existing filter state
                        startDate = currentState?.filterStartDate
                        endDate = currentState?.filterEndDate
                    }

                    val filteredEvents = applyDateFilter(instances, startDate, endDate)

                    _uiState.value = EventUiState.Success(
                        events = filteredEvents,
                        isDialogOpen = currentState?.isDialogOpen ?: false,
                        editingEvent = currentState?.editingEvent,
                        filterStartDate = startDate,
                        filterEndDate = endDate
                    )
                }
        }
    }

    /**
     * Get default filter dates based on the primary calendar preference.
     */
    private suspend fun getDefaultFilterDates(): Pair<LocalDate, LocalDate> {
        return try {
            val calendarType = settingsPreferences.primaryCalendar.first()

            when (calendarType) {
                CalendarType.ETHIOPIAN -> {
                    val todayEthiopic = EthiopicDate.now()
                    val year = todayEthiopic.get(ChronoField.YEAR)
                    val month = todayEthiopic.get(ChronoField.MONTH_OF_YEAR)
                    val firstDay = EthiopicDate.of(year, month, 1)
                    val lastDay = EthiopicDate.of(year, month, firstDay.lengthOfMonth())
                    val startDate = firstDay.toLocalDate()
                    val endDate = lastDay.toLocalDate()
                    Pair(startDate, endDate)
                }

                CalendarType.GREGORIAN -> {
                    val todayDate = today()
                    val startDate = todayDate.firstDayOfMonth()
                    val endDate = todayDate.lastDayOfMonth()
                    Pair(startDate, endDate)
                }

                else -> {
                    // HIJRI or other - use Gregorian as fallback
                    val todayDate = today()
                    val startDate = todayDate.firstDayOfMonth()
                    val endDate = todayDate.lastDayOfMonth()
                    Pair(startDate, endDate)
                }
            }
        } catch (e: Exception) {
            val todayDate = today()
            Pair(todayDate.firstDayOfMonth(), todayDate.lastDayOfMonth())
        }
    }

    /**
     * Show the add event dialog.
     */
    fun showAddEventDialog() {
        val currentState = _uiState.value
        if (currentState is EventUiState.Success) {
            _uiState.value = currentState.copy(isDialogOpen = true, presetDate = null)
        }
    }

    /**
     * Show the add event dialog with a preset date.
     */
    fun showAddEventDialogWithDate(date: EthiopicDate) {
        val currentState = _uiState.value
        if (currentState is EventUiState.Success) {
            _uiState.value = currentState.copy(
                isDialogOpen = true,
                presetDate = date,
                editingEvent = null
            )
        }
    }

    /**
     * Hide the add event dialog.
     */
    fun hideAddEventDialog() {
        val currentState = _uiState.value
        if (currentState is EventUiState.Success) {
            _uiState.value = currentState.copy(
                isDialogOpen = false,
                editingEvent = null,
                presetDate = null
            )
        }
    }

    /**
     * Show the edit event dialog for a specific event.
     */
    fun showEditEventDialog(eventId: String) {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value
                if (currentState is EventUiState.Success) {
                    val eventToEdit = currentState.events.find { it.eventId == eventId }?.originalEvent
                    if (eventToEdit != null) {
                        _uiState.value = currentState.copy(
                            isDialogOpen = true,
                            editingEvent = eventToEdit,
                            presetDate = null
                        )
                    }
                }
            } catch (e: Exception) {
                // Handle error silently
            }
        }
    }

    /**
     * Create a new event.
     */
    fun createEvent(
        summary: String,
        description: String? = null,
        startTime: Instant,
        endTime: Instant? = null,
        isAllDay: Boolean = false,
        recurrenceRule: RecurrenceRule? = null,
        reminderMinutesBefore: Int? = null,
        category: String = "PERSONAL",
        timeZoneId: String = "Africa/Addis_Ababa",
        ethiopianYear: Int,
        ethiopianMonth: Int,
        ethiopianDay: Int
    ) {
        viewModelScope.launch {
            try {
                val now = Clock.System.now().toEpochMilliseconds()
                val event = EventEntity(
                    id = randomUUID(),
                    summary = summary.trim(),
                    description = description?.trim(),
                    startTime = startTime,
                    endTime = endTime,
                    isAllDay = isAllDay,
                    timeZone = timeZoneId,
                    recurrenceRule = recurrenceRule?.toRRuleString(),
                    recurrenceEndDate = recurrenceRule?.endDate?.let {
                        Instant.fromEpochMilliseconds(it)
                    },
                    reminderMinutesBefore = reminderMinutesBefore,
                    category = category,
                    ethiopianYear = ethiopianYear,
                    ethiopianMonth = ethiopianMonth,
                    ethiopianDay = ethiopianDay,
                    createdAt = now,
                    updatedAt = now
                )

                // Create event in database
                eventRepository.createEvent(event)

                // Schedule alarm if reminder is enabled
                if (reminderMinutesBefore != null) {
                    alarmScheduler.scheduleAlarm(event)
                }

                // Track event creation
                analyticsManager.logEvent(
                    AnalyticsEvent.EventCreated(
                        isAllDay = isAllDay,
                        hasReminder = reminderMinutesBefore != null,
                        reminderMinutes = reminderMinutesBefore ?: 0,
                        isRecurring = recurrenceRule != null,
                        recurrenceType = recurrenceRule?.frequency?.name,
                        hasDescription = !description.isNullOrBlank()
                    )
                )

                hideAddEventDialog()
            } catch (e: Exception) {
                _uiState.value = EventUiState.Error(
                    message = e.message ?: "Failed to create event"
                )
            }
        }
    }

    /**
     * Delete an event by ID.
     */
    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            try {
                // Get event info before deletion for analytics
                val event = (_uiState.value as? EventUiState.Success)
                    ?.events
                    ?.find { it.eventId == eventId }
                    ?.originalEvent

                // Cancel alarm if it exists
                alarmScheduler.cancelAlarm(eventId)

                // Delete event from database
                eventRepository.deleteEventById(eventId)

                // Track deletion
                analyticsManager.logEvent(
                    AnalyticsEvent.EventDeleted(
                        eventId = eventId,
                        wasRecurring = event?.recurrenceRule != null
                    )
                )
            } catch (e: Exception) {
                _uiState.value = EventUiState.Error(
                    message = e.message ?: "Failed to delete event"
                )
            }
        }
    }

    /**
     * Update an existing event.
     */
    fun updateEvent(
        eventId: String,
        summary: String,
        description: String? = null,
        startTime: Instant,
        endTime: Instant? = null,
        isAllDay: Boolean = false,
        recurrenceRule: RecurrenceRule? = null,
        reminderMinutesBefore: Int? = null,
        category: String = "PERSONAL",
        timeZoneId: String = "Africa/Addis_Ababa",
        ethiopianYear: Int,
        ethiopianMonth: Int,
        ethiopianDay: Int
    ) {
        viewModelScope.launch {
            try {
                // Get the existing event to preserve createdAt timestamp
                val existingEvent = (_uiState.value as? EventUiState.Success)
                    ?.events
                    ?.find { it.eventId == eventId }
                    ?.originalEvent

                if (existingEvent == null) {
                    _uiState.value = EventUiState.Error("Event not found")
                    return@launch
                }

                // Track what changed for analytics
                val changedFields = mutableListOf<String>()
                if (existingEvent.summary != summary.trim()) changedFields.add("summary")
                if (existingEvent.description != description?.trim()) changedFields.add("description")
                if (existingEvent.startTime != startTime) changedFields.add("startTime")
                if (existingEvent.isAllDay != isAllDay) changedFields.add("isAllDay")
                if (existingEvent.recurrenceRule != recurrenceRule?.toRRuleString()) changedFields.add("recurrence")
                if (existingEvent.reminderMinutesBefore != reminderMinutesBefore) changedFields.add("reminder")
                if (existingEvent.category != category) changedFields.add("category")

                val updatedEvent = EventEntity(
                    id = eventId,
                    summary = summary.trim(),
                    description = description?.trim(),
                    startTime = startTime,
                    endTime = endTime,
                    isAllDay = isAllDay,
                    timeZone = timeZoneId,
                    recurrenceRule = recurrenceRule?.toRRuleString(),
                    recurrenceEndDate = recurrenceRule?.endDate?.let {
                        Instant.fromEpochMilliseconds(it)
                    },
                    reminderMinutesBefore = reminderMinutesBefore,
                    category = category,
                    ethiopianYear = ethiopianYear,
                    ethiopianMonth = ethiopianMonth,
                    ethiopianDay = ethiopianDay,
                    createdAt = existingEvent.createdAt,
                    updatedAt = Clock.System.now().toEpochMilliseconds(),
                    googleCalendarEventId = existingEvent.googleCalendarEventId,
                    isSynced = false
                )

                // Cancel old alarm first
                alarmScheduler.cancelAlarm(eventId)

                // Update event in database
                eventRepository.updateEvent(updatedEvent)

                // Schedule new alarm if reminder is enabled
                if (reminderMinutesBefore != null) {
                    alarmScheduler.scheduleAlarm(updatedEvent)
                }

                // Track event update
                if (changedFields.isNotEmpty()) {
                    analyticsManager.logEvent(
                        AnalyticsEvent.EventEdited(
                            eventId = eventId,
                            changedFields = changedFields
                        )
                    )
                }

                hideAddEventDialog()
            } catch (e: Exception) {
                _uiState.value = EventUiState.Error(
                    message = e.message ?: "Failed to update event"
                )
            }
        }
    }

    /**
     * Set the start date filter.
     */
    fun setFilterStartDate(startDate: LocalDate?) {
        val currentState = _uiState.value as? EventUiState.Success ?: return
        val filteredEvents = applyDateFilter(allEvents, startDate, currentState.filterEndDate)
        _uiState.value = currentState.copy(
            events = filteredEvents,
            filterStartDate = startDate
        )
    }

    /**
     * Set the end date filter.
     */
    fun setFilterEndDate(endDate: LocalDate?) {
        val currentState = _uiState.value as? EventUiState.Success ?: return
        val filteredEvents = applyDateFilter(allEvents, currentState.filterStartDate, endDate)
        _uiState.value = currentState.copy(
            events = filteredEvents,
            filterEndDate = endDate
        )
    }

    /**
     * Clear the start date filter.
     */
    fun clearFilterStartDate() {
        val currentState = _uiState.value as? EventUiState.Success ?: return
        val filteredEvents = applyDateFilter(allEvents, null, currentState.filterEndDate)
        _uiState.value = currentState.copy(
            events = filteredEvents,
            filterStartDate = null
        )
    }

    /**
     * Clear the end date filter.
     */
    fun clearFilterEndDate() {
        val currentState = _uiState.value as? EventUiState.Success ?: return
        val filteredEvents = applyDateFilter(allEvents, currentState.filterStartDate, null)
        _uiState.value = currentState.copy(
            events = filteredEvents,
            filterEndDate = null
        )
    }

    /**
     * Clear all date filters by setting a wide date range (1900-2100).
     */
    fun clearDateFilters() {
        val showAllStartDate = LocalDate(1900, 1, 1)
        val showAllEndDate = LocalDate(2100, 12, 31)

        val currentState = _uiState.value as? EventUiState.Success ?: return
        val filteredEvents = applyDateFilter(allEvents, showAllStartDate, showAllEndDate)
        _uiState.value = currentState.copy(
            events = filteredEvents,
            filterStartDate = showAllStartDate,
            filterEndDate = showAllEndDate
        )
    }

    /**
     * Set filter to current month.
     */
    fun setFilterToCurrentMonth() {
        viewModelScope.launch {
            val currentState = _uiState.value as? EventUiState.Success ?: return@launch

            val defaultDates = getDefaultFilterDates()
            val startDate = defaultDates.first
            val endDate = defaultDates.second

            val filteredEvents = applyDateFilter(allEvents, startDate, endDate)
            _uiState.value = currentState.copy(
                events = filteredEvents,
                filterStartDate = startDate,
                filterEndDate = endDate
            )
        }
    }

    /**
     * Set filter to a specific Ethiopian month.
     */
    fun setFilterToMonth(date: EthiopicDate) {
        viewModelScope.launch {
            val currentState = _uiState.value as? EventUiState.Success ?: return@launch

            val year = date.get(ChronoField.YEAR)
            val month = date.get(ChronoField.MONTH_OF_YEAR)
            val firstDay = EthiopicDate.of(year, month, 1)
            val lastDay = EthiopicDate.of(year, month, firstDay.lengthOfMonth())

            val startDate = firstDay.toLocalDate()
            val endDate = lastDay.toLocalDate()

            val filteredEvents = applyDateFilter(allEvents, startDate, endDate)
            _uiState.value = currentState.copy(
                events = filteredEvents,
                filterStartDate = startDate,
                filterEndDate = endDate
            )
        }
    }

    /**
     * Set filter to a specific month and open add event dialog with preset date.
     */
    fun setFilterAndOpenDialog(date: EthiopicDate) {
        viewModelScope.launch {
            val currentState = _uiState.value as? EventUiState.Success ?: return@launch

            val year = date.get(ChronoField.YEAR)
            val month = date.get(ChronoField.MONTH_OF_YEAR)
            val firstDay = EthiopicDate.of(year, month, 1)
            val lastDay = EthiopicDate.of(year, month, firstDay.lengthOfMonth())

            val startDate = firstDay.toLocalDate()
            val endDate = lastDay.toLocalDate()

            val filteredEvents = applyDateFilter(allEvents, startDate, endDate)
            _uiState.value = currentState.copy(
                events = filteredEvents,
                filterStartDate = startDate,
                filterEndDate = endDate,
                isDialogOpen = true,
                presetDate = date,
                editingEvent = null
            )
        }
    }

    /**
     * Apply date filtering to the event list.
     */
    private fun applyDateFilter(
        events: List<com.shalom.calendar.data.local.entity.EventInstance>,
        startDate: LocalDate?,
        endDate: LocalDate?
    ): List<com.shalom.calendar.data.local.entity.EventInstance> {
        if (startDate == null && endDate == null) {
            return events
        }

        val tz = TimeZone.currentSystemDefault()

        return events.filter { event ->
            val eventStartTime = event.instanceStart

            // Check start date filter
            val passesStartFilter = if (startDate != null) {
                val filterStartInstant = startDate.atStartOfDayIn(tz)
                eventStartTime >= filterStartInstant
            } else {
                true
            }

            // Check end date filter
            val passesEndFilter = if (endDate != null) {
                // End of day = start of next day
                val nextDay = LocalDate.fromEpochDays(endDate.toEpochDays() + 1)
                val filterEndInstant = nextDay.atStartOfDayIn(tz)
                eventStartTime < filterEndInstant
            } else {
                true
            }

            passesStartFilter && passesEndFilter
        }
    }

    /**
     * Helper function to convert EventEntity to EventInstance.
     */
    private fun EventEntity.toEventInstance() = com.shalom.calendar.data.local.entity.EventInstance(
        eventId = id,
        summary = summary,
        description = description,
        instanceStart = startTime,
        instanceEnd = endTime,
        isAllDay = isAllDay,
        category = category,
        reminderMinutesBefore = reminderMinutesBefore,
        ethiopianYear = ethiopianYear,
        ethiopianMonth = ethiopianMonth,
        ethiopianDay = ethiopianDay,
        isRecurring = recurrenceRule != null,
        originalEvent = this
    )
}
