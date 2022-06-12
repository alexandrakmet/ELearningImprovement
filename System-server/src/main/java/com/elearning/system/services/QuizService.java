package com.elearning.system.services;

import com.elearning.system.repositories.dao.QuizDao;
import com.elearning.system.repositories.entities.NotificationType;
import com.elearning.system.repositories.entities.Question;
import com.elearning.system.repositories.entities.Quiz;
import com.elearning.system.repositories.entities.Tag;
import com.elearning.system.repositories.entities.enums.QuizStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class QuizService {
    @Autowired
    private QuizDao quizDao;

    @Autowired
    private TagService tagService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private SuggestionsService suggestionsService;

    @Autowired
    private WebSocketSenderService webSocketSenderService;

    @Transactional
    public int createQuiz(Quiz quiz) {
        quiz.setStatus(QuizStatus.ACTIVATED);

        if (quiz.getImageId() == -1) {
            quiz.setImageId(imageService.addLogoImage());
        }

        int quizId = quizDao.save(quiz);
        if (quizId == -1) {
            log.info("createQuiz: Quiz isn't saved in data base");
            return -1;
        }
        quiz.setId(quizId);

        for (Question question : quiz.getQuestions()) {
            question.setQuizId(quizId);
            questionService.addQuestion(question);
        }

        addQuizTags(quiz);

        log.info("createQuiz: Quiz successfully saved");
        return quizId;
    }

    @Transactional
    public boolean updateQuiz(Quiz quiz) {
        if (quiz == null) {
            log.info("updateQuiz: Quiz is null");
            return false;
        }

        Quiz beforeUpdateQuiz = getQuizById(quiz.getId());
        deleteQuizTags(beforeUpdateQuiz);

        List<Question> afterUpdateQuestions = quiz.getQuestions();
        List<Question> beforeUpdateQuestions = beforeUpdateQuiz.getQuestions();

        List<Question> toInsert = new ArrayList<>(afterUpdateQuestions);
        List<Question> toDelete = new ArrayList<>(beforeUpdateQuestions);

        for (Question buq : beforeUpdateQuestions) {
            for (Question auq : afterUpdateQuestions) {
                if (buq.getId() == auq.getId() && buq.equals(auq)) {
                    toInsert.remove(auq);
                    toDelete.remove(buq);
                }
            }
        }

        questionService.deleteQuestions(toDelete);
        questionService.addQuestions(toInsert);

        addQuizTags(quiz);
        quizDao.update(quiz);

        return true;
    }

    void addQuizTags(Quiz quiz) {
        for (Tag tag : quiz.getTags()) {
            int tagId = tagService.addTag(tag);
            if (tagId != -1) {
                quizDao.addTag(quiz.getId(), tagId);
            }
        }
    }

    void deleteQuizTags(Quiz quiz) {
        int quizId = quiz.getId();
        for (Tag tag : quiz.getTags()) {
            quizDao.removeTag(quizId, tag.getId());
        }
    }

    public Quiz getQuizByIdForUser(int userId, int quizId) {
        Quiz quiz = getQuizById(quizId);
        quiz.setFavorite(getFavouriteMarkByUserIdAndQuizId(userId, quizId));
        return quiz;
    }

    Quiz getQuizById(int id) {
        return quizDao.getFullInfo(id);
    }

    public Page<Quiz> showPage(int page, int size, String name, String author, List<String> category,
                               Date minDate, Date maxDate, List<String> tags, QuizStatus[] status) {

        Timestamp tMinDate = null;
        Timestamp tMaxDate = null;
        if (minDate != null && maxDate != null) {
            tMinDate = new Timestamp(minDate.getTime());
            tMaxDate = new Timestamp(maxDate.getTime());
        }
        return quizDao.findAllForPage(PageRequest.of(page, size, Sort.Direction.DESC, "id"),
                name, author, category, tMinDate, tMaxDate, tags, status);
    }


    public Page<Quiz> getCompletedQuizzesByUserId(int userId, int page, int size) {
        Page<Quiz> completedQuizzes = quizDao.getCompletedQuizzesByUserId(userId, PageRequest.of(page, size,
                Sort.Direction.DESC, "id"));
        if (completedQuizzes.isEmpty()) {
            log.warn("There are no completed quizzes for user with id={}", userId);
            return Page.empty();
        } else {
            return completedQuizzes;
        }
    }

    public Page<Quiz> getCreatedQuizzesByUserId(int userId, int page, int size) {
        Page<Quiz> createdQuizzes = quizDao.getCreatedQuizzesByUserId(userId, PageRequest.of(page, size,
                Sort.Direction.DESC, "id"));
        if (createdQuizzes.isEmpty()) {
            log.warn("There are no created quizzes for user with id={}", userId);
            return Page.empty();
        } else {
            return createdQuizzes;
        }
    }

    public Page<Quiz> getFavouriteQuizzesByUserId(int userId, int page, int size) {
        Page<Quiz> favouriteQuizzes = quizDao.getFavouriteQuizzesByUserId(userId, PageRequest.of(page, size,
                Sort.Direction.DESC, "id"));
        if (favouriteQuizzes.isEmpty()) {
            log.warn("There are no favourite quizzes for user with id={}", userId);
            return Page.empty();
        } else {

            return favouriteQuizzes;
        }
    }

    boolean getFavouriteMarkByUserIdAndQuizId(int userId, int quizId) {
        return quizDao.getFavouriteMarkByUserIdAndQuizId(userId, quizId);
    }

    public void updateQuizStatus(int quizId, QuizStatus quizStatus) {
        quizDao.updateQuizStatus(quizId, quizStatus);

        if (quizStatus.equals(QuizStatus.ACTIVATED)) {
            sendSuggestion(quizId, quizStatus);
            Quiz forAuthorId = getQuizById(quizId);
            webSocketSenderService.sendNotification(forAuthorId.getAuthorId(), quizId, null, NotificationType.CREATED_QUIZ);
        }
    }

    private void sendSuggestion(int quizId, QuizStatus quizStatus) {
        if (quizStatus.equals(QuizStatus.ACTIVATED)) {
            Quiz quiz = getQuizById(quizId);
            suggestionsService.sendSuggestion(quizId, quiz.getName(), quiz.getCategory().getName());
        }
    }

    public boolean setQuizIsFavorite(int userId, int quizId, boolean isFavorite) {
        if (isFavorite) {
            return quizDao.markQuizAsFavorite(userId, quizId);
        } else {
            return quizDao.unmarkQuizAsFavorite(userId, quizId);
        }
    }


}
