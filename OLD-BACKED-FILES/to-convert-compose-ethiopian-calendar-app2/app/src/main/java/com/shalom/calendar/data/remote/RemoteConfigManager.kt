package com.shalom.calendar.data.remote

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.shalom.calendar.R
import com.shalom.calendar.data.preferences.SettingsPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data class representing the flat holiday offset configuration from Firebase Remote Config
 */
@Serializable
data class ConfigHolidayOffset(
    @SerialName("offset_description") val offsetDescription: String = "",

    @SerialName("offset_eid_al_adha") val offsetEidAlAdha: Int = 0,

    @SerialName("offset_eid_al_fitr") val offsetEidAlFitr: Int = 0,

    @SerialName("offset_mawlid") val offsetMawlid: Int = 0,

    @SerialName("offset_ramadan_start") val offsetRamadanStart: Int = 0,

    @SerialName("offset_ethio_year") val offsetEthioYear: Int = 0,

    @SerialName("offset_hirji_year") val offsetHirjiYear: Int = 0,

    @SerialName("offset_greg_year") val offsetGregYear: Int = 0,

    @SerialName("offset_stage") val offsetStage: String = "prod",

    @SerialName("offset_update_timestamp") val offsetUpdateTimestamp: Long = 0
)

/**
 * Manages Firebase Remote Config for Muslim holiday date offsets.
 *
 * This manager handles:
 * - Initialization of Firebase Remote Config
 * - Fetching and activating remote config values
 * - Syncing remote config values to local preferences
 *
 * Remote Config Keys:
 * - config_holiday_offset: JSON configuration for all holiday offsets
 */
@Singleton
class RemoteConfigManager @Inject constructor(
    private val settingsPreferences: SettingsPreferences
) {
    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    // JSON parser with lenient configuration to handle various JSON formats
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    companion object {
        // Remote Config Keys
        const val KEY_CONFIG_HOLIDAY_OFFSET = "config_holiday_offset"

        // Cache expiration time (in seconds)
        private const val CACHE_EXPIRATION_DEBUG = 60L // 1 minute for debug
        private const val CACHE_EXPIRATION_RELEASE = 6 * 3600L // 6 hours for release
    }

    fun initialize(isDebug: Boolean = false) {
        val configSettings = FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(if (isDebug) CACHE_EXPIRATION_DEBUG else CACHE_EXPIRATION_RELEASE).build()

        remoteConfig.setConfigSettingsAsync(configSettings)

        // Set default values from XML resource
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        // Fetch and activate config values
        fetchAndActivate()
    }

    fun fetchAndActivate() {
        coroutineScope.launch {
            try {
                val updated = remoteConfig.fetchAndActivate().await()

                if (updated) {
                    syncToPreferences()
                } else { // Still sync to ensure preferences are up to date
                    syncToPreferences()
                }
            } catch (e: Exception) {
            }
        }
    }

    /**
     * Sync Remote Config values to local preferences
     * Merges new configurations with existing ones by Ethiopian year
     */
    private suspend fun syncToPreferences() {
        try {
            val jsonString = remoteConfig.getString(KEY_CONFIG_HOLIDAY_OFFSET)

            if (jsonString.isNotEmpty()) {
                val incomingConfigs = json.decodeFromString<List<ConfigHolidayOffset>>(jsonString)

                // Get existing configs from DataStore
                val existingJsonString = settingsPreferences.holidayOffsetConfigJson.first()
                val existingConfigs = if (existingJsonString.isNotEmpty()) {
                    json.decodeFromString<List<ConfigHolidayOffset>>(existingJsonString)
                } else {
                    emptyList()
                }

                // Merge configs: incoming configs override existing ones with same ethio_year
                val mergedConfigs = mergeConfigurations(existingConfigs, incomingConfigs)

                // Store merged array back to DataStore
                val mergedJsonString = json.encodeToString<List<ConfigHolidayOffset>>(mergedConfigs)
                settingsPreferences.setHolidayOffsetConfigJson(mergedJsonString)
            }

        } catch (e: Exception) { // Silent fail - preferences will retain previous values
        }
    }

    /**
     * Merge two lists of configurations by Ethiopian year
     * Incoming configs override existing ones with the same ethio_year
     */
    private fun mergeConfigurations(
        existing: List<ConfigHolidayOffset>, incoming: List<ConfigHolidayOffset>
    ): List<ConfigHolidayOffset> { // Create a map of existing configs by ethio_year for quick lookup
        val existingMap = existing.associateBy { it.offsetEthioYear }

        // Update with incoming configs
        val mergedMap = existingMap.toMutableMap()
        for (incomingConfig in incoming) {
            mergedMap[incomingConfig.offsetEthioYear] = incomingConfig
        }

        // Return as list, sorted by year for consistency
        return mergedMap.values.sortedBy { it.offsetEthioYear }
    }

    /**
     * Get the holiday offset configuration from Remote Config
     */
    fun getConfigHolidayOffset(): ConfigHolidayOffset {
        return try {
            val jsonString = remoteConfig.getString(KEY_CONFIG_HOLIDAY_OFFSET)
            if (jsonString.isNotEmpty()) {
                json.decodeFromString<ConfigHolidayOffset>(jsonString)
            } else {
                ConfigHolidayOffset() // Return default if empty
            }
        } catch (e: Exception) {
            ConfigHolidayOffset() // Return default on error
        }
    }
}
