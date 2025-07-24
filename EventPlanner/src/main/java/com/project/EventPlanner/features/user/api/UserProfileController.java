package com.project.EventPlanner.features.user.api;

import com.project.EventPlanner.features.user.domain.dto.UserProfileDto;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<UserProfileDto> createProfile(@RequestBody UserProfileDto dto,
                                                        @AuthenticationPrincipal User currentUser) {
        UserProfileDto created = userProfileService.createUserProfile(dto, currentUser);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDto> updateProfile(@PathVariable Long id,
                                                        @RequestBody UserProfileDto dto,
                                                        @AuthenticationPrincipal User currentUser) {
        UserProfileDto updated = userProfileService.updateUserProfile(id, dto, currentUser);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> getProfile(@PathVariable Long id,
                                                     @AuthenticationPrincipal User currentUser) {
        UserProfileDto profile = userProfileService.getUserProfileById(id);

        // Only allow access if owner or admin
        if (!profile.getUserId().equals(currentUser.getId()) &&
                !currentUser.getRole().getName().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(profile);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserProfileDto>> getAllProfiles() {
        return ResponseEntity.ok(userProfileService.getAllUserProfiles());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id,
                                              @AuthenticationPrincipal User currentUser) {
        userProfileService.deleteUserProfile(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
