package com.project.EventPlanner.features.registration.domain.repository;

import com.project.EventPlanner.features.registration.domain.model.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByEventId(Long eventId);
    Optional<Registration> findByEventIdAndUserId(Long eventId, Long userId);
    boolean existsByEventIdAndUserId(Long eventId, Long userId);
    List<Registration> findByUserId(Long userId);
    Page<Registration> findByUserId(Long userId, Pageable pageable);

}
