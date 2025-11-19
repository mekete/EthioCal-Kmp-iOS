package com.shalom.calendar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shalom.calendar.data.local.converter.DateConverter
import com.shalom.calendar.data.local.dao.EventDao
import com.shalom.calendar.data.local.entity.EventEntity

/**
 * Room database for the Calendar application.
 * KMP-compatible version.
 *
 * This database stores:
 * - Events and reminders
 * - (Future: Holiday customizations, user preferences, sync state)
 */
@Database(
    entities = [EventEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateConverter::class)
abstract class CalendarDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        const val DATABASE_NAME = "calendar_database"
    }
}
