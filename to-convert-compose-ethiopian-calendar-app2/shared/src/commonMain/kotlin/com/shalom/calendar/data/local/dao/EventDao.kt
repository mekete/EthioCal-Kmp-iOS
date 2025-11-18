package com.shalom.calendar.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shalom.calendar.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Event operations.
 * KMP-compatible version using Flow for reactive queries.
 */
@Dao
interface EventDao {

    // ========== CREATE ==========

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>): List<Long>

    // ========== READ ==========

    @Query("SELECT * FROM events ORDER BY startTime ASC")
    fun getAllEventsFlow(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events ORDER BY startTime ASC")
    suspend fun getAllEvents(): List<EventEntity>

    @Query("SELECT * FROM events WHERE id = :eventId")
    suspend fun getEventById(eventId: String): EventEntity?

    @Query("SELECT * FROM events WHERE id = :eventId")
    fun getEventByIdFlow(eventId: String): Flow<EventEntity?>

    @Query("""
        SELECT * FROM events
        WHERE ethiopianYear = :year
        AND ethiopianMonth = :month
        AND ethiopianDay = :day
        ORDER BY startTime ASC
    """)
    fun getEventsForDate(year: Int, month: Int, day: Int): Flow<List<EventEntity>>

    @Query("""
        SELECT * FROM events
        WHERE ethiopianYear = :year
        AND ethiopianMonth = :month
        ORDER BY ethiopianDay ASC, startTime ASC
    """)
    fun getEventsForMonth(year: Int, month: Int): Flow<List<EventEntity>>

    @Query("""
        SELECT * FROM events
        WHERE ethiopianYear = :year
        ORDER BY ethiopianMonth ASC, ethiopianDay ASC, startTime ASC
    """)
    fun getEventsForYear(year: Int): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE recurrenceRule IS NOT NULL")
    suspend fun getRecurringEvents(): List<EventEntity>

    @Query("SELECT * FROM events WHERE category = :category ORDER BY startTime ASC")
    fun getEventsByCategory(category: String): Flow<List<EventEntity>>

    @Query("""
        SELECT * FROM events
        WHERE summary LIKE '%' || :query || '%'
        OR description LIKE '%' || :query || '%'
        ORDER BY startTime DESC
    """)
    fun searchEvents(query: String): Flow<List<EventEntity>>

    @Query("""
        SELECT * FROM events
        WHERE startTime >= :startOfDayMillis
        ORDER BY startTime ASC
        LIMIT :limit
    """)
    fun getUpcomingEvents(startOfDayMillis: Long, limit: Int = 10): Flow<List<EventEntity>>

    // ========== UPDATE ==========

    @Update
    suspend fun updateEvent(event: EventEntity): Int

    @Update
    suspend fun updateEvents(events: List<EventEntity>): Int

    @Query("""
        UPDATE events
        SET isSynced = :isSynced,
            syncedAt = :syncedAt,
            googleCalendarEventId = :googleEventId
        WHERE id = :eventId
    """)
    suspend fun updateSyncStatus(
        eventId: String,
        isSynced: Boolean,
        syncedAt: Long,
        googleEventId: String?
    ): Int

    // ========== DELETE ==========

    @Delete
    suspend fun deleteEvent(event: EventEntity): Int

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun deleteEventById(eventId: String): Int

    @Query("DELETE FROM events")
    suspend fun deleteAllEvents(): Int

    @Query("DELETE FROM events WHERE category = :category")
    suspend fun deleteEventsByCategory(category: String): Int

    @Query("DELETE FROM events WHERE startTime < :beforeTimeMillis AND recurrenceRule IS NULL")
    suspend fun deletePastEvents(beforeTimeMillis: Long): Int

    // ========== COUNT ==========

    @Query("SELECT COUNT(*) FROM events")
    suspend fun getEventCount(): Int

    @Query("""
        SELECT COUNT(*) FROM events
        WHERE ethiopianYear = :year
        AND ethiopianMonth = :month
        AND ethiopianDay = :day
    """)
    suspend fun getEventCountForDate(year: Int, month: Int, day: Int): Int
}
