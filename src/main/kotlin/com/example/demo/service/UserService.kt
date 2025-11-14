package com.example.demo.service

import com.example.demo.daos.UserRepository
import com.example.demo.daos.UsersDao
import com.example.demo.models.PageRequest
import com.example.demo.models.PaginationResponse
import com.example.demo.models.User
import com.example.demo.models.UserCreationRequest
import com.example.demo.models.UserPartialSearchKey
import com.example.demo.models.UserWithDegree
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class UserService(
    private val usersDao: UsersDao,
    private val userRepository: UserRepository,
) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    suspend fun createUser(request: UserCreationRequest): Long =
        withContext(Dispatchers.Default) {
            logger.info("Create User")

            try {
                val userIdDeferred =
                    async {
                        val userId = usersDao.insertUser(request.copy(joinDate = LocalDateTime.now())).await()
                        val newUser =
                            User(
                                userId = userId,
                                username = request.username,
                                firstName = request.firstName,
                                lastName = request.lastName,
                                email = request.email,
                                phoneNumber = request.phoneNumber,
                                joinDate = LocalDateTime.now(),
                            )
                        userRepository.save(newUser)

                        userId
                    }
                userIdDeferred.await()
            } catch (e: Exception) {
                logger.error("Error creating user with request: $request")
                throw RuntimeException("Error creating user with request", e)
            }
        }

    suspend fun searchUsersPartial(request: UserPartialSearchKey): List<User> =
        withContext(Dispatchers.IO) {
            logger.info("Open Search Users")

            try {
                val pageRequest =
                    PageRequest(
                        request.pageNumber,
                        request.pageSize,
                    )

                userRepository.findUsersByLikeTermWithFriendPriority(
                    username = request.username,
                    searchTerm = request.term,
                    offset = pageRequest.offset,
                    limit = pageRequest.limit,
                )
            } catch (e: Exception) {
                logger.error("Error during search users partial with request: $request")
                throw RuntimeException("Error during search users partial", e)
            }
        }

    suspend fun fetchPaginatedUsers(request: UserPartialSearchKey): PaginationResponse<UserWithDegree> =
        withContext(Dispatchers.IO) {
            logger.info("Search Paginated Users")

            try {
                val pageRequest =
                    PageRequest(
                        request.pageNumber,
                        request.pageSize,
                    )

                val usersDeferred =
                    async {
                        userRepository.findUsersByTerm(
                            userId = request.userId,
                            searchTerm = request.term,
                            offset = pageRequest.offset,
                            limit = pageRequest.limit,
                        )
                    }

                val usersCountDeferred =
                    async {
                        userRepository.findUsersCountByTerm(request.term)
                    }

                buildPaginatedUserDetails(
                    count = usersCountDeferred.await(),
                    pageNumber = request.pageNumber,
                    pageSize = request.pageSize,
                    records = usersDeferred.await(),
                )
            } catch (e: Exception) {
                logger.error("Error fetching paginated users with request: $request")
                throw RuntimeException("Error fetching paginated users", e)
            }
        }

    suspend fun addFriend(userId: Long, targetUserId: Long): Boolean = withContext(Dispatchers.IO) {
        logger.info("Add friend")

        try{
            val outgoingConnection = async {
                userRepository.addConnection(
                    userId = userId,
                    targetUserId = targetUserId
                )
            }
            val incomingConnection = async {
                userRepository.addConnection(
                    userId = targetUserId,
                    targetUserId = userId
                )
            }

            (outgoingConnection.await() && incomingConnection.await())
        } catch(e: Exception) {
            logger.error("Error adding friend connection for request: userId $userId, targetUserId $targetUserId")
            throw RuntimeException("Error adding friend connection", e)
        }
    }

    suspend fun removeFriend(userId: Long, targetUserId: Long): Boolean = withContext(Dispatchers.IO) {
        logger.info("Remove friend")

        try{
            val outgoingConnection = async {
                userRepository.removeConnection(
                    userId = userId,
                    targetUserId = targetUserId
                )
            }
            val incomingConnection = async {
                userRepository.removeConnection(
                    userId = targetUserId,
                    targetUserId = userId
                )
            }

            (outgoingConnection.await() && incomingConnection.await())
        } catch(e: Exception) {
            logger.error("Error removing friend connection for request: userId $userId, targetUserId $targetUserId")
            throw RuntimeException("Error removing friend connection", e)
        }
    }


    private fun buildPaginatedUserDetails(
        count: Int,
        pageNumber: Int,
        pageSize: Int,
        records: List<UserWithDegree>,
    ): PaginationResponse<UserWithDegree> {
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
