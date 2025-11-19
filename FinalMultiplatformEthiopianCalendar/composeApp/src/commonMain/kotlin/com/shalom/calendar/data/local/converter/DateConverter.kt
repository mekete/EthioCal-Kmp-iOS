package com.shalom.calendar.data.local.converter

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

/**
 * Room TypeConverter for kotlinx.datetime.Instant
 *
 * Stores Instant as epoch milliseconds (Long).
 * Cross-platform compatible with Android and iOS.
 */
class DateConverter {

    @TypeConverter
    fun fromInstant(value: Instant?): Long? {
        return value?.toEpochMilliseconds()
    }

    @TypeConverter
    fun toInstant(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }
}
