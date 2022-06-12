package com.elearning.system.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameAccessor {
    private String code;
    private Image qrCode;
}
