package com.elearning.system.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
public class UserAchievement {
    @EqualsAndHashCode.Exclude
    private int id;
    private int userId;
    @EqualsAndHashCode.Exclude
    private User user;
    private int achievementId;
    @EqualsAndHashCode.Exclude
    private Achievement achievement;
    @EqualsAndHashCode.Exclude
    private Date date;
}
