package com.shalom.calendar.data.migration

import android.database.Cursor

/**
 * Represents the old Alarm table schema from the Java-based calendar app.
 *
 * Old database structure:
 * - Table name: "Alarm"
 * - Database name: "EthiopianCalendar.db"
 * - Version: <= 16
 *
 * CREATE TABLE Alarm (
 *   _id INTEGER PRIMARY KEY,
 *   guid TEXT NOT NULL,
 *   alarmCategory TEXT,           -- Color code: "0"-"4"
 *   alarmNote TEXT,                -- Event description
 *   notificationEnabled TEXT,      -- "true" or "false"
 *   alarmTimeStamp TEXT,           -- When alarm triggers (milliseconds)
 *   timeStamp TEXT,                -- Creation time (milliseconds)
 *   UNIQUE(guid)
 * );
 */
data class OldAlarm(
    val rowId: Int, val guid: String, val alarmColor: String?, val alarmNote: String, val notificationEnabled: Boolean, val alarmTimeStamp: Long, val timeStamp: Long
) {
    companion object {

        fun fromCursor(cursor: Cursor): OldAlarm {
            return OldAlarm(rowId = cursor.getInt(cursor.getColumnIndexOrThrow("_id")),
                guid = cursor.getString(cursor.getColumnIndexOrThrow("guid")),
                alarmColor = cursor.getString(cursor.getColumnIndexOrThrow("alarmCategory")),
                alarmNote = cursor.getString(cursor.getColumnIndexOrThrow("alarmNote")) ?: "",
                notificationEnabled = "true".equals(cursor.getString(cursor.getColumnIndexOrThrow("notificationEnabled")), ignoreCase = true),
                alarmTimeStamp = cursor.getLong(cursor.getColumnIndexOrThrow("alarmTimeStamp")),
                timeStamp = cursor.getLong(cursor.getColumnIndexOrThrow("timeStamp")))
        }
    }
}

object OldDatabaseConstants {
    const val DATABASE_NAME = "EthiopianCalendar.db"
    const val TABLE_NAME = "Alarm"
    const val COLUMN_ID = "_id"
    const val COLUMN_GUID = "guid"
    const val COLUMN_ALARM_CATEGORY = "alarmCategory"
    const val COLUMN_ALARM_NOTE = "alarmNote"
    const val COLUMN_NOTIFICATION_ENABLED = "notificationEnabled"
    const val COLUMN_ALARM_TIME_STAMP = "alarmTimeStamp"
    const val COLUMN_TIME_STAMP = "timeStamp"

    /**
     * SQL query to select alarms from a specific time onwards
     */
    fun getSelectQuery(startTimeMillis: Long): String {
        return """
            SELECT
                $COLUMN_ID,
                $COLUMN_GUID,
                $COLUMN_ALARM_CATEGORY,
                $COLUMN_ALARM_NOTE,
                $COLUMN_NOTIFICATION_ENABLED,
                $COLUMN_ALARM_TIME_STAMP,
                $COLUMN_TIME_STAMP
            FROM $TABLE_NAME
            WHERE $COLUMN_ALARM_TIME_STAMP >= $startTimeMillis
            ORDER BY $COLUMN_ALARM_TIME_STAMP ASC
        """.trimIndent()
    }


}

/**
 * Maps old alarm color codes to new event categories
 */
object AlarmColorMapper {

    fun mapToCategory(alarmColor: String?): String {
        return when (alarmColor) {
            "0" -> "PERSONAL"     // Default blue
            "1" -> "CUSTOM"       // Red
            "2" -> "WORK"         // Yellow
            "3" -> "RELIGIOUS"    // Green
            "4" -> "CUSTOM"       // Grey
            else -> "PERSONAL"    // Fallback to personal
        }
    }


    fun mapToReminderMinutes(notificationEnabled: Boolean): Int? {
        return if (notificationEnabled) 0 else null
    }
}
