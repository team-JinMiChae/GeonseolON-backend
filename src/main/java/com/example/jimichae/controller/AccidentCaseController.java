package com.example.jimichae.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jimichae.controller.chatbot.request.ChatRequest;
import com.example.jimichae.controller.chatbot.response.ChatResponse;
import com.example.jimichae.service.AccidentCaseService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/chat")
public class AccidentCaseController {
    private final AccidentCaseService accidentCaseService;

    public AccidentCaseController(AccidentCaseService accidentCaseService) {
        this.accidentCaseService = accidentCaseService;
    }

    @GetMapping
    public ChatResponse getQuestion(
        @RequestBody
        List<ChatRequest> requests,
        HttpServletRequest servletRequest
    ) {
        return accidentCaseService.getQuestion(requests, servletRequest);
    }

}
