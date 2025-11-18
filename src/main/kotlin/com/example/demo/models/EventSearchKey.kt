package com.example.demo.models

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class EventSearchKey(
    val userId: Long,
    val hostName: String? = null,
    val eventName: String? = null,
    val location: String,
    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    val currentTime: LocalDateTime,
)
