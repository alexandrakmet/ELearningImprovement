package com.elearning.system.repositories.entities;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Message {
    private int id;
    private int chatId;
    private int authorId;
    private User author;
    private Timestamp creationDate;
    private String messageText;
}
