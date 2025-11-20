package com.shalom.calendar.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.shalom.calendar.data.preferences.Language

/**
 * CompositionLocal for providing the current app language
 */
val LocalAppLanguage = compositionLocalOf { Language.AMHARIC }

/**
 * Provides the app language to the composition tree.
 * Platform-specific implementations will handle locale configuration.
 */
@Composable
expect fun LanguageProvider(
    language: Language,
    content: @Composable () -> Unit
)
