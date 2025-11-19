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
        val MIN_YEAR: Int
        val MAX_YEAR: Int
        val MIN_MONTH: Int
        val MAX_MONTH: Int
        val MIN_DAY: Int
        val MAX_DAY: Int
    }
}
