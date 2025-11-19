package com.shalom.calendar.data.preferences

import com.shalom.calendar.ui.theme.AppTheme
import com.shalom.calendar.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow

/**
 * Interface for theme preference storage.
 * Platform-specific implementations handle persistence using DataStore (Android) or NSUserDefaults (iOS).
 */
interface ThemePreferences {
    /**
     * Current app color theme
     */
    val appTheme: Flow<AppTheme>

    /**
     * Current theme mode (system, light, dark)
     */
    val themeMode: Flow<ThemeMode>

    /**
     * Set the app color theme
     */
    suspend fun setAppTheme(theme: AppTheme)

    /**
     * Set the theme mode
     */
    suspend fun setThemeMode(mode: ThemeMode)
}
