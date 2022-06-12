package com.elearning.system.services;

import com.elearning.system.repositories.dao.NotificationSettingsDao;
import com.elearning.system.repositories.entities.NotificationSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class NotificationSettingsService {
    @Autowired
    private NotificationSettingsDao notificationSettingsDao;

    boolean createNotificationSettings(NotificationSettings notificationSettings) {
        int notificationId = notificationSettingsDao.save(notificationSettings);
        if (notificationId == -1) {
            log.info("createNotificationSettings: Notification Settings wasn't saved");
            return false;
        }
        return true;
    }

    public void updateNotificationSettings(NotificationSettings notificationSettings) {
        if (notificationSettings == null) {
            log.info("updateNotificationSettings: Notification Settings is null");
            return;
        }
        notificationSettingsDao.update(notificationSettings);
    }

    public NotificationSettings getSettingsByUserId(int userId) {
        return notificationSettingsDao.getSettingsByUserId(userId);
    }
}
