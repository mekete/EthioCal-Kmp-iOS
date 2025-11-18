package com.shalom.calendar

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.ui.App
import com.shalom.calendar.ui.more.ThemeViewModel
import com.shalom.calendar.ui.theme.EthiopianCalendarTheme
import com.shalom.calendar.util.AppInfo
import com.shalom.calendar.util.ShareManager
import com.shalom.calendar.util.UrlLauncher
import org.koin.compose.koinInject
import platform.UIKit.UIViewController

/**
 * Main entry point for iOS app using Compose Multiplatform.
 * This creates a UIViewController that hosts the Compose UI.
 */
fun MainViewController(): UIViewController = ComposeUIViewController {
    val settingsPreferences: SettingsPreferences = koinInject()
    val urlLauncher: UrlLauncher = koinInject()
    val shareManager: ShareManager = koinInject()
    val appInfo: AppInfo = koinInject()
    val themeViewModel: ThemeViewModel = koinInject()

    val appTheme by themeViewModel.appTheme.collectAsState()
    val themeMode by themeViewModel.themeMode.collectAsState()

    EthiopianCalendarTheme(appTheme = appTheme, themeMode = themeMode) {
        App(
            settingsPreferences = settingsPreferences,
            urlLauncher = urlLauncher,
            shareManager = shareManager,
            appInfo = appInfo
        )
    }
}
