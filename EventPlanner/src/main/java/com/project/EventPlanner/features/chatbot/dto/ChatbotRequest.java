package com.project.EventPlanner.features.chatbot.dto;

import lombok.Data;

@Data
public class ChatbotRequest {
    private String text;

    public ChatbotRequest() {}
    public ChatbotRequest(String text) { this.text = text; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}

