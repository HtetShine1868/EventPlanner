package com.project.EventPlanner.features.feedback.domain.service;


import com.project.EventPlanner.features.feedback.domain.dto.SentimentRequest;
import com.project.EventPlanner.features.feedback.domain.dto.SentimentResponse;
import com.project.EventPlanner.features.feedback.domain.model.SentimentResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class SentimentClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String url = "http://localhost:5000/sentiment";

    public SentimentResult analyzeSentiment(String text) {
        Map<String, String> request = new HashMap<>();
        request.put("text", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        // Map response as List<Map> because Flask returns a JSON array
        ResponseEntity<List> response = restTemplate.postForEntity(url, entity, List.class);
        List<Map<String, Object>> body = response.getBody();

        if (body == null || body.isEmpty() || !body.get(0).containsKey("label") || !body.get(0).containsKey("score")) {
            throw new RuntimeException("Invalid response from sentiment API");
        }

        String label = body.get(0).get("label").toString();
        Double score = Double.parseDouble(body.get(0).get("score").toString());

        return new SentimentResult(label, score);
    }
}
