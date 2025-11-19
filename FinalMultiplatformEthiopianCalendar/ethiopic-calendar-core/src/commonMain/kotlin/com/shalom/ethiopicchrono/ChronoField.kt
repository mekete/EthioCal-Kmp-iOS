package com.shalom.ethiopicchrono

/**
 * Common ChronoField enum for cross-platform date field access
 */
expect enum class ChronoField {
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
    PROLEPTIC_MONTH
}
