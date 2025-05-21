package com.example.jimichae.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jimichae.dto.response.CardNewsGroupResponse;
import com.example.jimichae.service.CardNewsService;

@RestController
@RequestMapping("/api/v1/card_news")
public class CardNewsController {
	private final CardNewsService cardNewsService;

	public CardNewsController(CardNewsService cardNewsService) {
		this.cardNewsService = cardNewsService;
	}
	@GetMapping
	public ResponseEntity<CardNewsGroupResponse> getCardNews(
		@RequestParam(name = "pageNo", defaultValue = "1")
		int pageNo
	) {
		return ResponseEntity.ok(cardNewsService.getCardNews(pageNo, pageNo+2));
	}
}
