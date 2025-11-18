package com.shalom.ethiopicchrono

import java.time.DateTimeException
import java.time.chrono.Era

enum class EthiopicEra : Era {
    BEFORE_INCARNATION,
    INCARNATION;

    override fun getValue(): Int = ordinal

    companion object {
        @JvmStatic
        fun of(era: Int): EthiopicEra = when (era) {
            0 -> BEFORE_INCARNATION
            1 -> INCARNATION
            else -> throw DateTimeException("Invalid era: $era")
        }
    }
}
