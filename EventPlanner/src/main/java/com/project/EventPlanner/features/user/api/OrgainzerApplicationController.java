package com.project.EventPlanner.features.user.api;

import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationDTO;
import com.project.EventPlanner.features.user.domain.model.OrganizerApplication;
import com.project.EventPlanner.features.user.domain.service.OrganizerApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizer-application")
@RequiredArgsConstructor
public class OrgainzerApplicationController {
    private final OrganizerApplicationService service;

    @PostMapping
    public ResponseEntity<OrganizerApplicationDTO> apply(@RequestBody OrganizerApplicationDTO dto) {
        return ResponseEntity.ok(service.apply(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizerApplicationDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrganizerApplicationDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}

