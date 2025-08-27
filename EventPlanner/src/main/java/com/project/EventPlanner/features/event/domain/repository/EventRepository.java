package com.project.EventPlanner.features.event.domain.repository;

import com.project.EventPlanner.features.event.domain.EventStatus;
import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.event.domain.model.EventCategory;
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
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends
        JpaRepository<Event, Long>,
        JpaSpecificationExecutor<Event> {
        List<Event> findByCategoryId(Long categoryId);
        List<Event> findByOrganizerId(Long organizerId);
        List<Event> findByTitleContainingIgnoreCase(String keyword);
        List<Event> findByStatus(EventStatus status);
        List<Event> findByCreatedById(Long organizerId);
        List<Event> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

        List<Event> findByCategoryInAndIdNotIn(Set<EventCategory> categories, Set<Long> excludedIds);

        List<Event> findByCategoryInAndIdNotInAndStartTimeAfter(
                Set<EventCategory> categories,
                Set<Long> excludedIds,
                LocalDateTime startTime
        );
        List<Event> findByStartTimeAfter(LocalDateTime startTime);

        Page<Event> findByOrganizerIdOrderByStartTimeDesc(Long organizerId, Pageable pageable);
        Page<Event> findByStatus(EventStatus status, Pageable pageable);

        Page<Event> findByOrganizerId(Long organizerId, Pageable pageable);

        Page<Event> findByCategoryIdAndLocationContainingIgnoreCaseAndStatus(Long categoryId, String location, EventStatus status, Pageable pageable);

        Page<Event> findByCategoryIdAndStatus(Long categoryId, EventStatus status, Pageable pageable);

        Page<Event> findByLocationContainingIgnoreCaseAndStatus(String location, EventStatus status, Pageable pageable);

        Page<Event> findByCategoryIdOrderByRegisteredCountDesc(Long categoryId, Pageable pageable);

        Page<Event> findAllByOrderByRegisteredCountDesc(Pageable pageable);
        Optional<Event> findById(Long id);
        Long countByStatus(EventStatus status);

        // Use correct method name for sum
        @Query("SELECT COALESCE(SUM(e.registeredCount), 0) FROM Event e")
        Long sumAttendees();
        @Query("SELECT COALESCE(SUM(e.registeredCount), 0) FROM Event e WHERE e.status = 'APPROVED'")
        Long getTotalAttendeesForApprovedEvents();

        @Query("""
SELECT e FROM Event e
WHERE e.location = :location
AND (
    (:startTime >= e.startTime AND :startTime < e.endTime) OR
    (:endTime > e.startTime AND :endTime <= e.endTime) OR
    (e.startTime >= :startTime AND e.startTime < :endTime) OR
    (e.endTime > :startTime AND e.endTime <= :endTime)
)
AND (e.status = 'APPROVED' OR e.status = 'PENDING')

""")
        List<Event> findConflictingEvents(
                String location,
                LocalDateTime startTime,
                LocalDateTime endTime
        );

        @Query("""
SELECT e FROM Event e
WHERE e.location = :location
AND e.id <> :excludeId
AND (
    (:startTime >= e.startTime AND :startTime < e.endTime) OR
    (:endTime > e.startTime AND :endTime <= e.endTime) OR
    (e.startTime >= :startTime AND e.startTime < :endTime) OR
    (e.endTime > :startTime AND e.endTime <= :endTime)
)
AND (e.status = 'APPROVED' OR e.status = 'PENDING')
""")
        List<Event> findConflictingEventsExcludingSelf(
                String location,
                LocalDateTime startTime,
                LocalDateTime endTime,
                Long excludeId
        );

        @Query("SELECT e FROM Event e WHERE e.status = 'APPROVED' ORDER BY SIZE(e.registrations) DESC, e.startTime ASC")
        Page<Event> findTrendingEvents(Pageable pageable);

        @Query("SELECT e FROM Event e WHERE e.category.id = :categoryId AND e.status = 'APPROVED' ORDER BY SIZE(e.registrations) DESC, e.startTime ASC")
        Page<Event> findTrendingEventsByCategory(@Param("categoryId") Long categoryId, Pageable pageable);
        @Query("""
    SELECT e FROM Event e
    WHERE e.createdBy.id = :organizerId
    ORDER BY SIZE(e.registrations) DESC
""")
        List<Event> findTopEventByOrganizer(@Param("organizerId") Long organizerId, Pageable pageable);



}
