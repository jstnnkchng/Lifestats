package com.example.demo.models

import java.time.LocalDateTime

data class UserCreationRequest(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val bio: String = "",
    val joinDate: LocalDateTime? = null,
) {
    init {
        require(username.isNotBlank()) { "Username cannot be blank" }
        require(firstName.isNotBlank()) { "First name cannot be blank" }
        require(lastName.isNotBlank()) { "Last name cannot be blank" }
        require(email.isNotBlank()) { "Email cannot be blank" }
        require(phoneNumber.isNotBlank()) { "Phone number cannot be blank" }
        require(phoneNumber.length == 10 && phoneNumber.all { it.isDigit() }) { "Phone number must be digits" }
    }
}
