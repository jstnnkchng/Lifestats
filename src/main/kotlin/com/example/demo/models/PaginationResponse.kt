package com.example.demo.models

data class PaginationResponse<T>(
    val content: List<T>,
    val currentPage: Int,
    val pageSize: Int,
    val totalRecords: Int,
    val nextPage: Int? = null,
    val prevPage: Int? = null,
)
