package com.project.EventPlanner.features.chatbot.api;

import com.project.EventPlanner.features.chatbot.domain.service.ChatbotService;
import com.project.EventPlanner.features.chatbot.dto.ChatbotRequest;
import com.project.EventPlanner.features.chatbot.dto.ChatbotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping
    public ResponseEntity<ChatbotResponse> chat(@RequestBody ChatbotRequest request) {
        ChatbotResponse response = chatbotService.getChatbotReply(request.getText());
        return ResponseEntity.ok(response);
    }
}