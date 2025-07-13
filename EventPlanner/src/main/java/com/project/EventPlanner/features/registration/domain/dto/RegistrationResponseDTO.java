package com.project.EventPlanner.features.registration.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegistrationRespnseDTO {
    private Long id;
    private String eventTitle;
    private String username;
    private LocalDateTime registeredAt;

}
