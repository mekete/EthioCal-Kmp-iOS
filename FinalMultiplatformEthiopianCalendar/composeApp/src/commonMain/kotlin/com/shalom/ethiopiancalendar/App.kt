package com.shalom.ethiopiancalendar

import androidx.compose.runtime.Composable
import com.shalom.calendar.data.preferences.SettingsPreferences
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

    CalendarApp(
        settingsPreferences = settingsPreferences,
        urlLauncher = urlLauncher,
        shareManager = shareManager,
        appInfo = appInfo
    )
}
