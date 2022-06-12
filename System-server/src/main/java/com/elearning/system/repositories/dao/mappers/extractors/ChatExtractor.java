package com.elearning.system.repositories.dao.mappers.extractors;

import com.elearning.system.repositories.entities.Chat;
import com.elearning.system.repositories.entities.Image;
import com.elearning.system.repositories.entities.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ChatExtractor implements ResultSetExtractor<List<Chat>> {
    @Override
    public List<Chat> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Chat> chatMap = new HashMap<>();
        Map<Integer, User> userMap = new HashMap<>();

        while (rs.next()) {
            int chatId = rs.getInt("chat_id");
            Chat chat = chatMap.get(chatId);
            if (chat == null) {
                userMap = new HashMap<>();
                chat = Chat.builder()
                        .id(chatId)
                        .name(rs.getString("name"))
                        .creationDate(rs.getTimestamp("creation_date"))
                        .build();
                chatMap.put(chatId, chat);
            }

            List<User> users = chat.getUsers();
            if (users == null) {
                users = new ArrayList<>();
                chat.setUsers(users);
            }
            int userId = rs.getInt("user_id");
            if (userId != 0) {
                User user = userMap.get(userId);
                if (user == null) {
                    user = User.builder()
                            .id(userId)
                            .login(rs.getString("login"))
                            .firstName(rs.getString("first_name"))
                            .secondName(rs.getString("second_name"))
                            .mail(rs.getString("email"))
                            .imageId(rs.getInt("image_id"))
                            .image(new Image(rs.getInt("image_id"), rs.getString("src")))
                            .build();

                    userMap.put(userId, user);
                }
                users.add(user);
            }
        }
        return new ArrayList<>(chatMap.values());
    }
}
