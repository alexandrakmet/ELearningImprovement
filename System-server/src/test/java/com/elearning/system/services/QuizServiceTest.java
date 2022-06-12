package com.elearning.system.services;

import com.elearning.system.repositories.dao.QuizDao;
import com.elearning.system.repositories.entities.Question;
import com.elearning.system.repositories.entities.QuestionOption;
import com.elearning.system.repositories.entities.Quiz;
import com.elearning.system.repositories.entities.Tag;
import com.elearning.system.repositories.entities.enums.QuestionType;
import org.junit.Assert;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QuizServiceTest {

    List<Question> creationQuestions;
    List<Question> updateQuestions;
    List<Tag> tags = new ArrayList<>();
    List<Question> toInsertExpected = new ArrayList<>();
    List<Question> toDeleteExpected = new ArrayList<>();

    @Captor
    ArgumentCaptor<List<Question>> toInsert;

    @Captor
    ArgumentCaptor<List<Question>> toDelete;

    @InjectMocks
    private QuizService quizService;

    @Mock
    private QuestionService questionService;

    @Mock
    private ImageService imageService;

    @Mock
    private QuizDao quizDao;

    @Mock
    private TagService tagService;

    @Before
    public void init() {
        creationQuestions = new ArrayList<>();
        updateQuestions = new ArrayList<>();

        ArrayList<QuestionOption> options1 = new ArrayList<>();
        options1.add(QuestionOption.builder().id(11).questionId(20).isCorrect(true).build());
        options1.add(QuestionOption.builder().id(21).questionId(20).isCorrect(false).build());
        options1.add(QuestionOption.builder().id(31).questionId(20).isCorrect(false).build());
        options1.add(QuestionOption.builder().id(41).questionId(20).isCorrect(false).build());

        creationQuestions.add(Question.builder()
                .id(20)
                .type(QuestionType.SELECT_OPTION)
                .content("What is the name of this app?")
                .options(options1)
                .build());

        ArrayList<QuestionOption> options2 = new ArrayList<>();
        options2.add(QuestionOption.builder().id(11).questionId(20).isCorrect(false).build());
        options2.add(QuestionOption.builder().id(21).questionId(20).isCorrect(true).build());
        options2.add(QuestionOption.builder().id(31).questionId(20).isCorrect(true).build());
        options2.add(QuestionOption.builder().id(41).questionId(20).isCorrect(true).build());

        updateQuestions.add(Question.builder()
                .id(20)
                .type(QuestionType.SELECT_OPTION)
                .content("What is the name?")
                .options(options2)
                .build());

        toDeleteExpected.add(creationQuestions.get(0));
        toInsertExpected.add(updateQuestions.get(0));

        ArrayList<QuestionOption> opt = new ArrayList<>();
        opt.add(QuestionOption.builder().id(8).questionId(18).isCorrect(true).build());
        Question q2 = Question.builder()
                .id(18)
                .type(QuestionType.ENTER_ANSWER)
                .options(opt)
                .build();

        creationQuestions.add(q2);
        updateQuestions.add(q2);

        ArrayList<QuestionOption> optionsForSequence = new ArrayList<>();
        optionsForSequence.add(QuestionOption.builder().id(3).questionId(22).sequenceOrder(3).build());
        optionsForSequence.add(QuestionOption.builder().id(1).questionId(22).sequenceOrder(1).build());
        optionsForSequence.add(QuestionOption.builder().id(4).questionId(22).sequenceOrder(4).build());
        optionsForSequence.add(QuestionOption.builder().id(2).questionId(22).sequenceOrder(2).build());
        creationQuestions.add(
                Question.builder()
                        .id(22)
                        .type(QuestionType.SELECT_SEQUENCE)
                        .content("What is the order?")
                        .options(optionsForSequence)
                        .build()
        );

        updateQuestions.add(
                Question.builder()
                        .id(14)
                        .type(QuestionType.TRUE_FALSE)
                        .imageId(-1)
                        .build()
        );

        toDeleteExpected.add(creationQuestions.get(2));
        toInsertExpected.add(updateQuestions.get(2));

        tags.add(new Tag(1, "Test Tag 1"));
        tags.add(new Tag(2, "Test Tag 2"));
        tags.add(new Tag(3, "Test Tag 3"));
    }

    @Test
    public void createQuizSuccessfully() {
        final Quiz testQuiz = Quiz.builder().questions(creationQuestions).tags(tags).build();

        when(quizDao.save(any(Quiz.class))).thenReturn(1);
        when(questionService.addQuestion(any(Question.class))).thenReturn(1);
        when(tagService.addTag(any(Tag.class))).thenReturn(-1);

        quizService.createQuiz(testQuiz);
        Assert.assertEquals(1, testQuiz.getId());

        verify(quizDao).save(any(Quiz.class));
        verify(questionService, times(3)).addQuestion(any(Question.class));
        verify(tagService, times(3)).addTag(any(Tag.class));
    }

    @Test
    public void createQuizSaveProblem() {
        final Quiz testQuiz = Quiz.builder().questions(creationQuestions).tags(tags).build();

        when(quizDao.save(any(Quiz.class))).thenReturn(-1);

        int result = quizService.createQuiz(testQuiz);
        Assert.assertEquals(-1, result);

        verify(quizDao).save(any(Quiz.class));
    }

    @Test
    public void createQuizWithNoImage() {
        final Quiz testQuiz = Quiz.builder().questions(creationQuestions).tags(tags).imageId(-1).build();

        when(imageService.addLogoImage()).thenReturn(1);
        when(quizDao.save(any(Quiz.class))).thenReturn(1);
        when(questionService.addQuestion(any(Question.class))).thenReturn(1);
        when(tagService.addTag(any(Tag.class))).thenReturn(-1);

        quizService.createQuiz(testQuiz);

        Assert.assertEquals(1, testQuiz.getImageId());
        verify(imageService).addLogoImage();
    }

    @Test
    public void createNullQuiz() {
        Assert.assertEquals(-1, quizService.createQuiz(null));
    }

    @Test
    public void updateQuizQuestionsSuccessfully() {
        final Quiz testQuizBefore = Quiz.builder().questions(creationQuestions).tags(tags).build();
        final Quiz testQuizUpdate = Quiz.builder().questions(updateQuestions).tags(tags).build();

        when(quizDao.getFullInfo(anyInt())).thenReturn(testQuizBefore);
        when(tagService.addTag(any(Tag.class))).thenReturn(-1);
        doNothing().when(quizDao).removeTag(anyInt(), anyInt());
        doNothing().when(questionService).deleteQuestions(anyList());
        doNothing().when(questionService).addQuestions(anyList());
        doNothing().when(quizDao).update(any(Quiz.class));

        Assert.assertTrue(quizService.updateQuiz(testQuizUpdate));

        verify(questionService).deleteQuestions(toDelete.capture());
        verify(questionService).addQuestions(toInsert.capture());

        Assert.assertEquals(toDelete.getValue(), toDeleteExpected);
        Assert.assertEquals(toInsert.getValue(), toInsertExpected);
    }

    @Test
    public void updateNullQuiz() {
        Assert.assertFalse(quizService.updateQuiz(null));
    }
}
