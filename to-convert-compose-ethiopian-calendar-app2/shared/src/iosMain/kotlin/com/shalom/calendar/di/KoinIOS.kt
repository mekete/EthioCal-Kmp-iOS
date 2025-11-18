package com.shalom.calendar.di

import com.shalom.calendar.data.permissions.PermissionManager
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.data.preferences.ThemePreferences
import com.shalom.calendar.ui.more.ThemeViewModel
import com.shalom.calendar.util.AppInfo
import com.shalom.calendar.util.ShareManager
import com.shalom.calendar.util.UrlLauncher
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * iOS-specific Koin module providing platform dependencies.
 */
val iosModule = module {
    // Platform utilities
    singleOf(::UrlLauncher)
    singleOf(::ShareManager)
    singleOf(::AppInfo)
    singleOf(::PermissionManager)

    // Preferences
    singleOf(::SettingsPreferences)
    singleOf(::ThemePreferences)

    // ViewModels
    singleOf(::ThemeViewModel)
}

/**
 * Initialize Koin for iOS.
 * Call this from Swift AppDelegate or App initialization.
 */
fun initKoin() {
    startKoin {
        modules(iosModule)
    }
}
