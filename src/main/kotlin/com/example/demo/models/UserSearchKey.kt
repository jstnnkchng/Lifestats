package com.example.demo.models

data class UserSearchKey(
    val pageNumber: Int,
    val pageSize: Int,
    val username: String?,
    val firstName: String?,
    val lastName: String?,
) {
    init {
        require(pageNumber >= 0) { "pageNumber must be greater than 0" }
        require(listOf(username, firstName, lastName).count { !it.isNullOrBlank() } >= 1) { "At least one field must be provided" }
    }
}
