package com.example.demo.models

import com.example.demo.constants.Constants.JSON_DATE_PATTERN
import com.example.demo.constants.VisibilityType
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class Event(
    val eventId: Long,
    val userId: Long,
    val hostName: String,
    val eventName: String,
    val eventDescription: String,
    val location: String,
    @JsonFormat(pattern = JSON_DATE_PATTERN)
    val eventTime: LocalDateTime,
    val currentParticipants: Int,
    val maxNumParticipants: Int?,
    val visibility: VisibilityType?,
    @JsonFormat(pattern = JSON_DATE_PATTERN)
    val createdAt: LocalDateTime,
) {
    init {
        require(eventName.isNotEmpty()) { "eventName cannot be empty" }
        require(hostName.isNotEmpty()) { "hostName cannot be empty" }
        require(eventDescription.isNotEmpty()) { "eventDescription cannot be empty" }
        require(location.isNotEmpty()) { "location cannot be empty" }
    }
}
