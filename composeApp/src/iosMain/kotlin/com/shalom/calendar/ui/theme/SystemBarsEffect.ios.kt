package com.shalom.calendar.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

/**
 * iOS implementation - no-op as iOS handles system bar colors through its own mechanisms.
 */
@Composable
actual fun SystemBarsEffect(
    colorScheme: ColorScheme,
    isDarkTheme: Boolean
) {
    // iOS handles status bar appearance through Info.plist and UIViewController
    // No additional action needed here
}
