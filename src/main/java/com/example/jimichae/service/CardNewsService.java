package com.example.jimichae.service;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jimichae.dto.response.AccidentCaseAttachResponse;
import com.example.jimichae.dto.response.AccidentCaseResponse;
import com.example.jimichae.dto.response.CardNewsAttachmentResponse;
import com.example.jimichae.dto.response.CardNewsGroupResponse;
import com.example.jimichae.dto.response.CardNewsResponse;
import com.example.jimichae.entity.Accident;
import com.example.jimichae.entity.AccidentType;
import com.example.jimichae.exception.BaseException;
import com.example.jimichae.exception.ErrorCode;
import com.example.jimichae.repository.AccidentTypeRepository;

@Service
public class CardNewsService {
	private final ApiUtils apiUtils;
	private final Logger log = LoggerFactory.getLogger(CardNewsService.class);
	private final AccidentTypeRepository accidentTypeRepository;

	public CardNewsService(ApiUtils apiUtils, AccidentTypeRepository accidentTypeRepository) {
		this.apiUtils = apiUtils;
		this.accidentTypeRepository = accidentTypeRepository;
	}
	@Transactional
	public CardNewsGroupResponse getCardNews(int pageNo , int limit) {
		if (pageNo==limit){
			throw new BaseException(ErrorCode.RETRIES_EXCEEDED_ERROR);
		}
		List<AccidentCaseResponse.Item> accidentCaseList = apiUtils.parseAccidentCaseResponse( pageNo, 10);
		List<CardNewsResponse> cardNewsResponseList = accidentCaseList.stream()
			.map(item -> {
				List<AccidentCaseAttachResponse.Item> accidentCaseAttachList = apiUtils.parseAccidentCaseAttachResponse(item.getBoardno());
				List<CardNewsAttachmentResponse> attachmentResponses =accidentCaseAttachList.stream().map(it->new CardNewsAttachmentResponse(it.getFilepath())).toList();
				if (!attachmentResponses.isEmpty()){
					AccidentType accidentType = accidentTypeRepository.findByBoardNo(item.getBoardno());
					Accident accident;
					if(accidentType!=null) {
						accident = accidentType.getAccident();
					}else {
						accident =apiUtils.getAccident(item.getKeyword());
						if (accident == null) {
							accident = Accident.OTHER;
						}
						accidentTypeRepository.save(new AccidentType(item.getBoardno(), accident));
					}
					return new CardNewsResponse(item.getKeyword(), item.getBoardno(), attachmentResponses, item.getContents(), accident);
				}else {
					return null;
				}
			}).filter(Objects::nonNull)
			.toList();
		if (cardNewsResponseList.isEmpty()) {
			return getCardNews(pageNo+1, limit);
		}
		return new CardNewsGroupResponse(pageNo, cardNewsResponseList);
	}
}
