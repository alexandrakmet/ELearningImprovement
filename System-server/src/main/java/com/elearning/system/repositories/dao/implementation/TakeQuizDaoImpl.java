package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.mappers.TakeQuizMapper;
import com.elearning.system.repositories.dao.TakeQuizDao;
import com.elearning.system.repositories.entities.TakeQuiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@PropertySource("classpath:takeQuiz.properties")
public class TakeQuizDaoImpl extends GenericDaoImpl<TakeQuiz> implements TakeQuizDao {


    @Value("#{${sql.takeQuiz}}")
    private Map<String, String> queries;

    protected TakeQuizDaoImpl() {
        super(new TakeQuizMapper(), TABLE_NAME);
    }

    @Override
    public TakeQuiz getUserResultByQuiz(int userId, int quizId) {
        TakeQuiz takeQuiz;
        try {
            takeQuiz = jdbcTemplate.queryForObject(queries.get("getQuizResultByUser"),
                    new Object[]{userId, quizId}, new TakeQuizMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return takeQuiz;
    }

    @Override
    protected String getInsertQuery() {
        return queries.get("insert");
    }

    @Override
    protected PreparedStatement getInsertPreparedStatement(PreparedStatement preparedStatement,
                                                           TakeQuiz takeQuiz) throws SQLException {
        preparedStatement.setInt(1, takeQuiz.getUserId());
        preparedStatement.setInt(2, takeQuiz.getQuizId());
        preparedStatement.setBoolean(3, takeQuiz.isCompleted());
        preparedStatement.setInt(4, takeQuiz.getScore());
        preparedStatement.setFloat(5, takeQuiz.getCorrectAnswersPercentage());

        return preparedStatement;
    }

    @Override
    protected String getUpdateQuery() {
        return queries.get("update");
    }

    @Override
    protected Object[] getUpdateParameters(TakeQuiz takeQuiz) {
        return new Object[]{
                takeQuiz.getScore(),
                takeQuiz.getCorrectAnswersPercentage(),
                takeQuiz.getUserId(),
                takeQuiz.getQuizId()
        };
    }

    @Override
    public int save(TakeQuiz takeQuiz) {
        String insertQuery = getInsertQuery();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection
                        .prepareStatement(insertQuery);
                return getInsertPreparedStatement(preparedStatement, takeQuiz);
            });
        } catch (DuplicateKeyException e) {
            return -1;
        }
        return 0;
    }
}
