package com.furence.recsee.wooribank.script.util.file.excel;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.furence.recsee.common.util.StringUtil;

public class ExcelMaker {
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelMaker.class);
	
	public static File createXlsx(Map<String, Object> pojoObjectList, String filePath)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		logger.info("pojoObjectList size: " + pojoObjectList.size());
		logger.info("filePath: " + filePath);
		
		
		List<String[]> contents = (List<String[]>)pojoObjectList.get("excelList");
		String fileName = pojoObjectList.get("target").toString();
		String tempFile = null;
		try (Workbook workbook = new SXSSFWorkbook()){
			SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet();
			sheet.setColumnWidth(0, 20*256);
			sheet.setColumnWidth(1, 90*256);
			sheet.setColumnWidth(2, 15*256);
			
			// 상품정보 행 merge
			List<String[]> productInfo = (List<String[]>)pojoObjectList.get("productRow");
			List<Integer> productRowIndex = new ArrayList<Integer>();
			for(int i=0; i<contents.size(); i++) {
				if(productInfo.contains(contents.get(i))) {
					productRowIndex.add(i);
				}
			}
			CellRangeAddress[] productRange = new CellRangeAddress[productInfo.size()];
			for(int i=0; i<productRowIndex.size(); i++) {
				productRange[i] = new CellRangeAddress(productRowIndex.get(i), productRowIndex.get(i), 0, 1);
				sheet.addMergedRegion(productRange[i]);
			}
			
			// 행내한
			sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
			
			// 스텝 행 분류 - 전체스텝, 상위스텝, 하위스텝
			List<String[]> steps = (List<String[]>)pojoObjectList.get("stepRow");
			List<Integer> stepRowIndex = new ArrayList<Integer>();
			for(int i=0; i<contents.size(); i++) {
				if(steps.contains(contents.get(i))) {
					stepRowIndex.add(i);
				}
			}
			
			List<String[]> stepParents = (List<String[]>)pojoObjectList.get("stepParentRow");
			List<Integer> stepParentRowIndex = new ArrayList<Integer>();
			for(int i=0; i<contents.size(); i++) {
				if(stepParents.contains(contents.get(i))) {
					stepParentRowIndex.add(i);
				}
			}
			
			// 스텝 행 merge
			CellRangeAddress[] stepRange = new CellRangeAddress[steps.size()];
			for(int i=0; i<stepRowIndex.size(); i++) {
				stepRange[i] = new CellRangeAddress(stepRowIndex.get(i), stepRowIndex.get(i), 0, 1);
				sheet.addMergedRegion(stepRange[i]);
			}
			
			Font font = workbook.createFont();
	        font.setFontHeightInPoints((short)11);
	        font.setBold(true);
	        
	        CellStyle product_style = workbook.createCellStyle(); // 상품코드, 상품명
	        product_style.setBorderTop(BorderStyle.NONE);
	        product_style.setBorderBottom(BorderStyle.NONE);
	        product_style.setBorderLeft(BorderStyle.NONE);
	        product_style.setBorderRight(BorderStyle.NONE);
	        product_style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        product_style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
			product_style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
			product_style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
			product_style.setAlignment(HorizontalAlignment.LEFT);
			product_style.setVerticalAlignment(VerticalAlignment.CENTER);
			product_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			product_style.setFillForegroundColor(IndexedColors.WHITE.index);					
			product_style.setFont(font);
	
			CellStyle step_parent_style = workbook.createCellStyle(); // 상위스텝
			step_parent_style.setBorderTop(BorderStyle.THIN);
			step_parent_style.setBorderBottom(BorderStyle.THIN);
			step_parent_style.setBorderLeft(BorderStyle.THIN);
			step_parent_style.setBorderRight(BorderStyle.THIN);
			step_parent_style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
			step_parent_style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
			step_parent_style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
			step_parent_style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
			step_parent_style.setAlignment(HorizontalAlignment.LEFT);
			step_parent_style.setVerticalAlignment(VerticalAlignment.CENTER);
			step_parent_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			step_parent_style.setFillForegroundColor(IndexedColors.PALE_BLUE.index);
			step_parent_style.setFont(font);
			
			CellStyle remarks_title_style = workbook.createCellStyle(); // 비고 header
			remarks_title_style.setBorderTop(BorderStyle.THIN);
			remarks_title_style.setBorderBottom(BorderStyle.THIN);
			remarks_title_style.setBorderLeft(BorderStyle.THIN);
			remarks_title_style.setBorderRight(BorderStyle.THIN);
			remarks_title_style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
			remarks_title_style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
			remarks_title_style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
			remarks_title_style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
			remarks_title_style.setAlignment(HorizontalAlignment.CENTER);
			remarks_title_style.setVerticalAlignment(VerticalAlignment.CENTER);
			remarks_title_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			remarks_title_style.setFillForegroundColor(IndexedColors.PALE_BLUE.index);
			remarks_title_style.setFont(font);
			
			font = workbook.createFont();
	        font.setFontHeightInPoints((short) 10);
	        font.setBold(true);
			
	        XSSFColor stepChildColor = new XSSFColor(new Color(163, 221, 255)); // 하위스텝 custom color
	        
	        XSSFCellStyle step_child_style = (XSSFCellStyle)workbook.createCellStyle(); // 하위스텝
			step_child_style.setBorderTop(BorderStyle.THIN);
			step_child_style.setBorderBottom(BorderStyle.THIN);
			step_child_style.setBorderLeft(BorderStyle.THIN);
			step_child_style.setBorderRight(BorderStyle.THIN);
			step_child_style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
			step_child_style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
			step_child_style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
			step_child_style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
			step_child_style.setAlignment(HorizontalAlignment.LEFT);
			step_child_style.setVerticalAlignment(VerticalAlignment.CENTER);
			step_child_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			step_child_style.setFillForegroundColor(stepChildColor);
			step_child_style.setFont(font);
	        
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 10);
			font.setBold(false);
			
			XSSFColor accentColor = new XSSFColor(new Color(255, 230, 153)); // 고객답변 custom color
			
			XSSFCellStyle type_accent = (XSSFCellStyle)workbook.createCellStyle(); // 유형(방카 - 직원직접리딩, 그외 - 고객답변)
			type_accent.setBorderTop(BorderStyle.THIN);
			type_accent.setBorderBottom(BorderStyle.THIN);
			type_accent.setBorderLeft(BorderStyle.THIN);
			type_accent.setBorderRight(BorderStyle.THIN);
			type_accent.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
			type_accent.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
			type_accent.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
			type_accent.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
			type_accent.setAlignment(HorizontalAlignment.CENTER);
			type_accent.setVerticalAlignment(VerticalAlignment.CENTER);
			type_accent.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			type_accent.setFillForegroundColor(accentColor);
			type_accent.setFont(font);
			
			XSSFCellStyle text_accent = (XSSFCellStyle)workbook.createCellStyle(); // 내용(방카 - 직원직접리딩, 그외 - 고객답변)
			text_accent.setBorderTop(BorderStyle.THIN);
			text_accent.setBorderBottom(BorderStyle.THIN);
			text_accent.setBorderLeft(BorderStyle.THIN);
			text_accent.setBorderRight(BorderStyle.THIN);
			text_accent.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
			text_accent.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
			text_accent.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
			text_accent.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
			text_accent.setAlignment(HorizontalAlignment.LEFT);
			text_accent.setVerticalAlignment(VerticalAlignment.CENTER);
			text_accent.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			text_accent.setFillForegroundColor(accentColor);
			text_accent.setWrapText(true); // 개행 사용
			text_accent.setFont(font);
			
	        CellStyle type_style = workbook.createCellStyle(); // 유형
	        type_style.setBorderTop(BorderStyle.THIN);
	        type_style.setBorderBottom(BorderStyle.THIN);
	        type_style.setBorderLeft(BorderStyle.THIN);
	        type_style.setBorderRight(BorderStyle.THIN);
	        type_style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        type_style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        type_style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        type_style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        type_style.setAlignment(HorizontalAlignment.CENTER);
	        type_style.setVerticalAlignment(VerticalAlignment.CENTER);
	        type_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        type_style.setFillForegroundColor(IndexedColors.WHITE.index);
	        type_style.setFont(font);
			
			CellStyle text_style = workbook.createCellStyle(); // 내용
			text_style.setBorderTop(BorderStyle.THIN);
			text_style.setBorderBottom(BorderStyle.THIN);
			text_style.setBorderLeft(BorderStyle.THIN);
			text_style.setBorderRight(BorderStyle.THIN);
			text_style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
			text_style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
			text_style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
			text_style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
			text_style.setAlignment(HorizontalAlignment.LEFT);
			text_style.setVerticalAlignment(VerticalAlignment.CENTER);
			text_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			text_style.setFillForegroundColor(IndexedColors.WHITE.index);
			text_style.setWrapText(true); // 개행 사용
			text_style.setFont(font);
			
	        XSSFColor employeeColor = new XSSFColor(new Color(222, 176, 156)); // 직원직접리딩 employee color
	        
			XSSFCellStyle employee_text_accent = (XSSFCellStyle)workbook.createCellStyle(); // 유형(직원직접리딩)
			employee_text_accent.setBorderTop(BorderStyle.THIN);
			employee_text_accent.setBorderBottom(BorderStyle.THIN);
			employee_text_accent.setBorderLeft(BorderStyle.THIN);
			employee_text_accent.setBorderRight(BorderStyle.THIN);
			employee_text_accent.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
			employee_text_accent.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
			employee_text_accent.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
			employee_text_accent.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
			employee_text_accent.setAlignment(HorizontalAlignment.CENTER);
			employee_text_accent.setVerticalAlignment(VerticalAlignment.CENTER);
			employee_text_accent.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			employee_text_accent.setFillForegroundColor(employeeColor);
			employee_text_accent.setFont(font);
	        
	        XSSFCellStyle employee_type_accent = (XSSFCellStyle)workbook.createCellStyle(); // 내용(직원직접리딩)
	        employee_type_accent.setBorderTop(BorderStyle.THIN);
	        employee_type_accent.setBorderBottom(BorderStyle.THIN);
	        employee_type_accent.setBorderLeft(BorderStyle.THIN);
	        employee_type_accent.setBorderRight(BorderStyle.THIN);
	        employee_type_accent.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        employee_type_accent.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        employee_type_accent.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        employee_type_accent.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        employee_type_accent.setAlignment(HorizontalAlignment.LEFT);
	        employee_type_accent.setVerticalAlignment(VerticalAlignment.CENTER);
	        employee_type_accent.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        employee_type_accent.setFillForegroundColor(employeeColor);
	        employee_type_accent.setWrapText(true); // 개행 사용
	        employee_type_accent.setFont(font);
	        
	        font = workbook.createFont();
			font.setFontHeightInPoints((short) 10);
			font.setColor(IndexedColors.BLUE.index);
			font.setBold(true);
	        
			CellStyle remarks_style = workbook.createCellStyle(); // 비고
	        remarks_style.setBorderTop(BorderStyle.THIN);
	        remarks_style.setBorderBottom(BorderStyle.THIN);
	        remarks_style.setBorderLeft(BorderStyle.THIN);
	        remarks_style.setBorderRight(BorderStyle.THIN);
	        remarks_style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        remarks_style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        remarks_style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        remarks_style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        remarks_style.setAlignment(HorizontalAlignment.LEFT);
	        remarks_style.setVerticalAlignment(VerticalAlignment.CENTER);
	        remarks_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        remarks_style.setFillForegroundColor(IndexedColors.WHITE.index);
	        remarks_style.setWrapText(true); // 개행 사용
	        remarks_style.setFont(font);
	        
	        XSSFCellStyle remarks_accent = (XSSFCellStyle)workbook.createCellStyle(); // 비고(방카 - 직원직접리딩, 그외 - 고객답변)
	        remarks_accent.setBorderTop(BorderStyle.THIN);
	        remarks_accent.setBorderBottom(BorderStyle.THIN);
	        remarks_accent.setBorderLeft(BorderStyle.THIN);
	        remarks_accent.setBorderRight(BorderStyle.THIN);
	        remarks_accent.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        remarks_accent.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        remarks_accent.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        remarks_accent.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        remarks_accent.setAlignment(HorizontalAlignment.LEFT);
	        remarks_accent.setVerticalAlignment(VerticalAlignment.CENTER);
	        remarks_accent.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        remarks_accent.setFillForegroundColor(accentColor);
	        remarks_accent.setWrapText(true); // 개행 사용
	        remarks_accent.setFont(font);

	        XSSFCellStyle employee_remarks_accent = (XSSFCellStyle)workbook.createCellStyle(); // 비고(직원직접리딩)
	        employee_remarks_accent.setBorderTop(BorderStyle.THIN);
	        employee_remarks_accent.setBorderBottom(BorderStyle.THIN);
	        employee_remarks_accent.setBorderLeft(BorderStyle.THIN);
	        employee_remarks_accent.setBorderRight(BorderStyle.THIN);
	        employee_remarks_accent.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        employee_remarks_accent.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        employee_remarks_accent.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        employee_remarks_accent.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
	        employee_remarks_accent.setAlignment(HorizontalAlignment.LEFT);
	        employee_remarks_accent.setVerticalAlignment(VerticalAlignment.CENTER);
	        employee_remarks_accent.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        employee_remarks_accent.setFillForegroundColor(employeeColor);
	        employee_remarks_accent.setWrapText(true); // 개행 사용
	        employee_remarks_accent.setFont(font);
	        
	        font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setColor(IndexedColors.WHITE.index);
			font.setBold(true);
	        
	        CellStyle warn_style = workbook.createCellStyle(); // 행내한
	        warn_style.setBorderTop(BorderStyle.NONE);
	        warn_style.setBorderBottom(BorderStyle.NONE);
	        warn_style.setBorderLeft(BorderStyle.NONE);
	        warn_style.setBorderRight(BorderStyle.NONE);
	        warn_style.setTopBorderColor(IndexedColors.WHITE.index);
	        warn_style.setBottomBorderColor(IndexedColors.WHITE.index);
	        warn_style.setLeftBorderColor(IndexedColors.WHITE.index);
	        warn_style.setRightBorderColor(IndexedColors.WHITE.index);
	        warn_style.setAlignment(HorizontalAlignment.CENTER);
	        warn_style.setVerticalAlignment(VerticalAlignment.CENTER);
	        warn_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        warn_style.setFillForegroundColor(IndexedColors.DARK_BLUE.index);
	        warn_style.setFont(font);
	        
	        CellStyle[] detailStyles = new CellStyle[9];
	        detailStyles[0] = type_style;
	        detailStyles[1] = text_style;
	        detailStyles[2] = remarks_style;
	        detailStyles[3] = type_accent;
	        detailStyles[4] = text_accent;
	        detailStyles[5] = remarks_accent;
	        detailStyles[6] = employee_text_accent;
	        detailStyles[7] = employee_type_accent;
	        detailStyles[8] = employee_remarks_accent;
	        List<String> detailRowType = (List<String>)pojoObjectList.get("detailRowType");
	        
	        int detailIndex = 0;
	        
			for (int i = 0; i < contents.size(); i++) {
				Row row = sheet.createRow(i);
				if (productRowIndex.contains(i)) {
					row.setHeight((short) 500);
					returnProduct(contents.get(i), row, product_style, text_style, warn_style);
				} else if (stepRowIndex.contains(i)) {
					row.setHeight((short) 460);
					if(stepParentRowIndex.contains(i)) {
						returnStepParent(contents.get(i), row, step_parent_style, remarks_title_style);
					}else {
						returnStepChild(contents.get(i), row, step_child_style);
					}
				} else{
					row.setHeight((short) 400);
					returnDetail(contents.get(i), row, detailStyles, detailRowType.get(detailIndex++));
				}			
			}
			
			// merged region border style
			for(int i=0; i<productRange.length; i++) {
				RegionUtil.setBorderTop(BorderStyle.NONE, productRange[i], sheet);
				RegionUtil.setBorderLeft(BorderStyle.NONE, productRange[i], sheet);
				RegionUtil.setBorderRight(BorderStyle.NONE, productRange[i], sheet);
				RegionUtil.setBorderBottom(BorderStyle.NONE, productRange[i], sheet);
			}
			for(int i=0; i<stepRange.length; i++) {
				RegionUtil.setBorderTop(BorderStyle.THIN, stepRange[i], sheet);
				RegionUtil.setBorderLeft(BorderStyle.THIN, stepRange[i], sheet);
				RegionUtil.setBorderRight(BorderStyle.THIN, stepRange[i], sheet);
				RegionUtil.setBorderBottom(BorderStyle.THIN, stepRange[i], sheet);
			}
			
			String path = filePath + fileName + ".xlsx";
			logger.info("path: " + path);
	
			tempFile = getValidPath(path);
			logger.info("tempFile: " + tempFile);
			
			try(FileOutputStream fos = new FileOutputStream(tempFile)){
				workbook.write(fos);
			}catch(Exception e) {
				logger.info("workbook write error");
				logger.error("error", e);
				return null; // tempFile이 null값 등 정상적으로 write 실행되지 않은 경우 리턴
			}
		
		} catch (Exception e) {
			logger.info("workbook create error");
			logger.error("error", e);
			return null; // workbook 생성 에러 난 경우 리턴
		}

		return new File(tempFile);
	}
	
	private static Row returnProduct(String[] pojoObject, Row row, CellStyle product_style, CellStyle text_style, CellStyle warn_style) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		for(int i = 0; i < pojoObject.length; i++) {
			if(pojoObject[i] != null) {
				row.createCell(i).setCellValue(pojoObject[i]);
				if(pojoObject[i].startsWith("*")) { // 다운로드일자
					row.getCell(i).setCellStyle(text_style);
				}else if(pojoObject[i].equals("행내한")){ // 행내한 병합셀
					row.getCell(i).setCellStyle(warn_style);
				}else {
					row.getCell(i).setCellStyle(product_style);
				}
			}else {
				row.createCell(i).setCellValue("");
			}
		}
		return row;
	}
	
	private static Row returnStepParent(String[] pojoObject, Row row, CellStyle step_parent_style, CellStyle remarks_title_style) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		for(int i = 0; i < pojoObject.length; i++) {
			row.createCell(i).setCellValue(pojoObject[i]);
			if(pojoObject[i] != null) {
				if(pojoObject[i].equals("비고")) {
					row.getCell(i).setCellStyle(remarks_title_style);
				}else {
					row.getCell(i).setCellStyle(step_parent_style);
				}
			}else {
				row.createCell(i).setCellValue("");
			}
		}
		return row;
	}
	
	private static Row returnStepChild(String[] pojoObject, Row row, CellStyle step_child_style) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		for(int i = 0; i < pojoObject.length; i++) {
			if(pojoObject[i] != null) {
				row.createCell(i).setCellValue(pojoObject[i]);
				row.getCell(i).setCellStyle(step_child_style);
				
			}else {
				row.createCell(i).setCellValue("");
			}
		}
		return row;
	}
	

	private static Row returnDetail(String[] pojoObject, Row row, CellStyle[] detailStyles, String detailRowType ) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		for(int i = 0; i < pojoObject.length; i++) {
			// cell은 null일 수 없음
			if(pojoObject[i] != null) {
				row.createCell(i).setCellValue(pojoObject[i]);
			}else {
				row.createCell(i).setCellValue("");
			}
			// 스타일 구분
			if(i==0) { // 유형은 가운데 정렬  유형에 배경색 추가
				if(detailRowType.equals("G")) { //고객답변 g
					row.getCell(i).setCellStyle(detailStyles[3]); 					
				}else if(detailRowType.equals("S")) { //직원직접리딩 s
					row.getCell(i).setCellStyle(detailStyles[6]); 
				}else {
					row.getCell(i).setCellStyle(detailStyles[0]);
				}
			}else { // 내용과 비고는 좌측 정렬, 내용에 배경색 추가
				if(i!=pojoObject.length-1) {// 내용
					if(row.getCell(0).getCellStyle()==detailStyles[3]) {// 고객답변
						row.getCell(i).setCellStyle(detailStyles[4]);
					}else if(row.getCell(0).getCellStyle()==detailStyles[6]) {// 직원직접리딩
						row.getCell(i).setCellStyle(detailStyles[7]); 
					}else {
						row.getCell(i).setCellStyle(detailStyles[1]);
					}
					
				}else {// 비고란에 배경색 추가
					if(row.getCell(0).getCellStyle()==detailStyles[3]) { // 고객답변
						row.getCell(i).setCellStyle(detailStyles[5]); 
					}else if(row.getCell(0).getCellStyle()==detailStyles[6]) { // 직원직접리딩
						row.getCell(i).setCellStyle(detailStyles[8]); 
					}else {
						row.getCell(i).setCellStyle(detailStyles[2]);
					}
				}
				row.setHeight((short) -1); // 행 높이 텍스트길이에 맞춤
			}
		}

		return row;
	}
	
	public static String getValidPath(String path) throws Exception{
		
		if(path == null || "".equals(path)) {
			throw new Exception("path is null");
		}
		
		if(path.indexOf("..") > -1) {
			throw new Exception("path contains ..");
		}
		
		if(path.indexOf("//") > -1) {
			throw new Exception("path contains //");
		}
		
		return path;
		
	}
	
	
	/**
	 * String 배열의 특정 위치에 value를 할당하는 메소드
	 *  
	 *  @param arr String 배열
	 *  @param value 배열에 담을 값
	 *  @param pos value를 담을 위치 
	 */
	public static void setValue(String[] arr, String value, Integer pos) {
		if (!StringUtil.isNull(value))
			arr[pos] = value;
		else
			arr[pos] = "";
	}

}
