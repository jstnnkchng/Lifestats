package com.example.demo.constants

enum class VisibilityType(val visibilityValue: String) {
    FIRSTS("FIRSTCONNECTIONS"),
    SECONDS("SECONDCONNECTIONS"),
    THIRDS("THIRDCONNECTIONS"),
    OPEN("OPEN"), ;

    companion object {
        private val visibilityMap = VisibilityType.entries.associateBy { it.visibilityValue }

        fun fromString(value: String) = visibilityMap[value]
    }
}
