package com.project.EventPlanner.features.user.domain.dto;


import lombok.Data;

@Data
public class OrganizerApplicationRequestDTO {
    private String organizerName;
    private String email;
    private String description;
}
