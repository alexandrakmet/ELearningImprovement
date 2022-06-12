package com.elearning.system.controllers;

import com.elearning.system.services.UserService;
import com.elearning.system.repositories.entities.User;
import com.elearning.system.repositories.entities.enums.Lang;
import com.elearning.system.repositories.entities.enums.Role;
import com.elearning.system.repositories.entities.enums.UserAccountStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("{id}")
    public User getUser(@PathVariable int id) {
        return userService.getUserDataById(id);
    }

    @PutMapping("{id}/setLang")
    public void updateLang(@PathVariable int id, @RequestBody Lang lang) {
        userService.updateUserLanguage(id, lang);
    }

    @GetMapping("{id}/getLang")
    public Lang getLang(@PathVariable int id) {
        return userService.getUserLanguage(id);
    }

    @PutMapping
    public void updateUser(@RequestBody @Valid User editedUser) {
        userService.updateUserProfile(editedUser);
    }

    @PutMapping("/password/{login}")
    public void changePassword(@PathVariable String login,
                               @RequestBody String newPassword) {
        userService.changeUserPassword(login, newPassword);
    }

    @GetMapping("/password/check")
    public boolean checkPasswords(@RequestParam(value = "login") String login,
                                  @RequestParam(value = "password") String password) {
        return userService.checkPasswords(login, password);
    }

    @PutMapping("/photo")
    public void updateUserPhoto(@RequestBody User editedUser) {
        userService.updateUserImage(editedUser);
    }

    @GetMapping
    public List<User> getUsers(@RequestParam(value = "pageNumber") int pageNumber,
                               @RequestParam(value = "allUsers") boolean allUsers,
                               @RequestParam(value = "filter", defaultValue = "") String filter) {
        return allUsers ? userService.searchUsersByLogin(filter, Optional.of(pageNumber), Optional.of(10)).toList() :
                userService.searchUsersByLogin(filter, Role.USER, Optional.of(pageNumber), Optional.of(10)).toList();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        return userService.getUserFriends(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/checkFriend/{friendId}")
    public boolean checkFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.checkUsersFriendship(id, friendId);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/addFriend")
    public boolean addFriend(@PathVariable int id,
                             @RequestBody int friendId) {
        return userService.addUserFriend(id, friendId);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/removeFriend")
    public boolean removeFriend(@PathVariable int id,
                                @RequestBody int friendId) {
        return userService.deleteUserFriend(id, friendId);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @PutMapping("/{id}/status/change")
    public void changeUserStatus(@PathVariable int id, @RequestBody String newStatus) {
        userService.updateUserStatus(id, UserAccountStatus.valueOf(newStatus));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @PostMapping("/create")
    public boolean createUser(@RequestBody @Valid User user) {
        return userService.createUser(user);
    }
}
