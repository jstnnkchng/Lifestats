package com.example.demo.daos

import com.example.demo.models.EventCreationRequest
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService

class EventsDao(
    private val executorService: ExecutorService,
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger(EventsDao::class.java)

        private val CREATE_OPERATION_NAME = "createEvent"
        private val COUNT_OPERATION_NAME = "countEvent"
        private val FINDBYPAGE_OPERATION_NAME = "findByPageEvent"
        private val UPDATE_OPERATION_NAME = "updateEvent"
        private val DELETE_OPERATION_NAME = "deleteEvent"
    }

    fun insertEvent(request: EventCreationRequest): CompletableFuture<Long> {
        LOGGER.info(CREATE_OPERATION_NAME)

        val sql =
            """
            INSERT INTO events (
            user_id,
            host_name,
            event_name,
            event_description,
            location,
            event_time,
            max_num_participants,
            visibility,
            created_at
            ) VALUES (
            :user_id,
            :host_name,
            :event_name,
            :event_description,
            :location,
            :event_time,
            :max_num_participants,
            :visibility,
            :created_at
            )
            RETURNING event_id
            """.trimIndent()

        val namedParameters =
            MapSqlParameterSource()
                .addValue("user_id", request.userId)
                .addValue("host_name", request.hostName)
                .addValue("event_name", request.eventName)
                .addValue("event_description", request.eventDescription)
                .addValue("location", request.location)
                .addValue("event_time", request.eventTime)
                .addValue("max_num_participants", request.maxNumParticipants)
                .addValue("visibility", request.visiblity)
                .addValue("created_at", request.createdAt)

        return CompletableFuture.supplyAsync(
            {
                jdbcTemplate.queryForObject(
                    sql,
                    namedParameters,
                    Long::class.java,
                )
            },
            executorService,
        )
    }
}
