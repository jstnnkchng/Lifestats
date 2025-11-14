package com.example.demo.service

import com.example.demo.daos.UserRepository
import com.example.demo.daos.UsersDao
import com.example.demo.models.PageRequest
import com.example.demo.models.PaginationResponse
import com.example.demo.models.User
import com.example.demo.models.UserCreationRequest
import com.example.demo.models.UserPartialSearchKey
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.concurrent.ExecutorService

class UserService(
    private val executorService: ExecutorService,
    private val usersDao: UsersDao,
    private val userRepository: UserRepository,
) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    fun createUser(request: UserCreationRequest): CompletableFuture<Int> {
        logger.info("Create User")

        val inserted = usersDao.insertUser(request.copy(joinDate = LocalDateTime.now()))

        // TODO: publish event to trigger insert into Neo4j Users table
        // for now, insert user into Neo4j
        val newUser =
            User(
                username = request.username,
                firstName = request.firstName,
                lastName = request.lastName,
                email = request.email,
                phoneNumber = request.phoneNumber,
                joinDate = LocalDateTime.now(),
            )
        userRepository.save(newUser)

        return inserted
    }

    fun openSearchUsers(request: UserPartialSearchKey): CompletableFuture<List<User>> {
        logger.info("Open Search Users")

        val pageRequest =
            PageRequest(
                request.pageNumber,
                request.pageSize,
            )

        return supplyAsync(
            {
                userRepository.findUsersByLikeTermWithFriendPriority(
                    username = request.username,
                    searchTerm = request.term,
                    offset = pageRequest.offset,
                    limit = pageRequest.limit,
                )
            },
            executorService,
        )
    }

    fun fetchPaginatedUsers(request: UserPartialSearchKey): CompletableFuture<PaginationResponse<User>> {
        logger.info("Search Paginated Users")

        val pageRequest =
            PageRequest(
                request.pageNumber,
                request.pageSize,
            )

        return supplyAsync {
            userRepository.findUsersByTerm(
                searchTerm = request.term,
                offset = pageRequest.offset,
                limit = pageRequest.limit,
            )
        }.thenApplyAsync({
                userList ->
            buildPaginatedUserDetails(
                count = userRepository.findUsersCountByTerm(request.term),
                pageNumber = request.pageNumber,
                pageSize = request.pageSize,
                userList,
            )
        }, executorService)
            .exceptionallyAsync(
                { ex ->
                    logger.error("Error fetching paginated users")
                    throw RuntimeException("Error fetching paginated users", ex)
                },
                executorService,
            )
    }

    private fun buildPaginatedUserDetails(
        count: Int,
        pageNumber: Int,
        pageSize: Int,
        records: List<User>,
    ): PaginationResponse<User> {
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
