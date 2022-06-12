package com.elearning.system.repositories.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class CategoryStatistics {
    private int categoryId;
    private String name;
    private Timestamp takeDate;
    private int count;
}
