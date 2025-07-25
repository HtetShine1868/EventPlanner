package com.project.EventPlanner.features.event.domain.repository;

import com.project.EventPlanner.features.event.domain.EventStatus;
import com.project.EventPlanner.features.event.domain.model.Event;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.rmi.registry.LocateRegistry;
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

        Page<Event> findByLocationIsNotNullAndLocationContainingIgnoreCaseAndStatus(String location,Double latitude,Double longitude, EventStatus status, Pageable pageable);

        Page<Event> findByCategoryIdAndLocationIsNotNullAndLocationContainingIgnoreCaseAndStatus(
                Long categoryId, String location,Double latitude,Double longitude, EventStatus status, Pageable pageable);
        @Query("""
SELECT e FROM Event e
WHERE e.location = :location
AND e.latitude = :latitude
AND e.longitude = :longitude
AND (
    (:startTime BETWEEN e.startTime AND e.endTime) OR
    (:endTime BETWEEN e.startTime AND e.endTime) OR
    (e.startTime BETWEEN :startTime AND :endTime)
)
AND (e.status = 'APPROVED' OR e.status = 'PENDING')

""")
        List<Event> findConflictingEvents(
                String location,
                Double latitude,
                Double longitude,
                LocalDateTime startTime,
                LocalDateTime endTime
        );

        @Query("""
SELECT e FROM Event e
WHERE e.id <> :excludeId
AND e.location = :location
AND e.latitude = :latitude
AND e.longitude = :longitude
AND (
    (:startTime BETWEEN e.startTime AND e.endTime) OR
    (:endTime BETWEEN e.startTime AND e.endTime) OR
    (e.startTime BETWEEN :startTime AND :endTime)
)
AND (e.status = 'APPROVED' OR e.status = 'PENDING')
""")
        List<Event> findConflictingEventsExcludingSelf(
                String location,
                Double latitude,
                Double longitude,
                LocalDateTime startTime,
                LocalDateTime endTime,
                Long excludeId
        );


}
