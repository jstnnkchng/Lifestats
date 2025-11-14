package com.example.demo.config

import com.example.demo.constants.Constants.JDBC
import com.example.demo.daos.UsersDao
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.concurrent.ExecutorService

@Import(JdbcDataSourceBeans::class)
@Configuration
class DaoBeans {
    @Bean
    open fun usersDao(
        @Qualifier(JDBC) dbPool: ExecutorService,
        jdbcTemplate: NamedParameterJdbcTemplate,
    ): UsersDao = UsersDao(dbPool, jdbcTemplate)
}
