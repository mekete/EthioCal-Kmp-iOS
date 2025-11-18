package com.shalom.calendar

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.lifecycleScope
import com.shalom.calendar.data.preferences.Language
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.ui.App
import com.shalom.calendar.ui.more.ThemeViewModel
import com.shalom.calendar.ui.theme.EthiopianCalendarTheme
import com.shalom.calendar.ui.theme.ThemeMode
import com.shalom.calendar.util.AppInfo
import com.shalom.calendar.util.LocaleHelper
import com.shalom.calendar.util.ShareManager
import com.shalom.calendar.util.UrlLauncher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()

    @Inject
    lateinit var settingsPreferences: SettingsPreferences

    @Inject
    lateinit var urlLauncher: UrlLauncher

    @Inject
    lateinit var shareManager: ShareManager

    @Inject
    lateinit var appInfo: AppInfo

    private var currentLanguage: Language? = null

    override fun attachBaseContext(newBase: Context) { // Get the saved language preference and wrap the context with it
        val language = runBlocking {
            try {
                SettingsPreferences(newBase).language.first()
            } catch (e: Exception) {
                Language.AMHARIC
            }
        }
        currentLanguage = language
        val wrappedContext = LocaleHelper.wrapContext(newBase, language)
        super.attachBaseContext(wrappedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Listen for language changes and recreate activity when language changes
        lifecycleScope.launch {
            settingsPreferences.language.collect { newLanguage ->
                if (currentLanguage != null && currentLanguage != newLanguage) {
                    recreate()
                }
            }
        }

        setContent {
            val appTheme by themeViewModel.appTheme.collectAsState()
            val themeMode by themeViewModel.themeMode.collectAsState()

            EthiopianCalendarTheme(appTheme = appTheme, themeMode = themeMode) {

                val scrim = MaterialTheme.colorScheme.surfaceContainer.toArgb()

                val isDarkTheme = when (themeMode) {
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                    ThemeMode.SYSTEM -> isSystemInDarkTheme()
                }

                SideEffect {
                    enableEdgeToEdge(statusBarStyle = if (isDarkTheme) {
                        SystemBarStyle.dark(scrim)
                    } else {
                        SystemBarStyle.light(scrim, scrim)
                    }, navigationBarStyle = if (isDarkTheme) {
                        SystemBarStyle.dark(scrim)
                    } else {
                        SystemBarStyle.light(scrim, scrim)
                    })
                }
                App(
                    settingsPreferences = settingsPreferences,
                    urlLauncher = urlLauncher,
                    shareManager = shareManager,
                    appInfo = appInfo
                )
            }
        }
    }
}
