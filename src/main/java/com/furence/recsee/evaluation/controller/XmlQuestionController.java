package com.furence.recsee.evaluation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogInfoService;
import com.furence.recsee.common.service.MenuAccessInfoService;
import com.furence.recsee.common.util.CookieSetToLang;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.evaluation.model.SheetInfo;
import com.furence.recsee.evaluation.service.EvaluationResultInfoService;
import com.furence.recsee.evaluation.service.EvaluationService;
import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadCall;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridXml;

@Controller
public class XmlQuestionController {

	@Autowired
	private EvaluationService evaluationService;

	@Autowired
	private MenuAccessInfoService menuAccessInfoService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private EvaluationResultInfoService evaluationResultInfoService;

	@Autowired
	private LogInfoService logInfoService;

	@Autowired
	private MessageSource messageSource;


	// 대분류 탭
	@RequestMapping(value = "/bgInfo.xml", method=RequestMethod.GET, produces="application/xml")
		public @ResponseBody dhtmlXGridXml bgInfo(HttpServletRequest request, HttpServletResponse response	){
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
			LoginVO userInfo = SessionManager.getUserInfo(request);
			dhtmlXGridXml xmls = null;

			if (userInfo != null) {

				xmls = new dhtmlXGridXml();
				xmls.setHeadElement(new dhtmlXGridHead());

				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());


				for(int j = 0; j < 14; j++){
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

					column.setType("ro");
					column.setAlign("center");
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setHidden("0");

					switch (j) {

						// 체크
						case 0:
							/*column.setWidth("50");
							column.setType("ch");
							// column.setHidden("1");
							// FIXME:(언어팩)체크
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.check", null,Locale.getDefault())+"</div>");
							*/
							column.setWidth("30");
							column.setType("ch");
							column.setSort("na");
							// FIXME:(언어팩)체크
							column.setValue("<div id='allcheck' style=\"cursor: pointer; top:2px;\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif'/></div>");
							break;
						// 순번
						case 1:
							column.setWidth("50");
							column.setSort("int");
							// FIXME:(언어팩)순번
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.no", null,Locale.getDefault())+"</div>");
							break;
						// 대분류명 (itemCode)
						case 2:
							column.setWidth("*");
							// FIXME:(언어팩)대분류명
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.bigname", null,Locale.getDefault())+"</div>");
							break;
						// 분류 설명 (itemContent)
						case 3:
							column.setWidth("*");
							// FIXME:(언어팩)분류 설명
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.bigdesc", null,Locale.getDefault())+"</div>");
							break;
						// 사용 횟수
						case 4:
							column.setWidth("0");
							column.setHidden("1");
							// FIXME:(언어팩) 사용횟수
							column.setValue("<div style=\"text-align:center;\">사용횟수</div>");
							break;
						// 생성 날짜
						case 5:
							column.setWidth("150");
							column.setSort("int");
							// FIXME:(언어팩) 생성날짜
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.createdate", null,Locale.getDefault())+"</div>");
							break;
						// 최종 수정 날짜
						case 6:
							column.setWidth("150");
							// FIXME:(언어팩) 최종 수정 날짜
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.modifydate", null,Locale.getDefault())+"</div>");
							break;
						// 수정
						case 7:
							column.setWidth("0");
							//FIXME:(언어팩) 수정
							column.setHidden("1");
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.modify", null,Locale.getDefault())+"</div>");
							break;
						// 공백
						case 8:
							column.setWidth("0");
							column.setValue("");
							column.setHidden("1");
							break;
						// 대분류 코드
						case 9:
							column.setWidth("0");
							column.setHidden("1");
							// FimxMe:(언어팩) 대분류 코드
							column.setValue("<div style=\"text-align:center;\">대분류 코드</div>");
							break;
						// 중분류 코드
						case 10:
							column.setWidth("0");
							column.setHidden("1");
							//FimxMe:(언어팩) 중분류 코드
							column.setValue("<div style=\"text-align:center;\">중분류 코드</div>");
							break;
						// 소분류 코드
						case 11:
							column.setWidth("0");
							column.setHidden("1");
							//FIXME:(언어팩) 소분류 코드
							column.setValue("<div style=\"text-align:center;\">소분류 코드</div>");
							break;
						// 점수
						case 12:
							column.setWidth("0");
							column.setHidden("1");
							column.setSort("int");
							//FIXME:(언어팩) 점수
							column.setValue("<div style=\"text-align:center;\">점수</div>");
							break;
						// 대분류 코드
						case 13:
							column.setWidth("0");
							column.setHidden("1");
							//FIXME:(언어팩)대분류코드
							column.setValue("<div style=\"text-align:center;\">대분류 코드</div>");
							break;
				}
				head.getColumnElement().add(column);
				column = null;
			}
			dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
			headCall.setParamElement(new ArrayList<String>());

			headCall.setCommand("attachHeader");
			xmls.setHeadElement(head);

			SheetInfo sheetInfo = new SheetInfo();

			//검색 타입
			if(request.getParameter("searchType") != null
					&& !request.getParameter("searchType").isEmpty()){
				sheetInfo.setSearchType(request.getParameter("searchType"));
			}
			//검색 단어
			if(request.getParameter("searchWord") != null
					&& !request.getParameter("searchWord").isEmpty()){
				sheetInfo.setSearchWord(request.getParameter("searchWord"));
			}
			if(request.getParameter("sBgName") != null
					&& !request.getParameter("sBgName").isEmpty()){
				sheetInfo.setsBgName(request.getParameter("sBgName"));
			}
			if (request.getParameter("sBgcode") != null
					&& !request.getParameter("sBgcode").isEmpty()) {
				sheetInfo.setSheetName(request.getParameter("sBgcode"));
			}
			if (request.getParameter("sBgContent") != null
					&& !request.getParameter("sBgContent").isEmpty()) {
				sheetInfo.setsDate(request.getParameter("sBgContent"));
			}
			if (request.getParameter("bPartCode") != null
					&& !request.getParameter("bPartCode").isEmpty()) {
				sheetInfo.seteDate(request.getParameter("bPartCode"));
			}
			if (request.getParameter("mPartCode") != null
					&& !request.getParameter("mPartCode").isEmpty()) {
				sheetInfo.setSheetName(request.getParameter("mPartCode"));
			}
			if (request.getParameter("sPartCode") != null
					&& !request.getParameter("sPartCode").isEmpty()) {
				sheetInfo.setSheetName(request.getParameter("sPartCode"));
			}
			if (request.getParameter("sBgMark") != null
					&& !request.getParameter("sBgMark").isEmpty()) {
				sheetInfo.setSheetName(request.getParameter("sBgMark"));
			}

			List<SheetInfo> selectResult = evaluationService.selectBgInfo(sheetInfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for (int i = 0; i < selectResult.size(); i++) {
				SheetInfo item = selectResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i + 1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				//체크
				cellInfo = new dhtmlXGridRowCell();
				if (item.getCheck() == null
						|| (item.getCheck().toString().trim().equals(""))) {
					cellInfo.setValue("0");
				} else {
					cellInfo.setValue(item.getCheck().toString().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//순번
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(i + 1));
				rowItem.getCellElements().add(cellInfo);

				// 대분류명
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsBgName() == null
						|| ("".equals(item.getsBgName().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsBgName().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 대분류 설명
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsBgContent() == null
						|| ("".equals(item.getsBgContent().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsBgContent().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//사용횟수
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);

				//생성 날짜
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsBgMdate() == null
						|| ("".equals(item.getsBgMdate().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsBgMdate().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//최종 수정 날짜
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsBgUdate() == null
						|| ("".equals(item.getsBgUdate().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsBgUdate().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//수정
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<button class=\"ui_main_btn_flat updateInfo\" id="+(i+1)+" data-target=\"ub\"><spring:message code=\"evaluation.management.sheet.modify\"/></button>");

				rowItem.getCellElements().add(cellInfo);

				//콜백
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);

				// 대분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getbPartCode() == null
						|| ("".equals(item.getbPartCode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getbPartCode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 중분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getmPartCode() == null
						|| ("".equals(item.getmPartCode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getmPartCode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//소분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsPartCode() == null
						|| ("".equals(item.getsPartCode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsPartCode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 점수
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsBgMark() == null
						|| (item.getsBgMark().toString().trim().equals(""))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsBgMark().toString().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//대분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsBgcode() == null
						|| ("".equals(item.getsBgcode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsBgcode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);
				rowItem = null;
				}
			} else {
			xmls = null;
		}
			return xmls;
	}



	//중분류 탭
	@RequestMapping(value = "/mgInfo.xml", method=RequestMethod.GET, produces="application/xml")
		public @ResponseBody dhtmlXGridXml mgInfo(HttpServletRequest request, HttpServletResponse response){
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
			LoginVO userInfo = SessionManager.getUserInfo(request);
			dhtmlXGridXml xmls = null;

			if (userInfo != null) {

				xmls = new dhtmlXGridXml();
				xmls.setHeadElement(new dhtmlXGridHead());

				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());


				for(int j = 0; j < 14; j++){
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

					column.setType("ro");
					column.setAlign("center");
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setHidden("0");

					switch (j) {

						// 체크
						case 0:
							/*column.setWidth("50");
							column.setType("ch");
							// column.setHidden("1");
							// FIXME:(언어팩)체크
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.check", null,Locale.getDefault())+"</div>");
							*/
							column.setWidth("30");
							column.setType("ch");
							column.setSort("na");
							// FIXME:(언어팩)체크
							column.setValue("<div id='allcheck' style=\"cursor: pointer; top:2px;\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif'/></div>");
							break;
						// 순번
						case 1:
							column.setWidth("50");
							column.setSort("int");
							// FIXME:(언어팩)순번
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.no", null,Locale.getDefault())+"</div>");
							break;
						// 중분류명 (itemCode)
						case 2:
							column.setWidth("*");
							// FIXME:(언어팩)중분류명
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.middlename", null,Locale.getDefault())+"</div>");
							break;
						// 분류 설명 (itemContent)
						case 3:
							column.setWidth("*");
							// FIXME:(언어팩)분류 설명
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.middledesc", null,Locale.getDefault())+"</div>");
							break;
						// 사용 횟수
						case 4:
							column.setWidth("0");
							column.setHidden("1");
							// FIXME:(언어팩) 사용횟수
							column.setValue("<div style=\"text-align:center;\">사용횟수</div>");
							break;
						// 생성 날짜
						case 5:
							column.setWidth("150");
							column.setSort("int");
							// FIXME:(언어팩) 생성날짜
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.createdate", null,Locale.getDefault())+"</div>");
							break;
						// 최종 수정 날짜
						case 6:
							column.setWidth("150");
							// FIXME:(언어팩) 최종 수정 날짜
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.modifydate", null,Locale.getDefault())+"</div>");
							break;
						//수정
						case 7:
							column.setWidth("0");
							//FIXME:(언어팩) 수정
							column.setHidden("1");
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.modify", null,Locale.getDefault())+"</div>");
							break;
						// 공백
						case 8:
							column.setWidth("0");
							column.setValue("");
							column.setHidden("1");
							break;
						// 대분류 코드
						case 9:
							column.setWidth("0");
							column.setHidden("1");
							// FimxMe:(언어팩) 대분류 코드
							column.setValue("<div style=\"text-align:center;\">대분류 코드</div>");
							break;
						// 중분류 코드
						case 10:
							column.setWidth("0");
							column.setHidden("1");
							//FimxMe:(언어팩) 중분류 코드
							column.setValue("<div style=\"text-align:center;\">중분류 코드</div>");
							break;
						// 소분류 코드
						case 11:
							column.setWidth("0");
							column.setHidden("1");
							//FIXME:(언어팩) 소분류 코드
							column.setValue("<div style=\"text-align:center;\">소분류 코드</div>");
							break;
						// 점수
						case 12:
							column.setWidth("0");
							column.setHidden("1");
							column.setSort("int");
							//FIXME:(언어팩) 점수
							column.setValue("<div style=\"text-align:center;\">점수</div>");
							break;
						//중분류 코드
						case 13:
							column.setWidth("0");
							column.setHidden("1");
							//FIXME:(언어팩)대분류코드
							column.setValue("<div style=\"text-align:center;\">중분류 코드</div>");
							break;
				}
				head.getColumnElement().add(column);
				column = null;
			}

			dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
			headCall.setParamElement(new ArrayList<String>());

			headCall.setCommand("attachHeader");
			xmls.setHeadElement(head);

			SheetInfo sheetInfo = new SheetInfo();
			//검색 타입
			if(request.getParameter("searchType") != null
					&& !request.getParameter("searchType").isEmpty()){
				sheetInfo.setSearchType(request.getParameter("searchType"));
			}
			//검색 단어
			if(request.getParameter("searchWord") != null
					&& !request.getParameter("searchWord").isEmpty()){
				sheetInfo.setSearchWord(request.getParameter("searchWord"));
			}
			if(request.getParameter("sMgName") != null
					&& !request.getParameter("sMgName").isEmpty()){
				sheetInfo.setsMgName(request.getParameter("sMgName"));
			}
			if (request.getParameter("sMgcode") != null
					&& !request.getParameter("sMgcode").isEmpty()) {
				sheetInfo.setSheetName(request.getParameter("sMgcode"));
			}
			if (request.getParameter("sMgContent") != null
					&& !request.getParameter("sMgContent").isEmpty()) {
				sheetInfo.setsDate(request.getParameter("sMgContent"));
			}
			if (request.getParameter("bPartCode") != null
					&& !request.getParameter("bPartCode").isEmpty()) {
				sheetInfo.seteDate(request.getParameter("bPartCode"));
			}
			if (request.getParameter("mPartCode") != null
					&& !request.getParameter("mPartCode").isEmpty()) {
				sheetInfo.setSheetName(request.getParameter("mPartCode"));
			}
			if (request.getParameter("sPartCode") != null
					&& !request.getParameter("sPartCode").isEmpty()) {
				sheetInfo.setSheetName(request.getParameter("sPartCode"));
			}
			if (request.getParameter("sMgMark") != null
					&& !request.getParameter("sMgMark").isEmpty()) {
				sheetInfo.setSheetName(request.getParameter("sMgMark"));
			}

			List<SheetInfo> selectResult = evaluationService.selectMgInfo(sheetInfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for (int i = 0; i < selectResult.size(); i++) {
				SheetInfo item = selectResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i + 1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				//체크
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);

				//순번
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(i + 1));
				rowItem.getCellElements().add(cellInfo);

				// 중분류명
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsMgName() == null
						|| ("".equals(item.getsMgName().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsMgName().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 중분류 설명
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsMgContent() == null
						|| ("".equals(item.getsMgContent().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsMgContent().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//사용횟수
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				//생성 날짜
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsMgMdate() == null
						|| ("".equals(item.getsMgMdate().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsMgMdate().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//최종 수정 날짜
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsMgUdate() == null
						|| ("".equals(item.getsMgUdate().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsMgUdate().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//수정
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<button class=\"ui_main_btn_flat updateInfo\" id="+(i+1)+" data-target=\"um\"><spring:message code=\"evaluation.management.sheet.modify\"/></button>");
				rowItem.getCellElements().add(cellInfo);

				//공백
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				// 대분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getbPartCode() == null
						|| ("".equals(item.getbPartCode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getbPartCode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 중분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getmPartCode() == null
						|| ("".equals(item.getmPartCode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getmPartCode().trim());
//					cellInfo.setValue(item.getsMgcode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//소분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsPartCode() == null
						|| ("".equals(item.getsPartCode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsPartCode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 점수
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsMgMark() == null
						|| (item.getsMgMark().toString().trim().equals(""))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsMgMark().toString().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//중분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsMgcode() == null
						|| ("".equals(item.getsMgcode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsMgcode().trim());
				}
				rowItem.getCellElements().add(cellInfo);


				xmls.getRowElements().add(rowItem);
				rowItem = null;
				}
			} else {
			xmls = null;
		}
			return xmls;
	}

	//소분류 탭
	@RequestMapping(value = "/sgInfo.xml", method=RequestMethod.GET, produces="application/xml")
		public @ResponseBody dhtmlXGridXml sgInfo(HttpServletRequest request, HttpServletResponse response){
		
		
			CookieSetToLang cls = new CookieSetToLang();
			cls.langSetFunc(request, response);
		
			LoginVO userInfo = SessionManager.getUserInfo(request);
			dhtmlXGridXml xmls = null;

			if (userInfo != null) {

				xmls = new dhtmlXGridXml();
				xmls.setHeadElement(new dhtmlXGridHead());

				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

				for(int j = 0; j < 14; j++){
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

					column.setType("ro");
					column.setAlign("center");
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setHidden("0");

					switch (j) {

						// 체크
						case 0:
							/*column.setWidth("50");
							column.setType("ch");
							// column.setHidden("1");
							// FIXME:(언어팩)체크
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.check", null,Locale.getDefault())+"</div>");
							*/
							column.setWidth("30");
							column.setType("ch");
							column.setSort("na");
							// FIXME:(언어팩)체크
							column.setValue("<div id='allcheck' style=\"cursor: pointer; top:2px;\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif'/></div>");
							break;
						// 순번
						case 1:
							column.setWidth("50");
							column.setSort("int");
							// FIXME:(언어팩)순번
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.no", null,Locale.getDefault())+"</div>");
							break;
						// 소분류명 (itemCode)
						case 2:
							column.setWidth("*");
							// FIXME:(언어팩)소분류명
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.smallname", null,Locale.getDefault())+"</div>");
							break;
						// 분류 설명 (itemContent)
						case 3:
							column.setWidth("*");
							// FIXME:(언어팩)분류 설명
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.smalldesc", null,Locale.getDefault())+"</div>");
							break;
						// 사용 횟수
						case 4:
							column.setWidth("0");
							column.setHidden("1");
							// FIXME:(언어팩) 사용횟수
							column.setValue("<div style=\"text-align:center;\">사용횟수</div>");
							break;
						// 생성 날짜
						case 5:
							column.setWidth("150");
							column.setSort("int");
							// FIXME:(언어팩) 생성날짜
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.createdate", null,Locale.getDefault())+"</div>");
							break;
						// 최종 수정 날짜
						case 6:
							column.setWidth("150");
							// FIXME:(언어팩) 최종 수정 날짜
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.modifydate", null,Locale.getDefault())+"</div>");
							break;
						//수정
						case 7:
							column.setWidth("0");
							//FIXME:(언어팩) 수정
							column.setHidden("1");
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.modify", null,Locale.getDefault())+"</div>");
							break;
						// 공백
						case 8:
							column.setWidth("0");
							column.setValue("");
							column.setHidden("1");
							break;
						// 대분류 코드
						case 9:
							column.setWidth("0");
							column.setHidden("1");
							// FimxMe:(언어팩) 대분류 코드
							column.setValue("<div style=\"text-align:center;\">대분류 코드</div>");
							break;
						// 중분류 코드
						case 10:
							column.setWidth("0");
							column.setHidden("1");
							//FimxMe:(언어팩) 중분류 코드
							column.setValue("<div style=\"text-align:center;\">중분류 코드</div>");
							break;
						// 소분류 코드
						case 11:
							column.setWidth("0");
							column.setHidden("1");
							//FIXME:(언어팩) 소분류 코드
							column.setValue("<div style=\"text-align:center;\">소분류 코드</div>");
							break;
						// 점수
						case 12:
							column.setWidth("0");
							column.setHidden("1");
							//FIXME:(언어팩) 점수
							column.setValue("<div style=\"text-align:center;\">점수</div>");
							break;
						// 소분류 코드
						case 13:
							column.setWidth("0");
							column.setHidden("1");
							//FIXME:(언어팩)대분류코드
							column.setValue("<div style=\"text-align:center;\">소분류 코드</div>");
							break;
					}
				head.getColumnElement().add(column);
				column = null;
			}
			dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
			headCall.setParamElement(new ArrayList<String>());

			headCall.setCommand("attachHeader");
			xmls.setHeadElement(head);

			SheetInfo sheetInfo = new SheetInfo();
			//검색 타입
			if(request.getParameter("searchType") != null
					&& !request.getParameter("searchType").isEmpty()){
				sheetInfo.setSearchType(request.getParameter("searchType"));
			}
			//검색 단어
			if(request.getParameter("searchWord") != null
					&& !request.getParameter("searchWord").isEmpty()){
				sheetInfo.setSearchWord(request.getParameter("searchWord"));
			}
			if(request.getParameter("sSgName") != null
					&& !request.getParameter("sSgName").isEmpty()){
				sheetInfo.setsSgName(request.getParameter("sSgName"));
			}
			if (request.getParameter("sSgcode") != null
					&& !request.getParameter("sSgcode").isEmpty()) {
				sheetInfo.setSheetName(request.getParameter("sSgcode"));
			}
			if (request.getParameter("sSgContent") != null
					&& !request.getParameter("sSgContent").isEmpty()) {
				sheetInfo.setsDate(request.getParameter("sSgContent"));
			}
			if (request.getParameter("bPartCode") != null
					&& !request.getParameter("bPartCode").isEmpty()) {
				sheetInfo.seteDate(request.getParameter("bPartCode"));
			}
			if (request.getParameter("mPartCode") != null
					&& !request.getParameter("mPartCode").isEmpty()) {
				sheetInfo.setSheetName(request.getParameter("mPartCode"));
			}
			if (request.getParameter("sPartCode") != null
					&& !request.getParameter("sPartCode").isEmpty()) {
				sheetInfo.setSheetName(request.getParameter("sPartCode"));
			}
			if (request.getParameter("sSgMark") != null
					&& !request.getParameter("sSgMark").isEmpty()) {
				sheetInfo.setSheetName(request.getParameter("sSgMark"));
			}

			List<SheetInfo> selectResult = evaluationService.selectSgInfo(sheetInfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for (int i = 0; i < selectResult.size(); i++) {
				SheetInfo item = selectResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i + 1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				//체크
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);

				//순번
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(i + 1));
				rowItem.getCellElements().add(cellInfo);

				// 소분류명
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsSgName() == null
						|| ("".equals(item.getsSgName().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsSgName().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 소분류 설명
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsSgContent() == null
						|| ("".equals(item.getsSgContent().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsSgContent().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//사용횟수
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);

				//생성 날짜
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsSgMdate() == null
						|| ("".equals(item.getsSgMdate().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsSgMdate().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//최종 수정 날짜
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsSgUdate() == null
						|| ("".equals(item.getsSgUdate().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsSgUdate().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//수정
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<button class=\"ui_main_btn_flat updateInfo\" id="+(i+1)+" data-target=\"us\"><spring:message code=\"evaluation.management.sheet.modify\"/></button>");
				rowItem.getCellElements().add(cellInfo);

				//콜백
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);

				// 대분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getbPartCode() == null
						|| ("".equals(item.getbPartCode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getbPartCode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 중분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getmPartCode() == null
						|| ("".equals(item.getmPartCode().trim()))) {
					cellInfo.setValue("");
				} else {
//					cellInfo.setValue(item.getmPartCode().trim());
					cellInfo.setValue(item.getsSgcode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//소분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsPartCode() == null
						|| ("".equals(item.getsPartCode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsPartCode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 점수
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsSgMark() == null
						|| (item.getsSgMark().toString().trim().equals(""))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsSgMark().toString().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//생성 날짜
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsSgcode() == null
						|| ("".equals(item.getsSgcode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsSgcode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}
		} else {
			xmls = null;
		}
			return xmls;
	}

	//세부사항 탭
	@RequestMapping(value = "/igInfo.xml", method=RequestMethod.GET, produces="application/xml")
		public @ResponseBody dhtmlXGridXml igInfo(HttpServletRequest request, HttpServletResponse response){
			
			CookieSetToLang cls = new CookieSetToLang();
			cls.langSetFunc(request, response);
			LoginVO userInfo = SessionManager.getUserInfo(request);
			dhtmlXGridXml xmls = null;

			if (userInfo != null) {

				xmls = new dhtmlXGridXml();
				xmls.setHeadElement(new dhtmlXGridHead());

				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

				for(int j = 0; j < 15; j++){
					dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

					column.setType("ro");
					column.setAlign("center");
					column.setFiltering("0");
					column.setEditable("0");
					column.setCache("0");
					column.setHidden("0");

					switch (j) {

						// 체크
						case 0:
							/*column.setWidth("50");
							column.setType("ch");
							// column.setHidden("1");
							// FIXME:(언어팩)체크
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.check", null,Locale.getDefault())+"</div>");
							*/
							column.setWidth("30");
							column.setType("ch");
							column.setSort("na");
							// FIXME:(언어팩)체크
							column.setValue("<div id='allcheck' style=\"cursor: pointer; top:2px;\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif'/></div>");
							break;
						// 순번
						case 1:
							column.setWidth("50");
							column.setSort("int");
							// FIXME:(언어팩)순번
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.no", null,Locale.getDefault())+"</div>");
							break;
						// 소분류 명
						case 2:
							column.setWidth("0");
							//FIXME:(언어팩) 소분류명
							column.setHidden("1");
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.smallname", null,Locale.getDefault())+"</div>");
							break;
						// 세부항목명(itemCode)
						case 3:
							column.setWidth("*");
							column.setAlign("left");
							// FIXME:(언어팩)세부항목명
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.itemname", null,Locale.getDefault())+"</div>");
							break;
						// subitem
						case 4:
							column.setWidth("*");
							// FIXME:(언어팩)세부항목 설명
							column.setValue("SUB Item");
							break;
							// subitem
						/*case 5:
							column.setWidth("*");
							column.setHidden("1");
							// FIXME:(언어팩)세부항목 설명
							column.setValue("SUB Memo");
							break;*/
						// 사용 횟수
						case 5:
							column.setWidth("0");
							column.setHidden("1");
							// FIXME:(언어팩) 사용횟수
							column.setValue("<div style=\"text-align:center;\">사용횟수</div>");
							break;
						// 생성 날짜
						case 6:
							column.setWidth("150");
							column.setSort("int");
							// FIXME:(언어팩) 생성날짜
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.createdate", null,Locale.getDefault())+"</div>");
							break;
						// 최종 수정 날짜
						case 7:
							column.setWidth("150");
							// FIXME:(언어팩) 최종 수정 날짜
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.modifydate", null,Locale.getDefault())+"</div>");
							break;
						//수정
						case 8:
							column.setWidth("0");
							//FIXME:(언어팩) 수정
							column.setHidden("1");
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.modify", null,Locale.getDefault())+"</div>");
							break;

						// 공백
						case 9:
							column.setWidth("0");
							//FIXME:(언어팩) 수정
							column.setValue("");
							column.setHidden("1");
							break;
						// 대분류 코드
						case 10:
							column.setWidth("0");
							column.setHidden("1");
							// FimxMe:(언어팩) 대분류 코드
							column.setValue("<div style=\"text-align:center;\">대분류 코드</div>");
							break;
						// 중분류 코드
						case 11:
							column.setWidth("0");
							column.setHidden("1");
							//FimxMe:(언어팩) 중분류 코드
							column.setValue("<div style=\"text-align:center;\">중분류 코드</div>");

						// 점수
						case 12:
							column.setWidth("150");
							column.setSort("int");
							/*column.setHidden("1");*/
							//FIXME:(언어팩) 점수
							column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.score2", null,Locale.getDefault())+"</div>");
							break;
						// 세부사항 코드
						case 13:
							column.setWidth("0");
							column.setHidden("1");
							//FIXME:(언어팩) 세부사항 코드
							column.setValue("<div style=\"text-align:center;\">아이템 코드</div>");
							break;
						// 소분류 코드
						case 14:
							column.setWidth("0");
							column.setHidden("1");
							//FIXME:(언어팩) 소분류 코드
							column.setValue("<div style=\"text-align:center;\">소분류 코드</div>");
							break;
					}
				head.getColumnElement().add(column);
				column = null;
			}
			dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
			headCall.setParamElement(new ArrayList<String>());

			headCall.setCommand("attachHeader");
			xmls.setHeadElement(head);

			SheetInfo sheetInfo = new SheetInfo();

			/*//검색 타입
			if(request.getParameter("searchType") != null
					&& !request.getParameter("searchType").isEmpty()){
				sheetInfo.setSearchType(request.getParameter("searchType"));
			}
			//검색 단어
			if(request.getParameter("searchWord") != null
					&& !request.getParameter("searchWord").isEmpty()){
				sheetInfo.setSearchWord(request.getParameter("searchWord"));
			}

			if(request.getParameter("itemCode") != null
					&& !request.getParameter("itemCode").isEmpty()){
				sheetInfo.setIcode(request.getParameter("itemCode"));
			}
			if(request.getParameter("Icode") != null
					&& !request.getParameter("Icode").isEmpty()){
				sheetInfo.setIcode(request.getParameter("Icode"));
			}
			//검색 단어
			if(request.getParameter("itemContent") != null
					&& !request.getParameter("itemContent").isEmpty()){
				sheetInfo.setItemContent(request.getParameter("searchWord"));
			}
			if(request.getParameter("bPartCode") != null
					&& !request.getParameter("bPartCode").isEmpty()){
				sheetInfo.setbPartCode(request.getParameter("bPartCode"));
			}
			if (request.getParameter("mPartCode") != null
					&& !request.getParameter("mPartCode").isEmpty()) {
				sheetInfo.setmPartCode(request.getParameter("mPartCode"));
			}
			if (request.getParameter("sPartCode") != null
					&& !request.getParameter("sPartCode").isEmpty()) {
				sheetInfo.setsPartCode(request.getParameter("sPartCode"));
			}
			//소분류 명 (updatessgItem는 소분류 코드다.
			if(request.getParameter("updatessgItem") != null
					&& !request.getParameter("updatessgItem").isEmpty()){
				sheetInfo.setsSgcode(request.getParameter("updatessgItem"));
			}

			if (request.getParameter("sitemMark") != null
					&& !request.getParameter("sitemMark").isEmpty()) {
				sheetInfo.setSitemMark(request.getParameter("sitemMark"));
			}
			if (request.getParameter("itemName") != null
					&& !request.getParameter("itemName").isEmpty()) {
				sheetInfo.setItemName(request.getParameter("updateItemName"));
			}*/

			List<SheetInfo> selectResult = evaluationService.selectIgInfo(sheetInfo);
//			List<SheetInfo> selectsSgName = evaluationService.selectsSgName(sheetInfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for (int i = 0; i < selectResult.size(); i++) {
				SheetInfo item = selectResult.get(i);
//				item.setsSgName(selectsSgName.get(i).getsSgName());

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i + 1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				//체크
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);

				//순번
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(i + 1));
				rowItem.getCellElements().add(cellInfo);

				//소분류명
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);

				// 세부내용명
				cellInfo = new dhtmlXGridRowCell();
				if (item.getItemCode() == null
						|| ("".equals(item.getItemCode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getItemCode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// subitem
				cellInfo = new dhtmlXGridRowCell();
				if(i != selectResult.size()){
					if (item.getIcode() != null) {
						String stringSubItemList = "<select class=\"non_border_select\">";
						SheetInfo subItem_sheetInfo = new SheetInfo();
						subItem_sheetInfo.setIcode(item.getIcode());
						List<SheetInfo> subItemList = evaluationService.selectSubItemInfo(subItem_sheetInfo);

						if(subItemList.size()>0) {
							for(int s=0; s<subItemList.size(); s++) {
								String subItemMark = subItemList.get(s).getSitemMark();
								if(s == 0){
									stringSubItemList = stringSubItemList + "<option selected>" + subItemList.get(s).getsItemContent();
										  if(!subItemMark.contains("Input") && !subItemMark.contains("입력없음")) {
											  stringSubItemList +=  " &nbsp;&nbsp;&nbsp; : " + subItemMark + messageSource.getMessage("evaluation.statistics.point", null,Locale.getDefault())/*"점"*/;
										  }
									stringSubItemList += "</option>";
								}else{
									stringSubItemList = stringSubItemList + "<option disabled>" + subItemList.get(s).getsItemContent();
										  if(!subItemMark.contains("Input") && !subItemMark.contains("입력없음")) {
											  stringSubItemList +=  " &nbsp;&nbsp;&nbsp; : " + subItemMark + messageSource.getMessage("evaluation.statistics.point", null,Locale.getDefault())/*"점"*/ ;
										  }
								}
									stringSubItemList += "</option>";
							}
							stringSubItemList = stringSubItemList + "</select>";
						}
						cellInfo.setValue(stringSubItemList);
					}else {
						cellInfo.setValue("<div style=\"text-aling:center\">-</div>");
					}
				}else {
					cellInfo.setValue("");
				}
				rowItem.getCellElements().add(cellInfo);
				
				// submemo
				/*cellInfo = new dhtmlXGridRowCell();
				if(i != selectResult.size()){
					if (item.getIcode() != null) {
						String stringSubItemList = "<select class=\"non_border_select\">";
						SheetInfo subItem_sheetInfo = new SheetInfo();
						subItem_sheetInfo.setIcode(item.getIcode());
						List<SheetInfo> subItemList = evaluationService.selectSubMemoInfo(subItem_sheetInfo);

						if(subItemList.size()>0) {
							for(int s=0; s<subItemList.size(); s++) {
								String subItemMark = subItemList.get(s).getSitemMark();
								if(s == 0){
									stringSubItemList = stringSubItemList + "<option selected>" + subItemList.get(s).getsItemContent()"점" + "</option>";
								}else{
									stringSubItemList = stringSubItemList + "<option disabled>" + subItemList.get(s).getsItemContent()"점" + "</option>";
								}
							}
							stringSubItemList = stringSubItemList + "</select>";
						}
						cellInfo.setValue(stringSubItemList);
					}else {
						cellInfo.setValue("<div style=\"text-aling:center\">-</div>");
					}
				}else {
					cellInfo.setValue("");
				}
				rowItem.getCellElements().add(cellInfo);*/

				//사용횟수
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);

				//생성 날짜
				cellInfo = new dhtmlXGridRowCell();
				if (item.getItemMdate() == null
						|| ("".equals(item.getItemMdate().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getItemMdate().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//최종 수정 날짜
				cellInfo = new dhtmlXGridRowCell();
				if (item.getItemUdate() == null
						|| ("".equals(item.getItemUdate().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getItemUdate().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//수정
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<button class=\"ui_main_btn_flat updateInfo\" id="+(i+1)+" data-target=\"ui\"><spring:message code=\"evaluation.management.sheet.modify\"/></button>");
				rowItem.getCellElements().add(cellInfo);

				//콜백...(소분류에는 있길래 일단 가져와봄)
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);

				// 대분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getbPartCode() == null
						|| ("".equals(item.getbPartCode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getbPartCode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 중분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getmPartCode() == null
						|| ("".equals(item.getmPartCode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getmPartCode().trim());
				}
				rowItem.getCellElements().add(cellInfo);


				// 점수
				cellInfo = new dhtmlXGridRowCell();
				if (item.getMark() == null
						|| (item.getMark().trim().equals(""))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(""+item.getMark());
				}
				rowItem.getCellElements().add(cellInfo);

				//세부사항 코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getIcode() == null
						|| ("".equals(item.getIcode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getIcode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				//소분류 코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getsPartCode() == null
						|| ("".equals(item.getsPartCode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getsPartCode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);
				rowItem = null;


			}
		} else {
			xmls = null;
		}
			return xmls;
	}

}





