package com.example.ayush.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ChatGPTService {
    private static final Logger logger = LoggerFactory.getLogger(ChatGPTService.class);

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate template;

    private final Map<String, List<Map<String, String>>> conversationHistory = new HashMap<>();

    private static final String SYSTEM_PROMPT = """
        You are an AI assistant for the AYUSH Startup Portal, specializing in:
        1. AYUSH licensing and registration processes
        2. Document requirements and verification
        3. Fee structures and payment processes
        4. Application status tracking
        5. Eligibility criteria for different AYUSH systems
        6. Compliance requirements
        7. Traditional medicine regulations

        Note:Reply should be to the point and should not be more than 100 words.
        
        Provide accurate, helpful information about AYUSH (Ayurveda, Yoga & Naturopathy, Unani, Siddha, and Homeopathy) systems.
        If you're unsure about any specific details, please ask for clarification.
        Keep responses concise but informative, and always maintain a professional tone.
        """;

    private boolean isValidApiKey() {
        return apiKey != null && !apiKey.isEmpty() &&
               !apiKey.equals("${GEMINI_API_KEY}") &&
               !apiKey.equals("your-gemini-api-key-here");
    }

    public String generateResponse(String prompt, String sessionId) {
        try {
            // Validate API key
            if (!isValidApiKey()) {
                logger.error("Gemini API key is not configured properly");
                return "The AI service is not properly configured. Please contact the administrator to set up the Gemini API key.";
            }

            logger.debug("Generating response for prompt: {}", prompt);
            logger.debug("Using API URL: {}", apiUrl);

            // Initialize conversation history if needed
            conversationHistory.putIfAbsent(sessionId, new ArrayList<>());
            List<Map<String, String>> history = conversationHistory.get(sessionId);

            // Build the messages for Gemini (history + system prompt)
            List<Map<String, Object>> contents = new ArrayList<>();
            contents.add(Map.of("role", "user", "parts", List.of(Map.of("text", SYSTEM_PROMPT))));
            for (Map<String, String> entry : history) {
                String role = entry.get("role");
                String content = entry.get("content");
                contents.add(Map.of("role", role, "parts", List.of(Map.of("text", content))));
            }
            contents.add(Map.of("role", "user", "parts", List.of(Map.of("text", prompt))));

            Map<String, Object> request = new HashMap<>();
            request.put("contents", contents);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // Gemini API key is passed as a query param, not header, but keep for future use if needed

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            logger.debug("Sending request to Gemini API with body: {}", request);

            // Call Gemini API
            ResponseEntity<Map> responseEntity = template.exchange(apiUrl + apiKey, HttpMethod.POST, entity, Map.class);
            Map<String, Object> response = responseEntity.getBody();

            if (response != null) {
                logger.debug("Received response from Gemini API: {}", response);
                if (response.containsKey("candidates")) {
                    List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                    if (!candidates.isEmpty()) {
                        Map<String, Object> candidate = candidates.get(0);
                        Map<String, Object> content = (Map<String, Object>) candidate.get("content");
                        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                        if (parts != null && !parts.isEmpty()) {
                            String text = (String) parts.get(0).get("text");
                            // Update conversation history
                            history.add(Map.of("role", "user", "content", prompt));
                            history.add(Map.of("role", "assistant", "content", text));
                            // Limit history size
                            if (history.size() > 10) {
                                history.subList(0, 2).clear();
                            }
                            logger.debug("Successfully generated response");
                            return text;
                        }
                    }
                } else if (response.containsKey("error")) {
                    Map<String, Object> error = (Map<String, Object>) response.get("error");
                    String errorMessage = (String) error.get("message");
                    logger.error("Gemini API returned error: {}", errorMessage);
                    return "I encountered an error: " + errorMessage;
                }
            }
            logger.warn("Received unexpected response format from Gemini API: {}", response);
            return "I apologize, but I couldn't generate a response at the moment. Please try again.";
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error while calling Gemini API. Status: {}, Response: {}",
                e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return "The AI service is not properly authenticated. Please contact the administrator to verify the Gemini API key.";
            } else if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                return "The AI service is currently handling too many requests. Please try again in a few moments.";
            } else {
                return "I encountered an error processing your request. Please try again later.";
            }
        } catch (Exception e) {
            logger.error("Error while generating response: {}", e.getMessage(), e);
            return "I apologize, but I encountered an error while processing your request. Please try again later.";
        }
    }

    public void clearConversationHistory(String sessionId) {
        conversationHistory.remove(sessionId);
        logger.debug("Cleared conversation history for session: {}", sessionId);
    }
} 