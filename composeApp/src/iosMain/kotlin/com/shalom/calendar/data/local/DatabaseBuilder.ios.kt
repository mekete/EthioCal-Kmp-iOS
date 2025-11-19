package com.shalom.calendar.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

/**
 * iOS-specific database builder.
 * Uses NSHomeDirectory for database file location.
 */
actual object DatabaseBuilder {
    actual fun build(): CalendarDatabase {
        return getDatabaseBuilder()
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .build()
    }
}

internal actual fun getDatabaseBuilder(): RoomDatabase.Builder<CalendarDatabase> {
    val dbFile = NSHomeDirectory() + "/Documents/${CalendarDatabase.DATABASE_NAME}"
    return Room.databaseBuilder<CalendarDatabase>(
        name = dbFile
    )
}
