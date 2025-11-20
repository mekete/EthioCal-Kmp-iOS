package com.shalom.calendar.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Android implementation that sets the system bar colors (status bar and navigation bar)
 * based on the current color scheme.
 */
@Composable
actual fun SystemBarsEffect(
    colorScheme: ColorScheme,
    isDarkTheme: Boolean
) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect

            // Set status bar color to primary color
            window.statusBarColor = colorScheme.primary.toArgb()

            // Set navigation bar color to surface color
            window.navigationBarColor = colorScheme.surface.toArgb()

            // Configure whether system bar icons should be dark or light
            val insetsController = WindowCompat.getInsetsController(window, view)

            // For status bar: use light icons on dark primary color
            // We assume primary colors are dark enough to need light icons
            insetsController.isAppearanceLightStatusBars = false

            // For navigation bar: match the theme (light theme = dark icons, dark theme = light icons)
            insetsController.isAppearanceLightNavigationBars = !isDarkTheme
        }
    }
}
