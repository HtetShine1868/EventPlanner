package com.project.EventPlanner.features.event.domain.dto;

import com.project.EventPlanner.features.event.domain.EventStatus;
import com.project.EventPlanner.features.event.domain.model.Event;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
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

    public static EventResponseDto fromEntity(Event event) {
        return EventResponseDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .location(event.getLocation())
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .capacity(event.getCapacity())
                .registeredCount(event.getRegisteredCount()) // Make sure this is a field in Event
                .status(event.getStatus())
                .categoryName(event.getCategory() != null ? event.getCategory().getName() : null)
                .organizerUsername(event.getOrganizer() != null ? event.getOrganizer().getUsername() : null)
                .build();
    }
}
