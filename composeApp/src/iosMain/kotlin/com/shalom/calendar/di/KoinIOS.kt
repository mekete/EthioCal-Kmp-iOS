package com.shalom.calendar.di

import org.koin.core.context.startKoin

/**
 * Initialize Koin for iOS.
 * Call this from Swift AppDelegate or App initialization.
 */
fun initKoin() {
    startKoin {
        modules(appModules + iosPlatformModule)
    }
}
