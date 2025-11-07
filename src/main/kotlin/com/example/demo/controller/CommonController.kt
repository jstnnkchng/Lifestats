package com.example.demo.controller

import com.example.demo.daos.UsersDao
import com.example.demo.models.UserDetails
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

@RestController
class CommonController(
     private val usersDao: UsersDao,
) {
    private val logger = LoggerFactory.getLogger(CommonController::class.java)

    @GetMapping("/api/v1/health")
    fun getHealth(
        @RequestHeader(required = false) header: Map<String,String>
    ): ResponseEntity<String> {
        logger.info("Get Health")
        return ResponseEntity("Lifestats application is up and running!", HttpStatus.OK)
    }

    @GetMapping("/api/v1/users")
    open fun getUsersById(
        @RequestHeader header: Map<String,String>,
        @RequestParam userId: Int,
    ): ResponseEntity<CompletableFuture<List<UserDetails>>> {


        return ResponseEntity.ok(usersDao.getUserByUserId(userId))
    }

}