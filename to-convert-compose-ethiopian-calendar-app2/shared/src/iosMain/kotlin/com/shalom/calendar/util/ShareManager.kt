package com.shalom.calendar.util

import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

actual class ShareManager {
    actual fun shareText(text: String, subject: String) {
        val activityItems = listOf(text)
        val activityViewController = UIActivityViewController(
            activityItems = activityItems,
            applicationActivities = null
        )

        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            activityViewController,
            animated = true,
            completion = null
        )
    }
}
