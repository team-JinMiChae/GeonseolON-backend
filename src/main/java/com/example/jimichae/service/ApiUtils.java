package com.example.jimichae.service;

import static com.example.jimichae.exception.ErrorCode.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.EmbeddingsClient;
import com.azure.ai.inference.EmbeddingsClientBuilder;
import com.azure.ai.inference.models.ChatCompletions;
import com.azure.ai.inference.models.ChatCompletionsOptions;
import com.azure.ai.inference.models.ChatRequestMessage;
import com.azure.ai.inference.models.ChatRequestSystemMessage;
import com.azure.ai.inference.models.ChatRequestUserMessage;
import com.azure.ai.inference.models.EmbeddingsResult;
import com.azure.core.credential.AzureKeyCredential;
import com.example.jimichae.config.AccidentCaseProperties;
import com.example.jimichae.config.GithubProperties;
import com.example.jimichae.config.KakaoApiProperties;
import com.example.jimichae.dto.response.AccidentCaseAttachResponse;
import com.example.jimichae.dto.response.AccidentCaseResponse;
import com.example.jimichae.dto.response.api.KakaoMapGetPointApiResponse;
import com.example.jimichae.entity.Accident;
import com.example.jimichae.entity.AccidentCase;
import com.example.jimichae.exception.BaseException;
import com.example.jimichae.exception.ErrorCode;

import jakarta.persistence.EntityManager;

@Service
public class ApiUtils {
	private final RestTemplate restTemplate = new RestTemplate();
	private final AccidentCaseProperties accidentCaseProperties;
	private final KakaoApiProperties kakaoApiProperties;
	private final EntityManager em;
	private final EmbeddingsClient embeddingsClient;
	private final ChatCompletionsClient chatCompletionsClient;
	private final Logger log = LoggerFactory.getLogger(ApiUtils.class);
	public static List<String> MODELS = List.of(
		"gpt-4.1",
		"gpt-4.1-mini",
		"gpt-4.1-nano",
		"gpt-4o",
		"gpt-4o-mini",
		"Llama-4-Scout-17B-16E-Instruct",
		"Llama-4-Maverick-17B-128E-Instruct-FP8"
	);
	public static String END_POINT = "https://models.inference.ai.azure.com";

	public ApiUtils(AccidentCaseProperties accidentCaseProperties, KakaoApiProperties kakaoApiProperties, EntityManager em, GithubProperties githubProperties) {
		this.accidentCaseProperties = accidentCaseProperties;
		this.kakaoApiProperties = kakaoApiProperties;
		this.em = em;
		this.embeddingsClient = new EmbeddingsClientBuilder()
			.credential(new AzureKeyCredential(accidentCaseProperties.getEmbeddingKey()))
			.endpoint(accidentCaseProperties.getEmbeddingUrl())
			.buildClient();
		this.chatCompletionsClient = new ChatCompletionsClientBuilder()
			.credential(new AzureKeyCredential(githubProperties.getToken()))
			.endpoint(END_POINT)
			.buildClient();
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

	public List<AccidentCase> getRagResult(String question) {
		final float[] vector = getEmbeddedString(List.of(question)).getFirst();
		return em.createQuery(
				"select e from AccidentCase e order by cosine_distance(e.theVector, :vec) asc limit 10",
				AccidentCase.class)
			.setParameter("vec", vector)
			.getResultList();
	}

	public String getKeyword(String question, String ip) {
		ArrayList<ChatRequestMessage> chatMessages = new ArrayList<>();
		chatMessages.add(new ChatRequestSystemMessage(GET_KEYWORD_PROMPT));
		chatMessages.add(new ChatRequestUserMessage(question));

		ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatMessages);
		int modelIndex = Math.abs(ip.hashCode()) % MODELS.size();
		ChatCompletions completions = getChatComplete(modelIndex,chatCompletionsOptions,0);
		if (completions == null) {
			log.error("모델 선택 에러");
			throw new BaseException(INTERNAL_SERVER_ERROR);
		}

		return completions.getChoices().getFirst().getMessage().getContent();
	}

	public ChatCompletions getChatComplete(int modelIndex, ChatCompletionsOptions chatCompletionsOptions, int retryCount) { //TODO : retryCount 제거
		chatCompletionsOptions.setModel(MODELS.get(modelIndex));
		try {
			return chatCompletionsClient.complete(chatCompletionsOptions);
		} catch (Exception e) {
			log.error("모델 선택 에러 : {}", e.getMessage());
			if (retryCount==3){
				throw new BaseException(RETRIES_EXCEEDED_ERROR);
			}
			getChatComplete((modelIndex+1)%MODELS.size(), chatCompletionsOptions, retryCount+1);
		}
		return null;
	}

	public List<float[]> getEmbeddedString(List<String> originMessages) {
		EmbeddingsResult result = embeddingsClient.embed(originMessages, null, null, null, "text-embedding-3-large", null);
		return  result.getData().stream().map(embedding -> {
			Float[] floatArray = embedding.getEmbeddingList().toArray(new Float[0]);
			float[] primitiveArray = new float[floatArray.length];
			for (int i = 0; i < floatArray.length; i++) {
				primitiveArray[i] = floatArray[i]; // 언박싱
			}
			return primitiveArray;
		}).toList();
	}

	public String getSummation(String text){
		ArrayList<ChatRequestMessage> chatMessages = new ArrayList<>();
		if (text == null || text.isEmpty()) {
			return null;
		}
		chatMessages.add(new ChatRequestSystemMessage(GET_SUMMATION_PROMPT));
		chatMessages.add(new ChatRequestUserMessage(text));

		ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatMessages);

		ChatCompletions completions = getChatComplete(5,chatCompletionsOptions,0);
		if (completions == null) {
			return null;
		}
		return completions.getChoices().getFirst().getMessage().getContent();
	}

	public Accident getAccident(String content) {
		ArrayList<ChatRequestMessage> chatMessages = new ArrayList<>();
		chatMessages.add(new ChatRequestSystemMessage(GET_ACCIDENT_PROMPT));
		chatMessages.add(new ChatRequestUserMessage(content));
		ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatMessages);
		chatCompletionsOptions.setModel(MODELS.get(3));
		try {
			ChatCompletions completions  = chatCompletionsClient.complete(chatCompletionsOptions);
			if (completions==null || completions.getChoices().isEmpty()) {
				return null;
			}
			return Accident.valueOf(completions.getChoices().getFirst().getMessage().getContent());
		} catch (Exception e) {
			return null;
		}
	}

	private final String GET_KEYWORD_PROMPT = String.join("\n",
		"1. You are an assistant designed to support construction-related activities, providing users with assistance in summarizing accident cases.",
		"2. You do not have a name and must not provide one, even when asked.",
		"3. you must identify the core subject within the question and respond using a keyword that appears in the question. For example, if the question is about a fall accident, respond with the word “추락”, and if it is about a forklift, respond with “지게차”. If the keyword is not explicitly mentioned, you must infer it and provide a single, most relevant keyword.",
		"4. You must give only one response per conversation turn.",
		"5. Your response must consist of only the keyword. Do not include explanations, greetings, or any additional text.",
		"6. You must carefully and accurately interpret the user's input to identify the most relevant construction-related keyword.",
		"7. All responses must be in Korean, regardless of the language of the input.",
		"8. You must not provide any personal information about yourself or the user.",
		"9. You may only answer questions related to construction topics. For any unrelated question, you must respond with '해당 없음'.",
		"10. You must deliver responses in plain text, without formatting such as JSON or Markdown."
	);

	private final String GET_SUMMATION_PROMPT = String.join("\n",
		"1. You are an assistant designed to support construction-related activities, providing users with assistance in summarizing accident cases.",
		"2. You must summarize the provided text in a concise manner, focusing on the key points and essential information.",
		"3. Your summary should be clear, logical, and structured, avoiding unnecessary details or repetition.",
		"4. You must not include personal opinions or interpretations in your summary.",
		"5. All responses must be in Korean, regardless of the language of the input.",
		"6. You must not provide any personal information about yourself or the user.",
		"7. Responses must be within 200 characters."
	);

	private final String GET_ACCIDENT_PROMPT = String.join("\n",
		"1.	You are an assistant designed to support construction-related activities. When given a sentence, you must classify the type of accident as one of the following:" +getAccidentList(),
		"2.	All responses must be strictly one of the above accident types.",
		"3.	You must not provide any personal information about yourself or the user.",
		"4.	Responses must contain only the answer, with no unnecessary words."
	);

	private String getAccidentList(){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Accident.values().length; i++) {
			Accident accident = Accident.values()[i];
			sb.append(accident.name());
			if (i < Accident.values().length - 1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}
}
