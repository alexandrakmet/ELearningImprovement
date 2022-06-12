package com.elearning.system.repositories.entities;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Group {
    private int id;
    private String name;
    private int authorId;
    private String invitationCode;
    private Timestamp creationDate;
}
