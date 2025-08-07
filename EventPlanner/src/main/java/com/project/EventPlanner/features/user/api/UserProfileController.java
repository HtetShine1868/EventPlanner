package com.project.EventPlanner.features.user.api;

import com.project.EventPlanner.features.user.domain.dto.UserProfileRequestDTO;
import com.project.EventPlanner.features.user.domain.dto.UserProfileResponseDTO;
import com.project.EventPlanner.features.user.domain.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/{userId}/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Operation(summary = "Create user profile", description = "Create a profile for a registered user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profile created"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
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


    @Operation(summary = "Get user profile", description = "Returns profile data by user ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile found"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @GetMapping
    public ResponseEntity<UserProfileResponseDTO> get(@PathVariable Long userId) {
        return ResponseEntity.ok(userProfileService.getByUserId(userId));
    }


    @Operation(summary = "Update user profile", description = "Update existing profile for a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("#userId == authentication.principal.id")
    @PutMapping
    public ResponseEntity<UserProfileResponseDTO> update(
            @PathVariable Long userId,
            @RequestBody UserProfileRequestDTO dto
    ) {
        return ResponseEntity.ok(userProfileService.update(userId, dto));
    }
}
