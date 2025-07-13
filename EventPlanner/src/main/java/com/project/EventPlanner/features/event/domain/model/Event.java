package com.project.EventPlanner.features.event.domain.model;

import com.project.EventPlanner.features.event.domain.EventStatus;
import com.project.EventPlanner.features.user.domain.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private String location;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private EventCategory category;

    private Integer capacity;

    private Integer registeredCount;


    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.PENDING;



}

