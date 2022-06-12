package com.elearning.system.controllers;

import com.elearning.system.repositories.entities.Notification;
import com.elearning.system.repositories.entities.NotificationSettings;
import com.elearning.system.services.NotificationService;
import com.elearning.system.services.NotificationSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationSettingsService settingsService;

    @GetMapping("/notifications/{userId}")
    public Notification[] getNotifications(@PathVariable int userId) {
        return notificationService.getNotificationsByUserId(userId).toArray(Notification[]::new);
    }

    @GetMapping("/notifications/messages/{userId}")
    public Notification[] getMessages(@PathVariable int userId) {
        return notificationService.getMessagesByUserId(userId).toArray(Notification[]::new);
    }

    @PutMapping("/notification")
    public void updateNotificationViewed(@RequestBody Notification notification) {
        notificationService.updateNotification(notification);
    }

    @DeleteMapping("/notification/user/{userId}")
    public void deleteAllUserNotification(@PathVariable int userId) {
        notificationService.deleteAllUserNotifications(userId);
    }

    @DeleteMapping("/notification/{id}")
    public void deleteNotificationById(@PathVariable int id) {
        notificationService.deleteNotificationById(id);
    }

    @GetMapping("/notification/settings/{userId}")
    public NotificationSettings getSettingsByUserId(@PathVariable int userId) {
        return settingsService.getSettingsByUserId(userId);
    }

    @PutMapping("/notification/settings")
    public void updateSettings(@RequestBody NotificationSettings notificationSettings) {
        settingsService.updateNotificationSettings(notificationSettings);
    }
}
