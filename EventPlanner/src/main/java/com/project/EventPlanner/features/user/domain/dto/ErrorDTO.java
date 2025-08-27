package com.project.EventPlanner.features.user.domain.dto;

public class ErrorDTO {
    private String message;

    public ErrorDTO(String message) {
        this.message = message;
    }
    public String getMessage() { return message; }
}

