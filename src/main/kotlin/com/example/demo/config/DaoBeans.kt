package com.example.demo.config

import com.example.demo.constants.Constants.EVENTS
import com.example.demo.constants.Constants.JDBC
import com.example.demo.daos.EventsDao
import com.example.demo.daos.UsersDao
import com.example.demo.models.Event
import com.example.demo.rowmappers.EventRowMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.concurrent.ExecutorService

@Import(JdbcDataSourceBeans::class)
@Configuration
class DaoBeans {
    @Bean
    @Qualifier(EVENTS)
    open fun eventRowMapper(): RowMapper<Event> = EventRowMapper()

    @Bean
    open fun usersDao(
        @Qualifier(JDBC) dbPool: ExecutorService,
        jdbcTemplate: NamedParameterJdbcTemplate,
    ): UsersDao = UsersDao(dbPool, jdbcTemplate)

    @Bean
    open fun eventsDao(
        @Qualifier(JDBC) dbPool: ExecutorService,
        jdbcTemplate: NamedParameterJdbcTemplate,
        @Qualifier(EVENTS) eventRowMapper: RowMapper<Event>,
    ): EventsDao = EventsDao(dbPool, jdbcTemplate, eventRowMapper)
}
