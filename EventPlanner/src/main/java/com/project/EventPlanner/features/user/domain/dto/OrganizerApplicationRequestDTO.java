package com.project.EventPlanner.features.user.domain.dto;


import lombok.Data;

@Data
public class OrganizerApplicationRequestDTO {
    private Long userId;
    private String organizerName;
}
