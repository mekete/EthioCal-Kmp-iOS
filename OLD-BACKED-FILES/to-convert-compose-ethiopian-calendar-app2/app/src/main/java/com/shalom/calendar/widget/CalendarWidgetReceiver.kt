package com.shalom.calendar.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

private const val TAG = "CalendarWidgetReceiver"

/**
 * CalendarWidgetReceiver - BroadcastReceiver for the Calendar Glance Widget
 *
 * This receiver handles widget lifecycle events:
 * - Widget added to home screen
 * - Widget updated
 * - Widget removed from home screen
 * - Widget configuration changed
 */
class CalendarWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = CalendarGlanceWidget()

    /**
     * Called when the first widget instance is added
     * Schedule background workers for periodic updates and date monitoring
     */
    override fun onEnabled(context: Context) {
        Log.d(TAG, "========================================")
        Log.d(TAG, "onEnabled() called - First widget instance added")
        try {
            super.onEnabled(context)

            // Schedule periodic widget updates every 30 minutes
            Log.d(TAG, "Scheduling CalendarWidgetWorker")
            CalendarWidgetWorker.schedule(context)

            // Schedule hourly date updates
            Log.d(TAG, "Scheduling DateChangeWorker")
            DateChangeWorker.scheduleDaily(context)

            // Note: MinuteUpdateWorker removed - widget now uses TextClocks which auto-update

            Log.d(TAG, "onEnabled() completed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "ERROR in onEnabled(): ${e.message}", e)
        }
        Log.d(TAG, "========================================")
    }

    /**
     * Called when widget is updated
     */
    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
    ) {
        Log.d(TAG, "========================================")
        Log.d(TAG, "onUpdate() called with ${appWidgetIds.size} widget IDs: ${appWidgetIds.contentToString()}")
        try {
            super.onUpdate(context, appWidgetManager, appWidgetIds)
            Log.d(TAG, "onUpdate() completed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "ERROR in onUpdate(): ${e.message}", e)
            Log.e(TAG, "Stack trace: ${e.stackTraceToString()}")
        }
        Log.d(TAG, "========================================")
    }

    /**
     * Called when the last widget instance is removed
     * Cancel all scheduled background workers
     */
    override fun onDisabled(context: Context) {
        Log.d(TAG, "========================================")
        Log.d(TAG, "onDisabled() called - Last widget instance removed")
        try {
            super.onDisabled(context)

            // Cancel periodic widget updates
            Log.d(TAG, "Cancelling CalendarWidgetWorker")
            CalendarWidgetWorker.cancel(context)

            // Cancel date change monitoring
            Log.d(TAG, "Cancelling DateChangeWorker")
            DateChangeWorker.cancel(context)

            // Note: MinuteUpdateWorker removed - widget now uses TextClocks which auto-update

            Log.d(TAG, "onDisabled() completed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "ERROR in onDisabled(): ${e.message}", e)
        }
        Log.d(TAG, "========================================")
    }
}
