package com.example.demo.daos

import com.example.demo.models.User
import com.example.demo.models.UserWithDegree
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : Neo4jRepository<User, Long> {
    fun findByUsername(username: String): User?

    fun findByEmail(email: String): User?

    @Query(
        """
        MATCH (root:User {id: ${'$'}user_id})

        MATCH (u:User)
        WHERE toLower(u.username) CONTAINS toLower(${'$'}search_term)
           OR toLower(u.name) CONTAINS toLower(${'$'}search_term)

        OPTIONAL MATCH path = shortestPath((root)-[*..4]-(u))

        RETURN
            u,
            CASE
                WHEN path IS NULL THEN -1
                ELSE length(path)
            END AS degree
        SKIP ${'$'}offset
        LIMIT ${'$'}limit
    """,
    )
    fun findUsersByTerm(
        @Param("user_id") userId: Long,
        @Param("search_term") searchTerm: String,
        @Param("offset") offset: Int,
        @Param("limit") limit: Int,
    ): List<UserWithDegree>

    @Query(
        """
        MATCH (u:User)
        WHERE toLower(u.username) CONTAINS toLower(${ '$' }search_term)
           OR toLower(u.name) CONTAINS toLower(${ '$' }search_term)
        RETURN count(u) AS totalCount
    """,
    )
    fun findUsersCountByTerm(
        @Param("search_term") searchTerm: String,
    ): Int

    @Query(
        """
        MATCH (me:User {username: ${'$'}username})

        OPTIONAL MATCH (me)-[r:CONNECTED_WITH]-(u:User)
        WHERE toLower(u.username) CONTAINS toLower(${'$'}search_term)
        WITH u,
             CASE
                WHEN r IS NOT NULL THEN 1  // directly connected
                ELSE 0
             END AS isConnected

        RETURN u AS user, isConnected
        ORDER BY isConnected DESC, toLower(u.username)
        SKIP ${'$'}offset
        LIMIT ${'$'}limit
    """,
    )
    fun findUsersByLikeTermWithFriendPriority(
        @Param("username") username: String,
        @Param("search_term") searchTerm: String,
        @Param("limit") limit: Int,
        @Param("offset") offset: Int,
    ): List<User>

    @Query(
        """
        MATCH (a:User {userId: ${ '$' }user_id})-[:CONNECTED_WITH]-(b:User)
        RETURN b
    """,
    )
    fun findConnections(
        @Param("user_id") userId: Long,
    ): List<User>

    @Query(
        """
        MATCH (a:User {userId: ${'$'}user_id}), (b:User {userId: $${'$'}target_user_id})
        MERGE (a)-[:CONNECTED_WITH]-(b)
        RETURN true
    """,
    )
    fun addConnection(
        @Param("user_id") userId: Long,
        @Param("target_user_id") targetUserId: Long,
    ): Boolean

    @Query(
        """
        MATCH (a:User {userId: ${ '$' }user_id})-[r:CONNECTED_WITH]-(b:User {userId: ${ '$' }target_user_id})
        DELETE r
        RETURN true
    """,
    )
    fun removeConnection(
        @Param("user_id") userId: Long,
        @Param("target_user_id") targetUserId: Long,
    ): Boolean
}
