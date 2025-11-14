package com.example.demo.models

data class UserPartialSearchKey(
    val userId: Long,
    val pageNumber: Int,
    val pageSize: Int,
    val term: String,
    val username: String,
) {
    init {
        require(pageSize > 0) { "pageSize must be greater than 0" }
        require(pageNumber >= 0) { "pageNumber must be greater than 0" }
        require(term.isNotBlank()) { "Term must not be blank" }
        require(username.isNotBlank()) { "Username must not be blank" }
    }
}
