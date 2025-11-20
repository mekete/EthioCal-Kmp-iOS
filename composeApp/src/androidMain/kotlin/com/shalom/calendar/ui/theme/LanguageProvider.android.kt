package com.shalom.calendar.ui.theme

import android.content.res.Configuration
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.shalom.calendar.data.preferences.Language
import java.util.Locale

/**
 * Android implementation of LanguageProvider.
 * Overrides the Configuration locale to apply the selected language.
 */
@Composable
actual fun LanguageProvider(
    language: Language,
    content: @Composable () -> Unit
) {
    Log.d("CHECK-LANG-ONBOARDING", "LanguageProvider received language: ${language.name}, localeTag: ${language.localeTag}")

    val context = LocalContext.current
    val locale = Locale(language.localeTag)

    // Create a new configuration with the selected locale
    val configuration = Configuration(context.resources.configuration).apply {
        setLocale(locale)
    }

    // Create a context with the new configuration
    val localizedContext = context.createConfigurationContext(configuration)

    CompositionLocalProvider(
        LocalAppLanguage provides language,
        LocalConfiguration provides configuration,
        LocalContext provides localizedContext
    ) {
        content()
    }
}
