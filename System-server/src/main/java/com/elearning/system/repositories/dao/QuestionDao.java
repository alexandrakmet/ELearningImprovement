package com.elearning.system.repositories.dao;

import com.elearning.system.repositories.entities.Question;

import java.util.List;

public interface QuestionDao extends GenericDao<Question> {
    String TABLE_NAME = "question";

    void deleteQuestions(List<Integer> questionId);
}
