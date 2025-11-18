package com.shalom.calendar.widget

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

private const val TAG = "MinuteUpdateWorker"

/**
 * MinuteUpdateWorker - Updates the widget every minute to refresh time display
 *
 * @deprecated This worker is no longer needed. The widget now uses TextClock widgets
 * which automatically update the time display without requiring periodic WorkManager updates.
 * See CalendarGlanceWidget.kt lines 98-103 for TextClock implementation.
 *
 * This worker previously provided TextClock-like behavior by updating the widget time every minute.
 * However, it was battery-intensive and has been replaced with native TextClock support.
 */
@Deprecated("Widget now uses TextClocks which auto-update. This worker is no longer scheduled.")
@HiltWorker
class MinuteUpdateWorker @AssistedInject constructor(
    @Assisted private val context: Context, @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "Updating widget time")
        return try { // Just trigger a UI update - the time will be recalculated in CalendarWidgetContent
            CalendarGlanceWidget().updateAll(context)
            Log.d(TAG, "Widget time updated successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "ERROR updating widget time: ${e.message}", e)
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "MinuteUpdateWidgetWork"

        /**
         * Schedule periodic updates every 15 minutes
         * Note: WorkManager minimum interval is 15 minutes for periodic work.
         * For more frequent updates, consider using AlarmManager instead.
         */
        fun schedule(context: Context) {
            val workRequest = PeriodicWorkRequestBuilder<MinuteUpdateWorker>(repeatInterval = 15, // Minimum interval for PeriodicWork
                repeatIntervalTimeUnit = TimeUnit.MINUTES).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, workRequest)
            Log.d(TAG, "Scheduled periodic updates every 15 minutes")
        }

        /**
         * Cancel scheduled minute updates
         */
        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
            Log.d(TAG, "Cancelled periodic updates")
        }
    }
}
