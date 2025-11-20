package com.shalom.calendar.ui.theme

import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.shalom.calendar.data.preferences.Language
import java.util.Locale

/**
 * Android implementation of LanguageProvider.
 * Overrides the Configuration locale to apply the selected language.
 * Recreates the Activity when language changes for proper resource reloading.
 */
@Composable
actual fun LanguageProvider(
    language: Language,
    content: @Composable () -> Unit
) {
    Log.d("CHECK-LANG-ONBOARDING", "LanguageProvider received language: ${language.name}, localeTag: ${language.localeTag}")

    val context = LocalContext.current
    val locale = Locale(language.localeTag)

    // Track initial language to detect changes
    var initialLanguage by remember { mutableStateOf(language) }
    var hasRecreated by remember { mutableStateOf(false) }

    // Recreate activity when language changes (only once)
    LaunchedEffect(language) {
        if (language != initialLanguage && !hasRecreated) {
            Log.d("CHECK-LANG-ONBOARDING", "Language changed from ${initialLanguage.name} to ${language.name}, recreating activity")
            hasRecreated = true
            (context as? Activity)?.recreate()
        }
    }

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
