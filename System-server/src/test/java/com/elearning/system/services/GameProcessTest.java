package com.elearning.system.services;

import com.elearning.system.repositories.dao.GameDao;
import com.elearning.system.repositories.dto.game.*;
import com.qucat.quiz.repositories.dto.game.*;
import com.elearning.system.repositories.entities.Question;
import com.elearning.system.repositories.entities.QuestionOption;
import com.elearning.system.repositories.entities.enums.QuestionType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class GameProcessTest {

    private final int QUESTION_SCORE = 20;
    @Mock
    private GameDao gameDao;
    @Mock
    private WebSocketSenderService socketSenderService;
    @Mock
    private TakeQuizService takeQuizService;
    @InjectMocks
    private GameProcess gameProcess;
    private GameDto game;
    private int questionsCount;
    private List<UserDto> users = new ArrayList<>();
    private List<AnswerDto> answers = new ArrayList<>();

    @Captor
    private ArgumentCaptor<List<UserDto>> playersResult;

    private void initValues() {
        QuizDto quiz = QuizDto.builder().questions(new ArrayList<>()).build();

        ArrayList<QuestionOption> options1 = new ArrayList<>();
        options1.add(QuestionOption.builder().id(11).questionId(20).isCorrect(true).build());
        options1.add(QuestionOption.builder().id(21).questionId(20).isCorrect(false).build());
        options1.add(QuestionOption.builder().id(31).questionId(20).isCorrect(false).build());
        options1.add(QuestionOption.builder().id(41).questionId(20).isCorrect(false).build());

        quiz.getQuestions().add(Question.builder()
                .id(20)
                .score(QUESTION_SCORE)
                .type(QuestionType.SELECT_OPTION)
                .content("What is the name of this app?")
                .options(options1)
                .build());

        ArrayList<QuestionOption> options2 = new ArrayList<>();
        options2.add(QuestionOption.builder().id(11).questionId(20).isCorrect(false).build());
        options2.add(QuestionOption.builder().id(21).questionId(20).isCorrect(true).build());
        options2.add(QuestionOption.builder().id(31).questionId(20).isCorrect(true).build());
        options2.add(QuestionOption.builder().id(41).questionId(20).isCorrect(true).build());

        quiz.getQuestions().add(Question.builder()
                .id(20)
                .score(QUESTION_SCORE)
                .type(QuestionType.SELECT_OPTION)
                .content("What is the name?")
                .options(options2)
                .build());

        questionsCount = quiz.getQuestions().size();
        game = GameDto.builder().gameId("12345678").countQuestions(quiz.getQuestions().size()).quiz(quiz).build();

        users.add(UserDto.builder().gameId(game.getGameId()).id(1).registerId(1).build());
        users.add(UserDto.builder().gameId(game.getGameId()).id(2).build());

        answers.add(AnswerDto.builder().user(users.get(0)).percent(100).question(quiz.getQuestions().get(0)).build());
        answers.add(AnswerDto.builder().user(users.get(1)).percent(100).question(quiz.getQuestions().get(0)).build());
        answers.add(AnswerDto.builder().user(users.get(0)).percent(100).question(quiz.getQuestions().get(1)).build());
        answers.add(AnswerDto.builder().user(users.get(1)).percent(100).question(quiz.getQuestions().get(1)).build());
    }

    @Before
    public void initMocks() {
        initValues();

        when(gameDao.getGame(any())).thenReturn(game);
        when(gameDao.getCountGameQuestion(any()))
                .thenReturn(questionsCount)
                .thenReturn(questionsCount)
                .thenReturn(questionsCount - 1)
                .thenReturn(questionsCount - 1)
                .thenReturn(questionsCount - 2);
        when(gameDao.getUsersByGame(any())).thenReturn(users);
        when(gameDao.getGameQuestion(any(), anyInt())).thenReturn(GameQuestionDto.builder().build());

        when(gameDao.getAnswersToCurrentQuestionByGameId(any()))
                .thenReturn(answers.subList(0, answers.size() / 2))
                .thenReturn(answers.subList(answers.size() / 2, answers.size()));

    }

    @Test
    public void gameProcessWithoutSettingsRunTest() {
        when(gameDao.getQuestionById(anyInt()))
                .thenReturn(game.getQuiz().getQuestions().get(0))
                .thenReturn(game.getQuiz().getQuestions().get(0))
                .thenReturn(game.getQuiz().getQuestions().get(1))
                .thenReturn(game.getQuiz().getQuestions().get(1));
        gameProcess.run();

        verify(gameDao, times(1)).getGame(any());
        verify(gameDao, times(questionsCount)).getGameQuestion(any(), anyInt());
        verify(gameDao, times(questionsCount)).updateGameQuestionToCurrent(anyInt());
        verify(socketSenderService, times(questionsCount)).sendQuestion(any(), anyString(), anyInt());
        verify(gameDao, times(answers.size())).updateUserDto(any());
        verify(gameDao, times(questionsCount)).deleteGameQuestion(anyInt());
        verify(socketSenderService, times(1)).sendResults(any(), any(), anyInt());
        verify(takeQuizService).saveUsersResults(playersResult.capture());

        for (UserDto player : playersResult.getValue()) {
            assertEquals(player.getScore(), QUESTION_SCORE * questionsCount);
        }
    }

    @Test
    public void gameProcessWithIntermediateResultSettingRunTest() {
        game.setIntermediateResult(true);
        when(gameDao.getQuestionById(anyInt()))
                .thenReturn(game.getQuiz().getQuestions().get(0))
                .thenReturn(game.getQuiz().getQuestions().get(0))
                .thenReturn(game.getQuiz().getQuestions().get(1))
                .thenReturn(game.getQuiz().getQuestions().get(1));
        gameProcess.run();

        verify(gameDao, times(1)).getGame(any());
        verify(gameDao, times(questionsCount)).getGameQuestion(any(), anyInt());
        verify(gameDao, times(questionsCount)).updateGameQuestionToCurrent(anyInt());
        verify(socketSenderService, times(questionsCount)).sendQuestion(any(), anyString(), anyInt());
        verify(gameDao, times(answers.size())).updateUserDto(any());
        verify(gameDao, times(questionsCount)).deleteGameQuestion(anyInt());
        verify(socketSenderService, times(questionsCount)).sendResults(any(), any(), anyInt());
        verify(takeQuizService).saveUsersResults(playersResult.capture());

        for (UserDto player : playersResult.getValue()) {
            assertEquals(player.getScore(), QUESTION_SCORE * questionsCount);
        }
    }

    @Test
    public void gameProcessWithQuickAnswerBonusSettingRunTest() {
        game.setQuickAnswerBonus(true);
        when(gameDao.getQuestionById(anyInt()))
                .thenReturn(game.getQuiz().getQuestions().get(0))
                .thenReturn(game.getQuiz().getQuestions().get(0))
                .thenReturn(game.getQuiz().getQuestions().get(1))
                .thenReturn(game.getQuiz().getQuestions().get(1));
        gameProcess.run();

        verify(gameDao, times(1)).getGame(any());
        verify(gameDao, times(questionsCount)).getGameQuestion(any(), anyInt());
        verify(gameDao, times(questionsCount)).updateGameQuestionToCurrent(anyInt());
        verify(socketSenderService, times(questionsCount)).sendQuestion(any(), anyString(), anyInt());
        verify(gameDao, times(answers.size() + questionsCount)).updateUserDto(any());
        verify(gameDao, times(questionsCount)).deleteGameQuestion(anyInt());
        verify(socketSenderService, times(1)).sendResults(any(), any(), anyInt());
        verify(takeQuizService).saveUsersResults(playersResult.capture());

        assertEquals(QUESTION_SCORE * questionsCount * questionsCount, playersResult.getValue().get(0).getScore());
        assertEquals(QUESTION_SCORE * questionsCount, playersResult.getValue().get(1).getScore());

    }

    @Test
    public void gameProcessWithQuestionAnswerSequenceSettingRunTest() {
        game.setQuestionAnswerSequence(true);
        when(gameDao.getQuestionById(anyInt()))
                .thenReturn(game.getQuiz().getQuestions().get(0))
                .thenReturn(cloneQuestion(game.getQuiz().getQuestions().get(0)))
                .thenReturn(game.getQuiz().getQuestions().get(0))
                .thenReturn(game.getQuiz().getQuestions().get(1))
                .thenReturn(cloneQuestion(game.getQuiz().getQuestions().get(1)))
                .thenReturn(game.getQuiz().getQuestions().get(1));

        gameProcess.run();

        verify(gameDao, times(1)).getGame(any());
        verify(gameDao, times(questionsCount * 3)).getQuestionById(anyInt());
        verify(gameDao, times(questionsCount)).getGameQuestion(any(), anyInt());
        verify(gameDao, times(questionsCount)).updateGameQuestionToCurrent(anyInt());
        verify(socketSenderService, times(questionsCount)).sendQuestion(any(), anyString(), anyInt());
        verify(gameDao, times(answers.size())).updateUserDto(any());
        verify(gameDao, times(questionsCount)).deleteGameQuestion(anyInt());
        verify(socketSenderService, times(1)).sendResults(any(), any(), anyInt());
        verify(takeQuizService).saveUsersResults(playersResult.capture());

        for (UserDto player : playersResult.getValue()) {
            assertEquals(player.getScore(), QUESTION_SCORE * questionsCount);
        }
    }

    private Question cloneQuestion(Question question) {
        return Question.builder()
                .id(question.getId())
                .score(question.getScore())
                .content(question.getContent())
                .type(question.getType())
                .options(question.getOptions())
                .build();
    }
}