package com.example.jimichae.dto.response;

import java.util.List;

public record CardNewsGroupResponse(int pageNo, List<CardNewsResponse> cardNewsResponse) {
}
