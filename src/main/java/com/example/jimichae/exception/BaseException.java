package com.example.jimichae.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
	ErrorCode errorCode;
	String message;

	public BaseException(ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.message = errorCode.getErrorMessage();
	}

	public BaseException(ErrorCode errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}
}
