package com.project.EventPlanner.features.chatbot.dto;

import lombok.Data;

@Data
public class ChatbotResponse {
    private String intent;
    private double confidence;
    private String answer;

    public ChatbotResponse() {}

    // getters and setters
    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }

    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}


