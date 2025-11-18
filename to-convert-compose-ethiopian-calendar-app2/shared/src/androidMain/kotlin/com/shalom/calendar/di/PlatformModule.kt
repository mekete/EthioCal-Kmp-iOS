package com.shalom.calendar.di

import android.content.Context
import com.shalom.calendar.data.analytics.AnalyticsManager
import com.shalom.calendar.data.analytics.NoOpAnalyticsManager
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.data.preferences.SettingsPreferencesImpl
import com.shalom.calendar.data.preferences.ThemePreferences
import com.shalom.calendar.data.preferences.ThemePreferencesImpl
import org.koin.dsl.module

/**
 * Android platform module - provides Android-specific implementations
 */
fun androidPlatformModule(context: Context) = module {
    // Settings preferences using DataStore
    single<SettingsPreferences> { SettingsPreferencesImpl(context) }

    // Theme preferences using DataStore
    single<ThemePreferences> { ThemePreferencesImpl(context) }

    // Analytics manager - using NoOp for now, can be replaced with Firebase later
    single<AnalyticsManager> { NoOpAnalyticsManager() }
}
