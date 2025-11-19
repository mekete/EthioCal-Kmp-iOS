package com.shalom.calendar.data.repository

import com.shalom.calendar.domain.model.Holiday
import com.shalom.calendar.domain.model.HolidayType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * Repository for holiday operations.
 * KMP-compatible version.
 *
 * Provides access to:
 * - National holidays
 * - Orthodox holidays
 * - Muslim holidays
 * - Custom holidays
 */
class HolidayRepository {

    private val _customHolidays = MutableStateFlow<List<Holiday>>(emptyList())

    /**
     * Get all holidays for a specific Ethiopian year.
     * Combines national, Orthodox, and Muslim holidays.
     */
    fun getHolidaysForYear(
        ethiopianYear: Int,
        includeNational: Boolean = true,
        includeOrthodox: Boolean = true,
        includeMuslim: Boolean = true
    ): Flow<List<Holiday>> {
        return _customHolidays.map { customHolidays ->
            val allHolidays = mutableListOf<Holiday>()

            // Add custom holidays
            allHolidays.addAll(customHolidays.filter { it.ethiopianYear == ethiopianYear })

            // TODO: Add calculators for national, orthodox, and muslim holidays
            // For now, return just custom holidays
            allHolidays.sortedWith(compareBy({ it.ethiopianMonth }, { it.ethiopianDay }))
        }
    }

    /**
     * Get holidays for a specific month.
     */
    fun getHolidaysForMonth(year: Int, month: Int): Flow<List<Holiday>> {
        return _customHolidays.map { holidays ->
            holidays.filter { it.ethiopianYear == year && it.ethiopianMonth == month }
                .sortedBy { it.ethiopianDay }
        }
    }

    /**
     * Get holidays for a specific date.
     */
    fun getHolidaysForDate(year: Int, month: Int, day: Int): Flow<List<Holiday>> {
        return _customHolidays.map { holidays ->
            holidays.filter {
                it.ethiopianYear == year &&
                it.ethiopianMonth == month &&
                it.ethiopianDay == day
            }
        }
    }

    /**
     * Add a custom holiday.
     */
    suspend fun addCustomHoliday(holiday: Holiday) {
        _customHolidays.value = _customHolidays.value + holiday
    }

    /**
     * Remove a custom holiday.
     */
    suspend fun removeCustomHoliday(holidayId: String) {
        _customHolidays.value = _customHolidays.value.filter { it.id != holidayId }
    }

    /**
     * Get all custom holidays.
     */
    fun getCustomHolidays(): Flow<List<Holiday>> {
        return _customHolidays
    }
}
