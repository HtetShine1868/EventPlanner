package com.project.EventPlanner.features.user.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileDto {
    private Long id;
    private Long userId;          // reference to User entity's ID
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
}

