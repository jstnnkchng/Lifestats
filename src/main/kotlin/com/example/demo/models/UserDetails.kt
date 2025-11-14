package com.example.demo.models

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class UserDetails(
    val userId: Long? = null,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val bio: String?,
    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    val joinDate: LocalDateTime,
)
