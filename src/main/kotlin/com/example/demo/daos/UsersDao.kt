package com.example.demo.daos

import com.example.demo.models.PageRequest
import com.example.demo.models.UserCreationRequest
import com.example.demo.models.UserDetails
import com.example.demo.models.UserSearchKey
import com.example.demo.rowmappers.UserDetailsRowMapper
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService

class UsersDao(
    private val executorService: ExecutorService,
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val userDetailsRowMapper: UserDetailsRowMapper,
) {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger(UsersDao::class.java)

        private val CREATE_OPERATION_NAME = "createUser"
        private val COUNT_OPERATION_NAME = "countUser"
        private val FINDBYPAGE_OPERATION_NAME = "findByPageUser"
        private val UPDATE_OPERATION_NAME = "updateUser"
        private val DELETE_OPERATION_NAME = "deleteUser"
    }

    open fun insertUser(request: UserCreationRequest): CompletableFuture<Int> {
        LOGGER.info(CREATE_OPERATION_NAME)

        val sql =
            """
            INSERT INTO users (
                username,
                first_name,
                last_name,
                email,
                phone,
                bio,
                join_date
            ) VALUES (
            :username,
            :first_name,
            :last_name,
            :email,
            :phone,
            :bio,
            :join_date
            )
            """.trimIndent()

        val namedParameters =
            MapSqlParameterSource()
                .addValue("username", request.username)
                .addValue("first_name", request.firstName)
                .addValue("last_name", request.lastName)
                .addValue("email", request.email)
                .addValue("phone", request.phoneNumber)
                .addValue("bio", request.bio)
                .addValue("join_date", request.joinDate)

        return CompletableFuture.supplyAsync(
            {
                jdbcTemplate.queryForObject(
                    sql,
                    namedParameters,
                    Int::class.java,
                )
            },
            executorService,
        )
    }

    open fun count(userSearchKey: UserSearchKey): CompletableFuture<Int> {
        LOGGER.info(COUNT_OPERATION_NAME)

        val sql =
            StringBuilder(1024)
                .append(
                    "SELECT COUNT(*) FROM users WHERE ",
                )

        val namedParameters = MapSqlParameterSource()

        addOptionalFilters(namedParameters, sql, userSearchKey)

        return CompletableFuture.supplyAsync(
            { jdbcTemplate.queryForObject(sql.toString(), namedParameters, Int::class.java) },
            executorService,
        )
    }

    open fun findByPages(
        pageRequest: PageRequest,
        userSearchKey: UserSearchKey,
    ): CompletableFuture<List<UserDetails>> {
        LOGGER.info(FINDBYPAGE_OPERATION_NAME)

        val sql =
            StringBuilder(1024)
                .append(
                    "SELECT * FROM users WHERE ",
                )

        val namedParameters = MapSqlParameterSource()

        addOptionalFilters(namedParameters, sql, userSearchKey)

        return CompletableFuture.supplyAsync(
            { jdbcTemplate.query(sql.toString(), namedParameters, userDetailsRowMapper) },
            executorService,
        )
    }

    private fun addOptionalFilters(
        namedParameters: MapSqlParameterSource,
        sql: StringBuilder,
        userSearchKey: UserSearchKey,
    ) {
        val conditions = mutableListOf<String>()

        if (!userSearchKey.username.isNullOrBlank()) {
            conditions.add("username = :username")
            namedParameters.addValue("username", userSearchKey.username)
        }
        if (!userSearchKey.firstName.isNullOrBlank()) {
            conditions.add("first_name = :firstName")
            namedParameters.addValue("firstName", userSearchKey.firstName)
        }
        if (!userSearchKey.lastName.isNullOrBlank()) {
            conditions.add("last_name = :lastName")
            namedParameters.addValue("lastName", userSearchKey.lastName)
        }

        sql.append(conditions.joinToString(" AND "))
    }
}
