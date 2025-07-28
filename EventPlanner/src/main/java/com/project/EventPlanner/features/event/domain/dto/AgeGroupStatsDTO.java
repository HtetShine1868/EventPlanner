package com.project.EventPlanner.features.event.domain.dto;

import lombok.Data;

import java.util.Map;

@Data
public class AgeGroupStatsDTO {
    private Long eventId;
    private String eventTitle;
    private Map<String, Long> ageGroups;


}
