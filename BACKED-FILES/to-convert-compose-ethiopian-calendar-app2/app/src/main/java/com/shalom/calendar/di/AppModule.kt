package com.shalom.calendar.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.shalom.calendar.data.analytics.AnalyticsManager
import com.shalom.calendar.data.initialization.AppInitializationManager
import com.shalom.calendar.data.initialization.ReminderReregistrationManager
import com.shalom.calendar.data.local.CalendarDatabase
import com.shalom.calendar.data.local.dao.EventDao
import com.shalom.calendar.data.migration.LegacyDataMigrator
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.data.preferences.ThemePreferences
import com.shalom.calendar.data.remote.RemoteConfigManager
import com.shalom.calendar.data.repository.EventRepository
import com.shalom.calendar.util.AppInfo
import com.shalom.calendar.util.ShareManager
import com.shalom.calendar.util.UrlLauncher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideThemePreferences(@ApplicationContext context: Context): ThemePreferences {
        return ThemePreferences(context)
    }

    @Provides
    @Singleton
    fun provideSettingsPreferences(@ApplicationContext context: Context): SettingsPreferences {
        return SettingsPreferences(context)
    }

    @Provides
    @Singleton
    fun provideRemoteConfigManager(settingsPreferences: SettingsPreferences): RemoteConfigManager {
        return RemoteConfigManager(settingsPreferences)
    }

    // ========== Room Database ==========

    @Provides
    @Singleton
    fun provideCalendarDatabase(@ApplicationContext context: Context): CalendarDatabase {
        return Room.databaseBuilder(context, CalendarDatabase::class.java, CalendarDatabase.DATABASE_NAME).fallbackToDestructiveMigration(false) // For development - replace with migrations in production
                .build()
    }

    @Provides
    @Singleton
    fun provideEventDao(database: CalendarDatabase): EventDao {
        return database.eventDao()
    }

    @Provides
    @Singleton
    fun provideEventRepository(eventDao: EventDao): EventRepository {
        return EventRepository(eventDao)
    }

    // ========== Initialization Managers ==========

    @Provides
    @Singleton
    fun provideAppInitializationManager(
        @ApplicationContext context: Context,
        settingsPreferences: SettingsPreferences,
        remoteConfigManager: RemoteConfigManager,
        database: CalendarDatabase,
        legacyDataMigrator: LegacyDataMigrator,
        analyticsManager: AnalyticsManager
    ): AppInitializationManager {
        return AppInitializationManager(
            context,
            settingsPreferences,
            remoteConfigManager,
            database,
            legacyDataMigrator,
            analyticsManager
        )
    }

    @Provides
    @Singleton
    fun provideReminderReregistrationManager(
        @ApplicationContext context: Context, eventDao: EventDao
    ): ReminderReregistrationManager {
        return ReminderReregistrationManager(context, eventDao)
    }

    // ========== Analytics ==========

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(): FirebaseAnalytics {
        return Firebase.analytics
    }

    @Provides
    @Singleton
    fun provideAnalyticsManager(
        firebaseAnalytics: FirebaseAnalytics,
        settingsPreferences: SettingsPreferences
    ): AnalyticsManager {
        return AnalyticsManager(firebaseAnalytics, settingsPreferences)
    }

    @Provides
    @Singleton
    fun providePermissionManager(
        @ApplicationContext context: Context,
        analyticsManager: AnalyticsManager
    ): com.shalom.calendar.data.permissions.PermissionManager {
        return com.shalom.calendar.data.permissions.PermissionManager(context, analyticsManager)
    }

    @Provides
    @Singleton
    fun provideUserPropertiesManager(
        analyticsManager: AnalyticsManager,
        settingsPreferences: SettingsPreferences,
        themePreferences: ThemePreferences,
        permissionManager: com.shalom.calendar.data.permissions.PermissionManager
    ): com.shalom.calendar.data.analytics.UserPropertiesManager {
        return com.shalom.calendar.data.analytics.UserPropertiesManager(
            analyticsManager,
            settingsPreferences,
            themePreferences,
            permissionManager
        )
    }

    // ========== Platform Utilities ==========

    @Provides
    @Singleton
    fun provideUrlLauncher(@ApplicationContext context: Context): UrlLauncher {
        return UrlLauncher(context)
    }

    @Provides
    @Singleton
    fun provideShareManager(@ApplicationContext context: Context): ShareManager {
        return ShareManager(context)
    }

    @Provides
    @Singleton
    fun provideAppInfo(@ApplicationContext context: Context): AppInfo {
        return AppInfo(context)
    }
}
