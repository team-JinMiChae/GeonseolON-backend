package com.example.jimichae.dto.response;

import lombok.Data;

@Data
public class WeatherApiResponse {
	private Response response;

	@Data
	public static class Response {
		private Header header;
		private Body body;
	}

	@Data
	public static class Header {
		private String resultCode;
		private String resultMsg;
	}

	@Data
	public static class Body {
		private String dataType;
		private Items items;
		private int pageNo;
		private int numOfRows;
		private int totalCount;
	}

	@Data
	public static class Items {
		private java.util.List<Item> item;
	}

	@Data
	public static class Item {
		private String baseDate;   // 예: "20250508"
		private String baseTime;   // 예: "0500"
		private String category;   // 예: "TMP", "REH" 등
		private String fcstDate;   // 예: "20250508"
		private String fcstTime;   // 예: "0600"
		private String fcstValue;  // 예: "9", "강수없음" 등
		private int nx;
		private int ny;
	}
}
