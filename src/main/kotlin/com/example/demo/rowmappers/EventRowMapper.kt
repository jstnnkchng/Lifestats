package com.example.demo.rowmappers

import com.example.demo.constants.VisibilityType
import com.example.demo.models.Event
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class EventRowMapper : RowMapper<Event> {
    override fun mapRow(
        rs: ResultSet,
        rowNum: Int,
    ): Event? {
        return Event(
            rs.getLong("event_id"),
            rs.getLong("user_id"),
            rs.getString("host_name"),
            rs.getString("event_name"),
            rs.getString("event_description"),
            rs.getString("location"),
            rs.getTimestamp("event_time").toLocalDateTime(),
            rs.getInt("current_participants"),
            rs.getInt("max_num_participants"),
            if (rs.getString("visibility") == null) null else VisibilityType.fromString(rs.getString("visibility")),
            rs.getTimestamp("created_at").toLocalDateTime(),
        )
    }
}
