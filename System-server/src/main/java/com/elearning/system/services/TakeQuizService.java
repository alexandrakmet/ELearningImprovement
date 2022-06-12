package com.elearning.system.services;

import com.elearning.system.repositories.dto.game.UserDto;
import com.elearning.system.repositories.dao.TakeQuizDao;
import com.elearning.system.repositories.entities.Quiz;
import com.elearning.system.repositories.entities.TakeQuiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class TakeQuizService {

    @Autowired
    private TakeQuizDao takeQuizDao;

    @Autowired
    private UserService userService;

    @Autowired
    private QuizService quizService;

    void saveUsersResults(List<UserDto> users) {

        users.stream()
                .filter(user -> user.getRegisterId() != 0)
                .forEach(this::saveOrUpdateResult);

        users.stream()
                .max(Comparator
                        .comparing(UserDto::getRegisterId))
                .ifPresent(this::updateQuizMaxScore);

    }

    private void saveOrUpdateResult(UserDto user) {
        TakeQuiz takeQuiz = takeQuizDao.getUserResultByQuiz(
                user.getRegisterId(), user.getQuizId());
        if (takeQuiz != null && user.getScore() > takeQuiz.getScore()) {
            takeQuizDao.update(createNewTakeQuiz(user));
        } else if (takeQuiz == null) {
            takeQuizDao.save(createNewTakeQuiz(user));
        }
        userService.updateUsersScore(user);
    }

    private void updateQuizMaxScore(UserDto user) {
        Quiz quiz = quizService.getQuizById(user.getQuizId());

        if (user.getScore() > quiz.getMaxScore()) {
            quiz.setMaxScore(user.getScore());
            quizService.updateQuiz(quiz);
        }
    }

    private TakeQuiz createNewTakeQuiz(UserDto user) {
        return TakeQuiz.builder()
                .userId(user.getRegisterId())
                .score(user.getScore())
                .correctAnswersPercentage(user.getPercent())
                .quizId(user.getQuizId())
                .isCompleted(true)
                .build();
    }
}
