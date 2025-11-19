package com.shalom.calendar.util

/**
 * Platform-specific share functionality
 */
expect class ShareManager {
    fun shareText(text: String, subject: String = "")
}
