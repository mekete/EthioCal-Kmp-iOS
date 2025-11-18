package com.shalom.calendar.util

/**
 * Platform-specific app information
 */
expect class AppInfo {
    val versionName: String
    val buildTime: String
}
