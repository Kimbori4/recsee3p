package com.furence.recsee.wooribank.script.util.file.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.furence.recsee.wooribank.facerecording.model.BkProductAndScriptStepDetail;
import com.furence.recsee.wooribank.facerecording.model.ProductListVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo;
import com.furence.recsee.wooribank.script.repository.entity.ExcelDataInfo;
import com.furence.recsee.wooribank.script.repository.entity.InsuranceAndProductList;
import com.furence.recsee.wooribank.script.repository.entity.ProductCharacterDetail;
import com.furence.recsee.wooribank.script.repository.entity.ProductFundDetail;
import com.initech.inisafenet.iniplugin.log.Logger;

public class ExcelUtil {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
	public static ExcelDataInfo readExcel(MultipartFile excelFile , String[] header) throws Exception{
		InputStream inputStream =null;
		try {
			inputStream = excelFile.getInputStream();
		}catch (Exception e) {
			logger.error("error : {}",e.getMessage());
		}finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				}catch (Exception e) {
					logger.error("error : {}",e.getMessage());
				}
			}
		}
		OPCPackage opcPackage = OPCPackage.open(inputStream);
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String, Object> data = null;
		
		//워크북
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(opcPackage);
		}catch (Exception e) {
			logger.error("error : {}",e.getMessage());
		}finally {
			if(opcPackage != null) {
				try { opcPackage.close(); } catch (Exception e) {logger.error("error : {}",e.getMessage());	}
			}
			if(workbook != null) {
				try { workbook.close(); } catch (Exception e) {logger.error("error : {}",e.getMessage());	}
			}
		}
		
		//행
		XSSFRow row = null;
		
		//셇
		XSSFCell cell = null;
		
		//방카엑셀 기본 시트 개수
//		int sheetNum = workbook.getNumberOfSheets();
//		if(sheetNum != 4) {
//			throw new Exception("Excel sheetNum :"+sheetNum);
//		}
		//Sheet 수만큼 개별 루프
		List<XSSFSheet> sheetList = new ArrayList<XSSFSheet>();
		workbook = Optional.of(workbook).orElse(new XSSFWorkbook());
		for (int num = 0; num < workbook.getNumberOfSheets() ; num++) {
			XSSFSheet sheetAt = workbook.getSheetAt(num);
			sheetList.add(sheetAt);
		}
		try {
			if(opcPackage!=null) {
				opcPackage.close();
			}
		}catch(Exception e){
			logger.error("error : {}",e.getMessage());
		}finally {
			if(opcPackage != null) {
				try{
					opcPackage.close();
				}catch (Exception e) {
					logger.error("error : {}",e.getMessage());
				}
			}
			if(inputStream != null) {
				try {
					inputStream.close();
				}catch (Exception e) {
					logger.error("error : {}",e.getMessage());
				}
			}
		}
		return ExcelDataInfo.builder().workbook(workbook).sheetList(sheetList).sheetSize(workbook.getNumberOfSheets()).build();
	}
	
	public static InsuranceAndProductList ReadSheetOne(XSSFSheet xssfSheet) throws Exception{
		List<ProductListVo> list = new ArrayList<ProductListVo>();
		List<HashMap<String, String>> insureList = new ArrayList<HashMap<String,String>>();
		//첫번째 시트 Row
		
		//읽기시작할 줄번호
		int readRowNum = 4;
		//현재 시트에있는 최대 값 로우
		int SheetMaxRow = xssfSheet.getPhysicalNumberOfRows();
		
		//최대로우까지 루프
		for(int i = (readRowNum-1); i < SheetMaxRow; i++) {
			XSSFRow row = xssfSheet.getRow(i);
			ProductListVo vo = new ProductListVo();
			//A시작
			int startCell = 0;
			//G까지 리딩예정
			int maxCell = row.getPhysicalNumberOfCells();
			if(maxCell != 7) throw new Exception("T_0 sheet over cell ( idx > 7 )");
			
			
			HashMap<String, String> insure = new HashMap<String, String>();
			insure.put("bCode", readCell(row.getCell(0)));
			insure.put("bName", readCell(row.getCell(1)));
			insureList.add(insure);
			for (int j = startCell; j < maxCell; j++) {
				XSSFCell cell = row.getCell(j);
				//셀 텍스트
				String cellValue = caseOneSheetCellValue(cell,j);
				//구간별 vo 값 지정
				caseOneSheetDateSet(cell,j,vo);
			}
			list.add(vo);
		}
		return InsuranceAndProductList.builder().insureList(insureList).pList(list).build();
	}
	
	public static List<BkProductAndScriptStepDetail> ReadSheetTwo(XSSFSheet xssfSheet) throws Exception{
		List<BkProductAndScriptStepDetail> resultList = new ArrayList<BkProductAndScriptStepDetail>();
		List<ScriptStepDetailVo> list = new ArrayList<ScriptStepDetailVo>();
		//첫번째 시트 Row
		
		//읽기시작할 줄번호
		int readRowNum = 4;
		//현재 시트에있는 최대 값 로우
		int SheetMaxRow = xssfSheet.getPhysicalNumberOfRows();

		// 한 상품당 순번 37까지 있으며
		// 37로 나누었을때 나머지가있으면 미적정 사이즈
		if ( (SheetMaxRow-readRowNum % 37) == 0 ? true : false ) {
			throw new Exception("T_1 sheet not match  max size");
		}
		
		List<String> codeList = new ArrayList<String>();
		for(int i = (readRowNum-1); i < SheetMaxRow; i++) {
			XSSFRow row = xssfSheet.getRow(i);
			String productCode = null;
			//상품보종 셀부터 시작
			int startCell = 2;
			//녹취 기본내용까지 
			int maxCell = row.getPhysicalNumberOfCells();
			//그이상 내용있으면 에러
			if(maxCell != 5) {
				throw new Exception("T_1 sheet over cell ( idx > 5 ) or low cell ( idx < 5)");
			}
			
			int order = 0;
			ScriptStepDetailVo vo = new ScriptStepDetailVo();
			for (int j = startCell; j < maxCell; j++) {
				XSSFCell cell = row.getCell(j);
				//셀 텍스트
				String cellValue = caseTwoSheetCellValue(cell,j);	
				if(j == 2) {
					productCode = cellValue;
					codeList.add(cellValue);
				}
				if(j == 3) {
					order = Integer.parseInt(cellValue);
					//구간별 vo 값 지정
					caseTwoSheetDateSet(vo,j,cellValue , productCode);
				}
				//가변 체크후 텍스트 변경
				if(j == 4) {
					vo.setrScriptDetailText(caseTwoSheetTextVariable(cellValue,order));
				}
			}
			if(order == 2) {
				ScriptStepDetailVo vo1 = new ScriptStepDetailVo(); // 대리인O
				vo1.bkDetailOrderCommon(productCode, "S", 0, "SCRT06", "SCRT0601", resJsonString(1), 2);
				vo1.setrScriptDetailText("안녕하세요. 저는 {BZBR_NM}지점 방카슈랑스 판매인 {ADVPE_NM}입니다.\r\n" + 
						"{CUS_NM} 고객님의 대리인 {AGNPE_NM}님이 맞으신가요?");
				
				list.add(vo1);
				ScriptStepDetailVo vo2 = new ScriptStepDetailVo(); // 대리인X
				vo2.bkDetailOrderCommon(productCode, "S", 0, "SCRT06", "SCRT0601", resJsonString(2), 3);
				vo2.setrScriptDetailText("안녕하세요. 저는 {BZBR_NM}지점 방카슈랑스 판매인 {ADVPE_NM}입니다.\r\n" + 
						"{CUS_NM} 고객님이 맞으신가요?");
				list.add(vo2);
			}else {
			list.add(vo);
			}
		}
		codeList = codeList.stream().distinct().collect(Collectors.toList());
		for (String string : codeList) {
			//상품코드로 필터링
			List<ScriptStepDetailVo> collect = list.stream().filter(i->{
				if(i.getrScriptStepFk().equals(string)) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());
			//조합
			resultList.add(BkProductAndScriptStepDetail.builder().productCode(string).detailList(collect).build());
		}
		//조합 리스트 리턴
		return resultList;
	}
	
	
	private static void caseTwoSheetDateSet(ScriptStepDetailVo vo, int cellIdx ,String cellValue ,String productCode) {
		switch (cellIdx) {
		case 2:	//상품코드
			vo.setrScriptStepFk(cellValue);
			break;
		case 3: //순번
			caseTwoSheetOrderCell(vo,Integer.parseInt(cellValue),productCode);
		case 4:
			caseTwoSheetTextVariable(cellValue,cellIdx);
			vo.setrScriptDetailText(cellValue);
			break;
		}
	}
	
	private static String caseTwoSheetTextVariable(String cellValue, int cellIdx) {
		switch (cellIdx) {
		case 6:
			cellValue = cellValue.replace("{1}", "{JOIN_AFCO_PRD}");
			break;
		case 17:
			cellValue = cellValue.replace("{1}", "{PAY_INFO}");
			cellValue = cellValue.replace("{2}", "{ISFE}");
			break;
		case 22:
			cellValue = cellValue.replace("{1}", "{INS_COMM_1_TIME}");
			cellValue = cellValue.replace("{2}", "{INS_COMM_1_COST}");
			cellValue = cellValue.replace("{3}", "{INS_COMM_4_TIME}");
			cellValue = cellValue.replace("{4}", "{INS_COMM_4_COST}");
			cellValue = cellValue.replace("{5}", "{INS_COMM_2_TIME}");
			cellValue = cellValue.replace("{6}", "{INS_COMM_2_COST}");
			cellValue = cellValue.replace("{7}", "{SA_APINCM_TIME}");
			cellValue = cellValue.replace("{8}", "{SA_APINCM_COST}");
			cellValue = cellValue.replace("{9}", "{SA_ETC_COST_TIME}");
			cellValue = cellValue.replace("{10}", "{SA_ETC_COST_COST}");
			cellValue = cellValue.replace("{11}", "{SA_FUND_INCM_TIME}");
			cellValue = cellValue.replace("{12}", "{SA_FUND_INCM_COST}");
			cellValue = cellValue.replace("{13}", "{ANNU_ACUM_GUR_TIME}");
			cellValue = cellValue.replace("{14}", "{ANNU_ACUM_GUR_COST}");
			cellValue = cellValue.replace("{15}", "{DEAD_AMT_GUR_TIME}");
			cellValue = cellValue.replace("{16}", "{DEAD_AMT_GUR_COST}");
			cellValue = cellValue.replace("{17}", "{CONT_MGNT_COST_OBJ}");
			cellValue = cellValue.replace("{18}", "{CONT_MGNT_COST_TIME}");
			cellValue = cellValue.replace("{19}", "{CONT_MGNT_COST_COST}");
			cellValue = cellValue.replace("{20}", "{FUND_CHG_COST_OBJ}");
			cellValue = cellValue.replace("{21}", "{FUND_CHG_COST_TIME}");
			cellValue = cellValue.replace("{22}", "{FUND_CHG_COST_COST}");
			cellValue = cellValue.replace("{23}", "{SVR_COMN_OBJ}");
			cellValue = cellValue.replace("{24}", "{SVR_COMN_TIME}");
			cellValue = cellValue.replace("{25}", "{SVR_COMN_COST}");
			break;

		default:
			break;
		}
		return cellValue;
	}

	/*
	 *  type  = 0 : redText
	 *  type  = 1 : isDeputy : true
	 *  type  = 2 : isDeputy : false
	 *  type  = 3 : isFundDetail : true
	 *  type  = 4 : isFundRate : true
	 */
	private static String resJsonString(int type) {
		JSONObject jObj = new JSONObject();
		switch (type) {
		case 0: jObj.put("isTextRed", true); break;
		case 1: jObj.put("isDeputy", true); break;
		case 2: jObj.put("isDeputy", false); break;
		case 3: jObj.put("isFundDetail", true); break;
		case 4: jObj.put("isFundRate", true); break;
		}
		return jObj.toJSONString();
	}
	
	
	
	private static void caseTwoSheetOrderCell(ScriptStepDetailVo vo , int orderNum , String productCode) {
		switch (orderNum) {
		case 1:	 vo.bkDetailOrderCommon(productCode, "S", 259, null, null, resJsonString(0), 1); break;
		case 2:	 vo.bkDetailOrderCommon(productCode, "S", 0, "SCRT06", "SCRT0601", resJsonString(1), 2); break;
		case 3:	 vo.bkDetailOrderCommon(productCode, "S", 260, null, null, resJsonString(0), 4); break;
		case 4:	 vo.bkDetailOrderCommon(productCode, "T", 261, null, null, null, 5); break;
		case 5:	 vo.bkDetailOrderCommon(productCode, "T", 262, null, null, resJsonString(0), 6); break;
		
		case 6:	 vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 1);break;
		case 7:	 vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 2);break;
		case 8:	 vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 3);break;
		case 9:	 vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 4);break;
		case 10: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 5);break;
		
		case 11: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 1);break;
		case 12: vo.bkDetailOrderCommon(productCode, "T", 0, "SCRT05", "SCRT0501", resJsonString(3), 2);break;
		case 13: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 3);break;
		case 14: vo.bkDetailOrderCommon(productCode, "T", 0, "SCRT05", "SCRT0402", resJsonString(4), 4);break;
		case 15: vo.bkDetailOrderCommon(productCode, "T", 266, null, null, null, 5);break;
		case 16: vo.bkDetailOrderCommon(productCode, "S", 262, null, null, resJsonString(0), 6); break;
		
		case 17: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 1); break;
		case 18: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 2); break;
		case 19: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 3); break;
		case 20: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 4); break;
		case 21: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 5); break;
		
		case 22: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 1); break;
		
		case 23: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 1); break;
		case 24: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 2); break;
		case 25: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 3); break;
		case 26: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 4); break;
		case 27: vo.bkDetailOrderCommon(productCode, "T", 267, null, null, null, 5); break;
		case 28: vo.bkDetailOrderCommon(productCode, "S", 262, null, null, resJsonString(0), 6); break;
		case 29: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 1); break;
		case 30: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 2); break;
		case 31: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 3); break;
		case 32: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 4); break;
		case 33: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 5); break;
		
		case 34: vo.bkDetailOrderCommon(productCode, "T", 0, null, null, null, 1); break;
		case 35: vo.bkDetailOrderCommon(productCode, "S", 263, null, null, resJsonString(0), 2); break;
		case 36: vo.bkDetailOrderCommon(productCode, "S", 264, null, null, null, 3); break;
		case 37: vo.bkDetailOrderCommon(productCode, "S", 265, null, null, resJsonString(0), 4); break;
		}
		
		
		
	}

	public static String caseTwoSheetCellValue(XSSFCell cell, int idx) throws Exception {
		String result = null;
		switch (idx) {
		case 2:	//상품코드
			result = readCell(cell);
			break;
		case 3: //텍스트
			result = readCell(cell);
			break;
		case 4: //텍스트
			result = readCell(cell);
			break;
		}
		return result;
	}

	public static String readCell(XSSFCell cell) throws Exception {
		String str = "";
		if(cell == null) {
			throw new NullPointerException("cell is Null");
		}
		
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			str = String.valueOf((int)cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING:
			str = cell.getStringCellValue();
			break;
		}
		str = Optional.ofNullable(str).orElse("");
		return str;
	}
	
	
	public static String caseOneSheetCellValue(XSSFCell cell , int idx) throws Exception {
		String result = null;
		
		switch (idx) {
		case 0:		//보험사코드
			result = readCell(cell);
			break;
		case 1:		//보험사명
			result = readCell(cell);
			break;
		case 2:		//대표상품명
			result = readCell(cell);
			break;
		case 3:		//대표상품코드
			result = readCell(cell);
			break;
		case 4:		//보종코드 (상품코드)
			result = readCell(cell);
			break;
		case 5:		//상품명
			result = readCell(cell);
			break;
		case 6:		//판매여부
			result = readCell(cell);
			break;
		}
		return result;	
	}
	public static void  caseOneSheetDateSet(XSSFCell cell , int idx , ProductListVo vo) throws Exception {
		String result = null;
		
		switch (idx) {
		case 0:		//보험사코드
			result = readCell(cell);
			break;
		case 1:		//보험사명
			result = readCell(cell);
			break;
		case 2:		//대표상품명
			result = readCell(cell);
			break;
		case 3:		//대표상품코드
			result = readCell(cell);
			break;
		case 4:		//보종코드 (상품코드)
			result = readCell(cell);
			vo.setrProductCode(result);
			break;
		case 5:		//상품명
			result = readCell(cell);
			vo.setrProductName(result);
			break;
		case 6:		//판매여부
			result = readCell(cell);
			vo.setrUseYn(result);
			break;
		}
	}

	public static List<ProductCharacterDetail> ReadSheetThree(XSSFSheet xssfSheet) throws Exception {
		List<ProductCharacterDetail> resultList = new ArrayList<ProductCharacterDetail>();
		//첫번째 시트 Row
		
		//읽기시작할 줄번호
		int readRowNum = 4;
		//현재 시트에있는 최대 값 로우
		int SheetMaxRow = xssfSheet.getPhysicalNumberOfRows();

		// 한 상품당 순번 37까지 있으며
		// 37로 나누었을때 나머지가있으면 미적정 사이즈
		
		for(int i = (readRowNum-1); i < SheetMaxRow; i++) {
			XSSFRow row = xssfSheet.getRow(i);
			int startCell = 2;
			//녹취 기본내용까지 
			int maxCell = row.getPhysicalNumberOfCells();
		
			//그이상 내용있으면 에러
			if(maxCell != 4) {
				throw new Exception("T_2 sheet over cell ( idx > 5 ) or low cell ( idx < 5)");
			}
			
			ProductCharacterDetail build = ProductCharacterDetail.builder()
											.rsProductSubCode(readCell(row.getCell(0)))
											.rsProductCode(readCell(row.getCell(1)))
											.rsProductType("4")
											.rsProductName(readCell(row.getCell(2)))
											.rsProductDetailText(readCell(row.getCell(3)))
											.build();
			
			resultList.add(build);
		}	
		return resultList;
	}

	public static List<ProductFundDetail> ReadSheetFour(XSSFSheet xssfSheet) throws Exception {
		List<ProductFundDetail> resultList = new ArrayList<ProductFundDetail>();
		//첫번째 시트 Row
		
		//읽기시작할 줄번호
		int readRowNum = 4;
		//현재 시트에있는 최대 값 로우
		int SheetMaxRow = xssfSheet.getPhysicalNumberOfRows();

		// 한 상품당 순번 37까지 있으며
		// 37로 나누었을때 나머지가있으면 미적정 사이즈
		
		for(int i = (readRowNum-1); i < SheetMaxRow; i++) {
			XSSFRow row = xssfSheet.getRow(i);
			int startCell = 1;
			//녹취 기본내용까지 
			int maxCell = row.getPhysicalNumberOfCells();
		
			//그이상 내용있으면 에러
			if(maxCell != 5) {
				throw new Exception("T_2 sheet over cell ( idx > 5 ) or low cell ( idx < 5)");
			}
			
			resultList.add(ProductFundDetail.builder()
							 .rsProductFundBCode(readCell(row.getCell(0)))
							 .rsProductCode(readCell(row.getCell(1)))
							 .rsProductFundDetailCode(readCell(row.getCell(2)))
							 .rsProductFundDetailName(readCell(row.getCell(3)))
							 .rsProductFundDetailText(readCell(row.getCell(4)))
							 .build());
			
		}	
		return resultList;
	}
		
		
}
