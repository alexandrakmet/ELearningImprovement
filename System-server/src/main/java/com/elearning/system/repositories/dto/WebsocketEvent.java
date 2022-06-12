package com.elearning.system.repositories.dto;

import com.elearning.system.repositories.dto.game.Users;
import com.elearning.system.repositories.entities.Message;
import com.elearning.system.repositories.entities.Notification;
import com.elearning.system.repositories.entities.Question;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WebsocketEvent {
    private EventType type;
    private List<String> players;
    private Question question;
    private Users gameResults;
    private Notification notification;
    private Message message;
    private int currQuestion;

    public enum EventType {
        RESULTS,
        QUESTION,
        PLAYERS,
        MESSAGE,
        NOTIFICATION
    }

}
