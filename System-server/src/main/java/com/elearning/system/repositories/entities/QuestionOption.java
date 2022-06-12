package com.elearning.system.repositories.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionOption {
    private int id;
    private int questionId;
    private String content;
    @JsonProperty("isCorrect")
    private boolean isCorrect;
    private int sequenceOrder;
    private int imageId;
    private Image image;
}