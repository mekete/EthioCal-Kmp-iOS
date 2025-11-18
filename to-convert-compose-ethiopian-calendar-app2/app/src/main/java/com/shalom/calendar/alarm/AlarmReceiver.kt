package com.shalom.calendar.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.shalom.calendar.data.repository.EventRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * BroadcastReceiver that handles event alarm triggers.
 *
 * Responsibilities:
 * - Receive alarm broadcasts from AlarmManager
 * - Show notification to user
 * - For repeating events, schedule the next occurrence
 * - Handle edge cases (deleted events, modified events, etc.)
 *
 * Design notes:
 * - Uses Hilt for dependency injection
 * - Uses coroutines for async database operations
 * - Uses goAsync() to allow async work in BroadcastReceiver
 *
 * Important:
 * - BroadcastReceivers have a limited time to complete work (~10 seconds)
 * - Use goAsync() to extend the time limit for async operations
 * - Don't perform long-running operations here; use WorkManager for that
 */
@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var eventRepository: EventRepository

    // Use a supervisor job so failure in one coroutine doesn't cancel others
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    companion object {
        /**
         * Action for event alarm broadcasts.
         */
        const val ACTION_EVENT_ALARM = "com.shalom.calendar.ACTION_EVENT_ALARM"

        /**
         * Intent extras for event details.
         */
        const val EXTRA_EVENT_ID = "event_id"
        const val EXTRA_EVENT_TITLE = "event_title"
        const val EXTRA_EVENT_DESCRIPTION = "event_description"
        const val EXTRA_EVENT_TIME = "event_time"
        const val EXTRA_IS_RECURRING = "is_recurring"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_EVENT_ALARM) {
            return
        }

        // Extract event details from intent
        val eventId = intent.getStringExtra(EXTRA_EVENT_ID) ?: return
        val eventTitle = intent.getStringExtra(EXTRA_EVENT_TITLE) ?: "Event Reminder"
        val eventDescription = intent.getStringExtra(EXTRA_EVENT_DESCRIPTION)
        val eventTime = intent.getLongExtra(EXTRA_EVENT_TIME, 0L)
        val isRecurring = intent.getBooleanExtra(EXTRA_IS_RECURRING, false)

        // Use goAsync() to allow async operations
        // This gives us ~10 seconds to complete our work
        val pendingResult = goAsync()

        scope.launch {
            try { // Verify the event still exists and hasn't been deleted
                val event = eventRepository.getEventById(eventId).firstOrNull()

                if (event == null) {
                    return@launch
                }

                // Show notification
                NotificationHelper.showEventReminderNotification(context, eventId, eventTitle, eventDescription, eventTime)

                // For recurring events, schedule the next occurrence
                if (isRecurring && event.recurrenceRule != null) {
                    val alarmScheduler = AlarmScheduler(context)
                    alarmScheduler.scheduleAlarm(event)
                }

            } catch (e: Exception) {
            } finally { // Signal that we're done
                pendingResult.finish()
            }
        }
    }
}
