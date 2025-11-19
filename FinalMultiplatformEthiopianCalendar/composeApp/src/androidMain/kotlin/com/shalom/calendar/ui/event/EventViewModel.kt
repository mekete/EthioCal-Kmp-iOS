package com.shalom.calendar.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shalom.calendar.alarm.AlarmScheduler
import com.shalom.calendar.data.analytics.AnalyticsEvent
import com.shalom.calendar.data.analytics.AnalyticsManager
import com.shalom.calendar.data.analytics.AnalyticsParams
import com.shalom.calendar.data.local.entity.EventEntity
import com.shalom.calendar.data.local.entity.RecurrenceRule
import com.shalom.calendar.data.local.entity.toRRuleString
import com.shalom.calendar.data.preferences.CalendarType
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.shalom.ethiopicchrono.EthiopicDate
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters
import java.util.UUID


/**
 * ViewModel for EventScreen.
 *
 * Follows the same pattern as HolidayListViewModel:
 * - Uses Koin for dependency injection
 * - Exposes StateFlow for UI state
 * - Handles business logic and repository operations
 * - Uses viewModelScope for coroutines
 * - Integrates alarm scheduling for event reminders
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
     * Uses Flow to automatically update UI when data changes.
     */
    private fun loadEvents() {
        viewModelScope.launch {
            eventRepository.getAllEvents().catch { exception ->
                _uiState.value = EventUiState.Error(message = exception.message ?: "Failed to load events")
            }.collect { events -> // Convert EventEntity to EventInstance
                val instances = events.map { event ->
                    event.toEventInstance()
                }
                allEvents = instances

                val currentState = _uiState.value as? EventUiState.Success

                // Determine filter dates
                val startDate: LocalDate?
                val endDate: LocalDate?

                if (!hasInitializedFilters) { // First load: Initialize with default dates based on calendar preference
                    val defaultDates = getDefaultFilterDates()
                    startDate = defaultDates.first
                    endDate = defaultDates.second
                    hasInitializedFilters = true
                } else { // Subsequent loads: Preserve existing filter state
                    startDate = currentState?.filterStartDate
                    endDate = currentState?.filterEndDate
                }

                val filteredEvents = applyDateFilter(instances, startDate, endDate)

                _uiState.value = EventUiState.Success(events = filteredEvents, isDialogOpen = currentState?.isDialogOpen ?: false, editingEvent = currentState?.editingEvent, filterStartDate = startDate, filterEndDate = endDate)
            }
        }
    }

    /**
     * Get default filter dates based on the primary calendar preference.
     * Returns a Pair of (startDate, endDate) representing the first and last day of the current month.
     */
    private suspend fun getDefaultFilterDates(): Pair<LocalDate, LocalDate> {
        return try {
            val calendarType = settingsPreferences.primaryCalendar.first()

            when (calendarType) {
                CalendarType.ETHIOPIAN -> { // Get current Ethiopian date
                    val today = EthiopicDate.now()
                    today.lengthOfMonth()

                    // First day of the month
                    val firstDay = today.with(TemporalAdjusters.firstDayOfMonth())

                    // Last day of the month
                    val lastDay = today.with(TemporalAdjusters.lastDayOfMonth())

                    // Convert to LocalDate for filtering
                    val startDate = LocalDate.from(firstDay)
                    val endDate = LocalDate.from(lastDay)

                    Pair(startDate, endDate)
                }

                CalendarType.GREGORIAN -> { // Get current Gregorian date
                    val today = LocalDate.now()
                    val lengthOfMonth = today.lengthOfMonth()

                    // First day of the month
                    val startDate = today.withDayOfMonth(1)

                    // Last day of the month
                    val endDate = today.withDayOfMonth(lengthOfMonth)

                    Pair(startDate, endDate)
                }

                CalendarType.HIRJI -> { // For Hijri calendar, default to Gregorian for now
                    val today = LocalDate.now()
                    val lengthOfMonth = today.lengthOfMonth()
                    val startDate = today.withDayOfMonth(1)
                    val endDate = today.withDayOfMonth(lengthOfMonth)

                    Pair(startDate, endDate)
                }
            }
        } catch (e: Exception) { // Fallback to Gregorian month if something goes wrong
            val today = LocalDate.now()
            val lengthOfMonth = today.lengthOfMonth()
            Pair(today.withDayOfMonth(1), today.withDayOfMonth(lengthOfMonth))
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
            _uiState.value = currentState.copy(isDialogOpen = true, presetDate = date, editingEvent = null)
        }
    }

    /**
     * Hide the add event dialog.
     */
    fun hideAddEventDialog() {
        val currentState = _uiState.value
        if (currentState is EventUiState.Success) {
            _uiState.value = currentState.copy(isDialogOpen = false, editingEvent = null, presetDate = null)
        }
    }

    /**
     * Show the edit event dialog for a specific event.
     */
    fun showEditEventDialog(eventId: String) {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value
                if (currentState is EventUiState.Success) { // Find the event to edit
                    val eventToEdit = currentState.events.find { it.eventId == eventId }?.originalEvent
                    if (eventToEdit != null) {
                        _uiState.value = currentState.copy(isDialogOpen = true, editingEvent = eventToEdit, presetDate = null  // Clear preset date when editing
                        )
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    /**
     * Create a new event.
     *
     * @param summary Event title
     * @param description Event description (optional)
     * @param startTime Start date/time with timezone
     * @param endTime End date/time with timezone (optional)
     * @param isAllDay Whether the event is all-day
     * @param recurrenceRule Recurrence pattern (optional)
     * @param reminderMinutesBefore Minutes before event to remind (optional)
     * @param category Event category
     * @param ethiopianYear Ethiopian calendar year
     * @param ethiopianMonth Ethiopian calendar month
     * @param ethiopianDay Ethiopian calendar day
     */
    fun createEvent(
        summary: String,
        description: String? = null,
        startTime: ZonedDateTime,
        endTime: ZonedDateTime? = null,
        isAllDay: Boolean = false,
        recurrenceRule: RecurrenceRule? = null,
        reminderMinutesBefore: Int? = null,
        category: String = "PERSONAL",
        ethiopianYear: Int,
        ethiopianMonth: Int,
        ethiopianDay: Int
    ) {
        viewModelScope.launch {
            try {
                val event = EventEntity(
                    id = UUID.randomUUID().toString(),
                    summary = summary.trim(),
                    description = description?.trim(),
                    startTime = startTime,
                    endTime = endTime,
                    isAllDay = isAllDay,
                    timeZone = startTime.zone.id,
                    recurrenceRule = recurrenceRule?.toRRuleString(),
                    recurrenceEndDate = recurrenceRule?.endDate?.let {
                        ZonedDateTime.ofInstant(java.time.Instant.ofEpochMilli(it), startTime.zone)
                    },
                    reminderMinutesBefore = reminderMinutesBefore,
                    category = category,
                    ethiopianYear = ethiopianYear,
                    ethiopianMonth = ethiopianMonth,
                    ethiopianDay = ethiopianDay,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
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
                _uiState.value = EventUiState.Error(message = e.message ?: "Failed to create event")
            }
        }
    }

    /**
     * Delete an event by ID.
     * Also cancels any scheduled alarms for the event.
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
                _uiState.value = EventUiState.Error(message = e.message ?: "Failed to delete event")
            }
        }
    }

    /**
     * Update an existing event.
     *
     * This method:
     * 1. Updates the event in the database
     * 2. Cancels the old alarm
     * 3. Schedules a new alarm if reminder is enabled
     *
     * @param eventId ID of the event to update
     * @param summary Updated event title
     * @param description Updated event description (optional)
     * @param startTime Updated start date/time with timezone
     * @param endTime Updated end date/time with timezone (optional)
     * @param isAllDay Whether the event is all-day
     * @param recurrenceRule Updated recurrence pattern (optional)
     * @param reminderMinutesBefore Updated minutes before event to remind (optional)
     * @param category Updated event category
     * @param ethiopianYear Updated Ethiopian calendar year
     * @param ethiopianMonth Updated Ethiopian calendar month
     * @param ethiopianDay Updated Ethiopian calendar day
     */
    fun updateEvent(
        eventId: String,
        summary: String,
        description: String? = null,
        startTime: ZonedDateTime,
        endTime: ZonedDateTime? = null,
        isAllDay: Boolean = false,
        recurrenceRule: RecurrenceRule? = null,
        reminderMinutesBefore: Int? = null,
        category: String = "PERSONAL",
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
                    timeZone = startTime.zone.id,
                    recurrenceRule = recurrenceRule?.toRRuleString(),
                    recurrenceEndDate = recurrenceRule?.endDate?.let {
                        ZonedDateTime.ofInstant(java.time.Instant.ofEpochMilli(it), startTime.zone)
                    },
                    reminderMinutesBefore = reminderMinutesBefore,
                    category = category,
                    ethiopianYear = ethiopianYear,
                    ethiopianMonth = ethiopianMonth,
                    ethiopianDay = ethiopianDay,
                    createdAt = existingEvent.createdAt,
                    updatedAt = System.currentTimeMillis(),
                    googleCalendarEventId = existingEvent.googleCalendarEventId,
                    isSynced = false // Mark as not synced since we modified it
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
                _uiState.value = EventUiState.Error(message = e.message ?: "Failed to update event")
            }
        }
    }

    /**
     * Set the start date filter.
     * Events starting before this date (at 00:00:00) will be filtered out.
     */
    fun setFilterStartDate(startDate: LocalDate?) {
        val currentState = _uiState.value as? EventUiState.Success ?: return

        val filteredEvents = applyDateFilter(allEvents, startDate, currentState.filterEndDate)

        _uiState.value = currentState.copy(events = filteredEvents, filterStartDate = startDate, presetDate = currentState.presetDate, isDialogOpen = currentState.isDialogOpen, editingEvent = currentState.editingEvent)
    }

    /**
     * Set the end date filter.
     * Events starting after this date (at 23:59:59) will be filtered out.
     */
    fun setFilterEndDate(endDate: LocalDate?) {
        val currentState = _uiState.value as? EventUiState.Success ?: return

        val filteredEvents = applyDateFilter(allEvents, currentState.filterStartDate, endDate)

        _uiState.value = currentState.copy(events = filteredEvents, filterEndDate = endDate, presetDate = currentState.presetDate, isDialogOpen = currentState.isDialogOpen, editingEvent = currentState.editingEvent)
    }

    /**
     * Clear the start date filter.
     * Events will be shown from the beginning of time (or until the end date if set).
     */
    fun clearFilterStartDate() {
        val currentState = _uiState.value as? EventUiState.Success ?: return

        val filteredEvents = applyDateFilter(allEvents, null, currentState.filterEndDate)

        _uiState.value = currentState.copy(events = filteredEvents, filterStartDate = null, presetDate = currentState.presetDate, isDialogOpen = currentState.isDialogOpen, editingEvent = currentState.editingEvent)
    }

    /**
     * Clear the end date filter.
     * Events will be shown until the end of time (or from the start date if set).
     */
    fun clearFilterEndDate() {
        val currentState = _uiState.value as? EventUiState.Success ?: return

        val filteredEvents = applyDateFilter(allEvents, currentState.filterStartDate, null)

        _uiState.value = currentState.copy(events = filteredEvents, filterEndDate = null, presetDate = currentState.presetDate, isDialogOpen = currentState.isDialogOpen, editingEvent = currentState.editingEvent)
    }

    /**
     * Clear all date filters by setting a wide date range (1900-2100).
     */
    fun clearDateFilters() {
        val showAllStartDate = LocalDate.of(1900, 1, 1)
        val showAllEndDate = LocalDate.of(2100, 12, 31)

        val currentState = _uiState.value as? EventUiState.Success ?: return

        val filteredEvents = applyDateFilter(allEvents, showAllStartDate, showAllEndDate)

        _uiState.value = currentState.copy(events = filteredEvents, filterStartDate = showAllStartDate, filterEndDate = showAllEndDate, presetDate = currentState.presetDate, isDialogOpen = currentState.isDialogOpen, editingEvent = currentState.editingEvent)
    }

    /**
     * Set filter to current month (same as default filter).
     */
    fun setFilterToCurrentMonth() {
        viewModelScope.launch {
            val currentState = _uiState.value as? EventUiState.Success ?: return@launch

            val defaultDates = getDefaultFilterDates()
            val startDate = defaultDates.first
            val endDate = defaultDates.second

            val filteredEvents = applyDateFilter(allEvents, startDate, endDate)

            _uiState.value = currentState.copy(events = filteredEvents, filterStartDate = startDate, filterEndDate = endDate, presetDate = currentState.presetDate, isDialogOpen = currentState.isDialogOpen, editingEvent = currentState.editingEvent)
        }
    }

    /**
     * Set filter to a specific Ethiopian month.
     */
    fun setFilterToMonth(date: EthiopicDate) {
        viewModelScope.launch {
            val currentState = _uiState.value as? EventUiState.Success ?: return@launch

            // Get first and last day of the month
            val firstDay = date.with(TemporalAdjusters.firstDayOfMonth())
            val lastDay = date.with(TemporalAdjusters.lastDayOfMonth())

            // Convert to LocalDate
            val startDate = LocalDate.from(firstDay)
            val endDate = LocalDate.from(lastDay)

            val filteredEvents = applyDateFilter(allEvents, startDate, endDate)

            _uiState.value = currentState.copy(events = filteredEvents, filterStartDate = startDate, filterEndDate = endDate, presetDate = currentState.presetDate  // Preserve presetDate
            )
        }
    }

    /**
     * Set filter to a specific month and open add event dialog with preset date.
     * This ensures both operations happen atomically.
     */
    fun setFilterAndOpenDialog(date: EthiopicDate) {
        viewModelScope.launch {
            val currentState = _uiState.value as? EventUiState.Success ?: return@launch

            // Get first and last day of the month
            val firstDay = date.with(TemporalAdjusters.firstDayOfMonth())
            val lastDay = date.with(TemporalAdjusters.lastDayOfMonth())

            // Convert to LocalDate
            val startDate = LocalDate.from(firstDay)
            val endDate = LocalDate.from(lastDay)

            val filteredEvents = applyDateFilter(allEvents, startDate, endDate)

            _uiState.value = currentState.copy(events = filteredEvents, filterStartDate = startDate, filterEndDate = endDate, isDialogOpen = true, presetDate = date, editingEvent = null)
        }
    }

    /**
     * Apply date filtering to the event list.
     *
     * @param events List of all events
     * @param startDate Filter start date (events before this are excluded), time set to 00:00:00
     * @param endDate Filter end date (events after this are excluded), time set to 23:59:59
     * @return Filtered list of events
     */
    private fun applyDateFilter(
        events: List<com.shalom.calendar.data.local.entity.EventInstance>, startDate: LocalDate?, endDate: LocalDate?
    ): List<com.shalom.calendar.data.local.entity.EventInstance> {
        if (startDate == null && endDate == null) {
            return events
        }

        val filtered = events.filter { event ->
            val eventStartTime = event.instanceStart

            // Check start date filter (with time 00:00:00)
            val passesStartFilter = if (startDate != null) {
                val filterStartDateTime = startDate.atTime(LocalTime.MIN).atZone(eventStartTime.zone)
                eventStartTime >= filterStartDateTime
            } else {
                true
            }

            // Check end date filter (with time 23:59:59.999999999)
            val passesEndFilter = if (endDate != null) {
                val filterEndDateTime = endDate.atTime(LocalTime.MAX).atZone(eventStartTime.zone)
                eventStartTime <= filterEndDateTime
            } else {
                true
            }

            passesStartFilter && passesEndFilter
        }

        return filtered
    }

    /**
     * Helper function to convert EventEntity to EventInstance.
     */
    private fun EventEntity.toEventInstance() = com.shalom.calendar.data.local.entity.EventInstance(eventId = id, summary = summary, description = description, instanceStart = startTime, instanceEnd = endTime, isAllDay = isAllDay, category = category, reminderMinutesBefore = reminderMinutesBefore, ethiopianYear = ethiopianYear, ethiopianMonth = ethiopianMonth, ethiopianDay = ethiopianDay, isRecurring = recurrenceRule != null, originalEvent = this)
}
