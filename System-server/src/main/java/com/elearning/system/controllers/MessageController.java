package com.elearning.system.controllers;

import com.google.gson.Gson;
import com.elearning.system.repositories.dto.WebsocketEvent;
import com.elearning.system.repositories.entities.Message;
import com.elearning.system.repositories.entities.NotificationType;
import com.elearning.system.services.MessageService;
import com.elearning.system.services.WebSocketSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class MessageController {
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private MessageService messageService;

    @Autowired
    private WebSocketSenderService webSocketSenderService;

    @MessageMapping("/{chatId}")
    public void receiveMessage(@DestinationVariable String chatId, String message) {
        Gson gson = new Gson();
        Message msg = gson.fromJson(message, Message.class);

        Message sm = messageService.saveMessage(msg);

        if (sm != null) {
            this.template.convertAndSend(String.format("/chat/%s", chatId),
                    gson.toJson(WebsocketEvent.builder().type(WebsocketEvent.EventType.MESSAGE).message(sm).build()));
            webSocketSenderService.sendNotification(sm.getAuthorId(), Integer.parseInt(chatId), null, NotificationType.MESSAGE);
        }
    }

    @GetMapping("api/v1/chat/{chatId}/messages")
    public List<Message> getChatMessages(@PathVariable int chatId,
                                         @RequestParam(value = "pageNumber") int pageNumber) {
        return messageService.getPageMessages(chatId, Optional.of(pageNumber), Optional.of(10)).toList();
    }
}
