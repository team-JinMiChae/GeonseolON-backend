package com.example.jimichae.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.jimichae.config.AccidentCaseProperties;
import com.example.jimichae.dto.response.AccidentCaseAttachResponse;
import com.example.jimichae.dto.response.AccidentCaseResponse;
import com.example.jimichae.exception.BaseException;
import com.example.jimichae.exception.ErrorCode;

@Service
public class ApiUtils {
	private final RestTemplate restTemplate = new RestTemplate();
	private final AccidentCaseProperties accidentCaseProperties;

	public ApiUtils(AccidentCaseProperties accidentCaseProperties) {
		this.accidentCaseProperties = accidentCaseProperties;
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
}
