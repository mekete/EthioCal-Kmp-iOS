package com.shalom.calendar.ui.theme

/**
 * Available color themes for the application
 */
enum class AppTheme(val displayName: String) {
    BLUE("Blue"),
    RED("Red"),
    GREEN("Green"),
    PURPLE("Purple"),
    ORANGE("Orange");

    companion object {
        fun fromOrdinal(ordinal: Int): AppTheme {
            return entries.getOrElse(ordinal) { BLUE }
        }
    }
}

/**
 * Theme mode for dark/light theme selection
 */
enum class ThemeMode(val displayName: String) {
    SYSTEM("System Default"),
    LIGHT("Light"),
    DARK("Dark");

    companion object {
        fun fromOrdinal(ordinal: Int): ThemeMode {
            return entries.getOrElse(ordinal) { SYSTEM }
        }
    }
}
