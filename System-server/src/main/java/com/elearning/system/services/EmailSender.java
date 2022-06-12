package com.elearning.system.services;

import com.elearning.system.repositories.entities.enums.MessageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
@PropertySource("classpath:mail/application-mail-config.properties")
public class EmailSender {

    private final String USERNAME_TOKEN = "&\\{userName}";
    private final String QUIZNAME_TOKEN = "&\\{quizName}";
    private final String QUIZCATEGORY_TOKEN = "&\\{quizCategory}";
    private final String URL_TOKEN = "&\\{url}";
    private final String CONTENT_ENCODING = "text/html ; charset=utf-8";

    @Value("${login}")
    private String login;

    @Autowired
    private Session emailSession;

    public void sendMessage(String receiverEmailAddress, String username, String url, MessageInfo.MessageInfoItem messageInfoItem) {
        try {
            Message message = generateMessage(receiverEmailAddress);
            Map<String, String> replace = new HashMap<>();
            replace.put(USERNAME_TOKEN, username);
            replace.put(URL_TOKEN, url);
            setContent(message, messageInfoItem, replace);
            Transport.send(message);
        } catch (MessagingException e) {
            log.error("cant send message", e);
        } catch (IOException e) {
            log.error("cant read file with message", e);
        }
    }

    public void sendMessage(String receiverEmailAddress, String username, String url,
                            String quizName, String categoryName,
                            MessageInfo.MessageInfoItem messageInfoItem) {
        try {
            Map<String, String> replace = new HashMap<>();
            replace.put(USERNAME_TOKEN, username);
            replace.put(QUIZNAME_TOKEN, quizName);
            replace.put(QUIZCATEGORY_TOKEN, categoryName);
            replace.put(URL_TOKEN, url);

            Message message = generateMessage(receiverEmailAddress);
            setContent(message, messageInfoItem, replace);
            Transport.send(message);
        } catch (MessagingException e) {
            log.error("cant send message", e);
        } catch (IOException e) {
            log.error("cant read file with message", e);
        }
    }


    public void sendMessage(String receiverEmailAddress, String username, MessageInfo.MessageInfoItem messageInfoItem) {
        try {
            Map<String, String> replace = new HashMap<>();
            replace.put(USERNAME_TOKEN, username);
            Message message = generateMessage(receiverEmailAddress);
            setContent(message, messageInfoItem, replace);
            Transport.send(message);

        } catch (MessagingException e) {
            log.error("cant send message", e);
        } catch (IOException e) {
            log.error("cant read file with message", e);
        }
    }


    private Message generateMessage(String receiverEmailAddress) throws MessagingException {
        Message message = new MimeMessage(emailSession);
        message.setFrom(new InternetAddress(login));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(receiverEmailAddress)
        );
        return message;
    }

    private void setContent(Message message, MessageInfo.MessageInfoItem messageInfoItem,
                            Map<String, String> replace) throws MessagingException, IOException {
        message.setSubject(messageInfoItem.getSubject());
        String content = new String(Objects.requireNonNull(
                getClass().getClassLoader().getResourceAsStream(
                        messageInfoItem.getFilename())).readAllBytes());
        for (Map.Entry<String, String> entry : replace.entrySet()) {
            content = content.replaceFirst(entry.getKey(), entry.getValue());
        }
        message.setContent(content, CONTENT_ENCODING);
    }

}
