package com.elearning.system.services;

import com.elearning.system.repositories.dao.QuestionDao;
import com.elearning.system.repositories.entities.QuestionOption;
import com.elearning.system.repositories.entities.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionService {
    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private QuestionOptionService questionOptionService;

    public int addQuestion(Question question) {
        int questionId = questionDao.save(question);
        if (questionId != -1) {
            for (QuestionOption option : question.getOptions()) {
                option.setQuestionId(questionId);
                questionOptionService.addQuestionOption(option);
            }
        }
        return questionId;
    }

    public void updateQuestion(Question question) {
        questionDao.update(question);
        for (QuestionOption option : question.getOptions()) {
            questionOptionService.updateQuestionOption(option);
        }
    }

    public void deleteQuestion(Question question) {
        questionDao.deleteById(question.getId());
    }

    public void deleteQuestionsById(List<Integer> questionsId) {
        questionDao.deleteQuestions(questionsId);
    }

    public void deleteQuestions(List<Question> questions) {
        questionDao.deleteQuestions(
                questions.stream().map(Question::getId).collect(Collectors.toList())
        );
    }

    public void addQuestions(List<Question> questions) {
        questions.stream().map(this::addQuestion).close();
    }
}
