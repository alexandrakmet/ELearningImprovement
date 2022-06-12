package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.DashboardDao;
import com.elearning.system.repositories.dao.mappers.UserMapper;
import com.elearning.system.repositories.dto.statistic.*;
import com.qucat.quiz.repositories.dto.statistic.*;
import com.elearning.system.repositories.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@PropertySource("classpath:system.properties")
public class DashboardDaoImpl implements DashboardDao {
    @Value("#{${sql.dashboard}}")
    private Map<String, String> dashboardQueries;

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getTopUsers(int limit) {
        return jdbcTemplate.query(dashboardQueries.get("getTopUsers"),
                new Object[]{limit},
                new UserMapper());
    }

    @Override
    public User getBestUserByQuizId(int quizId) {
        User user;
        try {
            user = jdbcTemplate.queryForObject(dashboardQueries.get("getBestUserByQuizId"),
                    new Object[]{quizId},
                    new UserMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return user;
    }

    @Override
    public List<CategoryStatistics> getStatisticInTheCategory(int userId) {
        return jdbcTemplate.query(dashboardQueries.get("getStatisticInTheCategoryById"),
                new Object[]{userId},
                (rs, rowNum) ->
                        new CategoryStatistics(
                                rs.getInt("category_id"),
                                rs.getString("name"),
                                rs.getTimestamp("take_date"),
                                rs.getInt("count")
                        ));
    }

    @Override
    public List<QuizStatistics> getPercentOfCorrectAnswers(int userId) {
        return jdbcTemplate.query(dashboardQueries.get("getPercentOfCorrectAnswersById"),
                new Object[]{userId},
                (rs, rowNum) ->
                        new QuizStatistics(
                                rs.getString("name"),
                                rs.getDouble("correct_answers_persentage")
                        ));
    }

    @Override
    public BestQuiz getTheMostSuccessfulQuiz(int userId) {
        try {
            return jdbcTemplate.queryForObject(dashboardQueries.get("getMaxScoreById"),
                    new Object[]{userId},
                    (rs, rowNum) ->
                            new BestQuiz(
                                    rs.getString("name"),
                                    rs.getTimestamp("take_date"),
                                    rs.getInt("score")
                            ));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<ComparedScores> getComparedScores(int userId) {
        return jdbcTemplate.query(dashboardQueries.get("getComparedScores"),
                new Object[]{userId},
                (rs, rowNum) ->
                        new ComparedScores(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getInt("score"),
                                rs.getInt("userId"),
                                rs.getInt("record")
                        ));
    }

    @Override
    public List<QuizStatistics> getFriendsPreferences(int userId) {
        return jdbcTemplate.query(dashboardQueries.get("getFriendsPreferences"),
                new Object[]{userId},
                (rs, rowNum) ->
                        new QuizStatistics(
                                rs.getString("name"),
                                rs.getDouble("count")
                        ));
    }

    @Override
    public List<QuizStatistics> getStatisticOfQuizzesPlayed() {
        return jdbcTemplate.query(dashboardQueries.get("getStatisticOfQuizzesPlayed"),
                (rs, rowNum) ->
                        new QuizStatistics(
                                rs.getString("name"),
                                rs.getDouble("count")
                        ));
    }

    private void createTemporaryTable() {
        jdbcTemplate.update(dashboardQueries.get("createTemporaryTable"));
    }

    @Override
    public List<AdminStatistics> getAmountOfCreatedAndPublishedQuizzes() {
        createTemporaryTable();
        return jdbcTemplate.query(dashboardQueries.get("getAmountOfPublishedQuizzes"), (rs, rowNum) ->
                new AdminStatistics(
                        rs.getDate("date"),
                        rs.getInt("created"),
                        rs.getInt("published")
                ));
    }

}

