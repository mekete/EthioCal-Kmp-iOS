package com.shalom.calendar.di

import com.shalom.calendar.data.analytics.AnalyticsManager
import com.shalom.calendar.data.analytics.NoOpAnalyticsManager
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.data.preferences.SettingsPreferencesImpl
import com.shalom.calendar.data.preferences.ThemePreferences
import com.shalom.calendar.data.preferences.ThemePreferencesImpl
import org.koin.dsl.module

/**
 * iOS platform module - provides iOS-specific implementations
 */
val iosPlatformModule = module {
    // Settings preferences using NSUserDefaults
    single<SettingsPreferences> { SettingsPreferencesImpl() }

    // Theme preferences using NSUserDefaults
    single<ThemePreferences> { ThemePreferencesImpl() }

    // Analytics manager - using NoOp for now, can be replaced with Firebase later
    single<AnalyticsManager> { NoOpAnalyticsManager() }
}
