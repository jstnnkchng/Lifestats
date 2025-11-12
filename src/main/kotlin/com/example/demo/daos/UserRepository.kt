package com.example.demo.daos

import com.example.demo.models.User
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : Neo4jRepository<User, Long> {
    fun findByUsername(username: String): User?

    @Query(
        """
        MATCH (me:User {username: ${'$'}username})

        OPTIONAL MATCH (me)-[r:CONNECTED_WITH]-(u:User)
        WHERE toLower(u.username) CONTAINS toLower(${'$'}searchTerm)
        WITH u,
             CASE
                WHEN r IS NOT NULL THEN 1  // directly connected
                ELSE 0
             END AS isConnected

        RETURN u AS user, isConnected
        ORDER BY isConnected DESC, toLower(u.username)
        LIMIT ${'$'}limit
    """,
    )
    fun findConnectionByLikeString(
        username: String,
        searchTerm: String,
        limit: Int,
    ): List<User>

    @Query(
        """
        MATCH (a:User {id: ${'$'}userId}), (b:User {id: $${'$'}otherUserId})
        MERGE (a)-[:CONNECTED_WITH]-(b)
    """,
    )
    fun addConnection(
        userId: Long,
        otherUserId: Long,
    )
}
