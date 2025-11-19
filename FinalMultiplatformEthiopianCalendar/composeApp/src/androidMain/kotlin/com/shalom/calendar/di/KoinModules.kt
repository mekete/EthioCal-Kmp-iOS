package com.shalom.calendar.di

import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.shalom.calendar.alarm.AlarmScheduler
import com.shalom.calendar.data.analytics.AnalyticsManager
import com.shalom.calendar.data.analytics.UserPropertiesManager
import com.shalom.calendar.data.initialization.AppInitializationManager
import com.shalom.calendar.data.initialization.ReminderReregistrationManager
import com.shalom.calendar.data.local.CalendarDatabase
import com.shalom.calendar.data.migration.LegacyDataMigrator
import com.shalom.calendar.data.permissions.PermissionManager
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.data.preferences.ThemePreferences
import com.shalom.calendar.data.provider.ResourceProvider
import com.shalom.calendar.data.remote.RemoteConfigManager
import com.shalom.calendar.data.repository.EventRepository
import com.shalom.calendar.data.repository.HolidayRepository
import com.shalom.calendar.domain.calculator.MuslimHolidayCalculator
import com.shalom.calendar.domain.calculator.OrthodoxHolidayCalculator
import com.shalom.calendar.domain.calculator.PublicHolidayCalculator
import com.shalom.calendar.ui.converter.DateConverterViewModel
import com.shalom.calendar.ui.event.EventViewModel
import com.shalom.calendar.ui.holidaylist.CalendarItemListViewModel
import com.shalom.calendar.ui.month.MonthCalendarViewModel
import com.shalom.calendar.ui.more.SettingsViewModel
import com.shalom.calendar.ui.more.ThemeViewModel
import com.shalom.calendar.ui.onboarding.OnboardingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val preferencesModule = module {
    single { ThemePreferences(androidContext()) }
    single { SettingsPreferences(androidContext()) }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            CalendarDatabase::class.java,
            CalendarDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }
    single { get<CalendarDatabase>().eventDao() }
}

val repositoryModule = module {
    single { EventRepository(get()) }
    single { HolidayRepository(get(), get(), get(), get()) }
}

val calculatorModule = module {
    singleOf(::ResourceProvider)
    singleOf(::OrthodoxHolidayCalculator)
    singleOf(::PublicHolidayCalculator)
    singleOf(::MuslimHolidayCalculator)
}

val analyticsModule = module {
    single { Firebase.analytics }
    single { AnalyticsManager(get(), get()) }
    single { UserPropertiesManager(get(), get(), get(), get()) }
}

val managerModule = module {
    single { RemoteConfigManager(get()) }
    single { PermissionManager(androidContext(), get()) }
    single { AlarmScheduler(androidContext()) }
    single { LegacyDataMigrator(androidContext(), get()) }
    single { ReminderReregistrationManager(androidContext(), get()) }
    single {
        AppInitializationManager(
            androidContext(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}

val viewModelModule = module {
    viewModel { ThemeViewModel(get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { OnboardingViewModel(get(), get(), get()) }
    viewModel { CalendarItemListViewModel(get(), get()) }
    viewModel { EventViewModel(get(), get(), get(), get()) }
    viewModel { MonthCalendarViewModel(get(), get(), get(), get()) }
    viewModel { DateConverterViewModel() }
}

val appModules = listOf(
    preferencesModule,
    databaseModule,
    repositoryModule,
    calculatorModule,
    analyticsModule,
    managerModule,
    viewModelModule
)
