package com.example.jimichae.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode{
	// global error
	INVALID_REQUEST(HttpStatus.BAD_REQUEST, -10000, "Invalid request"),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, -10001, "Unauthorized"),
	FORBIDDEN(HttpStatus.FORBIDDEN, -10002, "Forbidden"),
	NO_SUCH_ELEMENT(HttpStatus.NOT_FOUND, -10003, "No such element"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -10004, "Internal server error"),
	EXIST_RESOURCE(HttpStatus.CONFLICT, -10005, "Exist resource"),
	NOT_FOUND_DEFAULT_RESOURCE(
		HttpStatus.INTERNAL_SERVER_ERROR,
		-10007,
		"Not found default resource"
		),
	PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, -10008, "Payload too large"),
	REDISSON_LOCK_TOO_MANY_REQUEST(HttpStatus.TOO_MANY_REQUESTS, -10009, "동시에 많은 요청이 발생했습니다. 잠시 후 다시 시도해주세요"),

	// Bad Request -10000으로 코드 통일
	SIZE_NON_POSITIVE(HttpStatus.BAD_REQUEST, -10000, "조회할 개수는 양수여야 합니다."),
	LAST_ID_NON_POSITIVE(HttpStatus.BAD_REQUEST, -10000, "마지막 ID는 양수여야 합니다."),
	LATITUDE_TOO_HIGH(HttpStatus.BAD_REQUEST, -10000, "경도는 180 이하여야 합니다."),
	LATITUDE_TOO_LOW(HttpStatus.BAD_REQUEST, -10000, "경도는 -180 이상이어야 합니다."),
	LONGITUDE_TOO_HIGH(HttpStatus.BAD_REQUEST, -10000, "위도는 180 이하여야 합니다."),
	LONGITUDE_TOO_LOW(HttpStatus.BAD_REQUEST, -10000, "위도는 -180 이상이어야 합니다."),
	IMAGES_TOO_MANY(HttpStatus.BAD_REQUEST, -10000, "이미지는 최대 3개까지 업로드할 수 있습니다."),
	INVALID_IMAGE_TYPE(HttpStatus.BAD_REQUEST, -10000, "하나 이상의 파일이 잘못 되었습니다."),
	IMAGES_SIZE_TOO_LARGE(HttpStatus.BAD_REQUEST, -10000, "하나 이상의 파일이 잘못 되었습니다."),
	INVALID_WEBP_IMAGE(HttpStatus.BAD_REQUEST, -10000, "webp 형식이 아닙니다."),
	MAX_FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, -10000, "파일 사이즈가 초과하였습니다."),

	// External API error 11000대
	REST_CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -11001, "외부 API 호출 중 에러 발생"),
	RETRIES_EXCEEDED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -11002, "외부 API 호출 재시도 횟수 초과"),

	// Institution API error 11000대
	INVALID_ADDRESS_TO_POINT(HttpStatus.BAD_REQUEST, -11000, "주소를 Point로 변환할 수 없습니다."),

	// ChatAPI error 12000대
	NOT_CLIENT_USED(HttpStatus.FORBIDDEN, -12001, "클라이언트에서 사용하지 않는 API입니다.");

	private final HttpStatus httpStatus;
	private final int code;
	private final String errorMessage;

	ErrorCode(HttpStatus httpStatus, int code, String errorMessage) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.errorMessage = errorMessage;
	}
}
