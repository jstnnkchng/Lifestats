package com.example.demo.rowmappers

import com.example.demo.models.UserDetails
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class UserDetailsRowMapper : RowMapper<UserDetails> {
    override fun mapRow(
        rs: ResultSet,
        rowNum: Int,
    ): UserDetails? {
        return UserDetails(
            rs.getInt("user_id"),
            rs.getString("username"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("bio"),
            rs.getTimestamp("join_date").toLocalDateTime(),
        )
    }
}
