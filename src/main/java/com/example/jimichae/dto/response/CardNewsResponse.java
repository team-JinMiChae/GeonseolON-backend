package com.example.jimichae.dto.response;

import java.util.List;

public record CardNewsResponse(String title, int boardNo, List<CardNewsAttachmentResponse> attachments, String content){
}
