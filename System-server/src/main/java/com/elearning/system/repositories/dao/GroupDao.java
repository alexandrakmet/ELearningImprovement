package com.elearning.system.repositories.dao;

import com.elearning.system.repositories.entities.Group;
import com.elearning.system.repositories.entities.User;

import java.util.List;

public interface GroupDao extends GenericDao<Group> {
    String TABLE_NAME = "\"group\"";

    List<Group> getUserGroups(int userId);

    void addGroupMember(int groupId, int userId);

    List<User> getGroupMembers(int groupId);

    Group getByCode(String code);
}
