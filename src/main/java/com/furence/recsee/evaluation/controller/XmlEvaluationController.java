package com.furence.recsee.evaluation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.formula.functions.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
import com.furence.recsee.common.util.StringUtil;

@Controller
public class XmlEvaluationController {

	@Autowired
	private EvaluationService evaluationService;

	@Autowired
	private MenuAccessInfoService menuAccessInfoService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	private EvaluationResultInfoService evaluationResultInfoService;

	@Autowired
	private LogInfoService logInfoService;

	@Autowired
	private MessageSource messageSource;

	//gridEvalStatisticsResult
	@RequestMapping(value = "/gridEvalStatisticsResultSSSS.xml", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml gridEvalStatisticsResult(
			HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		ModelAndView result = new ModelAndView();
		result.addObject("userInfo", userInfo);
		if (userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for (int j = 0; j < 7; j++) {

				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("0");
				column.setHidden("0");

				switch (j) {

				// 순번
				case 0:
					column.setWidth("50");
					// FIXME:(언어팩)체크
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.no", null,Locale.getDefault())+"</div>");
					break;
				// 대분류
				case 1:
					column.setWidth("100");
					// FIXME:(언어팩)순번
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.big", null,Locale.getDefault())+"</div>");
					break;
				// 중분류
				case 2:
					column.setWidth("100");
					// FIXME:(언어팩)평가지 제목
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.Middle", null,Locale.getDefault())+"</div>");
					break;
				// 소분류
				case 3:
					column.setWidth("100");
					// FIXME:(언어팩)평가지 설명
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.Small", null,Locale.getDefault())+"</div>");
					break;
				// 세부항목
				case 4:
					column.setWidth("565");
					// FIXME:(언어팩) 항목 수
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.item", null,Locale.getDefault())+"</div>");
					break;
				// 채점
				case 5:
					column.setWidth("0");
					// FIXME:(언어팩) 평가 횟수
					column.setValue("<div style=\"text-align:center;\">채점</div>");
					column.setWidth("1");
					break;
				// 점수
				case 6:
					column.setWidth("100");
					// FIXME:(언어팩)평가지 단계
					column.setValue("<div style=\"text-align:center);\">"+messageSource.getMessage("evaluation.management.sheet.score2", null,Locale.getDefault())+"<div>");
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
			sheetInfo.setSheetCode(request.getParameter("sheetCode"));
			List<SheetInfo> selectResult = evaluationService.upSelectCate(sheetInfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			/*System.out.println(selectResult.size());*/
			if (selectResult.size() == 0) {

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				// 순번
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(1));
				rowItem.getCellElements().add(cellInfo);

				// 대분류
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				// 중분류
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				// 소분류
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				// 세부항목
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);


				// 점수
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);
				rowItem = null;
			} else {

				for (int i = 0; i < selectResult.size(); i++) {
					SheetInfo item = selectResult.get(i);

					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i + 1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					// 순번
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(String.valueOf(i + 1));
					rowItem.getCellElements().add(cellInfo);

					// 대분류
					cellInfo = new dhtmlXGridRowCell();
					if (item.getsBgcode() != null) {
						cellInfo.setValue(item.getsBgName());
					} else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					// 중분류
					cellInfo = new dhtmlXGridRowCell();
					if (item.getsMgcode() != null) {
						cellInfo.setValue(item.getsMgName());
					} else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					// 소분류
					cellInfo = new dhtmlXGridRowCell();
					if (item.getsSgcode() != null) {
						cellInfo.setValue(item.getsSgName());
					} else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					// 세부항목
					cellInfo = new dhtmlXGridRowCell();
					if (item.getIcode() != null) {
						cellInfo.setValue("<select class=\"igList\" id=\"igList_eval\"><option value="
								+ item.getIcode().trim()
								+ ">"
								+ item.getItemCode() + "</option></select>");
					} else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					// 채점
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("채점");
					rowItem.getCellElements().add(cellInfo);

					// 점수
					cellInfo = new dhtmlXGridRowCell();
					if (item.getCategoryICode() == null
							|| ("".equals(item.getCategoryICode().trim()))) {
						cellInfo.setValue("");
					} else {
						cellInfo.setValue(""+item.getMark());
					}
					rowItem.getCellElements().add(cellInfo);

					xmls.getRowElements().add(rowItem);
					rowItem = null;
					item =null;

				}
			}

		} else {
			xmls = null;

		}
		return xmls;
	}


	// evaluation_manage Sheet 뿌리기
	@RequestMapping(value = "/evManage.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml evManage(HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		ModelAndView result = new ModelAndView();
		result.addObject("userInfo", userInfo);
		if (userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for (int j = 0; j < 17; j++) {

				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("0");
				column.setHidden("0");

				switch (j) {

				// 체크
				case 0:
					column.setWidth("30");
					column.setType("ch");
					column.setSort("na");
					// FIXME:(언어팩)체크
					column.setValue("<div id='allcheck' style=\"cursor: pointer; top:2px;\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif'/></div>");
					break;
				// 순번
				case 1:
					column.setWidth("50");
					// FIXME:(언어팩)순번
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.no", null,Locale.getDefault())+"</div>");
					break;
				// 평가지 제목
				case 2:
					column.setWidth("*");
					// FIXME:(언어팩)평가지 제목
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.sheetname", null,Locale.getDefault())+"</div>");
					break;
				// 평가지 설명
				case 3:
					column.setWidth("*");
					// FIXME:(언어팩)평가지 설명
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.sheetdesc", null,Locale.getDefault())+"</div>");
					break;
				// 항목 수
				case 4:
					column.setWidth("70");
					// FIXME:(언어팩) 항목 수
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.itemcount", null,Locale.getDefault())+"</div>");
					break;
				// 평가 횟수
				case 5:
					column.setWidth("0");
					// FIXME:(언어팩) 평가 횟수
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">평가 횟수</div>");
					break;
				// 평가지 단계
				case 6:
					column.setWidth("100");
					// FIXME:(언어팩)평가지 단계
					column.setValue("<div style=\"text-align:center);\">"+messageSource.getMessage("evaluation.management.sheet.stepofeval", null,Locale.getDefault())+"<div>");
					break;
				// 총 점수
				case 7:
					column.setWidth("50");
					column.setHidden("1");
					// FIXME:(언어팩)총 점수
					column.setValue("<div style=\"text-align:center);\">"+messageSource.getMessage("evaluation.management.sheet.score2", null,Locale.getDefault())+"<div>");
					break;
				// 생성 날짜
				case 8:
					column.setWidth("100");
					// FIXME:(언어팩) 생성 날짜
					column.setValue("<div style=\"text-align:center);\">"+messageSource.getMessage("evaluation.management.sheet.createdate", null,Locale.getDefault())+"<div>");
					break;
				// 최종 수정 날짜
				case 9:
					column.setWidth("100");
					column.setHidden("1");
					// FIXME:(언어팩) 최종 수정 날짜
					column.setValue("<div style=\"text-align:center);\">"+messageSource.getMessage("evaluation.management.sheet.modifydate", null,Locale.getDefault())+"<div>");
					break;
				// 생성자
				case 10:
					column.setWidth("80");
					// FIXME:(언어팩) 생성자
					column.setValue("<div style=\"text-align:center);\">"+messageSource.getMessage("evaluation.management.sheet.creator", null,Locale.getDefault())+"<div>");
					break;
				// 수정
				case 11:
					column.setWidth("80");
					// FIXME:(언어팩) 수정
//					column.setValue("<div style=\"text-align:center);\">"+messageSource.getMessage("evaluation.management.sheet.modify", null,Locale.getDefault())+"<div>");
					column.setValue(messageSource.getMessage("evaluation.management.sheet.detail", null,Locale.getDefault()));
					break;
				// 평가 코드
				case 12:
					column.setWidth("0");
					column.setHidden("1");
					// FIXME:(언어팩) sheet코드
					column.setValue("<div style=\"text-align:center);\">평가코드<div>");
					break;
				// 평가 사용 여부
				case 13:
					column.setWidth("0");
					column.setHidden("1");
					// FIXME:(언어팩)평가 사용 여부
					column.setValue("<div style=\"text-align:center;\">평가 사용 여부</div>");
					break;
				// 대분류 코드
				case 14:
					column.setWidth("0");
					column.setHidden("1");
					// FIXME:(언어팩)대분류 코드
					column.setValue("<div style=\"text-align:center;\">대분류 코드</div>");
					break;
				// 중분류 코드
				case 15:
					column.setWidth("0");
					column.setHidden("1");
					// FIXME:(언어팩) 중분류 코드
					column.setValue("<div style=\"text-align:center;\">중분류 코드</div>");
					break;
				// 소분류 코드
				case 16:
					column.setWidth("0");
					column.setHidden("1");
					// FIXME:(언어팩) 소분류 코드
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

//			if (request.getParameter("searchStep") != null && !request.getParameter("searchStep").isEmpty()) {
//				sheetInfo.setSearchStep(request.getParameter("searchStep"));
//			}
//			if (request.getParameter("searchWord") != null && !request.getParameter("searchWord").isEmpty()) {
//				sheetInfo.setSearchWord(request.getParameter("searchWord"));
//			}
//			if (request.getParameter("searchType") != null && !request.getParameter("searchType").isEmpty()) {
//				sheetInfo.setSearchType(request.getParameter("searchType"));
//			}
//			if (request.getParameter("sheetName") != null && !request.getParameter("sheetName").isEmpty()) {
//				sheetInfo.setSheetName(request.getParameter("sheetName"));
//			}
//			if (request.getParameter("sheetCode") != null && !request.getParameter("sheetCode").isEmpty()) {
//				sheetInfo.setSheetName(request.getParameter("sheetCode"));
//			}
//			if (request.getParameter("sheetCreator") != null && !request.getParameter("sheetCreator").isEmpty()) {
//				sheetInfo.setSheetName(request.getParameter("sheetCreator"));
//			}
//			if (request.getParameter("sheetDepth") != null && !request.getParameter("sheetDepth").isEmpty()) {
//				sheetInfo.setSheetName(request.getParameter("sheetDepth"));
//			}
//			if (request.getParameter("sheetMdate") != null && !request.getParameter("sheetMdate").isEmpty()) {
//				sheetInfo.setSheetName(request.getParameter("sheetMdate"));
//			}
//			if (request.getParameter("sheetWdate") != null && !request.getParameter("sheetWdate").isEmpty()) {
//				sheetInfo.setSheetName(request.getParameter("sheetWdate"));
//			}
//			if (request.getParameter("sheetUsingYn") != null && !request.getParameter("sheetUsingYn").isEmpty()) {
//				sheetInfo.setSheetName(request.getParameter("sheetUsingYn"));
//			}
//			if (request.getParameter("bPartCode") != null && !request.getParameter("bPartCode").isEmpty()) {
//				sheetInfo.setSheetName(request.getParameter("bPartCode"));
//			}
//			if (request.getParameter("mPartCode") != null && !request.getParameter("mPartCode").isEmpty()) {
//				sheetInfo.setSheetName(request.getParameter("mPartCode"));
//			}
//			if (request.getParameter("sPartCode") != null && !request.getParameter("sPartCode").isEmpty()) {
//				sheetInfo.setSheetName(request.getParameter("sPartCode"));
//			}

			List<SheetInfo> selectResult = evaluationService.selectSheetList(sheetInfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for (int i = 0; i < selectResult.size(); i++) {
				SheetInfo item = selectResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i + 1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				// 체크
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("0");
				rowItem.getCellElements().add(cellInfo);

				// 순번
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(i + 1));
				rowItem.getCellElements().add(cellInfo);

				// 평가지 제목
				cellInfo = new dhtmlXGridRowCell();
				if (item.getSheetName() == null
						|| ("".equals(item.getSheetName().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getSheetName().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 평가지 설명
				cellInfo = new dhtmlXGridRowCell();
				if (item.getSheetContent() == null
						|| ("".equals(item.getSheetContent().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getSheetContent().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 항목수
				cellInfo = new dhtmlXGridRowCell();
				if (item.getRowNum() == null
						|| (item.getRowNum().toString().trim().equals(""))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getRowNum().toString().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 평가 횟수
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);

				// 평가지 단계
				cellInfo = new dhtmlXGridRowCell();
				if (item.getSheetDepth() == null
						|| (item.getSheetDepth().toString().trim().equals(""))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getSheetDepth().toString().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 총 점수
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				// 생성 날짜
				cellInfo = new dhtmlXGridRowCell();
				if (item.getSheetMdate() == null
						|| ("".equals(item.getSheetMdate().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue("<div>"
							+ item.getSheetMdate().trim() + "</div>");
				}
				rowItem.getCellElements().add(cellInfo);

				// 수정 날짜
				cellInfo = new dhtmlXGridRowCell();
				if (item.getSheetUdate() == null
						|| ("".equals(item.getSheetUdate().trim()))) {
					cellInfo.setValue("");
				} else {
					// cellInfo.setValue("<img src=\"/recsee/resources/common/recsee/images/project/icon/form_icon/icon_input_cal.png\">"
					// +item.getSheetUdate().trim()+ "</img>");
					cellInfo.setValue("<div>"
							+ item.getSheetUdate().trim() + "</div>");
				}

				rowItem.getCellElements().add(cellInfo);

				// 생성자
				cellInfo = new dhtmlXGridRowCell();
				if (item.getSheetCreator() == null
						|| ("".equals(item.getSheetCreator().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getSheetCreator().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 수정
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<button class=\"ui_main_btn_flat radius_button updateSheet\" id=" + (i + 1) + " data-target=\"us\" "
									+ "onclick=\"updateEvalSheet()\">"
//									+ messageSource.getMessage("evaluation.management.sheet.modify", null,Locale.getDefault())
									+ messageSource.getMessage("evaluation.management.sheet.detail", null,Locale.getDefault()) + "</button>"
								);
				rowItem.getCellElements().add(cellInfo);

				// 평가 코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getSheetCode() == null
						|| ("".equals(item.getSheetCode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getSheetCode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 평가 사용 여부
				cellInfo = new dhtmlXGridRowCell();
				if (item.getSheetUsingYn() == null
						|| ("".equals(item.getSheetUsingYn().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getSheetUsingYn().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 대분류코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getBcode() == null
						|| ("".equals(item.getBcode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getBcode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 중분류코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getMcode() == null
						|| ("".equals(item.getMcode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getMcode().trim());
				}
				rowItem.getCellElements().add(cellInfo);

				// 소분류코드
				cellInfo = new dhtmlXGridRowCell();
				if (item.getScode() == null
						|| ("".equals(item.getScode().trim()))) {
					cellInfo.setValue("");
				} else {
					cellInfo.setValue(item.getScode().trim());
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



	// 평가지 만들기 그리드
	@RequestMapping(value = "/evaluationMakeGrid.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml evaluationMakeGrid(HttpServletRequest request, HttpServletResponse response) {
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if (userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for (int j = 0; j < 13; j++) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("na");
				column.setAlign("center");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("0");

				switch (j) {
				// 체크
				case 0:
					column.setWidth("50");
					column.setType("ch");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.check", null,Locale.getDefault())+"</div>");
					break;
				// 대분류
				case 1:
					column.setWidth("150");
					// FIXME:(언어팩)대분류
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.big", null,Locale.getDefault())+"</div>");
					break;
				// 중분류
				case 2:
					column.setWidth("150");
					// FIXME:(언어팩)중분류
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.Middle", null,Locale.getDefault())+"</div>");
					break;
				// 소분류
				case 3:
					column.setWidth("150");
					// FIXME:(언어팩) 소분류
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.Small", null,Locale.getDefault())+"</div>");
					break;
				// item
				case 4:
					column.setWidth("150");
					// FIXME:(언어팩) 세부사항
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.item", null,Locale.getDefault())+"</div>");
					break;
				// sub item
				case 5:
					column.setWidth("*");
					// FIXME:(언어팩) 세부사항
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.subitem", null,Locale.getDefault())+"</div>");
					break;
				// 합계
				case 6:
					column.setWidth("50");
					// FIXME:(언어팩) 점수
//					column.setType("edn");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.score2", null,Locale.getDefault())+"</div>");
					break;
				// 삭제
				case 7:
					column.setWidth("50");
					// FIXME:(언어팩) 수정
//					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.modify", null,Locale.getDefault())+"</div>");
					column.setValue("");
					break;
				// re_sbg_code
				case 8:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">reg_sbg_code</div>");
					break;
				// re_smg_code
				case 9:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">reg_smg_code</div>");
					break;
				// re_ssg_code
				case 10:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">reg_ssg_code</div>");
					break;
				// re_item_code
				case 11:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">reg_item_code</div>");
					break;
				// re_sub_item_code
				case 12:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">reg_sub_item_code</div>");
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

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			String sheetCode = request.getParameter("sheetCode");
			sheetInfo.setSheetCode(sheetCode);

			List<SheetInfo> selectResult = evaluationService.upSelectCate(sheetInfo);
			if (selectResult.size() == 0) {
				for (int i = 0; i < 2; i++) {
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					if(i == 0){
						rowItem.setId(String.valueOf(i + 1));
					}else{
						rowItem.setId(String.valueOf("btn"));
					}
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					// 체크
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("1");
					rowItem.getCellElements().add(cellInfo);

					// 대분류
					cellInfo = new dhtmlXGridRowCell();
					if(i == 0){
						cellInfo.setValue("<select class=\"bgList non_border_select\"></select>");
					}else{
						cellInfo.setValue("<button class=\"ui_main_btn_flat radius_button addbgCode\" style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease;\" id=\"addbg\" onclick=\"evalListAddButton('addbg')\">"+messageSource.getMessage("evaluation.management.sheet.add", null,Locale.getDefault())/*추가*/+"</button>");
					}
					rowItem.getCellElements().add(cellInfo);

					// 중분류
					cellInfo = new dhtmlXGridRowCell();
					if(i == 0){
						cellInfo.setValue("<select class=\"mgList non_border_select\"></select>");
					}else{
						cellInfo.setValue("<button class=\"ui_main_btn_flat radius_button addmgCode\" style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease;\" id=\"addmg\" onclick=\"evalListAddButton('addmg')\">"+messageSource.getMessage("evaluation.management.sheet.add", null,Locale.getDefault())/*추가*/+"</button>");
					}
					rowItem.getCellElements().add(cellInfo);

					// 소분류
					cellInfo = new dhtmlXGridRowCell();
					if(i == 0){
						cellInfo.setValue("<select class=\"sgList non_border_select\"></select>");
					}else{
						cellInfo.setValue("<button class=\"ui_main_btn_flat radius_button addsgCode\" style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease;\" id=\"addsg\" onclick=\"evalListAddButton('addsg')\">"+messageSource.getMessage("evaluation.management.sheet.add", null,Locale.getDefault())/*추가*/+"</button>");
					}
					rowItem.getCellElements().add(cellInfo);

					// item
					cellInfo = new dhtmlXGridRowCell();
					if(i == 0){
						cellInfo.setValue("<select class='igList non_border_select' id=\"igList_make_sheet\"></select>");
					}else{
						cellInfo.setValue("<button class=\"ui_main_btn_flat radius_button additemCode\" style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease;\" id=\"additem\" onclick=\"evalListAddButton('additem')\">"+messageSource.getMessage("evaluation.management.sheet.add", null,Locale.getDefault())/*추가*/+"</button>");
					}
					rowItem.getCellElements().add(cellInfo);

					// sub item
					cellInfo = new dhtmlXGridRowCell();
					if(i == 0) {
						cellInfo.setValue("<div style=\"text-aling:center\">-</div>");
					}else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					// 점수
					cellInfo = new dhtmlXGridRowCell();
					if(i==0) {
						cellInfo.setValue("<div>0</div>");
					}else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					// 삭제 버튼
					cellInfo = new dhtmlXGridRowCell();
//					if(i==0) {
//						cellInfo.setValue("<button id=\"evalPage_DeleteRow\" class=\"ui_btn_white icon_btn_trash_gray\" onclick=\"deleteRowFunc()\"></button>");
//					}else {
//						cellInfo.setValue("");
//					}
					cellInfo.setValue("");  //첫번째열은 삭제를 못하게 하기 위해 공백으로 비워둠
					rowItem.getCellElements().add(cellInfo);

					// sbg_code
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					// smg_code
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					// ssg_code
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					// item_code
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					// sub_item_code
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					xmls.getRowElements().add(rowItem);
					rowItem = null;
				}
			} else {
//				System.out.println("size >>> "+selectResult.size());
				for (int i = 0; i < selectResult.size(); i++) {
					SheetInfo item = selectResult.get(i);

					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i + 1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					// 체크
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("1");
					rowItem.getCellElements().add(cellInfo);

					// 대분류
					cellInfo = new dhtmlXGridRowCell();
					if (item.getCategoryBCode() != null) {
						cellInfo.setValue("<select class=\"bgList non_border_select\" save='"+item.getCategoryBCode().trim()+"'></option></select>");
					} else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					// 중분류
					cellInfo = new dhtmlXGridRowCell();
					if (item.getCategoryMCode() != null) {
						cellInfo.setValue("<select class=\"mgList non_border_select\" save='"+item.getCategoryMCode().trim()+"'></select>");
					} else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					// 소분류
					cellInfo = new dhtmlXGridRowCell();
					if (item.getCategorySCode() != null) {
						cellInfo.setValue("<select class=\"sgList non_border_select\" save='"+item.getCategorySCode().trim()+"'></select>");
					} else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					// item
					cellInfo = new dhtmlXGridRowCell();
					if (item.getIcode() != null) {
						cellInfo.setValue("<select class='igList non_border_select' id=\"igList_make_sheet\"  save='"+item.getIcode().trim().trim()+"'></select>");
					} else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					// sub item
					cellInfo = new dhtmlXGridRowCell();
					if (item.getIcode() != null) {
						String stringSubItemList = "<select class=\"non_border_select\">";
						SheetInfo subItem_sheetInfo = new SheetInfo();
						subItem_sheetInfo.setIcode(item.getIcode());
						List<SheetInfo> subItemList = evaluationService.selectSubItemInfo(subItem_sheetInfo);
						if(subItemList.size()>0) {
							/*for(int s=0; s<subItemList.size(); s++) {
								String subItemMark = subItemList.get(s).getSitemMark();
								if(s == 0)
									stringSubItemList = stringSubItemList + "<option disabled selected>" + subItemList.get(s).getsItemContent() +" &nbsp;&nbsp;&nbsp; *P : " + subItemMark + "</option>";
								else
									stringSubItemList = stringSubItemList + "<option disabled>" + subItemList.get(s).getsItemContent() +" &nbsp;&nbsp;&nbsp; *P : " + subItemMark + "</option>";
							}*/
							for(int s=0; s<subItemList.size(); s++) {
								String subItemMark = subItemList.get(s).getSitemMark();
								if(s == 0){
									stringSubItemList = stringSubItemList + "<option disabled selected>" + subItemList.get(s).getsItemContent();
										  if(!subItemMark.contains("Input") && !subItemMark.contains("입력없음")) {
											  stringSubItemList +=  " &nbsp;&nbsp;&nbsp; *P : " + subItemMark;
										  }
									stringSubItemList += "</option>";
								}else{
									stringSubItemList = stringSubItemList + "<option disabled>" + subItemList.get(s).getsItemContent();
									if(!subItemMark.contains("Input") && !subItemMark.contains("입력없음")) {
											  stringSubItemList +=  " &nbsp;&nbsp;&nbsp; *P : " + subItemMark;
										  }
									stringSubItemList += "</option>";
								}
							}
							stringSubItemList = stringSubItemList + "</select>";
						}
						cellInfo.setValue(stringSubItemList);
					}else {
						cellInfo.setValue("<div style=\"text-aling:center\">-</div>");
					}
					rowItem.getCellElements().add(cellInfo);

					// 점수
					cellInfo = new dhtmlXGridRowCell();
					if (item.getMark() != null) {
						cellInfo.setValue((item.getMark()));
					}else {
						cellInfo.setValue("0");
					}
					rowItem.getCellElements().add(cellInfo);

					// 수정
					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue("<button class=\"ui_main_btn_flat updateCode\" id="
//							+ (i + 1) + ">" +messageSource.getMessage("evaluation.management.sheet.modify", null,Locale.getDefault())+"</button>");
					cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					// reg_sbg_code
					cellInfo = new dhtmlXGridRowCell();
					if (item.getCategoryBCode() == null	|| ("".equals(item.getCategoryBCode().trim()))) {
						cellInfo.setValue("");
					} else {
						cellInfo.setValue(item.getCategoryBCode());
					}
					rowItem.getCellElements().add(cellInfo);

					// reg_smg_code
					cellInfo = new dhtmlXGridRowCell();
					if (item.getCategoryMCode() == null	|| ("".equals(item.getCategoryMCode().trim()))) {
						cellInfo.setValue("");
					} else {
						cellInfo.setValue(item.getCategoryMCode());
					}
					rowItem.getCellElements().add(cellInfo);

					// reg_ssg_code
					cellInfo = new dhtmlXGridRowCell();
					if (item.getCategorySCode() == null	|| ("".equals(item.getCategorySCode().trim()))) {
						cellInfo.setValue("");
					} else {
						cellInfo.setValue(item.getCategorySCode());
					}
					rowItem.getCellElements().add(cellInfo);

					// item_code
					cellInfo = new dhtmlXGridRowCell();
					//getCategoryICode()
					if (item.getIcode() == null	|| ("".equals(item.getIcode().trim()))) {
						cellInfo.setValue("");
					} else {
						cellInfo.setValue(item.getIcode());
					}
					rowItem.getCellElements().add(cellInfo);

					// sub_item_code
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					xmls.getRowElements().add(rowItem);
					rowItem = null;





//					if(i == selectResult.size()) {
//						item = selectResult.get(i-1);
//					}else {
//						item = selectResult.get(i);
//					}
//
//
//					dhtmlXGridRow rowItem = new dhtmlXGridRow();
//					rowItem.setId(String.valueOf(i + 1));
//					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());
//
//					// 체크
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue("1");
//					rowItem.getCellElements().add(cellInfo);
//
//					// 대분류
//					cellInfo = new dhtmlXGridRowCell();
//					if(i != selectResult.size()){
//						if (item.getCategoryBCode() != null) {
//							cellInfo.setValue("<select class=\"bgList non_border_select\" save='"+item.getCategoryBCode().trim()+"'></option></select>");
//						} else {
//							cellInfo.setValue("");
//						}
//					}else {
//						//추가 버튼
////						cellInfo.setValue("<button class=\"ui_main_btn_flat radius_button addbgCode\" style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease;\" id=\"addbg\" onclick=\"evalListAddButton('addbg')\">추가</button>");
//						cellInfo.setValue("");
//					}
//					rowItem.getCellElements().add(cellInfo);
//
//					// 중분류
//					cellInfo = new dhtmlXGridRowCell();
//					if(i != selectResult.size()){
//						if (item.getCategoryMCode() != null) {
//							cellInfo.setValue("<select class=\"mgList non_border_select\" save='"+item.getCategoryMCode().trim()+"'></select>");
//						} else {
//							cellInfo.setValue("");
//						}
//					}else{
//						//추가 버튼
////						cellInfo.setValue("<button class=\"ui_main_btn_flat radius_button addmgCode\" style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease;\" id=\"addmg\" onclick=\"evalListAddButton('addmg')\">추가</button>");
//						cellInfo.setValue("");
//					}
//					rowItem.getCellElements().add(cellInfo);
//
//					// 소분류
//					cellInfo = new dhtmlXGridRowCell();
//					if(i != selectResult.size()){
//						if (item.getCategorySCode() != null) {
//							cellInfo.setValue("<select class=\"sgList non_border_select\" save='"+item.getCategorySCode().trim()+"'></select>");
//						} else {
//							cellInfo.setValue("");
//						}
//					}else{
//						//추가버튼
////						cellInfo.setValue("<button class=\"ui_main_btn_flat radius_button addsgCode\" style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease;\" id=\"addsg\" onclick=\"evalListAddButton('addsg')\">추가</button>");
//						cellInfo.setValue("");
//					}
//					rowItem.getCellElements().add(cellInfo);
//
//					// item
//					cellInfo = new dhtmlXGridRowCell();
//					if(i != selectResult.size()){
//						if (item.getIcode() != null) {
//							cellInfo.setValue("<select class='igList non_border_select' id=\"igList_make_sheet\"  save='"+item.getIcode().trim().trim()+"'></select>");
//						} else {
//							cellInfo.setValue("");
//						}
//					}else{
//						//추가버튼
////						cellInfo.setValue("<button class=\"ui_main_btn_flat radius_button additemCode\" style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease;\" id=\"additem\" onclick=\"evalListAddButton('additem')\">추가</button>");
//						cellInfo.setValue("");
//					}
//					rowItem.getCellElements().add(cellInfo);
//
//					// sub item
//					cellInfo = new dhtmlXGridRowCell();
//					if(i != selectResult.size()){
//						if (item.getIcode() != null) {
//							String stringSubItemList = "<select class=\"non_border_select\">";
//							SheetInfo subItem_sheetInfo = new SheetInfo();
//							subItem_sheetInfo.setIcode(item.getIcode());
//							List<SheetInfo> subItemList = evaluationService.selectSubItemInfo(subItem_sheetInfo);
//							if(subItemList.size()>0) {
//								for(int s=0; s<subItemList.size(); s++) {
//									String subItemMark = subItemList.get(s).getSitemMark();
//									stringSubItemList = stringSubItemList + "<option disabled>" + subItemList.get(s).getsItemCode() +" &nbsp;&nbsp;&nbsp; *P : " + subItemMark + "</option>";
//
//								}
//								stringSubItemList = stringSubItemList + "</select>";
//							}
//							cellInfo.setValue(stringSubItemList);
//						}else {
//							cellInfo.setValue("<div style=\"text-aling:center\">-</div>");
//						}
//					}else {
//						cellInfo.setValue("");
//					}
//					rowItem.getCellElements().add(cellInfo);
//
//					// 점수
//					cellInfo = new dhtmlXGridRowCell();
//					if(i != selectResult.size()){
//						if (item.getMark() != null) {
//							cellInfo.setValue((item.getMark()));
//						}else {
//							cellInfo.setValue("0");
//						}
//					}else {
//						cellInfo.setValue("");
//					}
//					rowItem.getCellElements().add(cellInfo);
//
//					// 수정
//					cellInfo = new dhtmlXGridRowCell();
////					cellInfo.setValue("<button class=\"ui_main_btn_flat updateCode\" id="
////							+ (i + 1) + ">" +messageSource.getMessage("evaluation.management.sheet.modify", null,Locale.getDefault())+"</button>");
//					cellInfo.setValue("");
//					rowItem.getCellElements().add(cellInfo);
//
//					// reg_sbg_code
//					cellInfo = new dhtmlXGridRowCell();
//					if(i != selectResult.size()){
//						if (item.getCategoryBCode() == null
//								|| ("".equals(item.getCategoryBCode().trim()))) {
//							cellInfo.setValue("");
//						} else {
//							cellInfo.setValue(item.getCategoryBCode());
//						}
//					}else {
//						cellInfo.setValue("");
//					}
//					rowItem.getCellElements().add(cellInfo);
//
//					// reg_smg_code
//					cellInfo = new dhtmlXGridRowCell();
//					if(i != selectResult.size()){
//						if (item.getCategoryMCode() == null
//								|| ("".equals(item.getCategoryMCode().trim()))) {
//							cellInfo.setValue("");
//						} else {
//							cellInfo.setValue(item.getCategoryMCode());
//						}
//					}else {
//						cellInfo.setValue("");
//					}
//					rowItem.getCellElements().add(cellInfo);
//
//					// reg_ssg_code
//					cellInfo = new dhtmlXGridRowCell();
//					if(i != selectResult.size()){
//						if (item.getCategorySCode() == null
//								|| ("".equals(item.getCategorySCode().trim()))) {
//							cellInfo.setValue("");
//						} else {
//							cellInfo.setValue(item.getCategorySCode());
//						}
//					}else {
//						cellInfo.setValue("");
//					}
//					rowItem.getCellElements().add(cellInfo);
//
//					// item_code
//					cellInfo = new dhtmlXGridRowCell();
//					if(i != selectResult.size()){
//						if (item.getCategoryICode() == null
//								|| ("".equals(item.getCategoryICode().trim()))) {
//							cellInfo.setValue("");
//						} else {
//							cellInfo.setValue(item.getCategoryICode());
//						}
//					}else {
//						cellInfo.setValue("");
//					}
//					rowItem.getCellElements().add(cellInfo);
//
//					// sub_item_code
//					cellInfo = new dhtmlXGridRowCell();
//					cellInfo.setValue("");
//					rowItem.getCellElements().add(cellInfo);
//
//					xmls.getRowElements().add(rowItem);
//					rowItem = null;
				}
				if(!StringUtil.isNull(request.getParameter("copyFlag"))){
//					System.out.println("copy start !!!");
					String CopyFlag = request.getParameter("copyFlag");
					if(CopyFlag.equals("Y")) { // 캠페인 복사 할때는 마지막 줄에 버튼 넣어주기
						dhtmlXGridRow rowItem = new dhtmlXGridRow();

						rowItem.setId(String.valueOf("btn"));
						rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

						// 체크
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("1");
						rowItem.getCellElements().add(cellInfo);

						// 대분류
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("<button class=\"ui_main_btn_flat radius_button addbgCode\" style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease;\" id=\"addbg\" onclick=\"evalListAddButton('addbg')\">추가</button>");
						rowItem.getCellElements().add(cellInfo);

						// 중분류
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("<button class=\"ui_main_btn_flat radius_button addmgCode\" style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease;\" id=\"addmg\" onclick=\"evalListAddButton('addmg')\">추가</button>");
						rowItem.getCellElements().add(cellInfo);

						// 소분류
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("<button class=\"ui_main_btn_flat radius_button addsgCode\" style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease;\" id=\"addsg\" onclick=\"evalListAddButton('addsg')\">추가</button>");
						rowItem.getCellElements().add(cellInfo);

						// item
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("<button class=\"ui_main_btn_flat radius_button additemCode\" style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease;\" id=\"additem\" onclick=\"evalListAddButton('additem')\">추가</button>");
						rowItem.getCellElements().add(cellInfo);

						// sub item
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("");
						rowItem.getCellElements().add(cellInfo);

						// 점수
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("");
						rowItem.getCellElements().add(cellInfo);

						// 삭제 버튼
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("");
						rowItem.getCellElements().add(cellInfo);

						// sbg_code
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("");
						rowItem.getCellElements().add(cellInfo);

						// smg_code
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("");
						rowItem.getCellElements().add(cellInfo);

						// ssg_code
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("");
						rowItem.getCellElements().add(cellInfo);

						// item_code
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("");
						rowItem.getCellElements().add(cellInfo);

						// sub_item_code
						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("");
						rowItem.getCellElements().add(cellInfo);

						xmls.getRowElements().add(rowItem);
						rowItem = null;
					}
				}
			}
		} else {
			xmls = null;
		}
		return xmls;
	}




	// 평가지 만들기 그리드
	/*@RequestMapping(value = "/evaluationGrid.xml", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml evaluationGrid(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if (userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for (int j = 0; j < 14; j++) {
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("na");
//				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("0");

				switch (j) {
				// 체크
				case 0:
					column.setWidth("50");
					column.setType("ch");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.check", null,Locale.getDefault())+"</div>");
					break;
				// 순번
				case 1:
					column.setWidth("50");
					// FIXME:(언어팩)순번
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.no", null,Locale.getDefault())+"</div>");
					break;
				// 대분류
				case 2:
					column.setWidth("120");
					// FIXME:(언어팩)대분류
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.big", null,Locale.getDefault())+"</div>");
					break;
				// 중분류
				case 3:
					column.setWidth("120");
					// FIXME:(언어팩)중분류
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.Middle", null,Locale.getDefault())+"</div>");
					break;
				// 소분류
				case 4:
					column.setWidth("120");
					// FIXME:(언어팩) 소분류
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.Small", null,Locale.getDefault())+"</div>");
					break;
				// item
				case 5:
					column.setWidth("120");
					// FIXME:(언어팩) 세부사항
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.item", null,Locale.getDefault())+"</div>");
					break;
				// sub item
				case 6:
					column.setWidth("170");
					// FIXME:(언어팩) 세부사항
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.subitem", null,Locale.getDefault())+"</div>");
					break;
				// 점수
				case 7:
					column.setWidth("50");
					// FIXME:(언어팩) 점수
					column.setType("edn");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.score2", null,Locale.getDefault())+"</div>");
					break;
				// 수정
				case 8:
					column.setWidth("80");
					// FIXME:(언어팩) 수정
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.modify", null,Locale.getDefault())+"</div>");
					break;
				// 공백
				case 9:
					column.setWidth("");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\"></div>");
					break;
				// reg_sbg_code
				case 10:
					column.setWidth("");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">reg_sbg_code</div>");
					break;
				// reg_smg_code
				case 11:
					column.setWidth("");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">reg_smg_code</div>");
					break;
				// reg_ssg_code
				case 12:
					column.setWidth("");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">reg_ssg_code</div>");
					break;
				// reg_item_code
				case 13:
					column.setWidth("");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">reg_item_code</div>");
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

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			String sheetCode = request.getParameter("sheetCode");
			sheetInfo.setSheetCode(sheetCode);

			List<SheetInfo> selectResult = evaluationService.upSelectCate(sheetInfo);
			System.out.println("result Size :::::::::::: "+selectResult.size());
			if (selectResult.size() == 0) {
				System.out.println("for lang1 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

				for (int i = 0; i < 2; i++) {
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i + 1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					// 체크
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("1");
					rowItem.getCellElements().add(cellInfo);

					// 순번
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(String.valueOf(i + 1));
					rowItem.getCellElements().add(cellInfo);

					// 대분류
					cellInfo = new dhtmlXGridRowCell();
					if(i == 0){
						cellInfo.setValue("<select class=\"bgList\"></select>");
					}else{
						cellInfo.setValue("<button class=\"ui_main_btn_flat addbgCode\" style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease;\" id=\"addbg\">추가</button>");
					}
					rowItem.getCellElements().add(cellInfo);

					// 중분류
					cellInfo = new dhtmlXGridRowCell();
					if(i == 0){
						cellInfo.setValue("<select class=\"mgList\"></select>");
					}else{
						cellInfo.setValue("<button class=\"ui_main_btn_flat addmgCode\" style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease;\" id=\"addmg\">추가</button>");
					}
					rowItem.getCellElements().add(cellInfo);

					// 소분류
					cellInfo = new dhtmlXGridRowCell();
					if(i == 0){
						cellInfo.setValue("<select class=\"sgList\"></select>");
					}else{
						cellInfo.setValue("<button class=\"ui_main_btn_flat addsgCode\" style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease;\" id=\"addsg\">추가</button>");
					}
					rowItem.getCellElements().add(cellInfo);

					// 세부사항
					cellInfo = new dhtmlXGridRowCell();
					if(i == 0){
						cellInfo.setValue("<select class='igList' id=\"igList_make_sheet\"></select>");
					}else{
						cellInfo.setValue("<button class=\"ui_main_btn_flat additemCode\" style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease;\" id=\"additem\">추가</button>");
					}
					rowItem.getCellElements().add(cellInfo);

					// 상세 점수 사항
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("");
					rowItem.getCellElements().add(cellInfo);

					// 점수
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("<div>0</div>");
					rowItem.getCellElements().add(cellInfo);

					xmls.getRowElements().add(rowItem);
					rowItem = null;
				}
				System.out.println("for lang2 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			} else {

				for (int i = 0; i < selectResult.size(); i++) {

					SheetInfo item = selectResult.get(i);

					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i + 1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					// 체크
					cellInfo = new dhtmlXGridRowCell();
					rowItem.getCellElements().add(cellInfo);

					// 순번
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(String.valueOf(i + 1));
					rowItem.getCellElements().add(cellInfo);

					// 대분류
					cellInfo = new dhtmlXGridRowCell();
					if(i != selectResult.size()+1){
						if (item.getsBgcode() != null) {
							cellInfo.setValue("<select class=\"bgList\" id=\"b" + i +"\"><option value="
									+ item.getsBgcode().trim()
									+ ">"
									+ item.getsBgName() + "</option></select>");
						} else {
							cellInfo.setValue("");
						}
					}else {
						//추가 버튼
						cellInfo.setValue("<button class=\"ui_main_btn_flat addbutton\" id="
								+ (i + 1) + ">추가</button>");
					}
					rowItem.getCellElements().add(cellInfo);

					// 중분류
					cellInfo = new dhtmlXGridRowCell();
					if(i != selectResult.size()+1){
						if (item.getsMgcode() != null) {
							cellInfo.setValue("<select class=\"mgList\"id=\"m" + i +"\"><option value="
									+ item.getsMgcode().trim()
									+ ">"
									+ item.getsMgName() + "</option></select>");
						} else {
							cellInfo.setValue("");
						}
					}else{
						//추가 버튼
					}
					rowItem.getCellElements().add(cellInfo);

					// 소분류
					cellInfo = new dhtmlXGridRowCell();
					if(i != selectResult.size()+1){
						if (item.getsSgcode() != null) {
							cellInfo.setValue("<select class=\"sgList\"id=\"s" + i +"\"><option value="
									+ item.getsSgcode().trim()
									+ ">"
									+ item.getsSgName() + "</option></select>");

						} else {
							cellInfo.setValue("");
						}
					}else{
						//추가버튼

					}
					rowItem.getCellElements().add(cellInfo);

					// 세부사항
					cellInfo = new dhtmlXGridRowCell();
					if(i != selectResult.size()+1){
						if (item.getIcode() != null) {
							cellInfo.setValue("<select class=\"igList\" id=\"igList_update_sheet\"><option value="
									+ item.getIcode().trim()
									+ ">"
									+ item.getItemCode() + "</option></select>");
						} else {
							cellInfo.setValue("");
						}
					}else{
						//추가버튼
					}
					rowItem.getCellElements().add(cellInfo);

					// 점수
					cellInfo = new dhtmlXGridRowCell();
					if (item.getIcode() != null) {
						sheetInfo.setCategoryICode(item.getIcode());
						cellInfo.setValue(evaluationService.searchMark(sheetInfo));
					}else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					// 수정
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("<button class=\"ui_main_btn_flat updateCode\" id="
							+ (i + 1) + ">" +messageSource.getMessage("evaluation.management.sheet.modify", null,Locale.getDefault())+"</button>");
					rowItem.getCellElements().add(cellInfo);

					// 삭제
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("<button class=\"ui_main_btn_flat deleteCode\" id="
							+ (i + 1) + ">삭제</button>");
					rowItem.getCellElements().add(cellInfo);

					// reg_sbg_code
					cellInfo = new dhtmlXGridRowCell();
					if (item.getCategoryBCode() == null
							|| ("".equals(item.getCategoryBCode().trim()))) {
						cellInfo.setValue("");
					} else {
						cellInfo.setValue(item.getCategoryBCode());
					}
					rowItem.getCellElements().add(cellInfo);

					// reg_smg_code
					cellInfo = new dhtmlXGridRowCell();
					if (item.getCategoryMCode() == null
							|| ("".equals(item.getCategoryMCode().trim()))) {
						cellInfo.setValue("");
					} else {
						cellInfo.setValue(item.getCategoryMCode());
					}
					rowItem.getCellElements().add(cellInfo);

					// reg_ssg_code
					cellInfo = new dhtmlXGridRowCell();
					if (item.getCategorySCode() == null
							|| ("".equals(item.getCategorySCode().trim()))) {
						cellInfo.setValue("");
					} else {
						cellInfo.setValue(item.getCategorySCode());
					}
					rowItem.getCellElements().add(cellInfo);

					// reg_item_code
					cellInfo = new dhtmlXGridRowCell();
					if (item.getCategoryICode() == null
							|| ("".equals(item.getCategoryICode().trim()))) {
						cellInfo.setValue("");
					} else {
						cellInfo.setValue(item.getCategoryICode());
					}
					rowItem.getCellElements().add(cellInfo);

					xmls.getRowElements().add(rowItem);
					rowItem = null;
				}
			}
		} else {
			xmls = null;
		}
		return xmls;
	}*/

	// evaluationWrap 뿌리기
	@RequestMapping(value = "/gridEvaluationWrap.xml", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/xml")
	public @ResponseBody dhtmlXGridXml gridEvaluationWrap(
			HttpServletRequest request, HttpServletResponse response) {
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		ModelAndView result = new ModelAndView();
		result.addObject("userInfo", userInfo);
		if (userInfo != null) {

			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for (int j = 0; j < 7; j++) {

				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("0");
				column.setHidden("0");

				switch (j) {

				// 순번
				case 0:
					column.setWidth("50");
					// FIXME:(언어팩)체크
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.no", null,Locale.getDefault())+"</div>");
					break;
				// 대분류
				case 1:
					column.setWidth("100");
					// FIXME:(언어팩)순번
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.big", null,Locale.getDefault())+"</div>");
					break;
				// 중분류
				case 2:
					column.setWidth("100");
					// FIXME:(언어팩)평가지 제목
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.Middle", null,Locale.getDefault())+"</div>");
					break;
				// 소분류
				case 3:
					column.setWidth("100");
					// FIXME:(언어팩)평가지 설명
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.Small", null,Locale.getDefault())+"</div>");
					break;
				// 세부항목
				case 4:
					column.setWidth("565");
					// FIXME:(언어팩) 항목 수
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.item", null,Locale.getDefault())+"</div>");
					break;
				// 채점
				case 5:
					column.setWidth("0");
					// FIXME:(언어팩) 평가 횟수
					column.setValue("<div style=\"text-align:center;\">채점</div>");
					column.setWidth("1");
					break;
				// 점수
				case 6:
					column.setWidth("100");
					// FIXME:(언어팩)평가지 단계
					column.setValue("<div style=\"text-align:center);\">"+messageSource.getMessage("evaluation.management.sheet.score2", null,Locale.getDefault())+"<div>");
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
			sheetInfo.setSheetCode(request.getParameter("sheetCode"));
			List<SheetInfo> selectResult = evaluationService.upSelectCate(sheetInfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;
			/*System.out.println(selectResult.size());*/
			if (selectResult.size() == 0) {

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				// 순번
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(1));
				rowItem.getCellElements().add(cellInfo);

				// 대분류
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				// 중분류
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				// 소분류
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				// 세부항목
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);


				// 점수
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);
				rowItem = null;
			} else {

				for (int i = 0; i < selectResult.size(); i++) {
					SheetInfo item = selectResult.get(i);

					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i + 1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					// 순번
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(String.valueOf(i + 1));
					rowItem.getCellElements().add(cellInfo);

					// 대분류
					cellInfo = new dhtmlXGridRowCell();
					if (item.getsBgcode() != null) {
						cellInfo.setValue(item.getsBgName());
					} else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					// 중분류
					cellInfo = new dhtmlXGridRowCell();
					if (item.getsMgcode() != null) {
						cellInfo.setValue(item.getsMgName());
					} else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					// 소분류
					cellInfo = new dhtmlXGridRowCell();
					if (item.getsSgcode() != null) {
						cellInfo.setValue(item.getsSgName());
					} else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					// 세부항목
					cellInfo = new dhtmlXGridRowCell();
					if (item.getIcode() != null) {
						cellInfo.setValue("<select class=\"igList\" id=\"igList_eval\"><option value="
								+ item.getIcode().trim()
								+ ">"
								+ item.getItemCode() + "</option></select>");
					} else {
						cellInfo.setValue("");
					}
					rowItem.getCellElements().add(cellInfo);

					// 채점
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue("채점");
					rowItem.getCellElements().add(cellInfo);

					// 점수
					cellInfo = new dhtmlXGridRowCell();
					if (item.getCategoryICode() == null
							|| ("".equals(item.getCategoryICode().trim()))) {
						cellInfo.setValue("");
					} else {
						cellInfo.setValue(""+item.getMark());
					}
					rowItem.getCellElements().add(cellInfo);

					xmls.getRowElements().add(rowItem);
					rowItem = null;
					item =null;

				}
			}

		} else {
			xmls = null;

		}
		return xmls;
	}

	// 평가 피드백 그리드 그리기
		@RequestMapping(value = "/feedbackGrid.xml", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/xml")
		public @ResponseBody dhtmlXGridXml feedbackGrid(
				HttpServletRequest request, HttpServletResponse response) {
			
			CookieSetToLang cls = new CookieSetToLang();
			cls.langSetFunc(request, response);
			
			LoginVO userInfo = SessionManager.getUserInfo(request);
			dhtmlXGridXml xmls = null;
			ModelAndView result = new ModelAndView();
			result.addObject("userInfo", userInfo);
			if (userInfo != null) {

				xmls = new dhtmlXGridXml();
				xmls.setHeadElement(new dhtmlXGridHead());

				dhtmlXGridHead head = new dhtmlXGridHead();
				head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

				//컬럼 한개 그리는부분
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("0");
				column.setHidden("0");


				//유일한 컬럼값
				column.setWidth("1000");
				column.setValue("피드백 내역");


				head.getColumnElement().add(column);
				column = null;

				//행값 시작
				dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
				headCall.setParamElement(new ArrayList<String>());
				headCall.setCommand("attachHeader");
				xmls.setHeadElement(head);

				SheetInfo sheetInfo = new SheetInfo();
				sheetInfo.seteCampCode(request.getParameter("campCode"));
				List<SheetInfo> selectResult = evaluationService.selectFeedback(sheetInfo);

				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;
				SheetInfo item = null;
					for (int i = 0; i < selectResult.size(); i++) {

						int tempRowId = i*3+1;
						item = selectResult.get(i);

						//1행 id(이름)
						dhtmlXGridRow rowItem = new dhtmlXGridRow();
						rowItem.setId(String.valueOf(tempRowId));
						rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

						cellInfo = new dhtmlXGridRowCell();
						if (!userInfo.getUserId().trim().equals(item.getRecName().trim())){
							cellInfo.setValue("<div id=feedbackTalk1>"+item.getRecName()+"("+item.getRecId()+")"+"</div>");
						} else {
							cellInfo.setValue("<div id=feedbackTalk3>"+item.getRecName()+"("+item.getRecId()+")"+"</div>");
						}
						rowItem.getCellElements().add(cellInfo);
						xmls.getRowElements().add(rowItem);

						//2행 피드백내용
						rowItem = new dhtmlXGridRow();
						rowItem.setId(String.valueOf(tempRowId+1));
						rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

						cellInfo = new dhtmlXGridRowCell();
						if (!userInfo.getUserId().trim().equals(item.getRecName().trim())){
							cellInfo.setValue("<span id=feedbackTalk2>"+item.getFeedback()+"<span>");
						} else {
							cellInfo.setValue("<span id=feedbackTalk4>"+item.getFeedback()+"<span>");
						}
						rowItem.getCellElements().add(cellInfo);
						xmls.getRowElements().add(rowItem);

						//3행 공백
						rowItem = new dhtmlXGridRow();
						rowItem.setId(String.valueOf(tempRowId+2));
						rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

						cellInfo = new dhtmlXGridRowCell();
						cellInfo.setValue("<div id=feedbackTalk1><div>");
						rowItem.getCellElements().add(cellInfo);
						xmls.getRowElements().add(rowItem);

				}

			} else {
				xmls = null;

			}
			return xmls;
		}




}
