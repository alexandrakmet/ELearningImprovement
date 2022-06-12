package com.elearning.system.repositories.dao.mappers.extractors;

import com.elearning.system.repositories.entities.*;
import com.qucat.quiz.repositories.entities.*;
import com.elearning.system.repositories.entities.enums.QuestionType;
import com.elearning.system.repositories.entities.enums.QuizStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class QuizExtractor implements ResultSetExtractor<List<Quiz>> {
    @Override
    public List<Quiz> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Quiz> quizMap = new HashMap<>();
        Map<Integer, Question> questionMap = new HashMap<>();
        Map<Integer, QuestionOption> optionMap = new HashMap<>();
        Map<Integer, Tag> tagMap = new HashMap<>();

        while (rs.next()) {
            int quizId = rs.getInt("quiz_id");
            Quiz quiz = quizMap.get(quizId);
            if (quiz == null) {
                quiz = Quiz.builder()
                        .id(quizId)
                        .name(rs.getString("quiz_name"))
                        .description(rs.getString("description"))
                        .authorId(rs.getInt("author_id"))
                        .categoryId(rs.getInt("category_id"))
                        .category(new Category(rs.getInt("category_id"), rs.getString("category_name")))
                        .status(QuizStatus.valueOf(rs.getString("status").toUpperCase()))
                        .publishedDate(rs.getTimestamp("published_date"))
                        .updatedDate(rs.getTimestamp("updated_date"))
                        .createdDate(rs.getTimestamp("created_date"))
                        .questionNumber(rs.getInt("questions_number"))
                        .maxScore(rs.getInt("max_score"))
                        .imageId(rs.getInt("quiz_image_id"))
                        .image(new Image(rs.getInt("quiz_image_id"), rs.getString("quiz_image_src")))
                        .build();
                quizMap.put(quizId, quiz);
            }

            List<Tag> tags = quiz.getTags();
            if (tags == null) {
                tags = new ArrayList<>();
                quiz.setTags(tags);
            }
            int tagId = rs.getInt("tag_id");
            if (tagId != 0) {
                Tag tag = tagMap.get(tagId);
                if (tag == null) {
                    tag = new Tag(rs.getInt("tag_id"), rs.getString("tag_name"));
                    tags.add(tag);
                    tagMap.put(tagId, tag);
                }
            }

            List<Question> questions = quiz.getQuestions();
            if (questions == null) {
                questions = new ArrayList<>();
                quiz.setQuestions(questions);
            }

            int questionId = rs.getInt("question_id");
            if (questionId == 0) {
                continue;
            }
            Question question = questionMap.get(questionId);
            if (question == null) {
                question = Question.builder()
                        .id(rs.getInt("question_id"))
                        .quizId(quizId)
                        .type(QuestionType.valueOf(rs.getString("question_type").toUpperCase()))
                        .content(rs.getString("question_content"))
                        .score(rs.getInt("question_score"))
                        .imageId(rs.getInt("question_image_id"))
                        .image(new Image(rs.getInt("question_image_id"), rs.getString("question_image_src")))
                        .build();
                questions.add(question);
                questionMap.put(questionId, question);
            }


            List<QuestionOption> options = question.getOptions();
            if (options == null) {
                options = new ArrayList<>();
                question.setOptions(options);
            }
            int optionId = rs.getInt("option_id");
            if (optionId != 0) {
                QuestionOption option = optionMap.get(optionId);
                if (option == null) {
                    option = QuestionOption.builder()
                            .id(rs.getInt("option_id"))
                            .questionId(questionId)
                            .content(rs.getString("option_content"))
                            .isCorrect(rs.getBoolean("option_is_correct"))
                            .sequenceOrder(rs.getInt("sequence_order"))
                            .imageId(rs.getInt("option_image_id"))
                            .image(new Image(rs.getInt("option_image_id"), rs.getString("option_image_src")))
                            .build();
                    options.add(option);
                    optionMap.put(optionId, option);
                }
            }


        }
        return new ArrayList<>(quizMap.values());
    }
}
