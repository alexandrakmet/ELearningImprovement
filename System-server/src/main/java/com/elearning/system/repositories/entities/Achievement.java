package com.elearning.system.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Achievement {
    private int id;
    private String name;
    private String description;
    private List<AchievementCondition> conditions;
}
