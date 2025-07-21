package com.project.EventPlanner.features.event.domain.dto;

import com.project.EventPlanner.features.event.domain.EventStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private Double latitude;
    private Double longitude;
    private Integer capacity;
    private Integer registeredCount;
    private EventStatus status;
    private String categoryName;
    private String organizerUsername;
}
