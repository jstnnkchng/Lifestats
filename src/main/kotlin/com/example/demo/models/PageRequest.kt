package com.example.demo.models

data class PageRequest(
    val number: Int,
    val size: Int,
) {
    val offset: Int get() = number * size
    val limit: Int get() = size
}
