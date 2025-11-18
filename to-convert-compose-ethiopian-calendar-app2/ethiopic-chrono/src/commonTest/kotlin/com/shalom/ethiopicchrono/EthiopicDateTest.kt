package com.shalom.ethiopicchrono

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Basic unit tests for EthiopicDate
 * Tests core functionality across all platforms
 */
class EthiopicDateTest {

    @Test
    fun testCreateEthiopicDate() {
        val date = EthiopicDate.of(2016, 1, 1)
        assertEquals(2016, date.get(ChronoField.YEAR))
        assertEquals(1, date.get(ChronoField.MONTH_OF_YEAR))
        assertEquals(1, date.get(ChronoField.DAY_OF_MONTH))
    }

    @Test
    fun testLeapYear() {
        // 2015 is a leap year (2015 % 4 == 3)
        val leapDate = EthiopicDate.of(2015, 13, 6)
        assertTrue(leapDate.isLeapYear)
        assertEquals(6, leapDate.lengthOfMonth())
        assertEquals(366, leapDate.lengthOfYear())

        // 2016 is not a leap year
        val normalDate = EthiopicDate.of(2016, 13, 5)
        assertTrue(!normalDate.isLeapYear)
        assertEquals(5, normalDate.lengthOfMonth())
        assertEquals(365, normalDate.lengthOfYear())
    }

    @Test
    fun testConversionFromGregorian() {
        // 2024-01-01 (Gregorian) should be around 2016-04-22 (Ethiopian)
        val gregorian = LocalDate(2024, 1, 1)
        val ethiopian = EthiopicDate.from(gregorian)

        // The conversion should produce a valid Ethiopian date
        assertTrue(ethiopian.get(ChronoField.YEAR) > 2000)
        assertTrue(ethiopian.get(ChronoField.MONTH_OF_YEAR) in 1..13)
        assertTrue(ethiopian.get(ChronoField.DAY_OF_MONTH) in 1..30)
    }

    @Test
    fun testConversionToGregorian() {
        // Create an Ethiopian date and convert to Gregorian
        val ethiopian = EthiopicDate.of(2016, 1, 1)
        val gregorian = ethiopian.toLocalDate()

        // Verify round-trip conversion
        val backToEthiopian = EthiopicDate.from(gregorian)
        assertEquals(ethiopian.get(ChronoField.YEAR), backToEthiopian.get(ChronoField.YEAR))
        assertEquals(ethiopian.get(ChronoField.MONTH_OF_YEAR), backToEthiopian.get(ChronoField.MONTH_OF_YEAR))
        assertEquals(ethiopian.get(ChronoField.DAY_OF_MONTH), backToEthiopian.get(ChronoField.DAY_OF_MONTH))
    }

    @Test
    fun testPlusDays() {
        val date = EthiopicDate.of(2016, 1, 1)
        val plus10Days = date.plus(10, ChronoUnit.DAYS)
        assertEquals(11, plus10Days.get(ChronoField.DAY_OF_MONTH))
        assertEquals(1, plus10Days.get(ChronoField.MONTH_OF_YEAR))

        // Test crossing month boundary
        val date2 = EthiopicDate.of(2016, 1, 25)
        val plus10Days2 = date2.plus(10, ChronoUnit.DAYS)
        assertEquals(5, plus10Days2.get(ChronoField.DAY_OF_MONTH))
        assertEquals(2, plus10Days2.get(ChronoField.MONTH_OF_YEAR))
    }

    @Test
    fun testPlusMonths() {
        val date = EthiopicDate.of(2016, 1, 15)
        val plus3Months = date.plus(3, ChronoUnit.MONTHS)
        assertEquals(4, plus3Months.get(ChronoField.MONTH_OF_YEAR))
        assertEquals(15, plus3Months.get(ChronoField.DAY_OF_MONTH))
    }

    @Test
    fun testPlusYears() {
        val date = EthiopicDate.of(2016, 5, 20)
        val plus5Years = date.plus(5, ChronoUnit.YEARS)
        assertEquals(2021, plus5Years.get(ChronoField.YEAR))
        assertEquals(5, plus5Years.get(ChronoField.MONTH_OF_YEAR))
        assertEquals(20, plus5Years.get(ChronoField.DAY_OF_MONTH))
    }

    @Test
    fun testEquality() {
        val date1 = EthiopicDate.of(2016, 3, 15)
        val date2 = EthiopicDate.of(2016, 3, 15)
        val date3 = EthiopicDate.of(2016, 3, 16)

        assertEquals(date1, date2)
        assertTrue(date1 != date3)
    }
}
