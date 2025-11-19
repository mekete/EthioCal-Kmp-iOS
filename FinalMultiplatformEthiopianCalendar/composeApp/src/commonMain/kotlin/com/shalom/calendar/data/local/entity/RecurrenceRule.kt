package com.shalom.calendar.data.local.entity

/**
 * Represents a recurrence rule for events.
 * Based on iCalendar RRULE specification.
 */
data class RecurrenceRule(
    val frequency: Frequency,
    val interval: Int = 1,
    val count: Int? = null,
    val endDate: Long? = null, // Epoch milliseconds
    val byDay: List<DayOfWeek>? = null,
    val byMonth: List<Int>? = null,
    val byMonthDay: List<Int>? = null
) {
    enum class Frequency {
        DAILY, WEEKLY, MONTHLY, YEARLY
    }

    enum class DayOfWeek(val rruleValue: String) {
        MONDAY("MO"),
        TUESDAY("TU"),
        WEDNESDAY("WE"),
        THURSDAY("TH"),
        FRIDAY("FR"),
        SATURDAY("SA"),
        SUNDAY("SU")
    }
}

/**
 * Convert RecurrenceRule to RRULE string format.
 */
fun RecurrenceRule.toRRuleString(): String {
    val parts = mutableListOf<String>()

    parts.add("FREQ=${frequency.name}")

    if (interval > 1) {
        parts.add("INTERVAL=$interval")
    }

    count?.let {
        parts.add("COUNT=$it")
    }

    endDate?.let {
        // Convert to ISO date format for RRULE
        val instant = kotlinx.datetime.Instant.fromEpochMilliseconds(it)
        val localDateTime = instant.toLocalDateTime(kotlinx.datetime.TimeZone.UTC)
        val dateStr = "${localDateTime.year}${localDateTime.monthNumber.toString().padStart(2, '0')}${localDateTime.dayOfMonth.toString().padStart(2, '0')}T${localDateTime.hour.toString().padStart(2, '0')}${localDateTime.minute.toString().padStart(2, '0')}${localDateTime.second.toString().padStart(2, '0')}Z"
        parts.add("UNTIL=$dateStr")
    }

    byDay?.let { days ->
        if (days.isNotEmpty()) {
            parts.add("BYDAY=${days.joinToString(",") { it.rruleValue }}")
        }
    }

    byMonth?.let { months ->
        if (months.isNotEmpty()) {
            parts.add("BYMONTH=${months.joinToString(",")}")
        }
    }

    byMonthDay?.let { days ->
        if (days.isNotEmpty()) {
            parts.add("BYMONTHDAY=${days.joinToString(",")}")
        }
    }

    return "RRULE:${parts.joinToString(";")}"
}

/**
 * Parse an RRULE string to a RecurrenceRule object.
 */
fun parseRRuleString(rrule: String): RecurrenceRule? {
    if (!rrule.startsWith("RRULE:")) return null

    val content = rrule.removePrefix("RRULE:")
    val parts = content.split(";").associate {
        val (key, value) = it.split("=", limit = 2)
        key to value
    }

    val frequency = try {
        RecurrenceRule.Frequency.valueOf(parts["FREQ"] ?: return null)
    } catch (e: Exception) {
        return null
    }

    val interval = parts["INTERVAL"]?.toIntOrNull() ?: 1
    val count = parts["COUNT"]?.toIntOrNull()

    val endDate = parts["UNTIL"]?.let { until ->
        // Parse UNTIL date (simplified parsing)
        try {
            // Format: YYYYMMDDTHHmmssZ
            val year = until.substring(0, 4).toInt()
            val month = until.substring(4, 6).toInt()
            val day = until.substring(6, 8).toInt()
            val localDate = kotlinx.datetime.LocalDate(year, month, day)
            localDate.atStartOfDayIn(kotlinx.datetime.TimeZone.UTC).toEpochMilliseconds()
        } catch (e: Exception) {
            null
        }
    }

    val byDay = parts["BYDAY"]?.split(",")?.mapNotNull { dayStr ->
        RecurrenceRule.DayOfWeek.entries.find { it.rruleValue == dayStr }
    }

    val byMonth = parts["BYMONTH"]?.split(",")?.mapNotNull { it.toIntOrNull() }
    val byMonthDay = parts["BYMONTHDAY"]?.split(",")?.mapNotNull { it.toIntOrNull() }

    return RecurrenceRule(
        frequency = frequency,
        interval = interval,
        count = count,
        endDate = endDate,
        byDay = byDay,
        byMonth = byMonth,
        byMonthDay = byMonthDay
    )
}
