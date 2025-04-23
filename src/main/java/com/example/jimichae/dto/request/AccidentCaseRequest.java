package com.example.jimichae.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccidentCaseRequest {
    private int pageNo = 1;
    private int numOfRows = 10;
    private String keyword = null;
}
