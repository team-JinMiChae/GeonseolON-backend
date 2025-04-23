package com.example.jimichae.repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.example.jimichae.controller.chatbot.request.ChatRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class AccidentCaseCacheRepository {
    private final RedisTemplate<String,String> redisTemplate;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private static final Logger log = LoggerFactory.getLogger(AccidentCaseCacheRepository.class);

    public AccidentCaseCacheRepository(RedisTemplate<String,String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    private final String keyPrefix = "accidentCase:";

	public void save(String keyword, List<ChatRequest> accidentCase){
        try {
			String key = keyPrefix + keyword;
			String json = objectMapper.writeValueAsString(accidentCase);
			// 3 day
			long expireTime = 1000 * 60 * 60 * 24L * 3;
			redisTemplate.opsForValue().set(key, json, expireTime, TimeUnit.MILLISECONDS);
		} catch (JsonProcessingException e) {
			log.error("Error serializing accident case data", e);
		}
    }

    public boolean existsByKeyword(String keyword){
        String key = keyPrefix + keyword;
        return redisTemplate.hasKey(key);
    }

	public List<ChatRequest> findByKeyword(String keyword) throws JsonProcessingException {
		String key = keyPrefix + keyword;
		return objectMapper.readValue(redisTemplate.opsForValue().get(key), new TypeReference<List<ChatRequest>>() {});
	}
}
