package com.project.EventPlanner.features.admin.api;

import com.project.EventPlanner.features.admin.domain.service.AdminService;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationDTO;
import com.project.EventPlanner.features.user.domain.dto.OrganizerApplicationReviewDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PutMapping("/organizer-applications/review")
    public ResponseEntity<OrganizerApplicationDTO> reviewApplication(@RequestBody OrganizerApplicationReviewDTO dto) {
        return ResponseEntity.ok(adminService.reviewOrganizerApplication(dto));
    }
}
