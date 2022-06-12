package com.elearning.system.repositories.dto.game;

import com.elearning.system.repositories.entities.Image;
import com.elearning.system.repositories.entities.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class QuizDto {
    private int id;
    private String name;
    private int questionNumber;
    private int imageId;
    private Image image;
    private List<Question> questions;
}
