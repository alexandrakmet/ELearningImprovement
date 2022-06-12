package com.elearning.system.repositories.dao;

import com.elearning.system.repositories.entities.QuestionOption;

import java.util.List;

public interface QuestionOptionDao extends GenericDao<QuestionOption> {
    String TABLE_NAME = "question_option";

    List<QuestionOption> getByQuestionId(int id);
}
