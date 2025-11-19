package com.shalom.ethiopicchrono

/**
 * Android-compatible ChronoUnit enum
 * Maps to java.time.temporal.ChronoUnit
 */
actual enum class ChronoUnit {
    DAYS,
    WEEKS,
    MONTHS,
    YEARS,
    DECADES,
    CENTURIES,
    MILLENNIA,
    ERAS;

    /**
     * Convert to java.time.temporal.ChronoUnit
     */
    fun toJavaChronoUnit(): java.time.temporal.ChronoUnit = when (this) {
        DAYS -> java.time.temporal.ChronoUnit.DAYS
        WEEKS -> java.time.temporal.ChronoUnit.WEEKS
        MONTHS -> java.time.temporal.ChronoUnit.MONTHS
        YEARS -> java.time.temporal.ChronoUnit.YEARS
        DECADES -> java.time.temporal.ChronoUnit.DECADES
        CENTURIES -> java.time.temporal.ChronoUnit.CENTURIES
        MILLENNIA -> java.time.temporal.ChronoUnit.MILLENNIA
        ERAS -> java.time.temporal.ChronoUnit.ERAS
    }
}
