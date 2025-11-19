package com.shalom.calendar.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.shalom.calendar.alarm.NotificationHelper
import com.shalom.calendar.data.model.NotificationCategory
import com.shalom.calendar.data.model.toAppNotification
import com.shalom.calendar.data.permissions.PermissionHelper

/**
 * Handles incoming Firebase Cloud Messaging (FCM) notifications.
 *
 * This service receives push notifications from Firebase for:
 * - Holiday reminders and updates
 * - Seasonal events
 * - Daily insights and fun facts
 * - App updates and announcements
 * - General notifications
 *
 * Notifications are categorized and routed to appropriate notification channels.
 * Rich media (images) and action buttons are supported.
 *
 * Note: Notifications will only show if user has granted POST_NOTIFICATIONS permission.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "CheckNotification"
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "===== FCM Service onCreate() called =====")
        Log.e(TAG, "Service created successfully, creating notification channels...")
        createNotificationChannels()
        Log.e(TAG, "FCM Service initialization complete")
    }

    /**
     * Called when a new FCM token is generated.
     * This happens on first install and when token is refreshed.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e(TAG, "===== NEW FCM TOKEN GENERATED =====")
        Log.e(TAG, "FCM Token: $token")
        Log.e(TAG, "Token length: ${token.length}")
        // TODO: Send this token to your server if needed
    }

    /**
     * Called when a message is received from Firebase.
     *
     * Handles both notification and data payloads in a unified way.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.e(TAG, "========================================")
        Log.e(TAG, "===== FCM MESSAGE RECEIVED =====")
        Log.e(TAG, "========================================")
        Log.e(TAG, "Message ID: ${remoteMessage.messageId}")
        Log.e(TAG, "From: ${remoteMessage.from}")
        Log.e(TAG, "Sent time: ${remoteMessage.sentTime}")
        Log.e(TAG, "Message type: ${remoteMessage.messageType}")

        // Log notification payload
        remoteMessage.notification?.let { notification ->
            Log.e(TAG, "----- Notification Payload -----")
            Log.e(TAG, "Title: ${notification.title}")
            Log.e(TAG, "Body: ${notification.body}")
            Log.e(TAG, "Image URL: ${notification.imageUrl}")
            Log.e(TAG, "Click action: ${notification.clickAction}")
        } ?: Log.e(TAG, "No notification payload present")

        // Log data payload
        if (remoteMessage.data.isNotEmpty()) {
            Log.e(TAG, "----- Data Payload -----")
            remoteMessage.data.forEach { (key, value) ->
                Log.e(TAG, "Data[$key] = $value")
            }
        } else {
            Log.e(TAG, "No data payload present")
        }

        // Check if we have permission to show notifications
        val hasPermission = PermissionHelper.canShowNotifications(this)
        Log.e(TAG, "----- Permission Check -----")
        Log.e(TAG, "Can show notifications: $hasPermission")

        if (!hasPermission) {
            Log.e(TAG, "❌ NOTIFICATION PERMISSION NOT GRANTED!")
            Log.e(TAG, "Notification will NOT be displayed")
            Log.e(TAG, "User needs to grant POST_NOTIFICATIONS permission")
            return
        }

        Log.e(TAG, "✅ Permission granted, proceeding with notification")

        // Convert to unified AppNotification model
        Log.e(TAG, "----- Converting to AppNotification -----")
        val appNotification = remoteMessage.toAppNotification()
        Log.e(TAG, "Notification ID: ${appNotification.id}")
        Log.e(TAG, "Title: ${appNotification.title}")
        Log.e(TAG, "Body: ${appNotification.body}")
        Log.e(TAG, "Category: ${appNotification.category}")
        Log.e(TAG, "Priority: ${appNotification.priority}")
        Log.e(TAG, "Action Type: ${appNotification.actionType}")
        Log.e(TAG, "Action Target: ${appNotification.actionTarget}")
        Log.e(TAG, "Action Label: ${appNotification.actionLabel}")
        Log.e(TAG, "Image URL: ${appNotification.imageUrl}")

        // Ensure notification channel exists for this category
        Log.e(TAG, "----- Creating/Verifying Channel -----")
        Log.e(TAG, "Channel ID: ${appNotification.category.channelId}")
        Log.e(TAG, "Channel Name: ${appNotification.category.channelName}")
        createChannelForCategory(appNotification.category)
        Log.e(TAG, "Channel verified/created")

        // Display the notification
        Log.e(TAG, "----- Displaying Notification -----")
        try {
            showEnhancedNotification(appNotification)
            Log.e(TAG, "✅ ✅ ✅ NOTIFICATION DISPLAYED SUCCESSFULLY ✅ ✅ ✅")
            Log.e(TAG, "Notification should now be visible in notification drawer")
        } catch (e: Exception) {
            Log.e(TAG, "❌ ❌ ❌ ERROR DISPLAYING NOTIFICATION ❌ ❌ ❌")
            Log.e(TAG, "Exception: ${e.message}")
            Log.e(TAG, "Stack trace:")
            e.printStackTrace()
        }

        Log.e(TAG, "========================================")
        Log.e(TAG, "===== FCM MESSAGE PROCESSING COMPLETE =====")
        Log.e(TAG, "========================================")
    }

    /**
     * Create notification channels for all categories on service startup.
     * This is called once when the service is created.
     */
    private fun createNotificationChannels() {
        Log.e(TAG, "Creating notification channels...")
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Create a channel for each notification category
        NotificationCategory.entries.forEach { category ->
            Log.e(TAG, "Creating channel: ${category.channelName} (ID: ${category.channelId})")
            createChannelForCategory(category, notificationManager)
        }

        Log.e(TAG, "✅ Created ${NotificationCategory.entries.size} notification channels")
    }

    /**
     * Create or update a notification channel for a specific category.
     *
     * @param category The notification category
     * @param notificationManager Optional NotificationManager instance (for efficiency)
     */
    private fun createChannelForCategory(
        category: NotificationCategory,
        notificationManager: NotificationManager? = null
    ) {
        val manager = notificationManager ?: getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            category.channelId,
            category.channelName,
            category.importance
        ).apply {
            description = getDescriptionForCategory(category)
            enableVibration(true)
            enableLights(true)
        }

        manager.createNotificationChannel(channel)
    }

    /**
     * Get description text for a notification category.
     */
    private fun getDescriptionForCategory(category: NotificationCategory): String {
        return when (category) {
            NotificationCategory.HOLIDAY -> "Updates about Ethiopian holidays and celebrations"
            NotificationCategory.SEASONAL -> "Seasonal events and observances"
            NotificationCategory.DAILY_INSIGHT -> "Daily insights, fun facts, and tips"
            NotificationCategory.APP_UPDATE -> "App updates and new features"
            NotificationCategory.GENERAL -> "General app notifications and announcements"
        }
    }

    /**
     * Display an enhanced notification with rich content and actions.
     */
    private fun showEnhancedNotification(appNotification: com.shalom.calendar.data.model.AppNotification) {
        Log.e(TAG, "Building rich notification...")
        val notificationBuilder = NotificationHelper.buildRichNotification(
            context = this,
            notification = appNotification
        )
        Log.e(TAG, "Notification builder created successfully")

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Use notification ID hash to allow multiple notifications
        val notificationId = appNotification.id.hashCode()
        Log.e(TAG, "Notification ID: $notificationId")
        Log.e(TAG, "Calling notificationManager.notify()...")

        notificationManager.notify(notificationId, notificationBuilder.build())

        Log.e(TAG, "notificationManager.notify() completed")
    }
}
