package com.elearning.system.repositories.dao;

import com.elearning.system.repositories.dto.statistic.*;
import com.qucat.quiz.repositories.dto.statistic.*;
import com.elearning.system.repositories.entities.User;

import java.util.List;

public interface DashboardDao {

    List<User> getTopUsers(int limit);

    User getBestUserByQuizId(int quizId);

    List<CategoryStatistics> getStatisticInTheCategory(int id);

    List<QuizStatistics> getPercentOfCorrectAnswers(int id);

    BestQuiz getTheMostSuccessfulQuiz(int id);

    List<ComparedScores> getComparedScores(int userId);

    List<QuizStatistics> getFriendsPreferences(int userId);

    List<QuizStatistics> getStatisticOfQuizzesPlayed();

    List<AdminStatistics> getAmountOfCreatedAndPublishedQuizzes();
}
