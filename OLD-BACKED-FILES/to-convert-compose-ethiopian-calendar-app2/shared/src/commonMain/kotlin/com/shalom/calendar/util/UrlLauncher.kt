package com.shalom.calendar.util

/**
 * Platform-specific URL launcher for opening links, emails, and settings
 */
expect class UrlLauncher {
    fun openUrl(url: String)
    fun openEmail(email: String, subject: String = "")
    fun openAppSettings()
}
