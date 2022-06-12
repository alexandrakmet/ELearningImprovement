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

//FROM "group" g LEFT JOIN group_members cm ON g.id = cm.group_id LEFT JOIN users u ON cm.user_id = u.id left join image i on u.image_id = i.id \
//  WHERE g.id = ?;

}
