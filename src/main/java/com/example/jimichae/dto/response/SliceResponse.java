package com.example.jimichae.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SliceResponse {
	private boolean hasNext;
	private List<?> content;
	public SliceResponse(boolean hasNext, List<?> content) {
		this.hasNext = hasNext;
		this.content = content;
	}
}
