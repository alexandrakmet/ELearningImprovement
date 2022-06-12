package com.elearning.system.repositories.dao;

import com.elearning.system.repositories.entities.Quiz;
import com.elearning.system.repositories.entities.enums.QuizStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;

public interface QuizDao extends GenericDao<Quiz> {

    String TABLE_NAME = "quiz";

    List<Quiz> getAllFullInfo();

    Quiz getFullInfo(int id);

    boolean addTag(int quizId, int tagId);

    void removeTag(int quizId, int tagId);

    boolean isUsersFavorite(int userId, int quizId);

    Page<Quiz> getQuizByStatus(QuizStatus status, Pageable pageable);

    Page<Quiz> findAllForPage(Pageable pageable, String name, String author, List<String> category, Timestamp minDate,
                              Timestamp maxDate, List<String> tags, QuizStatus[] status);


    Page<Quiz> getCompletedQuizzesByUserId(int userId, Pageable pageable);

    Page<Quiz> getCreatedQuizzesByUserId(int userId, Pageable pageable);

    Page<Quiz> getFavouriteQuizzesByUserId(int userId, Pageable pageable);

    boolean getFavouriteMarkByUserIdAndQuizId(int userId, int quizId);

    void updateQuizStatus(int quizId, QuizStatus quizStatus);

    boolean markQuizAsFavorite(int userId, int quizId);

    boolean unmarkQuizAsFavorite(int userId, int quizId);
}
