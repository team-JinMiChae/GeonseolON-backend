package com.example.jimichae.controller.chatbot.request;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SenderType {
	USER,
	BOT;

	@JsonCreator
	public static SenderType fromString(String value) {
		String upperValue = value.toUpperCase(Locale.KOREAN);
		for (SenderType type : SenderType.values()) {
			if (type.name().equals(upperValue)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown value: " + value); // TODO: 예외 처리
	}
}
