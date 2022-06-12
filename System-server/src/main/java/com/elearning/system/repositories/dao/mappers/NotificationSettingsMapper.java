package com.elearning.system.repositories.dao.mappers;

import com.elearning.system.repositories.entities.NotificationSettings;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationSettingsMapper implements RowMapper<NotificationSettings> {

    @Override
    public NotificationSettings mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return NotificationSettings.builder()
                .id(resultSet.getInt("id"))
                .newQuiz(resultSet.getBoolean("new_quiz"))
                .newAnnouncement(resultSet.getBoolean("new_announcement"))
                .gameInvitation(resultSet.getBoolean("game_invitation"))
                .friendInvitation(resultSet.getBoolean("friend_invitation"))
                .userId(resultSet.getInt("user_id"))
                .build();
    }
}