package com.shalom.ethiopiancalendar

import androidx.compose.ui.window.ComposeUIViewController
import com.shalom.calendar.di.initKoin
import org.koin.core.context.GlobalContext

fun MainViewController() = ComposeUIViewController {
    // Initialize Koin if not already initialized
    if (GlobalContext.getOrNull() == null) {
        initKoin()
    }
    App()
}