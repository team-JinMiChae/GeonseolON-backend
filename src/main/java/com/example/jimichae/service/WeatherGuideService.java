package com.example.jimichae.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.jimichae.config.WeatherGuideProperties;
import com.example.jimichae.dto.GetBaseDateTime;
import com.example.jimichae.dto.response.WeatherApiResponse;
import com.example.jimichae.dto.response.WeatherInfoResponse;
import com.example.jimichae.entity.WeatherCategory;
import com.example.jimichae.entity.WeatherType;

@Service
public class WeatherGuideService {
	private final WeatherGuideProperties weatherGuideProperties;
	private final RestTemplate restTemplate = new RestTemplate();

	public WeatherGuideService(WeatherGuideProperties weatherGuideProperties) {
		this.weatherGuideProperties = weatherGuideProperties;
	}

	public List<WeatherInfoResponse> getWeatherGuide(int latitude, int longitude) {
		String encodedDataType = URLEncoder.encode("JSON", StandardCharsets.UTF_8);

		LocalDateTime koreaDateTime = LocalDateTime.now();
		GetBaseDateTime baseDateTime = getBaseDateTime(koreaDateTime);
		URI uri = UriComponentsBuilder.fromUriString("https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst")
			.queryParam("serviceKey", weatherGuideProperties.getApiKey())
			.queryParam("pageNo", 1)
			.queryParam("numOfRows", 100)
			.queryParam("dataType", encodedDataType)
			.queryParam("base_date", URLEncoder.encode(baseDateTime.baseDate(), StandardCharsets.UTF_8))
			.queryParam("base_time", URLEncoder.encode(baseDateTime.baseTime(), StandardCharsets.UTF_8))
			.queryParam("nx", latitude)
			.queryParam("ny", longitude)
			.build(true)
			.encode(StandardCharsets.UTF_8)
			.toUri();

		WeatherApiResponse response = restTemplate.getForObject(uri, WeatherApiResponse.class);

		if (response!=null&&response.getResponse() != null && response.getResponse().getBody() != null && response.getResponse().getBody().getItems() != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
			LocalDateTime nowDateTime = LocalDateTime.parse(baseDateTime.baseDate() + baseDateTime.baseTime(), formatter);
			Map<String,Map<String,String>> map = response.getResponse().getBody().getItems().getItem().stream().filter(item -> {
				LocalDateTime fcstDateTime = LocalDateTime.parse(item.getFcstDate() + item.getFcstTime(), formatter);
				return !fcstDateTime.isBefore(koreaDateTime.withMinute(0).withSecond(0).withNano(0)) && fcstDateTime.isBefore(nowDateTime.plusHours(3).plusMinutes(30));
			})
				.collect(Collectors.groupingBy(
					WeatherApiResponse.Item::getFcstTime,
					Collectors.toMap(
						WeatherApiResponse.Item::getCategory,
						WeatherApiResponse.Item::getFcstValue
					)
				));
			return map.entrySet().stream().map(entry -> {
					Map<String, String> timeMap = entry.getValue();
					return getWeatherInfoResponse(entry.getKey(), timeMap);
				})
				.sorted(Comparator.comparing(WeatherInfoResponse::getFcstTime))
				.toList();
		}

		return List.of();
	}

	private GetBaseDateTime getBaseDateTime(LocalDateTime koreaDateTime) {

		List<Integer> times = Arrays.asList(2, 5, 8, 11, 14, 17, 20, 23);

		int currentHour = koreaDateTime.getHour();
		int targetHour = -1;
		for (int i = times.size() - 1; i >= 0; i--) {
			if (currentHour > times.get(i)) {
				targetHour = times.get(i);
				break;
			}
		}

		LocalDate targetDate = koreaDateTime.toLocalDate();
		if (currentHour <= 2 || targetHour == -1) {
			targetHour = 23;
			targetDate = targetDate.minusDays(1);
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

		String formattedDate = targetDate.format(formatter);
		String formattedTime = LocalTime.of(targetHour, 0).format(timeFormatter);
		return new GetBaseDateTime(formattedDate, formattedTime);
	}

	private WeatherInfoResponse getWeatherInfoResponse(String fcstTime,Map<String, String> timeMap) {
			WeatherType type =  WeatherType.NO_DATA;
			String pty = timeMap.get(WeatherCategory.PTY.name());
			switch (pty) {
				case "0" -> {
					if (Integer.parseInt(timeMap.get(WeatherCategory.TMX.name())) >= 33){
						type = WeatherType.HOT;

					} else if (Double.parseDouble(timeMap.get(WeatherCategory.WSD.name()))>=4.0){
						type = WeatherType.WIND;
					} else if (timeMap.get(WeatherCategory.SKY.name()).equals("4")){
						type = WeatherType.CLOUDY;
					} else {
						type = WeatherType.CLEAR_DAY;
					}
				}
				case "1", "4" -> type = WeatherType.RAIN;
				case "2" -> type = WeatherType.RAIN_SNOW;
				case "3" -> type = WeatherType.SNOWY;
			}
			String contents = getContents(type, timeMap);
		return WeatherInfoResponse.builder().pop(getWeatherCategoryValue(timeMap, WeatherCategory.POP))
			.pcp(getWeatherCategoryValue(timeMap, WeatherCategory.PCP))
			.sno(getWeatherCategoryValue(timeMap, WeatherCategory.SNO))
			.reh(getWeatherCategoryValue(timeMap, WeatherCategory.REH))
			.tmp(getWeatherCategoryValue(timeMap, WeatherCategory.TMP))
			.tmn(getWeatherCategoryValue(timeMap, WeatherCategory.TMN))
			.tmx(getWeatherCategoryValue(timeMap, WeatherCategory.TMX))
			.wsd(getWeatherCategoryValue(timeMap, WeatherCategory.WSD))
			.fcstTime(fcstTime)
			.type(type)
			.build();
	}

	private String getWeatherCategoryValue(Map<String, String> timeMap, WeatherCategory category) {
		return timeMap.getOrDefault(category.name(),null);
	}

	private String getContents(WeatherType type, Map<String, String> timeMap) {
				StringBuilder advice = new StringBuilder("📋 오늘의 작업 현장 날씨 안내\n\n");

				String pcp = timeMap.getOrDefault(WeatherCategory.PCP.name(), null);
				String sno = timeMap.getOrDefault(WeatherCategory.SNO.name(), null);
				String tmp = timeMap.getOrDefault(WeatherCategory.TMP.name(), null);
				String reh = timeMap.getOrDefault(WeatherCategory.REH.name(), null);
				String wsd = timeMap.getOrDefault(WeatherCategory.WSD.name(), null);

				// 하늘 상태
				switch (type) {
					case WeatherType.RAIN:
						double f = Double.parseDouble(pcp);
						String rain;
						if(f < 1.0) {
							rain = "1mm 미만";
						} else if(f >= 1.0 && f < 30.0) {
							rain= "1.0~29.0mm";
						}else if(f >= 30.0&& f < 50.0) {
							rain= "30.0~50.0mm";}
						else if(f >= 50.0) {
							rain= "50.0mm이상";}
						advice.append(String.format("비가 내리며, 시간당 강수량은 %.1smm입니다.\n", pcp));
						advice.append("미끄럼 사고와 감전 위험에 유의하세요.\n");
						break;
					case WeatherType.SNOWY:
						advice.append(String.format("❄️ 눈이 %.1scm 내릴 예정입니다.\n", sno));
						advice.append("제설 작업 및 미끄럼 사고 예방 조치가 필요합니다.\n");
						break;
					case WeatherType.LIGHTNING:
						advice.append("⚡ 천둥번개가 동반된 날씨입니다.\n");
						advice.append("야외 고소 작업과 전기 작업은 최대한 피해주세요.\n");
						break;
					case WeatherType.WIND:
						advice.append(String.format("💨 강한 바람이 불고 있습니다 (풍속 %.1sm/s).\n", wsd));
						advice.append("고소 장비나 크레인 사용 전 안전 점검이 필요합니다.\n");
						break;
					case WeatherType.CLEAR_DAY:
						advice.append("☀️ 맑고 쾌청한 날씨입니다.\n");
						break;
					case WeatherType.CLOUDY:
						advice.append("☁️ 흐린 날씨로 시야 확보에 주의가 필요합니다.\n");
						break;
					case WeatherType.RAIN_SNOW:
						advice.append(String.format("🌨️ 비와 눈이 섞여 내릴 예정입니다 (강수량 %.1smm, 신적설 %.1scm).\n", pcp, sno));
						advice.append("바닥 미끄럼과 기계 결빙 모두에 대비해야 합니다.\n");
						break;
					case WeatherType.HOT:
						advice.append("🔥 무더운 날씨입니다.\n");
						break;
				}

				// 기온
				if (tmp!=null) {
					double newTmp = Double.parseDouble(tmp);
					if (newTmp >= 30) {
						advice.append(String.format("🌡️ 현재 기온은 %.1s℃로 매우 덥습니다.\n", tmp));
						advice.append("열사병 예방을 위해 그늘 휴식과 수분 섭취를 잊지 마세요.\n");
					} else if (newTmp <= 0) {
						advice.append(String.format("🌡️ 현재 기온은 %.1s℃로 매우 춥습니다.\n", tmp));
						advice.append("방한복 착용 및 장비 결빙 여부를 확인하세요.\n");
					}
				}

				// 습도
		if (reh!=null){
					double newReh = Double.parseDouble(reh);
					if (newReh >= 85) {
						advice.append(String.format("💧 습도는 %.1s%%로 높아 장비 결로와 불쾌지수 상승에 유의하세요.\n", reh));
					} else if (newReh <= 35) {
						advice.append(String.format("💧 습도는 %.1s%%로 매우 건조합니다. 화재 예방과 수분 섭취가 필요합니다.\n", reh));
					}
				}


				if (wsd >= 6) {
					advice.append(String.format("🌬️ 바람이 강하게 붑니다 (%.1sm/s). 낙하물 및 비산물 사고에 주의하세요.\n", wsd));
				}

				advice.append("\n⛑️ 오늘도 안전한 작업 되시길 바랍니다. 장비 점검과 작업 전 교육을 잊지 마세요.\n");

				return advice.toString();
			}
	}
}
