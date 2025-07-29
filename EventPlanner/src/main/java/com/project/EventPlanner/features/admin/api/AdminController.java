package com.project.EventPlanner.features.admin.api;

import com.project.EventPlanner.features.admin.domain.service.AdminService;
import com.project.EventPlanner.features.event.domain.EventStatus;
import com.project.EventPlanner.features.event.domain.Mapper.EventMapper;
import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
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


    //Organizer-application
    @Operation(summary = "Review organizer application", description = "Approve or reject organizer application")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application reviewed")
    })
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

    @Operation(summary = "Approve event", description = "Admin approves event")
    @PutMapping("/{eventId}/approve")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EventResponseDto> approveEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(adminService.approveEvent(eventId));
    }

    @Operation(summary = "Reject event", description = "Admin rejects event")
    @PutMapping("/{eventId}/reject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EventResponseDto> rejectEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(adminService.rejectEvent(eventId));
    }

    @Operation(summary = "Get pending events", description = "Returns all pending events")
    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<EventResponseDto> getPendingEvents() {
        return adminService.getPendingEvents();
    }

}
