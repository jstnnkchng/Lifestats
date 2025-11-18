package com.example.demo.models

import com.example.demo.constants.Constants.JSON_DATE_PATTERN
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
    @JsonFormat(pattern = JSON_DATE_PATTERN)
    val joinDate: LocalDateTime,
)
