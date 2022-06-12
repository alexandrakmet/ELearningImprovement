package com.elearning.system.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Image {
    private int id;
    private String src;
}
