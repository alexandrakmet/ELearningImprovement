package com.elearning.system.services;

import com.elearning.system.repositories.dao.implementation.FriendListDaoImpl;
import com.elearning.system.repositories.dto.WebsocketEvent;
import com.elearning.system.repositories.dto.game.UserDto;
import com.elearning.system.repositories.dto.game.Users;
import com.elearning.system.repositories.entities.NotificationType;
import com.google.gson.Gson;
import com.elearning.system.repositories.entities.Notification;
import com.elearning.system.repositories.entities.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WebSocketSenderService {

    private final Gson gson = new Gson();
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private FriendListDaoImpl friendListDao;


    void sendResults(String gameId, Users users, int currQuestion) {
        log.info("send results " + users);
        this.template.convertAndSend(String.format("/game/%s/play", gameId),
                gson.toJson(WebsocketEvent.builder()
                        .type(WebsocketEvent.EventType.RESULTS)
                        .gameResults(users)
                        .currQuestion(currQuestion)
                        .build()));
    }

    void sendQuestion(Question question, String gameId, int currQuestion) {
        log.info("send question");
        Gson g = new Gson();
        this.template.convertAndSend(String.format("/game/%s/play", gameId),
                g.toJson(WebsocketEvent.builder()
                        .type(WebsocketEvent.EventType.QUESTION)
                        .question(question)
                        .currQuestion(currQuestion)
                        .build()));
    }

    void sendUsers(String gameId, List<UserDto> users) {
        List<String> players = new ArrayList<>();
        for (UserDto user : users) {
            players.add(user.getLogin());
        }
        this.template.convertAndSend(String.format("/game/%s/play", gameId),
                gson.toJson(WebsocketEvent.builder().type(WebsocketEvent.EventType.PLAYERS).players(players).build()));
    }

    public void sendNotification(int authorId, int objectId, String gameId, NotificationType notificationType) {

        if (notificationType == NotificationType.CREATED_NEWS | notificationType == NotificationType.CREATED_QUIZ
                | notificationType == NotificationType.MESSAGE) {
            List<Integer> friendsId;
            if (notificationType == NotificationType.MESSAGE) {
                friendsId = friendListDao.getForNotification(objectId, notificationType);
            } else {
                friendsId = friendListDao.getForNotification(authorId, notificationType);
            }
            for (int friendId : friendsId) {
                Notification notification = notificationService.generateNotification(authorId, objectId, friendId,
                        null, notificationType);
                this.template.convertAndSend("/notification" + friendId,
                        gson.toJson(WebsocketEvent.builder().type(WebsocketEvent.EventType.NOTIFICATION)
                                .notification(notification).build()));
            }
        }

        if (notificationType == NotificationType.FRIEND_INVITATION) {
            Notification notification = notificationService.generateNotification(authorId, objectId, objectId,
                    null, notificationType);
            if (friendListDao.isSendNotification(objectId, notificationType)) {
                this.template.convertAndSend("/notification" + objectId,
                        gson.toJson(WebsocketEvent.builder().type(WebsocketEvent.EventType.NOTIFICATION)
                                .notification(notification).build()));
            }
        }

        if (notificationType == NotificationType.GAME_INVITATION) {
            Notification notification = notificationService.generateNotification(authorId, objectId, objectId,
                    gameId, notificationType);
            if (friendListDao.isSendNotification(authorId, notificationType)) {
                this.template.convertAndSend("/notification" + objectId,
                        gson.toJson(WebsocketEvent.builder().type(WebsocketEvent.EventType.NOTIFICATION)
                                .notification(notification).build()));
            }
        }
    }
}
