package com.elearning.system.repositories.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Notification {
    private int id;
    private int userId;
    @JsonProperty("isViewed")
    private boolean isViewed;
    private String author;
    private String action;
    private String authorLink;
    private String actionLink;
    @JsonProperty("isMessage")
    private boolean isMessage;
}
