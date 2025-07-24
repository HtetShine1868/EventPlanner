package com.project.EventPlanner.features.user.api;

import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationDTO;
import com.project.EventPlanner.features.user.domain.model.OrganizerApplication;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.service.OrganizerApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/organizer-applications")
@RequiredArgsConstructor
public class OrganizerApplicationController {

    private final OrganizerApplicationService service;

    @PostMapping
    public ResponseEntity<OrganizerApplicationDTO> apply(@RequestBody OrganizerApplicationDTO dto,
                                                         @AuthenticationPrincipal User currentUser) {
        OrganizerApplicationDTO created = service.createApplication(dto, currentUser);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizerApplicationDTO> getById(@PathVariable Long id,
                                                           @AuthenticationPrincipal User currentUser) {
        OrganizerApplicationDTO app = service.getById(id);

        // Only owner or admin can view
        if (!app.getUserId().equals(currentUser.getId()) &&
                !currentUser.getRole().getName().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(app);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<OrganizerApplicationDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
