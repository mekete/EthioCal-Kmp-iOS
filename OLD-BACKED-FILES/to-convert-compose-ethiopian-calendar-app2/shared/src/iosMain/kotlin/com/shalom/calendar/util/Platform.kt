package com.shalom.calendar.util

actual object Platform {
    actual fun requiresNotificationPermission(): Boolean {
        // iOS always requires notification permission
        return true
    }

    actual fun name(): String = "iOS"
}
