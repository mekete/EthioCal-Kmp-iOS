package com.shalom.calendar.domain.calculator

/**
 * Cross-platform resource provider interface
 * Platform implementations will provide actual string resources
 */
interface ResourceProvider {
    fun getString(key: String): String
}

/**
 * Simple in-memory resource provider for KMP
 * Can be replaced with platform-specific implementations
 */
class SimpleResourceProvider : ResourceProvider {
    private val strings = mapOf(
        // Orthodox Holidays
        "holiday_orthodox_nineveh" to "Nineveh Fast",
        "holiday_orthodox_abiy_tsom" to "Great Lent",
        "holiday_orthodox_debre_zeit" to "Debre Zeit",
        "holiday_orthodox_hosanna" to "Palm Sunday",
        "holiday_orthodox_siklet" to "Good Friday",
        "holiday_orthodox_fasika" to "Easter",
        "holiday_orthodox_rikbe_kahinat" to "Rikbe Kahinat",
        "holiday_orthodox_erget" to "Ascension",
        "holiday_orthodox_peraklitos" to "Pentecost",
        "holiday_orthodox_tsome_hawariat" to "Apostles' Fast",
        "holiday_orthodox_tsome_dihnet" to "Assumption Fast",
        "holiday_orthodox_ghad" to "Epiphany Eve",
        "holiday_orthodox_kana_zegelila" to "Wedding at Cana",
        "holiday_orthodox_lideta_mariam" to "Birth of Mary",
        "holiday_orthodox_filseta" to "Assumption of Mary",
        "holiday_orthodox_debre_tabor" to "Transfiguration",

        // Public Holidays
        "holiday_public_enkutatash" to "Ethiopian New Year",
        "holiday_public_christian_meskel" to "Meskel",
        "holiday_public_christian_genna" to "Ethiopian Christmas",
        "holiday_public_christian_timket" to "Timket (Epiphany)",
        "holiday_public_adwa" to "Adwa Victory Day",
        "holiday_public_labour_day" to "Labor Day",
        "holiday_public_patriots_day" to "Patriots' Day",

        // Default descriptions and celebrations
        "holiday_default_description" to "Traditional Ethiopian holiday",
        "holiday_default_celebration" to "Celebrated throughout Ethiopia"
    )

    override fun getString(key: String): String {
        return strings[key] ?: key
    }
}
