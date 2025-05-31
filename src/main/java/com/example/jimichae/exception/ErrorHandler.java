package com.example.jimichae.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

	private final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request) {
		log.error("MethodArgumentNotValidException: {}", ex.getMessage());
		return getInvalidRequestResponse(getMessages(ex));
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
		HttpMessageNotReadableException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request) {
		String errorMessage;
		Throwable cause = ex.getCause();
		if (cause instanceof InvalidFormatException invalidFormatEx) {
			errorMessage = getPathJoinedMessage(invalidFormatEx.getPath()) + ": " + ex.getMessage();
		} else if (cause instanceof MismatchedInputException mismatchedEx) {
			errorMessage = getPathJoinedMessage(mismatchedEx.getPath()) + ": " + ex.getMessage();
		} else {
			log.error(ex.getMessage());
			errorMessage = "유효하지 않은 요청입니다";
		}
		log.error("HttpMessageNotReadableException: {}", errorMessage);
		return getInvalidRequestResponse(errorMessage);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
		HttpRequestMethodNotSupportedException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request) {
		log.error("HttpRequestMethodNotSupportedException: {}", ex.getMessage());
		return getInvalidRequestResponse(ex.getMessage());
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart(
		MissingServletRequestPartException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request) {
		log.error("MissingServletRequestPartException: {}", ex.getMessage());
		return getInvalidRequestResponse(ex.getMessage());
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
		MissingServletRequestParameterException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request) {
		log.error("MissingServletRequestParameterException: {}", ex.getMessage());
		return getInvalidRequestResponse(ex.getMessage());
	}

	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(
		MissingPathVariableException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request) {
		log.error("MissingPathVariableException: {}", ex.getMessage());
		return getInvalidRequestResponse(ex.getMessage());
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
		HttpMediaTypeNotSupportedException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request) {
		log.error("HttpMediaTypeNotSupportedException: {}", ex.getMessage());
		return getInvalidRequestResponse(ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	@Override
	protected ResponseEntity<Object> handleHandlerMethodValidationException(
		HandlerMethodValidationException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request) {
		return getInvalidRequestResponse(getValidationMessages(ex));
	}

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
		log.error("BaseException: {}", ex.getMessage());
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
			.body(new ErrorResponse(ex.getErrorCode(), ex.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		log.error("Exception: {}", ex.getMessage());
		ErrorCode internalServerErrorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		return ResponseEntity.status(internalServerErrorCode.getHttpStatus())
			.body(new ErrorResponse(internalServerErrorCode, ex.getMessage()));
	}

	private String getMessages(MethodArgumentNotValidException ex) {
		return ex.getBindingResult().getFieldErrors().stream()
			.map(error -> error.getField() + ": " + (error.getDefaultMessage() != null ? error.getDefaultMessage() : ""))
			.collect(Collectors.joining(", "));
	}

	private String getValidationMessages(HandlerMethodValidationException ex) {
		return ex.getAllErrors().stream()
			.map(error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "")
			.collect(Collectors.joining(", "));
	}

	private ResponseEntity<Object> getInvalidRequestResponse(String errorMessage) {
		return getInvalidRequestResponse(errorMessage, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<Object> getInvalidRequestResponse(String errorMessage, HttpStatus httpStatus) {
		ErrorCode invalidRequestErrorCode = ErrorCode.INVALID_REQUEST;
		return ResponseEntity.status(httpStatus)
			.body(new ErrorResponse(invalidRequestErrorCode, errorMessage));
	}

	private String getPathJoinedMessage(List<JsonMappingException.Reference> path) {
		return path.stream()
			.map(ref -> ref.getFieldName() != null ? ref.getFieldName() : "")
			.collect(Collectors.joining("."));
	}
}
