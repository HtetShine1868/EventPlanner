package com.project.EventPlanner.features.user.domain.model;

import lombok.Data;

@Data
public class VerifyEmailDTO {
    private String email;
    private String code;
}
