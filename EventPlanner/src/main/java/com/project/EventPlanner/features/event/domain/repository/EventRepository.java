package com.project.EventPlanner.features.event.domain.repository;

import com.project.EventPlanner.features.event.domain.EventStatus;
import com.project.EventPlanner.features.event.domain.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.util.List;

public interface EventRepository extends
        JpaRepository<Event, Long>,
        JpaSpecificationExecutor<Event> {
        List<Event> findByCategoryId(Long categoryId);
        List<Event> findByOrganizerId(Long organizerId);
        List<Event> findByTitleContainingIgnoreCase(String keyword);
        List<Event> findByStatus(EventStatus status);
        Page<Event> findByStatus(EventStatus status, Pageable pageable);

}
