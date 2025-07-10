package com.project.EventPlanner.features.event.domain.repository;

import com.project.EventPlanner.features.event.domain.model.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
    Optional<EventCategory> findByName(String name);
}
