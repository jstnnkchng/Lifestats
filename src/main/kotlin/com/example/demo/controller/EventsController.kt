package com.example.demo.controller

import com.example.demo.models.EventCreationRequest
import com.example.demo.service.EventsService
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.future
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/api/v1/events")
class EventsController(
    private val eventsService: EventsService,
) {
    private val logger = LoggerFactory.getLogger(EventsController::class.java)

    companion object {
        private val CREATE_OPERATION_NAME = "createEvent"
        private val DELETE_OPERATION_NAME = "deleteEvent"
        private val READ_OPERATION_NAME = "readEvent"
        private val UPDATE_OPERATION_NAME = "updateEvent"
    }

    @PostMapping("/create")
    fun createEvent(
        @RequestBody request: EventCreationRequest,
    ): CompletableFuture<ResponseEntity<Long>> {
        logger.info("$CREATE_OPERATION_NAME: $request")

        return CoroutineScope(CoroutineName("CreateNewUser") + Dispatchers.IO).future {
            ResponseEntity.ok(eventsService.createEvent(request))
        }
    }
}
