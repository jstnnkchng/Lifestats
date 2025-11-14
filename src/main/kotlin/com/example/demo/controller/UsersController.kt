package com.example.demo.controller

import com.example.demo.models.PaginationResponse
import com.example.demo.models.User
import com.example.demo.models.UserCreationRequest
import com.example.demo.models.UserPartialSearchKey
import com.example.demo.models.UserWithDegree
import com.example.demo.service.UserService
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.future
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/api/v1/users")
class UsersController(
    private val userService: UserService,
) {
    private val logger = LoggerFactory.getLogger(UsersController::class.java)

    companion object {
        private val ADD_FRIEND_OPERATION_NAME = "addFriend"
        private val CREATE_OPERATION_NAME = "createUser"
        private val DELETE_OPERATION_NAME = "deleteUser"
        private val READ_OPERATION_NAME = "readUser"
        private val REMOVE_FRIEND_OPERATION_NAME = "removeFriend"
        private val UPDATE_OPERATION_NAME = "updateUser"
    }

    @PostMapping("/create")
    fun createUser(
        @RequestBody request: UserCreationRequest,
    ): CompletableFuture<ResponseEntity<Long>> {
        logger.info("$CREATE_OPERATION_NAME: $request")

        return CoroutineScope(CoroutineName("CreateNewUser") + Dispatchers.IO).future {
            ResponseEntity.ok(userService.createUser(request))
        }
    }

    @GetMapping("/search")
    fun search(
        @RequestBody request: UserPartialSearchKey,
    ): CompletableFuture<ResponseEntity<PaginationResponse<UserWithDegree>>> {
        logger.info("$READ_OPERATION_NAME: $request")

        return CoroutineScope(CoroutineName("searchUser") + Dispatchers.IO).future {
            ResponseEntity.ok(userService.fetchPaginatedUsers(request))
        }
    }

    @GetMapping("/searchPartial")
    fun searchPartial(
        @RequestBody request: UserPartialSearchKey,
    ): CompletableFuture<ResponseEntity<List<User>>> {
        logger.info("$READ_OPERATION_NAME: $request")

        return CoroutineScope(CoroutineName("searchPartialUser") + Dispatchers.IO).future {
            ResponseEntity.ok(userService.searchUsersPartial(request))
        }
    }

    @PostMapping("/addFriend")
    fun addFriend(
        @RequestParam(value = "userId") userId: Long,
        @RequestParam(value = "targetUserId") targetUserId: Long,
    ): CompletableFuture<ResponseEntity<Boolean>> {
        logger.info("$ADD_FRIEND_OPERATION_NAME: userId: $userId <-> targetUserId: $targetUserId")

        return CoroutineScope(CoroutineName("addFriend") + Dispatchers.IO).future {
            ResponseEntity.ok(userService.addFriend(userId, targetUserId))
        }
    }

    @PostMapping("/removeFriend")
    fun removeFriend(
        @RequestParam(value = "userId") userId: Long,
        @RequestParam(value = "targetUserId") targetUserId: Long,
    ): CompletableFuture<ResponseEntity<Boolean>> {
        logger.info("$REMOVE_FRIEND_OPERATION_NAME: userId: $userId <-> targetUserId: $targetUserId")

        return CoroutineScope(CoroutineName("removeFriend") + Dispatchers.IO).future {
            ResponseEntity.ok(userService.removeFriend(userId, targetUserId))
        }
    }
}
