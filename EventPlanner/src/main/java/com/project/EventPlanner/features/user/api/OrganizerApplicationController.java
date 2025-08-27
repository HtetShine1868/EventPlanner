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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/organizer-applications")
@RequiredArgsConstructor
public class OrganizerApplicationController {

    private final OrganizerApplicationRepository appRepository;
    private final UserRepository userRepository;
    private final OrganizerApplicationService organizerApplicationService;

    @Operation(summary = "Apply to become organizer", description = "User submits organizer application to become the organizer")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Application submitted"),
            @ApiResponse(responseCode = "400", description = "Already has an active application")
    })
    @PostMapping
    public ResponseEntity<?> apply(
            @RequestBody OrganizerApplicationRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            OrganizerApplicationDTO response =
                    organizerApplicationService.createApplication(dto, userDetails.getUsername());

            // Optional: fetch previous rejected feedback to return
            User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
            Optional<OrganizerApplication> lastRejected =
                    appRepository.findFirstByUserAndStatusOrderByAppliedAtDesc(user, OrganizerApplicationStatus.REJECTED);

            Map<String, Object> result = new HashMap<>();
            result.put("application", response);
            lastRejected.ifPresent(app -> result.put("lastRejectedFeedback", app.getFeedback()));

            return new ResponseEntity<>(result, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
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
