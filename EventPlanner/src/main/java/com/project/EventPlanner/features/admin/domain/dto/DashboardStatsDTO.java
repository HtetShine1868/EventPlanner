package com.project.EventPlanner.features.admin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalEvents;
    private Long pendingReview;
    private Long approved;
    private Long totalAttendees;
}
