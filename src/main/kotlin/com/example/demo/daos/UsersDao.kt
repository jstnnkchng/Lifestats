package com.example.demo.daos

import com.example.demo.models.UserDetails
import com.example.demo.rowmappers.UserDetailsRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService

class UsersDao(
    private val executorService: ExecutorService,
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val userDetailsRowMapper: UserDetailsRowMapper
    ) {

    fun getUserByUserId(
        userId: Int,
    ): CompletableFuture<List<UserDetails>> {
        val sql = """
            SELECT * FROM users 
            WHERE user_id = :user_id
        """.trimIndent()

        val namedParameters = MapSqlParameterSource()
        namedParameters.addValue("user_id", userId)

        return CompletableFuture.supplyAsync(
            { jdbcTemplate.query(sql, namedParameters, userDetailsRowMapper)  },
            executorService
        )
    }

}