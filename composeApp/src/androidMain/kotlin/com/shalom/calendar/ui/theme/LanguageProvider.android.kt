package com.shalom.calendar.ui.theme

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
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

    // Get the Activity's currently applied locale (set in attachBaseContext)
    val activity = context as? Activity
    val currentLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        activity?.resources?.configuration?.locales?.get(0)
    } else {
        @Suppress("DEPRECATION")
        activity?.resources?.configuration?.locale
    }

    Log.d("CHECK-LANG-ONBOARDING", "LanguageProvider current Activity locale: ${currentLocale?.language}, requested: ${language.localeTag}")

    // Recreate activity only when the requested language differs from what's currently applied
    LaunchedEffect(language) {
        if (currentLocale != null && currentLocale.language != locale.language) {
            Log.d("CHECK-LANG-ONBOARDING", "Locale mismatch detected, recreating activity")
            activity?.recreate()
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
