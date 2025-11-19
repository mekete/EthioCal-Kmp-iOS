package com.shalom.ethiopicchrono

/**
 * iOS-compatible ChronoUnit enum
 * Simplified version of java.time.temporal.ChronoUnit for cross-platform compatibility
 */
actual enum class ChronoUnit {
    DAYS,
    WEEKS,
    MONTHS,
    YEARS,
    DECADES,
    CENTURIES,
    MILLENNIA,
    ERAS
}
