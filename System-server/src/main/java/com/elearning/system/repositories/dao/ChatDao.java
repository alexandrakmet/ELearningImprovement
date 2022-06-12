package com.elearning.system.repositories.dao;


import com.elearning.system.repositories.entities.Chat;

import java.util.List;

public interface ChatDao extends GenericDao<Chat> {
    String TABLE_NAME = "chat";

    List<Chat> getAllFullInfo();

    List<Chat> getAllFullInfoForUser(int id);

    Chat getFullInfo(int id);

    Chat getGroupChat(String name);

    List<Chat> getChatsForUser(int userId);

    void addChatMember(int chatId, int userId);

    void removeChatMember(int chatId, int userId);

    boolean checkChatAffiliation(int chatId, int userId);

}
