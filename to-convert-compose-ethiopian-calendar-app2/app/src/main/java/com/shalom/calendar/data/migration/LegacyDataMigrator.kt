package com.shalom.calendar.data.migration

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.shalom.calendar.data.local.entity.EventEntity
import com.shalom.calendar.data.preferences.MigrationStatus
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.data.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LegacyDataMigrator @Inject constructor(
    private val context: Context, private val eventRepository: EventRepository, private val settingsPreferences: SettingsPreferences
) {

    companion object {
        private const val TAG = "LegacyDataMigrator"

        /**
         * Import events from last 90 days onwards.
         * This provides recent historical context plus all future events.
         */
        private val IMPORT_WINDOW_DAYS = 90L

        /**
         * Old app version threshold. Only versions < 99 had the old database structure.
         */
        private const val OLD_APP_MAX_VERSION = 94
    }

    /**
     * Path to the old database file.
     */
    private val oldDbPath: String
        get() = context.getDatabasePath(OldDatabaseConstants.DATABASE_NAME).absolutePath

    /**
     * Check if old database file exists in the app's database directory.
     */
    fun oldDatabaseExists(): Boolean {
        val oldDbFile = File(oldDbPath)
        return oldDbFile.exists() && oldDbFile.length() > 0
    }

    /**
     * Determine if migration should be attempted based on:
     * - Current version is >= 96 (new app structure)
     * - Previous version was < 96 (had old app structure)
     * - Old database exists
     * - Migration hasn't been completed yet
     */
    suspend fun shouldAttemptMigration(): Boolean {
        val migrationStatus = settingsPreferences.migrationStatus.first()

        if (migrationStatus == MigrationStatus.COMPLETED || migrationStatus == MigrationStatus.NO_OLD_DATA) {
            return false
        }

        if (!oldDatabaseExists()) {
            settingsPreferences.setMigrationStatus(MigrationStatus.NO_OLD_DATA)
            return false
        }

        return true
    }

    /**
     * Perform the migration from old database to new database.
     *
     * @param onProgress Callback for progress updates (current, total)
     * @return MigrationResult containing success/failure counts and errors
     */
    suspend fun migrateData(
        onProgress: (current: Int, total: Int) -> Unit = { _, _ -> }
    ): MigrationResult = withContext(Dispatchers.IO) {

        val oldDb = openOldDatabase() ?: return@withContext MigrationResult(successCount = 0, failureCount = 0, errors = listOf("Cannot open old database"))

        settingsPreferences.setMigrationStatus(MigrationStatus.IN_PROGRESS)

        try {
            val startTimeMillis = getImportWindowStartTime()
            val cursor = oldDb.rawQuery(OldDatabaseConstants.getSelectQuery(startTimeMillis), null)

            val total = cursor.count
            var successCount = 0
            val errors = mutableListOf<String>()


            while (cursor.moveToNext()) {
                try {
                    val alarm = OldAlarm.fromCursor(cursor)
                    val event = convertAlarmToEvent(alarm)
                    eventRepository.createEvent(event)

                    successCount++
                    onProgress(successCount, total)

                } catch (e: Exception) {
                    errors.add("Row ${cursor.position}: ${e.message}")
                }
            }

            cursor.close()

            if (successCount > 0) {
                settingsPreferences.setMigrationStatus(MigrationStatus.COMPLETED, successCount)
            } else {
                settingsPreferences.setMigrationStatus(MigrationStatus.FAILED)
            }

            MigrationResult(successCount = successCount, failureCount = total - successCount, errors = errors)

        } catch (e: Exception) {
            settingsPreferences.setMigrationStatus(MigrationStatus.FAILED)
            MigrationResult(successCount = 0, failureCount = 0, errors = listOf("Migration failed: ${e.message}"))
        } finally {
            oldDb.close()
        }
    }

    /**
     * Delete old database after successful migration.
     * Also deletes associated SQLite files (WAL, SHM).
     *
     * @return true if deletion was successful
     */
    suspend fun deleteOldDatabase(): Boolean = withContext(Dispatchers.IO) {
        try {
            val oldDbFile = File(oldDbPath)
            val walFile = File("$oldDbPath-wal")
            val shmFile = File("$oldDbPath-shm")

            val dbDeleted = if (oldDbFile.exists()) oldDbFile.delete() else true
            val walDeleted = if (walFile.exists()) walFile.delete() else true
            val shmDeleted = if (shmFile.exists()) shmFile.delete() else true

            val success = dbDeleted && walDeleted && shmDeleted

            success
        } catch (e: Exception) {
            Timber.e(e, "Failed to delete old database")
            false
        }
    }

    /**
     * Opens the old SQLite database in read-only mode.
     */
    private fun openOldDatabase(): SQLiteDatabase? {
        return try {
            SQLiteDatabase.openDatabase(oldDbPath, null, SQLiteDatabase.OPEN_READONLY)
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Cannot open old database")
            null
        }
    }

    /**
     * Converts an old Alarm record to a new EventEntity.
     */
    private fun convertAlarmToEvent(alarm: OldAlarm): EventEntity {
        val ethiopianDateTime = EthiopianDateConverter.convertGregorianMillisToEthiopianDateTime(alarm.alarmTimeStamp)
        val startTime = EthiopianDateConverter.convertMillisToZonedDateTime(alarm.alarmTimeStamp)
        val endTime = startTime.plusHours(1)

        return EventEntity(id = "migrated_${alarm.guid}",  // Add prefix to avoid GUID conflicts
            summary = alarm.alarmNote.ifBlank { "Imported Event" },
            description = null,  // Old app didn't have separate description
            startTime = startTime,
            endTime = endTime,
            isAllDay = false,  // Old app used time pickers, so not all-day events
            timeZone = "Africa/Addis_Ababa",
            recurrenceRule = null,  // Old app didn't support recurrence
            recurrenceEndDate = null,
            category = AlarmColorMapper.mapToCategory(alarm.alarmColor),
            reminderMinutesBefore = AlarmColorMapper.mapToReminderMinutes(alarm.notificationEnabled),
            notificationChannelId = "event_reminders",
            ethiopianYear = ethiopianDateTime.year,
            ethiopianMonth = ethiopianDateTime.month,
            ethiopianDay = ethiopianDateTime.day,
            googleCalendarEventId = null,
            googleCalendarId = null,
            isSynced = false,
            syncedAt = null,
            createdAt = alarm.timeStamp,  // Preserve original creation time
            updatedAt = System.currentTimeMillis()  // Migration timestamp
        )
    }

    /**
     * Calculate the start time for the import window (30 days ago).
     */
    private fun getImportWindowStartTime(): Long {
        return System.currentTimeMillis() - TimeUnit.DAYS.toMillis(IMPORT_WINDOW_DAYS)
    }
}

/**
 * Result of a migration operation.
 */
data class MigrationResult(
    val successCount: Int, val failureCount: Int, val errors: List<String>
) {
    val totalProcessed: Int get() = successCount + failureCount
    val isSuccess: Boolean get() = successCount > 0 && errors.isEmpty()

    override fun toString(): String {
        return "MigrationResult(success=$successCount, failed=$failureCount, total=$totalProcessed)"
    }
}
