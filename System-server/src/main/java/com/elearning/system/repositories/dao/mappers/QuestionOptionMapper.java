package com.elearning.system.repositories.dao.mappers;

import com.elearning.system.repositories.entities.QuestionOption;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionOptionMapper implements RowMapper<QuestionOption> {

    @Override
    public QuestionOption mapRow(ResultSet resultSet, int i) throws SQLException {
        return QuestionOption.builder()
                .id(resultSet.getInt("id"))
                .questionId(resultSet.getInt("question_id"))
                .content(resultSet.getString("content"))
                .isCorrect(resultSet.getBoolean("is_correct"))
                .sequenceOrder(resultSet.getInt("sequence_order"))
                .imageId(resultSet.getInt("image_id"))
                .build();
    }
}