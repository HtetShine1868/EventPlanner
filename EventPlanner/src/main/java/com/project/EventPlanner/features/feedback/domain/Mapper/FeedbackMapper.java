package com.project.EventPlanner.features.feedback.domain.Mapper;

import com.project.EventPlanner.features.feedback.domain.dto.FeedbackResponseDTO;
import com.project.EventPlanner.features.feedback.domain.model.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.title", target = "eventTitle")
    FeedbackResponseDTO toDTO(Feedback feedback);

    List<FeedbackResponseDTO> toDTOs(List<Feedback> feedbackList);
}

