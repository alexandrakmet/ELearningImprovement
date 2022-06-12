package com.elearning.system.repositories.dao.mappers;

import com.elearning.system.repositories.entities.Achievement;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AchievementMapper implements RowMapper<Achievement> {
    @Override
    public Achievement mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Achievement.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .build();
    }
}
