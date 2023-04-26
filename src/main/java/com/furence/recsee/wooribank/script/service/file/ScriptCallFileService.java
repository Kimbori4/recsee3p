package com.furence.recsee.wooribank.script.service.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.wooribank.script.param.request.FileDownloadParam;
import com.furence.recsee.wooribank.script.param.request.FileDownloadParam.ScriptCallInfo;
import com.furence.recsee.wooribank.script.param.response.ScriptInfo;
import com.furence.recsee.wooribank.script.param.response.ScriptInfo.Detail;
import com.furence.recsee.wooribank.script.param.response.ScriptInfo.Step;
import com.furence.recsee.wooribank.script.repository.dao.FileContentDao;
import com.furence.recsee.wooribank.script.service.file.types.FileService;
import com.furence.recsee.wooribank.script.util.file.excel.ExcelMaker;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service("scriptCallFileService")
public class ScriptCallFileService extends FileService<ScriptInfo, FileDownloadParam.ScriptCallInfo>{

	private static final Logger logger = LoggerFactory.getLogger(ScriptCallFileService.class);
	
	private final int TOTAL_COLUMNS = 3;
	
	@Autowired
	public ScriptCallFileService(FileContentDao fileContentDao) {
		super(fileContentDao);
	}
	
	@Override
	public FileServiceType getType() {
		return FileServiceType.CallScript;
	}
	
	/**
	 * 스크립트 변경 이력정보 가져오기
	 * @param history
	 * @return
	 */
	private ScriptInfo getProductCallScriptInfo(ScriptCallInfo param) throws Exception {		
		return ScriptInfo.from(this.fileContentDao.selectCallScriptInfo(param));
	}
	
	private ScriptInfo getProductCallScriptInfoRec(ScriptCallInfo param) throws Exception {		
		return ScriptInfo.from(this.fileContentDao.selectCallScriptInfoRec(param));
	}
	
	@Override
	protected ScriptInfo getFileContent(ScriptCallInfo param) {
		ScriptInfo info = null;
		
		try {
			if(param.getType() == 1 ) {
				info = this.getProductCallScriptInfo(param);
			}else if(param.getType() == 2) {
				info = this.getProductCallScriptInfoRec(param);
			}else {
				info = this.getProductCallScriptInfo(param);
			}
		} catch (Exception e) {
			logger.error("error",e);
		}
		
		return info;
	}
	
	/**
	 * PDF 파일 생성
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	@Override
	protected File makePDF(ScriptInfo info, ScriptCallInfo param)  {
		
		Document pdf = null;
		FileOutputStream fos = null;
		
		try{
		
			pdf = new Document(PageSize.A4, 0, 0, 10, 10 ); 
			
			// 기본 한글폰트
			BaseFont basefont = BaseFont.createFont(this.fontPath, 
					BaseFont.IDENTITY_H, 
					BaseFont.EMBEDDED);
			
			Font stepFont = new Font(basefont, 10, Font.BOLD);
			Font warnFont = new Font(basefont, 12, Font.BOLD, BaseColor.WHITE);
			Font normalFont = new Font(basefont, 9);
			Font boldFont = new Font(basefont, 9, Font.BOLD);
			Font noteFont = new Font(basefont, 9, Font.BOLD, BaseColor.BLUE);
			
			String file = this.filePath 
					+ info.getProductName() 
					+ "_" + param.getCallKey() 
					+ ".pdf";
			
			fos = new FileOutputStream(file);
			PdfWriter.getInstance(pdf, fos);
			pdf.open();
			
			// 상품명
			String productName = Optional.ofNullable(info.getProductName()).orElse("");
			
			// 상품코드
			String productCode = Optional.ofNullable(info.getProductCode()).orElse("");
			
			PdfPTable titleTable = new PdfPTable(TOTAL_COLUMNS);
			titleTable.getDefaultCell().setBorder(0);
			titleTable.setWidthPercentage(90);
			titleTable.setWidths(new int[]{5,20,5});
			
			PdfPCell cell =  new PdfPCell(new Phrase( "상품코드 : "+ productCode  , boldFont ));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setFixedHeight(25);
			cell.setColspan(TOTAL_COLUMNS - 1);
			cell.setPaddingLeft(5);
			cell.setBorder(0);
			titleTable.addCell(cell);
			
			cell = new PdfPCell(new Phrase( "행내한" , warnFont ));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBackgroundColor(new BaseColor(0, 0, 139));
			cell.setPadding(10);
			
			PdfPTable warnTable = new PdfPTable(1);
			warnTable.getDefaultCell().setBorder(0);
			warnTable.addCell(cell);
			
			cell = new PdfPCell(warnTable);
			cell.setRowspan(2);
			cell.setFixedHeight(40);
			titleTable.addCell(cell);
			
			cell =  new PdfPCell(new Phrase( "상품명 : " + productName , boldFont ));	
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setFixedHeight(25);
			cell.setColspan(TOTAL_COLUMNS - 1);
			cell.setPaddingLeft(5);
			cell.setBorder(0);
			titleTable.addCell(cell);
			
			LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
			Phrase downloadDate = (new Phrase( "* 다운로드일자 : " + now + " (변경될 수 있으니 실제 녹취 시 녹취 당일 최신 버전의 스크립트를 다운로드하시기 바랍니다.)" , normalFont ));
			cell =  new PdfPCell(downloadDate);
			
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setFixedHeight(25);
			cell.setColspan(TOTAL_COLUMNS);
			cell.setBorder(0);
			cell.setPaddingLeft(5);
			titleTable.addCell(cell);
			
			titleTable.setSpacingAfter(15);
			
			pdf.add(titleTable);
			
			List<Step> stepList = info.getStepList();
			int index = 0;
			
			for (int i = 0; i < stepList.size(); i++) {
				Step step = stepList.get(i);
				
				if(param.getRecType() == 0) {
					if(i == 0) {
						continue;
					}
				}
				PdfPTable stepTable = new PdfPTable(TOTAL_COLUMNS);
				stepTable.setWidthPercentage(90);
				stepTable.setWidths(new int[]{5,20,5});
				stepTable.getDefaultCell().setBorderWidth(0.25f);
				stepTable.getDefaultCell().setUseBorderPadding(false);
				
				boolean isParentStep = step.getStepParent().equals("0");
				
				if(isParentStep && index++ > 0 ) {
					stepTable.setSpacingBefore(10);
				}
				
				cell = new PdfPCell(new Phrase( isParentStep? step.getStepName() : " - " + step.getStepName() , normalFont ));	
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);				
				cell.setFixedHeight(25);			
				cell.setBackgroundColor( isParentStep? new BaseColor(122, 172, 254) : new BaseColor(163, 221, 255) );
				cell.setColspan(TOTAL_COLUMNS - 1);				
				cell.setPaddingLeft(isParentStep ? 5 : 15);				
				stepTable.addCell(cell);
				
				// 상위 스텝은 비고란 추가
				cell = new PdfPCell(new Phrase( isParentStep ? "비고" : " ", stepFont ));	
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBackgroundColor( isParentStep? new BaseColor(122, 172, 254) : new BaseColor(163, 221, 255)  );
				cell.setFixedHeight(25);
				stepTable.addCell(cell);
				
				for(Detail detail: step.getDetailList()) {
					
					if(detail.getDetailType() != null) {
						cell = new PdfPCell(new Phrase( detail.getDetailTypeName() , normalFont ));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						if( detail.getDetailType().equals("G") ) {
							cell.setBackgroundColor(new BaseColor(255, 230, 153)); //고객답변
						} else if( detail.getDetailType().equals("S")) {
							cell.setBackgroundColor(new BaseColor(222, 176, 156));//직원직접리딩
						}
						stepTable.addCell(cell);
						
						cell = new PdfPCell(new Phrase( detail.getUnescapedDetailText() , normalFont ));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						if( detail.getDetailType().equals("G") ) {
							cell.setBackgroundColor(new BaseColor(255, 230, 153)); //고객답변
						} else if( detail.getDetailType().equals("S")) {
							cell.setBackgroundColor(new BaseColor(222, 176, 156));//직원직접리딩
						}
						cell.setPaddingLeft(5);
						cell.setLeading(1f, 1.5f);
						cell.setPaddingBottom(10);					
						stepTable.addCell(cell);
						
						String note = getNoteDescription(detail);
						
						cell = new PdfPCell(new Phrase( note , noteFont ));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						if( detail.getDetailType().equals("G") ) {
							cell.setBackgroundColor(new BaseColor(255, 230, 153)); //고객답변
						} else if( detail.getDetailType().equals("S")) {
							cell.setBackgroundColor(new BaseColor(222, 176, 156));//직원직접리딩
						}
						cell.setPaddingLeft(5);
						cell.setLeading(1f, 1.5f);
						cell.setPaddingBottom(10);					
						stepTable.addCell(cell);
					}
					
				}
				
				pdf.add(stepTable);	
			}
			
			pdf.close();
			return new File(file);
			
		} catch (Exception e) {
			logger.error("error"+e.getMessage());
		} finally {
			try {
				pdf.close();
			}catch(Exception e) {
				logger.error("error"+e.getMessage());
			}
			if(fos != null) {
				try {
					fos.close();
				}catch (Exception e) {
					logger.error("error"+e.getMessage());
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 엑셀 파일 생성
	 * @param param
	 * @return
	 */
	@Override
	protected File makeExcel(ScriptInfo info, ScriptCallInfo param) {
		
		// 엑셀에 담길 전체 내용
		List<String[]> rows = new ArrayList<String[]>();		
		List<Step> stepList = info.getStepList();
		
		int productCodePos;
		int productNamePos;
		int infoPos;
		int stepPos;
		int detailPos;
		int emptyPos;
		
		List<String[]> productInfo = new ArrayList<String[]>();
		// 상품코드
		String[] productCode = new String[3];
		productCodePos = 0;
		ExcelMaker.setValue(productCode, "상품코드: " + info.getProductCode(), productCodePos++);
		ExcelMaker.setValue(productCode, "", productCodePos++);
		ExcelMaker.setValue(productCode, "행내한", productCodePos++);
		rows.add(productCode);
		productInfo.add(productCode);
		
		// 상품명
		String[] productName = new String[3];
		productNamePos = 0;
		ExcelMaker.setValue(productName, "상품명: " + info.getProductName(), productNamePos++);
		ExcelMaker.setValue(productName, "", productNamePos++);
		ExcelMaker.setValue(productName, "", productNamePos++);
		rows.add(productName);
		productInfo.add(productName);
		
		// 다운로드일자
		LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
		String[] dateInfo = new String[3];
		infoPos = 0;
		ExcelMaker.setValue(dateInfo, "* 다운로드일자 : " + now + " (변경될 수 있으니 실제 녹취 시 녹취 당일 최신 버전의 스크립트를 다운로드하시기 바랍니다.)", infoPos++);
		ExcelMaker.setValue(dateInfo, "", infoPos++);
		ExcelMaker.setValue(dateInfo, "", infoPos++);
		rows.add(dateInfo);
		productInfo.add(dateInfo);
		
		// 빈칸(엑셀 표 시작 전에 공백 한 칸 주기)
		String[] emptyCell = new String[3];
		emptyPos = 0;
		ExcelMaker.setValue(emptyCell, "", emptyPos++);
		ExcelMaker.setValue(emptyCell, "", emptyPos++);
		ExcelMaker.setValue(emptyCell, "", emptyPos++);
		rows.add(emptyCell);
		productInfo.add(emptyCell);
		
		// 엑셀 스타일 구분용
		List<String[]> steps = new ArrayList<String[]>();
		List<String[]> stepParents = new ArrayList<String[]>();
		List<String[]> details = new ArrayList<String[]>();
		List<String> detailRowType = new ArrayList<String>();
		
		// 스텝 그리기
		for(Step step: stepList) {
			
			String[] stepStr = new String[3];
			stepPos = 0;
			
			// 상위 스텝과 하위 스텝 구분
			boolean isParentStep = step.getStepParent().equals("0");
			if(isParentStep) {
				ExcelMaker.setValue(stepStr, step.getStepName(), stepPos++);
				ExcelMaker.setValue(stepStr, "", stepPos++);
				ExcelMaker.setValue(stepStr, "비고", stepPos++);
				stepParents.add(stepStr);
			}else {
				ExcelMaker.setValue(stepStr, " - " + step.getStepName(), stepPos++);
				ExcelMaker.setValue(stepStr, "", stepPos++);
				ExcelMaker.setValue(stepStr, "", stepPos++);
			}
			
			steps.add(stepStr);
			rows.add(stepStr);
			
			// 디테일 그리기			
			for(Detail detail: step.getDetailList()) {
				
				if(detail.getDetailType()!=null) { // 스텝에 디테일이 없는 경우 row를 add하지 않음
				
					String[] detailStr = new String[3];
					detailPos = 0;
					
					ExcelMaker.setValue(detailStr, detail.getDetailTypeName(), detailPos++);
					ExcelMaker.setValue(detailStr, detail.getUnescapedDetailText(), detailPos++);
					String note = getNoteDescription(detail);
					ExcelMaker.setValue(detailStr, note, detailPos++);
	
					rows.add(detailStr);
					details.add(detailStr);
					detailRowType.add(detail.getDetailType());
					
				}
			}
			
			
			
		}
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("excelList", rows);
		modelMap.put("productRow", productInfo);
		modelMap.put("stepParentRow", stepParents);
		modelMap.put("stepRow", steps);
		modelMap.put("detailRow", details);
		modelMap.put("detailRowType", detailRowType);
		modelMap.put("target", info.getProductName() 
				+ "_" + param.getCallKey() );

		
		File file = null;
		try {
			file = ExcelMaker.createXlsx(modelMap, filePath);
			logger.info("file: " + file);
		} catch (Exception e) {
			logger.error("error",e);
			logger.error("makeExcel error");
		}
		rows = null;
		
		return file;
		
	}
	
	
	public String getNoteDescription(ScriptInfo.Detail detail) {
		String attrText = getAttributeText(detail); 
		return attrText.isEmpty() ? getVariableCondition(detail) : attrText;
	}
	
	public String getVariableCondition(ScriptInfo.Detail detail) {
		return (detail.getIfCaseValue() != null && detail.getIfCaseDetailValue() !=null )
				? detail.getIfCaseValue() + "\n→ " + detail.getIfCaseDetailValue()
				: "";
	}
	
	public String getAttributeText(ScriptInfo.Detail detail) {
		return Optional.ofNullable(detail.getProductAttributesText())
			.orElse(Collections.emptyList())
			.stream()
			.reduce( (a,b) -> a + ',' + b )
			.orElse("");
	}
}
