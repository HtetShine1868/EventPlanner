package com.project.EventPlanner.features.event.domain.dto;

import com.project.EventPlanner.features.event.domain.EventStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private Double latitude;
    private Double longitude;
    private Integer capacity;
    private EventStatus status;
    private Long categoryId;
    private Long organizerId;
}
