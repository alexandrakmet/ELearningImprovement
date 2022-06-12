package com.elearning.system.repositories.dao;

import com.elearning.system.repositories.entities.TakeQuiz;

import java.util.List;

public interface TakeQuizDao extends GenericDao<TakeQuiz> {

    String TABLE_NAME = "take_quiz";

    TakeQuiz getUserResultByQuiz(int userId, int quizId);
}
