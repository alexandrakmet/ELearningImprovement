package com.elearning.system.controllers;

import com.elearning.system.repositories.dto.statistic.AdminStatistics;
import com.elearning.system.repositories.dto.statistic.CategoryStatistics;
import com.elearning.system.repositories.dto.statistic.ComparedScores;
import com.elearning.system.repositories.dto.statistic.QuizStatistics;
import com.elearning.system.repositories.entities.User;
import com.elearning.system.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/profile/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    private static final int TOP_USERS_LIMIT = 7;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/top")
    public List<User> getTopPlayers() {
        return dashboardService.getTopUsers(TOP_USERS_LIMIT);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('MODERATOR')")
    @GetMapping("/{id}/quizzes/played/categories")
    public List<CategoryStatistics> getStatisticInTheCategory(@PathVariable int id) {
        return dashboardService.getStatisticInTheCategory(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/quizzes/played/percent")
    public List<QuizStatistics> getPercentOfCorrectAnswers(@PathVariable int id) {
        return dashboardService.getPercentOfCorrectAnswers(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/quizzes/played/compare")
    public List<ComparedScores> getComparedScores(@PathVariable int id) {
        return dashboardService.getComparedScores(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/friends/preferences")
    public List<QuizStatistics> getFriendsPreferences(@PathVariable int id) {
        return dashboardService.getFriendsPreferences(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('MODERATOR')")
    @GetMapping("/quizzes/played/amount")
    public List<QuizStatistics> getStatisticOfQuizzesPlayed() {
        return dashboardService.getStatisticOfQuizzesPlayed();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or hasRole('MODERATOR')")
    @GetMapping("/quizzes/status")
    public List<AdminStatistics> getAmountOfCreatedAndPublishedQuizzes() {
        return dashboardService.getAmountOfCreatedAndPublishedQuizzes();
    }
}
