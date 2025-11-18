package com.shalom.calendar.ui.converter

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.shalom.ethiopicchrono.EthiopicDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import javax.inject.Inject

@HiltViewModel
class DateConverterViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(DateConverterUiState())
    val uiState: StateFlow<DateConverterUiState> = _uiState.asStateFlow()

    // Gregorian to Ethiopian conversion
    fun setGregorianDate(day: String, month: String, year: String) {
        _uiState.value = _uiState.value.copy(gregorianDay = day, gregorianMonth = month, gregorianYear = year, gregorianError = null)
    }

    fun convertToEthiopian() {
        try {
            val day = _uiState.value.gregorianDay.toIntOrNull()
            val month = _uiState.value.gregorianMonth.toIntOrNull()
            val year = _uiState.value.gregorianYear.toIntOrNull()

            if (day == null || month == null || year == null) {
                _uiState.value = _uiState.value.copy(gregorianError = "Please enter valid numbers")
                return
            }

            if (day !in 1..31 || month !in 1..12 || year < 1) {
                _uiState.value = _uiState.value.copy(gregorianError = "Please enter valid date values")
                return
            }

            val gregorianDate = LocalDate.of(year, month, day)
            val ethiopianDate = EthiopicDate.from(gregorianDate)

            val ethiopianYear = ethiopianDate.get(ChronoField.YEAR_OF_ERA)
            val ethiopianMonth = ethiopianDate.get(ChronoField.MONTH_OF_YEAR)
            val ethiopianDay = ethiopianDate.get(ChronoField.DAY_OF_MONTH)

            val monthName = getEthiopianMonthName(ethiopianMonth)
            val result = "$monthName $ethiopianDay, $ethiopianYear"

            _uiState.value = _uiState.value.copy(ethiopianResult = result, gregorianError = null)

        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(gregorianError = "Invalid date: ${e.message}")
        }
    }

    fun setGregorianDateFromPicker(date: LocalDate) {
        _uiState.value = _uiState.value.copy(gregorianDay = date.dayOfMonth.toString(), gregorianMonth = date.monthValue.toString(), gregorianYear = date.year.toString())
    }

    // Ethiopian to Gregorian conversion
    fun setEthiopianDate(day: String, month: String, year: String) {
        _uiState.value = _uiState.value.copy(ethiopianDay = day, ethiopianMonth = month, ethiopianYear = year, ethiopianError = null)
    }

    fun convertToGregorian() {
        try {
            val day = _uiState.value.ethiopianDay.toIntOrNull()
            val month = _uiState.value.ethiopianMonth.toIntOrNull()
            val year = _uiState.value.ethiopianYear.toIntOrNull()

            if (day == null || month == null || year == null) {
                _uiState.value = _uiState.value.copy(ethiopianError = "Please enter valid numbers")
                return
            }

            // Ethiopian calendar validation
            val maxDay = when (month) {
                in 1..12 -> 30
                13 -> if (year % 4 == 3) 6 else 5 // Pagume
                else -> 0
            }

            if (month !in 1..13 || day !in 1..maxDay || year < 1) {
                _uiState.value = _uiState.value.copy(ethiopianError = "Please enter valid Ethiopian date values")
                return
            }

            val ethiopianDate = EthiopicDate.of(year, month, day)
            val gregorianDate = LocalDate.from(ethiopianDate)

            val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
            val result = gregorianDate.format(formatter)

            _uiState.value = _uiState.value.copy(gregorianResult = result, ethiopianError = null)

        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(ethiopianError = "Invalid date: ${e.message}")
        }
    }

    fun setEthiopianDateFromPicker(date: EthiopicDate) {
        _uiState.value = _uiState.value.copy(ethiopianDay = date.get(ChronoField.DAY_OF_MONTH).toString(), ethiopianMonth = date.get(ChronoField.MONTH_OF_YEAR).toString(), ethiopianYear = date.get(ChronoField.YEAR_OF_ERA).toString())
    }

    fun clearResults() {
        _uiState.value = DateConverterUiState()
    }

    // Date Difference calculation
    fun setStartDate(date: EthiopicDate) {
        _uiState.value = _uiState.value.copy(startDate = date, differenceError = null)
    }

    fun setEndDate(date: EthiopicDate) {
        _uiState.value = _uiState.value.copy(endDate = date, differenceError = null)
    }

    fun calculateDateDifference() {
        try {
            val start = _uiState.value.startDate
            val end = _uiState.value.endDate

            if (start == null || end == null) {
                _uiState.value = _uiState.value.copy(differenceError = "Please select both start and end dates")
                return
            }

            // Convert to LocalDate for calculation
            val startLocalDate = LocalDate.from(start)
            val endLocalDate = LocalDate.from(end)

            // Ensure start date is before end date
            if (startLocalDate.isAfter(endLocalDate)) {
                _uiState.value = _uiState.value.copy(differenceError = "Start date must be before end date")
                return
            }

            // Calculate the difference
            java.time.temporal.ChronoUnit.DAYS.between(startLocalDate, endLocalDate)

            // Calculate years, months, and days using Ethiopian calendar
            val startYear = start.get(ChronoField.YEAR)
            val startMonth = start.get(ChronoField.MONTH_OF_YEAR)
            val startDay = start.get(ChronoField.DAY_OF_MONTH)

            val endYear = end.get(ChronoField.YEAR)
            val endMonth = end.get(ChronoField.MONTH_OF_YEAR)
            val endDay = end.get(ChronoField.DAY_OF_MONTH)

            var years = endYear - startYear
            var months = endMonth - startMonth
            var days = endDay - startDay

            // Adjust if days are negative
            if (days < 0) {
                months -= 1 // Ethiopian calendar has 30 days per month for first 12 months
                days += 30
            }

            // Adjust if months are negative
            if (months < 0) {
                years -= 1
                months += 13 // Ethiopian calendar has 13 months
            }

            // Format the result
            val result = formatDifference(years, months, days)

            _uiState.value = _uiState.value.copy(differenceResult = result, differenceError = null)

        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(differenceError = "Error calculating difference: ${e.message}")
        }
    }

    private fun formatDifference(years: Int, months: Int, days: Int): String {
        val parts = mutableListOf<String>()

        if (years > 0) {
            parts.add("$years ${if (years == 1) "year" else "years"}")
        }
        if (months > 0) {
            parts.add("$months ${if (months == 1) "month" else "months"}")
        }
        if (days > 0 || parts.isEmpty()) {
            parts.add("$days ${if (days == 1) "day" else "days"}")
        }

        return parts.joinToString(", ")
    }

    private fun getEthiopianMonthName(month: Int): String {
        return when (month) {
            1 -> "Meskerem"
            2 -> "Tikimt"
            3 -> "Hidar"
            4 -> "Tahsas"
            5 -> "Tir"
            6 -> "Yekatit"
            7 -> "Megabit"
            8 -> "Miazia"
            9 -> "Ginbot"
            10 -> "Sene"
            11 -> "Hamle"
            12 -> "Nehase"
            13 -> "Pagume"
            else -> "Unknown"
        }
    }
}

data class DateConverterUiState( // Gregorian inputs
    val gregorianDay: String = "", val gregorianMonth: String = "", val gregorianYear: String = "", val ethiopianResult: String = "", val gregorianError: String? = null,

    // Ethiopian inputs
    val ethiopianDay: String = "", val ethiopianMonth: String = "", val ethiopianYear: String = "", val gregorianResult: String = "", val ethiopianError: String? = null,

    // Date Difference inputs
    val startDate: EthiopicDate? = null, val endDate: EthiopicDate? = null, val differenceResult: String = "", val differenceError: String? = null
)