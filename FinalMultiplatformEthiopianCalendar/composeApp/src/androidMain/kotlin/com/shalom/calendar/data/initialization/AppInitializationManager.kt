package com.shalom.calendar.data.initialization

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.shalom.calendar.BuildConfig
import com.shalom.calendar.data.analytics.AnalyticsEvent
import com.shalom.calendar.data.analytics.AnalyticsManager
import com.shalom.calendar.data.analytics.AnalyticsParams
import com.shalom.calendar.data.local.CalendarDatabase
import com.shalom.calendar.data.migration.LegacyDataMigrator
import com.shalom.calendar.data.migration.SharedPreferencesReader
import com.shalom.calendar.data.preferences.CalendarType
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.data.remote.RemoteConfigManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private const val LAST_VERSION_OF_LEGACY_APP = 96

/**
 * Manages app initialization logic on launch.
 * Handles first-time setup, version upgrades, locale configuration,
 * Firebase services initialization, and analytics logging.
 */
@Singleton
class AppInitializationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsPreferences: SettingsPreferences,
    private val remoteConfigManager: RemoteConfigManager,
    private val database: CalendarDatabase,
    private val legacyDataMigrator: LegacyDataMigrator,
    private val analyticsManager: AnalyticsManager
) {
    private val firebaseAnalytics: FirebaseAnalytics by lazy { Firebase.analytics }
    private val applicationScope = CoroutineScope(Dispatchers.IO)

    /**
     * Main initialization entry point.
     * Should be called from Application.onCreate()
     */
    fun initialize() {
        applicationScope.launch {
            try {
                val storedVersionCode = settingsPreferences.versionCode.first()
                val currentVersionCode = BuildConfig.VERSION_CODE
                val currentVersionName = BuildConfig.VERSION_NAME

                // Check old SharedPreferences for upgrade detection
                // Old Java app used SharedPreferences, new app uses DataStore
                val sharedPrefsReader = SharedPreferencesReader(context)
                val oldVersionCode = if (storedVersionCode == -1) {
                    sharedPrefsReader.getOldAppVersionCode()
                } else {
                    null
                }

                val effectiveOldVersionCode = oldVersionCode ?: storedVersionCode

                when {
                    effectiveOldVersionCode == -1 -> {
                        handleFirstTimeSetup(currentVersionCode, currentVersionName)
                    }

                    effectiveOldVersionCode < currentVersionCode -> {
                        handleVersionUpgrade(effectiveOldVersionCode, currentVersionCode, currentVersionName)
                    }

                    else -> {
                        handleNormalLaunch()
                    }
                }

                initializeLocaleSettings()
                settingsPreferences.setLastUsedTimestamp(System.currentTimeMillis())
                logLaunchEvent()

            } catch (e: Exception) {
                Log.e("CheckNotification","Error during app initialization",e)
            }
        }
    }

    /**
     * First-time setup for new installations.
     */
    private suspend fun handleFirstTimeSetup(versionCode: Int, versionName: String) {
        try {
            settingsPreferences.setPrimaryCalendar(CalendarType.ETHIOPIAN)
            settingsPreferences.setSecondaryCalendar(CalendarType.GREGORIAN)
            settingsPreferences.setDisplayDualCalendar(false)
            settingsPreferences.setVersionCode(versionCode)
            settingsPreferences.setVersionName(versionName)
            settingsPreferences.setIsFirstRun(false)
            settingsPreferences.setAppFirstLaunchTimestamp(System.currentTimeMillis())

            // Check for legacy data migration
            if (versionCode >= LAST_VERSION_OF_LEGACY_APP) {
                handleLegacyDataMigration()
            }

            try {
                val installationId = FirebaseInstallations.getInstance().id.await()
                settingsPreferences.setFirebaseInstallationId(installationId)
                Log.e("CheckNotification", "TAG installationId $installationId")

            } catch (e: Exception) {
                Log.e("CheckNotification","Failed to get Firebase Installation ID",e)
            }

            subscribeToFCMTopics(listOf(
                "general",
                "holiday-updates",
                "Version$versionCode"  // Version-specific topic for targeted notifications
            ))

            firebaseAnalytics.logEvent("app_first_install", Bundle().apply {
                putInt("user_type", versionCode)
                putString("screen_count", versionName)
            })

        } catch (e: Exception) {
            Log.e("CheckNotification","Error in first-time setup")
        }
    }

    /**
     * Version upgrade handling.
     */
    private suspend fun handleVersionUpgrade(
        oldVersionCode: Int, newVersionCode: Int, newVersionName: String
    ) {
        try {
            runMigrations(oldVersionCode, newVersionCode)

            // Trigger legacy data migration for old Java app upgrades
            if (oldVersionCode < LAST_VERSION_OF_LEGACY_APP && newVersionCode >= LAST_VERSION_OF_LEGACY_APP) {
                handleLegacyDataMigration()
            }

            if (oldVersionCode < getVersionCodeWhenMuslimHolidaysAdded()) {
                handleMuslimHolidaysMigration()
            }

            // Reset notification permission flag on upgrade
            val currentPermissionRequested = settingsPreferences.notificationPermissionRequested.first()
            if (currentPermissionRequested) {
                settingsPreferences.setNotificationPermissionRequested(false)
            }

            // Unsubscribe from old version topic and subscribe to new version topic
            unsubscribeFromFCMTopic("Version$oldVersionCode")
            subscribeToFCMTopics(listOf(
                "general",
                "holiday-updates",
                "Version$newVersionCode"  // Subscribe to new version-specific topic
            ))

            settingsPreferences.setVersionCode(newVersionCode)
            settingsPreferences.setVersionName(newVersionName)

            firebaseAnalytics.logEvent("app_upgraded", Bundle().apply {
                putLong("old_version_code", oldVersionCode.toLong())
                putLong("new_version_code", newVersionCode.toLong())
                putString("new_version_name", newVersionName)
            })

        } catch (e: Exception) {
            Log.e("CheckNotification","Error in version upgrade",e)
        }
    }

    /**
     * Handle normal app launch (not first run or upgrade)
     */
    private fun handleNormalLaunch() {
        try { // Refresh Firebase Remote Config in background
            applicationScope.launch {
                try {
                    remoteConfigManager.initialize(BuildConfig.DEBUG)
                } catch (e: Exception) {
                }
            }
        } catch (e: Exception) {
        }
    }

    /**
     * 1.1 Locale & Chronology Initialization
     */
    private suspend fun initializeLocaleSettings() {
        try {
            val storedPrimaryLocale = settingsPreferences.primaryLocale.first()
            val storedSecondaryLocale = settingsPreferences.secondaryLocale.first()

            // If no preference exists (first run)
            if (storedPrimaryLocale.isEmpty() || storedSecondaryLocale.isEmpty()) {
                val deviceLocale = getDeviceLocale()
                val countryCode = getDeviceCountryCode()

                // 1.1.2 Set locales based on device country
                val (primaryLocale, secondaryLocale) = when {
                    isDeviceInEthiopia(countryCode) -> { // User is in Ethiopia
                        Pair("Africa/Addis_Ababa", "America/New_York")
                    }

                    else -> { // User is NOT in Ethiopia
                        Pair("Africa/Addis_Ababa", deviceLocale)
                    }
                }

                settingsPreferences.setPrimaryLocale(primaryLocale)
                settingsPreferences.setSecondaryLocale(secondaryLocale)
                settingsPreferences.setDeviceCountryCode(countryCode)
            }
        } catch (e: Exception) {
        }
    }

    /**
     * 1.3.2 Handle Muslim holidays migration for Arabic-speaking countries
     */
    private suspend fun handleMuslimHolidaysMigration() {
        try {
            val countryCode = settingsPreferences.deviceCountryCode.first()

            if (isArabicSpeakingCountry(countryCode)) { // Enable Muslim holidays toggle but keep it hidden
                settingsPreferences.setShowMuslimHolidays(false)
            }
        } catch (e: Exception) {
        }
    }

    /**
     * Handle migration from old Java app to new Compose app.
     * Migrates events from old database (EthiopianCalendar.db) to new Room database.
     * Runs silently in the background.
     */
    private suspend fun handleLegacyDataMigration() {
        applicationScope.launch {
            try {
                if (!legacyDataMigrator.shouldAttemptMigration()) {
                    return@launch
                }

                val result = legacyDataMigrator.migrateData()

                firebaseAnalytics.logEvent("legacy_data_migration", Bundle().apply {
                    putInt("success_count", result.successCount)
                    putInt("failure_count", result.failureCount)
                    putInt("total_count", result.totalProcessed)
                    putBoolean("is_success", result.isSuccess)
                })

                if (result.isSuccess && result.successCount > 0) {
                    legacyDataMigrator.deleteOldDatabase()
                }

            } catch (e: Exception) {
                Log.e("CheckNotification","Legacy data migration failed",e)
                firebaseAnalytics.logEvent("legacy_migration_error", Bundle().apply {
                    putString("error_message", e.message ?: "Unknown error")
                })
            }
        }
    }

    /**
     * 1.3.1 Run database and preference migrations
     */
    private fun runMigrations(oldVersionCode: Int, newVersionCode: Int) {
        try { // Add specific migrations as needed when schema changes
            // Example:
            // if (oldVersionCode < 2) {
            //     // Migration for version 2
            // }

            // Database migrations are handled by Room Migration objects
            // This function is for preference/settings migrations

        } catch (e: Exception) {
        }
    }

    /**
     * 1.2.4 Subscribe to Firebase Cloud Messaging topics
     */
    private fun subscribeToFCMTopics(topics: List<String>) {
        applicationScope.launch {
            try {
                Log.e("CheckNotification", "===== SUBSCRIBING TO FCM TOPICS =====")
                Log.e("CheckNotification", "Topics to subscribe: $topics")

                val messaging = FirebaseMessaging.getInstance()
                topics.forEach { topic ->
                    Log.e("CheckNotification", "Subscribing to topic: $topic...")
                    messaging.subscribeToTopic(topic).await()
                    Log.e("CheckNotification", "✅ Successfully subscribed to topic: $topic")
                }

                Log.e("CheckNotification", "✅ ✅ ALL TOPICS SUBSCRIBED SUCCESSFULLY ✅ ✅")
                Log.e("CheckNotification", "You should now receive notifications sent to these topics")
            } catch (e: Exception) {
                Log.e("CheckNotification", "❌ ❌ FAILED TO SUBSCRIBE TO FCM TOPICS ❌ ❌")
                Log.e("CheckNotification", "Error: ${e.message}")
                Log.e("CheckNotification", "Stack trace:")
                e.printStackTrace()
            }
        }
    }

    /**
     * Unsubscribe from a specific Firebase Cloud Messaging topic.
     * Used when upgrading app versions to remove old version-specific subscriptions.
     */
    private fun unsubscribeFromFCMTopic(topic: String) {
        applicationScope.launch {
            try {
                Log.e("CheckNotification", "Unsubscribing from old topic: $topic")
                FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic(topic)
                    .await()
                Log.e("CheckNotification", "✅ Successfully unsubscribed from topic: $topic")
            } catch (e: Exception) {
                Log.e("CheckNotification", "❌ Failed to unsubscribe from topic: $topic")
                Log.e("CheckNotification", "Error: ${e.message}")
            }
        }
    }

    /**
     * 1.6 Analytics and Usage Logging
     */
    private suspend fun logLaunchEvent() {
        try {
            settingsPreferences.primaryCalendar.first()
            settingsPreferences.secondaryCalendar.first()
            settingsPreferences.deviceCountryCode.first()
            getDeviceLocale()

            firebaseAnalytics.logEvent("app_launch", Bundle().apply {
                putString("app_version", BuildConfig.VERSION_NAME)
                putLong("version_code", BuildConfig.VERSION_CODE.toLong()) //                param("device_locale", deviceLocale)
                //                param("primary_calendar", primaryCalendar.name)
                //                param("secondary_calendar", secondaryCalendar.name)
                //                param("country", country)
                putLong("android_version", Build.VERSION.SDK_INT.toLong())
            })

        } catch (e: Exception) {
        }
    }

    // Helper functions

    private fun getDeviceLocale(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0].toString()
        } else {
            @Suppress("DEPRECATION") context.resources.configuration.locale.toString()
        }
    }

    private fun getDeviceCountryCode(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0].country
        } else {
            @Suppress("DEPRECATION") context.resources.configuration.locale.country
        }
    }

    private fun isDeviceInEthiopia(countryCode: String): Boolean {
        return countryCode.equals("ET", ignoreCase = true)
    }

    private fun isArabicSpeakingCountry(countryCode: String): Boolean {
        val arabicCountries = setOf("SA", "EG", "DZ", "SD", "IQ", "MA", "YE", "SY", "TN", "JO", "AE", "LB", "LY", "OM", "KW", "MR", "QA", "BH", "DJ", "SO")
        return arabicCountries.contains(countryCode.uppercase())
    }

    private fun getVersionCodeWhenMuslimHolidaysAdded(): Int { // Return the version code when Muslim holidays feature was added
        // This can be updated when the feature is actually added
        return 1 // Placeholder - update with actual version
    }
}
