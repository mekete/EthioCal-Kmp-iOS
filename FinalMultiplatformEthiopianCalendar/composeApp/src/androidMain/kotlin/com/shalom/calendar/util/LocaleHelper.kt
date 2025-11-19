package com.shalom.calendar.util

import android.content.Context
import android.content.res.Configuration
import com.shalom.calendar.data.preferences.Language
import java.util.Locale

/**
 * Helper object for managing app localization
 */
object LocaleHelper {

    /**
     * Wraps the given context with the locale corresponding to the selected language
     */
    fun wrapContext(context: Context, language: Language): Context {
        val locale = getLocaleFromLanguage(language)
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)

        return context.createConfigurationContext(configuration)
    }

    /**
     * Converts a Language enum to the corresponding Locale
     */
    fun getLocaleFromLanguage(language: Language): Locale {
        return when (language) {
            Language.ENGLISH -> Locale.forLanguageTag("en")
            Language.AMHARIC -> Locale.forLanguageTag("am")
            Language.OROMIFFA -> Locale.forLanguageTag("om")
            Language.TIGRIGNA -> Locale.forLanguageTag("ti")
            Language.FRENCH -> Locale.forLanguageTag("fr")
        }
    }

    /**
     * Gets the locale code as a string
     */
    fun getLocaleCode(language: Language): String {
        return when (language) {
            Language.ENGLISH -> "en"
            Language.AMHARIC -> "am"
            Language.OROMIFFA -> "om"
            Language.TIGRIGNA -> "ti"
            Language.FRENCH -> "fr"
        }
    }

    /**
     * Updates the configuration of the given context with the selected language
     */
    fun updateConfiguration(context: Context, language: Language) {
        val locale = getLocaleFromLanguage(language)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)

        @Suppress("DEPRECATION") context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }
}
