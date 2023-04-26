package com.furence.recsee.wooribank.script.repository.entity;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ExcelDataInfo {
	
	private XSSFWorkbook workbook;
	
	private List<XSSFSheet> sheetList;
	
	private int sheetSize;
	
	
}
