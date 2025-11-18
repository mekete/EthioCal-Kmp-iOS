package com.shalom.calendar.data.migration

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import timber.log.Timber

/**
 * Helper class to read from old Java app's SharedPreferences.
 *
 * The old app used SharedPreferences (XML-based storage), while the new app
 * uses DataStore (protobuf-based). This class bridges the gap by reading
 * legacy preference values to determine if an upgrade should be performed.
 */
class SharedPreferencesReader(private val context: Context) {

    private val legacyPrefs: SharedPreferences by lazy { // The old app used default SharedPreferences via PreferenceManager
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    companion object {
        // Old app's preference keys (based on SettingManager usage in HomeActivity)
        // These are the most likely key names based on Android conventions
        private const val KEY_VERSION_CODE = "version_code"
        private const val KEY_CURRENT_VERSION = "current_version"
        private const val KEY_APP_VERSION = "app_version"
        private const val KEY_FIRST_TIME_ACCESS = "first_time_access"
        private const val KEY_IS_FIRST_RUN = "is_first_run"
    }

    /**
     * Attempts to read the old app's stored version code.
     * Tries multiple possible key names to maximize compatibility.
     *
     * @return The stored version code from old app, or null if not found
     */
    fun getOldAppVersionCode(): Int? {
        return try {
            val possibleKeys = listOf(KEY_VERSION_CODE, KEY_CURRENT_VERSION, KEY_APP_VERSION, "pref_version_code", "pref_current_version")

            for (key in possibleKeys) {
                val versionCode = legacyPrefs.getInt(key, -1)
                if (versionCode != -1) {
                    Timber.d("Found old version code: $versionCode")
                    return versionCode
                }
            }

            null
        } catch (e: Exception) {
            Timber.e(e, "Error reading old SharedPreferences")
            null
        }
    }

}
