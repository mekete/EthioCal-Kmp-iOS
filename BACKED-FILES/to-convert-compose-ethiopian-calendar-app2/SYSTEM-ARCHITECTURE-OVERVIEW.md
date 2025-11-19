# ComposeCalendarTwp - System Architecture Overview

## Project Overview

**ComposeCalendarTwp** is a modern Android calendar application featuring Ethiopian calendar support, event management, and comprehensive holiday tracking. The app is built with Jetpack Compose, Kotlin, and follows modern Android architectural patterns.

### Key Characteristics
- **Target**: Android app supporting Ethiopian, Gregorian, and Hirji calendars
- **UI Framework**: Jetpack Compose
- **Architecture Pattern**: MVVM/MVI with Dependency Injection
- **Database**: Room (SQLite)
- **Preferences**: DataStore
- **DI Framework**: Dagger Hilt
- **Supported Languages**: Amharic, English, Oromiffa, Tigrigna, French

---

## Architecture Overview

### Layered Architecture

```
┌─────────────────────────────────────┐
│           UI Layer                  │
│   (Compose, ViewModels, Screens)   │
├─────────────────────────────────────┤
│       Business Logic Layer          │
│  (ViewModels, UseCases, Managers)  │
├─────────────────────────────────────┤
│        Data Layer                   │
│  (Repositories, DAOs, Preferences) │
├─────────────────────────────────────┤
│    Infrastructure Layer             │
│   (Database, Remote Services)      │
└─────────────────────────────────────┘
```

---

## Core Components

### 1. Application Entry Points

#### **CalendarApplication.kt**
- **Type**: Hilt Application class
- **Responsibilities**:
  - App-wide Hilt dependency injection setup
  - WorkManager configuration with HiltWorkerFactory
  - Language context wrapping for locale support
  - Initialization of core services on app launch

- **Key Initializations**:
  1. Notification channel creation
  2. Permission state checking
  3. Widget scheduling (calendar & date change workers)
  4. Firebase Remote Config initialization
  5. App initialization manager execution
  6. Reminder re-registration

#### **MainActivity.kt**
- **Type**: Jetpack Compose Activity
- **Responsibilities**:
  - Primary UI entry point
  - Navigation between app screens
  - Theme and language management
  - Splash screen display
  - Onboarding flow
  - Edge-to-edge display handling

- **Screens Managed**:
  - Onboarding
  - Splash screen
  - Month calendar view
  - Event detail view
  - Holiday list
  - Date converter
  - Settings/More screen

- **Bottom Navigation Items**:
  - Month (Calendar)
  - Event (Daily view)
  - Holiday (Holiday list)
  - Converter (Date converter)
  - More (Settings)

---

### 2. UI Layer

#### **Compose Screens**

**OnboardingScreen.kt** (`ui/onboarding/`)
- Multi-page onboarding with HorizontalPager
- 5 pages: Welcome, Language, Theme, Holidays, Calendar Display
- User preference collection and persistence
- Skip/Done functionality

**DateConverterScreen.kt** (`ui/converter/`)
- Gregorian ↔ Ethiopian date conversion
- Date difference calculator
- Visual date picker support
- Result dialogs

**CalendarGlanceWidget.kt** (`widget/`)
- Glance widget framework (modern AppWidget API)
- Displays:
  - Current Ethiopian and Gregorian dates
  - Dual clock support (configurable timezones)
  - Upcoming events list
  - Event reminders

#### **Theme Management** (`ui/theme/`)

**AppTheme.kt**
```kotlin
enum class AppTheme: BLUE, RED, GREEN, PURPLE, ORANGE
enum class ThemeMode: SYSTEM, LIGHT, DARK
```

**Theme.kt**
- 5 color schemes (Blue, Red, Green, Purple, Orange)
- Light/Dark variants for each theme
- Material 3 design system integration
- Dynamic theme switching

#### **ViewModels**

**OnboardingViewModel.kt**
- Manages onboarding flow state
- Persists user selections immediately
- Handles smart defaults for skipped onboarding
- Uses StateFlow for reactive updates

**ThemeViewModel.kt** (in `ui/more/`)
- Manages theme (color) and mode (light/dark/system) selection
- Observes theme preferences changes

---

### 3. Data Layer

#### **Models & Entities**

**EventEntity.kt** (`data/local/entity/`)
```kotlin
@Entity(tableName = "events")
data class EventEntity(
    val id: String,                    // UUID
    val summary: String,               // Event title
    val description: String?,
    val startTime: ZonedDateTime,
    val endTime: ZonedDateTime?,
    val isAllDay: Boolean,
    val timeZone: String,              // IANA timezone
    val recurrenceRule: String?,       // RRULE format
    val reminderMinutesBefore: Int?,
    val ethiopianYear: Int,
    val ethiopianMonth: Int,
    val ethiopianDay: Int,
    val googleCalendarEventId: String?,// Google Calendar sync
    val isSynced: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)

data class EventInstance(...)         // Computed model for recurring events
```

#### **Preferences** (`data/preferences/`)

**SettingsPreferences.kt**
- Uses DataStore (replaces SharedPreferences)
- Manages all app settings
- Key preference categories:

| Category | Preferences |
|----------|-----------|
| **Version** | versionCode, versionName, isFirstRun |
| **Onboarding** | hasCompletedOnboarding |
| **Locale** | primaryLocale, secondaryLocale, deviceCountryCode |
| **Calendar Display** | primaryCalendar, secondaryCalendar, displayDualCalendar |
| **Holidays** | showPublicHolidays, showOrthodoxHolidays, showMuslimHolidays |
| **UI** | language, useGeezNumbers, use24HourFormat |
| **Widget** | displayTwoClocks, primaryWidgetTimezone, secondaryWidgetTimezone |
| **Theme** | (managed by ThemeViewModel) |
| **Migration** | migrationStatus, migrationEventCount |
| **Muslim Holidays** | holidayOffsetConfigJson (from Firebase) |

**Enums**:
```kotlin
enum class CalendarType: ETHIOPIAN, GREGORIAN, HIRJI
enum class Language: ENGLISH, AMHARIC, OROMIFFA, TIGRIGNA, FRENCH
enum class MigrationStatus: NOT_CHECKED, IN_PROGRESS, COMPLETED, FAILED, NO_OLD_DATA
```

#### **Repositories** (`data/repository/`)

**EventRepository.kt**
- **CRUD Operations**: Create, Read, Update, Delete events
- **Query Operations**:
  - Get events by date, month, year
  - Get upcoming events
  - Get events in date range
  - Search events
  - Get events by category
- **Recurring Event Handling**:
  - Generates event instances from RRULE
  - Supports WEEKLY recurrence patterns
  - Expandable for MONTHLY, YEARLY patterns
- **Reactive**: All operations return Flow<T> for real-time UI updates

#### **Remote Configuration** (`data/remote/`)

**RemoteConfigManager.kt**
- Manages Firebase Remote Config
- Fetches and activates config values
- Merges incoming configs with local preferences
- Handles Muslim holiday offsets from remote
- Cache expiration: 1 minute (debug) / 6 hours (release)

**ConfigHolidayOffset** Model:
```kotlin
@Serializable
data class ConfigHolidayOffset(
    val offsetDescription: String,
    val offsetEidAlAdha: Int,
    val offsetEidAlFitr: Int,
    val offsetMawlid: Int,
    val offsetRamadanStart: Int,
    val offsetEthioYear: Int,
    val offsetHirjiYear: Int,
    val offsetGregYear: Int,
    val offsetStage: String,           // "prod" or other
    val offsetUpdateTimestamp: Long
)
```

#### **Initialization & Managers** (`data/initialization/`)

**AppInitializationManager.kt**
Comprehensive app initialization on launch:
1. **Version Detection**: First-time setup vs upgrade vs normal launch
2. **Legacy Data Migration**: Migrates events from old Java app (versionCode < 96)
3. **Locale Initialization**: Sets primary/secondary timezones based on device location
4. **Muslim Holidays**: Enables for Arabic-speaking countries
5. **Firebase Services**: FCM topic subscriptions, Firebase Installation ID
6. **Analytics**: Logs app lifecycle events
7. **Remote Config**: Initializes Firebase Remote Config

**ReminderReregistrationManager.kt**
- Re-registers all event alarms on app launch
- Ensures alarms survive:
  - App updates
  - Device reboots
  - System alarm clearing
- Filters events:
  - Recurring events: Always reschedule
  - Future one-time events: Reschedule
  - Past events: Skip

**PermissionManager.kt** (`data/permissions/`)
- Tracks app permissions with StateFlow
- Manages:
  - POST_NOTIFICATIONS (Android 13+)
  - SCHEDULE_EXACT_ALARM (Android 12+)
  - RECEIVE_BOOT_COMPLETED
- Handles permission change broadcasts
- Provides permission request flows
- Opens system settings for special permissions

---

### 4. Alarm & Notification System

#### **NotificationHelper.kt** (`alarm/`)
- Creates notification channels (required for Android 8+)
- Builds event reminder notifications
- Handles notification permissions before displaying
- Uses alarm sound for prominent notifications

**Channel Configuration**:
- Channel ID: `event_reminders`
- Importance: HIGH
- Sound: Alarm tone
- Vibration: Enabled
- Lights: Enabled

#### **AlarmReceiver.kt** (`alarm/`)
- BroadcastReceiver for event alarm triggers
- Responsibilities:
  - Receives alarm broadcasts from AlarmManager
  - Shows notification to user
  - For recurring events, schedules next occurrence
  - Handles deleted/modified events edge cases
- Uses Hilt dependency injection
- Uses goAsync() for async database operations

**Intent Actions**:
```kotlin
const val ACTION_EVENT_ALARM = "com.shalom.calendar.ACTION_EVENT_ALARM"
```

**Intent Extras**:
- EXTRA_EVENT_ID
- EXTRA_EVENT_TITLE
- EXTRA_EVENT_DESCRIPTION
- EXTRA_EVENT_TIME
- EXTRA_IS_RECURRING

#### **AlarmScheduler.kt** (implied)
- Schedules alarms for events with reminders
- Respects SCHEDULE_EXACT_ALARM permission
- Falls back to inexact alarms if permission denied

---

### 5. Android Components

#### **Receivers** (defined in AndroidManifest.xml)

**BootCompleteReceiver**
- Action: `android.intent.action.BOOT_COMPLETED`
- Purpose: Re-register reminders after device reboot

**AlarmReceiver**
- Action: `com.shalom.calendar.ACTION_EVENT_ALARM`
- Purpose: Handle event alarm triggers (defined in CalendarApplication)

**CalendarWidgetReceiver**
- Action: `android.appwidget.action.APPWIDGET_UPDATE`
- Purpose: Handle widget updates

**DateChangeBroadcastReceiver**
- Actions:
  - `android.intent.action.DATE_CHANGED`
  - `android.intent.action.TIME_SET`
  - `android.intent.action.TIMEZONE_CHANGED`
  - `com.shalom.calendar.widget.ACTION_REFRESH_WIDGET`
- Purpose: Update widget when date/time changes

#### **Services**

**MyFirebaseMessagingService**
- Handles Firebase Cloud Messaging
- Receives push notifications
- Manages FCM topic subscriptions

---

## Data Flow & State Management

### Dependency Injection (Hilt)

**Modules** (implied from usage):
- DatabaseModule: Provides Room database
- RepositoryModule: Provides repository instances
- PreferencesModule: Provides SettingsPreferences
- ManagerModule: Provides managers (Init, RemoteConfig, Permissions)

### State Management

**ViewModels + StateFlow**:
- State is managed through reactive StateFlow
- Updates are observed by Compose UI
- Immediate persistence to DataStore/Database

**Flow-Based Queries**:
- Repository methods return Flow<T>
- UI automatically updates when data changes
- No need for manual recomposition triggers

### Event Lifecycle

```
User creates event
    ↓
EventRepository.createEvent() → Room insert
    ↓
Event persisted in database
    ↓
If reminder set: AlarmManager.setAndAllowWhileIdle()
    ↓
At scheduled time: AlarmReceiver triggered
    ↓
NotificationHelper.showEventReminderNotification()
    ↓
If recurring: Schedule next occurrence
```

---

## Initialization & Startup Sequence

### Application Startup (`CalendarApplication.onCreate()`)

1. **Create Notification Channels**
   - Event reminder channel (alarm sound)

2. **Initialize Permissions**
   - `permissionManager.refreshPermissionState()`
   - Register permission change listener

3. **Schedule Widget Updates**
   - CalendarWidgetWorker (30-minute intervals)
   - DateChangeWorker (daily/hourly updates)

4. **Initialize Remote Config**
   - Firebase Remote Config setup (1 min debug / 6 hours release)

5. **Comprehensive App Initialization**
   - AppInitializationManager.initialize():
     - Version detection & upgrade handling
     - First-time setup (if new installation)
     - Legacy data migration (from old app)
     - Locale initialization
     - Firebase services setup
     - Analytics logging

6. **Re-register Reminders**
   - ReminderReregistrationManager.reregisterReminders()
   - Recovers alarms after app update or reboot

### MainActivity Startup

1. Load saved language preference
2. Wrap context with locale
3. Set Compose content
4. Determine start destination:
   - If onboarding not completed → OnboardingScreen
   - Else → SplashScreen
5. Initialize navigation
6. Listen for language changes (recreate if changed)

### Onboarding Flow

```
OnboardingScreen shows 5 pages:
1. Welcome page → "Get Started"
2. Language selection
3. Theme (light/dark/system) selection
4. Holiday preferences
5. Calendar display options

Each selection is saved immediately via SettingsPreferences
On completion/skip → Navigate to SplashScreen → Splash animation → Month calendar
```

---

## Key Features & Implementation

### 1. Multi-Calendar Support

**Supported Calendars**:
- Ethiopian (Ge'ez)
- Gregorian (International)
- Hirji (Islamic)

**Calendar Conversion**:
- Uses ThreeTen Extra library for chronology
- EthiopicDate/HirjiDate classes for date manipulation
- DateTimeFormatter for custom formatting

### 2. Event Management

**Event Creation**:
- User creates via EventScreen
- Stored in Room database as EventEntity
- Reminder can be set (minutes before)

**Event Queries**:
- By date (Ethiopian calendar)
- By month (Ethiopian calendar)
- By date range (Gregorian)
- Upcoming events
- By category

**Recurring Events**:
- Uses RRULE format (RFC 5545)
- Currently supports: FREQ=WEEKLY;BYDAY=...
- Generates instances dynamically

### 3. Holiday Management

**Holiday Types**:
- Public holidays (day-off)
- Orthodox Christian holidays
- Muslim holidays (with Firebase Remote Config offsets)
- Cultural holidays
- Western holidays

**Holiday Configuration**:
- Per-user preferences (include/exclude)
- Toggles in settings
- Stored in DataStore
- Firebase Remote Config for Muslim offsets

### 4. Theme System

**Theme Customization**:
- 5 base colors: Blue, Red, Green, Purple, Orange
- 3 modes: Light, Dark, System (respects device setting)
- Real-time theme switching
- Material 3 design system
- Color scheme for both light and dark variants

### 5. Language Support

- Amharic (primary)
- English
- Oromiffa
- Tigrigna
- French

**Implementation**:
- Context wrapping via LocaleHelper
- String resources in `res/values-*`
- Activity recreation on language change
- Settings persist language choice

### 6. Widget Support

**CalendarGlanceWidget** (Glance AppWidget framework):
- Shows Ethiopian & Gregorian dates
- Dual clock display (configurable timezones)
- Upcoming events list
- Refresh button
- Click to open app

**Widget Data**:
- Real-time date/time display
- Upcoming events from database
- User preferences (timezone, 24-hour format)
- State managed via GlanceAppWidget

### 7. Date Converter Tool

**Conversions**:
- Gregorian → Ethiopian
- Ethiopian → Gregorian
- Date difference calculator

**Features**:
- Manual input with validation
- Date picker support (native & custom)
- Real-time conversion
- Error handling and validation
- Result dialogs

---

## Firebase Integration

### Firebase Services Used

1. **Firebase Remote Config**
   - Manages Muslim holiday date offsets
   - Key: `config_holiday_offset`
   - Synced to DataStore
   - Mergeable configuration (by year)

2. **Firebase Cloud Messaging (FCM)**
   - Topic-based messaging
   - Subscribed topics: "general", "holiday-updates"
   - Custom message handling

3. **Firebase Analytics**
   - App lifecycle events (install, upgrade, launch)
   - Legacy data migration tracking
   - Version code/name logging
   - Android SDK version tracking

4. **Firebase Crashlytics**
   - Automatic crash reporting
   - Error tracking and analysis

5. **Firebase Installations**
   - Unique installation ID generation
   - Persisted for analytics

### Remote Config Setup

```
MinimumFetchInterval:
  - Debug: 1 minute
  - Release: 6 hours

Default values from: res/xml/remote_config_defaults.xml

Syncing:
  RemoteConfig → DataStore preferences
  Merge logic: Incoming configs override by year
```

---

## Permissions

### Manifest Permissions

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
  <!-- Android 13+: Show notifications -->

<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
  <!-- Receive boot complete broadcast -->

<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
  <!-- Android 12+: Schedule exact alarms -->

<uses-permission android:name="android.permission.INTERNET" />
  <!-- Firebase services -->
```

### Runtime Permissions

- **POST_NOTIFICATIONS**: Requested in onboarding or on first event creation
- **SCHEDULE_EXACT_ALARM**: Cannot be requested at runtime (special app access permission)

### Permission Flow

```
PermissionManager monitors → Broadcasts
  ↓
On SCHEDULE_EXACT_ALARM grant → ReminderReregistrationManager.reregisterReminders()
```

---

## Database Schema

### Room Database

**EventEntity Table**:
```sql
CREATE TABLE events (
    id TEXT PRIMARY KEY,
    summary TEXT NOT NULL,
    description TEXT,
    startTime ZONED_DATETIME,
    endTime ZONED_DATETIME,
    isAllDay BOOLEAN,
    timeZone TEXT,
    recurrenceRule TEXT,
    category TEXT,
    reminderMinutesBefore INTEGER,
    notificationChannelId TEXT,
    ethiopianYear INTEGER,
    ethiopianMonth INTEGER,
    ethiopianDay INTEGER,
    googleCalendarEventId TEXT,
    googleCalendarId TEXT,
    isSynced BOOLEAN,
    syncedAt LONG,
    createdAt LONG,
    updatedAt LONG
)
```

### DataStore Preferences

**Location**: `settings_preferences` DataStore

**Key Groups**:
- Version tracking
- Onboarding status
- Locale & timezone
- Calendar display
- Holiday preferences
- Theme preferences
- Widget settings
- Migration status
- Firebase Installation ID

---

## Build Configuration

### Gradle Dependencies (from build.gradle.kts)

**Core Android**:
- Jetpack Compose
- Material 3
- Activity Compose
- Navigation Compose

**Data**:
- Room (database)
- DataStore (preferences)

**DI**:
- Dagger Hilt

**Firebase**:
- Firebase Remote Config
- Firebase Cloud Messaging
- Firebase Analytics
- Firebase Crashlytics
- Firebase Installations

**Date/Time**:
- ThreeTen Extra (Ethiopian/Hirji calendar)
- Java Time (ZonedDateTime)

**Other**:
- Kotlinx Serialization (JSON parsing)
- Lottie (animations)
- Glance (AppWidget)
- Timber (logging)

### Plugins

```kotlin
com.android.application
org.jetbrains.kotlin.android
com.google.dagger.hilt.android
com.google.gms.google-services
com.google.devtools.ksp
org.jetbrains.kotlin.plugin.compose
com.google.firebase.crashlytics
```

---

## Architecture Patterns Used

### MVVM (Model-View-ViewModel)
- ViewModels manage UI state
- Views (Compose) observe StateFlow
- Models represent data

### Repository Pattern
- EventRepository abstracts data access
- Single source of truth for event data
- Clean separation between UI and data layers

### Dependency Injection (Hilt)
- Constructor injection for all dependencies
- Singleton scopes for shared resources
- Module-based configuration

### Flow-Based Reactive Patterns
- Repositories return Flow<T>
- UI collects flows and recomposes on changes
- No manual state management needed

### Manager Pattern
- AppInitializationManager: Orchestrates startup
- ReminderReregistrationManager: Handles alarm lifecycle
- PermissionManager: Centralized permission handling
- RemoteConfigManager: Remote configuration sync

---

## File Structure

```
app/src/main/
├── java/com/shalom/calendar/
│   ├── CalendarApplication.kt
│   ├── MainActivity.kt
│   ├── alarm/
│   │   ├── AlarmReceiver.kt
│   │   ├── AlarmScheduler.kt
│   │   └── NotificationHelper.kt
│   ├── data/
│   │   ├── initialization/
│   │   │   ├── AppInitializationManager.kt
│   │   │   ├── ReminderReregistrationManager.kt
│   │   │   └── ...
│   │   ├── local/
│   │   │   ├── entity/
│   │   │   │   └── EventEntity.kt
│   │   │   ├── dao/
│   │   │   │   └── EventDao.kt
│   │   │   └── CalendarDatabase.kt
│   │   ├── migration/
│   │   │   └── ...
│   │   ├── model/
│   │   │   └── ...
│   │   ├── permissions/
│   │   │   ├── PermissionManager.kt
│   │   │   └── PermissionHelper.kt
│   │   ├── preferences/
│   │   │   ├── SettingsPreferences.kt
│   │   │   └── ThemePreferences.kt
│   │   ├── remote/
│   │   │   └── RemoteConfigManager.kt
│   │   └── repository/
│   │       └── EventRepository.kt
│   ├── receiver/
│   │   └── BootCompleteReceiver.kt
│   ├── ui/
│   │   ├── components/
│   │   │   ├── MonthHeaderItem.kt
│   │   │   ├── AutocompleteTextField.kt
│   │   │   └── ...
│   │   ├── converter/
│   │   │   └── DateConverterScreen.kt
│   │   ├── event/
│   │   │   ├── EventScreen.kt
│   │   │   ├── EventViewModel.kt
│   │   │   └── ...
│   │   ├── month/
│   │   │   ├── MonthCalendarScreen.kt
│   │   │   ├── MonthCalendarViewModel.kt
│   │   │   └── ...
│   │   ├── onboarding/
│   │   │   ├── OnboardingScreen.kt
│   │   │   ├── OnboardingViewModel.kt
│   │   │   ├── LanguageSelectionPage.kt
│   │   │   ├── ThemeSelectionPage.kt
│   │   │   ├── HolidaySelectionPage.kt
│   │   │   ├── CalendarDisplayPage.kt
│   │   │   └── WelcomePage.kt
│   │   ├── theme/
│   │   │   ├── Theme.kt
│   │   │   ├── AppTheme.kt
│   │   │   ├── Color.kt
│   │   │   └── Type.kt
│   │   └── ...
│   ├── widget/
│   │   ├── CalendarGlanceWidget.kt
│   │   ├── CalendarWidgetReceiver.kt
│   │   ├── CalendarWidgetWorker.kt
│   │   ├── DateChangeBroadcastReceiver.kt
│   │   ├── DateChangeWorker.kt
│   │   └── ...
│   └── util/
│       ├── LocaleHelper.kt
│       └── ...
├── AndroidManifest.xml
└── res/
    ├── layout/
    ├── values/
    ├── values-*/ (localized strings)
    ├── raw/
    └── xml/
```

---

## Performance Considerations

1. **Database Queries**: Room uses Flow for reactive queries
2. **Lazy Loading**: Compose recomposes only changed items
3. **Coroutine Scopes**: ApplicationScope for long-running tasks
4. **Cache Expiration**: Remote Config caching (6 hours release)
5. **WorkManager**: For periodic widget updates
6. **Glance Widget**: Lightweight widget framework (replaces RemoteViews)

---

## Testing Considerations

- **Hilt**: Facilitates dependency injection for testing
- **Flow**: Testable through collection and assertion
- **ViewModel**: Can be tested independently
- **Repository**: Abstract data layer for mocking
- **Firebase**: Remote Config and Analytics provide debug variants

---

## Future Enhancements

Based on code comments and structure:

1. **Google Calendar Sync**: Fields exist in EventEntity for sync status
2. **Complex Recurrence**: RRULE parsing for MONTHLY, YEARLY patterns
3. **Event Categories**: Color-coded event categories
4. **User Preferences**: Admin controls for holiday display
5. **Notification Customization**: Sound, vibration per event
6. **Widget Customization**: User-configurable widget display options

---

## Security & Best Practices

1. **PendingIntent Flags**: Uses FLAG_IMMUTABLE for Android 12+ safety
2. **Permission Checks**: PermissionHelper validates before showing notifications
3. **DataStore**: Secure by default (encrypted preferences)
4. **Hilt Injection**: Type-safe dependency management
5. **Null Safety**: Kotlin null-safety enforced throughout
6. **Exception Handling**: Try-catch blocks for robust error handling

---

## Glossary

- **RRULE**: Recurrence Rule (RFC 5545 format for recurring events)
- **EthiopicDate**: Date representation in Ethiopian calendar system
- **ZonedDateTime**: Date-time with timezone information
- **DataStore**: Modern replacement for SharedPreferences
- **Glance**: Jetpack Glance for app widgets
- **Hilt**: Dependency injection framework built on Dagger
- **Flow**: Kotlin coroutine-based reactive stream
- **Compose**: Declarative UI framework for Android
- **Remote Config**: Firebase service for remote feature configuration

---

*Generated: 2025-11-16*
*Application: ComposeCalendarTwp*
*Architecture Pattern: MVVM with Hilt DI*
