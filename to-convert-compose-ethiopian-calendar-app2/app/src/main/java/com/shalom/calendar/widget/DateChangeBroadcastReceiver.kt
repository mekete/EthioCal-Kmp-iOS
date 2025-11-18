package com.shalom.calendar.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

private const val TAG = "DateChangeBroadcastReceiver"

/**
 * DateChangeBroadcastReceiver - Listens for date, time, timezone changes and manual refresh requests
 *
 * Listens for:
 * - ACTION_DATE_CHANGED: When the date changes (at midnight)
 * - ACTION_TIME_CHANGED: When the user manually changes the time
 * - ACTION_TIMEZONE_CHANGED: When the user changes the timezone
 * - ACTION_REFRESH_WIDGET: When the user taps the refresh button
 *
 * When any of these events occur, triggers an immediate widget update
 * to ensure the displayed date, time, and events are always current.
 */
class DateChangeBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "========================================")
        Log.d(TAG, "onReceive() called with action: ${intent.action}")
        when (intent.action) {
            Intent.ACTION_DATE_CHANGED, Intent.ACTION_TIME_CHANGED, Intent.ACTION_TIMEZONE_CHANGED -> {
                Log.d(TAG, "Date/Time/Timezone changed - triggering immediate date update") // Trigger immediate date update when date/time/timezone changes
                DateChangeWorker.triggerImmediateUpdate(context)
                Log.d(TAG, "Immediate date update triggered")
            }

            ACTION_REFRESH_WIDGET -> {
                Log.d(TAG, "Manual refresh requested - triggering both date and event updates") // Show toast notification to user
                Toast.makeText(context, "Widget refreshed", Toast.LENGTH_SHORT).show() // Trigger both workers for a full refresh
                DateChangeWorker.triggerImmediateUpdate(context)
                CalendarWidgetWorker.triggerImmediateUpdate(context)
                Log.d(TAG, "Both workers triggered for full refresh")
            }

            else -> {
                Log.d(TAG, "Unknown action: ${intent.action}, ignoring")
            }
        }
        Log.d(TAG, "========================================")
    }

    companion object {
        const val ACTION_REFRESH_WIDGET = "com.shalom.calendar.widget.ACTION_REFRESH_WIDGET"
    }
}
