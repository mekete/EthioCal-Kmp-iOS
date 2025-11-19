package com.shalom.ethiopicchrono

import kotlinx.datetime.LocalDate

/**
 * Common EthiopicDate for cross-platform Ethiopian calendar date operations
 */
expect class EthiopicDate {
    val chronology: EthiopicChronology
    val era: EthiopicEra
    val isLeapYear: Boolean

    fun lengthOfMonth(): Int
    fun lengthOfYear(): Int
    fun get(field: ChronoField): Int
    fun toEpochDay(): Long
    fun toLocalDate(): LocalDate
    fun plus(amountToAdd: Long, unit: ChronoUnit): EthiopicDate
    fun plus(amountToAdd: Int, unit: ChronoUnit): EthiopicDate
    fun minus(amountToSubtract: Long, unit: ChronoUnit): EthiopicDate
    fun minus(amountToSubtract: Int, unit: ChronoUnit): EthiopicDate

    companion object {
        fun now(): EthiopicDate
        fun of(prolepticYear: Int, month: Int, dayOfMonth: Int): EthiopicDate
        fun from(gregorianDate: LocalDate): EthiopicDate
        fun ofYearDay(prolepticYear: Int, dayOfYear: Int): EthiopicDate
        fun ofEpochDay(epochDay: Long): EthiopicDate
    }
}
