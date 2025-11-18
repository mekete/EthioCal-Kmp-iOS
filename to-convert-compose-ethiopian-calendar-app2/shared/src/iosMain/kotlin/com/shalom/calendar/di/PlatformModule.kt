package com.shalom.calendar.di

import com.shalom.calendar.data.analytics.AnalyticsManager
import com.shalom.calendar.data.analytics.NoOpAnalyticsManager
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.data.preferences.SettingsPreferencesImpl
import org.koin.dsl.module

/**
 * iOS platform module - provides iOS-specific implementations
 */
val iosPlatformModule = module {
    // Settings preferences using NSUserDefaults
    single<SettingsPreferences> { SettingsPreferencesImpl() }

    // Analytics manager - using NoOp for now, can be replaced with Firebase later
    single<AnalyticsManager> { NoOpAnalyticsManager() }
}
