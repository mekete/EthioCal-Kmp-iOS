package com.shalom.ethiopicchrono

/**
 * Android-compatible ChronoField enum
 * Maps to java.time.temporal.ChronoField
 */
actual enum class ChronoField {
    DAY_OF_WEEK,
    DAY_OF_MONTH,
    DAY_OF_YEAR,
    MONTH_OF_YEAR,
    YEAR,
    YEAR_OF_ERA,
    ERA,
    EPOCH_DAY,
    ALIGNED_DAY_OF_WEEK_IN_MONTH,
    ALIGNED_DAY_OF_WEEK_IN_YEAR,
    ALIGNED_WEEK_OF_MONTH,
    ALIGNED_WEEK_OF_YEAR,
    PROLEPTIC_MONTH;

    /**
     * Convert to java.time.temporal.ChronoField
     */
    fun toJavaChronoField(): java.time.temporal.ChronoField = when (this) {
        DAY_OF_WEEK -> java.time.temporal.ChronoField.DAY_OF_WEEK
        DAY_OF_MONTH -> java.time.temporal.ChronoField.DAY_OF_MONTH
        DAY_OF_YEAR -> java.time.temporal.ChronoField.DAY_OF_YEAR
        MONTH_OF_YEAR -> java.time.temporal.ChronoField.MONTH_OF_YEAR
        YEAR -> java.time.temporal.ChronoField.YEAR
        YEAR_OF_ERA -> java.time.temporal.ChronoField.YEAR_OF_ERA
        ERA -> java.time.temporal.ChronoField.ERA
        EPOCH_DAY -> java.time.temporal.ChronoField.EPOCH_DAY
        ALIGNED_DAY_OF_WEEK_IN_MONTH -> java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH
        ALIGNED_DAY_OF_WEEK_IN_YEAR -> java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR
        ALIGNED_WEEK_OF_MONTH -> java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH
        ALIGNED_WEEK_OF_YEAR -> java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR
        PROLEPTIC_MONTH -> java.time.temporal.ChronoField.PROLEPTIC_MONTH
    }
}
