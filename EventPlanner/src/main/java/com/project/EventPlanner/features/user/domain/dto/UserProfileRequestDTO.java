package com.project.EventPlanner.features.user.domain.dto;

import com.project.EventPlanner.features.user.domain.model.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileRequestDTO {
    private String fullName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String address;
}

