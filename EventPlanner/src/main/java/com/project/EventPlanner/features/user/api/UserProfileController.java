package com.project.EventPlanner.features.user.api;

import com.project.EventPlanner.features.user.domain.dto.UserProfileRequestDTO;
import com.project.EventPlanner.features.user.domain.dto.UserProfileResponseDTO;
import com.project.EventPlanner.features.user.domain.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/{userId}/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    @PreAuthorize("#userId == authentication.principal.id")
    @PostMapping
    public ResponseEntity<UserProfileResponseDTO> create(
            @PathVariable Long userId,
            @RequestBody UserProfileRequestDTO dto
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated user: " + auth);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userProfileService.create(userId, dto));
    }

    @GetMapping
    public ResponseEntity<UserProfileResponseDTO> get(@PathVariable Long userId) {
        return ResponseEntity.ok(userProfileService.getByUserId(userId));
    }
    @PreAuthorize("#userId == authentication.principal.id")
    @PutMapping
    public ResponseEntity<UserProfileResponseDTO> update(
            @PathVariable Long userId,
            @RequestBody UserProfileRequestDTO dto
    ) {
        return ResponseEntity.ok(userProfileService.update(userId, dto));
    }
}
