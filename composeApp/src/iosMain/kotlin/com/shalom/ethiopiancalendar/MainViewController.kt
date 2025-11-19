package com.shalom.ethiopiancalendar

import androidx.compose.ui.window.ComposeUIViewController
import com.shalom.calendar.di.initKoin
import org.koin.mp.KoinPlatformTools

fun MainViewController() = ComposeUIViewController {
    // Initialize Koin if not already initialized
    if (KoinPlatformTools.defaultContext().getOrNull() == null) {
        initKoin()
    }
    App()
}