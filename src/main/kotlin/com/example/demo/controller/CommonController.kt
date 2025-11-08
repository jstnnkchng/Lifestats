package com.example.demo.controller

import com.example.demo.constants.Qualifiers.JDBC
import com.example.demo.daos.UsersDao
import com.example.demo.models.UserCreationRequest
import com.example.demo.models.UserDetails
import com.example.demo.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService

@RestController
class CommonController {
    private val logger = LoggerFactory.getLogger(CommonController::class.java)

    @GetMapping("/api/v1/health")
    fun getHealth(
        @RequestHeader(required = false) header: Map<String,String>
    ): ResponseEntity<String> {
        logger.info("Get Health")
        return ResponseEntity("HelpTgthr application is up and running!", HttpStatus.OK)
    }
}