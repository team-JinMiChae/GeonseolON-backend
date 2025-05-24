package com.example.jimichae.dto.response.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoMapGetPointApiResponse {
		private Document[] documents;
		private Meta meta;

		@Getter
		@Setter
		public static class Document {
			private String address_type;
			private String address_name;
			private RoadAddress road_address;
			private Address address;
			private String x;
			private String y;
		}

		@Getter
		@Setter
		public static class Meta {
			private int total_count;
			private String pageable_count;
			private boolean is_end;
		}
		@Getter
		@Setter
		public static class Address {
			private String address_name;
			private String region_1depth_name;
			private String region_2depth_name;
			private String region_3depth_name;
			private String region_3depth_h_name;
			private String h_code;
			private String b_code;
			private String mountain_yn;
			private String main_address_no;
			private String sub_address_no;
			private String x;
			private String y;
		}

		@Getter
		@Setter
		public static class RoadAddress {
			private String address_name;
			private String region_1depth_name;
			private String region_2depth_name;
			private String region_3depth_name;
			private String road_name;
			private String underground_yn;
			private String main_building_no;
			private String sub_building_no;
			private String building_name;
			private String zone_no;
			private String y;
			private String x;
		}
}
