package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.ChatDao;
import com.elearning.system.repositories.dao.mappers.ChatMapper;
import com.elearning.system.repositories.dao.mappers.extractors.ChatExtractor;
import com.elearning.system.repositories.entities.Chat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@PropertySource("classpath:chat.properties")
public class ChatDaoImpl extends GenericDaoImpl<Chat> implements ChatDao {
    @Value("#{${sql.chat}}")
    private Map<String, String> chatQueries;

    protected ChatDaoImpl() {
        super(new ChatMapper(), TABLE_NAME);
    }

    @Override
    protected String getInsertQuery() {
        return chatQueries.get("insert");
    }

    @Override
    protected PreparedStatement getInsertPreparedStatement(PreparedStatement preparedStatement,
                                                           Chat chat) throws SQLException {
        preparedStatement.setString(1, chat.getName());
        return preparedStatement;
    }

    @Override
    protected String getUpdateQuery() {
        return chatQueries.get("update");
    }

    @Override
    protected Object[] getUpdateParameters(Chat chat) {
        return new Object[]{chat.getName(), chat.getId()};
    }

    @Override
    public List<Chat> getAllFullInfo() {
        return jdbcTemplate.query(chatQueries.get("getFullInfo"),
                new ChatExtractor());
    }

    @Override
    public List<Chat> getAllFullInfoForUser(int id) {
        return jdbcTemplate.query(chatQueries.get("getFullInfoForUser"),
                new Object[]{id},
                new ChatExtractor());
    }

    @Override
    public Chat getFullInfo(int id) {
        String getQuery = chatQueries.get("getFullInfo").replace(";", " WHERE chat_id = ?;");
        List<Chat> result = jdbcTemplate.query(getQuery,
                new Object[]{id},
                new ChatExtractor());
        if (result != null) {
            return result.size() == 0 ? null : result.get(0);
        } else
            return null;
    }

    @Override
    public Chat getGroupChat(String name) {
        List<Chat> result = jdbcTemplate.query(chatQueries.get("getGroupChat"),
                new Object[]{name},
                new ChatMapper());
        return result.size() == 0 ? null : result.get(0);
    }

    @Override
    public List<Chat> getChatsForUser(int userId) {
        return jdbcTemplate.query(chatQueries.get("getUsersChat"),
                new Object[]{userId},
                new ChatMapper());
    }

    @Override
    public void addChatMember(int chatId, int userId) {
        try {
            jdbcTemplate.update(chatQueries.get("addChatMember"), chatId, userId);
        } catch (DuplicateKeyException e) {
            log.info("User " + userId + " is already a member of chat " + chatId);
        }
    }

    @Override
    public void removeChatMember(int chatId, int userId) {
        jdbcTemplate.update(chatQueries.get("removeChatMember"), chatId, userId);
    }

    @Override
    public boolean checkChatAffiliation(int chatId, int userId) {
        Number total = jdbcTemplate.queryForObject(chatQueries.get("checkChatAffiliation"),
                new Object[]{chatId, userId},
                (resultSet, number) -> resultSet.getInt("row_count"));

        return total != null && total.intValue() > 0;
    }
}
