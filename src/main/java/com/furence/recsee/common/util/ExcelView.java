package com.furence.recsee.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileExistsException;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
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
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.util.FileCopyUtils;

public class ExcelView {

	@Autowired
	private MessageSource messageSource;
	private static final Logger logger = LoggerFactory.getLogger(ExcelView.class);
	@SuppressWarnings("unchecked")
	public static void createXlsx(Map<String, Object> pojoObjectList, String filePath, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		List<String[]> contents = (List<String[]>)pojoObjectList.get("excelList");
		String fileName = pojoObjectList.get("target").toString();

		Workbook workbook = new SXSSFWorkbook();
		SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet();
		sheet.setDefaultColumnWidth(15);

		if(pojoObjectList.get("marge")!=null)
			sheet.addMergedRegion(new CellRangeAddress(contents.size()-1,contents.size()-1,0,Integer.parseInt(pojoObjectList.get("marge").toString())));

		//sheet.createFreezePane(0,  1);

		CellStyle header_style = workbook.createCellStyle();
		header_style.setBorderTop(BorderStyle.THIN); // double lines border
		header_style.setBorderBottom(BorderStyle.THIN); // single line border
		header_style.setBorderLeft(BorderStyle.THIN);
		header_style.setBorderRight(BorderStyle.THIN);
		header_style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setAlignment(HorizontalAlignment.CENTER);
		header_style.setVerticalAlignment(VerticalAlignment.CENTER);
        header_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        header_style.setFillForegroundColor(IndexedColors.PALE_BLUE.index);

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setBold(true);
        header_style.setFont(font);

        CellStyle cell_style1 = workbook.createCellStyle();
		cell_style1.setBorderTop(BorderStyle.THIN); // double lines border
		cell_style1.setBorderBottom(BorderStyle.THIN); // single line border
		cell_style1.setBorderLeft(BorderStyle.THIN);
		cell_style1.setBorderRight(BorderStyle.THIN);
		cell_style1.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setAlignment(HorizontalAlignment.CENTER);
		cell_style1.setVerticalAlignment(VerticalAlignment.CENTER);
		cell_style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cell_style1.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.index);

        font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        cell_style1.setFont(font);

        CellStyle cell_style2 = workbook.createCellStyle();
		cell_style2.setBorderTop(BorderStyle.THIN); // double lines border
		cell_style2.setBorderBottom(BorderStyle.THIN); // single line border
		cell_style2.setBorderLeft(BorderStyle.THIN);
		cell_style2.setBorderRight(BorderStyle.THIN);
		cell_style2.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setAlignment(HorizontalAlignment.CENTER);
		cell_style2.setVerticalAlignment(VerticalAlignment.CENTER);

        font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        cell_style2.setFont(font);

		for (int i = 0; i < contents.size(); i++) {
			Row row = sheet.createRow(i);
			if (i == 0) {
				row.setHeight((short) 460);
				RowFilledWithPojoHeader(contents.get(i), row, header_style);
			} else {
				row.setHeight((short) 400);
				if(i%2 == 1) {
					RowFilledWithPojoData(contents.get(i), row, cell_style2);
				} else {
					RowFilledWithPojoData(contents.get(i), row, cell_style1);
				}
			}
		}


		String path = filePath + fileName + "_" + System.currentTimeMillis() + ".xlsx";
		
		String tempFile;
		try {
			tempFile = getValidPath(path);
		
		
		OutputStream out = null; 
		FileOutputStream fos= null;
		FileInputStream fis = null;

		File file = new File(tempFile);

		String fileNameResult = fileName;
		fileNameResult = fileNameResult.replaceAll("%0d", "");
		fileNameResult = fileNameResult.replaceAll("%0a", "");
		fileNameResult = fileNameResult.replaceAll("\r", "");
		fileNameResult = fileNameResult.replaceAll("\n", "");
		
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameResult + ".xlsx\";");
		response.setHeader("Content-Transfer-Encoding", "binary");

		try {
			
			if(tempFile != null && !"".equals(tempFile.trim())) {
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			}
			
			out = response.getOutputStream();
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);
		}finally {
			if (fis != null) {
				try {
					fis.close();
				}catch(Exception e){
					logger.error("error"+e.getMessage());
				}
			}
			if(fos!=null) {
				try {
					fos.close();
				}catch(Exception e) {
					logger.error("error"+e.getMessage());
				}
			}
			if(out!=null) {
				try {
					out.flush();
					out.close();
				}catch (IOException ex) {
					logger.error("error"+ex);
				}
			}
			file.delete();
		}
		
		} catch (Exception e) {
			logger.error("error"+e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void createXlsx_mobile_detail_statistics(Map<String, Object> pojoObjectList, String filePath, HttpServletResponse response, String statisticsUseUserColumn) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		List<String[]> contents = (List<String[]>)pojoObjectList.get("excelList");
		String fileName = pojoObjectList.get("target").toString();

		Workbook workbook = new SXSSFWorkbook();
		SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet();
		sheet.setDefaultColumnWidth(15);

		if ("N".equals(statisticsUseUserColumn)) {
			//셀병합시키기
			sheet.addMergedRegion(new CellRangeAddress(0,1,0,0)); //순번
			sheet.addMergedRegion(new CellRangeAddress(0,1,1,1)); //그룹명
			sheet.addMergedRegion(new CellRangeAddress(0,1,2,2)); //성명(아이디)
			sheet.addMergedRegion(new CellRangeAddress(0,0,3,4)); //발신건수
			sheet.addMergedRegion(new CellRangeAddress(1,1,3,4)); //발신건수
			sheet.addMergedRegion(new CellRangeAddress(0,0,5,6)); //수신건수
			sheet.addMergedRegion(new CellRangeAddress(1,1,5,6)); //수신건수
			sheet.addMergedRegion(new CellRangeAddress(0,0,7,8)); //총통화
			sheet.addMergedRegion(new CellRangeAddress(1,1,7,8)); //총통화
			sheet.addMergedRegion(new CellRangeAddress(0,1,9,9)); //발신총통화시간
			sheet.addMergedRegion(new CellRangeAddress(0,1,10,10)); //수신총통화시간
			sheet.addMergedRegion(new CellRangeAddress(0,1,11,11)); //총통화시간
			sheet.addMergedRegion(new CellRangeAddress(0,1,12,12)); //발신평균통화
			sheet.addMergedRegion(new CellRangeAddress(0,1,13,13)); //수신평균통화
			sheet.addMergedRegion(new CellRangeAddress(0,1,14,14)); //평균통화
		} else {
			//셀병합시키기
			sheet.addMergedRegion(new CellRangeAddress(0,1,0,0)); //순번
			sheet.addMergedRegion(new CellRangeAddress(0,1,1,1)); //그룹명
			sheet.addMergedRegion(new CellRangeAddress(0,1,2,2)); //성명(아이디)
			sheet.addMergedRegion(new CellRangeAddress(0,0,3,4)); //총통화
			sheet.addMergedRegion(new CellRangeAddress(1,1,3,4)); //총통화
			sheet.addMergedRegion(new CellRangeAddress(0,0,5,6)); //무료전회원
			sheet.addMergedRegion(new CellRangeAddress(1,1,5,6)); //무료전회원
			sheet.addMergedRegion(new CellRangeAddress(0,0,7,8)); //무료회원
			sheet.addMergedRegion(new CellRangeAddress(1,1,7,8)); //무료회원
			sheet.addMergedRegion(new CellRangeAddress(0,0,9,10)); //만료회원
			sheet.addMergedRegion(new CellRangeAddress(1,1,9,10)); //만료회원
			sheet.addMergedRegion(new CellRangeAddress(0,0,11,12)); //정회원
			sheet.addMergedRegion(new CellRangeAddress(1,1,11,12)); //정회원
			sheet.addMergedRegion(new CellRangeAddress(0,0,13,14)); //휴강회원
			sheet.addMergedRegion(new CellRangeAddress(1,1,13,14)); //휴강회원
			sheet.addMergedRegion(new CellRangeAddress(0,0,15,16)); //휴회회원
			sheet.addMergedRegion(new CellRangeAddress(1,1,15,16)); //휴회회원
			sheet.addMergedRegion(new CellRangeAddress(0,0,17,18)); //휴회예정
			sheet.addMergedRegion(new CellRangeAddress(1,1,17,18)); //휴회예정
			sheet.addMergedRegion(new CellRangeAddress(0,0,19,20)); //기타회원
			sheet.addMergedRegion(new CellRangeAddress(1,1,19,20)); //기타회원
			sheet.addMergedRegion(new CellRangeAddress(0,1,21,21)); //총통화시간
			sheet.addMergedRegion(new CellRangeAddress(0,1,22,22)); //평균통화
			sheet.addMergedRegion(new CellRangeAddress(0,0,23,24)); //
			sheet.addMergedRegion(new CellRangeAddress(1,1,23,24)); //
			sheet.addMergedRegion(new CellRangeAddress(0,0,25,26)); //
			sheet.addMergedRegion(new CellRangeAddress(1,1,25,26)); //
		}

		CellStyle header_style = workbook.createCellStyle();
		header_style.setBorderTop(BorderStyle.THIN); // double lines border
		header_style.setBorderBottom(BorderStyle.THIN); // single line border
		header_style.setBorderLeft(BorderStyle.THIN);
		header_style.setBorderRight(BorderStyle.THIN);
		header_style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setAlignment(HorizontalAlignment.CENTER);
		header_style.setVerticalAlignment(VerticalAlignment.CENTER);
        header_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        header_style.setFillForegroundColor(IndexedColors.PALE_BLUE.index);

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setBold(true);
        header_style.setFont(font);

        CellStyle cell_style1 = workbook.createCellStyle();
		cell_style1.setBorderTop(BorderStyle.THIN); // double lines border
		cell_style1.setBorderBottom(BorderStyle.THIN); // single line border
		cell_style1.setBorderLeft(BorderStyle.THIN);
		cell_style1.setBorderRight(BorderStyle.THIN);
		cell_style1.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setAlignment(HorizontalAlignment.CENTER);
		cell_style1.setVerticalAlignment(VerticalAlignment.CENTER);
		cell_style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cell_style1.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.index);

        font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        cell_style1.setFont(font);

        CellStyle cell_style2 = workbook.createCellStyle();
		cell_style2.setBorderTop(BorderStyle.THIN); // double lines border
		cell_style2.setBorderBottom(BorderStyle.THIN); // single line border
		cell_style2.setBorderLeft(BorderStyle.THIN);
		cell_style2.setBorderRight(BorderStyle.THIN);
		cell_style2.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setAlignment(HorizontalAlignment.CENTER);
		cell_style2.setVerticalAlignment(VerticalAlignment.CENTER);

        font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        cell_style2.setFont(font);

		for (int i = 0; i < contents.size(); i++) {
			Row row = sheet.createRow(i);
			if (i == 0 || i == 1) {
				row.setHeight((short) 460);
				RowFilledWithPojoHeader(contents.get(i), row, header_style);
			} else {
				row.setHeight((short) 400);
				if(i%2 == 1) {
					RowFilledWithPojoData(contents.get(i), row, cell_style2);
				} else {
					RowFilledWithPojoData(contents.get(i), row, cell_style1);
				}
			}
		}


		String path = filePath + fileName + "_" + System.currentTimeMillis() + ".xlsx";


		String tempFile;
		try {
			tempFile = getValidPath(path);
		
		
		FileOutputStream fos= null;
		File file = new File(tempFile);
		
		

		String fileNameResult = fileName;
		fileNameResult = fileNameResult.replaceAll("%0d", "");
		fileNameResult = fileNameResult.replaceAll("%0a", "");
		fileNameResult = fileNameResult.replaceAll("\r", "");
		fileNameResult = fileNameResult.replaceAll("\n", "");

		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameResult + ".xlsx\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		OutputStream out = null;
		FileInputStream fis = null;

		try {
			out = response.getOutputStream();
			if(tempFile != null && !"".equals(tempFile.trim())) {
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			}
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);
		}finally {
			if (fis != null) {
				try {
					fis.close();
				}catch(Exception e) {
					logger.error("error"+e.getMessage());
				}
			}
			if(fos!=null) {
				try {
						fos.close();
				}catch (IOException ex) {
					logger.error("error"+ex.getMessage());
		 		}
			}
			}

			out.flush();
			file.delete();
		} catch (Exception e) {
			logger.error("error"+e.getMessage());
		}
		
	}

	public static void createEvalXlsx(Map<String, Object> pojoObjectList, String filePath, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		List<String[]> contents = (List<String[]>)pojoObjectList.get("excelList");
		String fileName = pojoObjectList.get("target").toString();
		Workbook workbook = new SXSSFWorkbook();
		SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet();
		sheet.setDefaultColumnWidth(25);

		int bgMerge=0;
		int mgMerge=0;
		int sgMerge=0;
		int iMerge=0;
		int bgCnt=0;
		int mgCnt=0;
		int sgCnt=0;
		int iCnt=0;
		int markMerge = 0;
		int markCnt = 0;
		if(pojoObjectList.get("marge")!=null)
			sheet.addMergedRegion(new CellRangeAddress(contents.size()-1,contents.size()-1,0,Integer.parseInt(pojoObjectList.get("marge").toString())));

//		sheet.addMergedRegion(new CellRangeAddress(0,2,0,0));// 행시작, 행종료, 열시작, 열종료

		//같은 분류 코드들끼리 병합시키기 :::::::::::::::
		for(int z=2; z<contents.size(); z++) {
				String[] resultStr = contents.get(z);

				for(int y=0; y<resultStr.length; y++) {
					switch(y) {
					case 0: //대분류 병합하기
						if(resultStr[y].equals("")) {
							if(bgCnt == 0) {
								bgMerge = z-1;
							}

							bgCnt++;
						}else {
							if(z!=2 && bgCnt != 0  && bgMerge != 0) {
								sheet.addMergedRegion(new CellRangeAddress(bgMerge,(bgMerge+bgCnt),0,0));
							}
							bgCnt = 0;
							bgMerge = 0;
						}

						//마지막 행인 경우 병합할것이 남아 있으면
						if(z+1 == contents.size() && bgCnt != 0) {
							sheet.addMergedRegion(new CellRangeAddress(bgMerge,(bgMerge+bgCnt),0,0));
						}
						break;
					case 1: //중분류 병합하기
						if(resultStr[y].equals("")) {
							if(mgCnt == 0) {
								mgMerge = z-1;
							}

							mgCnt++;
						}else {
							if(z!=2 && mgCnt != 0  && mgMerge != 0) {
								sheet.addMergedRegion(new CellRangeAddress(mgMerge,(mgMerge+mgCnt),1,1));
							}
							mgCnt = 0;
							mgMerge = 0;
						}

						//마지막 행인 경우 병합할것이 남아 있으면
						if(z+1 == contents.size() && mgCnt != 0) {
							sheet.addMergedRegion(new CellRangeAddress(mgMerge,(mgMerge+mgCnt),1,1));
						}
						break;
					case 2: //소분류 병합하기
						if(resultStr[y].equals("")) {
							if(sgCnt == 0) {
								sgMerge = z-1;
							}

							sgCnt++;
						}else {
							if(z!=2 && sgCnt != 0  && sgMerge != 0) {
								sheet.addMergedRegion(new CellRangeAddress(sgMerge,(sgMerge+sgCnt),2,2));
							}
							sgCnt = 0;
							sgMerge = 0;
						}

						//마지막 행인 경우 병합할것이 남아 있으면
						if(z+1 == contents.size() && sgCnt != 0) {
							sheet.addMergedRegion(new CellRangeAddress(sgMerge,(sgMerge+sgCnt),2,2));
						}
						break;
					case 3: //상세항목 병합하기
						if(resultStr[y].equals("")) {
							if(iCnt == 0) {
								iMerge = z-1;
							}

							iCnt++;
						}else {
							if(z!=2 && iCnt != 0  && iMerge != 0) {
								sheet.addMergedRegion(new CellRangeAddress(iMerge,(iMerge+iCnt),3,3));
							}
							iCnt = 0;
							iMerge = 0;
						}

						//마지막 행인 경우 병합할것이 남아 있으면
						if(z+1 == contents.size() && iCnt != 0) {
							sheet.addMergedRegion(new CellRangeAddress(iMerge,(iMerge+iCnt),3,3));
						}
						break;

					case 6: //점수 병합하기
						if(resultStr[y].equals("")) {
							if(markCnt == 0) {
								markMerge = z-1;
							}
							markCnt++;
						}else {
							if(z!=2 && markCnt != 0  && markMerge != 0) {
								sheet.addMergedRegion(new CellRangeAddress(markMerge,(markMerge+markCnt),6,6));
							}
							markCnt = 0;
							markMerge = 0;
						}

						//마지막 행인 경우 병합할것이 남아 있으면
						if(z+1 == contents.size() && markCnt != 0) {
							sheet.addMergedRegion(new CellRangeAddress(markMerge,(markMerge+markCnt),6,6));
						}
						break;
					}


				}

				if(z > contents.size()-4){
					String a = "a";
					for(int i = 2; i<resultStr.length; i++){
						if(!StringUtil.isNull(resultStr[i]) && resultStr[i].equals(resultStr[1])){
							resultStr[i] = "";
						}
						a += resultStr[i];
					}
					if(a.equals("a")){
						sheet.addMergedRegion(new CellRangeAddress(z,z,1,resultStr.length-1));
//						CellAddress cell = (sheet.getLastRowNum());
					}
				}
		}


		CellStyle header_style = workbook.createCellStyle();
		header_style.setBorderTop(BorderStyle.THIN); // double lines border
		header_style.setBorderBottom(BorderStyle.THIN); // single line border
		header_style.setBorderLeft(BorderStyle.THIN);
		header_style.setBorderRight(BorderStyle.THIN);
		header_style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setAlignment(HorizontalAlignment.CENTER);
		header_style.setVerticalAlignment(VerticalAlignment.CENTER);
        header_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        header_style.setFillForegroundColor(IndexedColors.PALE_BLUE.index);

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setBold(true);
        header_style.setFont(font);

        CellStyle cell_style1 = workbook.createCellStyle();
		cell_style1.setBorderTop(BorderStyle.THIN); // double lines border
		cell_style1.setBorderBottom(BorderStyle.THIN); // single line border
		cell_style1.setBorderLeft(BorderStyle.THIN);
		cell_style1.setBorderRight(BorderStyle.THIN);
		cell_style1.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setAlignment(HorizontalAlignment.CENTER);
//		cell_style1.setAlignment(HorizontalAlignment.LEFT);
		cell_style1.setVerticalAlignment(VerticalAlignment.CENTER);
//		cell_style1.setVerticalAlignment(CellStyle.VERTICAL_TOP);
//		cell_style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//		cell_style1.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.index);

        font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        cell_style1.setFont(font);

        CellStyle cell_style2 = workbook.createCellStyle();
		cell_style2.setBorderTop(BorderStyle.THIN); // double lines border
		cell_style2.setBorderBottom(BorderStyle.THIN); // single line border
		cell_style2.setBorderLeft(BorderStyle.THIN);
		cell_style2.setBorderRight(BorderStyle.THIN);
		cell_style2.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setAlignment(HorizontalAlignment.CENTER);
//		cell_style2.setAlignment(HorizontalAlignment.LEFT);
		cell_style2.setVerticalAlignment(VerticalAlignment.CENTER);
//		cell_style2.setVerticalAlignment(CellStyle.VERTICAL_TOP);

        font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        cell_style2.setFont(font);

		for (int i = 0; i < contents.size(); i++) {
			Row row = sheet.createRow(i);
			if (i == 0) {
				row.setHeight((short) 460);
				RowFilledWithPojoHeader(contents.get(i), row, header_style);
			} else {
				row.setHeight((short) 400);
				if(i%2 == 1) {
					RowFilledWithPojoData(contents.get(i), row, cell_style2);
				} else {
					RowFilledWithPojoData(contents.get(i), row, cell_style1);
				}
			}
		}

		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 25000);
		sheet.setColumnWidth(4, 20000);
		sheet.setColumnWidth(5, 1000);
		sheet.setColumnWidth(6, 2000);

//		System.out.println("size == "+contents.size());
//		for (int i=0;i<6;i++) //autuSizeColumn after setColumnWidth setting!!
//		{
//			sheet.autoSizeColumn(i);
//			sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + 1000);
//		}


		String tempFile = filePath + fileName + "_" + System.currentTimeMillis() + ".xlsx";
		try {
			tempFile = TTSUtil.getrValidPath(tempFile);
		} catch (Exception e) {
			logger.error("error",e);
			
		}
		
		FileOutputStream fos= null;
		try {
			tempFile = TTSUtil.getrValidPath(tempFile);
		} catch (Exception e1) {
			tempFile ="";
		}
		
		File file = new File(tempFile);
		
		String fileNameResult = fileName;
		fileNameResult = fileNameResult.replaceAll("%0d", "");
		fileNameResult = fileNameResult.replaceAll("%0a", "");
		fileNameResult = fileNameResult.replaceAll("\r", "");
		fileNameResult = fileNameResult.replaceAll("\n", "");

		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameResult + ".xlsx\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		OutputStream out = null;
		FileInputStream fis = null;

		try {
			out = response.getOutputStream();
			if(tempFile != null && !"".equals(tempFile.trim())) {
				fos = new FileOutputStream(tempFile);
				workbook.write(fos);
			}
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);
		}catch (FileNotFoundException e) {
			logger.error("error"+e.getMessage());
		}catch (IOException e) {
			logger.error("error"+e.getMessage());
		}finally {
			if (fis != null) {
				try {
					fis.close();
				}catch(Exception e) {
					logger.error("error"+e.getMessage());
				}
			}
			if(fos!=null) {
				try {
					fos.close();
				}catch(Exception e){
					logger.error("error"+e.getMessage());
				}
			}
			if(out!=null) {
				try {
					out.flush();
					out.close();
				}catch(Exception e){
					logger.error("error"+e.getMessage());
				}
			}
			file.delete();
		}
	}
	// QA 평가 엑셀
	public static void createEvalResultXlsx(Map<String, Object> pojoObjectList, String filePath, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		List<String[]> contents = (List<String[]>)pojoObjectList.get("excelList");
		String evalDepth = pojoObjectList.get("evalDepth").toString();
		int Depth = 4;
		String fileName = pojoObjectList.get("target").toString();
		Workbook workbook = new SXSSFWorkbook();
		SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet();
		sheet.setDefaultColumnWidth(25);
		switch(evalDepth) {
		case "1":
			Depth = 3;
			break;
		case "2":
			Depth = 4;
			break;
		case "3":
			Depth = 5;
			break;
		case "4":
			Depth = 6;
			break;
		}
		int bgMerge=0;
		int mgMerge=0;
		int sgMerge=0;
		int iMerge=0;
		int memo1Merge=0;
		int memo2Merge=0;
		int bgCnt=0;
		int mgCnt=0;
		int sgCnt=0;
		int iCnt=0;
		int memo1Cnt=0;
		int memo2Cnt=0;
		int markMerge = 0;
		int markCnt = 0;
		if(pojoObjectList.get("marge")!=null)
			sheet.addMergedRegion(new CellRangeAddress(contents.size()-1,contents.size()-1,0,Integer.parseInt(pojoObjectList.get("marge").toString())));

//		sheet.addMergedRegion(new CellRangeAddress(0,2,0,0));// 행시작, 행종료, 열시작, 열종료
		int rowSize = contents.size();
		//같은 분류 코드들끼리 병합시키기 :::::::::::::::
		for(int z=2; z<rowSize; z++) {
        String[] resultStr = contents.get(z);

        	for(int y=0; y<resultStr.length; y++) {
        		if(y == 0) {
        			if(resultStr[y].equals("")) {
						if(bgCnt == 0) {
							bgMerge = z-1;
						}

						bgCnt++;
        			}else {
        				if(z!=2 && bgCnt != 0  && bgMerge != 0) {
        					sheet.addMergedRegion(new CellRangeAddress(bgMerge,(bgMerge+bgCnt),0,0));
        				}
        				bgCnt = 0;
        				bgMerge = 0;
        			}

        			//마지막 행인 경우 병합할것이 남아 있으면
        			if(z+1 == rowSize && bgCnt != 0) {
        				sheet.addMergedRegion(new CellRangeAddress(bgMerge,(bgMerge+bgCnt),0,0));
        			}
        		}else if(y  == 1) {
        			if(resultStr[y].equals("")) {
        				if(mgCnt == 0) {
        					mgMerge = z-1;
        				}

        				mgCnt++;
        			}else {
        				if(z!=2 && mgCnt != 0  && mgMerge != 0) {
        					sheet.addMergedRegion(new CellRangeAddress(mgMerge,(mgMerge+mgCnt),1,1));
        				}
        				mgCnt = 0;
        				mgMerge = 0;
        			}

        			//마지막 행인 경우 병합할것이 남아 있으면
        			if(z+1 == rowSize && mgCnt != 0) {
        				sheet.addMergedRegion(new CellRangeAddress(mgMerge,(mgMerge+mgCnt),1,1));
        			}
        		}else if(y  == 2) {
        			if(resultStr[y].equals("")) {
    					if(sgCnt == 0) {
    						sgMerge = z-1;
    					}

    					sgCnt++;
    				}else {
    					if(z!=2 && sgCnt != 0  && sgMerge != 0) {
    						sheet.addMergedRegion(new CellRangeAddress(sgMerge,(sgMerge+sgCnt),2,2));
    					}
    					sgCnt = 0;
    					sgMerge = 0;
    				}

    				//마지막 행인 경우 병합할것이 남아 있으면
    				if(z+1 == rowSize && sgCnt != 0) {
    					sheet.addMergedRegion(new CellRangeAddress(sgMerge,(sgMerge+sgCnt),2,2));
    				}
        		}else if(y  == 3) {
        			if(resultStr[1].equals("")) {
    					if(iCnt == 0) {
    						iMerge = z-1;
    					}

    					iCnt++;
    				}else {
    					if(z!=2) {
    						if(iCnt!=0) {
    							if(iMerge!=0) {
    								sheet.addMergedRegion(new CellRangeAddress(iMerge,(iMerge+iCnt),3,3));
    							}
    						}
    					}
    					iCnt = 0;
    					iMerge = 0;
    				}

    				//마지막 행인 경우 병합할것이 남아 있으면
    				if(z+1 == rowSize && iCnt != 0) {
    					sheet.addMergedRegion(new CellRangeAddress(iMerge,(iMerge+iCnt),3,3));
    				}
        		}
        		else if(y  == 4) {
        			if(resultStr[1].equals("")) {
    					if(memo1Cnt == 0) {
    						memo1Merge = z-1;
    					}

    					memo1Cnt++;
    				}else {
    					if(z!=2) {
    						if(memo1Cnt!=0) {
    							if(memo1Merge!=0) {
    								sheet.addMergedRegion(new CellRangeAddress(memo1Merge,(memo1Merge+memo1Cnt),4,4));
    							}
    						}
    					}
    					memo1Cnt = 0;
    					memo1Merge = 0;
    				}

    				//마지막 행인 경우 병합할것이 남아 있으면
    				if(z+1 == rowSize && memo1Cnt != 0) {
    					sheet.addMergedRegion(new CellRangeAddress(memo1Merge,(memo1Merge+memo1Cnt),4,4));
    				}
        		}else if(y  == 5) {
        			if(resultStr[1].equals("")) {
    					if(memo2Cnt == 0) {
    						memo2Merge = z-1;
    					}
    					memo2Cnt++;
    				}else {
    					if(z!=2) {
    						if(memo2Cnt!=0) {
    							if(memo2Merge!=0) {
    								sheet.addMergedRegion(new CellRangeAddress(memo2Merge,(memo2Merge+memo2Cnt),5,5));
    							}
    						}
    					}
    					memo2Cnt = 0;
    					memo2Merge = 0;
    				}

    				//마지막 행인 경우 병합할것이 남아 있으면
    				if(z+1 == rowSize && memo2Cnt != 0) {
    					sheet.addMergedRegion(new CellRangeAddress(memo2Merge,(memo2Merge+memo2Cnt),5,5));
    				}
        		}else if(y  == Depth) {
        			if(resultStr[y].equals("")) {
    					if(markCnt == 0) {
    						markMerge = z-1;
    					}
    					markCnt++;
    				}else {
    					if(z!=2 && markCnt != 0  && markMerge != 0) {
    						sheet.addMergedRegion(new CellRangeAddress(markMerge,(markMerge+markCnt),Depth,Depth));
    					}
    					markCnt = 0;
    					markMerge = 0;
    				}

    				//마지막 행인 경우 병합할것이 남아 있으면
    				if(z+1 == rowSize && markCnt != 0) {
    					sheet.addMergedRegion(new CellRangeAddress(markMerge,(markMerge+markCnt),Depth,Depth));
    				}
        		}
        }

	        if(z > (rowSize-4)){
	        	String a = "a";
	        	for(int i = 2; i<resultStr.length; i++){
	        		if(!StringUtil.isNull(resultStr[i]) && resultStr[i].equals(resultStr[1])){
	        			resultStr[i] = "";
	        		}
	        		a += resultStr[i];
	        	}
	
	        	if(a.equals("a")){
	        		sheet.addMergedRegion(new CellRangeAddress(z,z,1,resultStr.length-1));
	        	}
	
	        	if(z+1 == rowSize) {
	        		sheet.addMergedRegion(new CellRangeAddress(z+1,z+1,1,resultStr.length-1));
	        	}
	        }
		}
//		System.out.println("merge Set end !!!!!!!!");

		CellStyle header_style = workbook.createCellStyle();
		header_style.setBorderTop(BorderStyle.THIN); // double lines border
		header_style.setBorderBottom(BorderStyle.THIN); // single line border
		header_style.setBorderLeft(BorderStyle.THIN);
		header_style.setBorderRight(BorderStyle.THIN);
		header_style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setAlignment(HorizontalAlignment.CENTER);
		header_style.setVerticalAlignment(VerticalAlignment.CENTER);
        header_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        header_style.setFillForegroundColor(IndexedColors.PALE_BLUE.index);

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setBold(true);
        header_style.setFont(font);

        CellStyle cell_style1 = workbook.createCellStyle();
		cell_style1.setBorderTop(BorderStyle.THIN); // double lines border
		cell_style1.setBorderBottom(BorderStyle.THIN); // single line border
		cell_style1.setBorderLeft(BorderStyle.THIN);
		cell_style1.setBorderRight(BorderStyle.THIN);
		cell_style1.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setAlignment(HorizontalAlignment.CENTER);
		cell_style1.setVerticalAlignment(VerticalAlignment.CENTER);

        font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        cell_style1.setFont(font);

        CellStyle cell_style2 = workbook.createCellStyle();
		cell_style2.setBorderTop(BorderStyle.THIN); // double lines border
		cell_style2.setBorderBottom(BorderStyle.THIN); // single line border
		cell_style2.setBorderLeft(BorderStyle.THIN);
		cell_style2.setBorderRight(BorderStyle.THIN);
		cell_style2.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setAlignment(HorizontalAlignment.CENTER);
		cell_style2.setVerticalAlignment(VerticalAlignment.CENTER);

        font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        cell_style2.setFont(font);
        		
		CellStyle memo_style = workbook.createCellStyle();
		memo_style.setBorderTop(BorderStyle.THIN); // double lines border
		memo_style.setBorderBottom(BorderStyle.THIN); // single line border
		memo_style.setBorderLeft(BorderStyle.THIN);
		memo_style.setBorderRight(BorderStyle.THIN);
		memo_style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		memo_style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
		memo_style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
		memo_style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
		memo_style.setAlignment(HorizontalAlignment.LEFT);
		memo_style.setVerticalAlignment(VerticalAlignment.CENTER);
		
		font = workbook.createFont();
	    font.setFontHeightInPoints((short) 10);
	    memo_style.setFont(font);
        
        CellStyle blank_style = workbook.createCellStyle();
        blank_style.setBorderTop(BorderStyle.THIN);
        blank_style.setBorderBottom(BorderStyle.MEDIUM);
        blank_style.setBorderLeft(BorderStyle.NONE);
        blank_style.setBorderRight(BorderStyle.NONE);
        blank_style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
        blank_style.setBottomBorderColor(IndexedColors.BLACK.index);
        blank_style.setVerticalAlignment(VerticalAlignment.CENTER);
        blank_style.setFillForegroundColor(IndexedColors.WHITE.index);
        blank_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        CellStyle resultCell_style = workbook.createCellStyle();
        resultCell_style.setBorderTop(BorderStyle.MEDIUM);
        resultCell_style.setBorderBottom(BorderStyle.MEDIUM);
        resultCell_style.setBorderLeft(BorderStyle.MEDIUM);
        resultCell_style.setBorderRight(BorderStyle.MEDIUM);
        resultCell_style.setTopBorderColor(IndexedColors.BLACK.index);
        resultCell_style.setBottomBorderColor(IndexedColors.BLACK.index);
        resultCell_style.setLeftBorderColor(IndexedColors.BLACK.index);
        resultCell_style.setRightBorderColor(IndexedColors.BLACK.index);
        resultCell_style.setAlignment(HorizontalAlignment.CENTER);
        resultCell_style.setVerticalAlignment(VerticalAlignment.CENTER);
        resultCell_style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        resultCell_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        CellStyle resultCell_style2 = workbook.createCellStyle();
        resultCell_style2.setBorderTop(BorderStyle.MEDIUM);
        resultCell_style2.setBorderBottom(BorderStyle.MEDIUM);
        resultCell_style2.setBorderLeft(BorderStyle.MEDIUM);
        resultCell_style2.setBorderRight(BorderStyle.MEDIUM);
        resultCell_style2.setTopBorderColor(IndexedColors.BLACK.index);
        resultCell_style2.setBottomBorderColor(IndexedColors.BLACK.index);
        resultCell_style2.setLeftBorderColor(IndexedColors.BLACK.index);
        resultCell_style2.setRightBorderColor(IndexedColors.BLACK.index);
        resultCell_style2.setAlignment(HorizontalAlignment.CENTER);
        resultCell_style2.setVerticalAlignment(VerticalAlignment.CENTER);
        resultCell_style2.setFillForegroundColor(IndexedColors.WHITE.index);
        resultCell_style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        int size = contents.size()-3;
//        System.out.println("excel -3 size"+size);
		for (int i = 0; i < size; i++) {
			Row row = sheet.createRow(i);
			
			if (i == 0) {
				row.setHeight((short) 460);
				RowFilledWithPojoHeader(contents.get(i), row, header_style);
			} else {
				row.setHeight((short) 400);
				if(i%2 == 1) {			
					RowFilledWithPojoData(contents.get(i), row, cell_style2);
				} else {
					RowFilledWithPojoData(contents.get(i), row, cell_style1);
				}
			}		
		}
		
		//Row row = sheet.createRow(2);
		//row.getCell(3).setCellStyle(memo_style);
		
		// 대 중 소 상세 항목들 끝나면 공백 로우 하나 추가하기
		//Row row = sheet.createRow(size);
		//row.createCell(size).setCellValue(Cell.CELL_TYPE_BLANK);
		//row.getCell(size).setCellStyle(blank_style);
		
		// 공백 이후 총점/평가자의견/상담원 의견 넣기
		int startArr = size;
		for(int i=startArr; i<contents.size(); i++){
			Row addRow = sheet.createRow(i+1);

			String[] pojoObject = contents.get(i);
			for(int j = 0; j < pojoObject.length; j++) {
				if(pojoObject[j] != null) {
					if(pojoObject[j].matches("^[0-9]*?")&&!StringUtil.isNull(pojoObject[j],true)&&!pojoObject[j].substring(0, 1).equals("0")) {
						addRow.createCell(j).setCellValue((pojoObject[j]));
					}
					else {
						if(StringUtil.isNull(pojoObject[j])) {
							addRow.createCell(j).setCellValue("");
						}else {
							addRow.createCell(j).setCellValue(pojoObject[j]);
						}
					}
					
					if(i == startArr || j == 0) { // 총점 점수 열은 검은색상 바탕으로 처리
						addRow.getCell(j).setCellStyle(resultCell_style);
					}else {
						addRow.getCell(j).setCellStyle(resultCell_style2);
					}					
				}
			}
			
		}
		
		sheet.setColumnWidth(0, 5000);
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 3000);
		sheet.setColumnWidth(4, 15000);
		sheet.setColumnWidth(5, 15000);
		sheet.setColumnWidth(6, 15000);
		sheet.setColumnWidth(7, 15000);
		sheet.setColumnWidth(8, 15000);
		sheet.setColumnWidth(9, 15000);
		
//		System.out.println("size == "+contents.size());
//		for (int i=0;i<6;i++) //autuSizeColumn after setColumnWidth setting!!
//		{
//			sheet.autoSizeColumn(i);
//			sheet.setColumnWidth(i, (sheet.getColumnWidth(i)) + 1000);
//		}


		String tempFile = filePath + fileName + "_" + System.currentTimeMillis() + ".xlsx";
		try {
			tempFile = TTSUtil.getrValidPath(tempFile);
		} catch (Exception e) {
			tempFile = "";
			logger.error("error : "+e.getMessage());
		}
		FileOutputStream fos = null;
		
		File file = new File(tempFile);
		
		String fileNameResult = fileName;
		fileNameResult = fileNameResult.replaceAll("%0d", "");
		fileNameResult = fileNameResult.replaceAll("%0a", "");
		fileNameResult = fileNameResult.replaceAll("\r", "");
		fileNameResult = fileNameResult.replaceAll("\n", "");


		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameResult + ".xlsx\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		OutputStream out = null;
		FileInputStream fis = null;

		try {
			out = response.getOutputStream();
			fos = new FileOutputStream(tempFile);
			workbook.write(fos);
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);
		}catch (FileNotFoundException e) {
			logger.error("error"+e.getMessage());
			throw new FileNotFoundException();
		}catch (IOException e) {
			logger.error("error"+e.getMessage());
			throw new IOException();
		}finally {
			if (fis != null) {
					try {
						fis.close();
				} catch (IOException ex) {
					logger.error("error"+ex.getMessage());
				}
			}				
			if(fos!=null) {
				try {
					fos.close();
				}catch (Exception e) {
					logger.error("error"+e.getMessage());
				}
			}
			if(out!=null) {
				try {
				out.flush();
				out.close();
				}catch (Exception e) {
					logger.error("error"+e.getMessage());
				}
			}
			file.delete();
		}
	}

	 public static void createEvalResultAllXlsx(Map<String, Object> pojoObjectList, String filePath, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		List<String[]> contents = (List<String[]>) pojoObjectList.get("excelList");
		String fileName = pojoObjectList.get("target").toString();
		int resultSize = Integer.parseInt(pojoObjectList.get("resultSize").toString());
		int Depth = resultSize;
		int[] arrResultSize = new int[resultSize];
		int arrResultIdx = 0;
		Workbook workbook = new SXSSFWorkbook();
		SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet();
		sheet.setDefaultColumnWidth(25);

		CellStyle header_style = workbook.createCellStyle();
		header_style.setBorderTop(BorderStyle.THIN); // double lines border
		header_style.setBorderBottom(BorderStyle.THIN); // single line border
		header_style.setBorderLeft(BorderStyle.THIN);
		header_style.setBorderRight(BorderStyle.THIN);
		header_style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
		header_style.setAlignment(HorizontalAlignment.CENTER);
		header_style.setVerticalAlignment(VerticalAlignment.CENTER);
		header_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		header_style.setFillForegroundColor(IndexedColors.PALE_BLUE.index);

		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 9);
		font.setBold(true);
		header_style.setFont(font);

		CellStyle cell_style1 = workbook.createCellStyle();
		cell_style1.setBorderTop(BorderStyle.THIN); // double lines border
		cell_style1.setBorderBottom(BorderStyle.THIN); // single line border
		cell_style1.setBorderLeft(BorderStyle.THIN);
		cell_style1.setBorderRight(BorderStyle.THIN);
		cell_style1.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style1.setAlignment(HorizontalAlignment.CENTER);
		cell_style1.setVerticalAlignment(VerticalAlignment.CENTER);

		font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		cell_style1.setFont(font);

		CellStyle cell_style2 = workbook.createCellStyle();
		cell_style2.setBorderTop(BorderStyle.THIN); // double lines border
		cell_style2.setBorderBottom(BorderStyle.THIN); // single line border
		cell_style2.setBorderLeft(BorderStyle.THIN);
		cell_style2.setBorderRight(BorderStyle.THIN);
		cell_style2.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setRightBorderColor(IndexedColors.GREY_50_PERCENT.index);
		cell_style2.setAlignment(HorizontalAlignment.CENTER);
		cell_style2.setVerticalAlignment(VerticalAlignment.CENTER);
		cell_style2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		cell_style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		cell_style2.setFont(font);

		CellStyle blank_style = workbook.createCellStyle();
		blank_style.setBorderTop(BorderStyle.THIN);
		blank_style.setBorderBottom(BorderStyle.MEDIUM);
		blank_style.setBorderLeft(BorderStyle.NONE);
		blank_style.setBorderRight(BorderStyle.NONE);
		blank_style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.index);
		blank_style.setBottomBorderColor(IndexedColors.BLACK.index);
		blank_style.setVerticalAlignment(VerticalAlignment.CENTER);
		blank_style.setFillForegroundColor(IndexedColors.WHITE.index);
		blank_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		CellStyle resultCell_style = workbook.createCellStyle();
		resultCell_style.setBorderTop(BorderStyle.MEDIUM);
		resultCell_style.setBorderBottom(BorderStyle.MEDIUM);
		resultCell_style.setBorderLeft(BorderStyle.MEDIUM);
		resultCell_style.setBorderRight(BorderStyle.MEDIUM);
		resultCell_style.setTopBorderColor(IndexedColors.BLACK.index);
		resultCell_style.setBottomBorderColor(IndexedColors.BLACK.index);
		resultCell_style.setLeftBorderColor(IndexedColors.BLACK.index);
		resultCell_style.setRightBorderColor(IndexedColors.BLACK.index);
		resultCell_style.setAlignment(HorizontalAlignment.CENTER);
		resultCell_style.setVerticalAlignment(VerticalAlignment.CENTER);
		resultCell_style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		resultCell_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		CellStyle resultCell_style2 = workbook.createCellStyle();
		resultCell_style2.setBorderTop(BorderStyle.MEDIUM);
		resultCell_style2.setBorderBottom(BorderStyle.MEDIUM);
		resultCell_style2.setBorderLeft(BorderStyle.MEDIUM);
		resultCell_style2.setBorderRight(BorderStyle.MEDIUM);
		resultCell_style2.setTopBorderColor(IndexedColors.BLACK.index);
		resultCell_style2.setBottomBorderColor(IndexedColors.BLACK.index);
		resultCell_style2.setLeftBorderColor(IndexedColors.BLACK.index);
		resultCell_style2.setRightBorderColor(IndexedColors.BLACK.index);
		resultCell_style2.setAlignment(HorizontalAlignment.CENTER);
		resultCell_style2.setVerticalAlignment(VerticalAlignment.CENTER);
		resultCell_style2.setFillForegroundColor(IndexedColors.WHITE.index);
		resultCell_style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		int bgMerge = 0;
		int mgMerge = 0;
		int sgMerge = 0;
		int iMerge = 0;
		int bgCnt = 0;
		int mgCnt = 0;
		int sgCnt = 0;
		int iCnt = 0;
		int markMerge = 0;
		int markCnt = 0;
		int rowNum = 0;
		int rowNumMerge = 0;
		if (pojoObjectList.get("marge") != null)
			sheet.addMergedRegion(new CellRangeAddress(contents.size() - 1, contents.size() - 1, 0,
					Integer.parseInt(pojoObjectList.get("marge").toString())));

		// sheet.addMergedRegion(new CellRangeAddress(0,2,0,0));// 행시작, 행종료, 열시작, 열종료
		int rowSize = contents.size();
		// 같은 분류 코드들끼리 병합시키기 :::::::::::::::
		for (int z = 1; z < rowSize; z++) {
			String[] resultStr = contents.get(z);
			for (int y = 0; y < resultStr.length; y++) {
				if (y == 0) {
					if (!resultStr[y].equals("")) {
						if (rowNum == 0) {
							rowNumMerge = z;
						}
						rowNum++;
					}
					if (resultStr[4].equals("AVG")) {
						if (rowNum != 0 && rowNumMerge != 0) {
							sheet.addMergedRegion(new CellRangeAddress(rowNumMerge, (rowNumMerge + rowNum - 1), 0, 0));
							sheet.addMergedRegion(new CellRangeAddress(rowNumMerge, (rowNumMerge + rowNum - 1), 3, 3));
							sheet.addMergedRegion(new CellRangeAddress((rowNumMerge + rowNum - 1),
									(rowNumMerge + rowNum - 1), 4, 16));
							sheet.addMergedRegion(new CellRangeAddress((rowNumMerge + rowNum - 2),
									(rowNumMerge + rowNum - 2), 4, 16));
							/*
							 * sheet.addMergedRegion(new
							 * CellRangeAddress(rowNumMerge,(rowNumMerge+rowNum-1),3,3));
							 * sheet.addMergedRegion(new CellRangeAddress((z-1),(z-1),5,16));
							 * sheet.addMergedRegion(new CellRangeAddress((z-2),(z-2),5,16));
							 */
						}
						rowNum = 0;
						rowNumMerge = 0;
					}
					// 마지막 행인 경우 병합할것이 남아 있으면
					/*
					 * if(z+1 == rowSize && rowNum != 0) { if(((rowNumMerge+rowNum-1)-rowNumMerge)
					 * != 2 ) { sheet.addMergedRegion(new
					 * CellRangeAddress(rowNumMerge,(rowNumMerge+rowNum-3),16,16)); }
					 * sheet.addMergedRegion(new
					 * CellRangeAddress(rowNumMerge,(rowNumMerge+rowNum-1),0,0));
					 * sheet.addMergedRegion(new
					 * CellRangeAddress(rowNumMerge,(rowNumMerge+rowNum-1),3,3));
					 * sheet.addMergedRegion(new CellRangeAddress((z-1),(z-1),4,16));
					 * sheet.addMergedRegion(new CellRangeAddress((z-2),(z-2),4,16)); }
					 */
				}
				/*
				 * else if(y == Depth) { if(!resultStr[y].equals("")) { if(markCnt == 0) {
				 * markMerge = z; } }else { if(z!=2 && markCnt != 0 && markMerge != 0) {
				 * sheet.addMergedRegion(new
				 * CellRangeAddress(markMerge,(markMerge+markCnt),Depth,Depth)); } markCnt = 0;
				 * markMerge = 0; }
				 * 
				 * //마지막 행인 경우 병합할것이 남아 있으면 if(z+1 == rowSize && markCnt != 0) {
				 * sheet.addMergedRegion(new
				 * CellRangeAddress(markMerge,(markMerge+markCnt),Depth,Depth)); } }
				 */}

			/*
			 * if(z > (rowSize-4)){ String a = "a"; for(int i = 2; i<resultStr.length; i++){
			 * if(!StringUtil.isNull(resultStr[i]) && resultStr[i].equals(resultStr[1])){
			 * resultStr[i] = ""; } a += resultStr[i]; }
			 * 
			 * if(a.equals("a")){ sheet.addMergedRegion(new
			 * CellRangeAddress(z,z,1,resultStr.length-1)); }
			 * 
			 * if(z+1 == rowSize) { sheet.addMergedRegion(new
			 * CellRangeAddress(z+1,z+1,1,resultStr.length-1)); } }
			 */

		}
		// System.out.println("merge Set end !!!!!!!!");

		boolean isNewSheet = false;
		boolean isBlank = false;
		int size = contents.size();
		// System.out.println("excel -3 size"+size);
		for (int i = 0; i < size; i++) {
			isBlank = isNewSheet;
			Row row = sheet.createRow(i);
			if (i == 0) {
				row.setHeight((short) 460);
				RowFilledWithPojoHeader(contents.get(i), row, header_style);
			} else { // 헤더 제외 셀의 내용들 입력
				row.setHeight((short) 400);
				for (int k = 0; k < arrResultSize.length; k++) {
					if (i == arrResultSize[k]) { // 새로운 시트의 시작이면 스타일 변경
						isNewSheet = !isNewSheet;
						break;
					}
				}
				if (isBlank != isNewSheet) { // 시트 사이의 공백이면 스타일 적용 X
					continue;
				}
				if (isNewSheet) {
					RowFilledWithPojoData(contents.get(i), row, cell_style2);
				} else {
					RowFilledWithPojoData(contents.get(i), row, cell_style1);
				}
			}
		}

		// 대 중 소 상세 항목들 끝나면 공백 로우 하나 추가하기
		/*
		 * Row row = sheet.createRow(size);
		 * row.createCell(size).setCellValue(Cell.CELL_TYPE_BLANK);
		 * row.getCell(size).setCellStyle(blank_style);
		 */

		// 공백 이후 총점/평가자의견/상담원 의견 넣기
		/*
		 * int startArr = size; for(int i=startArr; i<contents.size(); i++){ Row addRow
		 * = sheet.createRow(i);
		 * 
		 * String[] pojoObject = contents.get(i); for(int j = 0; j < pojoObject.length;
		 * j++) { if(pojoObject[j] != null) {
		 * if(pojoObject[j].matches("^[0-9]*?")&&!StringUtil.isNull(pojoObject[j],true)&
		 * &!pojoObject[j].substring(0, 1).equals("0")) {
		 * addRow.createCell(j).setCellValue((pojoObject[j])); } else {
		 * if(StringUtil.isNull(pojoObject[j])) { addRow.createCell(j).setCellValue("");
		 * }else { addRow.createCell(j).setCellValue(pojoObject[j]); } }
		 * 
		 * if(i == startArr || j == 1) { // 총점 점수 열은 검은색상 바탕으로 처리
		 * addRow.getCell(j).setCellStyle(resultCell_style); }else {
		 * addRow.getCell(j).setCellStyle(resultCell_style2); } } } }
		 */

		sheet.setColumnWidth(0, 1000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 5000);
		sheet.setColumnWidth(5, 5000);
		sheet.setColumnWidth(6, 5000);
		sheet.setColumnWidth(7, 5000);
		sheet.setColumnWidth(8, 5000);
		sheet.setColumnWidth(9, 5000);
		sheet.setColumnWidth(10, 5000);
		sheet.setColumnWidth(11, 5000);
		sheet.setColumnWidth(12, 20000);
		sheet.setColumnWidth(13, 20000);
		sheet.setColumnWidth(14, 5000);
		sheet.setColumnWidth(15, 5000);
		sheet.setColumnWidth(16, 5000);

		String tempFile = filePath + fileName + "_" + System.currentTimeMillis() + ".xlsx";

		File file = new File(tempFile);

		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".xlsx\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		OutputStream out = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(tempFile);
			workbook.write(fos);
			fis = new FileInputStream(file);
			
			out = response.getOutputStream();
			FileCopyUtils.copy(fis, out);
		}catch (FileNotFoundException e) {
			logger.error("error"+e.getMessage());
		}catch (IOException e) {
			logger.error("error"+e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException ex) {
					logger.error("error"+ex.getMessage());
				}
			}
			if(fos !=null) {
				try {
					fos.close();
				}catch (Exception e) {
					logger.error("error"+e.getMessage());
				}
			}
			if(out !=null) {
				try {
					out.close();
				}catch (Exception e) {
					logger.error("error"+e.getMessage());
				}
			}
			if(workbook != null) {
				try {
					workbook.close();
				}catch (Exception e) {
					logger.error("error"+e.getMessage());
				}
			}
			file.delete();
		}
	   }


	private static Row RowFilledWithPojoHeader(String[] pojoObject, Row row, CellStyle header_style) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		for(int i = 0; i < pojoObject.length; i++) {
			if(pojoObject[i] != null) {
				row.createCell(i).setCellValue(pojoObject[i]);
				row.getCell(i).setCellStyle(header_style);
			}
		}
		return row;
	}

	private static Row RowFilledWithPojoData(String[] pojoObject, Row row, CellStyle cell_style) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		for(int i = 0; i < pojoObject.length; i++) {
			if(pojoObject[i] != null) {
				if(pojoObject[i].matches("^[0-9]*?")&&!StringUtil.isNull(pojoObject[i],true)&&!pojoObject[i].substring(0, 1).equals("0")) {										
					row.createCell(i).setCellValue((pojoObject[i]));
				}
				else {
					if(StringUtil.isNull(pojoObject[i])) {
						row.createCell(i).setCellValue("");
					}else {
						row.createCell(i).setCellValue(pojoObject[i]);
					}
				}
				row.getCell(i).setCellStyle(cell_style);
			}else {

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
			if(path.indexOf("..") > -1) {
				throw new Exception("path contains // : ");
			}
		
		
		return path;
		
	}
}
