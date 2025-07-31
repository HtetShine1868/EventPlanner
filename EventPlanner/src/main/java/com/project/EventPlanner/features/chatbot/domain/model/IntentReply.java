package com.project.EventPlanner.features.chatbot.domain.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "intent_reply")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntentReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String intent;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reply;
}