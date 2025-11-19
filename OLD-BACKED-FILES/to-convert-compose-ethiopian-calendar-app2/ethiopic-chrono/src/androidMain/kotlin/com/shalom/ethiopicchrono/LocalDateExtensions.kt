package com.shalom.ethiopicchrono

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate

/**
 * Extension functions for LocalDate to support Ethiopian calendar conversions on Android
 * Bridges kotlinx.datetime.LocalDate with java.time.LocalDate used by Android EthiopicDate
 */

/**
 * Creates a kotlinx.datetime.LocalDate from an EthiopicDate
 * This provides compatibility with the API pattern: LocalDate.from(ethiopicDate)
 */
fun LocalDate.Companion.from(ethiopicDate: EthiopicDate): LocalDate {
    val javaLocalDate = java.time.LocalDate.from(ethiopicDate)
    return javaLocalDate.toKotlinLocalDate()
}

/**
 * Extension function to create EthiopicDate from kotlinx.datetime.LocalDate
 */
fun EthiopicDate.Companion.from(date: LocalDate): EthiopicDate {
    return EthiopicDate.from(date.toJavaLocalDate())
}
