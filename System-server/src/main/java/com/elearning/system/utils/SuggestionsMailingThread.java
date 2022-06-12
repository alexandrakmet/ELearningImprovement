package com.elearning.system.utils;

import com.elearning.system.repositories.entities.enums.MessageInfo;
import com.elearning.system.services.EmailSender;
import lombok.Builder;

@Builder
public class SuggestionsMailingThread implements Runnable {
    private EmailSender emailSender;
    private String login;
    private String email;
    private String URL;
    private String quizName;
    private String categoryName;
    private String quizId;
    private MessageInfo.MessageInfoItem messageInfoItem;
    private final String QUIZ = "quiz/";


    @Override
    public void run() {
        emailSender.sendMessage(email, login, URL + QUIZ + quizId, quizName, categoryName, messageInfoItem);
    }

}
