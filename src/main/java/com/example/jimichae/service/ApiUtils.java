package com.example.jimichae.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.jimichae.config.AccidentCaseProperties;
import com.example.jimichae.config.KakaoApiProperties;
import com.example.jimichae.dto.response.AccidentCaseAttachResponse;
import com.example.jimichae.dto.response.AccidentCaseResponse;
import com.example.jimichae.dto.response.api.KakaoMapGetPointApiResponse;
import com.example.jimichae.exception.BaseException;
import com.example.jimichae.exception.ErrorCode;

@Service
public class ApiUtils {
	private final RestTemplate restTemplate = new RestTemplate();
	private final AccidentCaseProperties accidentCaseProperties;
	private final KakaoApiProperties kakaoApiProperties;

	public ApiUtils(AccidentCaseProperties accidentCaseProperties, KakaoApiProperties kakaoApiProperties) {
		this.accidentCaseProperties = accidentCaseProperties;
		this.kakaoApiProperties = kakaoApiProperties;
	}

	public List<AccidentCaseResponse.Item> parseAccidentCaseResponse(int pageNo, int numOfRows) {
		String encodedBusiness = URLEncoder.encode("건설업", StandardCharsets.UTF_8);
		URI uri = UriComponentsBuilder.fromUriString("https://apis.data.go.kr/B552468/disaster_api01/getdisaster_api")
			.queryParam("serviceKey", accidentCaseProperties.getApiKey())
			.queryParam("pageNo", pageNo)
			.queryParam("numOfRows", numOfRows)
			.queryParam("business", encodedBusiness)
			.build(true)
			.encode(StandardCharsets.UTF_8)
			.toUri();
		try {
		AccidentCaseResponse response = restTemplate.getForObject(uri, AccidentCaseResponse.class);
		if (response != null && response.getBody() != null && response.getBody().getItems() != null){
			return response.getBody().getItems().getItem();
		}
		return List.of();
	} catch (Exception e) {
		throw new BaseException(ErrorCode.REST_CLIENT_ERROR, e.getMessage());
	}
}

	public List<AccidentCaseAttachResponse.Item> parseAccidentCaseAttachResponse(int boardNo) {
		URI uri = UriComponentsBuilder.fromUriString("https://apis.data.go.kr/B552468/disaster_attach_api/Disaster_attach_api")
			.queryParam("serviceKey", accidentCaseProperties.getApiKey())
			.queryParam("boardno", boardNo)
			.queryParam("pageNo", 1)
			.queryParam("numOfRows", 10)
			.build(true)
			.encode(StandardCharsets.UTF_8)
			.toUri();
		try {
			AccidentCaseAttachResponse response = restTemplate.getForObject(uri, AccidentCaseAttachResponse.class);
			if (response != null && response.getBody() != null && response.getBody().getItems() != null){
				return response.getBody().getItems().getItem();
			}
			return List.of();
		} catch (Exception e) {
			throw new BaseException(ErrorCode.REST_CLIENT_ERROR, e.getMessage());
		}
	}

	public Double[]  getPoint(String address) {
		String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK " + kakaoApiProperties.getRestApiKey());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		URI uri = UriComponentsBuilder.fromUriString("https://dapi.kakao.com/v2/local/search/address.json")
			.queryParam("query", encodedAddress)
			.queryParam("size", 1)
			.build(true)
			.encode(StandardCharsets.UTF_8)
			.toUri();

		KakaoMapGetPointApiResponse response = restTemplate.exchange(uri, HttpMethod.GET, entity, KakaoMapGetPointApiResponse.class).getBody();

		if (response!=null && response.getDocuments().length > 0) {
			KakaoMapGetPointApiResponse.Document document = response.getDocuments()[0];
			return new Double[]{Double.parseDouble(document.getX()), Double.parseDouble(document.getY())};
		} else {
			System.out.println(address+" : 주소를 찾을 수 없습니다.");
			return new Double[]{0.0, 0.0};
		}
	}
}
