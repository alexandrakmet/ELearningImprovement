package com.elearning.system.services;

import com.elearning.system.repositories.dao.GameDao;
import com.elearning.system.repositories.dto.game.*;
import com.qucat.quiz.repositories.dto.game.*;
import com.elearning.system.repositories.entities.Question;
import com.elearning.system.repositories.entities.QuestionOption;
import edu.emory.mathcs.backport.java.util.Collections;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Data
@Component
@Scope("prototype")
public class GameProcess implements Runnable {

    private final int COMBO_COUNT = 3;

    private String gameId;

    @Autowired
    private WebSocketSenderService socketSenderService;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private TakeQuizService takeQuizService;


    private void questionAnswerSequence(Question question, int secondLeft, int currQuestion) {
        question.setOptions(null);
        socketSenderService.sendQuestion(question, gameId, currQuestion);
        try {
            Thread.sleep((secondLeft / 2) * 1000);
        } catch (InterruptedException e) {
            log.error("Thread was interrupted", e);
        }
    }

    private void waitAllAnswer(int secondLeft) {
        while (secondLeft != -1) {
            try {
                Thread.sleep(1000);
                secondLeft--;
                if (checkAllSendAnswer()) {
                    break;
                }
            } catch (InterruptedException e) {
                log.error("Thread was interrupted", e);
            }
        }
    }

    private void sendResults(boolean isFinal, int currQuestion) {
        Users users = new Users(gameDao.getUsersByGame(gameId), isFinal);
        socketSenderService.sendResults(gameId, users, currQuestion);
        if (isFinal) {
            return;
        }
        try {
            Thread.sleep((long) 1e4);
        } catch (InterruptedException e) {
            log.error("Thread was interrupted", e);
        }
    }

    @Override
    public void run() {
        GameDto gameDto = gameDao.getGame(gameId);
        gameDto.setCountQuestions(gameDao.getCountGameQuestion(gameId));

        int currentQuestion = 0;
        Map<Integer, Integer> answersPercents = new HashMap<>();
        setUsersId(answersPercents);

        while (gameDao.getCountGameQuestion(gameId) != 0) {
            currentQuestion++;
            int count = gameDao.getCountGameQuestion(gameId);
            GameQuestionDto questionDto = gameDao.getGameQuestion(gameId, (int) (Math.random() * count));
            Question question = gameDao.getQuestionById(questionDto.getQuestionId());
            gameDao.updateGameQuestionToCurrent(questionDto.getId());

            int secondLeft = gameDto.getTime();
            if (gameDto.isQuestionAnswerSequence()) {
                questionAnswerSequence(question, secondLeft, currentQuestion);
                secondLeft -= secondLeft / 2;
                question = gameDao.getQuestionById(questionDto.getQuestionId());
            }

            clearRightAnswer(question);
            Collections.shuffle(question.getOptions());
            socketSenderService.sendQuestion(question, gameDto.getGameId(), currentQuestion);
            waitAllAnswer(secondLeft);


            question = gameDao.getQuestionById(questionDto.getQuestionId());
            List<AnswerDto> currAnswers = gameDao.getAnswersToCurrentQuestionByGameId(gameId);

            checkAnswers(gameDto, question, currAnswers);

            quickAnswerBonus(gameDto, currAnswers);

            updatePercents(answersPercents, currAnswers);

            if (gameDto.isIntermediateResult() && count != 1) {
                sendResults(false, currentQuestion);
            }

            gameDao.deleteGameQuestion(questionDto.getId());
        }
        sendResults(true, currentQuestion);

        List<UserDto> users = gameDao.getUsersByGame(gameId);
        updateUserToSave(users, gameDto, answersPercents);
        takeQuizService.saveUsersResults(users);
        gameDao.deleteGame(gameId);
    }

    private void updatePercents(Map<Integer, Integer> answersPercents, List<AnswerDto> currAnswers) {
        currAnswers.forEach(answer -> {
            int registerId = answer.getUser().getRegisterId();
            if (registerId != 0) {
                answersPercents.replace(registerId, answersPercents.get(registerId) + answer.getPercent());
            }
        });
    }

    private void setUsersId(Map<Integer, Integer> answersPercents) {
        List<UserDto> users = gameDao.getUsersByGame(gameId);
        for (UserDto user : users) {
            if (user.getRegisterId() != 0) {
                answersPercents.put(user.getRegisterId(), 0);
            }
        }
    }

    private void updateUserToSave(List<UserDto> users, GameDto gameDto, Map<Integer, Integer> answersPercents) {
        int countQuestion = gameDto.getCountQuestions();
        users = users.stream()
                .filter(userDto -> userDto.getRegisterId() != 0)
                .collect(Collectors.toList());
        for (UserDto user : users) {
            user.setPercent(answersPercents.get(user.getRegisterId()) / countQuestion);
            user.setQuizId(gameDto.getQuizId());
        }
    }

    private void checkAnswers(GameDto gameDto, Question question, List<AnswerDto> answers) {
        for (AnswerDto answer : answers) {
            UserDto user = answer.getUser();
            user.setScore(user.getScore() + question.getScore() * answer.getPercent() / 100);

            comboBonus(gameDto, question, answer, user);
            gameDao.updateUserDto(user);
        }
    }

    private void comboBonus(GameDto gameDto, Question question, AnswerDto answer, UserDto user) {
        if (gameDto.isCombo() && answer.getPercent() == 100) {
            user.setComboAnswer(user.getComboAnswer() + 1);
            if (user.getComboAnswer() >= COMBO_COUNT) {
                user.setScore(user.getScore() + question.getScore() * (user.getComboAnswer() - COMBO_COUNT + 1));
            }
        } else {
            user.setComboAnswer(0);

        }
    }

    private void quickAnswerBonus(GameDto gameDto, List<AnswerDto> answers) {
        if (gameDto.isQuickAnswerBonus()) {
            Optional<AnswerDto> firstCorrectAnswer = answers.stream()
                    .filter(answerDto -> answerDto.getPercent() == 100)
                    .findFirst();
            if (firstCorrectAnswer.isPresent()) {
                UserDto user = firstCorrectAnswer.get().getUser();
                user.setScore(user.getScore() + firstCorrectAnswer.get().getQuestion().getScore());
                gameDao.updateUserDto(user);
            }
        }
    }

    private void clearRightAnswer(Question question) {
        switch (question.getType()) {
            case ENTER_ANSWER:
                question.getOptions().get(0).setContent(null);
                break;

            case TRUE_FALSE:
                question.getOptions().get(0).setCorrect(false);
                break;

            case SELECT_OPTION:
                for (QuestionOption option : question.getOptions()) {
                    option.setCorrect(false);
                }
                break;

            case SELECT_SEQUENCE:
                for (QuestionOption option : question.getOptions()) {
                    option.setSequenceOrder(0);
                }
                break;

            default:
                break;
        }
    }


    private boolean checkAllSendAnswer() {
        List<AnswerDto> answers = gameDao.getAnswersToCurrentQuestionByGameId(gameId);
        List<UserDto> users = gameDao.getUsersByGame(gameId);
        return answers.size() == users.size();
    }


}
