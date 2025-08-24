package com.project.EventPlanner.features.user.domain.repository;

import com.project.EventPlanner.features.user.domain.model.PendingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PendingUserRepository extends JpaRepository<PendingUser, Long> {
    Optional<PendingUser> findByEmail(String email);
    void deleteByEmail(String email);
    Optional<PendingUser> findTopByEmailOrderByExpiryDateDesc(String email);
}
