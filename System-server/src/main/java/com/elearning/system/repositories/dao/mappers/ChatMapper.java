package com.elearning.system.repositories.dao.mappers;

import com.elearning.system.repositories.entities.Chat;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatMapper implements RowMapper<Chat> {
    @Override
    public Chat mapRow(ResultSet resultSet, int i) throws SQLException {
        return Chat.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .creationDate(resultSet.getTimestamp("creation_date"))
                .build();
    }
}
