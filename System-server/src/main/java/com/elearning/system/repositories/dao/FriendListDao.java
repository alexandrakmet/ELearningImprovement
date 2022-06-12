package com.elearning.system.repositories.dao;

import com.elearning.system.repositories.entities.NotificationType;

import java.util.List;

public interface FriendListDao {

    List<Integer> getForNotification(int id, NotificationType notificationType);

    boolean isSendNotification(int userId, NotificationType notificationType);

}
