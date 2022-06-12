package com.elearning.system.repositories.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Game {
    private String gameId;
    private int quizId;
    private int time;
    private boolean questionAnswerSequence;
    private boolean quickAnswerBonus;
    private boolean combo;
    private boolean intermediateResult;
    private int hostId;
}
