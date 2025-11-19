package com.shalom.calendar.widget

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.shalom.calendar.data.repository.EventRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

private const val TAG = "CalendarWidgetWorker"

/**
 * CalendarWidgetWorker - Background worker to update the Calendar Widget
 *
 * This worker:
 * - Runs periodically (every 30 minutes)
 * - Fetches upcoming events from EventRepository
 * - Updates all widget instances with fresh data
 */
@HiltWorker
class CalendarWidgetWorker @AssistedInject constructor(
    @Assisted private val context: Context, @Assisted workerParams: WorkerParameters, private val eventRepository: EventRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "========================================")
        Log.d(TAG, "doWork() started - Updating widget")
        return try { // Fetch upcoming events from repository
            Log.d(TAG, "Fetching upcoming events from repository")
            val upcomingEvents = eventRepository.getUpcomingEvents(limit = 4).first()
            Log.d(TAG, "Fetched ${upcomingEvents.size} upcoming events")

            // Convert to WidgetEvent
            val widgetEvents = upcomingEvents.map { event ->
                WidgetEvent(id = event.eventId, title = event.summary, startTime = event.instanceStart.toInstant().toEpochMilli(), endTime = event.instanceEnd?.toInstant()?.toEpochMilli(), isAllDay = event.isAllDay, category = event.category)
            }
            Log.d(TAG, "Converted to ${widgetEvents.size} widget events")

            // Create new widget state
            val widgetState = CalendarWidgetState(events = widgetEvents)
            Log.d(TAG, "Created widget state: $widgetState")

            // Get all widget instances
            val glanceAppWidgetManager = GlanceAppWidgetManager(context)
            val glanceIds = glanceAppWidgetManager.getGlanceIds(CalendarGlanceWidget::class.java)
            Log.d(TAG, "Found ${glanceIds.size} widget instances: $glanceIds")

            // Update each widget instance with new state
            glanceIds.forEach { glanceId ->
                Log.d(TAG, "Updating state for widget ID: $glanceId")
                updateAppWidgetState(context = context, definition = CalendarWidgetStateDefinition, glanceId = glanceId, updateState = { widgetState })
                Log.d(TAG, "State updated for widget ID: $glanceId")
            }

            // Trigger widget update to refresh UI
            Log.d(TAG, "Triggering widget UI update")
            CalendarGlanceWidget().updateAll(context)
            Log.d(TAG, "Widget UI update completed")

            Log.d(TAG, "doWork() completed successfully")
            Log.d(TAG, "========================================")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "ERROR in doWork(): ${e.message}", e)
            Log.e(TAG, "Stack trace: ${e.stackTraceToString()}")
            Log.d(TAG, "========================================")
            e.printStackTrace()
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "CalendarWidgetUpdateWork"

        /**
         * Schedule periodic widget updates
         * Updates every 30 minutes to keep events fresh
         */
        fun schedule(context: Context) {
            val workRequest = PeriodicWorkRequestBuilder<CalendarWidgetWorker>(repeatInterval = 30, repeatIntervalTimeUnit = TimeUnit.MINUTES).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, workRequest)
        }

        /**
         * Trigger an immediate widget update
         * Useful for manual refresh or when events change
         */
        fun triggerImmediateUpdate(context: Context) {
            val workRequest = androidx.work.OneTimeWorkRequestBuilder<CalendarWidgetWorker>().build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }

        /**
         * Cancel scheduled widget updates
         */
        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
