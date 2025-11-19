package com.shalom.calendar.presentation.converter

import com.shalom.ethiopicchrono.EthiopicDate

/**
 * UI state for the Date Converter screen.
 * KMP-compatible version.
 */
data class DateConverterUiState(
    // Gregorian inputs
    val gregorianDay: String = "",
    val gregorianMonth: String = "",
    val gregorianYear: String = "",
    val ethiopianResult: String = "",
    val gregorianError: String? = null,

    // Ethiopian inputs
    val ethiopianDay: String = "",
    val ethiopianMonth: String = "",
    val ethiopianYear: String = "",
    val gregorianResult: String = "",
    val ethiopianError: String? = null,

    // Date Difference inputs
    val startDate: EthiopicDate? = null,
    val endDate: EthiopicDate? = null,
    val differenceResult: String = "",
    val differenceError: String? = null
)
