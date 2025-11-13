package com.example.demo.daos

import com.example.demo.models.User
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : Neo4jRepository<User, Long> {
    fun findByUsername(username: String): User?

    fun findByEmail(email: String): User?

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
    fun findUsersByLikeStringWithFriendPriority(
        @Param("username") username: String,
        @Param("searchTerm") searchTerm: String,
        @Param("limit") limit: Int,
    ): List<User>

    @Query(
        """
        MATCH (a:User {userId: ${ '$' }userId})-[:CONNECTED_WITH]-(b:User)
        RETURN b
    """,
    )
    fun findConnections(
        @Param("userId") userId: Long,
    ): List<User>

    @Query(
        """
        MATCH (a:User {userId: ${'$'}userId}), (b:User {userId: $${'$'}otherUserId})
        MERGE (a)-[:CONNECTED_WITH]-(b)
    """,
    )
    fun addConnection(
        @Param("userId") userId: Long,
        @Param("otherUserId") otherUserId: Long,
    )

    @Query(
        """
        MATCH (a:User {userId: ${ '$' }userId})-[r:CONNECTED_WITH]-(b:User {userId: ${ '$' }otherId})
        DELETE r
    """,
    )
    fun removeConnection(
        @Param("userId") userId: Long,
        @Param("otherId") otherId: Long,
    )
}
