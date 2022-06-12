package com.elearning.system.repositories.dao.mappers;

import com.elearning.system.repositories.entities.TakeQuiz;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TakeQuizMapper implements RowMapper<TakeQuiz> {
    @Override
    public TakeQuiz mapRow(ResultSet resultSet, int i) throws SQLException {
        return TakeQuiz.builder()
                .userId(resultSet.getInt("user_id"))
                .quizId(resultSet.getInt("quiz_id"))
                .isCompleted(resultSet.getBoolean("is_completed"))
                .score(resultSet.getInt("score"))
                .correctAnswersPercentage(resultSet.getFloat("correct_answers_persentage"))
                .takeDate(resultSet.getDate("take_date"))
                .build();
    }
}