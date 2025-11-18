package com.example.demo.service

import com.example.demo.daos.EventsDao
import com.example.demo.models.EventCreationRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class EventsService(
    private val eventsDao: EventsDao,
) {
    private val logger = LoggerFactory.getLogger(EventsService::class.java)

    suspend fun createEvent(request: EventCreationRequest): Long =
        withContext(Dispatchers.Default) {
            logger.info("Create Event")

            try {
                val eventIdDeferred = eventsDao.insertEvent(request.copy(currentParticipants = 0, createdAt = LocalDateTime.now()))

                eventIdDeferred.await()
            } catch (e: Exception) {
                logger.error("Error creating event with request: $request")
                throw RuntimeException("Error creating event", e)
            }
        }
}
