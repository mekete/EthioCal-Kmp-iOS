package com.shalom.calendar.data.repository

import com.shalom.calendar.data.local.dao.EventDao
import com.shalom.calendar.data.local.entity.EventEntity
import com.shalom.calendar.data.local.entity.EventInstance
import com.shalom.calendar.domain.model.currentTimeMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

/**
 * Repository for event operations.
 * KMP-compatible version.
 *
 * Provides clean API for UI layer:
 * - CRUD operations for events
 * - Event queries by date, month, year
 * - Uses Flow for reactive data
 */
class EventRepository(
    private val eventDao: EventDao
) {

    // ========== QUERY OPERATIONS (Flow-based) ==========

    fun getAllEvents(): Flow<List<EventEntity>> {
        return eventDao.getAllEventsFlow()
    }

    fun getEventById(eventId: String): Flow<EventEntity?> {
        return eventDao.getEventByIdFlow(eventId)
    }

    fun getEventsForDate(year: Int, month: Int, day: Int): Flow<List<EventInstance>> {
        return eventDao.getEventsForDate(year, month, day).map { events ->
            events.map { it.toEventInstance() }
        }
    }

    fun getEventsForMonth(year: Int, month: Int): Flow<List<EventInstance>> {
        return eventDao.getEventsForMonth(year, month).map { events ->
            events.map { it.toEventInstance() }
        }
    }

    fun getUpcomingEvents(limit: Int = 10): Flow<List<EventInstance>> {
        val startOfToday = currentTimeMillis()
        return eventDao.getUpcomingEvents(startOfToday, limit).map { events ->
            events.map { it.toEventInstance() }
        }
    }

    fun getEventsByCategory(category: String): Flow<List<EventEntity>> {
        return eventDao.getEventsByCategory(category)
    }

    fun searchEvents(query: String): Flow<List<EventEntity>> {
        return eventDao.searchEvents(query)
    }

    // ========== MUTATION OPERATIONS ==========

    suspend fun insertEvent(event: EventEntity): Long {
        return eventDao.insertEvent(event)
    }

    suspend fun insertEvents(events: List<EventEntity>): List<Long> {
        return eventDao.insertEvents(events)
    }

    suspend fun updateEvent(event: EventEntity): Int {
        return eventDao.updateEvent(event)
    }

    suspend fun deleteEvent(event: EventEntity): Int {
        return eventDao.deleteEvent(event)
    }

    suspend fun deleteEventById(eventId: String): Int {
        return eventDao.deleteEventById(eventId)
    }

    suspend fun deleteAllEvents(): Int {
        return eventDao.deleteAllEvents()
    }

    suspend fun deletePastEvents(beforeTimeMillis: Long): Int {
        return eventDao.deletePastEvents(beforeTimeMillis)
    }

    // ========== COUNT OPERATIONS ==========

    suspend fun getEventCount(): Int {
        return eventDao.getEventCount()
    }

    suspend fun getEventCountForDate(year: Int, month: Int, day: Int): Int {
        return eventDao.getEventCountForDate(year, month, day)
    }

    // ========== HELPER FUNCTIONS ==========

    private fun EventEntity.toEventInstance(): EventInstance {
        return EventInstance(
            eventId = id,
            summary = summary,
            description = description,
            instanceStart = startTime,
            instanceEnd = endTime,
            isAllDay = isAllDay,
            category = category,
            reminderMinutesBefore = reminderMinutesBefore,
            ethiopianYear = ethiopianYear,
            ethiopianMonth = ethiopianMonth,
            ethiopianDay = ethiopianDay,
            isRecurring = recurrenceRule != null,
            originalEvent = this
        )
    }
}
