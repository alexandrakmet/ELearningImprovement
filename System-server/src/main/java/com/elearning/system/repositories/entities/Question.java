package com.elearning.system.repositories.entities;

import com.elearning.system.repositories.entities.enums.QuestionType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Question {
    private int id;
    private int quizId;
    private QuestionType type;
    private String content;
    private int score;
    private int imageId;
    private Image image;
    private List<QuestionOption> options;
}