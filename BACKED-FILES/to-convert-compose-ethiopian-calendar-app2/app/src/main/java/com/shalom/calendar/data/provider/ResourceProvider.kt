package com.shalom.calendar.data.provider

import android.content.Context
import android.content.res.Configuration
import com.shalom.calendar.data.preferences.Language
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.util.LocaleHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provides access to localized string resources for the domain layer
 * Dynamically uses the current language preference to ensure strings respect locale changes
 */
@Singleton
class ResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context, private val settingsPreferences: SettingsPreferences
) {
    /**
     * Get a localized context that uses the current language preference
     */
    private fun getLocalizedContext(): Context {
        val language = runBlocking {
            try {
                settingsPreferences.language.first()
            } catch (e: Exception) {
                Language.AMHARIC
            }
        }
        val locale = LocaleHelper.getLocaleFromLanguage(language)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    fun getString(resId: Int): String {
        return getLocalizedContext().getString(resId)
    }

    fun getString(resId: Int, vararg formatArgs: Any): String {
        return getLocalizedContext().getString(resId, *formatArgs)
    }

    fun getStringArray(resId: Int): Array<String> {
        return getLocalizedContext().resources.getStringArray(resId)
    }
}
