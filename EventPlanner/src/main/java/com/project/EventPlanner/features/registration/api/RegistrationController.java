package com.project.EventPlanner.features.registration.api;

import com.project.EventPlanner.features.event.domain.dto.EventResponseDto;
import com.project.EventPlanner.features.registration.domain.dto.RegistrationRequestDTO;
import com.project.EventPlanner.features.registration.domain.dto.RegistrationResponseDTO;
import com.project.EventPlanner.features.registration.domain.model.Registration;
import com.project.EventPlanner.features.registration.domain.service.RegistrationService;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    // 🔹 Register for an Event (Authenticated User)
    @PostMapping("/register")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<RegistrationResponseDTO> register(
            @RequestBody RegistrationRequestDTO dto,
            @AuthenticationPrincipal CustomUserDetail currentUserDetail
    ) {
        // Unwrap User entity from CustomUserDetail
        User currentUser = currentUserDetail.getUser();

        RegistrationResponseDTO response = registrationService.registerUserToEvent(dto, currentUser);

        return ResponseEntity.ok(response);
    }

    // Get My Registered Events (Authenticated User)
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Page<EventResponseDto>> getMyRegistrations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @AuthenticationPrincipal CustomUserDetail currentUserDetail
    ) {
        User currentUser = currentUserDetail.getUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("registerAt").descending());
        return ResponseEntity.ok(registrationService.getRegisteredEventsByUser(currentUser, pageable));
    }

}
