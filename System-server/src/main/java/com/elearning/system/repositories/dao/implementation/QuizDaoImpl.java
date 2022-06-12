package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.mappers.QuizMapper;
import com.elearning.system.repositories.dao.mappers.extractors.QuizExtractor;
import com.elearning.system.repositories.dao.QuizDao;
import com.elearning.system.repositories.entities.Image;
import com.elearning.system.repositories.entities.Quiz;
import com.elearning.system.repositories.entities.enums.QuizStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Page.empty;

@Slf4j
@Repository
@PropertySource("classpath:system.properties")
public class QuizDaoImpl extends GenericDaoImpl<Quiz> implements QuizDao {

    @Value("#{${sql.quiz}}")
    private Map<String, String> quizQueries;

    protected QuizDaoImpl() {
        super(new QuizMapper(), TABLE_NAME);
    }

    @Override
    protected String getInsertQuery() {
        return quizQueries.get("insert");
    }

    @Override
    protected PreparedStatement getInsertPreparedStatement(PreparedStatement preparedStatement,
                                                           Quiz quiz) throws SQLException {
        preparedStatement.setString(1, quiz.getName());
        preparedStatement.setInt(2, quiz.getAuthorId());
        preparedStatement.setInt(3, quiz.getCategoryId());
        preparedStatement.setString(4,
                quiz.getStatus() != null
                        ? quiz.getStatus().name().toLowerCase()
                        : QuizStatus.UNSAVED.name().toLowerCase());
        if (quiz.getPublishedDate() != null) {
            preparedStatement.setTimestamp(5, new Timestamp(quiz.getPublishedDate().getTime()));
        } else {
            preparedStatement.setNull(5, Types.TIMESTAMP);
        }
        if (quiz.getUpdatedDate() != null) {
            preparedStatement.setTimestamp(6, new Timestamp(quiz.getUpdatedDate().getTime()));
        } else {
            preparedStatement.setNull(6, Types.TIMESTAMP);
        }
        preparedStatement.setInt(7, quiz.getQuestionNumber());
        preparedStatement.setInt(8, quiz.getMaxScore());
        preparedStatement.setInt(9, quiz.getImageId());
        preparedStatement.setString(10, quiz.getDescription());
        return preparedStatement;
    }

    @Override
    protected String getUpdateQuery() {
        return quizQueries.get("update");
    }

    @Override
    protected Object[] getUpdateParameters(Quiz quiz) {
        return new Object[]{quiz.getName(),
                quiz.getAuthorId(),
                quiz.getCategoryId(),
                quiz.getStatus().name().toLowerCase(),
                quiz.getPublishedDate(),
                quiz.getUpdatedDate(),
                quiz.getCreatedDate(),
                quiz.getQuestionNumber(),
                quiz.getMaxScore(),
                quiz.getImageId(),
                quiz.getDescription(),
                quiz.getId()};
    }


    @Override
    public List<Quiz> getAllFullInfo() {
        return jdbcTemplate.query(quizQueries.get("getFullInfo"),
                new QuizExtractor());
    }

    @Override
    public Quiz getFullInfo(int id) {
        String getQuery = quizQueries.get("getFullInfo").replace(";", " WHERE quiz.id = ?;");
        List<Quiz> result = jdbcTemplate.query(getQuery,
                new Object[]{id},
                new QuizExtractor());
        if (result == null || result.size() == 0) {
            log.info("No quiz with id {} found in database", id);
            return null;
        }
        return result.get(0);
    }

    @Override
    public boolean addTag(int quizId, int tagId) {
        try {
            jdbcTemplate.update(
                    quizQueries.get("addTag"),
                    quizId, tagId
            );
        } catch (DuplicateKeyException e) {
            log.info("Relation quizId = {}, tagId={} already exists ", quizId, tagId);
            return true;
        }
        return true;
    }

    @Override
    public void removeTag(int quizId, int tagId) {
        jdbcTemplate.update(quizQueries.get("removeTag"), quizId, tagId);
    }

    @Override
    public boolean isUsersFavorite(int userId, int quizId) {
        try {
            jdbcTemplate.queryForObject(quizQueries.get("isUsersFavorite"),
                    new Object[]{userId, quizId}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    public Page<Quiz> getQuizByStatus(QuizStatus status, Pageable pageable) {
        int rowTotal = jdbcTemplate.queryForObject(quizQueries.get("rowCountByStatus"),
                new Object[]{status.name().toLowerCase()}, Integer.class);
        List<Quiz> quizzes = jdbcTemplate.query(quizQueries.get("getPageByStatus"),
                new QuizMapper());
        return new PageImpl<>(quizzes, pageable, rowTotal);
    }

    private PreparedStatement getPagePreparedStatement(List<Integer> id) {
        StringBuilder pageQuery = new StringBuilder(quizQueries.get("getFullInfo").replace(";", ""));

        if (id != null) {
            pageQuery.append(quizQueries.get("caseId"));

            String insertion = makeInsertion(id.size(), false);
            pageQuery.replace(pageQuery.indexOf("(") + 1, pageQuery.indexOf(")") - 1, insertion);
        }

        return getParamForPreparedStatement(pageQuery.toString(), id, null, null, null, null,
                null, null, null, null);
    }

    private String makeInsertion(int size, boolean isQuizStatus) {
        List<String> mark = new ArrayList<>();
        String insertion;
        for (int i = 0; i < size; i++) {
            if (isQuizStatus) {
                mark.add("?::quiz_status");
            } else {
                mark.add("?");
            }
        }
        insertion = String.join(",", mark);
        return insertion;
    }

    private PreparedStatement getParamForPreparedStatement(String query, List<Integer> id, Pageable pageable,
                                                           String name, String author, List<String> category,
                                                           Timestamp minDate, Timestamp maxDate, List<String> tags,
                                                           QuizStatus[] status) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = jdbcTemplate.getDataSource().getConnection().prepareStatement(query);
            int paramIndex = 1;
            if (author != null) {
                preparedStatement.setString(paramIndex++, author);
            }
            if (name != null) {
                preparedStatement.setString(paramIndex++, name);
            }

            if (category != null) {
                for (String categoryItem : category) {
                    preparedStatement.setString(paramIndex++, categoryItem);
                }
            }

            if (minDate != null) {
                preparedStatement.setTimestamp(paramIndex++, minDate);
            }

            if (minDate != null) {
                preparedStatement.setTimestamp(paramIndex++, maxDate);
            }

            if (tags != null) {
                for (String tagItem : tags) {
                    preparedStatement.setString(paramIndex++, tagItem);
                }
            }

            if (status != null) {
                for (QuizStatus statusItem : status) {
                    preparedStatement.setString(paramIndex++, statusItem.toString().toLowerCase());
                }
            }

            if (pageable != null) {
                preparedStatement.setInt(paramIndex++, pageable.getPageSize());
                preparedStatement.setLong(paramIndex++, pageable.getOffset());
            }

            if (id != null) {
                for (Integer item : id) {
                    preparedStatement.setInt(paramIndex++, item);
                }
            }
        } catch (SQLException e) {
            log.error(" " + e.getMessage());
            e.printStackTrace();
        }
        return preparedStatement;
    }

    private PreparedStatement getQuizIdQuery(Pageable pageable, String name, String author, List<String> category,
                                             Timestamp minDate, Timestamp maxDate, List<String> tags, QuizStatus[] status) {

        StringBuilder query = new StringBuilder(quizQueries.get("getQuizId"));
        boolean anotherParameter = false;

        if (author != null) {
            author = '%' + author + '%';
            query.append(quizQueries.get("authorJoin"));
            query.append(" WHERE ");
            query.append(quizQueries.get("caseAuthor"));
            anotherParameter = true;
        } else if (name != null | category != null | minDate != null | maxDate != null
                | tags != null | status != null) {
            query.append(" WHERE ");
        } else {
            query.append(quizQueries.get("caseAll"));

            return getParamForPreparedStatement(query.toString(), null, pageable, name, author, category,
                    minDate, maxDate, tags, status);
        }

        if (name != null) {
            name = '%' + name + '%';
            if (anotherParameter) {
                query.append(" AND ");
            }
            anotherParameter = true;
            query.append(quizQueries.get("caseName"));
        }

        if (category != null) {
            if (anotherParameter) {
                query.append(" AND ");
            }
            anotherParameter = true;

            query.append(quizQueries.get("caseCategory"));

            String insertion = makeInsertion(category.size(), false);

            query.replace(query.indexOf("(") + 1, query.indexOf(")") - 1, insertion);
        }

        if (minDate != null & maxDate != null) {
            if (anotherParameter) {
                query.append(" AND ");
            }
            anotherParameter = true;
            query.append(quizQueries.get("caseDate"));
        }

        if (tags != null) {
            if (anotherParameter) {
                query.append(" AND ");
            }
            anotherParameter = true;
            query.append(quizQueries.get("caseTag"));

            String insertion = makeInsertion(tags.size(), false);

            query.replace(query.lastIndexOf("(") + 1, query.lastIndexOf(")") - 1, insertion);
        }

        if (status != null) {
            if (anotherParameter) {
                query.append(" AND ");
            }

            anotherParameter = true;
            query.append(quizQueries.get("caseStatus"));
            String insertion = makeInsertion(status.length, true);

            query.replace(query.lastIndexOf("(") + 1, query.lastIndexOf(")") - 1, insertion);
        }
        query.append(quizQueries.get("caseAll"));
        return getParamForPreparedStatement(query.toString(), null, pageable, name, author, category,
                minDate, maxDate, tags, status);
    }

    private PreparedStatement getQuizCount(String query, String name, String author, List<String> category,
                                           Timestamp minDate, Timestamp maxDate, List<String> tags, QuizStatus[] status) {
        StringBuilder countQuery = new StringBuilder(quizQueries.get("quizCount"));
        if (author != null) {
            countQuery.append(quizQueries.get("authorJoin"));
        }
        if (tags != null | name != null | category != null | minDate != null | maxDate != null | status != null) {
            countQuery.append(query, query.indexOf("W"), query.lastIndexOf("L"));
        }
        countQuery.append(";");

        return getParamForPreparedStatement(countQuery.toString(), null, null, null, null, null,
                null, null, null, null);

    }

    @Override
    public Page<Quiz> findAllForPage(Pageable pageable, String name, String author, List<String> category,
                                     Timestamp minDate, Timestamp maxDate, List<String> tags, QuizStatus[] status) {
        PreparedStatement psId = getQuizIdQuery(pageable, name, author, category, minDate, maxDate, tags, status);
        PreparedStatement psRows = getQuizCount(psId.toString(), name, author, category, minDate, maxDate, tags, status);
        PreparedStatement psForPage = null;

        List<Integer> id = new ArrayList<>();
        List<Quiz> quizzes = new ArrayList<>();
        int quizCount = 0;
        try {
            ResultSet rs = psId.executeQuery();
            while (rs.next()) {
                id.add(rs.getInt("quiz_id"));
            }
            if (id.size() > 0) {
                psForPage = getPagePreparedStatement(id);
                if (psForPage != null) {
                    QuizExtractor qe = new QuizExtractor();
                    quizzes = qe.extractData(psForPage.executeQuery());
                }
            }
            ResultSet rsRowsNum = psRows.executeQuery();
            if (rsRowsNum.next()) {
                quizCount = rsRowsNum.getInt("quiz_id");
            }

        } catch (Exception e) {
            log.error("In findAllForPage method while read page of quiz from DB: " + e.getMessage());
        } finally {
            try {
                psId.getConnection().close();
                if (psForPage != null) {
                    psForPage.getConnection().close();
                }
                psRows.getConnection().close();
            } catch (SQLException throwables) {
                log.error("cant close connection", throwables);
            }
        }
        if (quizzes == null) {
            return empty(Pageable.unpaged());
        }
        for (Quiz quiz : quizzes) {
            quiz.setQuestions(null);
        }
        return new PageImpl<>(quizzes, pageable, quizCount);
    }

    private Page<Quiz> getQuizzesByUserId(int userId, Pageable pageable, String sqlGetQuizzes, String sqlGetRowCount) {
        int rowTotal = jdbcTemplate.queryForObject(quizQueries.get(sqlGetRowCount),
                new Object[]{userId},
                (resultSet, number) -> resultSet.getInt(1));
        List<Quiz> quizzes = jdbcTemplate.query(quizQueries.get(sqlGetQuizzes),
                new Object[]{userId, pageable.getPageSize(), pageable.getOffset()}, (resultSet, rowNum) ->
                        new Quiz(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getInt("author_id"),
                                resultSet.getInt("category_id"),
                                QuizStatus.valueOf(resultSet.getString("status").toUpperCase()),
                                resultSet.getTimestamp("published_date"),
                                resultSet.getTimestamp("updated_date"),
                                resultSet.getTimestamp("created_date"),
                                resultSet.getInt("questions_number"),
                                resultSet.getInt("max_score"),
                                resultSet.getInt("image_id"),
                                null,
                                null,
                                null,
                                new Image(resultSet.getInt("image_id"), resultSet.getString("src")),
                                resultSet.getString("description"),
                                false
                        ));
        return new PageImpl<>(quizzes, pageable, rowTotal);
    }

    @Override
    public Page<Quiz> getCompletedQuizzesByUserId(int userId, Pageable pageable) {
        return getQuizzesByUserId(userId, pageable, "getCompletedQuizzesPageByUserId", "getRowCountOfCompletedQuizzes");
    }

    @Override
    public Page<Quiz> getCreatedQuizzesByUserId(int userId, Pageable pageable) {
        return getQuizzesByUserId(userId, pageable, "getCreatedQuizzesPageByUserId", "getRowCountOfCreatedQuizzes");
    }

    @Override
    public Page<Quiz> getFavouriteQuizzesByUserId(int userId, Pageable pageable) {
        return getQuizzesByUserId(userId, pageable, "getFavouriteQuizzesPageByUserId", "getRowCountOfFavouriteQuizzes");
    }

    @Override
    public boolean getFavouriteMarkByUserIdAndQuizId(int userId, int quizId) {
        Boolean result = jdbcTemplate.queryForObject(quizQueries.get("getFavouriteMarkByUserIdAndQuizId"), new Object[]{userId, quizId}, (rs, rowNum) ->
                rs.getBoolean("is_favourite")
        );
        return result != null && result;
    }

    @Override
    public void updateQuizStatus(int quizId, QuizStatus quizStatus) {
        if (quizStatus.equals(QuizStatus.ACTIVATED)) {
            jdbcTemplate.update(quizQueries.get("updateQuizStatusToPublished"), quizStatus.name().toLowerCase(), quizId);
        } else {
            jdbcTemplate.update(quizQueries.get("updateQuizStatus"), quizStatus.name().toLowerCase(), quizId);
        }
    }

    @Override
    public boolean markQuizAsFavorite(int userId, int quizId) {
        try {
            jdbcTemplate.update(
                    quizQueries.get("markAsFavorite"),
                    quizId, userId
            );
        } catch (DuplicateKeyException e) {
            log.info("Relation quizId = {}, userId={} in table favorite_mark already exists ", quizId, userId);
            return true;
        }
        return true;
    }

    @Override
    public boolean unmarkQuizAsFavorite(int userId, int quizId) {
        return jdbcTemplate.update(
                quizQueries.get("unmarkAsFavorite"),
                userId, quizId
        ) != 0;
    }

}
