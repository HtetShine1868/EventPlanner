package com.project.EventPlanner.features.event.domain.dto;

import com.project.EventPlanner.features.event.domain.EventStatus;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class EventRequestDto {
    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer capacity;
    private EventStatus status;
    private Long categoryId;
    private Long organizerId;
}
