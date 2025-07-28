package com.project.EventPlanner.features.event.domain.dto;

import lombok.Data;

@Data
public class RegisteredUserDTO {
    private Long userId;
    private String username;
    private String email;
    private String fullName;
}
