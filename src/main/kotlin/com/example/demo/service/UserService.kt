package com.example.demo.service

import com.example.demo.daos.UsersDao
import com.example.demo.models.PaginationResponse
import com.example.demo.models.UserCreationRequest
import com.example.demo.models.UserDetails
import com.example.demo.models.UserSearchKey
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService

class UserService(
    private val executorService: ExecutorService,
    private val usersDao: UsersDao,
) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    open fun createUser(
        request: UserCreationRequest,
    ): CompletableFuture<Int> {
        logger.info("Create User")

        return usersDao.insertUser(request.copy(joinDate = LocalDateTime.now()))
    }

    private fun buildPaginatedUserDetails(
        count: Int,
        pageNumber: Int,
        pageSize: Int,
        records: List<UserDetails>,
    ): PaginationResponse<UserDetails> {
        val totalRecords = count
        val currentPage = pageNumber
        val last = currentPage == totalRecords / pageSize
        val nextPage = if (last) null else currentPage + 1
        val prevPage = if (currentPage - 1 < 0) null else currentPage - 1

        return PaginationResponse(
            records,
            currentPage,
            pageSize,
            totalRecords,
            nextPage,
            prevPage,
        )
    }


}