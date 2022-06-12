package com.elearning.system.repositories.entities;

import com.elearning.system.repositories.entities.enums.TokenType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Token {

    private String token;
    private TokenType tokenType;
    private Date expiredDate;
    private int userId;
}
