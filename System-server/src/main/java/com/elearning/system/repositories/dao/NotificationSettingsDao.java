package com.elearning.system.repositories.dao;

import com.elearning.system.repositories.entities.Notification;
import com.elearning.system.repositories.entities.NotificationSettings;

import java.util.List;

public interface NotificationSettingsDao extends GenericDao<NotificationSettings> {
    String TABLE_NAME = "notification_settings";

    NotificationSettings getSettingsByUserId(int userId);
}
