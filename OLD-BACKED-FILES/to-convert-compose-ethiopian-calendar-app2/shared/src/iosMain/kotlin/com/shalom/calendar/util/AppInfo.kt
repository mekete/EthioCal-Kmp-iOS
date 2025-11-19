package com.shalom.calendar.util

import platform.Foundation.NSBundle

actual class AppInfo {
    actual val versionName: String
    actual val buildTime: String

    init {
        val bundle = NSBundle.mainBundle
        versionName = bundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "Unknown"
        buildTime = bundle.objectForInfoDictionaryKey("CFBundleVersion") as? String ?: "N/A"
    }
}
