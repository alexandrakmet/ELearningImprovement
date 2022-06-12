package com.elearning.system.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NotificationSettings {
    private int id;
    private boolean newQuiz;
    private boolean newAnnouncement;
    private boolean gameInvitation;
    private boolean friendInvitation;
    private int userId;
}
