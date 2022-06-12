package com.elearning.system.services;

import com.elearning.system.repositories.dao.UserAchievementsDao;
import com.elearning.system.repositories.entities.Achievement;
import com.elearning.system.repositories.entities.UserAchievement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserAchievementsService {

    @Autowired
    private UserAchievementsDao userAchievementsDao;

    public List<UserAchievement> getAchievementsForAll() {
        return userAchievementsDao.getAll();
    }

    public List<Achievement> getAchievementsByUserId(int userId) {
        return userAchievementsDao.getAchievementsByUserId(userId);
    }

    public void insertUserAchievements(List<UserAchievement> userAchievements) {
        userAchievementsDao.insert(userAchievements);
    }

    public void deleteUserAchievements(List<UserAchievement> userAchievements) {
        userAchievementsDao.delete(userAchievements.stream()
                .map(UserAchievement::getId)
                .collect(Collectors.toList()));
    }

}
