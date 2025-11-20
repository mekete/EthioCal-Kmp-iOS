package com.shalom.calendar.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

/**
 * Applies the system bar colors (status bar and navigation bar) based on the current color scheme.
 * This is platform-specific: on Android it sets window colors, on iOS it's a no-op.
 */
@Composable
expect fun SystemBarsEffect(
    colorScheme: ColorScheme,
    isDarkTheme: Boolean
)
