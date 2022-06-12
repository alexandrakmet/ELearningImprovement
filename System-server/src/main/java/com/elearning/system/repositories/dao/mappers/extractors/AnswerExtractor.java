package com.elearning.system.repositories.dao.mappers.extractors;

import com.elearning.system.repositories.dto.game.AnswerDto;
import com.elearning.system.repositories.dto.game.UserDto;
import com.elearning.system.repositories.entities.Question;
import com.elearning.system.repositories.entities.enums.QuestionType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AnswerExtractor implements ResultSetExtractor<List<AnswerDto>> {
    @Override
    public List<AnswerDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, AnswerDto> answerMap = new HashMap<>();

        while (rs.next()) {
            int answerId = rs.getInt("answer_id");
            AnswerDto answer = answerMap.get(answerId);
            if (answer == null) {
                answer = AnswerDto.builder()
                        .id(answerId)
                        .questionId(rs.getInt("question_id"))
                        .time(rs.getTimestamp("time"))
                        .percent(rs.getInt("percent"))
                        .userId(rs.getInt("user_id"))
                        .gameId(rs.getString("game_id"))
                        .build();
                answerMap.put(answerId, answer);
            }

            UserDto user = answer.getUser();
            if (user == null) {
                user = UserDto.builder().build();
                user.setId(rs.getInt("user_id"));
                user.setGameId(rs.getString("game_id"));
                user.setRegisterId(rs.getInt("registered_id"));
                user.setLogin(rs.getString("login"));
                user.setScore(rs.getInt("user_score"));
                user.setComboAnswer(rs.getInt("combo_answer"));
                answer.setUser(user);
            }

            int questionId = rs.getInt("question_id");
            Question question = answer.getQuestion();
            if (question == null) {
                question = Question.builder().build();
                question.setId(questionId);
                question.setQuizId(rs.getInt("quiz_id"));
                question.setType(QuestionType.valueOf(rs.getString("type").toUpperCase()));
                question.setContent(rs.getString("content"));
                question.setScore(rs.getInt("question_score"));
                answer.setQuestion(question);
            }
        }
        return new ArrayList<>(answerMap.values());
    }
}
