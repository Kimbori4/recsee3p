package com.furence.recsee.evaluation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.util.CookieSetToLang;
import com.furence.recsee.common.util.ParameterUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.evaluation.model.EvaluationResultInfo;
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
public class XmlEvaluatingController {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private EvaluationResultInfoService evaluationResultInfoService;
	@Autowired
	private EvaluationService evaluationService;
/*
	@RequestMapping(value = "/evaluatingGrid.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml evaluatingGrid(HttpServletRequest request, HttpServletResponse response){

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null){
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j=0;j<17;j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
//				column.setSort("str");
				column.setSort("na");
				column.setAlign("center");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("0");
				column.setHidden("0");

				switch(j){
					//체크
					case 0:
						column.setId("checkbox");
						column.setHidden("1");
						//FIXME:(언어팩)체크
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.check", null,Locale.getDefault())+"</div>");
						break;
					//순번
					case 1:
						column.setId("order");
						column.setHidden("1");
						//FIXME:(언어팩)순번
						column.setValue("<div style=\"text-align:center;\">순번</div>");
						break;
					// 평가결과 코드
					case 2:
						column.setId("rEvalCode");
						column.setHidden("1");
						column.setValue("r_eval_code");
						break;
					// 평가지 코드
					case 3:
						column.setId("rSheetCode");
						column.setHidden("1");
						column.setValue("r_sheet_code");
						break;
					// 캠페인 코드
					case 4:
						column.setId("rEcampCode");
						column.setHidden("1");
						column.setValue("r_ecamp_code");
						break;
					// 녹취 날짜
					case 5:
						column.setId("rRecDate");
						column.setWidth("100");
						// FIXME:(언어팩)녹취 날짜
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.result.placeholder.recDateStart", null,Locale.getDefault())+"</div>");
						break;
					// 녹취 시간
					case 6:
						column.setId("rRecTime");
						column.setWidth("100");
						// FIXME:(언어팩)녹취 시간
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.result.placeholder.rectime", null,Locale.getDefault())+"</div>");
						break;
					// 캠페인명
					case 7:
						column.setId("rEvalatorName");
						column.setWidth("*");
						// FIXME:(언어팩)캠페인명
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.result.option.campaignTitle", null,Locale.getDefault())+"</div>");
						break;
					// 평가상태코드
					case 8:
						column.setId("rEvalStatus");
						column.setHidden("1");
						column.setValue("r_eval_status");
						break;
					// 평가상태
					case 9:
						column.setId("rEvalStatusName");
						column.setWidth("80");
						// FIXME:(언어팩)평가상태
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.state", null,Locale.getDefault())+"</div>");
						break;
					// 평가자
					case 10:
						column.setId("rEvalatorId");
						column.setHidden("1");
						// FIXME:(언어팩)평가자ID
						column.setValue("평가자ID");
						break;
					// 평가자
					case 11:
						column.setId("rEvalatorName");
						column.setWidth("100");
						// FIXME:(언어팩)평가자
						column.setHidden("1");
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.result.placeholder.evaluator", null,Locale.getDefault())+"</div>");
						break;
					// 평가자 피드백
					case 12:
						column.setId("rEvalatorFeedback");
						column.setWidth("200");
						column.setHidden("1");
						// FIXME:(언어팩)평가자 피드백
						column.setValue("평가자 피드백");
						break;
					// 상담원 피드백
					case 13:
						column.setId("rAgentFeedback");
						column.setWidth("200");
						column.setHidden("1");
						// FIXME:(언어팩)상담원 피드백
						column.setValue("상담원 피드백");
						break;
					// 상담원명
					case 14:
						column.setId("rRecName");
						column.setWidth("100");
						// FIXME:(언어팩)상담원명
						column.setValue("상담원명");
						break;
					// 고객번호
					case 15:
						column.setId("rCustPhone1");
						column.setWidth("100");
						// FIXME:(언어팩)고객번호
						column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.result.placeholder.callNumber", null,Locale.getDefault())+"</div>");
						break;
					//보기
					case 16:
						column.setId("view");
						column.setWidth("60");
						// FIXME:(언어팩)보기
						column.setValue("평가");
						break;

				}
				head.getColumnElement().add(column);
				column = null;
			}
			dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
			headCall.setParamElement(new ArrayList<String>());
			headCall.setCommand("attachHeader");
			xmls.setHeadElement(head);

			// 파라미터 모든 값 가져오기
			Map<String, Object> param = ParameterUtil.getAllParameter(request);
			if(StringUtil.toString(param.get("head"), "false").equalsIgnoreCase("true")) {
				return xmls;
			}

			EvaluationResultInfo evaluationResultInfo = new EvaluationResultInfo();
			evaluationResultInfo.setsEvalDate((String) param.get("sEvalDate"));
			evaluationResultInfo.seteEvalDate((String) param.get("eEvalDate"));
			evaluationResultInfo.setsRecDate((String) param.get("sRecDate"));
			evaluationResultInfo.seteRecDate((String) param.get("eRecDate"));
			evaluationResultInfo.setsRecTime((String) param.get("sRecTime"));
			evaluationResultInfo.seteRecTime((String) param.get("eRecTime"));
			evaluationResultInfo.setrEcampCode((String) param.get("rEcampCode"));
			evaluationResultInfo.setrEvalatorId((String) param.get("rEvalatorId"));
			evaluationResultInfo.setrEvalatorName((String) param.get("rEvalatorName"));
			evaluationResultInfo.setrRecId((String) param.get("rRecId"));
			evaluationResultInfo.setrRecName((String) param.get("rRecName"));
			evaluationResultInfo.setrCustPhone1((String) param.get("rCustPhone1"));
			evaluationResultInfo.setrEvalStatus((String) param.get("rEvalStatus"));
			evaluationResultInfo.setrEvalatorFeedback((String) param.get("rEvalatorFeedback"));
			evaluationResultInfo.setrAgentFeedback((String) param.get("rAgentFeedback"));

			// 평가 결과 조회하기
			List<EvaluationResultInfo> selectResult = evaluationResultInfoService.selectEvaluating(evaluationResultInfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i=0; i< selectResult.size();i++) {
				EvaluationResultInfo item = selectResult.get(i);

				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
//				rowItem.setId(StringUtil.toString(item.getrEvalCode(), ""));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				//체크
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);

				//순번
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(i + 1));
				rowItem.getCellElements().add(cellInfo);

				// 평가결과 코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(""));
				rowItem.getCellElements().add(cellInfo);

				// 평가지 코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString( ""));
				rowItem.getCellElements().add(cellInfo);

				// 캠페인 코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(""));
				rowItem.getCellElements().add(cellInfo);

				//녹취 날짜
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrRecDate(), ""));
				rowItem.getCellElements().add(cellInfo);

				//녹취 시간
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrRecTime(), ""));
				rowItem.getCellElements().add(cellInfo);

				//캠페인명
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(""));
				rowItem.getCellElements().add(cellInfo);

				//평가상태코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(""));
				rowItem.getCellElements().add(cellInfo);

				//평가상태
				cellInfo = new dhtmlXGridRowCell();
				String status = StringUtil.toString( "");
				cellInfo.setValue(status);
				rowItem.getCellElements().add(cellInfo);

				//평가자ID
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue( "");
				rowItem.getCellElements().add(cellInfo);

				//평가자
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("");
				rowItem.getCellElements().add(cellInfo);

				//평가자 피드백
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue( "");
				rowItem.getCellElements().add(cellInfo);

				//상담원 피드백
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue( "");
				rowItem.getCellElements().add(cellInfo);

				//상담원명
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrUserName(), ""));
				rowItem.getCellElements().add(cellInfo);

				//고객번호
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrCustPhone1(), ""));
				rowItem.getCellElements().add(cellInfo);

				//보기
				// 버튼 달기 및 이벤트 연결시키기
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<button class='on_evaluation_open ui_btn_white ui_sub_btn_flat icon_btn_evaluation_white' onClick='openEvalPop(\""+item.getListenUrl()+"\")'></button>");
				rowItem.getCellElements().add(cellInfo);


				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}
		} else {
			xmls = null;
		}
		return xmls;
	}*/

}