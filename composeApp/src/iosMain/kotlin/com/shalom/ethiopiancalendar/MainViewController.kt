package com.shalom.ethiopiancalendar

import androidx.compose.ui.window.ComposeUIViewController
import com.shalom.calendar.di.initKoin
import org.koin.compose.KoinContext
import org.koin.mp.KoinPlatformTools

fun MainViewController() = ComposeUIViewController(
    configure = {
        // Initialize Koin before composables are created
        if (KoinPlatformTools.defaultContext().getOrNull() == null) {
            initKoin()
        }
    }
) {
    // Provide Koin context to composables
    KoinContext {
        App()
    }
}