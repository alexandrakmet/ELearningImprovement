package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.mappers.NotificationMapper;
import com.elearning.system.repositories.dao.NotificationSettingsDao;
import com.elearning.system.repositories.dao.mappers.NotificationSettingsMapper;
import com.elearning.system.repositories.entities.Notification;
import com.elearning.system.repositories.entities.NotificationSettings;
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
@PropertySource("classpath:notifications.properties")
public class NotificationSettingsDaoImpl extends GenericDaoImpl<NotificationSettings> implements NotificationSettingsDao {

    @Value("#{${sql.notificationSettings}}")
    private Map<String, String> notificationSettingsQueries;

    protected NotificationSettingsDaoImpl() {
        super(new NotificationSettingsMapper(), TABLE_NAME);
    }


    @Override
    protected String getInsertQuery() {
        return notificationSettingsQueries.get("insert");
    }

    @Override
    protected PreparedStatement getInsertPreparedStatement(PreparedStatement preparedStatement, NotificationSettings notificationSettings) throws SQLException {

        preparedStatement.setBoolean(1, notificationSettings.isNewQuiz());
        preparedStatement.setBoolean(2, notificationSettings.isNewAnnouncement());
        preparedStatement.setBoolean(3, notificationSettings.isGameInvitation());
        preparedStatement.setBoolean(4, notificationSettings.isFriendInvitation());
        preparedStatement.setInt(5, notificationSettings.getUserId());

        return preparedStatement;
    }

    @Override
    protected String getUpdateQuery() {
        return notificationSettingsQueries.get("update");
    }

    @Override
    protected Object[] getUpdateParameters(NotificationSettings notificationSettings) {
        return new Object[]{notificationSettings.isNewQuiz(),
                notificationSettings.isNewAnnouncement(),
                notificationSettings.isGameInvitation(),
                notificationSettings.isFriendInvitation(),
                notificationSettings.getUserId(),
                notificationSettings.getId()};
    }
    @Override
    public NotificationSettings getSettingsByUserId(int userId) {
        return jdbcTemplate.queryForObject(notificationSettingsQueries.get("getSettingsByUserId"),
                new Object[]{userId}, new NotificationSettingsMapper());
    }
}
