package com.shalom.calendar.util

/**
 * Platform-specific utilities
 */
expect object Platform {
    /**
     * Check if the platform requires notification permission request
     * (Android 13+ requires explicit permission, iOS always requires)
     */
    fun requiresNotificationPermission(): Boolean

    /**
     * Get the platform name
     */
    fun name(): String
}
