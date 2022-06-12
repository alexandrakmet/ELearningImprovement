package com.elearning.system.repositories.dto.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.elearning.system.repositories.entities.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class AnswerDto {
    private int id;
    private int userId;
    private UserDto user;
    private int percent;
    private int questionId;
    private Question question;
    private boolean isCorrect;
    private Timestamp time;


    private String gameId;

    private List<Integer> options;
    private String fullAnswer;
    private boolean trueFalse;
    @JsonProperty("sequence")
    private Map<Integer, Integer> sequence;
}
