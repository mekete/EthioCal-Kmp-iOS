package com.shalom.ethiopicchrono

/**
 * iOS-compatible EthiopicEra enum
 * Represents the eras in the Ethiopian calendar
 */
enum class EthiopicEra {
    /**
     * The era before the Incarnation (before year 1)
     */
    BEFORE_INCARNATION,

    /**
     * The era of the Incarnation (year 1 onwards)
     */
    INCARNATION;

    companion object {
        fun of(eraValue: Int): EthiopicEra {
            return when (eraValue) {
                0 -> BEFORE_INCARNATION
                1 -> INCARNATION
                else -> throw IllegalArgumentException("Invalid era value: $eraValue")
            }
        }
    }
}
