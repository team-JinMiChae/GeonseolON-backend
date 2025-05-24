package com.example.jimichae.service;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jimichae.dto.response.GetNearestInstitutionResponse;
import com.example.jimichae.dto.response.SliceResponse;
import com.example.jimichae.entity.Institution;
import com.example.jimichae.exception.BaseException;
import com.example.jimichae.exception.ErrorCode;
import com.example.jimichae.repository.InstitutionRepository;
import com.example.jimichae.util.Constants;

@Service
public class InstitutionService {
	private final InstitutionRepository institutionRepository;
	private final GeometryFactory geometryFactory = new GeometryFactory();
	private final ApiUtils apiUtils;
	private static final Logger log = LoggerFactory.getLogger(InstitutionService.class);

	public InstitutionService(InstitutionRepository institutionRepository, ApiUtils apiUtils) {
		this.institutionRepository = institutionRepository;
		this.apiUtils = apiUtils;
	}
	
	public SliceResponse getNearestInstitution(double longitude, double latitude, Long lastId) {
		Point point = Constants.createCoordinate(longitude, latitude);
		Pageable pageable = Pageable.ofSize(10);
		Slice<GetNearestInstitutionResponse> institutions = institutionRepository.findNearestInstitution(point,pageable, lastId);
		return new SliceResponse(institutions.hasNext(), institutions.getContent());
	}

	@Transactional
	public void saveInstitutionCoordinates(String filePath) {
		try {
			Workbook workbook = new XSSFWorkbook(filePath);
			Sheet sheet = workbook.getSheetAt(0);
			workbook.close();
			ArrayList<Institution> arr = new ArrayList<>();
			for (int i = 2; i <= sheet.getLastRowNum(); i++) { // skip header
				Row row = sheet.getRow(i);
				if (row == null) {
					break;
				}
				int registrationNumber = (int)row.getCell(1).getNumericCellValue();
				if (institutionRepository.existsByRegistrationNumber(registrationNumber)) {
					log.info("already exists: " + registrationNumber);
					continue;
				}
				Institution inst = new Institution();
				String address = row.getCell(6).getStringCellValue().replace(")", ") ");
				inst.setRegistrationNumber(registrationNumber);
				inst.setName(row.getCell(2).getStringCellValue());
				inst.setPhoneNumber(row.getCell(3).getStringCellValue());
				inst.setFaxNumber(row.getCell(4).getStringCellValue());
				inst.setPostalCode(getCellValue(row.getCell(5)));
				inst.setAddress(address);
				inst.setRegion(row.getCell(7).getStringCellValue());
				Double[] xy = apiUtils.getPoint(address);
				inst.setGeom(Constants.createCoordinate(xy[0], xy[1]));
				arr.add(inst);
				log.info(String.valueOf(row.getCell(1).getNumericCellValue()));
			}
			institutionRepository.saveAll(arr);
		} catch (IOException | BaseException e){
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new BaseException(ErrorCode.REST_CLIENT_ERROR);
		}
	}

	public String getCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}
		return switch (cell.getCellType()) {
			case STRING -> cell.getStringCellValue();
			case NUMERIC -> String.valueOf((int)(cell.getNumericCellValue()));
			default -> "";
		};
	}
}
