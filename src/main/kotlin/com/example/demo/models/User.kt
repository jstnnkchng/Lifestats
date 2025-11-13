package com.example.demo.models

import com.example.demo.constants.Constants.CONNECTED_WITH
import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.time.LocalDateTime

@Node("User")
data class User(
    @Id @GeneratedValue
    val userId: Long? = null,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    @Relationship(type = CONNECTED_WITH, direction = Relationship.Direction.OUTGOING)
    val friends: MutableSet<User> = mutableSetOf(),
    val joinDate: LocalDateTime,
)
