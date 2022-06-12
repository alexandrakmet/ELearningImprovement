package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.MessageDao;
import com.elearning.system.repositories.dao.mappers.MessageMapper;
import com.elearning.system.repositories.entities.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@PropertySource("classpath:system.properties")
public class MessageDaoImpl extends GenericDaoImpl<Message> implements MessageDao {

    @Value("#{${sql.message}}")
    private Map<String, String> messageQueries;

    protected MessageDaoImpl() {
        super(new MessageMapper(), TABLE_NAME);
    }

    @Override
    public List<Message> getMessagesFromChat(int chatId) {
        return jdbcTemplate.query(messageQueries.get("getMessagesFromChat"), new Object[]{chatId}, new MessageMapper());
    }

    @Override
    public Page<Message> getMessagesFromChat(int chatId, Pageable pageable) {
        Number total = jdbcTemplate.queryForObject(messageQueries.get("rowCount"),
                new Object[]{chatId},
                (resultSet, number) -> resultSet.getInt("row_count"));

        List<Message> messages = jdbcTemplate.query(
                messageQueries.get("getMessagesFromChat").replace(";", " LIMIT ? OFFSET ?;"),
                new Object[]{chatId, pageable.getPageSize(), pageable.getOffset()},
                new MessageMapper());
        return new PageImpl<>(messages, pageable, total != null ? total.intValue() : 0);
    }

    @Override
    protected String getInsertQuery() {
        return messageQueries.get("insert");
    }

    @Override
    protected PreparedStatement getInsertPreparedStatement(PreparedStatement preparedStatement,
                                                           Message message) throws SQLException {
        preparedStatement.setInt(1, message.getChatId());
        preparedStatement.setInt(2, message.getAuthorId());
        preparedStatement.setString(3, message.getMessageText());
        return preparedStatement;
    }

    @Override
    protected String getUpdateQuery() {
        return messageQueries.get("update");
    }

    @Override
    protected Object[] getUpdateParameters(Message message) {
        return new Object[]{message.getChatId(), message.getAuthorId(), message.getMessageText()};
    }

    @Override
    public Message get(int id) {
        Message message;
        try {
            message = jdbcTemplate.queryForObject(messageQueries.get("getById"),
                    new Object[]{id}, new MessageMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return message;
    }

}
