package com.project.EventPlanner.features.registration.domain.mapper;

import com.project.EventPlanner.features.feedback.domain.dto.FeedbackResponseDTO;
import com.project.EventPlanner.features.feedback.domain.model.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "eventTitle", source = "event.title")
    FeedbackResponseDTO toDTO(Feedback feedback);
}

