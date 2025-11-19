package com.shalom.ethiopicchrono

/**
 * Common EthiopicChronology for cross-platform calendar operations
 */
expect class EthiopicChronology {
    val id: String
    val calendarType: String

    fun isLeapYear(prolepticYear: Long): Boolean
    fun isLeapYear(prolepticYear: Int): Boolean

    companion object {
        val INSTANCE: EthiopicChronology
        const val MIN_YEAR: Int
        const val MAX_YEAR: Int
        const val MIN_MONTH: Int
        const val MAX_MONTH: Int
        const val MIN_DAY: Int
        const val MAX_DAY: Int
    }
}
