package com.elearning.system.repositories.dao;

import com.elearning.system.repositories.entities.Achievement;
import com.elearning.system.repositories.entities.UserAchievement;

import java.util.List;

public interface AchievementDao extends GenericDao<Achievement> {
    String TABLE_NAME = "achievement";
}
