package com.elearning.system.services;

import com.elearning.system.repositories.dao.GroupDao;
import com.elearning.system.repositories.entities.Group;
import com.elearning.system.repositories.entities.Chat;
import com.elearning.system.repositories.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class GroupService {
    @Autowired
    private GroupDao groupDao;

    @Autowired
    private ChatService chatService;

    public List<Group> getAllGroupsForUser(int userId) {
        if (userId == 0) {
            throw new IllegalArgumentException("0 can`t be user`s identifier");
        }
        return groupDao.getUserGroups(userId);
    }

    public Group getById(int userId) {
        if (userId == 0) {
            throw new IllegalArgumentException("0 can`t be user`s identifier");
        }
        return groupDao.get(userId);
    }

    public void addMemberToGroup(int groupId, int userId) {
        if (groupId == 0) {
            throw new IllegalArgumentException("0 can`t be group`s identifier");
        }
        if (userId == 0) {
            throw new IllegalArgumentException("0 can`t be user`s identifier");
        }
        //groupDao.addGroupMember(groupId, userId);
        chatService.addMemberToChat(chatService.getGroupChat(groupDao.get(groupId).getName()).getId(), userId);
    }

    public int createGroup(Group group, int userId) {
        group.setInvitationCode(UUID.randomUUID().toString().replace("-", ""));
        int groupId = groupDao.save(group);
        if (groupId != -1) {
            groupDao.addGroupMember(groupId, userId);
        }
        chatService.createChat(Chat.builder().name(group.getName()).build(), userId);
        return groupId;
    }

    public List<User> getGroupMembers(int groupId){
        return groupDao.getGroupMembers(groupId);
    }

    public void joinGroup(String code, int userId){
        Group group = groupDao.getByCode(code);
        groupDao.addGroupMember(group.getId(), userId);
        chatService.addMemberToChat(chatService.getGroupChat(group.getName()).getId(), userId);
    }
}
