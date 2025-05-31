package com.example.jimichae.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.jimichae.config.WeatherGuideProperties;
import com.example.jimichae.dto.GetBaseDateTime;
import com.example.jimichae.dto.request.WeatherGuide.WeatherDetailRequest;
import com.example.jimichae.dto.response.WeatherApiResponse;
import com.example.jimichae.dto.response.WeatherInfoResponse;
import com.example.jimichae.entity.Source;
import com.example.jimichae.entity.Threat;
import com.example.jimichae.entity.ThreatSafetyMeasures;
import com.example.jimichae.entity.WeatherCategory;
import com.example.jimichae.entity.WeatherSafetyMeasures;
import com.example.jimichae.entity.WeatherSafetyTip;
import com.example.jimichae.entity.WeatherSafetyTipSource;
import com.example.jimichae.entity.WeatherThreat;
import com.example.jimichae.entity.WeatherType;
import com.example.jimichae.repository.SourceRepository;
import com.example.jimichae.repository.ThreatRepository;
import com.example.jimichae.repository.ThreatSafetyMeasuresRepository;
import com.example.jimichae.repository.WeatherGuideCacheRepository;
import com.example.jimichae.repository.WeatherSafetyMeasuresRepository;
import com.example.jimichae.repository.WeatherSafetyTipRepository;
import com.example.jimichae.repository.WeatherSafetyTipSourceRepository;
import com.example.jimichae.repository.WeatherThreatRepository;
import com.example.jimichae.util.GeoUtil;
import com.example.jimichae.util.LatXLngY;

@Service
public class WeatherGuideService {
	private final WeatherGuideProperties weatherGuideProperties;
	private final RestTemplate restTemplate = new RestTemplate();
	private final WeatherGuideCacheRepository weatherGuideCacheRepository;
	private final SourceRepository sourceRepository;
	private final ThreatRepository threatRepository;
	private final WeatherSafetyMeasuresRepository weatherSafetyMeasuresRepository;
	private final WeatherSafetyTipRepository weatherSafetyTipRepository;
	private final WeatherSafetyTipSourceRepository weatherSafetyTipSourceRepository;
	private final ThreatSafetyMeasuresRepository threatSafetyMeasuresRepository;
	private final WeatherThreatRepository weatherThreatRepository;
	private final List<Integer> times = Arrays.asList(2, 5, 8, 11, 14, 17, 20, 23);

	public WeatherGuideService(WeatherGuideProperties weatherGuideProperties, WeatherGuideCacheRepository weatherGuideCacheRepository, SourceRepository sourceRepository, ThreatRepository threatRepository, WeatherSafetyMeasuresRepository weatherSafetyMeasuresRepository, WeatherSafetyTipRepository weatherSafetyTipRepository, WeatherSafetyTipSourceRepository weatherSafetyTipSourceRepository, ThreatSafetyMeasuresRepository threatSafetyMeasuresRepository, WeatherThreatRepository weatherThreatRepository) {
		this.weatherGuideProperties = weatherGuideProperties;
		this.weatherGuideCacheRepository = weatherGuideCacheRepository;
		this.sourceRepository = sourceRepository;
		this.threatRepository = threatRepository;
		this.weatherSafetyMeasuresRepository = weatherSafetyMeasuresRepository;
		this.weatherSafetyTipRepository = weatherSafetyTipRepository;
		this.weatherSafetyTipSourceRepository = weatherSafetyTipSourceRepository;
		this.threatSafetyMeasuresRepository = threatSafetyMeasuresRepository;
		this.weatherThreatRepository = weatherThreatRepository;
	}

	public WeatherInfoResponse getWeatherGuide(double latitude, double longitude, String regionName) {
		String encodedDataType = URLEncoder.encode("JSON", StandardCharsets.UTF_8);
		LocalDateTime koreaDateTime = LocalDateTime.now();
		String koreaDate = koreaDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String onTime = koreaDateTime.withMinute(0).format(DateTimeFormatter.ofPattern("HHmm"));
		if (weatherGuideCacheRepository.existsByWeatherInfo(koreaDate, onTime, regionName)) {
			return weatherGuideCacheRepository.getWeatherInfo(koreaDate, onTime, regionName);
		}
		GetBaseDateTime baseDateTime = getBaseDateTime(koreaDateTime);
		LatXLngY latXLngY = GeoUtil.convert(latitude,longitude);
		URI uri = UriComponentsBuilder.fromUriString("https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst")
			.queryParam("serviceKey", weatherGuideProperties.getApiKey())
			.queryParam("pageNo", 1)
			.queryParam("numOfRows", 60)
			.queryParam("dataType", encodedDataType)
			.queryParam("base_date", URLEncoder.encode(baseDateTime.baseDate(), StandardCharsets.UTF_8))
			.queryParam("base_time", URLEncoder.encode(baseDateTime.baseTime(), StandardCharsets.UTF_8))
			.queryParam("nx", latXLngY.x)
			.queryParam("ny", latXLngY.y)
			.build(true)
			.encode(StandardCharsets.UTF_8)
			.toUri();

		WeatherApiResponse response = restTemplate.getForObject(uri, WeatherApiResponse.class);
		if (response!=null&&response.getResponse() != null && response.getResponse().getBody() != null && response.getResponse().getBody().getItems() != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
			LocalDateTime nowDateTime = LocalDateTime.parse(baseDateTime.baseDate() + baseDateTime.baseTime(), formatter);
			Map<String,Map<String,String>> map = response.getResponse().getBody().getItems().getItem().stream().filter(item -> {
				LocalDateTime fcstDateTime = LocalDateTime.parse(item.getFcstDate() + item.getFcstTime(), formatter);
				return !fcstDateTime.isBefore(koreaDateTime.withMinute(0).withSecond(0).minusMinutes(1)) && fcstDateTime.isBefore(nowDateTime.plusHours(4).plusMinutes(30));
			})
				.collect(Collectors.groupingBy(
					item -> item.getFcstDate() + " " + item.getFcstTime(),
					Collectors.toMap(
						WeatherApiResponse.Item::getCategory,
						WeatherApiResponse.Item::getFcstValue
					)
				));
			String[] tmxAndTmn;
			if(weatherGuideCacheRepository.existsByTmxAndTmn(koreaDate,regionName)){
				tmxAndTmn = weatherGuideCacheRepository.getRegionTMXAndTMN(koreaDate,regionName);
			}else {
				tmxAndTmn = getTmxAndTmn(koreaDateTime,latXLngY);
				weatherGuideCacheRepository.saveTmxAndTmn(koreaDate,regionName, tmxAndTmn);
			}
			List<WeatherInfoResponse> list = map.entrySet().stream().map(entry -> {
					Map<String, String> timeMap = entry.getValue();
					return getWeatherInfoResponse(entry.getKey(), timeMap, tmxAndTmn);
				})
				.toList();

			WeatherInfoResponse weatherInfoResponse = list.getFirst();
			for (WeatherInfoResponse it : list) {
				if (it.getFcstTime().equals(onTime)){
					weatherInfoResponse = it;
				}
				if (it.getType() == WeatherType.NO_DATA) {
					break;
				}
				weatherGuideCacheRepository.saveWeatherInfo(it.getFcstDate(), it.getFcstTime(), regionName, it);
			}
			return weatherInfoResponse;
		}

		return WeatherInfoResponse.builder().fcstTime(koreaDateTime.withMinute(0).format(DateTimeFormatter.ofPattern("HHmm")))
			.fcstDate(koreaDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
			.type(WeatherType.NO_DATA)
			.build();
	}

	@Transactional
	public void saveWeatherGuideDetail(WeatherDetailRequest weatherDetailRequest) {
		List<Source> sources = List.of();
		if (weatherDetailRequest.sources() != null) {
			sources = weatherDetailRequest.sources().stream().map(source -> {
				if (!sourceRepository.existsByNameAndUrl(source.name(), source.url())) {
					return sourceRepository.save(new Source(null,source.name(), source.url()));
				}else {
					return sourceRepository.findByNameAndUrl(source.name(), source.url());
				}
			}).toList();
		}

		List<Threat> threats = List.of();
		if (weatherDetailRequest.threatRequests() != null) {
			threats = weatherDetailRequest.threatRequests().stream().map(threatRequest -> {
				if (!threatRepository.existsByName(threatRequest.name())) {
					Threat threat = threatRepository.save(new Threat(null, threatRequest.name()));
					if (threatRequest.safetyMeasures() != null) {
						List<String> safetyMeasures = getMeasures(threatRequest.safetyMeasures());
						safetyMeasures.forEach(safetyMeasure -> threatSafetyMeasuresRepository.save(new ThreatSafetyMeasures(null,threat, safetyMeasure)));
					}
					return threat;
				} else {
					return threatRepository.findByName(threatRequest.name());
				}
			}).toList();
		}
		WeatherSafetyTip weatherSafetyTip = weatherSafetyTipRepository.save(WeatherSafetyTip.builder().id(null)
			.simpleSafetyMeasures(weatherDetailRequest.simpleSafetyMeasures())
			.detailedSafetyMeasures(weatherDetailRequest.detailedSafetyMeasures())
			.type(weatherDetailRequest.type())
			.build());

		sources.forEach(source -> {
				weatherSafetyTipSourceRepository.save(new WeatherSafetyTipSource(null, weatherSafetyTip, source));
		});

		threats.forEach(threat -> {
			weatherThreatRepository.save(new WeatherThreat(null, weatherSafetyTip, threat));
		});

		if (weatherDetailRequest.weatherSafetyMeasures() != null) {
			List<String> weatherSafetyMeasures = getMeasures(weatherDetailRequest.weatherSafetyMeasures());
		weatherSafetyMeasures.forEach(safetyMeasure -> {
			weatherSafetyMeasuresRepository.save(new WeatherSafetyMeasures(null, weatherSafetyTip, safetyMeasure));
	});};
	}

	private GetBaseDateTime getBaseDateTime(LocalDateTime koreaDateTime) {

		int currentHour = koreaDateTime.minusMinutes(10).getHour();
		int targetHour = -1;
		LocalDate targetDate = koreaDateTime.toLocalDate();
		if (currentHour <= 2) {
			targetHour = 23;
			targetDate = targetDate.minusDays(1);
		}else {
			for (int i = times.size() - 1; i >= 0; i--) {
				if (currentHour > times.get(i)) {
					targetHour = times.get(i);
					break;
				}
			}
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

		String formattedDate = targetDate.format(formatter);
		String formattedTime = LocalTime.of(targetHour, 0).format(timeFormatter);
		return new GetBaseDateTime(formattedDate, formattedTime);
	}

	private WeatherInfoResponse getWeatherInfoResponse(String fcstDateTime,Map<String, String> timeMap, String[] tmxAndTmn) {
			WeatherType type =  WeatherType.NO_DATA;
			String pty = timeMap.get(WeatherCategory.PTY.name());
			switch (pty) {
				case "0" -> {
					if (Double.parseDouble(tmxAndTmn[0]) >= 33){
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
			String[] splitDateTime = fcstDateTime.split(" ");
		return WeatherInfoResponse.builder().pop(getWeatherCategoryValue(timeMap, WeatherCategory.POP))
			.reh(getWeatherCategoryValue(timeMap, WeatherCategory.REH))
			.tmp(getWeatherCategoryValue(timeMap, WeatherCategory.TMP))
			.tmx(tmxAndTmn[0])
			.tmn(tmxAndTmn[1])
			.fcstTime(splitDateTime[1])
			.fcstDate(splitDateTime[0])
			.type(type)
			.weatherMent(contents)
			.build();
	}

	private String getWeatherCategoryValue(Map<String, String> timeMap, WeatherCategory category) {
		return timeMap.getOrDefault(category.name(),null);
	}

	private String getContents(WeatherType type, Map<String, String> timeMap) {
	    StringBuilder advice = new StringBuilder();

	    String pcp = timeMap.getOrDefault(WeatherCategory.PCP.name(), null); // 강수량
	    String sno = timeMap.getOrDefault(WeatherCategory.SNO.name(), null); // 적설량
	    String tmp = timeMap.getOrDefault(WeatherCategory.TMP.name(), null); // 기온
	    String reh = timeMap.getOrDefault(WeatherCategory.REH.name(), null); // 습도
	    String wsd = timeMap.getOrDefault(WeatherCategory.WSD.name(), null); // 풍속

	    switch (type) {
	        case WeatherType.RAIN: {
	            advice.append(String.format("🌧️ 비가 내리고 있습니다. 시간당 강수량은 %smm입니다. 미끄럼 사고와 감전 위험에 각별히 주의하세요.", pcp != null ? pcp : "정보 없음"));
	            break;
	        }
	        case WeatherType.SNOWY: {
	            advice.append(String.format("❄️ 눈이 내릴 예정입니다. 신적설은 %scm입니다. 제설 작업과 미끄럼 사고 예방에 신경 써주세요.", sno != null ? sno : "정보 없음"));
	            break;
	        }
	        case WeatherType.LIGHTNING:
	            advice.append("⚡️ 천둥번개가 동반된 날씨입니다. 야외 고소 작업과 전기 작업은 반드시 피하시고, 안전한 장소에서 대기하세요.");
	            break;
	        case WeatherType.WIND:
	            advice.append(String.format("💨 강한 바람이 불고 있습니다. 풍속은 %sm/s입니다. 고소 장비나 크레인 사용 전 반드시 안전 점검을 실시하세요.", wsd != null ? wsd : "정보 없음"));
	            break;
	        case WeatherType.CLEAR_DAY:
	            advice.append("☀️ 맑고 쾌청한 날씨입니다.");
	            break;
	        case WeatherType.CLOUDY:
	            advice.append("☁️ 흐린 날씨로 시야 확보에 유의하세요.");
	            break;
	        case WeatherType.RAIN_SNOW:
	            advice.append(String.format("🌨️ 비와 눈이 섞여 내릴 예정입니다. 강수량은 %smm, 신적설은 %scm입니다. 바닥 미끄럼과 기계 결빙 모두에 대비하세요.", pcp != null ? pcp : "정보 없음", sno != null ? sno : "정보 없음"));
	            break;
	        case WeatherType.HOT:
	            advice.append(String.format("🔥 무더운 날씨입니다. 최고기온은 %s℃입니다. 열사병 예방을 위해 그늘에서 충분히 휴식하고, 수분을 자주 섭취하세요.", tmp != null ? tmp : "정보 없음"));
	            break;
	        case WeatherType.NO_DATA:
	            advice.append("기상 데이터가 없습니다. 현장 상황을 직접 확인하고 안전에 유의하세요.");
	            break;
	    }

	    if (tmp != null) {
	        double newTmp = Double.parseDouble(tmp);
	        if (newTmp >= 30) {
	            advice.append(String.format(" 현재 기온은 %s℃로 매우 덥습니다. 열사병 예방을 위해 그늘에서 충분히 휴식하고, 수분을 자주 섭취하세요.", tmp));
	        } else if (newTmp <= 0) {
	            advice.append(String.format(" 현재 기온은 %s℃로 매우 춥습니다. 방한복 착용과 장비 결빙 여부를 꼭 확인하세요.", tmp));
	        } else {
	            advice.append(String.format(" 현재 기온은 %s℃입니다.", tmp));
	        }
	    }

	    if (reh != null) {
	        double newReh = Double.parseDouble(reh);
	        if (newReh >= 80) {
	            advice.append(String.format(" 습도는 %s%%로 매우 높으니, 습기와 결로로 인한 장비 결빙에 주의하세요.", reh));
	        } else if (newReh <= 30) {
	            advice.append(String.format(" 습도는 %s%%로 매우 낮으니, 정전기 발생에 주의하세요.", reh));
	        } else {
	            advice.append(String.format(" 습도는 %s%%입니다.", reh));
	        }
	    }

	    if (wsd != null && type != WeatherType.WIND ) {
	        double newWsd = Double.parseDouble(wsd);
	        if (newWsd >= 4.0) {
	            advice.append(String.format(" 바람이 강하게 붑니다(풍속: %sm/s). 낙하물 및 비산물 사고에 각별히 주의하세요.", wsd));
	        }
	    }

	    advice.append(" 오늘도 안전을 최우선으로 생각하며, 장비 점검과 작업 전 안전 교육을 꼭 실시하시기 바랍니다.");
	    return advice.toString();
	}

	private String[] getTmxAndTmn(LocalDateTime dateTime, LatXLngY latXLngY) {
		String encodedDataType = URLEncoder.encode("JSON", StandardCharsets.UTF_8);
		String baseDate = dateTime.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		URI uri = UriComponentsBuilder.fromUriString("https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst")
			.queryParam("serviceKey", weatherGuideProperties.getApiKey())
			.queryParam("pageNo", 1)
			.queryParam("numOfRows", 15*16)
			.queryParam("dataType", encodedDataType)
			.queryParam("base_date", URLEncoder.encode(baseDate, StandardCharsets.UTF_8))
			.queryParam("base_time", URLEncoder.encode("2300", StandardCharsets.UTF_8))
			.queryParam("nx", latXLngY.x)
			.queryParam("ny", latXLngY.y)
			.build(true)
			.encode(StandardCharsets.UTF_8)
			.toUri();

		WeatherApiResponse response = restTemplate.getForObject(uri, WeatherApiResponse.class);

		if (response!=null&&response.getResponse() != null && response.getResponse().getBody() != null && response.getResponse().getBody().getItems() != null) {
			Map<String,Map<String,String>> map = response.getResponse().getBody().getItems().getItem().stream().collect(Collectors.groupingBy(
					WeatherApiResponse.Item::getFcstTime,
					Collectors.toMap(
						WeatherApiResponse.Item::getCategory,
						WeatherApiResponse.Item::getFcstValue
					)
				));
			String tmn = map.get("0600").get(WeatherCategory.TMN.name());
			String tmx = map.get("1500").get(WeatherCategory.TMX.name());

			return new String[]{tmx,tmn};
		}
		return new String[]{null,null};
	}

	private List<String> getMeasures(String safetyMeasures) {
		return Arrays.stream(safetyMeasures.split("\n")).filter(s -> !s.isBlank()).toList();
	}
}
