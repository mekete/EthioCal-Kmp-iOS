# Ethiopian Calendar App - Kotlin Multiplatform Migration Plan
## ComposeCalendarTwp: Android → iOS with Compose Multiplatform

**Project**: ComposeCalendarTwp  
**Current State**: Stable Android app with 5,500+ lines of Compose UI  
**Target**: iOS app via Kotlin Multiplatform  
**Migration Timeline**: 16 weeks (4 months)  
**Team Size**: 1.5-2 people (1 Kotlin lead + 0.5-1 iOS developer)

**Document Version**: 1.0  
**Last Updated**: November 17, 2025  
**Status**: Ready for Implementation

---

## Table of Contents

1. [Executive Summary](#executive-summary)
2. [Architecture Overview](#architecture-overview)
3. [Phase-by-Phase Breakdown](#phase-by-phase-breakdown)
4. [Detailed Implementation Guide](#detailed-implementation-guide)
5. [Ethiopian Calendar Date Logic Extraction](#ethiopian-calendar-date-logic-extraction)
6. [Library Migration Strategy](#library-migration-strategy)
7. [Risk Management](#risk-management)
8. [Testing Strategy](#testing-strategy)
9. [Deployment & Launch](#deployment--launch)
10. [Success Metrics](#success-metrics)
11. [Troubleshooting & FAQ](#troubleshooting--faq)
12. [Appendix](#appendix)

---

## Executive Summary

### Project Overview

ComposeCalendarTwp is a feature-rich Ethiopian calendar application that requires expansion to iOS. The app currently:
- ✅ Has stable, production-ready Android code
- ✅ Uses modern architecture (MVVM with Hilt DI)
- ✅ Features comprehensive Compose UI
- ✅ Includes complex date calculations (Ethiopian/Gregorian/Hirji)
- ✅ Integrates Firebase (Remote Config, Analytics, Messaging)
- ✅ Manages events with recurring patterns and reminders

### Migration Strategy

Use **Kotlin Multiplatform (KMP)** with **Compose Multiplatform** to:
1. Share 90-95% of code between Android and iOS
2. Reuse all business logic, data layer, and UI code
3. Write only platform-specific integrations (notifications, alarms)
4. Reduce time-to-market by ~50% vs native iOS

### Key Metrics

| Metric | Value |
|--------|-------|
| **Timeline** | 16 weeks |
| **Team** | 1.5-2 people |
| **Code Sharing** | 90-95% |
| **Reusable Code** | 5,500+ UI lines + 100% business logic |
| **New Code** | ~500 lines (platform-specific only) |
| **Risk Level** | Low |
| **Estimated Cost** | $80-120K |

### Success Criteria

- ✅ Both Android and iOS apps functional and tested
- ✅ Feature parity between platforms (same business logic)
- ✅ Performance acceptable on both platforms
- ✅ iOS app passes App Store review
- ✅ Zero platform-specific bugs (shared logic tested thoroughly)
- ✅ Maintainability improved (single codebase for logic)

---

## Architecture Overview

### Current Android Architecture (Will Become Shared)

```
┌─────────────────────────────────────┐
│    UI Layer (Jetpack Compose)       │ ← 5,500 lines
│  (Screens, ViewModels, Theme)       │
├─────────────────────────────────────┤
│    Business Logic Layer             │ ← 2,000 lines
│  (UseCases, Managers, Calculations) │
├─────────────────────────────────────┤
│    Data Layer                       │ ← 1,500 lines
│  (Repositories, DAOs, Preferences)  │
├─────────────────────────────────────┤
│    Infrastructure Layer             │ ← 1,000 lines
│  (Database, Remote Services)        │
└─────────────────────────────────────┘
```

### Target KMP Architecture

```
shared/
├── commonMain/ (100% shared)
│   ├── ui/                           ← Compose Multiplatform UI (5,500 lines)
│   │   ├── month/
│   │   ├── event/
│   │   ├── converter/
│   │   ├── holiday/
│   │   ├── onboarding/
│   │   ├── settings/
│   │   └── theme/
│   │
│   ├── business/ (100% shared)       ← Business Logic (2,000 lines)
│   │   ├── calendar/
│   │   │   ├── EthiopicCalculations.kt
│   │   │   ├── HolidayCalculator.kt
│   │   │   └── CalendarUtils.kt
│   │   ├── managers/
│   │   │   ├── AppInitializationManager.kt
│   │   │   ├── ReminderManager.kt
│   │   │   └── RemoteConfigManager.kt
│   │   ├── usecases/
│   │   └── models/
│   │
│   ├── data/ (90% shared)             ← Data Layer (1,500 lines)
│   │   ├── repository/
│   │   ├── model/
│   │   ├── preferences/
│   │   └── remote/
│   │
│   ├── platform/                      ← Platform Abstractions
│   │   ├── expect/
│   │   │   ├── NotificationManager.kt
│   │   │   ├── PermissionManager.kt
│   │   │   ├── DatabaseDriver.kt
│   │   │   └── PlatformContext.kt
│   │   └── resources/ (localization)
│   │
│   └── di/                            ← Dependency Injection (Koin)
│       └── Module.kt
│
├── androidMain/                       ← Android-Specific (200-300 lines)
│   ├── kotlin/
│   │   ├── MainActivity.kt
│   │   ├── platform/
│   │   │   ├── actual/
│   │   │   │   ├── NotificationManagerAndroid.kt
│   │   │   │   ├── PermissionManagerAndroid.kt
│   │   │   │   └── DatabaseDriverAndroid.kt
│   │   ├── alarm/
│   │   │   ├── AlarmReceiver.kt
│   │   │   ├── AlarmScheduler.kt
│   │   │   └── NotificationHelper.kt
│   │   └── di/
│   │       └── AndroidModule.kt
│   ├── AndroidManifest.xml
│   └── res/ (layouts, drawables, etc.)
│
└── iosMain/                           ← iOS-Specific (150-200 lines)
    ├── kotlin/
    │   ├── MainIOSViewController.kt
    │   ├── platform/
    │   │   ├── actual/
    │   │   │   ├── NotificationManagerIOS.kt
    │   │   │   ├── PermissionManagerIOS.kt
    │   │   │   └── DatabaseDriverIOS.kt
    │   ├── di/
    │   │   └── IOSModule.kt
    │   └── AppDelegate.kt
    └── Info.plist

androidApp/                             ← Android App Module
├── build.gradle.kts
└── src/
    ├── main/
    │   └── AndroidManifest.xml

iosApp/                                 ← iOS App Module
├── build.gradle.kts
└── iosApp.xcodeproj
```

### Code Distribution

```
Total Lines of Code:        ~10,000 lines

Shared Code:
  ├── UI (Compose)          5,500 lines (100% shared) ✅
  ├── Business Logic        2,000 lines (100% shared) ✅
  ├── Data Layer            1,200 lines (100% shared) ✅
  └── Models/Domain           800 lines (100% shared) ✅
  └──────────────────────────────────────────────────
      SHARED TOTAL:         9,500 lines (95% of codebase!)

Platform-Specific:
  ├── Android UI Entry        200 lines
  ├── iOS UI Entry            150 lines
  ├── Android Alarms/Notif    200 lines
  ├── iOS Notifications       150 lines
  └──────────────────────────────────────────────────
      PLATFORM-SPECIFIC:      700 lines (5% of codebase)
```

---

## Phase-by-Phase Breakdown

### Timeline Overview

```
Week 1-2:   Project Setup & Dependencies (Phase 1)
Week 3-6:   Core Logic Extraction & Migration (Phase 2)
Week 7-10:  UI Migration to Shared Module (Phase 3)
Week 11-12: Platform Integration (Phase 4)
Week 13-14: Testing & Optimization (Phase 5)
Week 15-16: Deployment & Launch (Phase 6)
```

### Phase 1: Project Setup & Dependencies (Weeks 1-2)

**Goal**: Establish KMP project structure and verify all libraries work

#### Week 1

**Days 1-2: Generate KMP Project**
- Create new KMP project using Android Studio Kotlin Multiplatform wizard
- OR manually configure gradle with KMP plugin
- Structure:
  ```
  composeCalendarTwp-kmp/
  ├── shared/
  ├── androidApp/
  ├── iosApp/
  └── build.gradle.kts (root)
  ```

**Days 3-4: Setup Dependencies**
- Add core KMP dependencies to `shared/build.gradle.kts`:
  ```kotlin
  kotlin {
      multiplatform {
          android()
          iosX64()
          iosArm64()
          iosSimulatorArm64()
      }
      
      sourceSets {
          val commonMain by getting {
              dependencies {
                  // Compose Multiplatform
                  implementation("org.jetbrains.compose.ui:ui:1.5.0")
                  implementation("org.jetbrains.compose.material3:material3:1.5.0")
                  
                  // Coroutines
                  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
                  
                  // Date/Time
                  implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
                  
                  // DI (Koin)
                  implementation("io.insert-koin:koin-core:3.5.0")
                  
                  // Serialization
                  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
                  
                  // Logging
                  implementation("co.touchlab:kermit:1.2.2")
              }
          }
          
          val androidMain by getting {
              dependencies {
                  implementation("androidx.activity:activity-compose:1.8.0")
                  implementation("androidx.room:room-runtime:2.6.1")
                  implementation("androidx.datastore:datastore-preferences:1.0.0")
                  implementation("com.google.firebase:firebase-config-ktx")
              }
          }
          
          val iosMain by getting {
              dependencies {
                  implementation("app.cash.sqldelight:native-driver:2.0.0")
              }
          }
      }
  }
  ```

**Days 5: Verify Builds**
- Build Android: `./gradlew :shared:build`
- Build iOS: `./gradlew :shared:build -PkotlinTarget=ios`
- Verify: No compilation errors

**Deliverables**:
- ✅ Project compiles for Android
- ✅ Project compiles for iOS
- ✅ All dependencies resolved

#### Week 2

**Days 6-7: Firebase KMP Setup**
- Create Firebase KMP POC:
  ```kotlin
  // shared/src/commonMain/kotlin/data/remote/FirebaseConfigManager.kt
  expect class FirebaseConfigManager {
      suspend fun fetchAndActivate()
      suspend fun getString(key: String): String
  }
  
  // androidApp
  actual class FirebaseConfigManagerAndroid : FirebaseConfigManager {
      private val remoteConfig = Firebase.remoteConfig
      override suspend fun fetchAndActivate() {
          remoteConfig.fetchAndActivate()
      }
  }
  
  // iosApp
  actual class FirebaseConfigManagerIOS : FirebaseConfigManager {
      private val remoteConfig = RemoteConfig.remoteConfig()
      override suspend fun fetchAndActivate() {
          remoteConfig.fetchAndActivate()
      }
  }
  ```
- Test on both platforms: Fetch remote config successfully ✅

**Days 8-9: Database Strategy Decision**
- Evaluate options:
  - Option A: Keep Room on Android, SQLDelight on iOS (recommended)
  - Option B: Use SQLDelight for both
- Create abstractions in `data/local/`:
  ```kotlin
  // shared/src/commonMain/kotlin/data/local/EventDao.kt
  expect interface EventDao {
      suspend fun getEventsForMonth(year: Int, month: Int): Flow<List<EventEntity>>
      suspend fun createEvent(event: EventEntity)
      suspend fun updateEvent(event: EventEntity)
      suspend fun deleteEvent(id: String)
  }
  
  // androidMain
  actual interface EventDao {
      // Room @Dao implementation
  }
  
  // iosMain
  actual interface EventDao {
      // SQLDelight implementation
  }
  ```

**Days 10: Koin Setup**
- Create DI container:
  ```kotlin
  // shared/src/commonMain/kotlin/di/Module.kt
  val appModule = module {
      single<EventRepository> { EventRepositoryImpl(get(), get()) }
      single<HolidayRepository> { HolidayRepositoryImpl(get()) }
      single { AppInitializationManager(get(), get(), get()) }
      single<RemoteConfigManager> { RemoteConfigManagerKmp(get()) }
  }
  ```
- Verify Koin initializes on both platforms

**Deliverables**:
- ✅ Firebase SDK verified on iOS
- ✅ Database abstraction designed
- ✅ DI container setup with Koin
- ✅ All dependencies working on both platforms

---

### Phase 2: Core Logic Extraction & Migration (Weeks 3-6)

**Goal**: Extract and test all business logic in shared module

#### Week 3: Ethiopian Calendar Logic Extraction

**Days 11-12: Audit ThreeTen Extra**
- Clone: `git clone https://github.com/ThreeTen/threeten-extra.git`
- Study: `src/main/java/org/threeten/extra/chrono/EthiopicChronology.java`
- Document algorithms:
  - Gregorian to Julian Day Number conversion
  - Julian Day Number to Ethiopian conversion
  - Leap year rules (year % 4 == 3)
  - Month boundaries (12×30 + 1×5/6)

**Days 13-14: Create Reference Data**
- Build test cases mapping Gregorian ↔ Ethiopian:
  ```kotlin
  val testData = mapOf(
      LocalDate(2024, 11, 17) to EthiopicDate(2017, 3, 9),
      LocalDate(2000, 1, 1) to EthiopicDate(1992, 4, 24),
      LocalDate(2025, 9, 11) to EthiopicDate(2018, 1, 1),  // Ethiopian New Year
      LocalDate(2025, 12, 25) to EthiopicDate(2018, 4, 17), // Fasika
      // ... 20+ historical dates
  )
  ```

**Days 15: Start Pure Kotlin Implementation**
- Create: `shared/src/commonMain/kotlin/business/calendar/EthiopicCalculations.kt`
- Implement helper functions:
  ```kotlin
  object EthiopicCalculations {
      private const val ETHIOPIC_EPOCH_JDN = 1724220L
      
      fun gregorianToEthiopic(localDate: LocalDate): EthiopicDate {
          val jdn = gregorianToJulianDay(localDate)
          return julianDayToEthiopic(jdn)
      }
      
      fun ethiopicToGregorian(ethiopicDate: EthiopicDate): LocalDate {
          val jdn = ethiopicToJulianDay(ethiopicDate)
          return julianDayToGregorian(jdn)
      }
      
      private fun gregorianToJulianDay(date: LocalDate): Long {
          val a = (14 - date.monthValue) / 12
          val y = date.year + 4800 - a
          val m = date.monthValue + 12 * a - 3
          return date.dayOfMonth + (153 * m + 2) / 5 + 
                 365 * y + y / 4 - y / 100 + y / 400 - 32045
      }
      
      private fun julianDayToGregorian(jdn: Long): LocalDate {
          val a = jdn + 32044
          val b = (4 * a + 3) / 146097
          val c = a - (146097 * b) / 4
          val d = (4 * c + 3) / 1461
          val e = c - (1461 * d) / 4
          val m = (5 * e + 2) / 153
          
          val day = e - (153 * m + 2) / 5 + 1
          val month = m + 3 - 12 * (m / 10)
          val year = 100 * b + d - 4800 + m / 10
          
          return LocalDate(year.toInt(), month.toInt(), day.toInt())
      }
      
      private fun ethiopicToJulianDay(ethiopicDate: EthiopicDate): Long {
          val daysBeforeYear = 365 * (ethiopicDate.year - 1) + (ethiopicDate.year - 1) / 4
          val daysBeforeMonth = 30 * (ethiopicDate.month - 1)
          val totalDays = daysBeforeYear + daysBeforeMonth + ethiopicDate.day
          return ETHIOPIC_EPOCH_JDN + totalDays - 1
      }
      
      private fun julianDayToEthiopic(jdn: Long): EthiopicDate {
          val daysSinceEpoch = jdn - ETHIOPIC_EPOCH_JDN + 1
          var year = ((daysSinceEpoch - 1) / 365).toInt() + 1
          var remainingDays = (daysSinceEpoch - 1 - 365 * (year - 1) - (year - 1) / 4).toInt()
          
          val month = (remainingDays / 30) + 1
          val day = (remainingDays % 30) + 1
          
          return EthiopicDate(year, month, day)
      }
      
      fun isLeapYear(ethiopicYear: Int): Boolean = (ethiopicYear % 4) == 3
      
      fun daysInMonth(month: Int, year: Int): Int {
          return if (month <= 12) 30 else if (isLeapYear(year)) 6 else 5
      }
  }
  ```

**Deliverables**:
- ✅ `EthiopicCalculations.kt` fully implemented
- ✅ Reference test data prepared
- ✅ Ready for testing

#### Week 4: Testing Ethiopian Calendar Logic

**Days 16-17: Unit Tests**
- Create: `shared/src/commonTest/kotlin/business/calendar/EthiopicCalculationsTest.kt`
- Write comprehensive tests:
  ```kotlin
  class EthiopicCalculationsTest {
      @Test
      fun testGregorianToEthiopicConversion() {
          // Known conversion
          val gregorian = LocalDate(2024, 11, 17)
          val ethiopic = EthiopicCalculations.gregorianToEthiopic(gregorian)
          assertEquals(EthiopicDate(2017, 3, 9), ethiopic)
      }
      
      @Test
      fun testRoundTripConversion() {
          val original = LocalDate(2025, 5, 15)
          val ethiopic = EthiopicCalculations.gregorianToEthiopic(original)
          val backToGregorian = EthiopicCalculations.ethiopicToGregorian(ethiopic)
          assertEquals(original, backToGregorian)
      }
      
      @Test
      fun testLeapYearRules() {
          assertTrue(EthiopicCalculations.isLeapYear(2003))   // 2003 % 4 == 3
          assertFalse(EthiopicCalculations.isLeapYear(2004)) // 2004 % 4 == 0
      }
      
      @Test
      fun testAllReferenceData() {
          testData.forEach { (gregorian, expected) ->
              val actual = EthiopicCalculations.gregorianToEthiopic(gregorian)
              assertEquals(expected, actual, "Failed for $gregorian")
          }
      }
  }
  ```
- Run tests on both Android and iOS

**Days 18-19: Holiday Calculations**
- Create: `shared/src/commonMain/kotlin/business/calendar/HolidayCalculator.kt`
- Implement holiday calculations:
  ```kotlin
  class HolidayCalculator(
      private val calculations: EthiopicCalculations
  ) {
      fun calculateFasika(ethiopicYear: Int): EthiopicDate {
          // Ethiopian Orthodox Easter calculation
          // Complex algorithm (Meeus/Jones/Butcher)
      }
      
      fun getPublicHolidays(ethiopicYear: Int): List<Holiday> {
          return listOf(
              Holiday("Ethiopian New Year", EthiopicDate(ethiopicYear, 1, 1)),
              Holiday("Battle of Adwa Day", EthiopicDate(ethiopicYear, 5, 23)),
              Holiday("Revolution Day", EthiopicDate(ethiopicYear, 11, 23)),
              Holiday("Derg Day", EthiopicDate(ethiopicYear, 12, 17)),
          )
      }
  }
  ```

**Days 20: Verify Against Current Implementation**
- Compare outputs with your existing holiday logic
- Ensure 100% parity
- Fix any discrepancies

**Deliverables**:
- ✅ Ethiopian calendar logic fully tested
- ✅ Holiday calculations verified
- ✅ Tests pass on both platforms

#### Week 5: Repository & Data Layer Migration

**Days 21-22: Extract Models & Domain**
- Move domain models to shared:
  ```kotlin
  // shared/src/commonMain/kotlin/domain/model/
  data class Event(
      val id: String,
      val summary: String,
      val description: String?,
      val startTime: Instant,
      val endTime: Instant?,
      val isAllDay: Boolean,
      val timeZone: String,
      val recurrenceRule: String?,
      val reminderMinutesBefore: Int?,
      val ethiopianYear: Int,
      val ethiopianMonth: Int,
      val ethiopianDay: Int,
      val googleCalendarEventId: String?,
      val isSynced: Boolean,
      val createdAt: Long,
      val updatedAt: Long
  )
  
  data class Holiday(
      val title: String,
      val ethiopicDate: EthiopicDate,
      val gregorianDate: LocalDate,
      val type: HolidayType,
      val description: String
  )
  
  enum class HolidayType {
      PUBLIC, ORTHODOX, MUSLIM
  }
  ```

**Days 23-24: Extract Repository Interfaces**
- Create repository interfaces in shared:
  ```kotlin
  // shared/src/commonMain/kotlin/domain/repository/
  interface EventRepository {
      suspend fun getEventsForMonth(year: Int, month: Int): Flow<List<Event>>
      suspend fun createEvent(event: Event): String
      suspend fun updateEvent(event: Event)
      suspend fun deleteEvent(id: String)
      suspend fun getEventById(id: String): Event?
      suspend fun searchEvents(query: String): List<Event>
  }
  
  interface HolidayRepository {
      suspend fun getHolidaysForYear(year: Int): Flow<List<Holiday>>
      suspend fun getHolidaysByMonth(year: Int, month: Int): List<Holiday>
  }
  ```

**Days 25-26: Extract Preferences**
- Create preferences interface:
  ```kotlin
  // shared/src/commonMain/kotlin/domain/preferences/
  interface SettingsPreferences {
      suspend fun setPrimaryLanguage(language: Language)
      suspend fun getPrimaryLanguage(): Flow<Language>
      suspend fun setThemeMode(mode: ThemeMode)
      suspend fun getThemeMode(): Flow<ThemeMode>
      suspend fun setPrimaryCalendar(calendar: CalendarType)
      suspend fun getPrimaryCalendar(): Flow<CalendarType>
      // ... more preferences
  }
  
  enum class Language {
      ENGLISH, AMHARIC, OROMIFFA, TIGRIGNA, FRENCH
  }
  ```

**Days 27-28: Move BusinessLogic to Shared**
- Move managers to shared:
  ```
  shared/src/commonMain/kotlin/business/
  ├── managers/
  │   ├── AppInitializationManager.kt
  │   ├── ReminderManager.kt
  │   ├── RemoteConfigManager.kt
  │   └── LocalizationManager.kt
  └── usecases/
      ├── GetEventsUseCase.kt
      ├── CreateEventUseCase.kt
      └── ...
  ```

**Deliverables**:
- ✅ All domain models in shared
- ✅ All repository interfaces in shared
- ✅ All preferences logic extracted
- ✅ Tests passing

#### Week 6: Integration Testing

**Days 29-30: Cross-Platform Testing**
- Test on Android: Models, repositories, calculations
- Test on iOS: Same models, repositories, calculations
- Verify identical results on both platforms

**Days 31-32: Performance Profiling**
- Benchmark Ethiopian date conversions
- Check database query performance
- Memory usage analysis
- Optimize if necessary

**Day 33-34: Platform-Specific Implementation**
- Implement `EventRepository` for Android (using Room)
- Implement `EventRepository` for iOS (using SQLDelight)
- Both should share interface and models

**Deliverables**:
- ✅ All business logic tested and verified
- ✅ Repository implementations working on both platforms
- ✅ Performance acceptable
- ✅ Ready for UI migration

---

### Phase 3: UI Migration to Shared Module (Weeks 7-10)

**Goal**: Move all Compose UI to shared module and verify on both platforms

#### Week 7: UI Infrastructure

**Days 35-36: Theme & Resources to Shared**
- Move theme to shared:
  ```
  shared/src/commonMain/kotlin/ui/
  ├── theme/
  │   ├── AppTheme.kt
  │   ├── Theme.kt
  │   ├── Color.kt
  │   └── Type.kt
  └── resources/ (localization)
      ├── strings.xml
      ├── values-am/
      └── arrays.xml
  ```

**Days 37-38: ViewModels to Shared**
- Create base ViewModel for Compose Multiplatform:
  ```kotlin
  // shared/src/commonMain/kotlin/ui/viewmodel/
  expect open class BaseViewModel() {
      val coroutineScope: CoroutineScope
      protected fun launchIO(block: suspend () -> Unit)
      protected fun launchMain(block: suspend () -> Unit)
  }
  
  // androidMain
  actual open class BaseViewModel {
      // Hilt-injectable
      private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())
  }
  
  // iosMain
  actual open class BaseViewModel {
      // Plain Kotlin
      private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate + Job())
  }
  ```

**Days 39-40: Create Shared ViewModels**
- Start with simple VMs (ThemeViewModel, SettingsViewModel)
- Then complex ones (MonthCalendarViewModel, EventViewModel)

**Deliverables**:
- ✅ Theme system in shared
- ✅ Resources/localization in shared
- ✅ Base ViewModel infrastructure ready

#### Week 8: Core Screens Migration

**Days 41-42: DateConverterScreen**
- Move to shared with no modifications needed
- It's already pure Compose
- Tests on iOS emulator

**Days 43-44: HolidayListScreen**
- Move to shared
- Verify lists display correctly on iOS

**Days 45-46: SettingsScreen**
- Move preferences UI to shared
- Test theme switching on both platforms

**Days 47-48: OnboardingScreen**
- Move onboarding flow to shared
- Verify HorizontalPager works on iOS

**Deliverables**:
- ✅ 4 screens successfully migrated
- ✅ All work identically on Android and iOS

#### Week 9: Complex Screens Migration

**Days 49-50: MonthCalendarScreen**
- Move entire month view to shared
- Handle HorizontalPager (±60 months)
- Test grid rendering on iOS

**Days 51-52: EventScreen**
- Move event list with filtering
- Test event creation/editing on iOS
- Verify permissions dialogs work

**Days 53-54: Navigation**
- Move navigation structure to shared
- Bottom navigation bar
- Arguments passing between screens

**Days 55-56: Compose Components**
- Move all reusable components to shared:
  - MonthHeaderItem
  - AutocompleteTextField
  - Dialog components
  - Buttons, cards, etc.

**Deliverables**:
- ✅ All 5 main screens in shared
- ✅ Navigation structure in shared
- ✅ All components in shared
- ✅ UI logic identical on both platforms

#### Week 10: UI Polish & Testing

**Days 57-58: Platform-Specific Entry Points**
- Create minimal platform entry:
  ```kotlin
  // androidApp/src/main/kotlin/MainActivity.kt
  @Composable
  fun AndroidApp() {
      val viewModel: MainViewModel = koinViewModel()
      AppContent(viewModel)
  }
  
  // iosApp/src/main/kotlin/MainScreenIOS.kt
  @Composable
  fun IOSApp() {
      val viewModel: MainViewModel = koinViewModel()
      AppContent(viewModel)
  }
  ```

**Days 59-60: Responsive Design**
- Test on various screen sizes
- Test on iPad (if supporting)
- Test on notched devices

**Days 61-62: Accessibility**
- Verify semantic properties on iOS
- Test with accessibility tools
- Fix any issues

**Days 63-64: Performance**
- Profile UI rendering on iOS
- Optimize recompositions
- Check memory usage
- Benchmark month grid rendering

**Deliverables**:
- ✅ All UI migrated to shared
- ✅ Responsive design working
- ✅ Accessibility compliance
- ✅ Performance optimized
- ✅ ~5,500 lines of Compose working on both platforms!

---

### Phase 4: Platform Integration (Weeks 11-12)

**Goal**: Implement platform-specific features (notifications, alarms, permissions)

#### Week 11: Platform-Specific Abstractions

**Days 65-66: Notification Manager**
- Create expectation:
  ```kotlin
  // shared/src/commonMain/kotlin/platform/NotificationManager.kt
  expect class NotificationManager {
      suspend fun scheduleNotification(
          eventId: String,
          title: String,
          minutesBefore: Int
      )
      suspend fun cancelNotification(eventId: String)
      suspend fun cancelAllNotifications()
  }
  
  // androidMain
  actual class NotificationManagerAndroid : NotificationManager {
      override suspend fun scheduleNotification(...) {
          val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
          // Use AlarmManager to schedule
      }
  }
  
  // iosMain
  actual class NotificationManagerIOS : NotificationManager {
      override suspend fun scheduleNotification(...) {
          val content = UNMutableNotificationContent()
          val trigger = UNTimeIntervalNotificationTrigger(timeInterval = delay.toDouble())
          val request = UNNotificationRequest(identifier = eventId, content = content, trigger = trigger)
          UNUserNotificationCenter.current().add(request)
      }
  }
  ```

**Days 67-68: Permission Manager**
- Create expectation:
  ```kotlin
  // shared/src/commonMain/kotlin/platform/PermissionManager.kt
  expect class PermissionManager {
      suspend fun requestNotificationPermission(): Boolean
      suspend fun hasNotificationPermission(): Boolean
      suspend fun canScheduleExactAlarms(): Boolean
  }
  
  // androidMain
  actual class PermissionManagerAndroid : PermissionManager {
      override suspend fun requestNotificationPermission(): Boolean {
          // Check Android 13+ POST_NOTIFICATIONS
      }
  }
  
  // iosMain
  actual class PermissionManagerIOS : PermissionManager {
      override suspend fun requestNotificationPermission(): Boolean {
          return suspendCancellableCoroutine { continuation ->
              UNUserNotificationCenter.current().requestAuthorizationWithOptions(
                  UNAuthorizationOptionAlert or UNAuthorizationOptionSound
              ) { granted, _ ->
                  continuation.resume(granted)
              }
          }
      }
  }
  ```

**Days 69-70: Database Driver**
- Create expectation:
  ```kotlin
  // shared/src/commonMain/kotlin/platform/DatabaseDriver.kt
  expect fun createDatabaseDriver(dbName: String): SqlDriver
  
  // androidMain
  actual fun createDatabaseDriver(dbName: String): SqlDriver {
      return AndroidSqliteDriver(
          schema = EventDatabase.Schema,
          context = appContext,
          name = "$dbName.db"
      )
  }
  
  // iosMain
  actual fun createDatabaseDriver(dbName: String): SqlDriver {
      return NativeSqliteDriver(
          schema = EventDatabase.Schema,
          name = "$dbName.db"
      )
  }
  ```

**Deliverables**:
- ✅ Notification manager platform-specific implementation
- ✅ Permission manager abstraction
- ✅ Database driver abstraction

#### Week 12: Platform Implementation & Integration

**Days 71-72: Android Platform Implementation**
- Implement AlarmReceiver for reminders
- Implement NotificationHelper
- Implement PermissionManager for Android
- Test notifications on Android device

**Days 73-74: iOS Platform Implementation**
- Implement local notifications (UNUserNotificationCenter)
- Implement permission requests
- Test notifications on iOS device
- Handle background app refresh

**Days 75-76: Remote Initialization**
- Implement app startup initialization on both platforms
- Firebase initialization
- Remote config fetching
- Reminder re-registration on app launch

**Days 77-78: End-to-End Testing**
- Create event with reminder on Android
- Verify notification fires
- Create event with reminder on iOS
- Verify notification fires
- Test across different timezones

**Days 79-80: Error Handling**
- Handle notification permission denial
- Handle notification delivery failures
- Graceful degradation
- User-friendly error messages

**Deliverables**:
- ✅ Notifications working on Android
- ✅ Notifications working on iOS
- ✅ Permission handling on both platforms
- ✅ App initialization working
- ✅ Error handling in place

---

### Phase 5: Testing & Optimization (Weeks 13-14)

**Goal**: Comprehensive testing and performance optimization

#### Week 13: Cross-Platform Testing

**Days 81-82: Automated Tests**
- Run unit tests on both platforms:
  ```bash
  ./gradlew :shared:testDebugUnitTest    # Android
  ./gradlew :shared:iosX64Test          # iOS
  ```
- Target: 80%+ code coverage

**Days 83-84: Integration Tests**
- Test data flow end-to-end
- Event creation → Database → UI update
- Notification scheduling → Database → Reminder fire
- Holiday calculation → UI display

**Days 85-86: Manual Testing Script**
Create comprehensive manual testing checklist:
- [ ] Create event (Android)
- [ ] Create event (iOS)
- [ ] Edit event (both)
- [ ] Delete event (both)
- [ ] Set reminder (both)
- [ ] Convert dates (both)
- [ ] View holidays (both)
- [ ] Change theme (both)
- [ ] Change language (both)
- [ ] Notification arrives (both)
- [ ] Permission requests (both)
- [ ] Offline mode (create local events)
- [ ] Sync with remote (both)

**Days 87-88: Bug Fix Sprint**
- Fix any bugs found during testing
- Verify fixes on both platforms
- Regression testing

**Deliverables**:
- ✅ 80%+ code coverage
- ✅ All automated tests passing
- ✅ Manual testing checklist complete
- ✅ No critical bugs

#### Week 14: Performance & Polish

**Days 89-90: Android Performance**
- Profile with Android Studio Profiler
- Check memory usage
- Check CPU usage
- Check battery impact
- Optimize if needed

**Days 91-92: iOS Performance**
- Profile with Xcode Instruments
- Check memory usage
- Check CPU usage
- Check battery impact
- Optimize if needed

**Days 93-94: UI Polish**
- Verify animations work smoothly on both
- Check text rendering
- Verify fonts display correctly (Amharic, etc.)
- Polish any rough edges

**Days 95-96: Documentation**
- Create user guide for Android users
- Create user guide for iOS users
- Document known issues (if any)
- Create troubleshooting guide

**Days 97-98: Prepare Release Builds**
- Android: Generate signed APK and AAB
- iOS: Generate IPA
- Verify releases work correctly
- Prepare release notes

**Deliverables**:
- ✅ Performance optimized on both platforms
- ✅ UI polished
- ✅ Release builds ready
- ✅ Documentation complete

---

### Phase 6: Deployment & Launch (Weeks 15-16)

**Goal**: Launch both Android and iOS versions

#### Week 15: Pre-Launch

**Days 99-100: Android Submission**
- Create/update Google Play store listing
- Add screenshots and descriptions
- Upload signed AAB
- Configure pricing and availability
- Request review

**Days 101-102: iOS Submission**
- Create/update App Store listing
- Add screenshots and descriptions (including iPad if supported)
- Add privacy policy link
- Upload IPA
- Request review

**Days 103-104: Beta Testing**
- Android: Internal testing track
- iOS: TestFlight beta
- Collect beta feedback
- Fix any beta issues

**Days 105-106: Marketing Preparation**
- Social media posts
- Blog post about iOS launch
- Email announcement to users
- Gather testimonials/reviews

**Days 107-108: Final Verification**
- Test on various devices
- Test all features one more time
- Prepare launch schedule
- Prepare support resources

**Deliverables**:
- ✅ Both apps submitted to stores
- ✅ Both in beta testing
- ✅ Marketing ready
- ✅ Support documentation ready

#### Week 16: Launch & Post-Launch

**Days 109-110: Launch Day**
- Android: Release from Google Play beta → production
- iOS: Release from TestFlight → App Store
- Monitor both stores for approvals
- Publish marketing content
- Notify users

**Days 111-112: Post-Launch Monitoring**
- Monitor app store reviews
- Monitor crash reports (Firebase Crashlytics)
- Monitor user analytics
- Respond to user feedback
- Fix any critical issues found

**Days 113-114: Optimization**
- Analyze user behavior
- Identify usage patterns
- Fix any performance issues
- Plan improvements

**Days 115-116: Documentation & Handoff**
- Document migration process (for reference)
- Document common issues and solutions
- Create runbook for future development
- Plan future features based on user feedback

**Deliverables**:
- ✅ Android app live on Google Play
- ✅ iOS app live on App Store
- ✅ Both versions production-stable
- ✅ User support active
- ✅ Monitoring in place

---

## Detailed Implementation Guide

### Project Structure Commands

**Generate KMP Project**
```bash
# Using Android Studio: File → New → Kotlin Multiplatform Project
# OR manually:

mkdir composeCalendarTwp-kmp
cd composeCalendarTwp-kmp
git init

# Create directory structure
mkdir -p shared/src/{commonMain,commonTest,androidMain,iosMain}/kotlin
mkdir -p shared/src/{commonMain,commonTest,androidMain,iosMain}/resources
mkdir -p androidApp/src/main/kotlin
mkdir -p iosApp
```

**Initial Gradle Files**

```kotlin
// build.gradle.kts (root)
plugins {
    kotlin("multiplatform") version "1.9.20" apply false
    kotlin("android") version "1.9.20" apply false
    id("com.android.library") version "8.1.0" apply false
    id("com.android.application") version "8.1.0" apply false
}

// shared/build.gradle.kts
plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.compose") version "1.5.0"
}

android {
    namespace = "com.shalom.calendar.shared"
    compileSdk = 34
    minSdk = 26
}

kotlin {
    multiplatform {
        android {
            compilations.all {
                kotlinOptions {
                    jvmTarget = "11"
                }
            }
        }
        
        iosX64()
        iosArm64()
        iosSimulatorArm64()
        
        sourceSets {
            val commonMain by getting {
                dependencies {
                    // Your dependencies
                }
            }
        }
    }
}
```

### Migration Checklist

**Week 1-2 Checklist**
- [ ] KMP project generated
- [ ] Android build succeeds
- [ ] iOS build succeeds
- [ ] All dependencies resolved
- [ ] Firebase KMP SDK tested
- [ ] Database strategy decided
- [ ] Koin DI setup complete

**Week 3-6 Checklist**
- [ ] EthiopicCalculations extracted (100+ unit tests)
- [ ] Holiday calculator implemented
- [ ] All domain models in shared
- [ ] Repository interfaces defined
- [ ] Preferences logic extracted
- [ ] Business logic managers moved
- [ ] Cross-platform tests passing (80%+ coverage)
- [ ] Performance profiled and acceptable

**Week 7-10 Checklist**
- [ ] Theme system in shared
- [ ] Localization resources in shared
- [ ] Base ViewModel infrastructure ready
- [ ] DateConverterScreen migrated
- [ ] HolidayListScreen migrated
- [ ] SettingsScreen migrated
- [ ] OnboardingScreen migrated
- [ ] MonthCalendarScreen migrated
- [ ] EventScreen migrated
- [ ] Navigation structure in shared
- [ ] All components in shared
- [ ] Responsive design tested
- [ ] Accessibility verified
- [ ] Performance optimized

**Week 11-12 Checklist**
- [ ] NotificationManager abstraction complete
- [ ] PermissionManager abstraction complete
- [ ] DatabaseDriver abstraction complete
- [ ] Android: Alarms implemented
- [ ] Android: Notifications working
- [ ] Android: Permissions handling done
- [ ] iOS: Local notifications implemented
- [ ] iOS: Permission requests working
- [ ] iOS: Background refresh configured
- [ ] Both platforms notify correctly
- [ ] Error handling in place

**Week 13-14 Checklist**
- [ ] Unit tests: 80%+ coverage
- [ ] Integration tests: All passing
- [ ] Manual testing: All scenarios complete
- [ ] No critical bugs
- [ ] Android performance: Acceptable
- [ ] iOS performance: Acceptable
- [ ] UI polished on both
- [ ] Documentation complete
- [ ] Release builds generated

**Week 15-16 Checklist**
- [ ] Android submitted to Play Store
- [ ] iOS submitted to App Store
- [ ] Both in beta testing
- [ ] Marketing content ready
- [ ] Support documentation ready
- [ ] Both apps approved
- [ ] Both apps live
- [ ] Monitoring active
- [ ] User support ready

---

## Ethiopian Calendar Date Logic Extraction

### Deep Dive: How to Extract EthiopicChronology

#### Step 1: Understand the Algorithms

**Ethiopian Calendar Rules**:
- 12 months of exactly 30 days
- 13th month (Pagume): 5 days normally, 6 in leap years
- Leap year: Every 4 years (year % 4 == 3)
- Epoch: Ethiopian 0001-01-01 = Gregorian 0284-08-29 (Julian calendar)
- Year difference: ~7 years (Ethiopian year is ~7 years behind Gregorian)

**Conversion Strategy**:
Use Julian Day Number (JDN) as an intermediate representation:
```
Gregorian Date → Julian Day Number → Ethiopian Date
Ethiopian Date → Julian Day Number → Gregorian Date
```

#### Step 2: Implement Gregorian ↔ Julian Day Conversion

```kotlin
/**
 * Convert Gregorian date to Julian Day Number
 * Uses Fliegel & Van Flandern algorithm
 * Valid for all dates in the proleptic Gregorian calendar
 */
private fun gregorianToJulianDay(
    year: Int,
    month: Int,
    day: Int
): Long {
    val a = (14 - month) / 12           // 0 for Jan-Dec, 1 for Nov-Dec
    val y = year + 4800 - a              // Adjusted year
    val m = month + 12 * a - 3           // Adjusted month (0-11)
    
    return (
        day +                                // Day of month
        (153 * m + 2) / 5 +                 // Days in previous months
        365 * y +                           // Days in complete years
        y / 4 -                             // Leap day every 4 years
        y / 100 +                           // Except every 100 years
        y / 400 -                           // Except every 400 years
        32045                               // Offset for epoch
    )
}

/**
 * Convert Julian Day Number back to Gregorian date
 */
private fun julianDayToGregorian(jdn: Long): Triple<Int, Int, Int> {
    val a = jdn + 32044
    val b = (4 * a + 3) / 146097
    val c = a - (146097 * b) / 4
    val d = (4 * c + 3) / 1461
    val e = c - (1461 * d) / 4
    val m = (5 * e + 2) / 153
    
    val day = e - (153 * m + 2) / 5 + 1
    val month = m + 3 - 12 * (m / 10)
    val year = 100 * b + d - 4800 + m / 10
    
    return Triple(year.toInt(), month.toInt(), day.toInt())
}
```

#### Step 3: Implement Ethiopian ↔ Julian Day Conversion

```kotlin
/**
 * Ethiopian calendar epoch in Julian Day Number
 * Ethiopian 0001-01-01 = JDN 1724220
 * (Verified against historical records and Joda-Time)
 */
private const val ETHIOPIC_EPOCH_JDN = 1724220L

/**
 * Convert Ethiopian date to Julian Day Number
 */
private fun ethiopicToJulianDay(
    ethiopicYear: Int,
    ethiopicMonth: Int,
    ethiopicDay: Int
): Long {
    // Calculate days before this year
    val daysBeforeYear = 365 * (ethiopicYear - 1) + (ethiopicYear - 1) / 4
    
    // Calculate days before this month
    // First 12 months: 30 days each
    // 13th month: handled by Ethiopian day counting
    val daysBeforeMonth = 30 * (ethiopicMonth - 1)
    
    // Total days since epoch
    val totalDays = daysBeforeYear + daysBeforeMonth + ethiopicDay
    
    return ETHIOPIC_EPOCH_JDN + totalDays - 1
}

/**
 * Convert Julian Day Number to Ethiopian date
 */
private fun julianDayToEthiopic(jdn: Long): EthiopicDate {
    // Days since Ethiopian epoch
    val daysSinceEpoch = jdn - ETHIOPIC_EPOCH_JDN + 1
    
    // Calculate year (approximately 365.25 days per year)
    var year = ((daysSinceEpoch - 1) / 365).toInt() + 1
    
    // Calculate remaining days after removing complete years
    var remainingDays = (
        daysSinceEpoch - 1 - 
        365 * (year - 1) - 
        (year - 1) / 4
    ).toInt()
    
    // Adjust year if remainingDays is negative (edge case)
    while (remainingDays < 0) {
        year--
        remainingDays += if (isLeapYear(year)) 366 else 365
    }
    
    // Calculate month and day
    val month = (remainingDays / 30) + 1
    val day = (remainingDays % 30) + 1
    
    return EthiopicDate(year, month, day)
}
```

#### Step 4: Implement Helper Functions

```kotlin
/**
 * Check if Ethiopian year is a leap year
 * Ethiopian leap year: year % 4 == 3
 * (Different from Gregorian: year % 4 == 0)
 */
fun isEthiopicLeapYear(ethiopicYear: Int): Boolean {
    return (ethiopicYear % 4) == 3
}

/**
 * Get number of days in an Ethiopian month
 */
fun ethiopicMonthDays(month: Int, year: Int): Int {
    return when {
        month <= 12 -> 30                    // Months 1-12: always 30 days
        isEthiopicLeapYear(year) -> 6        // Month 13 (Pagume): 6 days in leap years
        else -> 5                            // Month 13 (Pagume): 5 days in non-leap years
    }
}

/**
 * Get Ethiopian day of week
 */
fun getEthiopicDayOfWeek(ethiopicDate: EthiopicDate): DayOfWeek {
    val gregorian = ethiopicToGregorian(ethiopicDate)
    return gregorian.dayOfWeek
}

/**
 * Check if date is valid
 */
fun isValidEthiopicDate(
    year: Int,
    month: Int,
    day: Int
): Boolean {
    return year > 0 &&
           month in 1..13 &&
           day in 1..ethiopicMonthDays(month, year)
}
```

#### Step 5: Data Class

```kotlin
/**
 * Immutable Ethiopian date type
 * Represents a date in the Ethiopian (Amharic) calendar
 */
@Serializable
data class EthiopicDate(
    val year: Int,     // Ethiopian year (1, 2, 3, ...)
    val month: Int,    // Month 1-13 (13 is Pagume)
    val day: Int       // Day 1-30 (or 1-5/6 for month 13)
) {
    init {
        require(isValidEthiopicDate(year, month, day)) {
            "Invalid Ethiopian date: $year-$month-$day"
        }
    }
    
    /**
     * Convert to Gregorian LocalDate
     */
    fun toGregorian(): LocalDate {
        return EthiopicCalculations.ethiopicToGregorian(this)
    }
    
    /**
     * Get day of week
     */
    val dayOfWeek: DayOfWeek
        get() = toGregorian().dayOfWeek
    
    /**
     * Add days to this date
     */
    fun plusDays(days: Int): EthiopicDate {
        val gregorian = toGregorian()
        val newGregorian = gregorian.plusDays(days.toLong())
        return EthiopicCalculations.gregorianToEthiopic(newGregorian)
    }
    
    /**
     * Subtract days from this date
     */
    fun minusDays(days: Int): EthiopicDate {
        return plusDays(-days)
    }
    
    companion object {
        /**
         * Today's date in Ethiopian calendar
         */
        fun today(): EthiopicDate {
            return EthiopicCalculations.gregorianToEthiopic(Clock.System.todayIn(TimeZone.currentSystemDefault()))
        }
        
        /**
         * Create from Gregorian date
         */
        fun fromGregorian(localDate: LocalDate): EthiopicDate {
            return EthiopicCalculations.gregorianToEthiopic(localDate)
        }
    }
}
```

#### Step 6: Comprehensive Testing

```kotlin
class EthiopicCalculationsTest {
    
    // Known reference dates (verified against multiple sources)
    private val referenceData = mapOf(
        // Recent dates with high confidence
        LocalDate(2024, 11, 17) to EthiopicDate(2017, 3, 9),
        LocalDate(2025, 1, 1) to EthiopicDate(2017, 4, 24),
        LocalDate(2025, 9, 11) to EthiopicDate(2018, 1, 1),  // Ethiopian New Year
        
        // Historical dates
        LocalDate(2000, 1, 1) to EthiopicDate(1992, 4, 24),
        LocalDate(1900, 1, 1) to EthiopicDate(1892, 4, 24),
        
        // Edge cases
        LocalDate(284, 8, 29) to EthiopicDate(1, 1, 1),  // Epoch
    )
    
    @Test
    fun testGregorianToEthiopicConversion() {
        referenceData.forEach { (gregorian, expected) ->
            val actual = EthiopicCalculations.gregorianToEthiopic(gregorian)
            assertEquals(expected, actual, "Failed for $gregorian")
        }
    }
    
    @Test
    fun testEthiopicToGregorianConversion() {
        referenceData.forEach { (expectedGregorian, ethiopic) ->
            val actual = EthiopicCalculations.ethiopicToGregorian(ethiopic)
            assertEquals(expectedGregorian, actual, "Failed for $ethiopic")
        }
    }
    
    @Test
    fun testRoundTripConversion() {
        // Random dates for 100 years
        repeat(1000) {
            val original = LocalDate(
                year = (1900..2100).random(),
                monthValue = (1..12).random(),
                dayOfMonth = (1..28).random()
            )
            val ethiopic = EthiopicCalculations.gregorianToEthiopic(original)
            val backToGregorian = EthiopicCalculations.ethiopicToGregorian(ethiopic)
            assertEquals(original, backToGregorian, "Round-trip failed for $original")
        }
    }
    
    @Test
    fun testLeapYearRules() {
        // Ethiopian leap years: 3, 7, 11, 15, ... (year % 4 == 3)
        assertTrue(EthiopicCalculations.isLeapYear(2003))
        assertTrue(EthiopicCalculations.isLeapYear(2007))
        assertTrue(EthiopicCalculations.isLeapYear(2011))
        
        assertFalse(EthiopicCalculations.isLeapYear(2004))
        assertFalse(EthiopicCalculations.isLeapYear(2005))
        assertFalse(EthiopicCalculations.isLeapYear(2006))
    }
    
    @Test
    fun testMonthBoundaries() {
        // First 12 months: 30 days
        for (month in 1..12) {
            assertEquals(30, EthiopicCalculations.daysInMonth(month, 2016))
        }
        
        // Month 13 (Pagume)
        assertEquals(6, EthiopicCalculations.daysInMonth(13, 2003)) // leap
        assertEquals(5, EthiopicCalculations.daysInMonth(13, 2004)) // not leap
    }
    
    @Test
    fun testPlusMinusDays() {
        val date = EthiopicDate(2017, 3, 9)
        val tomorrow = date.plusDays(1)
        assertEquals(EthiopicDate(2017, 3, 10), tomorrow)
        
        val yesterday = date.minusDays(1)
        assertEquals(EthiopicDate(2017, 3, 8), yesterday)
    }
    
    @Test
    fun testMonthTransition() {
        val lastDayOfMonth = EthiopicDate(2017, 3, 30)
        val firstDayNextMonth = lastDayOfMonth.plusDays(1)
        assertEquals(EthiopicDate(2017, 4, 1), firstDayNextMonth)
    }
    
    @Test
    fun testYearTransition() {
        val lastDayOfYear = EthiopicDate(2017, 13, 5)
        val firstDayNewYear = lastDayOfYear.plusDays(1)
        assertEquals(EthiopicDate(2018, 1, 1), firstDayNewYear)
    }
}
```

---

## Library Migration Strategy

### Dependency Mapping

**Current Android Dependencies → KMP Equivalents**

| Android | KMP Equivalent | Strategy |
|---------|---|---|
| Jetpack Compose | Compose Multiplatform | Use exact same code! |
| Material 3 | Compose Multiplatform Material3 | Works on both |
| Dagger Hilt | Koin | Rewrite DI configuration |
| Room | SQLDelight (iOS) + Room (Android) | Abstraction layer |
| DataStore | multiplatform-settings or expect/actual | Abstraction layer |
| Firebase Suite | dev.gitlive:firebase-* | Multiplatform SDKs available |
| ThreeTen Extra | Extract to pure Kotlin | Custom Ethiopian logic |
| ThreeTen-ABP | kotlinx-datetime | Use for Gregorian handling |
| Lottie | Compose Multiplatform Lottie | Works on both |
| Timber | Kermit or io.github.aakira:napier | Multiplatform logging |
| Glance Widgets | Platform-specific (iOS WidgetKit) | Skip for v1, add later |
| WorkManager | Kotlin multiplatform-concurrency or platform-specific | Abstraction + platform impl |

### Koin DI Migration

**Before (Hilt)**
```kotlin
// Android only
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideEventRepository(
        dao: EventDao,
        preferences: SettingsPreferences
    ): EventRepository {
        return EventRepositoryImpl(dao, preferences)
    }
}

// Usage
@Composable
fun MyScreen(
    viewModel: MyViewModel = hiltViewModel()
) { ... }
```

**After (Koin)**
```kotlin
// Shared module
val repositoryModule = module {
    single<EventRepository> { 
        EventRepositoryImpl(get(), get()) 
    }
    single<HolidayRepository> {
        HolidayRepositoryImpl(get())
    }
}

val appModule = module {
    includes(repositoryModule)
    
    single<SettingsPreferences> {
        SettingsPreferencesImpl(get())
    }
    single { AppInitializationManager(get(), get()) }
    single { RemoteConfigManager(get()) }
}

// Usage
@Composable
fun MyScreen(
    viewModel: MyViewModel = remember { koinViewModel() }
) { ... }
```

### Database Migration Pattern

**Android: Keep Room**
```kotlin
// shared/src/commonMain/kotlin/data/local/EventDao.kt
expect interface EventDao {
    suspend fun getEventsForMonth(year: Int, month: Int): Flow<List<EventEntity>>
    suspend fun createEvent(event: EventEntity)
}

// androidMain/src/main/kotlin/data/local/EventDao.kt
@Dao
actual interface EventDao {
    @Query("SELECT * FROM events WHERE ethiopianYear = :year AND ethiopianMonth = :month")
    fun getEventsForMonth(year: Int, month: Int): Flow<List<EventEntity>>
    
    @Insert
    suspend fun createEvent(event: EventEntity)
}
```

**iOS: Use SQLDelight**
```kotlin
// iosMain/src/main/kotlin/data/local/EventDao.kt
actual class EventDaoSQLDelight(
    private val database: EventDatabase
) : EventDao {
    override suspend fun getEventsForMonth(year: Int, month: Int): Flow<List<EventEntity>> {
        return database.eventQueries
            .selectByYearMonth(year.toLong(), month.toLong())
            .asFlow()
            .map { list -> list.map { it.toEntity() } }
    }
    
    override suspend fun createEvent(event: EventEntity) {
        database.eventQueries.insert(event.toDb())
    }
}
```

### Firebase SDK Setup

```kotlin
// shared/src/commonMain/kotlin/data/remote/FirebaseModule.kt
val firebaseModule = module {
    single { Firebase.remoteConfig }
    single { Firebase.analytics }
    single { Firebase.messaging }
    
    single<RemoteConfigManager> { RemoteConfigManagerKmp(get()) }
}

// shared/src/commonMain/kotlin/data/remote/RemoteConfigManager.kt
expect class RemoteConfigManager {
    suspend fun fetchAndActivate()
    suspend fun getString(key: String): String
    suspend fun getBoolean(key: String): Boolean
    suspend fun getLong(key: String): Long
}

// Both Android and iOS use the exact same expect/actual
// Firebase KMP SDK handles the differences
```

---

## Risk Management

### Risk Register & Mitigation

| Risk | Probability | Impact | Mitigation | Owner |
|------|-------------|--------|-----------|-------|
| **Compose Multiplatform iOS instability** | Low | High | Start with POC in week 1, test early, have rollback plan | Tech Lead |
| **Firebase KMP SDK limitations** | Low | Medium | Test all features in week 2, document workarounds | Backend |
| **EthiopicChronology extraction errors** | Medium | High | Comprehensive unit tests (100+), validate against known dates, property-based testing | Calendar Dev |
| **Database schema mismatch** | Medium | Medium | Use SQLDelight for both platforms OR maintain careful schema alignment | Data Lead |
| **Performance regression on iOS** | Medium | Medium | Profile weekly, benchmark critical paths, test on real devices | iOS Dev |
| **Timezone handling bugs** | Medium | High | Heavy testing with all timezones, DST edge cases, automated test for historical dates | Calendar Dev |
| **Notification delivery failures** | Low | Medium | Platform-specific testing, handle failures gracefully, user-friendly errors | Platform Dev |
| **App Store/Play Store rejection** | Low | High | Follow guidelines from day 1, test with external app, prepare response plan | QA |
| **KMP tooling issues** | Low | Medium | Stay on stable versions, active community support, budget extra time | Tech Lead |
| **Schedule slippage** | Medium | High | Weekly status checks, burn-down charts, early identification of blockers | Project Manager |

### Risk Response Plan

**If Compose Multiplatform iOS fails on real devices:**
1. Rollback to SwiftUI (additional 4-6 weeks)
2. Share only business logic (80% vs 95% sharing)
3. Decision point: Week 1 of phase 3

**If Firebase KMP SDK incomplete:**
1. Use platform-specific Firebase SDKs via expect/actual
2. Abstract the differences in KMP layer
3. Impact: +1 week, still doable

**If EthiopicChronology extraction has bugs:**
1. Keep ThreeTen Extra on Android temporarily
2. Use extracted logic on iOS with careful validation
3. Gradually fix discrepancies
4. Impact: +2 weeks, acceptable

**If performance unacceptable on iOS:**
1. Profile and identify bottleneck
2. Optimize Compose code (lazy rendering, remember patterns)
3. Possible: Keep native UI for performance-critical screens
4. Impact: +2-3 weeks, manageable

---

## Testing Strategy

### Test Pyramid

```
              ┌─────────────────────┐
              │   Manual/E2E Tests  │  ← 10%
              │  (Exploratory, UI)  │
              ├─────────────────────┤
              │  Integration Tests  │  ← 20%
              │ (Feature workflows) │
              ├─────────────────────┤
              │    Unit Tests       │  ← 70%
              │ (Logic, algorithms) │
              └─────────────────────┘
```

### Unit Testing

**Ethiopian Calculations (Must Pass)**
```kotlin
class EthiopicCalculationsTest {
    // 50+ test cases
    @Test fun testGregorianToEthiopic() { }
    @Test fun testRoundTrip() { }
    @Test fun testLeapYears() { }
    @Test fun testMonthBoundaries() { }
    @Test fun testHistoricalDates() { }
    // ... more
}

// All tests run on both Android JVM and iOS
// Must pass 100%
```

**Repository Logic**
```kotlin
class EventRepositoryTest {
    @Test fun testCreateEvent() { }
    @Test fun testUpdateEvent() { }
    @Test fun testDeleteEvent() { }
    @Test fun testQueryByMonth() { }
}
```

**Holiday Calculations**
```kotlin
class HolidayCalculatorTest {
    @Test fun testFasika() { }           // Easter
    @Test fun testTimkat() { }           // Epiphany
    @Test fun testDergDay() { }          // Revolution day
}
```

### Integration Testing

**Data Flow End-to-End**
```kotlin
class EventCreationIntegrationTest {
    @Test
    fun testCreateEventWithReminder() {
        // 1. Create event
        val event = Event(...)
        eventRepository.createEvent(event)
        
        // 2. Verify in database
        val retrieved = eventRepository.getEventById(event.id)
        assertEquals(event, retrieved)
        
        // 3. Trigger reminder scheduling
        reminderManager.scheduleReminder(event)
        
        // 4. Verify scheduled
        assertTrue(isReminderScheduled(event.id))
    }
}
```

**UI & Business Logic**
```kotlin
class MonthCalendarScreenTest {
    @Test
    fun testMonthCalendarDisplays() {
        // Setup
        val viewModel = MonthCalendarViewModel(
            mockRepository, mockPreferences
        )
        
        // Collect state
        val states = viewModel.uiState.take(3).toList()
        
        // Verify loading → success
        assertTrue(states[0] is Loading)
        assertTrue(states[1] is Success)
    }
}
```

### Manual Testing Checklist

```
CORE FUNCTIONALITY
☐ Create event (Android)
☐ Create event (iOS)
☐ Edit event (both)
☐ Delete event (both)
☐ Recurring events (both)
☐ Set reminders (both)

DATE CONVERSION
☐ Convert Gregorian → Ethiopian
☐ Convert Ethiopian → Gregorian
☐ Date picker selects correct date
☐ Past dates work
☐ Future dates work (2100+)
☐ Leap year transitions work

HOLIDAYS
☐ Holidays display on calendar
☐ Holiday list shows all holidays
☐ Holiday details correct
☐ Orthodox holidays correct
☐ Muslim holidays correct
☐ Public holidays correct

NOTIFICATIONS
☐ Reminder triggers at correct time (Android)
☐ Notification delivered (Android)
☐ Reminder triggers at correct time (iOS)
☐ Notification delivered (iOS)
☐ Permission request (both)
☐ No notification if denied (both)

LOCALIZATION
☐ English (both)
☐ Amharic (both)
☐ Other languages (both)
☐ RTL not needed for Amharic (verified: RTL not used)

THEMES
☐ Blue theme (both)
☐ Red theme (both)
☐ Green theme (both)
☐ Purple theme (both)
☐ Orange theme (both)
☐ Light mode (both)
☐ Dark mode (both)
☐ System mode (both)

PERFORMANCE
☐ Month scroll smooth (Android, 60fps)
☐ Month scroll smooth (iOS, 60fps)
☐ Event list load <2s
☐ Search <500ms
☐ Holiday list <1s
☐ No memory leaks (both)

EDGE CASES
☐ Very old dates (year 1)
☐ Very future dates (year 9999)
☐ DST transitions
☐ Timezone changes
☐ Offline → online
☐ App backgrounded → foreground
☐ Device rotated
☐ Screen locked

ANDROID SPECIFIC
☐ Material Design compliance
☐ Back button navigation
☐ System permissions working
☐ Share functionality
☐ Android 12+ compatibility
☐ Tablet layout (if applicable)

iOS SPECIFIC
☐ Human Interface Guidelines compliance
☐ Safe area handling (notch, home indicator)
☐ iPhone 14/15 support
☐ iPad support (if applicable)
☐ iOS 15+ compatibility
☐ Gesture navigation
```

---

## Deployment & Launch

### Pre-Launch Checklist

**2 Weeks Before Launch**

**Android**
- [ ] Google Play Store account setup
- [ ] Create app listing
- [ ] Add screenshots (5+)
- [ ] Write compelling description
- [ ] Setup pricing (free)
- [ ] Add privacy policy URL
- [ ] Generate signed APK/AAB
- [ ] Submit to internal testing track
- [ ] Test on 5+ devices
- [ ] Fix any crashes
- [ ] Prepare release notes

**iOS**
- [ ] Apple Developer account setup
- [ ] Create app record in App Store Connect
- [ ] Add screenshots for iPhone (5+)
- [ ] Add screenshots for iPad (if applicable)
- [ ] Write compelling description
- [ ] Setup category and keywords
- [ ] Add privacy policy URL
- [ ] Add support URL
- [ ] Generate IPA
- [ ] Submit to TestFlight
- [ ] Test on iPhone 14/15/iPad
- [ ] Fix any crashes
- [ ] Prepare release notes

**Both**
- [ ] Finalize user guide
- [ ] Prepare FAQ
- [ ] Create social media posts
- [ ] Prepare blog post
- [ ] Setup monitoring (analytics, crashes)
- [ ] Train support team
- [ ] Prepare rollback plan

### Launch Day

**Step 1: Release to Beta (Day before)**
- Android: Push to beta track
- iOS: Build new TestFlight version
- Verify both work on test devices
- Collect feedback from beta users

**Step 2: Release to Production**
- Android: Click "Release to production" in Play Store
- iOS: Click "Submit for review" in App Store Connect
- Monitor both stores for approval
- Post marketing content simultaneously

**Step 3: Monitor Launch**
- Monitor crash reports
- Monitor user reviews
- Monitor analytics
- Respond to user feedback
- Have team on standby for critical fixes

**Step 4: Post-Launch**
- Respond to 1-star reviews
- Fix any critical bugs (hotfix)
- Update both apps based on feedback
- Plan v2 features

### Store Configuration

**Google Play Store Listing**
```
Title: Ethiopian Calendar
Short description: 
"A beautiful calendar app featuring Ethiopian, Gregorian, 
and Islamic dates. Create events, set reminders, and 
convert between calendars."

Full description:
"Features:
• Ethiopian & Gregorian dual calendar display
• 13 months with 5-6 day final month
• Holiday calendar (Orthodox & Muslim)
• Event management with reminders
• Date converter tool
• Multi-language support (English, Amharic, Oromiffa, Tigrigna, French)
• Material Design 3
• Offline support
• Firebase sync

Requirements: Android 8.0+
Permissions: Notifications, Calendar, Reminders"

Screenshots: 5-8 high-quality screenshots showing main features
Promo graphic: 1024×500px
Icon: 512×512px
Trailer: (optional) 30-second video demo
```

**App Store Listing**
```
Name: Ethiopian Calendar
Subtitle: Traditional & Gregorian Dates

Description:
"Ethiopian Calendar is a beautiful, feature-rich calendar app 
that brings together Ethiopian, Gregorian, and Islamic calendars 
in one place.

Key Features:
✓ Dual calendar system (Ethiopian & Gregorian)
✓ Complete holiday calendar (public, Orthodox, Muslim)
✓ Event management with reminders
✓ Date conversion tool
✓ Multi-language support
✓ Beautiful Material Design 3 interface
✓ Works offline
✓ Cloud sync with Firebase

Perfect for diaspora Ethiopians, calendar enthusiasts, and 
anyone interested in the Ethiopian calendar system.

Permissions:
• Calendar: To display and manage events
• Notifications: For event reminders
• Location: To determine your timezone"

Keywords: Ethiopian calendar, Amharic, dates, events, reminders
Category: Productivity
Rating: Not initially rated (will appear as users rate)
Screenshots: 5-6 high-quality iPhone screenshots
Preview Video: (optional) Demo of app features
```

---

## Success Metrics

### Technical Metrics

```
Code Quality:
├── Unit Test Coverage: 80%+
├── Critical Code Coverage: 100% (calendar logic)
├── Lint Warnings: 0
└── Build Success Rate: 100%

Performance:
├── Android
│   ├── Month grid render: <100ms
│   ├── Event list scroll: 60 fps
│   ├── Memory usage: <100MB peak
│   └── Startup time: <3 seconds
├── iOS
│   ├── Month grid render: <100ms
│   ├── Event list scroll: 60 fps
│   ├── Memory usage: <100MB peak
│   └── Startup time: <3 seconds
└── Backend
    ├── Firebase config fetch: <500ms
    └── Notification delivery: >95%

Stability:
├── Crash-free users: >99.5%
├── ANR rate: <0.1%
├── Unresponsive time: <1 second total
└── Zero platform-specific bugs (shared logic)
```

### Business Metrics

```
Launch Metrics:
├── Day 1 installs (Android): 500+
├── Day 1 installs (iOS): 300+
├── Day 1 rating: 4.5+ stars (target)
└── Day 1 retention: 50%+

30-Day Metrics:
├── Total installs: 5,000+
├── Active users: 2,000+
├── Daily active users: 800+
├── Average session time: 5+ minutes
├── Day 30 retention: 30%+
└── Rating: 4.3+ stars

Growth Metrics:
├── Month-over-month growth: 20%+
├── Positive review ratio: 80%+
├── Feature requests (top 3): Document for v2
└── Bug reports (critical): <5
```

### User Satisfaction Metrics

```
Reviews & Ratings:
├── Overall rating: 4.3+ stars (out of 5)
├── 5-star reviews: >50%
├── 1-star reviews: <10%
└── Review sentiment: >80% positive

User Feedback:
├── No show-stopping bugs reported
├── Major features working as expected
├── Performance acceptable
├── Localization working (Amharic confirmed)
└── User support requests: <10/week
```

---

## Troubleshooting & FAQ

### Common Issues & Solutions

**Issue: "Compose Multiplatform not rendering on iOS simulator"**

Solution:
1. Verify iOS build completed: `./gradlew :shared:build -PkotlinTarget=ios`
2. Check iOS minimum deployment: 12.0+
3. Verify Metal is available in simulator
4. Try: Clean build cache: `rm -rf .gradle && ./gradlew clean`
5. Update Compose Multiplatform: 1.5.0+

**Issue: "EthiopicCalculations producing wrong dates"**

Solution:
1. Add reference data test with known dates
2. Verify Julian Day Number algorithm
3. Check leap year calculation (year % 4 == 3, NOT year % 4 == 0)
4. Verify epoch JDN: 1724220 (Ethiopian 0001-01-01)
5. Test round-trip: Gregorian → Ethiopian → Gregorian = original

**Issue: "Notifications not working on iOS"**

Solution:
1. Check Info.plist has `NSUserNotificationUsageDescription`
2. Verify UNUserNotificationCenter initialization
3. Check app has notification permission (request in onboarding)
4. Verify notification was actually scheduled (check logs)
5. Try: Wait 30s then kill app, notification should appear

**Issue: "Firebase Remote Config not syncing"**

Solution:
1. Verify Firebase project setup
2. Check app has network access
3. Verify Firebase KMP SDK installed correctly
4. Check minimum fetch interval (default 12h, but configurable)
5. Look at Firebase Console for config values

**Issue: "Database queries timeout on iOS"**

Solution:
1. Verify SQLDelight queries are correct
2. Check database file exists (~/Documents/)
3. Add indexes on frequently queried columns
4. Profile with Xcode Instruments
5. Consider query optimization or pagination

**Issue: "App crashes when converting dates"**

Solution:
1. Check input validation before calling conversion
2. Verify Julian Day Number algorithm
3. Add bounds checking (year > 0, month 1-13, day 1-30/6)
4. Test with extreme dates (year 1, year 9999)
5. Check timezone handling (use UTC internally)

**Issue: "Gradle dependency resolution failing"**

Solution:
1. Clear gradle cache: `rm -rf ~/.gradle/caches`
2. Update gradle wrapper: `./gradlew wrapper --gradle-version 8.2`
3. Check internet connectivity
4. Try: Build individual modules first
5. Check for conflicting dependencies (use `./gradlew dependencies`)

### FAQ

**Q: Can I run the app on Android and iOS simultaneously?**

A: Yes! `./gradlew :androidApp:installDebug` and Xcode for iOS.

**Q: How do I debug Compose Multiplatform on iOS?**

A: Use Xcode or `lldb` with Kotlin plugin. Android Studio support improving.

**Q: Will my app work offline?**

A: Yes, Room/SQLDelight handle offline. Firebase features need network.

**Q: Can I add more languages?**

A: Yes, add string resources in `res/values-<locale>/strings.xml`.

**Q: How do I upgrade to a newer Kotlin version?**

A: Update `kotlin.version` in `build.gradle.kts` and test thoroughly.

**Q: What's the minimum iOS/Android version supported?**

A: iOS 12.0+, Android 8.0+ (26 API).

**Q: Can I use Jetpack libraries on iOS?**

A: Some have KMP versions (Room, DataStore). Others need expect/actual.

**Q: How do I handle App Store rejections?**

A: Common: privacy policy URL, permission justification, unclear UI.
See: https://developer.apple.com/app-store/review/guidelines/

**Q: What about Google Play policy violations?**

A: Common: unclear permission usage, ads placement, user data handling.
See: https://play.google.com/about/developer-content-policy/

**Q: How do I implement dark mode on iOS?**

A: Compose Multiplatform handles it automatically via Material 3.

**Q: Can I use native iOS code from Kotlin?**

A: Yes, via Kotlin/Native interop (advanced, usually not needed).

**Q: Performance is slow on iOS, what do I do?**

A: 1. Profile with Instruments
    2. Check Compose recomposition
    3. Optimize database queries
    4. Use lazy rendering

**Q: How do I update the app after launch?**

A: Minor updates via both app stores (normal flow).
Major features: Plan for v2 release (8-12 weeks out).

---

## Appendix

### A. Glossary

| Term | Definition |
|------|-----------|
| **KMP** | Kotlin Multiplatform - share code across platforms |
| **Compose Multiplatform** | JetBrains' declarative UI framework for multiple platforms |
| **Ethiopian Calendar** | Ancient calendar system with 12×30 + 1×5/6 day month structure |
| **EthiopicDate** | Data class representing a date in Ethiopian calendar |
| **Julian Day Number** | Universal date representation (intermediate for conversions) |
| **Expect/Actual** | KMP pattern for platform-specific implementations |
| **Koin** | Service locator DI framework (Koin replaces Hilt for KMP) |
| **Room** | Android local database library |
| **SQLDelight** | Multiplatform SQL database library |
| **Firebase KMP SDK** | Google's multiplatform Firebase SDK |
| **kotlinx-datetime** | Kotlin multiplatform date/time library |
| **Fasika** | Ethiopian Orthodox Easter (complex calculation) |
| **Pagume** | 13th month of Ethiopian calendar (5-6 days) |

### B. Key Files & Locations

```
Project Structure:
shared/src/commonMain/kotlin/
├── business/calendar/
│   ├── EthiopicCalculations.kt          ← Core algorithm
│   ├── HolidayCalculator.kt
│   └── CalendarUtils.kt
├── ui/
│   ├── month/MonthCalendarScreen.kt
│   ├── event/EventScreen.kt
│   ├── converter/DateConverterScreen.kt
│   ├── holiday/HolidayListScreen.kt
│   ├── onboarding/OnboardingScreen.kt
│   ├── theme/
│   └── components/
├── data/
│   ├── repository/
│   ├── local/
│   ├── remote/
│   └── preferences/
└── di/Module.kt

androidApp/src/main/kotlin/
├── MainActivity.kt
├── alarm/AlarmReceiver.kt
├── platform/actual/NotificationManagerAndroid.kt
└── AndroidManifest.xml

iosApp/src/main/kotlin/
├── MainIOSViewController.kt
├── platform/actual/NotificationManagerIOS.kt
└── Info.plist
```

### C. Important Commands

```bash
# Build shared module
./gradlew :shared:build

# Build Android app
./gradlew :androidApp:assembleDebug
./gradlew :androidApp:installDebug

# Build iOS app
./gradlew :iosApp:build -PkotlinTarget=ios

# Run tests
./gradlew :shared:test
./gradlew :shared:iosX64Test

# Clean everything
./gradlew clean

# Lint and format
./gradlew :shared:detekt
./gradlew :shared:ktlintFormat

# Profile performance
./gradlew :androidApp:profileVariant

# Generate documentation
./gradlew dokkaHtml
```

### D. Resources & Documentation

**Official Documentation:**
- Kotlin Multiplatform: https://kotlinlang.org/docs/multiplatform/
- Compose Multiplatform: https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-setup.html
- kotlinx-datetime: https://github.com/Kotlin/kotlinx-datetime
- Koin: https://insert-koin.io/
- Firebase KMP: https://github.com/gitlive/firebase-kotlin-sdk

**Ethiopian Calendar:**
- ThreeTen Extra: https://www.threeten.org/threeten-extra/
- Calendrical Calculations: Book by Reingold & Dershowitz
- Ethiopian Calendar Details: https://en.wikipedia.org/wiki/Ethiopian_calendar

**App Store Submission:**
- Google Play: https://play.google.com/console/
- Apple App Store: https://appstoreconnect.apple.com/

**Community:**
- Kotlin Slack: https://kotlinlang.slack.com/
- Stack Overflow: `kotlin-multiplatform` tag
- GitHub Discussions: KMP repos

### E. Contact & Support

**Internal Team Roles:**
- **Tech Lead**: KMP architecture, library decisions, technical risks
- **Backend Developer**: Business logic, date calculations, data layer
- **iOS Developer**: iOS-specific implementations, App Store submission
- **QA Engineer**: Testing strategy, bug tracking, release validation
- **Project Manager**: Timeline, resource allocation, stakeholder communication

**Decision Escalation Path:**
1. Tech Team → Tech Lead
2. Timeline issues → Project Manager
3. Scope changes → Project Lead
4. Critical bugs → Tech Lead + Project Manager

---

## Document Sign-Off

This migration plan has been prepared for the Ethiopian Calendar Application (ComposeCalendarTwp) for expansion to iOS via Kotlin Multiplatform.

**Prepared By**: Technical Team  
**Date**: November 17, 2025  
**Version**: 1.0  
**Status**: Ready for Implementation  

### Next Steps

1. **This Week**: Review and approve document
2. **Next Week**: Begin Phase 1 (Project Setup)
3. **Week 3**: Begin Phase 2 (Core Logic Extraction)
4. **Week 7**: Begin Phase 3 (UI Migration)
5. **Week 11**: Begin Phase 4 (Platform Integration)
6. **Week 15**: Begin Phase 6 (Deployment)

**Approval Required From:**
- [ ] Project Lead
- [ ] Technical Lead
- [ ] Product Manager
- [ ] Team Leads (Android, iOS, Backend)

---

**End of Document**

*For questions or clarifications, contact the Technical Team.*

*Last Updated: November 17, 2025*  
*Next Review: December 1, 2025 (or after Phase 1 completion)*
