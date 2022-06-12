package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.QuestionDao;
import com.elearning.system.repositories.dao.mappers.QuestionMapper;
import com.elearning.system.repositories.entities.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@PropertySource("classpath:database.properties")
public class QuestionDaoImpl extends GenericDaoImpl<Question> implements QuestionDao {

    @Value("#{${sql.question}}")
    private Map<String, String> questionQueries;

    protected QuestionDaoImpl() {
        super(new QuestionMapper(), TABLE_NAME);
    }


    @Override
    protected String getInsertQuery() {
        return questionQueries.get("insert");
    }

    @Override
    protected PreparedStatement getInsertPreparedStatement(PreparedStatement preparedStatement, Question question) throws SQLException {
        preparedStatement.setInt(1, question.getQuizId());
        preparedStatement.setString(2, question.getType().name().toLowerCase());
        preparedStatement.setString(3, question.getContent());
        preparedStatement.setInt(4, question.getImageId());
        return preparedStatement;
    }

    @Override
    protected String getUpdateQuery() {
        return questionQueries.get("update");
    }

    @Override
    protected Object[] getUpdateParameters(Question question) {
        return new Object[]{question.getQuizId(),
                question.getType().name().toLowerCase(),
                question.getContent(),
                question.getImageId(),
                question.getId()};
    }

    @Override
    public List<Question> getByQuizId(int id) {
        return jdbcTemplate.query(questionQueries.get("getByQuizId"),
                new Object[]{id},
                new QuestionMapper());
    }

    @Override
    public void deleteQuestions(List<Integer> questionId) {

        jdbcTemplate.update(getQueryForInsertQuestions(questionId),
                questionId.toArray());

    }


    private String getQueryForInsertQuestions(List<Integer> questions) {
        String query = questionQueries.get("deleteQuestions").concat(" ( ");

        for (int i = 0; i < questions.size() - 1; i++) {
            query = query.concat("?, ");
        }
        return query.concat(" ?);");
    }

}
