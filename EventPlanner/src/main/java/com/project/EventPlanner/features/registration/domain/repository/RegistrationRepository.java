package com.project.EventPlanner.features.registration.domain.repository;

import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.registration.domain.model.Registration;
import com.project.EventPlanner.features.user.domain.model.User;
import com.project.EventPlanner.features.user.domain.model.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByEventId(Long eventId);
    Optional<Registration> findByEventIdAndUserId(Long eventId, Long userId);
    boolean existsByEventIdAndUserId(Long eventId, Long userId);
    List<Registration> findByUserId(Long userId);
    Page<Registration> findByUserId(Long userId, Pageable pageable);
    boolean findByUserIdAndEventId(Long userId, Long eventId);
    boolean existsByUserAndEvent(User user, Event event);

    // 1) Total attendees for an event
    @Query(value = "SELECT COUNT(*) FROM registration WHERE event_id = :eventId", nativeQuery = true)
    long countAttendeesByEventId(@Param("eventId") Long eventId);

    // 2) Gender distribution (counts)
    // Cast to text in case 'gender' is a DB enum; LEFT JOIN to include users missing profile as UNKNOWN
    @Query(value = """
        SELECT COALESCE(CAST(up.gender AS text), 'UNKNOWN') AS gender, COUNT(*) AS count
        FROM registration r
        JOIN app_user u ON r.user_id = u.id
        LEFT JOIN user_profile up ON u.id = up.user_id
        WHERE r.event_id = :eventId
        GROUP BY gender
        ORDER BY gender
        """, nativeQuery = true)
    List<Object[]> genderCountsByEvent(@Param("eventId") Long eventId);

    // 3) Age group counts (for most common age group)
    @Query(value = """
        SELECT
          CASE
            WHEN up.date_of_birth IS NULL THEN 'UNKNOWN'
            WHEN EXTRACT(YEAR FROM age(current_date, up.date_of_birth)) BETWEEN 0 AND 17 THEN '0-17'
            WHEN EXTRACT(YEAR FROM age(current_date, up.date_of_birth)) BETWEEN 18 AND 25 THEN '18-25'
            WHEN EXTRACT(YEAR FROM age(current_date, up.date_of_birth)) BETWEEN 26 AND 35 THEN '26-35'
            WHEN EXTRACT(YEAR FROM age(current_date, up.date_of_birth)) BETWEEN 36 AND 50 THEN '36-50'
            ELSE '50+'
          END AS age_group,
          COUNT(*) AS count
        FROM registration r
        JOIN app_user u ON r.user_id = u.id
        LEFT JOIN user_profile up ON u.id = up.user_id
        WHERE r.event_id = :eventId
        GROUP BY age_group
        ORDER BY age_group
        """, nativeQuery = true)
    List<Object[]> ageGroupCountsByEvent(@Param("eventId") Long eventId);
}
