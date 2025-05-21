package com.example.jimichae.dto.response;

import lombok.Builder;

@Builder
public record CardNewsAttachmentResponse(
	String fileUrl,
	String fileName
){}
