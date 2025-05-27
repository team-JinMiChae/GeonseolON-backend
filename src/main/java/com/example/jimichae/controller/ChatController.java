package com.example.jimichae.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jimichae.dto.request.chatbot.ChatRequest;
import com.example.jimichae.dto.response.ChatResponse;
import com.example.jimichae.service.ChatService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatResponse getQuestion(
        @RequestBody
        List<ChatRequest> requests,
        HttpServletRequest servletRequest
    ) {
        return chatService.getQuestion(requests, servletRequest);
    }

    @PostMapping("/save/accident_case")
    public void saveAccidentCase(
        @RequestParam(name = "pageNo", defaultValue = "1")
        int pageNo,
        @RequestParam(value = "password", required = false, defaultValue = "no")
        String password
    ) {
        chatService.saveAccidentCase(pageNo, password);
    }
}
