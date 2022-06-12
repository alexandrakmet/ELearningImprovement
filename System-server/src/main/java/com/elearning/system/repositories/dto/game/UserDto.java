package com.elearning.system.repositories.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    private int id;
    private String gameId;
    private String login;
    private int registerId;
    private int score;
    private int percent;
    private int comboAnswer;
    private int quizId;

}
