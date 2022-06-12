package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.mappers.NotificationMapper;
import com.elearning.system.repositories.dao.NotificationDao;
import com.elearning.system.repositories.entities.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Slf4j
@Repository
@PropertySource("classpath:systemz.properties")
public class NotificationDaoImpl extends GenericDaoImpl<Notification> implements NotificationDao {

    @Value("#{${sql.notification}}")
    private Map<String, String> notificationQueries;

    protected NotificationDaoImpl() {
        super(new NotificationMapper(), TABLE_NAME);
    }

    @Override
    protected String getInsertQuery() {
        return notificationQueries.get("insert");
    }

    @Override
    protected PreparedStatement getInsertPreparedStatement(PreparedStatement preparedStatement, Notification notification) throws SQLException {
        preparedStatement.setInt(1, notification.getUserId());
        preparedStatement.setBoolean(2, notification.isViewed());
        preparedStatement.setString(3, notification.getAuthor());
        preparedStatement.setString(4, notification.getAction());
        preparedStatement.setString(5, notification.getAuthorLink());
        preparedStatement.setString(6, notification.getActionLink());
        preparedStatement.setBoolean(7, notification.isMessage());
        return preparedStatement;
    }

    @Override
    protected String getUpdateQuery() {
        return notificationQueries.get("update");
    }

    @Override
    protected Object[] getUpdateParameters(Notification notification) {
        return new Object[]{notification.getUserId(), notification.isViewed(), notification.getAuthor(),
                notification.getAction(), notification.getAuthorLink(), notification.getActionLink(), notification.isMessage(), notification.getId()};
    }

    @Override
    public List<Notification> getByUserId(int id) {
        return jdbcTemplate.query(notificationQueries.get("getByUserId"),
                new Object[]{id}, new NotificationMapper());
    }

    @Override
    public List<Notification> getMessagesByUserId(int id) {
        return jdbcTemplate.query(notificationQueries.get("getMessages"),
                new Object[]{id}, new NotificationMapper());
    }

    @Override
    public void deleteOldNotifications() {
        jdbcTemplate.update(notificationQueries.get("deleteOldNotifications"));
    }

    @Override
    public void deleteAllByUserId(int userId) {
        jdbcTemplate.update(notificationQueries.get("deleteAllByUserId"), userId);
    }
}
