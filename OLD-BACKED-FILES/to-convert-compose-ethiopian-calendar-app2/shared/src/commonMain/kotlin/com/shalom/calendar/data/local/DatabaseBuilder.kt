package com.shalom.calendar.data.local

import androidx.room.RoomDatabase

/**
 * Expect/actual builder for Room database.
 * Platform-specific implementations create the database instance.
 */
expect object DatabaseBuilder {
    fun build(): CalendarDatabase
}

/**
 * Get Room database builder for the platform.
 * This is an internal function used by platform-specific implementations.
 */
internal expect fun getDatabaseBuilder(): RoomDatabase.Builder<CalendarDatabase>
