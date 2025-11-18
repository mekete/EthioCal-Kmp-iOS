package com.shalom.calendar.data.model

import com.google.firebase.messaging.RemoteMessage
import java.util.UUID

/**
 * Represents a push notification received from Firebase Cloud Messaging.
 *
 * This is a unified model that handles both notification and data payloads
 * from FCM messages.
 */
data class AppNotification(
    val id: String,
    val title: String,
    val body: String,
    val category: NotificationCategory,
    val priority: NotificationPriority = NotificationPriority.NORMAL,
    val actionType: ActionType? = null,
    val actionTarget: String? = null,
    val actionLabel: String? = null,
    val imageUrl: String? = null
)

/**
 * Categories for organizing notifications.
 * Each category maps to a specific notification channel.
 */
enum class NotificationCategory(
    val channelId: String,
    val channelName: String,
    val importance: Int  // NotificationManager.IMPORTANCE_*
) {
    HOLIDAY(
        channelId = "holiday_notifications",
        channelName = "Holiday Reminders",
        importance = 4  // IMPORTANCE_HIGH
    ),
    SEASONAL(
        channelId = "seasonal_notifications",
        channelName = "Seasonal Events",
        importance = 3  // IMPORTANCE_DEFAULT
    ),
    DAILY_INSIGHT(
        channelId = "daily_insights",
        channelName = "Daily Insights",
        importance = 2  // IMPORTANCE_LOW
    ),
    APP_UPDATE(
        channelId = "app_updates",
        channelName = "App Updates",
        importance = 3  // IMPORTANCE_DEFAULT
    ),
    GENERAL(
        channelId = "general_notifications",
        channelName = "General Announcements",
        importance = 3  // IMPORTANCE_DEFAULT
    )
}

/**
 * Priority levels for notifications.
 */
enum class NotificationPriority {
    LOW,
    NORMAL,
    HIGH
}

/**
 * Types of actions that can be triggered when a notification is tapped.
 */
enum class ActionType {
    URL,                    // Open external URL in browser
    IN_APP_HOLIDAY,         // Navigate to holiday screen
    IN_APP_EVENT,           // Navigate to event screen
    IN_APP_CONVERTER,       // Navigate to date converter screen
    IN_APP_SETTINGS         // Navigate to settings screen
}

/**
 * Extension function to convert a Firebase RemoteMessage to AppNotification.
 *
 * Expected FCM JSON structure:
 * {
 *   "notification": {
 *     "title": "Title text",
 *     "body": "Body text"
 *   },
 *   "data": {
 *     "category": "HOLIDAY",
 *     "priority": "HIGH",
 *     "actionType": "IN_APP_HOLIDAY",
 *     "actionTarget": "holiday/meskel",
 *     "actionLabel": "Learn More",
 *     "imageUrl": "https://example.com/image.jpg"
 *   }
 * }
 */
fun RemoteMessage.toAppNotification(): AppNotification {
    val notif = this.notification
    val data = this.data

    return AppNotification(
        id = this.messageId ?: UUID.randomUUID().toString(),
        title = notif?.title ?: data["title"] ?: "",
        body = notif?.body ?: data["body"] ?: "",
        category = data["category"]?.let {
            try {
                NotificationCategory.valueOf(it)
            } catch (e: IllegalArgumentException) {
                NotificationCategory.GENERAL
            }
        } ?: NotificationCategory.GENERAL,
        priority = data["priority"]?.let {
            try {
                NotificationPriority.valueOf(it)
            } catch (e: IllegalArgumentException) {
                NotificationPriority.NORMAL
            }
        } ?: NotificationPriority.NORMAL,
        actionType = data["actionType"]?.let {
            try {
                ActionType.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        },
        actionTarget = data["actionTarget"],
        actionLabel = data["actionLabel"],
        imageUrl = notif?.imageUrl?.toString() ?: data["imageUrl"]
    )
}
