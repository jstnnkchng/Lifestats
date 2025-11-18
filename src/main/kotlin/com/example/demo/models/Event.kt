package com.example.demo.models

import com.example.demo.constants.VisibilityType
import java.time.LocalDateTime

data class Event(
    val eventId: Long,
    val userId: Long,
    val hostName: String,
    val eventName: String,
    val eventDescription: String,
    val location: String,
    val eventTime: LocalDateTime,
    val maxNumParticipants: Int?,
    val visibility: VisibilityType,
    val createdAt: LocalDateTime,
) {
    init {
        require(eventName.isNotEmpty()) { "eventName cannot be empty" }
        require(hostName.isNotEmpty()) { "hostName cannot be empty" }
        require(eventDescription.isNotEmpty()) { "eventDescription cannot be empty" }
        require(location.isNotEmpty()) { "location cannot be empty" }
    }
}
