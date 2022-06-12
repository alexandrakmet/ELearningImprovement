package com.elearning.system.repositories.dto.game;

import com.elearning.system.repositories.entities.Question;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class GameQuestionDto {
    private int id;
    private String gameId;
    private GameDto game;
    private int questionId;
    private Question question;
    private boolean isCurrent;
    private Timestamp finishTime;
}
