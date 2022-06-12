package com.elearning.system.repositories.dao.implementation;

import com.elearning.system.repositories.dao.mappers.GameDtoMapper;
import com.elearning.system.repositories.dao.mappers.UserDtoMapper;
import com.elearning.system.repositories.dao.mappers.extractors.AnswerExtractor;
import com.elearning.system.repositories.dao.mappers.extractors.QuestionExtractor;
import com.elearning.system.repositories.dao.GameDao;
import com.elearning.system.repositories.dao.mappers.GameQuestionMapper;
import com.elearning.system.repositories.dao.mappers.ImageMapper;
import com.elearning.system.repositories.dto.game.*;
import com.qucat.quiz.repositories.dto.game.*;
import com.elearning.system.repositories.entities.Image;
import com.elearning.system.repositories.entities.Question;
import com.elearning.system.repositories.entities.QuestionOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Slf4j
@Repository
@PropertySource("classpath:game.properties")
public class GameDaoImpl implements GameDao {

    @Value("#{${sql.game}}")
    private Map<String, String> queries;

    @Autowired
    @Qualifier("h2JdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<UserDto> getUsersByGame(String id) {
        String selectQuery = queries.get("getUsersByGameId");
        return jdbcTemplate.query(selectQuery,
                new Object[]{id}, new UserDtoMapper());
    }

    @Override
    public List<AnswerDto> getAnswersToCurrentQuestionByGameId(String id) {
        String selectQuery = queries.get("getAnswersToCurrentQuestionByGameId");
        return jdbcTemplate.query(selectQuery,
                new Object[]{id}, new AnswerExtractor());
    }

    @Override
    public GameQuestionDto getCurrentQuestionByGameId(String id) {
        String selectQuery = queries.get("getCurrentQuestionByGameId");
        GameQuestionDto g;
        try {
            g = jdbcTemplate.queryForObject(selectQuery,
                    new Object[]{id}, new GameQuestionMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return g;
    }

    @Override
    public Question getQuestionById(int id) {
        String selectQuery = queries.get("getQuestionById");
        List<Question> result = jdbcTemplate.query(selectQuery,
                new Object[]{id}, new QuestionExtractor());
        return result != null? result.get(0): null;
    }

    @Override
    public int saveUser(UserDto user) {
        log.info("save user " + user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection
                        .prepareStatement(queries.get("saveUser"), Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, user.getGameId());
                preparedStatement.setString(2, user.getLogin());
                preparedStatement.setInt(3, user.getRegisterId());
                return preparedStatement;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            log.warn("user is already exist id={} ", user.getId());
            return -1;
        }
        return (int) Objects.requireNonNull(keyHolder.getKeys()).get("id");
    }

    @Override
    public int saveAnswer(AnswerDto answer) {
        try {
            return jdbcTemplate.update(
                    queries.get("saveAnswer"), answer.getUserId(),
                    answer.getPercent(),
                    answer.getQuestionId(),
                    answer.getTime(),
                    answer.getGameId());
        } catch (DuplicateKeyException e) {
            log.warn("image is already exist id={} ", answer.getId());
            return -1;
        }
    }

    @Override
    public int saveSettings(GameDto game) {
        return jdbcTemplate.update(
                queries.get("saveSettings"), game.getGameId(),
                game.getTime(),
                game.isQuestionAnswerSequence(),
                game.isCombo(),
                game.isIntermediateResult(),
                game.isQuickAnswerBonus());
    }

    @Override
    public void saveQuiz(QuizDto quiz) {
        try {
            jdbcTemplate.update(
                    queries.get("saveQuiz"), quiz.getId(),
                    quiz.getName(),
                    quiz.getQuestionNumber(),
                    quiz.getImageId());
        } catch (DuplicateKeyException e) {
            log.warn("quiz is already exist id={} ", quiz.getId());
        }
    }

    @Override
    public void saveQuestions(List<Question> questions) {
        saveQuestionsImage(questions);
        try {
            jdbcTemplate.update(getQueryForInsertQuestions(questions),
                    getParamsToInsertQuestions(questions));
        } catch (DuplicateKeyException e) {
            log.warn("question options is already exist");
        }
    }

    @Override
    public void saveQuestionOptions(List<QuestionOption> questionOptions) {
        saveQuestionOptionsImage(questionOptions);
        try {
            jdbcTemplate.update(getQueryForInsertOptions(questionOptions),
                    getParamsToInsertOptions(questionOptions));
        } catch (DuplicateKeyException e) {
            log.warn("question options is already exist");
        }
    }

    @Override
    public void saveImage(Image image) {
        try {
            jdbcTemplate.update(queries.get("saveImage"),
                    image.getId(),
                    image.getSrc());
        } catch (DuplicateKeyException e) {
            log.warn("image is already exist id={} ", image.getId());
        }
    }

    @Override
    public int saveGameQuestion(String gameId, int questionId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection
                        .prepareStatement(queries.get("saveGameQuestion"), Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, gameId);
                preparedStatement.setInt(2, questionId);
                return preparedStatement;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            return -1;
        }
        return (int) Objects.requireNonNull(keyHolder.getKeys()).get("id");
    }

    @Override
    public void deleteGame(String id) {
        jdbcTemplate.update(queries.get("deleteGame"), id);
    }

    @Override
    public void saveGame(int quizId, String gameId, int hostId) {
        jdbcTemplate.update(queries.get("saveGame"),
                gameId, quizId, hostId);
    }

    @Override
    public GameDto getGame(String id) {
        return jdbcTemplate.queryForObject(queries.get("getGame"),
                new Object[]{id},
                new GameDtoMapper());

    }

    @Override
    public void updateUserDto(UserDto user) {
        jdbcTemplate.update(queries.get("updateUser"),
                user.getGameId(),
                user.getLogin(),
                user.getRegisterId(),
                user.getScore(),
                user.getComboAnswer(),
                user.getId());
    }

    @Override
    public void updateGameQuestionToCurrent(int id) {
        jdbcTemplate.update(queries.get("updateGameQuestionToCurrent"), true, id);
    }

    @Override
    public void deleteGameQuestion(int id) {
        jdbcTemplate.update(queries.get("deleteGameQuestion"), id);
    }

    @Override
    public void updateUserToHost(int id) {
        jdbcTemplate.update(queries.get("updateUserToHost"), true, id);
    }

    @Override
    public GameQuestionDto getGameQuestion(String gameId, int random) {
        return jdbcTemplate.queryForObject(queries.get("getGameQuestion"),
                new Object[]{gameId, random},
                new GameQuestionMapper());
    }

    @Override
    public int getCountGameQuestion(String gameId) {
        try {
            return jdbcTemplate.queryForObject(queries.get("getCountGameQuestion"),
                    new Object[]{gameId}, Integer.class);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    private Object[] getParamsToInsertOptions(List<QuestionOption> questionOptions) {
        List<Object> params = new ArrayList<>();
        for (QuestionOption options : questionOptions) {
            params.add(options.getId());
            params.add(options.getQuestionId());
            params.add(options.getContent());
            params.add(options.isCorrect());
            params.add(options.getSequenceOrder());
            params.add(options.getImageId());
        }
        return params.toArray();
    }

    private String getQueryForInsertOptions(List<QuestionOption> questionOptions) {
        String query = queries.get("multipleSaveOption");

        for (int i = 0; i < questionOptions.size() - 1; i++) {
            query = query.concat("(?, ?, ?, ?, ?, ?), ");
        }
        return query.concat("(?, ?, ?, ?, ?, ?);");
    }

    private void saveQuestionOptionsImage(List<QuestionOption> questionOptions) {
        Map<Integer, Image> images = new HashMap<>();

        for (QuestionOption option : questionOptions) {
            if (option.getImage() != null) {
                images.put(option.getImage().getId(), option.getImage());
            }
        }
        if (images.isEmpty()) {
            return;
        }
        List<Integer> existId = getAlreadyExistImage(new ArrayList<>(images.keySet()));

        for (Integer imageId : existId) {
            if (images.get(imageId) != null) {
                images.remove(imageId);
            }
        }

        if (!images.isEmpty()) {
            jdbcTemplate.update(getQueryForInsertImages(new ArrayList<>(images.values())),
                    getParamsToInsertImage(new ArrayList<>(images.values())));
        }
    }

    private Object[] getParamsToInsertImage(List<Image> images) {
        List<Object> params = new ArrayList<>();
        for (Image image : images) {
            params.add(image.getId());
            params.add(image.getSrc());
        }
        return params.toArray();
    }

    private String getQueryForInsertImages(List<Image> images) {
        String query = queries.get("multipleSaveOptionsImage");

        for (int i = 0; i < images.size() - 1; i++) {
            query = query.concat("(?, ?), ");
        }
        return query.concat("(?, ?);");
    }

    private List<Integer> getAlreadyExistImage(List<Integer> imagesId) {
        String query = queries.get("getExistImage").concat("( ");
        for (int i = 0; i < imagesId.size() - 1; i++) {
            query = query.concat("?, ");
        }
        query = query.concat("?)");

        List<Image> existImages = jdbcTemplate.query(query, imagesId.toArray(Object[]::new),
                new ImageMapper());

        List<Integer> existId = new ArrayList<>();

        for (Image image : existImages) {
            existId.add(image.getId());
        }
        return existId;
    }

    private Object[] getParamsToInsertQuestions(List<Question> questions) {
        List<Object> params = new ArrayList<>();
        for (Question question : questions) {
            params.add(question.getId());
            params.add(question.getQuizId());
            params.add(question.getType().name().toLowerCase());
            params.add(question.getContent());
            params.add(question.getScore());
            params.add(question.getImageId());
        }
        return params.toArray();
    }

    private String getQueryForInsertQuestions(List<Question> questions) {
        String query = queries.get("multipleSaveQuestion");

        for (int i = 0; i < questions.size() - 1; i++) {
            query = query.concat("(?, ?, ?, ?, ?, ?), ");
        }
        return query.concat("(?, ?, ?, ?, ?, ?);");
    }

    private void saveQuestionsImage(List<Question> questions) {
        Map<Integer, Image> images = new HashMap<>();

        for (Question question : questions) {
            if (question.getImage() != null) {
                images.put(question.getImage().getId(), question.getImage());
            }
        }

        if (images.size() == 0) {
            return;
        }

        List<Integer> existId = getAlreadyExistImage(new ArrayList<>(images.keySet()));

        for (Integer imageId : existId) {
            if (images.get(imageId) != null) {
                images.remove(imageId);
            }
        }
        if (images.size() != 0) {
            jdbcTemplate.update(getQueryForInsertImages(new ArrayList<>(images.values())),
                    getParamsToInsertImage(new ArrayList<>(images.values())));
        }
    }
}
