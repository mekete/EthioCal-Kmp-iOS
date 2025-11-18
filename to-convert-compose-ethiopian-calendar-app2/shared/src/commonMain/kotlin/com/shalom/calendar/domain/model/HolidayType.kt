package com.shalom.calendar.domain.model

/**
 * Types of holidays in the Ethiopian calendar
 * Color information moved to UI layer for KMP compatibility
 */
enum class HolidayType {
    NATIONAL_DAY_OFF,
    ORTHODOX_DAY_OFF,
    ORTHODOX_WORKING,
    MUSLIM_DAY_OFF,
    MUSLIM_WORKING,
    WESTERN,
    CULTURAL;

    /**
     * Get color value as hex for cross-platform compatibility
     * UI layer can convert this to platform-specific Color objects
     */
    fun getColorHex(): Long {
        return when (this) {
            NATIONAL_DAY_OFF -> 0xFF1976D2      // Blue
            ORTHODOX_DAY_OFF -> 0xFF03A9F4      // Light Blue
            ORTHODOX_WORKING -> 0xFFCDDC39      // Lime
            MUSLIM_DAY_OFF -> 0xFF009688        // Teal
            MUSLIM_WORKING -> 0xFF63EA67        // Light Green
            WESTERN -> 0xFFF57C00               // Orange
            CULTURAL -> 0xFF7B1FA2              // Purple
        }
    }
}
