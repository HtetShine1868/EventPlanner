package com.project.EventPlanner.features.user.domain.dto;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String roleName;
}
