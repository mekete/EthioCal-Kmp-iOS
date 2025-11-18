package com.shalom.calendar.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Android-specific database builder.
 * Requires Android Context to create the database.
 */
actual object DatabaseBuilder {
    private lateinit var context: Context

    /**
     * Initialize with Android context.
     * Call this from Application.onCreate()
     */
    fun init(context: Context) {
        this.context = context.applicationContext
    }

    actual fun build(): CalendarDatabase {
        return getDatabaseBuilder()
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
}

internal actual fun getDatabaseBuilder(): RoomDatabase.Builder<CalendarDatabase> {
    val dbFile = DatabaseBuilder.context.getDatabasePath(CalendarDatabase.DATABASE_NAME)
    return Room.databaseBuilder<CalendarDatabase>(
        context = DatabaseBuilder.context,
        name = dbFile.absolutePath
    )
}
