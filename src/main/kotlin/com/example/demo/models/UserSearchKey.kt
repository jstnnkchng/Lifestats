package com.example.demo.models

data class UserSearchKey(
    val username: String?,
    val firstName: String?,
    val lastName: String?,
) {
    init {
        require(listOf(username, firstName, lastName).count { !it.isNullOrBlank() } >= 1) { "At least one field must be provided" }
    }
}
