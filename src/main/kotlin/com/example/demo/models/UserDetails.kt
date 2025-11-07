package com.example.demo.models

import java.time.LocalDateTime

data class UserDetails(
    val userId: Int,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val bio: String,
    val joinDate: LocalDateTime,
    val profileImageUrl: String,
    val aiSummary: String,
)
