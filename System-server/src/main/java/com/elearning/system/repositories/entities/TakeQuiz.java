package com.elearning.system.repositories.entities;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class TakeQuiz {

    private int userId;
    private int quizId;
    private boolean isCompleted;
    private int score;
    private float correctAnswersPercentage;
    private Date takeDate;

}
