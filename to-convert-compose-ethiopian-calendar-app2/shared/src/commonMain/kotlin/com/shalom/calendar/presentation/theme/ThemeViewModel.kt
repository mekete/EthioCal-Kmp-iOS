package com.shalom.calendar.presentation.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shalom.calendar.data.analytics.AnalyticsEvent
import com.shalom.calendar.data.analytics.AnalyticsManager
import com.shalom.calendar.data.preferences.ThemePreferences
import com.shalom.calendar.ui.theme.AppTheme
import com.shalom.calendar.ui.theme.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for Theme settings.
 * Manages app theme and theme mode preferences.
 */
class ThemeViewModel(
    private val themePreferences: ThemePreferences,
    private val analyticsManager: AnalyticsManager
) : ViewModel() {

    val appTheme: StateFlow<AppTheme> = themePreferences.appTheme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppTheme.BLUE
    )

    val themeMode: StateFlow<ThemeMode> = themePreferences.themeMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ThemeMode.SYSTEM
    )

    fun setAppTheme(theme: AppTheme) {
        viewModelScope.launch {
            themePreferences.setAppTheme(theme)

            // Track theme change
            analyticsManager.logEvent(
                AnalyticsEvent.SettingsThemeChanged(theme = theme.name)
            )
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            themePreferences.setThemeMode(mode)

            // Track theme mode change
            analyticsManager.logEvent(
                AnalyticsEvent.SettingsThemeModeChanged(mode = mode.name)
            )
        }
    }
}
