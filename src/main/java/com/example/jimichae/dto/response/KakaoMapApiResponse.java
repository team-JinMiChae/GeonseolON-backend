package com.example.jimichae.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/*
@Getter
@Setter
public class KakaoMapApiResponse {
	private Document[] documents;
	private Meta meta;

	@Getter
	@Setter
	public class Document {
		private String region_type;
		private String address_name;
		private String region_1depth_name;
		private String region_2depth_name;
		private String region_3depth_name;
		private String region_4depth_name;
		private String code;
		private double x;
		private double y;
	}

	@Getter
	@Setter
	public class Meta {
		private int total_count;
	}
}
*/
@Getter
@Setter
public class KakaoMapApiResponse {
	@JsonProperty("meta")
	private Meta meta;

	@JsonProperty("documents")
	private Document[] documents;

	@Getter
	@Setter
	public static class Document { // 정적 클래스
		@JsonProperty("region_1depth_name")
		private String region_1depth_name;

		@JsonProperty("region_2depth_name")
		private String region_2depth_name;
	}

	@Getter
	@Setter
	public static class Meta {
		@JsonProperty("total_count")
		private int total_count;
	}
}
