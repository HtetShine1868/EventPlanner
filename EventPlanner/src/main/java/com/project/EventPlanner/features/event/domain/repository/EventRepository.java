package com.project.EventPlanner.features.event.domain.repository;

import com.project.EventPlanner.features.event.domain.EventStatus;
import com.project.EventPlanner.features.event.domain.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends
        JpaRepository<Event, Long>,
        JpaSpecificationExecutor<Event> {
        List<Event> findByCategoryId(Long categoryId);
        List<Event> findByOrganizerId(Long organizerId);
        List<Event> findByTitleContainingIgnoreCase(String keyword);
        List<Event> findByStatus(EventStatus status);
        Page<Event> findByStatus(EventStatus status, Pageable pageable);

        Page<Event> findByCategoryIdAndStatus(Long categoryId, EventStatus status, Pageable pageable);

        Page<Event> findByLocationIsNotNullAndLocationContainingIgnoreCaseAndStatus(String location, EventStatus status, Pageable pageable);

        Page<Event> findByCategoryIdAndLocationIsNotNullAndLocationContainingIgnoreCaseAndStatus(
                Long categoryId, String location, EventStatus status, Pageable pageable);
        @Query("""
SELECT e FROM Event e
WHERE e.location = :location
AND e.status = 'APPROVED'
AND (
    :startTime < e.endTime AND :endTime > e.startTime
)
""")
        List<Event> findConflictingEvents(
                @Param("location") String location,
                @Param("startTime") LocalDateTime startTime,
                @Param("endTime") LocalDateTime endTime
        );

}
