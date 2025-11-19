package com.shalom.ethiopicchrono

/**
 * iOS Implementation Stub
 *
 * TODO: Implement iOS-compatible version of EthiopicDate
 *
 * The Android version (androidMain) uses java.time.* classes which are not available on iOS.
 *
 * Options for iOS implementation:
 * 1. Use kotlinx-datetime and reimplement the calendar logic
 * 2. Create typealiases to kotlinx-datetime types where possible
 * 3. Implement a simplified version with just the core API needed by the app
 *
 * Required API (based on app usage):
 * - EthiopicDate.of(year, month, day)
 * - EthiopicDate.from(temporal)
 * - .get(ChronoField.MONTH_OF_YEAR)
 * - .get(ChronoField.DAY_OF_MONTH)
 * - .get(ChronoField.YEAR)
 * - .plus(n, ChronoUnit.DAYS)
 * - LocalDate.from(ethiopicDate)
 *
 * For now, this module is Android-only. iOS implementation will be added in Phase 2.
 */

// Placeholder to allow compilation
internal class IosPlaceholder
