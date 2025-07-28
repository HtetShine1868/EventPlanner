package com.project.EventPlanner.features.event.domain.dto;

import lombok.Data;

import java.util.Map;

@Data
public class GenderStatsDTO {
    private Long eventId;
    private String eventTitle;
    private Map<String, Long> genderCounts;
}
