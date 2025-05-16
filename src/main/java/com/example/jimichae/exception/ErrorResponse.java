package com.example.jimichae.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    int code;
    String errorMessage;

    ErrorResponse(ErrorCode errorCode){
        this.code = errorCode.getCode();
        this.errorMessage = errorCode.getErrorMessage();
    }

    ErrorResponse(ErrorCode errorCode, String message){
        this.code = errorCode.getCode();
        this.errorMessage = message;
    }
}
