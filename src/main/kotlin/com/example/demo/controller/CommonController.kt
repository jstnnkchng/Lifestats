package com.example.demo.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class CommonController {
    private val logger = LoggerFactory.getLogger(CommonController::class.java)

    @GetMapping("/api/v1/health")
    fun getHealth(
        @RequestHeader(required = false) header: Map<String, String>,
    ): ResponseEntity<String> {
        logger.info("Get Health")
        return ResponseEntity("HelpTgthr application is up and running!", HttpStatus.OK)
    }
}
