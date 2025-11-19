package com.shalom.calendar.util

import android.content.Context

actual class AppInfo(context: Context) {
    actual val versionName: String
    actual val buildTime: String

    init {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        versionName = packageInfo.versionName ?: "Unknown"
        // Build time would need to be set via BuildConfig - using placeholder
        buildTime = "N/A"
    }
}
