package com.example.demo.daos

import com.example.demo.models.Event
import com.example.demo.models.EventCreationRequest
import com.example.demo.models.EventSearchKey
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService

class EventsDao(
    private val executorService: ExecutorService,
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val eventRowMapper: RowMapper<Event>,
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
            current_participants,
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
            :current_participants,
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
                .addValue("current_participants", request.currentParticipants)
                .addValue("max_num_participants", request.maxNumParticipants)
                .addValue("visibility", request.visibility)
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

    fun count(searchKey: EventSearchKey): CompletableFuture<Int> {
        LOGGER.info(COUNT_OPERATION_NAME)

        val sql =
            StringBuilder(1024)
                .append(
                    "SELECT COUNT(*) FROM events " +
                        "WHERE user_id != :user_id" +
                        "AND LOWER(location) = LOWER(:location) " +
                        "AND eventTime > :current_time ",
                )

        val namedParameters: MapSqlParameterSource =
            MapSqlParameterSource()
                .addValue("user_id", searchKey.userId)
                .addValue("location", searchKey.location)
                .addValue("current_time", searchKey.currentTime)

        addOptionalFilters(sql, searchKey, namedParameters)

        return CompletableFuture.supplyAsync(
            {
                jdbcTemplate.queryForObject(
                    sql.toString(),
                    namedParameters,
                    Int::class.java,
                )
            },
            executorService,
        )
    }

    private fun addOptionalFilters(
        sql: StringBuilder,
        searchKey: EventSearchKey,
        namedParameters: MapSqlParameterSource,
    ) {
        if (!searchKey.hostName.isNullOrEmpty()) {
            sql.append("AND host_name = :host_name ")
            namedParameters.addValue("host_name", searchKey.hostName)
        }
        if (!searchKey.eventName.isNullOrEmpty()) {
            sql.append("AND event_name = :event_name ")
            namedParameters.addValue("event_name", searchKey.eventName)
        }
    }
}
