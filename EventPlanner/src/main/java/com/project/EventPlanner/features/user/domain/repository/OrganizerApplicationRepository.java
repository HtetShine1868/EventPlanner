package com.project.EventPlanner.features.user.domain.repository;

import com.project.EventPlanner.common.enums.OrganizerApplicationStatus;
import com.project.EventPlanner.features.user.domain.model.OrganizerApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizerApplicationRepository extends JpaRepository<OrganizerApplication, Long> {
    Optional<OrganizerApplication> findByUserId(Long userId);
    List<OrganizerApplication> findByStatus(String status);
    Boolean existsByUserId(Long userId);
    List<OrganizerApplication> findByStatus(OrganizerApplicationStatus status);

}
