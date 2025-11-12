package com.example.demo.models

import com.example.demo.constants.Constants.CONNECTED_WITH
import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node("User")
data class User(
    @Id @GeneratedValue
    val user_id: Long? = null,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    @Relationship(type = CONNECTED_WITH, direction = Relationship.Direction.OUTGOING)
    val friends: MutableSet<User> = mutableSetOf(),
) {
    fun connectTo(other: User) {
        this.friends.add(other)
        other.friends.add(this)
    }

    fun disconnectFrom(other: User) {
        this.friends.remove(other)
        other.friends.add(this)
    }
}
