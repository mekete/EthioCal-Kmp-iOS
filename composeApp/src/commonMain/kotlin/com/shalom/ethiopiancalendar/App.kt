package com.shalom.ethiopiancalendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.presentation.settings.SettingsViewModel
import com.shalom.calendar.presentation.theme.ThemeViewModel
import com.shalom.calendar.ui.theme.EthiopianCalendarTheme
import com.shalom.calendar.ui.theme.LanguageProvider
import com.shalom.calendar.util.AppInfo
import com.shalom.calendar.util.ShareManager
import com.shalom.calendar.util.UrlLauncher
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import com.shalom.calendar.ui.App as CalendarApp

@Composable
@Preview
fun App() {
    val settingsPreferences: SettingsPreferences = koinInject()
    val urlLauncher: UrlLauncher = koinInject()
    val shareManager: ShareManager = koinInject()
    val appInfo: AppInfo = koinInject()
    val themeViewModel: ThemeViewModel = koinInject()
    val settingsViewModel: SettingsViewModel = koinInject()

    println("CHECK-LANG-ONBOARDING: App() composable, SettingsViewModel instance: ${settingsViewModel.hashCode()}")

    val appTheme by themeViewModel.appTheme.collectAsState()
    val themeMode by themeViewModel.themeMode.collectAsState()
    val language by settingsViewModel.language.collectAsState()

    println("CHECK-LANG-ONBOARDING: App() collected language: ${language.name}")

    LanguageProvider(language = language) {
        EthiopianCalendarTheme(
            appTheme = appTheme,
            themeMode = themeMode
        ) {
            CalendarApp(
                settingsPreferences = settingsPreferences,
                urlLauncher = urlLauncher,
                shareManager = shareManager,
                appInfo = appInfo
            )
        }
    }
}
