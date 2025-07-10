package com.project.EventPlanner.features.user.api;

import com.project.EventPlanner.features.user.domain.dto.UserProfileDto;
import com.project.EventPlanner.features.user.domain.model.UserProfile;
import com.project.EventPlanner.features.user.domain.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {
    private final UserProfileService profileService;

    public UserProfileController(UserProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public ResponseEntity<UserProfileDto> create(@RequestBody UserProfileDto dto) {
        return ResponseEntity.ok(profileService.CreateUserProfile(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getUserProfileById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDto> update(@PathVariable Long id, @RequestBody UserProfileDto dto) {
        return ResponseEntity.ok(profileService.updateUserProfile(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profileService.deleteUserProfile(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserProfileDto>> getAll() {
        return ResponseEntity.ok(profileService.getAllUserProfiles());
    }

}
