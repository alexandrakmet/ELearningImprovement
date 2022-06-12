package com.elearning.system.controllers;

import com.elearning.system.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/pass-recovery")
public class PasswordRecoveryController {
    @Autowired
    private UserService userService;

    @PostMapping
    public boolean passwordRecovery(@RequestBody String email) {
        return userService.passwordRecovery(email);
    }

    @GetMapping("{token}")
    public boolean checkPasswordRecoveryTokenExistence(@PathVariable String token) {
        return userService.openPasswordRecoveryToken(token);
    }

    @PutMapping("{token}")
    public boolean editPassword(@PathVariable String token, @RequestBody String password) {
        return userService.editPassword(token, password);
    }
}
