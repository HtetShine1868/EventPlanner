package com.project.EventPlanner.features.chatbot.domain.service;
import com.project.EventPlanner.features.chatbot.dto.ChatbotRequest;
import com.project.EventPlanner.features.chatbot.dto.ChatbotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class ChatbotService {

    private static final String CHATBOT_API_URL = "http://localhost:5000/chatbot"; // Flask API URL

    @Autowired
    private RestTemplate restTemplate;

    public ChatbotResponse getChatbotReply(String userText) {
        ChatbotRequest request = new ChatbotRequest(userText);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatbotRequest> entity = new HttpEntity<>(request, headers);

        ChatbotResponse response = restTemplate.postForObject(CHATBOT_API_URL, entity, ChatbotResponse.class);

        return response;
    }
}

