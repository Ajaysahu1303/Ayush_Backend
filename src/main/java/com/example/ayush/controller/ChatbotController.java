package com.example.ayush.controller;

import com.example.ayush.service.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Autowired
    private ChatGPTService chatGPTService;

    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> chat(
            @RequestBody Map<String, String> request,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String userMessage = request.get("message");
        String sessionId = (String) session.getAttribute("chatSessionId");
        
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            session.setAttribute("chatSessionId", sessionId);
        }

        try {
            String botResponse = chatGPTService.generateResponse(userMessage, sessionId);
            response.put("status", "success");
            response.put("response", botResponse);
            response.put("sessionId", sessionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to generate response: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearChat(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String sessionId = (String) session.getAttribute("chatSessionId");
        if (sessionId != null) {
            chatGPTService.clearConversationHistory(sessionId);
        }
        response.put("status", "success");
        response.put("message", "Chat history cleared successfully");
        return ResponseEntity.ok(response);
    }
} 