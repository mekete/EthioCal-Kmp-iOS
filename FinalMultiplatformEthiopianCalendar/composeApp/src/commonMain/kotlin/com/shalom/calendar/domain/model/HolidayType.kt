package com.shalom.calendar.domain.model

import androidx.compose.ui.graphics.Color

enum class HolidayType {
    NATIONAL_DAY_OFF, ORTHODOX_DAY_OFF, ORTHODOX_WORKING, MUSLIM_DAY_OFF, MUSLIM_WORKING, WESTERN, CULTURAL;

    fun getColor(): Color {
        return when (this) {
            NATIONAL_DAY_OFF -> Color(0xFF1976D2)      // Blue
            ORTHODOX_DAY_OFF -> Color(0xFF03A9F4)      // Light Blue
            ORTHODOX_WORKING -> Color(0xFFCDDC39)     // Lime
            MUSLIM_DAY_OFF -> Color(0xFF009688)       // Teal
            MUSLIM_WORKING -> Color(0xFF63EA67)       // Light Green
            WESTERN -> Color(0xFFF57C00)              // Orange
            CULTURAL -> Color(0xFF7B1FA2)             // Purple
        }
    }
}
