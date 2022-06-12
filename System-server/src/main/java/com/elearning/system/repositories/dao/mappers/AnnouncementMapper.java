package com.elearning.system.repositories.dao.mappers;

import com.elearning.system.repositories.entities.Announcement;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnnouncementMapper implements RowMapper<Announcement> {
    @Override
    public Announcement mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Announcement.builder()
                .id(resultSet.getInt("id"))
                .authorLogin(resultSet.getString("author_login"))
                .isPublished(resultSet.getBoolean("is_published"))
                .title(resultSet.getString("title"))
                .subtitle(resultSet.getString("subtitle"))
                .fullText(resultSet.getString("full_text"))
                .createdDate(resultSet.getTimestamp("created_date"))
                .imageId(resultSet.getInt("image_id"))
                .build();
    }
}
