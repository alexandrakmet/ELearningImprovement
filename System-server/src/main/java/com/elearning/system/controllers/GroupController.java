package com.elearning.system.controllers;

import com.elearning.system.repositories.entities.Group;
import com.elearning.system.repositories.entities.User;
import com.elearning.system.services.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class GroupController {
    @Autowired
    private GroupService groupService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("api/v1/group/{groupId}")
    public Group getGroup(@PathVariable int groupId) {
        return groupService.getById(groupId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("api/v1/users/{userId}/groups")
    public List<Group> getUserGroups(@PathVariable int userId) {
        return groupService.getAllGroupsForUser(userId);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("api/v1/users/{id}/group/{groupId}/invite")
    public void addMemberToGroup(@PathVariable int id, @PathVariable String groupId) {
        groupService.joinGroup(groupId, id);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("api/v1/users/{id}/createGroup")
    public int createGroup(@PathVariable int id, @RequestBody Group group) {
        return groupService.createGroup(group, id);
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("api/v1/group/{groupId}/members")
    public List<User> getGroupMembers(@PathVariable int groupId) {
        return groupService.getGroupMembers(groupId);
    }

}
