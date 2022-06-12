package com.elearning.system.repositories.dao;

import com.elearning.system.repositories.dto.game.*;
import com.qucat.quiz.repositories.dto.game.*;
import com.elearning.system.repositories.entities.Image;
import com.elearning.system.repositories.entities.Question;
import com.elearning.system.repositories.entities.QuestionOption;

import java.util.List;

public interface GameDao {

    List<UserDto> getUsersByGame(String id);

    List<AnswerDto> getAnswersToCurrentQuestionByGameId(String id);

    GameQuestionDto getCurrentQuestionByGameId(String id);

    Question getQuestionById(int id);

    int saveUser(UserDto user);

    int saveAnswer(AnswerDto answer);

    int saveSettings(GameDto game);

    void saveQuiz(QuizDto quiz);

    void saveQuestions(List<Question> questions);

    void saveQuestionOptions(List<QuestionOption> questionOptions);

    void saveImage(Image image);

    int saveGameQuestion(String gameId, int questionId);

    void deleteGame(String id);

    void saveGame(int quizId, String gameId, int hostId);

    GameDto getGame(String id);

    void updateUserDto(UserDto user);

    void updateGameQuestionToCurrent(int id);

    void deleteGameQuestion(int id);

    void updateUserToHost(int id);

    GameQuestionDto getGameQuestion(String gameId, int random);

    int getCountGameQuestion(String gameId);
}
