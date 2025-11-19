package com.shalom.calendar.data.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.shalom.calendar.BuildConfig
import com.shalom.calendar.data.preferences.SettingsPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Centralized analytics manager for tracking user events and properties.
 * Provides type-safe event logging with Firebase Analytics.
 *
 * @param firebaseAnalytics Firebase Analytics instance
 * @param settingsPreferences App settings for analytics opt-in/out
 */
@Singleton
class AnalyticsManager @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val settingsPreferences: SettingsPreferences
) {
    private val analyticsScope = CoroutineScope(Dispatchers.IO)

    init {
        // Enable debug logging in debug builds
        if (BuildConfig.DEBUG) {
            Timber.d("AnalyticsManager initialized")
        }
    }

    /**
     * Log a screen view event
     * @param screenName Name of the screen
     * @param screenClass Class name of the screen composable
     * @param previousScreen Optional previous screen name
     */
    fun logScreenView(
        screenName: String,
        screenClass: String,
        previousScreen: String? = null
    ) {
        logEventInternal(AnalyticsEvent.ScreenView(screenName, screenClass, previousScreen))
    }

    /**
     * Log an analytics event
     * @param event The event to log (must be subclass of AnalyticsEvent)
     */
    fun logEvent(event: AnalyticsEvent) {
        logEventInternal(event)
    }

    /**
     * Set a user property
     * @param property Property name
     * @param value Property value
     */
    fun setUserProperty(property: String, value: String?) {
        analyticsScope.launch {
            try {
                val analyticsEnabled = settingsPreferences.analyticsEnabled.first()
                if (!analyticsEnabled) return@launch

                firebaseAnalytics.setUserProperty(property, value)
                if (BuildConfig.DEBUG) {
                    Timber.d("User property set: $property = $value")
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to set user property: $property")
            }
        }
    }

    /**
     * Set multiple user properties at once
     * @param properties Map of property name to value
     */
    fun setUserProperties(properties: Map<String, String?>) {
        properties.forEach { (key, value) ->
            setUserProperty(key, value)
        }
    }

    /**
     * Enable or disable analytics collection
     * @param enabled True to enable, false to disable
     */
    suspend fun setAnalyticsEnabled(enabled: Boolean) {
        try {
            settingsPreferences.setAnalyticsEnabled(enabled)
            firebaseAnalytics.setAnalyticsCollectionEnabled(enabled)
            Timber.d("Analytics collection ${if (enabled) "enabled" else "disabled"}")
        } catch (e: Exception) {
            Timber.e(e, "Failed to set analytics enabled state")
        }
    }

    /**
     * Internal method to log events with analytics enabled check
     */
    private fun logEventInternal(event: AnalyticsEvent) {
        analyticsScope.launch {
            try {
                val analyticsEnabled = settingsPreferences.analyticsEnabled.first()
                if (!analyticsEnabled) return@launch

                firebaseAnalytics.logEvent(event.eventName, event.parameters)

                if (BuildConfig.DEBUG) {
                    Timber.d("Analytics event logged: ${event.eventName} with ${event.parameters.size()} parameters")
                    event.parameters.keySet().forEach { key ->
                        Timber.d("  - $key: ${event.parameters.get(key)}")
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to log event: ${event.eventName}")
            }
        }
    }
}

/**
 * Helper function to create a Bundle from vararg pairs
 */
fun bundleOf(vararg pairs: Pair<String, Any?>): Bundle {
    return Bundle().apply {
        pairs.forEach { (key, value) ->
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Double -> putDouble(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                null -> putString(key, null)
                else -> putString(key, value.toString())
            }
        }
    }
}
