package com.example.jimichae.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jimichae.dto.request.chatbot.ChatRequest;
import com.example.jimichae.dto.response.ChatResponse;
import com.example.jimichae.service.AccidentCaseService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/chat")
public class AccidentCaseController {
    private final AccidentCaseService accidentCaseService;

    public AccidentCaseController(AccidentCaseService accidentCaseService) {
        this.accidentCaseService = accidentCaseService;
    }

    @PostMapping
    public ChatResponse getQuestion(
        @RequestBody
        List<ChatRequest> requests,
        HttpServletRequest servletRequest
    ) {
        return accidentCaseService.getQuestion(requests, servletRequest);
    }

    /*@PostMapping
    public void saveAccidentCase(
        @RequestParam("pageNo")
        int pageNo
    ) {
        accidentCaseService.saveAccidentCase(pageNo);
    }*/ // TODO: 빼기
}
