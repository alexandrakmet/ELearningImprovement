package com.elearning.system.repositories.entities;

import com.elearning.system.repositories.entities.enums.ConditionOperator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@AllArgsConstructor
public class AchievementCondition {
    @EqualsAndHashCode.Exclude
    private int id;
    private ConditionOperator operator;
    private int value;
    private int achievementId;
    private int characteristicId;
    @EqualsAndHashCode.Exclude
    private AchievementCharacteristic characteristic;
}
