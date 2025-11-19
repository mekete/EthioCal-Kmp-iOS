package com.shalom.calendar.ui.event

import com.shalom.calendar.data.local.entity.EventEntity
import com.shalom.calendar.data.local.entity.EventInstance
import com.shalom.ethiopicchrono.EthiopicDate
import java.time.LocalDate

/**
 * Sealed class representing the UI state for EventScreen.
 *
 * Follows the same pattern as HolidayListUiState:
 * - Loading: Initial state or when loading data
 * - Success: Data loaded successfully with list of events
 * - Error: Failed to load data with error message
 */
sealed class EventUiState {
    /**
     * Loading state - shown when fetching events from database.
     */
    data object Loading : EventUiState()

    /**
     * Success state - events loaded successfully.
     *
     * @param events List of event instances (includes recurring event instances)
     * @param isDialogOpen Whether the add event dialog is currently shown
     * @param editingEvent Event currently being edited (null if creating new event)
     * @param filterStartDate Start date for filtering events (null means no start filter)
     * @param filterEndDate End date for filtering events (null means no end filter)
     * @param presetDate Preset date for creating a new event (from navigation parameter)
     */
    data class Success(
        val events: List<EventInstance>, val isDialogOpen: Boolean = false, val editingEvent: EventEntity? = null, val filterStartDate: LocalDate? = null, val filterEndDate: LocalDate? = null, val presetDate: EthiopicDate? = null
    ) : EventUiState()

    /**
     * Error state - failed to load events.
     *
     * @param message Error message to display to user
     */
    data class Error(val message: String) : EventUiState()
}
