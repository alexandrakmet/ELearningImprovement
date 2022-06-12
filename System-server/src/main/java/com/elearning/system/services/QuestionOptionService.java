package com.elearning.system.services;

import com.elearning.system.repositories.dao.QuestionOptionDao;
import com.elearning.system.repositories.entities.QuestionOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class QuestionOptionService {
    @Autowired
    private QuestionOptionDao questionOptionDao;

    int addQuestionOption(QuestionOption questionOption) {
        return questionOptionDao.save(questionOption);
    }

    void updateQuestionOption(QuestionOption questionOption) {
        questionOptionDao.update(questionOption);
    }

    public List<QuestionOption> getOptionsByQuestionId(int questionId) {
        return questionOptionDao.getByQuestionId(questionId);
    }

    public void deleteQuestionOption(QuestionOption questionOption) {
        questionOptionDao.deleteById(questionOption.getId());
    }
}
