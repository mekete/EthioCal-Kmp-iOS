package com.shalom.calendar.util

import android.os.Build

actual object Platform {
    actual fun requiresNotificationPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    actual fun name(): String = "Android"
}
