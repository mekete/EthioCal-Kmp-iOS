package com.shalom.calendar.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.shalom.calendar.ui.theme.AppTheme
import com.shalom.calendar.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Android implementation of ThemePreferences using DataStore.
 */
class ThemePreferencesImpl(private val context: Context) : ThemePreferences {

    companion object {
        private val Context.themeDataStore by preferencesDataStore(name = "theme_preferences")

        private val APP_THEME_KEY = intPreferencesKey("app_theme")
        private val THEME_MODE_KEY = intPreferencesKey("theme_mode")
    }

    override val appTheme: Flow<AppTheme> = context.themeDataStore.data.map { preferences ->
        AppTheme.fromOrdinal(preferences[APP_THEME_KEY] ?: AppTheme.BLUE.ordinal)
    }

    override val themeMode: Flow<ThemeMode> = context.themeDataStore.data.map { preferences ->
        ThemeMode.fromOrdinal(preferences[THEME_MODE_KEY] ?: ThemeMode.SYSTEM.ordinal)
    }

    override suspend fun setAppTheme(theme: AppTheme) {
        context.themeDataStore.edit { preferences ->
            preferences[APP_THEME_KEY] = theme.ordinal
        }
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        context.themeDataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode.ordinal
        }
    }
}
