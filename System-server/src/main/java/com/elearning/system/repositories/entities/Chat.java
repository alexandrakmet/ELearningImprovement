package com.elearning.system.repositories.entities;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
public class Chat {
    private int id;
    private String name;
    private Timestamp creationDate;
    private List<User> users;
}
