package com.project.EventPlanner.features.chatbot.domain.service;
import com.project.EventPlanner.features.chatbot.domain.dto.ChatbotRequest;
import com.project.EventPlanner.features.chatbot.domain.dto.ChatbotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatbotService {


    @Autowired
    private RestTemplate restTemplate;


    public ChatbotResponse getChatbotReply(ChatbotRequest request) {
        String flaskUrl = "http://localhost:5000/chatbot";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatbotRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ChatbotResponse> response = restTemplate.exchange(
                flaskUrl,
                HttpMethod.POST,
                entity,
                ChatbotResponse.class
        );

        return response.getBody();
    }
}

