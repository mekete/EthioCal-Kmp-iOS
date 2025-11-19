package com.shalom.calendar.data.preferences

import com.shalom.calendar.ui.theme.AppTheme
import com.shalom.calendar.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Foundation.NSUserDefaults

/**
 * iOS implementation of ThemePreferences using NSUserDefaults.
 */
class ThemePreferencesImpl : ThemePreferences {

    private val userDefaults = NSUserDefaults.standardUserDefaults

    companion object {
        private const val APP_THEME_KEY = "app_theme"
        private const val THEME_MODE_KEY = "theme_mode"
    }

    // State flows for reactive updates
    private val _appTheme = MutableStateFlow(getAppTheme(APP_THEME_KEY, AppTheme.BLUE))
    private val _themeMode = MutableStateFlow(getThemeMode(THEME_MODE_KEY, ThemeMode.SYSTEM))

    override val appTheme: Flow<AppTheme> = _appTheme.asStateFlow()
    override val themeMode: Flow<ThemeMode> = _themeMode.asStateFlow()

    private fun getAppTheme(key: String, defaultValue: AppTheme): AppTheme {
        val ordinal = userDefaults.integerForKey(key).toInt()
        // Check if key exists in user defaults
        return if (userDefaults.objectForKey(key) != null) {
            AppTheme.fromOrdinal(ordinal)
        } else {
            defaultValue
        }
    }

    private fun getThemeMode(key: String, defaultValue: ThemeMode): ThemeMode {
        val ordinal = userDefaults.integerForKey(key).toInt()
        // Check if key exists in user defaults
        return if (userDefaults.objectForKey(key) != null) {
            ThemeMode.fromOrdinal(ordinal)
        } else {
            defaultValue
        }
    }

    override suspend fun setAppTheme(theme: AppTheme) {
        userDefaults.setInteger(theme.ordinal.toLong(), APP_THEME_KEY)
        userDefaults.synchronize()
        _appTheme.value = theme
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        userDefaults.setInteger(mode.ordinal.toLong(), THEME_MODE_KEY)
        userDefaults.synchronize()
        _themeMode.value = mode
    }
}
