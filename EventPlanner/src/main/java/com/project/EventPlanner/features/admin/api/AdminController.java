package com.project.EventPlanner.features.admin.api;

import com.project.EventPlanner.features.admin.domain.dto.DashboardStatsDTO;
import com.project.EventPlanner.features.admin.domain.service.AdminService;
import com.project.EventPlanner.features.event.domain.EventStatus;
import com.project.EventPlanner.features.event.domain.Mapper.EventMapper;
import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
import com.project.EventPlanner.features.event.domain.dto.EventReviewDTO;
import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.event.domain.repository.EventRepository;
import com.project.EventPlanner.features.event.domain.service.EventService;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationDTO;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationReviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;


    // Review organizer application already has feedback in DTO
    @PostMapping("/organizer-application/review")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrganizerApplicationDTO> review(
            @RequestBody OrganizerApplicationReviewDTO dto
    ) {
        return ResponseEntity.ok(adminService.reviewOrganizerApplication(dto));
    }

    @Operation(summary = "Get pending organizer applications", description = "Returns pending applications")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pending applications returned")
    })
    @GetMapping("/organizer-applications/pending")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<OrganizerApplicationDTO>> getPendingOrganizerApplications() {
        return ResponseEntity.ok(adminService.getPendingApplications());
    }

    //EventCrud

    // Approve event with feedback
    @PutMapping("/{eventId}/approve")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EventResponseDto> approveEvent(
            @PathVariable Long eventId,
            @RequestBody EventReviewDTO dto
    ) {
        return ResponseEntity.ok(adminService.approveEvent(eventId, dto.getFeedback()));
    }

    // Reject event with feedback
    @PutMapping("/{eventId}/reject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EventResponseDto> rejectEvent(
            @PathVariable Long eventId,
            @RequestBody EventReviewDTO dto
    ) {
        return ResponseEntity.ok(adminService.rejectEvent(eventId, dto.getFeedback()));
    }
    @Operation(summary = "Get pending events", description = "Returns all pending events")
    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<EventResponseDto> getPendingEvents() {
        return adminService.getPendingEvents();
    }
    @Operation(summary = "Get dashboard statistics", description = "Returns counts for total events, pending, approved, and attendees")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    @GetMapping("/statistics")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DashboardStatsDTO> getDashboardStatistics() {
        return ResponseEntity.ok(adminService.getDashboardStatistics());
    }

    // If you need separate endpoints for each statistic:
    @Operation(summary = "Get total events count", description = "Returns total number of events")
    @GetMapping("/statistics/total-events")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Long> getTotalEventsCount() {
        return ResponseEntity.ok(adminService.getTotalEventsCount());
    }

    @Operation(summary = "Get pending events count", description = "Returns number of pending events")
    @GetMapping("/statistics/pending-events")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Long> getPendingEventsCount() {
        return ResponseEntity.ok(adminService.getPendingEventsCount());
    }

    @Operation(summary = "Get approved events count", description = "Returns number of approved events")
    @GetMapping("/statistics/approved-events")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Long> getApprovedEventsCount() {
        return ResponseEntity.ok(adminService.getApprovedEventsCount());
    }

    @Operation(summary = "Get total attendees count", description = "Returns total number of attendees across all events")
    @GetMapping("/statistics/total-attendees")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Long> getTotalAttendeesCount() {
        return ResponseEntity.ok(adminService.getTotalAttendeesCount());
    }

}
