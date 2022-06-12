package com.elearning.system.repositories.entities;

import com.elearning.system.repositories.entities.enums.Lang;
import com.elearning.system.repositories.entities.enums.Role;
import com.elearning.system.repositories.entities.enums.UserAccountStatus;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class User {

    private int id;

    @NotEmpty(message = "Please, provide a name")
    @Pattern(regexp = "^[a-zA-Zа-яА-Яієї']{2,25}$", message = "Name should be at least 2 characters long and have no numbers")
    private String firstName;

    @NotEmpty(message = "Please, provide a surname")
    @Pattern(regexp = "^[a-zA-Zа-яА-Яієї']{2,25}$", message = "Surname should be at least 2 characters long and have no numbers")
    private String secondName;

    @NotEmpty(message = "Please, provide a login")
    private String login;

    @NotEmpty(message = "Please, provide an email")
    @Pattern(regexp = "^[A-z\\d._]+@[A-z]+\\.[A-z]+$", message = "Email does't match the pattern: example@gmail.com")
    private String mail;

    @NotEmpty(message = "Please, provide a password")
    @Pattern(regexp = "^.{8,}$", message = "Password should be at least 8 symbols")
    private String password;

    private String profile;

    private Date registrationDate;

    private int score;

    private UserAccountStatus status;

    private Role role;

    private List<Achievement> achievements;

    private int imageId;

    private Image image;

    private Lang language;

}
