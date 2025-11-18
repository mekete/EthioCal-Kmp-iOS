package com.shalom.calendar.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.shalom.calendar.MainActivity
import com.shalom.calendar.R
import com.shalom.calendar.data.model.ActionType
import com.shalom.calendar.data.model.AppNotification
import com.shalom.calendar.data.model.NotificationPriority
import com.shalom.calendar.data.permissions.PermissionHelper

/**
 * Helper class for managing notification channels and building notifications.
 *
 * Responsibilities:
 * - Create notification channels (required for Android 8.0+)
 * - Build event reminder notifications
 * - Handle notification actions
 *
 * Best practices:
 * - Create channels on app startup or first use
 * - Use unique channel IDs for different notification types
 * - Allow users to customize notification settings via system UI
 */
object NotificationHelper {

    /**
     * Notification channel ID for event reminders.
     * This should match the notificationChannelId field in EventEntity.
     */
    const val CHANNEL_ID_EVENT_REMINDERS = "event_reminders"

    /**
     * Notification channel name displayed to users.
     */
    private const val CHANNEL_NAME = "Event Reminders"

    /**
     * Notification channel description.
     */
    private const val CHANNEL_DESCRIPTION = "Notifications for upcoming calendar events"

    /**
     * Create notification channels.
     * Should be called when the app starts (e.g., in Application.onCreate()).
     *
     * On Android 8.0 (API 26) and higher, you must create notification channels
     * before posting any notifications.
     */
    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Use alarm sound for more prominent, attention-grabbing notifications
            val alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel = NotificationChannel(CHANNEL_ID_EVENT_REMINDERS, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH // High importance for time-sensitive reminders
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                enableLights(true)
                setSound(alarmSoundUri, audioAttributes) // Allow users to customize sound, vibration, etc. via system settings
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Build a notification for an event reminder.
     *
     * @param context Application context
     * @param eventId Unique event ID
     * @param title Event title/summary
     * @param description Event description (optional)
     * @param eventTimeMillis Event start time in milliseconds
     * @return NotificationCompat.Builder configured for the event reminder
     */
    fun buildEventReminderNotification(
        context: Context, eventId: String, title: String, description: String?, eventTimeMillis: Long
    ): NotificationCompat.Builder { // Create intent to open the app when notification is tapped
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // You can add extras here to navigate to the specific event
            putExtra("event_id", eventId)
        }

        val pendingIntent = PendingIntent.getActivity(context, eventId.hashCode(), // Use event ID hash as unique request code
            intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // Build the notification
        // Use alarm sound for more prominent, attention-grabbing notifications
        val alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        return NotificationCompat.Builder(context, CHANNEL_ID_EVENT_REMINDERS).setSmallIcon(R.drawable.ic_notification_calendar) // Simple calendar icon for notifications
                .setContentTitle(title).setContentText(description ?: "Event reminder").setPriority(NotificationCompat.PRIORITY_HIGH) // High priority for heads-up notification
                .setCategory(NotificationCompat.CATEGORY_REMINDER).setAutoCancel(true) // Dismiss when tapped
                .setContentIntent(pendingIntent).setSound(alarmSoundUri) // Use alarm sound instead of default notification sound
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS) // Vibration and lights
    }

    /**
     * Show an event reminder notification.
     *
     * This method checks for notification permission before attempting to show
     * the notification. On Android 13+, POST_NOTIFICATIONS permission is required.
     *
     * @param context Application context
     * @param eventId Unique event ID
     * @param title Event title
     * @param description Event description (optional)
     * @param eventTimeMillis Event start time in milliseconds
     * @return true if notification was shown, false if permission denied
     */
    fun showEventReminderNotification(
        context: Context, eventId: String, title: String, description: String?, eventTimeMillis: Long
    ): Boolean { // Check if we have permission to show notifications
        if (!PermissionHelper.canShowNotifications(context)) {
            return false
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Verify notification channel exists (should be created on app startup)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel(CHANNEL_ID_EVENT_REMINDERS)
            if (channel == null) {
                createNotificationChannels(context)
            }
        }

        val notification = buildEventReminderNotification(context, eventId, title, description, eventTimeMillis).build()

        try { // Use event ID hash as unique notification ID so each event has its own notification
            notificationManager.notify(eventId.hashCode(), notification)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * Build a rich notification for Firebase push notifications.
     *
     * Supports:
     * - Custom actions (URL, in-app navigation)
     * - Rich media (images via BigPictureStyle)
     * - Priority levels
     * - Action buttons
     *
     * @param context Application context
     * @param notification AppNotification object containing all notification data
     * @return NotificationCompat.Builder configured for the notification
     */
    fun buildRichNotification(
        context: Context,
        notification: AppNotification
    ): NotificationCompat.Builder {
        android.util.Log.e("CheckNotification", "NotificationHelper.buildRichNotification() called")
        android.util.Log.e("CheckNotification", "Creating intent for action...")

        val intent = createIntentForAction(context, notification)
        android.util.Log.e("CheckNotification", "Intent created: $intent")

        android.util.Log.e("CheckNotification", "Creating pending intent...")
        val pendingIntent = PendingIntent.getActivity(
            context,
            notification.id.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        android.util.Log.e("CheckNotification", "Pending intent created")

        android.util.Log.e("CheckNotification", "Building notification with:")
        android.util.Log.e("CheckNotification", "  Channel ID: ${notification.category.channelId}")
        android.util.Log.e("CheckNotification", "  Icon: R.drawable.ic_notification_calendar")
        android.util.Log.e("CheckNotification", "  Title: ${notification.title}")
        android.util.Log.e("CheckNotification", "  Body: ${notification.body}")
        android.util.Log.e("CheckNotification", "  Priority: ${notification.priority.toCompatPriority()}")

        val builder = NotificationCompat.Builder(context, notification.category.channelId)
            .setSmallIcon(R.drawable.ic_notification_calendar)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notification.body))
            .setPriority(notification.priority.toCompatPriority())
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        android.util.Log.e("CheckNotification", "Notification builder created")

        // Add action button if action is defined
        if (notification.actionType != null && notification.actionLabel != null) {
            android.util.Log.e("CheckNotification", "Adding action button: ${notification.actionLabel}")
            builder.addAction(
                0, // No icon for action button
                notification.actionLabel,
                pendingIntent
            )
            android.util.Log.e("CheckNotification", "Action button added")
        } else {
            android.util.Log.e("CheckNotification", "No action button to add")
        }

        // TODO: Add support for image loading
        // If imageUrl is present, load the image asynchronously and update notification
        // This requires using Glide/Coil to load bitmap from URL
        // For now, we use BigTextStyle; can be enhanced to BigPictureStyle later

        android.util.Log.e("CheckNotification", "✅ Notification builder ready to return")
        return builder
    }

    /**
     * Create an Intent based on the notification's action type.
     *
     * @param context Application context
     * @param notification AppNotification with action details
     * @return Intent configured for the specified action
     */
    private fun createIntentForAction(
        context: Context,
        notification: AppNotification
    ): Intent {
        return when (notification.actionType) {
            ActionType.URL -> {
                // Open URL in browser
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(notification.actionTarget)
                }
            }
            ActionType.IN_APP_HOLIDAY -> {
                // Navigate to holiday screen with specific holiday
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("navigate_to", "holiday")
                    putExtra("holiday_id", notification.actionTarget)
                }
            }
            ActionType.IN_APP_EVENT -> {
                // Navigate to event screen with specific event
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("navigate_to", "event")
                    putExtra("event_id", notification.actionTarget)
                }
            }
            ActionType.IN_APP_CONVERTER -> {
                // Navigate to date converter screen
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("navigate_to", "converter")
                }
            }
            ActionType.IN_APP_SETTINGS -> {
                // Navigate to settings screen
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("navigate_to", "settings")
                }
            }
            null -> {
                // Default: Just open the app
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
        }
    }

    /**
     * Convert NotificationPriority to NotificationCompat priority constant.
     */
    private fun NotificationPriority.toCompatPriority(): Int {
        return when (this) {
            NotificationPriority.LOW -> NotificationCompat.PRIORITY_LOW
            NotificationPriority.NORMAL -> NotificationCompat.PRIORITY_DEFAULT
            NotificationPriority.HIGH -> NotificationCompat.PRIORITY_HIGH
        }
    }
}
