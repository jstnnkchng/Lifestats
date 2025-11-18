package com.example.demo.models

import com.example.demo.constants.Constants.CONNECTED_WITH
import com.example.demo.constants.Constants.JSON_DATE_PATTERN
import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.time.LocalDateTime

@Node("User")
data class User(
    @Id
    val userId: Long,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val bio: String? = null,
    @Relationship(type = CONNECTED_WITH, direction = Relationship.Direction.OUTGOING)
    val friends: MutableSet<User> = mutableSetOf(),
    @JsonFormat(pattern = JSON_DATE_PATTERN)
    val joinDate: LocalDateTime,
)
