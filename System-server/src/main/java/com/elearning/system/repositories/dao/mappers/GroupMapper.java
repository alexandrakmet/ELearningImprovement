package com.elearning.system.repositories.dao.mappers;

import com.elearning.system.repositories.entities.Group;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMapper implements RowMapper<Group> {
    @Override
    public Group mapRow(ResultSet resultSet, int i) throws SQLException {
        return Group.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .authorId(resultSet.getInt("author_id"))
                .invitationCode(resultSet.getString("invitation_code"))
                .creationDate(resultSet.getTimestamp("creation_date"))
                .build();
    }
}