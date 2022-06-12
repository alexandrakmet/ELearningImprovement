package com.elearning.system.repositories.dao.mappers;

import com.elearning.system.repositories.entities.UserAchievement;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAchievementMapper implements RowMapper<UserAchievement> {
    @Override
    public UserAchievement mapRow(ResultSet resultSet, int i) throws SQLException {
        return UserAchievement.builder()
                .userId(resultSet.getInt("user_id"))
                .achievementId(resultSet.getInt("achievement_id"))
                .id(resultSet.getInt("id"))
                .build();
    }
}
