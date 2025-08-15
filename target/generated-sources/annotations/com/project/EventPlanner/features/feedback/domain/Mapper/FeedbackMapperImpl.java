package com.project.EventPlanner.features.feedback.domain.Mapper;

import com.project.EventPlanner.features.event.domain.model.Event;
import com.project.EventPlanner.features.feedback.domain.dto.FeedbackResponseDTO;
import com.project.EventPlanner.features.feedback.domain.model.Feedback;
import com.project.EventPlanner.features.user.domain.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-14T23:18:06+0630",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class FeedbackMapperImpl implements FeedbackMapper {

    @Override
    public FeedbackResponseDTO toDTO(Feedback feedback) {
        if ( feedback == null ) {
            return null;
        }

        FeedbackResponseDTO feedbackResponseDTO = new FeedbackResponseDTO();

        feedbackResponseDTO.setUsername( feedbackUserUsername( feedback ) );
        feedbackResponseDTO.setEventId( feedbackEventId( feedback ) );
        feedbackResponseDTO.setEventTitle( feedbackEventTitle( feedback ) );
        feedbackResponseDTO.setId( feedback.getId() );
        feedbackResponseDTO.setRating( feedback.getRating() );
        feedbackResponseDTO.setComment( feedback.getComment() );
        if ( feedback.getSentiment() != null ) {
            feedbackResponseDTO.setSentiment( feedback.getSentiment().name() );
        }
        feedbackResponseDTO.setSentimentScore( feedback.getSentimentScore() );
        feedbackResponseDTO.setCreatedAt( feedback.getCreatedAt() );

        return feedbackResponseDTO;
    }

    @Override
    public List<FeedbackResponseDTO> toDTOs(List<Feedback> feedbackList) {
        if ( feedbackList == null ) {
            return null;
        }

        List<FeedbackResponseDTO> list = new ArrayList<FeedbackResponseDTO>( feedbackList.size() );
        for ( Feedback feedback : feedbackList ) {
            list.add( toDTO( feedback ) );
        }

        return list;
    }

    private String feedbackUserUsername(Feedback feedback) {
        if ( feedback == null ) {
            return null;
        }
        User user = feedback.getUser();
        if ( user == null ) {
            return null;
        }
        String username = user.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }

    private Long feedbackEventId(Feedback feedback) {
        if ( feedback == null ) {
            return null;
        }
        Event event = feedback.getEvent();
        if ( event == null ) {
            return null;
        }
        Long id = event.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String feedbackEventTitle(Feedback feedback) {
        if ( feedback == null ) {
            return null;
        }
        Event event = feedback.getEvent();
        if ( event == null ) {
            return null;
        }
        String title = event.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }
}
