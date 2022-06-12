package com.elearning.system.repositories.dao.mappers;

import com.elearning.system.repositories.entities.Question;
import com.elearning.system.repositories.entities.enums.QuestionType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionMapper implements RowMapper<Question> {

    @Override
    public Question mapRow(ResultSet resultSet, int i) throws SQLException {
        return Question.builder()
                .id(resultSet.getInt("id"))
                .quizId(resultSet.getInt("quiz_id"))
                .type(QuestionType.valueOf(resultSet.getString("type").toUpperCase()))
                .content(resultSet.getString("content"))
                .score(resultSet.getInt("score"))
                .imageId(resultSet.getInt("image_id"))
                .build();
    }
}