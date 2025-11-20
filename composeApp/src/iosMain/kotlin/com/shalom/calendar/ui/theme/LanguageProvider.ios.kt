package com.shalom.calendar.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.shalom.calendar.data.preferences.Language

/**
 * iOS implementation of LanguageProvider.
 * Provides the language context to composables.
 * Note: Full iOS localization requires additional setup with NSBundle.
 */
@Composable
actual fun LanguageProvider(
    language: Language,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAppLanguage provides language
    ) {
        content()
    }
}
