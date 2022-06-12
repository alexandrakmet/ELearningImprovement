package com.elearning.system.repositories.dao.mappers;

import com.elearning.system.repositories.entities.Notification;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationMapper implements RowMapper<Notification> {
    @Override
    public Notification mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Notification.builder()
                .id(resultSet.getInt("id"))
                .userId(resultSet.getInt("user_id"))
                .isViewed(resultSet.getBoolean("is_viewed"))
                .author(resultSet.getString("author"))
                .action(resultSet.getString("action"))
                .authorLink(resultSet.getString("author_link"))
                .actionLink(resultSet.getString("action_link"))
                .isMessage(resultSet.getBoolean("is_message"))
                .build();
    }
}
