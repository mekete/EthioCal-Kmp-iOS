package com.shalom.calendar.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "settings_preferences")

data class HolidayPreferences(
    val includeAllDayOffHolidays: Boolean = true, val includeWorkingOrthodoxHolidays: Boolean = true, val includeWorkingMuslimHolidays: Boolean = false, //
    val includeWorkingWesternHolidays: Boolean = false, val includeWorkingCulturalHolidays: Boolean = false
)

enum class CalendarType {
    ETHIOPIAN, GREGORIAN, HIRJI
}

enum class Language(val displayName: String) {
    ENGLISH("English"), AMHARIC("አማርኛ (Amharic)"), OROMIFFA("Afan Oromoo"), TIGRIGNA("ትግርኛ (Tigrigna)"), FRENCH("French")
}

/**
 * Migration status for tracking legacy data migration from old Java app.
 * Used when upgrading from versionCode < 96 to versionCode >= 96.
 */
enum class MigrationStatus {
    NOT_CHECKED,    // Migration hasn't been attempted yet
    IN_PROGRESS,    // Migration is currently running
    COMPLETED,      // Migration successfully completed
    FAILED,         // Migration failed (will retry on next launch)
    NO_OLD_DATA     // No old database found to migrate
}

class SettingsPreferences(private val context: Context) {

    // App Version and First-Run Settings
    private val VERSION_CODE_KEY = intPreferencesKey("app_version_code")
    private val VERSION_NAME_KEY = stringPreferencesKey("app_version_name")
    private val IS_FIRST_RUN_KEY = booleanPreferencesKey("is_first_run")
    private val HAS_COMPLETED_ONBOARDING_KEY = booleanPreferencesKey("has_completed_onboarding")
    private val LAST_USED_TIMESTAMP_KEY = longPreferencesKey("last_used_timestamp")
    private val FIREBASE_INSTALLATION_ID_KEY = stringPreferencesKey("firebase_installation_id")
    private val NOTIFICATION_PERMISSION_REQUESTED_KEY = booleanPreferencesKey("notification_permission_requested")
    private val NOTIFICATION_PERMISSION_DONT_ASK_UNTIL_KEY = longPreferencesKey("notification_permission_dont_ask_until")
    private val NOTIFICATION_BANNER_DISMISSED_KEY = booleanPreferencesKey("notification_banner_dismissed")
    private val APP_FIRST_LAUNCH_TIMESTAMP_KEY = longPreferencesKey("app_first_launch_timestamp")

    // Migration Settings (for migrating from old Java app to new Compose app)
    private val MIGRATION_STATUS_KEY = stringPreferencesKey("legacy_migration_status")
    private val MIGRATION_COMPLETED_AT_KEY = longPreferencesKey("migration_completed_at")
    private val MIGRATION_EVENT_COUNT_KEY = intPreferencesKey("migration_event_count")

    // Locale Settings
    private val PRIMARY_LOCALE_KEY = stringPreferencesKey("primary_locale")
    private val SECONDARY_LOCALE_KEY = stringPreferencesKey("secondary_locale")
    private val DEVICE_COUNTRY_CODE_KEY = stringPreferencesKey("device_country_code")

    // Calendar Display Settings
    private val PRIMARY_CALENDAR_KEY = stringPreferencesKey("primary_calendar")
    private val DISPLAY_DUAL_CALENDAR_KEY = booleanPreferencesKey("display_dual_calendar")
    private val SECONDARY_CALENDAR_KEY = stringPreferencesKey("secondary_calendar")

    //
    private val SHOW_DAY_OFF_HOLIDAYS_KEY = booleanPreferencesKey("show_public_holidays")
    private val SHOW_WORKING_ORTHODOX_HOLIDAYS_KEY = booleanPreferencesKey("show_orthodox_fasting_holidays")
    private val SHOW_WORKING_MUSLIM_HOLIDAYS_KEY = booleanPreferencesKey("show_muslim_holidays")
    private val SHOW_WORKING_CULTURAL_HOLIDAYS_KEY = booleanPreferencesKey("show_cultural_holidays")
    private val SHOW_WORKING_WESTERN_HOLIDAYS_KEY = booleanPreferencesKey("show_working_western_holidays")

    //
    private val SHOW_ORTHODOX_DAY_NAMES_KEY = booleanPreferencesKey("show_orthodox_day_names")
    private val USE_GEEZ_NUMBERS_KEY = booleanPreferencesKey("use_geez_numbers")
    private val USE_24_HOUR_FORMAT_KEY = booleanPreferencesKey("use_24_hour_format_in_widgets")
    private val LANGUAGE_KEY = stringPreferencesKey("app_language")

    // Widget Settings
    private val DISPLAY_TWO_CLOCKS_KEY = booleanPreferencesKey("display_two_clocks")
    private val PRIMARY_WIDGET_TIMEZONE_KEY = stringPreferencesKey("primary_widget_timezone")
    private val SECONDARY_WIDGET_TIMEZONE_KEY = stringPreferencesKey("secondary_widget_timezone")
    private val USE_TRANSPARENT_BACKGROUND_KEY = booleanPreferencesKey("use_transparent_background")

    // Holiday Info Dialog Settings
    private val HIDE_HOLIDAY_INFO_DIALOG_KEY = booleanPreferencesKey("hide_holiday_info_dialog")

    // Analytics Settings
    private val ANALYTICS_ENABLED_KEY = booleanPreferencesKey("analytics_enabled")

    // Muslim Holiday Offset Settings (from Firebase Remote Config)
    private val DAY_OFFSET_EID_AL_ADHA_KEY = intPreferencesKey("config_day_offset_eid_al_adha")
    private val DAY_OFFSET_EID_AL_FITR_KEY = intPreferencesKey("config_day_offset_eid_al_fitir")
    private val DAY_OFFSET_MAWLID_KEY = intPreferencesKey("config_day_offset_mewlid")
    private val DAY_OFFSET_ETHIO_YEAR_KEY = intPreferencesKey("config_day_offset_ethio_year")

    // Consolidated Holiday Offset Config (stores entire JSON)
    private val HOLIDAY_OFFSET_CONFIG_JSON_KEY = stringPreferencesKey("config_holiday_offset_json")

    // Flow properties for app version and first-run
    val versionCode: Flow<Int> = context.settingsDataStore.data.map { preferences ->
        preferences[VERSION_CODE_KEY] ?: -1
    }

    val versionName: Flow<String> = context.settingsDataStore.data.map { preferences ->
        preferences[VERSION_NAME_KEY] ?: ""
    }

    val isFirstRun: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[IS_FIRST_RUN_KEY] ?: true
    }

    val hasCompletedOnboarding: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[HAS_COMPLETED_ONBOARDING_KEY] ?: false
    }

    val lastUsedTimestamp: Flow<Long> = context.settingsDataStore.data.map { preferences ->
        preferences[LAST_USED_TIMESTAMP_KEY] ?: 0L
    }

    val firebaseInstallationId: Flow<String> = context.settingsDataStore.data.map { preferences ->
        preferences[FIREBASE_INSTALLATION_ID_KEY] ?: ""
    }

    val notificationPermissionRequested: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[NOTIFICATION_PERMISSION_REQUESTED_KEY] ?: false
    }

    val notificationPermissionDontAskUntil: Flow<Long> = context.settingsDataStore.data.map { preferences ->
        preferences[NOTIFICATION_PERMISSION_DONT_ASK_UNTIL_KEY] ?: 0L
    }

    val notificationBannerDismissed: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[NOTIFICATION_BANNER_DISMISSED_KEY] ?: false
    }

    val appFirstLaunchTimestamp: Flow<Long> = context.settingsDataStore.data.map { preferences ->
        preferences[APP_FIRST_LAUNCH_TIMESTAMP_KEY] ?: 0L
    }

    // Flow properties for migration status
    val migrationStatus: Flow<MigrationStatus> = context.settingsDataStore.data.map { preferences ->
        val status = preferences[MIGRATION_STATUS_KEY] ?: MigrationStatus.NOT_CHECKED.name
        try {
            MigrationStatus.valueOf(status)
        } catch (e: IllegalArgumentException) {
            MigrationStatus.NOT_CHECKED
        }
    }

    val migrationCompletedAt: Flow<Long> = context.settingsDataStore.data.map { preferences ->
        preferences[MIGRATION_COMPLETED_AT_KEY] ?: 0L
    }

    val migrationEventCount: Flow<Int> = context.settingsDataStore.data.map { preferences ->
        preferences[MIGRATION_EVENT_COUNT_KEY] ?: 0
    }

    // Flow properties for locale settings
    val primaryLocale: Flow<String> = context.settingsDataStore.data.map { preferences ->
        preferences[PRIMARY_LOCALE_KEY] ?: ""
    }

    val secondaryLocale: Flow<String> = context.settingsDataStore.data.map { preferences ->
        preferences[SECONDARY_LOCALE_KEY] ?: ""
    }

    val deviceCountryCode: Flow<String> = context.settingsDataStore.data.map { preferences ->
        preferences[DEVICE_COUNTRY_CODE_KEY] ?: ""
    }

    // Flow properties for observing settings
    val primaryCalendar: Flow<CalendarType> = context.settingsDataStore.data.map { preferences ->
        val calendarString = preferences[PRIMARY_CALENDAR_KEY] ?: CalendarType.ETHIOPIAN.name
        try {
            CalendarType.valueOf(calendarString)
        } catch (e: IllegalArgumentException) {
            CalendarType.ETHIOPIAN
        }
    }

    val displayDualCalendar: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[DISPLAY_DUAL_CALENDAR_KEY] ?: false
    }

    val secondaryCalendar: Flow<CalendarType> = context.settingsDataStore.data.map { preferences ->
        val calendarString = preferences[SECONDARY_CALENDAR_KEY] ?: CalendarType.GREGORIAN.name
        try {
            CalendarType.valueOf(calendarString)
        } catch (e: IllegalArgumentException) {
            CalendarType.GREGORIAN
        }
    }

    val showOrthodoxDayNames: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[SHOW_ORTHODOX_DAY_NAMES_KEY] ?: false
    }

    val includeAllDayOffHolidays: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[SHOW_DAY_OFF_HOLIDAYS_KEY] ?: true
    }

    val includeWorkingOrthodoxHolidays: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[SHOW_WORKING_ORTHODOX_HOLIDAYS_KEY] ?: false
    }

    val includeWorkingMuslimHolidays: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[SHOW_WORKING_MUSLIM_HOLIDAYS_KEY] ?: false
    }

    val includeWorkingCulturalHolidays: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[SHOW_WORKING_CULTURAL_HOLIDAYS_KEY] ?: false
    }

    val includeWorkingWesternHolidays: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[SHOW_WORKING_WESTERN_HOLIDAYS_KEY] ?: false
    }

    val useGeezNumbers: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[USE_GEEZ_NUMBERS_KEY] ?: false
    }

    val use24HourFormat: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[USE_24_HOUR_FORMAT_KEY] ?: false
    }

    val displayTwoClocks: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[DISPLAY_TWO_CLOCKS_KEY] ?: true
    }

    val primaryWidgetTimezone: Flow<String> = context.settingsDataStore.data.map { preferences ->
        preferences[PRIMARY_WIDGET_TIMEZONE_KEY] ?: ""
    }

    val secondaryWidgetTimezone: Flow<String> = context.settingsDataStore.data.map { preferences ->
        preferences[SECONDARY_WIDGET_TIMEZONE_KEY] ?: ""
    }

    val useTransparentBackground: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[USE_TRANSPARENT_BACKGROUND_KEY] ?: false
    }

    val language: Flow<Language> = context.settingsDataStore.data.map { preferences ->
        val languageString = preferences[LANGUAGE_KEY] ?: Language.AMHARIC.name
        try {
            Language.valueOf(languageString)
        } catch (e: IllegalArgumentException) {
            Language.AMHARIC
        }
    }

    val hideHolidayInfoDialog: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[HIDE_HOLIDAY_INFO_DIALOG_KEY] ?: false
    }

    // Analytics Flow
    val analyticsEnabled: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[ANALYTICS_ENABLED_KEY] ?: true // Default: enabled
    }

    // Muslim Holiday Offset Flows
    val dayOffsetEidAlAdha: Flow<Int> = context.settingsDataStore.data.map { preferences ->
        preferences[DAY_OFFSET_EID_AL_ADHA_KEY] ?: 0
    }

    val dayOffsetEidAlFitr: Flow<Int> = context.settingsDataStore.data.map { preferences ->
        preferences[DAY_OFFSET_EID_AL_FITR_KEY] ?: 0
    }

    val dayOffsetMawlid: Flow<Int> = context.settingsDataStore.data.map { preferences ->
        preferences[DAY_OFFSET_MAWLID_KEY] ?: 0
    }

    val dayOffsetEthioYear: Flow<Int> = context.settingsDataStore.data.map { preferences ->
        preferences[DAY_OFFSET_ETHIO_YEAR_KEY] ?: 0
    }

    // Consolidated Holiday Offset Config Flow (returns JSON string)
    val holidayOffsetConfigJson: Flow<String> = context.settingsDataStore.data.map { preferences ->
        preferences[HOLIDAY_OFFSET_CONFIG_JSON_KEY] ?: ""
    }

    // Combined Holiday Preferences Flow
    val holidayPreferences: Flow<HolidayPreferences> = combine(includeAllDayOffHolidays, includeWorkingOrthodoxHolidays, includeWorkingMuslimHolidays, includeWorkingWesternHolidays, includeWorkingCulturalHolidays) { allDayOffHolidaysHolidays, orthodoxWorkingHolidays, muslimWorkingHolidays, westernWorkingHolidays, culturalWorkingHolidays ->
        HolidayPreferences(includeAllDayOffHolidays = allDayOffHolidaysHolidays, includeWorkingOrthodoxHolidays = orthodoxWorkingHolidays, includeWorkingMuslimHolidays = muslimWorkingHolidays, includeWorkingWesternHolidays = westernWorkingHolidays, includeWorkingCulturalHolidays = culturalWorkingHolidays)
    }

    // Setter functions for app version and first-run
    suspend fun setVersionCode(versionCode: Int) {
        context.settingsDataStore.edit { preferences ->
            preferences[VERSION_CODE_KEY] = versionCode
        }
    }

    suspend fun setVersionName(versionName: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[VERSION_NAME_KEY] = versionName
        }
    }

    suspend fun setIsFirstRun(isFirstRun: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[IS_FIRST_RUN_KEY] = isFirstRun
        }
    }

    suspend fun setHasCompletedOnboarding(completed: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[HAS_COMPLETED_ONBOARDING_KEY] = completed
        }
    }

    suspend fun setLastUsedTimestamp(timestamp: Long) {
        context.settingsDataStore.edit { preferences ->
            preferences[LAST_USED_TIMESTAMP_KEY] = timestamp
        }
    }

    suspend fun setFirebaseInstallationId(id: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[FIREBASE_INSTALLATION_ID_KEY] = id
        }
    }

    suspend fun setNotificationPermissionRequested(requested: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[NOTIFICATION_PERMISSION_REQUESTED_KEY] = requested
        }
    }

    suspend fun setNotificationPermissionDontAskUntil(timestamp: Long) {
        context.settingsDataStore.edit { preferences ->
            preferences[NOTIFICATION_PERMISSION_DONT_ASK_UNTIL_KEY] = timestamp
        }
    }

    suspend fun setNotificationBannerDismissed(dismissed: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[NOTIFICATION_BANNER_DISMISSED_KEY] = dismissed
        }
    }

    suspend fun setAppFirstLaunchTimestamp(timestamp: Long) {
        context.settingsDataStore.edit { preferences ->
            preferences[APP_FIRST_LAUNCH_TIMESTAMP_KEY] = timestamp
        }
    }

    // Setter functions for migration status
    suspend fun setMigrationStatus(status: MigrationStatus, eventCount: Int = 0) {
        context.settingsDataStore.edit { preferences ->
            preferences[MIGRATION_STATUS_KEY] = status.name
            if (status == MigrationStatus.COMPLETED) {
                preferences[MIGRATION_COMPLETED_AT_KEY] = System.currentTimeMillis()
                preferences[MIGRATION_EVENT_COUNT_KEY] = eventCount
            }
        }
    }

    // Setter functions for locale settings
    suspend fun setPrimaryLocale(locale: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[PRIMARY_LOCALE_KEY] = locale
        }
    }

    suspend fun setSecondaryLocale(locale: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[SECONDARY_LOCALE_KEY] = locale
        }
    }

    suspend fun setDeviceCountryCode(countryCode: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[DEVICE_COUNTRY_CODE_KEY] = countryCode
        }
    }

    // Setter functions for updating settings
    suspend fun setPrimaryCalendar(calendar: CalendarType) {
        context.settingsDataStore.edit { preferences ->
            preferences[PRIMARY_CALENDAR_KEY] = calendar.name
        }
    }

    suspend fun setDisplayDualCalendar(value: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[DISPLAY_DUAL_CALENDAR_KEY] = value
        }
    }

    suspend fun setSecondaryCalendar(calendar: CalendarType) {
        context.settingsDataStore.edit { preferences ->
            preferences[SECONDARY_CALENDAR_KEY] = calendar.name
        }
    }

    suspend fun setShowOrthodoxDayNames(value: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SHOW_ORTHODOX_DAY_NAMES_KEY] = value
        }
    }

    suspend fun setShowPublicHolidays(value: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SHOW_DAY_OFF_HOLIDAYS_KEY] = value
        }
    }

    suspend fun setShowOrthodoxFastingHolidays(value: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SHOW_WORKING_ORTHODOX_HOLIDAYS_KEY] = value
        }
    }

    suspend fun setShowMuslimHolidays(value: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SHOW_WORKING_MUSLIM_HOLIDAYS_KEY] = value
        }
    }

    suspend fun setShowCulturalHolidays(value: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SHOW_WORKING_CULTURAL_HOLIDAYS_KEY] = value
        }
    }

    suspend fun setShowUsHolidays(value: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SHOW_WORKING_WESTERN_HOLIDAYS_KEY] = value
        }
    }

    suspend fun setUseGeezNumbers(value: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[USE_GEEZ_NUMBERS_KEY] = value
        }
    }

    suspend fun setUse24HourFormat(value: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[USE_24_HOUR_FORMAT_KEY] = value
        }
    }

    suspend fun setDisplayTwoClocks(value: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[DISPLAY_TWO_CLOCKS_KEY] = value
        }
    }

    suspend fun setPrimaryWidgetTimezone(value: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[PRIMARY_WIDGET_TIMEZONE_KEY] = value
        }
    }

    suspend fun setSecondaryWidgetTimezone(value: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[SECONDARY_WIDGET_TIMEZONE_KEY] = value
        }
    }

    suspend fun setUseTransparentBackground(value: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[USE_TRANSPARENT_BACKGROUND_KEY] = value
        }
    }

    suspend fun setLanguage(language: Language) {
        context.settingsDataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language.name
        }
    }

    // Setter for consolidated Holiday Offset Config (stores entire JSON)
    suspend fun setHolidayOffsetConfigJson(jsonString: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[HOLIDAY_OFFSET_CONFIG_JSON_KEY] = jsonString
        }
    }

    suspend fun setHideHolidayInfoDialog(value: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[HIDE_HOLIDAY_INFO_DIALOG_KEY] = value
        }
    }

    // Setter for analytics enabled
    suspend fun setAnalyticsEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[ANALYTICS_ENABLED_KEY] = enabled
        }
    }

    // Helper function to check if 2 weeks passed since first launch
    suspend fun hasTwoWeeksPassedSinceFirstLaunch(): Boolean {
        val firstLaunch = appFirstLaunchTimestamp.first()
        if (firstLaunch == 0L) return false

        val twoWeeksInMillis = 14L * 24 * 60 * 60 * 1000 // 14 days
        return (System.currentTimeMillis() - firstLaunch) >= twoWeeksInMillis
    }
}
