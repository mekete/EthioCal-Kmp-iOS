package com.shalom.ethiopicchrono

import kotlinx.datetime.LocalDate

/**
 * Extension functions for LocalDate to support Ethiopian calendar conversions
 */

/**
 * Creates a LocalDate from an EthiopicDate
 * This provides compatibility with the Android API pattern: LocalDate.from(ethiopicDate)
 */
fun LocalDate.Companion.from(ethiopicDate: EthiopicDate): LocalDate {
    return ethiopicDate.toLocalDate()
}
