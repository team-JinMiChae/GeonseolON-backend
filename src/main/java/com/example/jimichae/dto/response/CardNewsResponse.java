package com.example.jimichae.dto.response;

import java.util.List;

import com.example.jimichae.entity.Accident;

public record CardNewsResponse(String title, int boardNo, List<CardNewsAttachmentResponse> attachments, String content, Accident accident){
}
