package com.elearning.system.services;

import com.elearning.system.repositories.dao.NotificationDao;
import com.elearning.system.repositories.entities.NotificationType;
import com.elearning.system.repositories.entities.Notification;
import com.elearning.system.repositories.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NotificationService {
    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private UserService userService;

    Notification generateNotification(int authorId, int objectId, int userId, String gameId,
                                      NotificationType notificationType) {
        User notificationAuthor = userService.getUserDataById(authorId);

        Notification notification = Notification.builder()
                .isViewed(false)
                .author(notificationAuthor.getLogin())
                .authorLink("users/" + authorId)
                .userId(userId)
                .isMessage(false)
                .build();

        if (notificationType == NotificationType.CREATED_NEWS) {
            notification.setAction("CREATED_NEWS");
            notification.setActionLink("announcement/" + objectId);

        } else if (notificationType == NotificationType.CREATED_QUIZ) {
            notification.setAction("CREATED_QUIZ");
            notification.setActionLink("quiz/" + objectId);

        } else if (notificationType == NotificationType.GAME_INVITATION) {
            notification.setAction("GAME_INVITATION");
            notification.setActionLink("game/" + gameId + "/play");

        } else if (notificationType == NotificationType.FRIEND_INVITATION) {
            notification.setAction("FRIEND_INVITATION");
            notification.setActionLink("users/" + authorId);
            notification.setUserId(objectId);

        } else if (notificationType == NotificationType.MESSAGE) {
            notification.setAction("MESSAGE");
            notification.setActionLink("chat/" + objectId);
            notification.setMessage(true);

        } else {
            return null;
        }
        notification.setId(createNotification(notification));
        return notification;
    }

    private int createNotification(Notification notification) {
        int notificationId = notificationDao.save(notification);
        if (notificationId == -1) {
            log.info("createNotification: Notification wasn't saved");
            return -1;
        }
        return notificationId;
    }

    @Scheduled(cron = "* * * 14 * *")
    public void deleteOldNotifications() {
        notificationDao.deleteOldNotifications();
    }

    public void updateNotification(Notification notification) {
        if (notification == null) {
            log.info("updateNotification: Notification is null");
            return;
        }
        notificationDao.update(notification);
    }

    public void deleteNotificationById(int id) {
        notificationDao.deleteById(id);
    }

    public Notification getNotificationById(int id) {
        return notificationDao.get(id);
    }

    public List<Notification> getNotificationsByUserId(int userId) {
        return notificationDao.getByUserId(userId);
    }

    public List<Notification> getMessagesByUserId(int userId) {
        return notificationDao.getMessagesByUserId(userId);
    }

    public void deleteAllUserNotifications(int userId) {
        notificationDao.deleteAllByUserId(userId);
    }
}
