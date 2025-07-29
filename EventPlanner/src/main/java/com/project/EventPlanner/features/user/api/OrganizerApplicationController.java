package com.project.EventPlanner.features.user.api;

import com.project.EventPlanner.common.enums.OrganizerApplicationStatus;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationDTO;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationRequestDTO;
import com.project.EventPlanner.features.user.domain.model.OrganizerApplication;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.repository.OrganizerApplicationRepository;
import com.project.EventPlanner.features.user.domain.repository.UserRepository;
import com.project.EventPlanner.features.user.domain.service.OrganizerApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/organizer-applications")
@RequiredArgsConstructor
public class OrganizerApplicationController {

    private final OrganizerApplicationRepository appRepository;
    private final UserRepository userRepository;
    private final OrganizerApplicationService organizerApplicationService;

    @Operation(summary = "Apply to become organizer", description = "User submits organizer application to become the organizer")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Application submitted"),
            @ApiResponse(responseCode = "400", description = "Already applied")
    })
    @PostMapping
    public ResponseEntity<OrganizerApplication> apply(@RequestBody OrganizerApplication request,
                                                      @AuthenticationPrincipal UserDetails userDetails) {

        // ✅ Load user from DB
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (appRepository.existsByUser(currentUser)) {
            throw new RuntimeException("You have already applied to become an organizer.");
        }
        // ✅ Set required fields
        request.setUser(currentUser);
        request.setAppliedAt(LocalDateTime.now());
        request.setStatus(OrganizerApplicationStatus.PENDING);

        OrganizerApplication saved = appRepository.save(request);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }


    @Operation(summary = "Get application by ID", description = "Fetch a specific organizer application")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrganizerApplication> getById(@PathVariable Long id) {
        OrganizerApplication app = appRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        return ResponseEntity.ok(app);
    }


    @Operation(summary = "List all applications", description = "Returns all organizer applications (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Applications returned")
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<OrganizerApplicationDTO>> getAll() {
        List<OrganizerApplicationDTO> pendingDtos = organizerApplicationService.getAll();
        return ResponseEntity.ok(pendingDtos);
    }
}
