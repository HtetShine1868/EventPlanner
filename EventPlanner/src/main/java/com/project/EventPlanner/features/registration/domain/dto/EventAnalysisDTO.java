package com.project.EventPlanner.features.registration.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
// EventSimpleAnalysisDto.java
public class EventAnalysisDTO {
    private Long eventId;
    private long totalAttendees;
    private List<GenderStat> genderDistribution; // count + percentage
    private String mostCommonAgeGroup;


    // getters/setters
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GenderStat {
        private String gender;
        private long count;
        private double percentage; // 0..100

        // getters/setters
    }
}

