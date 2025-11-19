# ================================================================================================
# ProGuard Configuration for ComposeCalendarTwp
# ================================================================================================
# This configuration enables R8 full mode optimization while preserving all necessary classes
# for Hilt, Room, Firebase, Compose, WorkManager, Glance Widgets, and kotlinx.serialization
# ================================================================================================

# ================================================================================================
# HILT / DAGGER DEPENDENCY INJECTION
# ================================================================================================
# Keep all Hilt-generated classes and components
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class **_HiltComponents { *; }
-keep class **_HiltComponents$* { *; }
-keep class **_HiltModules { *; }
-keep class **_HiltModules$* { *; }
-keep class **_Factory { *; }
-keep class **_MembersInjector { *; }

# Keep classes annotated with @HiltAndroidApp, @AndroidEntryPoint, @HiltViewModel, @HiltWorker
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
-keep @androidx.hilt.work.HiltWorker class * { *; }

# Keep Hilt-injected constructors
-keepclasseswithmembers class * {
    @javax.inject.Inject <init>(...);
}

# Keep specific Hilt-annotated classes
-keep class com.shalom.calendar.CalendarApplication { *; }
-keep class com.shalom.calendar.MainActivity { *; }
-keep class com.shalom.calendar.di.AppModule { *; }
-keep class com.shalom.calendar.alarm.AlarmReceiver { *; }
-keep class com.shalom.calendar.receiver.BootCompleteReceiver { *; }

# Keep all ViewModels
-keep class com.shalom.calendar.ui.month.MonthCalendarViewModel { *; }
-keep class com.shalom.calendar.ui.event.EventViewModel { *; }
-keep class com.shalom.calendar.ui.holidaylist.CalendarItemListViewModel { *; }
-keep class com.shalom.calendar.ui.converter.DateConverterViewModel { *; }
-keep class com.shalom.calendar.ui.more.SettingsViewModel { *; }
-keep class com.shalom.calendar.ui.more.ThemeViewModel { *; }

# Keep all Hilt Workers
-keep class com.shalom.calendar.widget.DateChangeWorker { *; }
-keep class com.shalom.calendar.widget.CalendarWidgetWorker { *; }

# ================================================================================================
# ROOM DATABASE
# ================================================================================================
# Keep Room database and DAO classes
-keep class * extends androidx.room.RoomDatabase
-keep class androidx.room.RoomDatabase { *; }
-keep @androidx.room.Database class * { *; }
-keep @androidx.room.Dao class * { *; }
-keep @androidx.room.Entity class * { *; }

# Keep all Room entity classes and their fields
-keepclassmembers @androidx.room.Entity class * {
    <fields>;
    <init>(...);
}

# Keep TypeConverters
-keep @androidx.room.TypeConverter class * { *; }
-keep class * {
    @androidx.room.TypeConverter <methods>;
}

# Keep specific database classes
-keep class com.shalom.calendar.data.local.CalendarDatabase { *; }
-keep interface com.shalom.calendar.data.local.dao.EventDao { *; }
-keep class com.shalom.calendar.data.local.entity.** { *; }
-keep class com.shalom.calendar.data.local.converter.DateConverter { *; }

# Don't warn about Room paging
-dontwarn androidx.room.paging.**

# ================================================================================================
# KOTLINX SERIALIZATION
# ================================================================================================
# Keep serializable classes and their synthetic methods
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-dontwarn kotlinx.serialization.**

# Keep Serializers
-keep,includedescriptorclasses class com.shalom.calendar.**$$serializer { *; }
-keepclassmembers class com.shalom.calendar.** {
    *** Companion;
}
-keepclasseswithmembers class com.shalom.calendar.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep @Serializable classes
-keep @kotlinx.serialization.Serializable class * { *; }
-keepclassmembers @kotlinx.serialization.Serializable class * {
    <fields>;
    <init>(...);
}

# Keep specific serializable classes
-keep class com.shalom.calendar.widget.CalendarWidgetState { *; }
-keep class com.shalom.calendar.widget.WidgetEvent { *; }
-keep class com.shalom.calendar.widget.CalendarWidgetStateSerializer { *; }

# ================================================================================================
# FIREBASE
# ================================================================================================
# Keep Firebase Remote Config classes
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# Keep RemoteConfigManager
-keep class com.shalom.calendar.data.remote.RemoteConfigManager { *; }

# Keep Firebase Analytics and Crashlytics
-keepattributes SourceFile,LineNumberTable
-keep class com.google.firebase.analytics.** { *; }
-keep class com.google.firebase.crashlytics.** { *; }

# ================================================================================================
# JETPACK COMPOSE
# ================================================================================================
# Keep Compose runtime classes
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.foundation.** { *; }
-keep class androidx.compose.material3.** { *; }
-keep class androidx.compose.animation.** { *; }

# Keep Composable functions
-keep @androidx.compose.runtime.Composable class * { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Keep stable/immutable classes
-keep @androidx.compose.runtime.Stable class * { *; }
-keep @androidx.compose.runtime.Immutable class * { *; }

# Keep ViewModel classes
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# ================================================================================================
# GLANCE APP WIDGETS
# ================================================================================================
# Keep Glance widget classes
-keep class androidx.glance.** { *; }
-keep class * extends androidx.glance.appwidget.GlanceAppWidget { *; }
-keep class * extends androidx.glance.appwidget.GlanceAppWidgetReceiver { *; }

# Keep specific widget classes
-keep class com.shalom.calendar.widget.CalendarGlanceWidget { *; }
-keep class com.shalom.calendar.widget.CalendarWidgetReceiver { *; }
-keep class com.shalom.calendar.widget.CalendarWidgetStateDefinition { *; }
-keep class com.shalom.calendar.widget.DateChangeBroadcastReceiver { *; }

# ================================================================================================
# WORKMANAGER
# ================================================================================================
# Keep Worker classes
-keep class * extends androidx.work.Worker { *; }
-keep class * extends androidx.work.CoroutineWorker { *; }
-keep class * extends androidx.work.ListenableWorker { *; }

-keepclassmembers class * extends androidx.work.Worker {
    public <init>(android.content.Context,androidx.work.WorkerParameters);
}

-keepclassmembers class * extends androidx.work.CoroutineWorker {
    public <init>(android.content.Context,androidx.work.WorkerParameters);
}

# Keep WorkerFactory
-keep class androidx.work.WorkerFactory { *; }
-keep class androidx.work.Configuration$Provider { *; }

# ================================================================================================
# DATA MODELS & DOMAIN CLASSES
# ================================================================================================
# Keep all domain models
-keep class com.shalom.calendar.domain.model.** { *; }

# Keep data classes
-keepclassmembers class com.shalom.calendar.data.** {
    <fields>;
    <init>(...);
}

# Keep repositories
-keep class com.shalom.calendar.data.repository.EventRepository { *; }
-keep class com.shalom.calendar.data.repository.HolidayRepository { *; }

# Keep preferences
-keep class com.shalom.calendar.data.preferences.SettingsPreferences { *; }
-keep class com.shalom.calendar.data.preferences.ThemePreferences { *; }
-keep class com.shalom.calendar.data.preferences.HolidayPreferences { *; }

# Keep calculators
-keep class com.shalom.calendar.domain.calculator.** { *; }

# Keep initialization managers
-keep class com.shalom.calendar.data.initialization.AppInitializationManager { *; }
-keep class com.shalom.calendar.data.initialization.ReminderReregistrationManager { *; }

# ================================================================================================
# ENUMS
# ================================================================================================
# Keep all enums and their methods
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    **[] $VALUES;
    public *;
}

# Keep specific enums
-keep enum com.shalom.calendar.domain.model.HolidayType { *; }
-keep enum com.shalom.calendar.domain.model.EventCategory { *; }
-keep enum com.shalom.calendar.data.preferences.CalendarType { *; }
-keep enum com.shalom.calendar.data.preferences.Language { *; }
-keep enum com.shalom.calendar.data.local.entity.RecurrenceFrequency { *; }
-keep enum com.shalom.calendar.data.local.entity.RecurrenceEndOption { *; }
-keep enum com.shalom.calendar.ui.theme.AppTheme { *; }
-keep enum com.shalom.calendar.ui.theme.ThemeMode { *; }

# ================================================================================================
# ANDROID COMPONENTS
# ================================================================================================
# Keep BroadcastReceivers
-keep class * extends android.content.BroadcastReceiver { *; }
-keep class com.shalom.calendar.alarm.AlarmReceiver { *; }
-keep class com.shalom.calendar.receiver.BootCompleteReceiver { *; }

# Keep Services
-keep class * extends android.app.Service { *; }

# Keep Activities
-keep class * extends android.app.Activity { *; }
-keep class * extends androidx.activity.ComponentActivity { *; }

# Keep Application class
-keep class * extends android.app.Application { *; }

# ================================================================================================
# PARCELABLE
# ================================================================================================
# Keep Parcelable implementations
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
    public static final android.os.Parcelable$Creator CREATOR;
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# ================================================================================================
# DATASTORE
# ================================================================================================
# Keep DataStore preferences
-keep class androidx.datastore.*.** { *; }

# Keep preference keys
-keepclassmembers class * extends androidx.datastore.preferences.core.Preferences$Key {
    *;
}

# ================================================================================================
# KOTLIN SPECIFIC
# ================================================================================================
# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Keep companion objects
-keepclassmembers class ** {
    ** Companion;
}

# Keep data class copy methods
-keepclassmembers class * {
    *** copy(...);
    *** component1();
    *** component2();
    *** component3();
    *** component4();
    *** component5();
}

# Keep sealed classes
-keep class * extends kotlin.** { *; }

# ================================================================================================
# THIRD-PARTY LIBRARIES
# ================================================================================================
# ThreeTen Extra (Ethiopian Calendar)
-keep class org.threeten.** { *; }
-dontwarn org.threeten.**

# Lottie
-keep class com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**

# Timber
-keep class timber.log.** { *; }
-dontwarn timber.log.**

# Accompanist
-keep class com.google.accompanist.** { *; }
-dontwarn com.google.accompanist.**

# Navigation
-keep class androidx.navigation.** { *; }

# ================================================================================================
# GENERAL OPTIMIZATIONS & ATTRIBUTES
# ================================================================================================
# Keep source file names and line numbers for debugging
-keepattributes SourceFile,LineNumberTable

# Keep annotations
-keepattributes *Annotation*

# Keep generic signatures
-keepattributes Signature

# Keep exceptions
-keepattributes Exceptions

# Keep inner classes
-keepattributes InnerClasses,EnclosingMethod

# Optimization settings
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# Aggressive optimizations (remove unused code)
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Remove Timber logs in release
-assumenosideeffects class timber.log.Timber* {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
}

# ================================================================================================
# SUPPRESS WARNINGS
# ================================================================================================
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn javax.annotation.**
-dontwarn edu.umd.cs.findbugs.annotations.**
-dontwarn com.google.errorprone.annotations.**

# ================================================================================================
# END OF PROGUARD CONFIGURATION
# ================================================================================================
