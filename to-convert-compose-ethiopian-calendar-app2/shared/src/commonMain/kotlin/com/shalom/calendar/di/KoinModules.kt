package com.shalom.calendar.di

import com.shalom.calendar.data.local.DatabaseBuilder
import com.shalom.calendar.data.repository.EventRepository
import com.shalom.calendar.data.repository.HolidayRepository
import com.shalom.calendar.domain.calculator.OrthodoxHolidayCalculator
import com.shalom.calendar.domain.calculator.PublicHolidayCalculator
import com.shalom.calendar.domain.calculator.ResourceProvider
import com.shalom.calendar.domain.calculator.SimpleResourceProvider
import com.shalom.calendar.presentation.converter.DateConverterViewModel
import com.shalom.calendar.presentation.holidaylist.CalendarItemListViewModel
import com.shalom.calendar.presentation.onboarding.OnboardingViewModel
import com.shalom.calendar.presentation.settings.SettingsViewModel
import com.shalom.calendar.presentation.theme.ThemeViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Database module - provides Room database and DAOs
 */
val databaseModule = module {
    single { DatabaseBuilder.build() }
    single { get<com.shalom.calendar.data.local.CalendarDatabase>().eventDao() }
}

/**
 * Repository module - provides data access
 */
val repositoryModule = module {
    singleOf(::EventRepository)
    singleOf(::HolidayRepository)
}

/**
 * Domain module - provides business logic (calculators)
 */
val domainModule = module {
    single<ResourceProvider> { SimpleResourceProvider() }
    singleOf(::OrthodoxHolidayCalculator)
    singleOf(::PublicHolidayCalculator)
    // TODO: Add MuslimHolidayCalculator when Hijrah calendar support is added
}

/**
 * ViewModel module - provides presentation layer ViewModels
 * Using factory scope so new instances are created for each injection
 */
val viewModelModule = module {
    factoryOf(::DateConverterViewModel)
    factoryOf(::CalendarItemListViewModel)
    factoryOf(::OnboardingViewModel)
    factoryOf(::SettingsViewModel)
    factoryOf(::ThemeViewModel)
}

/**
 * All app modules combined
 */
val appModules = listOf(
    databaseModule,
    repositoryModule,
    domainModule,
    viewModelModule
)
