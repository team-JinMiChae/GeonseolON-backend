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


	SEARCH_KEYWORD_LENGTH(HttpStatus.BAD_REQUEST, -11000, "검색어는 1자 이상 60자 이하로 입력해주세요."),
	RADIUS_SIZE_TOO_SMALL(HttpStatus.BAD_REQUEST, -11000, "검색 반경은 500m 이상 4000m 이하로 입력해주세요."),
	INVALID_POINT_TO_ADDRESS(HttpStatus.BAD_REQUEST, -11000, "좌표를 주소로 변환할 수 없습니다."),
	POPULAR_SEARCHES_NOT_FOUND(HttpStatus.NOT_FOUND, -11003, "인기 검색어가 존재하지 않습니다."),

	// Report API error 13000대
	INVALID_LENGTH_FOOD_SPOTS_NAME(
		HttpStatus.BAD_REQUEST,
		-13000,
		"상호명은 1자 이상 20자 이하 한글, 영문, 숫자, 특수문자 여야 합니다."
		),
	INVALID_CHARACTERS_FOOD_SPOTS_NAME(
		HttpStatus.BAD_REQUEST,
		-13000,
		"설명은 1자 이상 100자 이하로 입력해주세요."
		),
	NO_CATEGORY_SELECTED(HttpStatus.BAD_REQUEST, -13000, "음식 카테고리는 최소 1개 이상 선택해야 합니다."),
	INVALID_FORMAT_OPERATION_HOURS(HttpStatus.BAD_REQUEST, -13000, "시간은 00:00부터 23:59까지의 형식이어야 합니다."),
	INVALID_CHANGE_VALUE(HttpStatus.BAD_REQUEST, -13000, "변경할 값이 없습니다."),
	NON_POSITIVE_FOOD_SPOT_ID(HttpStatus.BAD_REQUEST, -13000, "음식점 ID는 양수여야 합니다."),
	NON_POSITIVE_FOOD_SPOTS_HISTORIES_ID(HttpStatus.BAD_REQUEST, -13000, "음식점 리포트 ID는 양수여야 합니다."),
	NOT_FOUND_FOOD_CATEGORY(HttpStatus.NOT_FOUND, -13001, "해당 카테고리가 존재하지 않습니다."),
	NOT_FOUND_FOOD_SPOTS_HISTORIES(HttpStatus.NOT_FOUND, -13002, "해당 음식점 리포트가 존재하지 않습니다."),
	NOT_FOOD_SPOTS_HISTORIES_OWNER(HttpStatus.FORBIDDEN, -13003, "해당 음식점 리포트의 소유자가 아닙니다."),
	FOOD_SPOTS_ALREADY_CLOSED(HttpStatus.CONFLICT, -13004, "이미 폐업 처리가 된 음식점입니다."),
	TOO_MANY_REPORT_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, -13005, "일일 리포트 생성 횟수를 초과하였습니다."),
	UNAUTHORIZED_PHOTO_REMOVE(HttpStatus.FORBIDDEN, -13006, "사진을 삭제할 권한이 없습니다.");

	private final HttpStatus httpStatus;
	private final int code;
	private final String errorMessage;

	ErrorCode(HttpStatus httpStatus, int code, String errorMessage) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.errorMessage = errorMessage;
	}
}
