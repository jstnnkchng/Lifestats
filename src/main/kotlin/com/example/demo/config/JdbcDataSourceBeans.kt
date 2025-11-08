package com.example.demo.config

import com.example.demo.constants.Qualifiers.JDBC
import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.util.concurrent.TimeUnit.MILLISECONDS
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import javax.sql.DataSource

@Configuration
open class JdbcDataSourceBeans {

    @Bean
    open fun postgresDataSource(env: Environment): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(env.getRequiredProperty("spring.datasource.driver-class-name"))
        dataSource.setUrl(env.getRequiredProperty("spring.datasource.url"))
        dataSource.setUsername(env.getRequiredProperty("spring.datasource.username"))
        dataSource.setPassword(env.getRequiredProperty("spring.datasource.password"))
        return dataSource
    }

    @Bean
    open fun jdbcTemplatePostgres(postgresDataSource: DataSource): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(postgresDataSource)
    }
}