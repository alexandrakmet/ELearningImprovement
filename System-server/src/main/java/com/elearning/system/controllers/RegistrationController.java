package com.elearning.system.controllers;

import com.elearning.system.services.UserService;
import com.elearning.system.repositories.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/registration")
public class RegistrationController {
    @Autowired
    private UserService userService;

    @PostMapping
    public boolean registerUser(@RequestBody @Valid User user) {
        return userService.registerUser(user);
    }

    @GetMapping("{token}")
    public boolean checkRegistrationTokenExistence(@PathVariable String token) {
        return userService.openRegistrationToken(token);
    }
}
