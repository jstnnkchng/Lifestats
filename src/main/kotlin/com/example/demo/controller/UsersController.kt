package com.example.demo.controller

import com.example.demo.constants.Constants.CPU
import com.example.demo.models.PaginationResponse
import com.example.demo.models.User
import com.example.demo.models.UserCreationRequest
import com.example.demo.models.UserPartialSearchKey
import com.example.demo.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService

@RestController
@RequestMapping("/api/v1/users")
class UsersController(
    @Qualifier(CPU) private val executorService: ExecutorService,
    private val userService: UserService,
) {
    private val logger = LoggerFactory.getLogger(UsersController::class.java)

    companion object {
        private val CREATE_OPERATION_NAME = "createUser"
        private val READ_OPERATION_NAME = "readUser"
        private val UPDATE_OPERATION_NAME = "updateUser"
        private val DELETE_OPERATION_NAME = "deleteUser"
    }

    @PostMapping("/create")
    open fun createUser(
        @RequestBody request: UserCreationRequest,
    ): CompletableFuture<ResponseEntity<Int>> {
        logger.info("$CREATE_OPERATION_NAME: $request")

        return userService.createUser(request)
            .thenApplyAsync({ ResponseEntity.ok(it) }, executorService)
    }

    @GetMapping("/search")
    open fun search(
        @RequestBody request: UserPartialSearchKey,
    ): CompletableFuture<ResponseEntity<PaginationResponse<User>>> {
        logger.info("$READ_OPERATION_NAME: $request")

        return userService.fetchPaginatedUsers(request)
            .thenApplyAsync({ ResponseEntity.ok(it) }, executorService)
    }

    @GetMapping("/searchPartial")
    open fun searchPartial(
        @RequestBody request: UserPartialSearchKey,
    ): CompletableFuture<ResponseEntity<List<User>>> {
        logger.info("$READ_OPERATION_NAME: $request")

        return userService.openSearchUsers(request)
            .thenApplyAsync({ ResponseEntity.ok(it) }, executorService)
    }
}
