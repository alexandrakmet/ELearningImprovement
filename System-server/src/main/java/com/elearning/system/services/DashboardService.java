package com.elearning.system.services;

import com.elearning.system.repositories.dto.statistic.*;
import com.elearning.system.repositories.dao.DashboardDao;
import com.qucat.quiz.repositories.dto.statistic.*;
import com.elearning.system.repositories.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class DashboardService {
    @Autowired
    private DashboardDao dashboardDao;

    public List<User> getTopUsers(int limit) {
        List<User> users = dashboardDao.getTopUsers(limit);
        if (users.isEmpty()) {
            log.warn("There are no users");
            return Collections.emptyList();
        } else {
            return users;
        }
    }

    public User getBestUserInTheQuiz(int quizId) {
        User user = dashboardDao.getBestUserByQuizId(quizId);
        if (user == null) {
            log.error("There is no the best user in the quiz with id={}", quizId);
            return null;
        } else {
            return user;
        }
    }

    public List<CategoryStatistics> getStatisticInTheCategory(int userId) {
        List<CategoryStatistics> categoryStatistics = dashboardDao.getStatisticInTheCategory(userId);
        if (categoryStatistics.isEmpty()) {
            log.warn("Statistic for user with id={} is empty", userId);
            return Collections.emptyList();
        } else {
            return categoryStatistics;
        }
    }

    public List<QuizStatistics> getPercentOfCorrectAnswers(int userId) {
        List<QuizStatistics> correctAnswers = dashboardDao.getPercentOfCorrectAnswers(userId);
        if (correctAnswers.isEmpty()) {
            log.warn("Can`t get information about correct user answers with id={} ", userId);
            return Collections.emptyList();
        } else {
            return correctAnswers;
        }
    }

    public BestQuiz getTheMostSuccessQuiz(int userId) {
        BestQuiz bestQuiz = dashboardDao.getTheMostSuccessfulQuiz(userId);
        if (bestQuiz == null) {
            log.error("There is no the most successful quiz for user with id = {}", userId);
            return null;
        } else {
            return bestQuiz;
        }
    }

    public List<ComparedScores> getComparedScores(int userId) {
        List<ComparedScores> comparedScores = dashboardDao.getComparedScores(userId);
        if (comparedScores.isEmpty()) {
            log.warn("Can`t get information about scores of user with id={}", userId);
            return Collections.emptyList();
        } else {
            return comparedScores;
        }
    }

    public List<QuizStatistics> getFriendsPreferences(int userId) {
        List<QuizStatistics> friendsPreferences = dashboardDao.getFriendsPreferences(userId);
        if (friendsPreferences.isEmpty()) {
            log.warn("Can`t get information about about the preferences of friends of a user with id = {}", userId);
            return Collections.emptyList();
        } else {
            return friendsPreferences;
        }
    }

    public List<QuizStatistics> getStatisticOfQuizzesPlayed() {
        List<QuizStatistics> playedQuizzes = dashboardDao.getStatisticOfQuizzesPlayed();
        if (playedQuizzes.isEmpty()) {
            log.warn("There are no quizzes played");
            return Collections.emptyList();
        } else {
            return playedQuizzes;
        }
    }

    @Transactional
    public List<AdminStatistics> getAmountOfCreatedAndPublishedQuizzes() {
        List<AdminStatistics> adminStatistics = dashboardDao.getAmountOfCreatedAndPublishedQuizzes();
        if (adminStatistics.isEmpty()) {
            log.warn("There are no created or published quizzes");
            return Collections.emptyList();
        } else {
            return adminStatistics;
        }
    }
}
