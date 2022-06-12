package com.elearning.system.repositories.dao.mappers;

import com.elearning.system.repositories.entities.Image;
import com.elearning.system.repositories.entities.Message;
import com.elearning.system.repositories.entities.User;
import com.elearning.system.repositories.entities.enums.Role;
import com.elearning.system.repositories.entities.enums.UserAccountStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = User.builder()
                .id(resultSet.getInt("author_id"))
                .firstName(resultSet.getString("first_name"))
                .secondName(resultSet.getString("second_name"))
                .login(resultSet.getString("login"))
                .mail(resultSet.getString("email"))
                .profile(resultSet.getString("profile"))
                .status(UserAccountStatus.valueOf(resultSet.getString("status").toUpperCase()))
                .role(Role.valueOf(resultSet.getString("role").toUpperCase()))
                .imageId(resultSet.getInt("image_id"))
                .image(new Image(resultSet.getInt("image_id"), resultSet.getString("src")))
                .build();
        return Message.builder()
                .id(resultSet.getInt("id"))
                .chatId(resultSet.getInt("chat_id"))
                .authorId(resultSet.getInt("author_id"))
                .messageText(resultSet.getString("message_text"))
                .creationDate(resultSet.getTimestamp("creation_date"))
                .author(user)
                .build();
    }
}
