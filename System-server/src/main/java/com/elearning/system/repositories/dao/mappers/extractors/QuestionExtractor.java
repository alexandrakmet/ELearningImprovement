package com.elearning.system.repositories.dao.mappers.extractors;

import com.elearning.system.repositories.entities.Image;
import com.elearning.system.repositories.entities.Question;
import com.elearning.system.repositories.entities.QuestionOption;
import com.elearning.system.repositories.entities.enums.QuestionType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionExtractor implements ResultSetExtractor<List<Question>> {
    @Override
    public List<Question> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Question question = null;
        Map<Integer, QuestionOption> optionMap = new HashMap<>();

        while (rs.next()) {

            int questionId = rs.getInt("question_id");
            if (question == null) {
                question = Question.builder()
                        .id(rs.getInt("question_id"))
                        .quizId(rs.getInt("quiz_id"))
                        .type(QuestionType.valueOf(rs.getString("type").toUpperCase()))
                        .content(rs.getString("question_content"))
                        .score(rs.getInt("question_score"))
                        .imageId(rs.getInt("question_image_id"))
                        .image(new Image(rs.getInt("question_image_id"), rs.getString("question_image_src")))
                        .build();
            }


            List<QuestionOption> options = question.getOptions();
            if (options == null) {
                options = new ArrayList<>();
                question.setOptions(options);
            }
            int optionId = rs.getInt("id_option");
            if (optionId != 0) {
                QuestionOption option = optionMap.get(optionId);
                if (option == null) {
                    option = QuestionOption.builder()
                            .id(rs.getInt("id_option"))
                            .questionId(questionId)
                            .content(rs.getString("content_option"))
                            .isCorrect(rs.getBoolean("is_correct_option"))
                            .sequenceOrder(rs.getInt("sequence"))
                            .imageId(rs.getInt("option_id_image"))
                            .image(new Image(rs.getInt("option_id_image"), rs.getString("option_image")))
                            .build();
                    options.add(option);
                    optionMap.put(optionId, option);
                }
            }


        }
        return List.of(question);
    }
}