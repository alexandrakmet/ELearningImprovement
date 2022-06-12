package com.elearning.system.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AchievementCharacteristic {
    private int id;
    private String name;
    private String script;
}
