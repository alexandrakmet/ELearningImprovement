package com.elearning.system.repositories.dao;

import com.elearning.system.repositories.entities.Notification;

import java.util.List;

public interface NotificationDao extends GenericDao<Notification> {
    String TABLE_NAME = "notification";

    List<Notification> getByUserId(int userId);

    List<Notification> getMessagesByUserId(int userId);

    void deleteOldNotifications();

    void deleteAllByUserId(int id);

}
