package com.example.jimichae.repository;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.example.jimichae.dto.response.WeatherInfoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class WeatherGuideCacheRepository {
    private final RedisTemplate<String,String> redisTemplate;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private static final Logger log = LoggerFactory.getLogger(WeatherGuideCacheRepository.class);

    public WeatherGuideCacheRepository(RedisTemplate<String,String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    private final String keyPrefix = "weather_guide:";
	private final String tmxTmnPrefix = "tmx_tmn:";

	public WeatherInfoResponse getWeatherInfo(String date, String time, String region) {
		String key = keyPrefix + date + ":"+time+":" + region;
		String value = redisTemplate.opsForValue().get(key);
		if (value != null) {
			try {
				return objectMapper.readValue(value, WeatherInfoResponse.class);
			} catch (Exception e) {
				log.error("Error deserializing weather info", e);
			}
		}
		return null;
	}

	public void saveWeatherInfo(String date, String time, String region, WeatherInfoResponse weatherInfo) {
		try {
			String key = keyPrefix + date + ":"+time+":" + region;
			String json = objectMapper.writeValueAsString(weatherInfo);
			long expireTime = 1000 * 60 * 60 * 4L; // 4 hours
			redisTemplate.opsForValue().set(key, json, expireTime, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			log.error("Error serializing weather info", e);
		}
	}

	public boolean existsByWeatherInfo(String date, String time, String region) {
		String key = keyPrefix + date + ":"+time+":" + region;
		return redisTemplate.hasKey(key);
	}

	public boolean existsByTmxAndTmn(String date, String region){
		String key = tmxTmnPrefix+ date+":" + region;
		return redisTemplate.hasKey(key);
	}

	public void saveTmxAndTmn(String date, String region, String[] getTmxAndTmn) {
			String key = tmxTmnPrefix+ date+":" + region;
			String value = getTmxAndTmn[0] + "," + getTmxAndTmn[1];
			long expireTime = 1000 * 60 * 60 * 24L;
			redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.MILLISECONDS);
	}

	public String[] getRegionTMXAndTMN(String date, String region) {
		String key = tmxTmnPrefix+ date+":" + region;
		String value = redisTemplate.opsForValue().get(key);
		if (value!= null) {
			return value.split(",");
		}else {
			return null;
		}
	}
}
