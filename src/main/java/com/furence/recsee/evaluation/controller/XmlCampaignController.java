package com.furence.recsee.evaluation.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.util.CookieSetToLang;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.evaluation.service.EvaluationResultInfoService;
import com.furence.recsee.evaluation.service.EvaluationService;
import com.furence.recsee.main.model.dhtmlXGridHead;
import com.furence.recsee.main.model.dhtmlXGridHeadCall;
import com.furence.recsee.main.model.dhtmlXGridHeadColumn;
import com.furence.recsee.main.model.dhtmlXGridRow;
import com.furence.recsee.main.model.dhtmlXGridRowCell;
import com.furence.recsee.main.model.dhtmlXGridXml;

@Controller
public class XmlCampaignController {

	@Autowired
	private EvaluationResultInfoService evaluationResultInfoService;
	@Autowired
	private EvaluationService evaluationService;

	@Autowired
	private MessageSource messageSource;

	String titleBaseName="evaluation.campaign.grid.";

	//캠페인 처음 시작 그리드
	@RequestMapping(value = "/campaignGrid.xml", method=RequestMethod.GET, produces="application/xml")
		public @ResponseBody dhtmlXGridXml campaignGrid(HttpServletRequest request, HttpServletResponse response) throws ParseException{
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null){
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j=0;j<37;j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("0");
				column.setHidden("0");

				switch(j){
				//체크
				case 0:
					/*column.setWidth("40");
					column.setType("ch");
					//FIXME:(언어팩)체크
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "check", null,Locale.getDefault())+"</div>");
					*/
					column.setWidth("30");
					column.setType("ch");
					column.setSort("na");
					// FIXME:(언어팩)체크
					column.setValue("<div id='allcheck' style=\"cursor: pointer; top:2px;\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif'/></div>");
					break;
				//순번
				case 1:
					column.setWidth("40");
					column.setSort("int");
					//FIXME:(언어팩)순번
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "num", null,Locale.getDefault())+"</div>");
					break;
				//캠페인 제목
				case 2:
					column.setWidth("*");
					//FIXME: (언어팩)캠페인 제목
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "campTitle", null,Locale.getDefault())+"</div>");
					break;
				//캠페인 설명
				case 3:
					column.setWidth("300");
					//FIXME:(언어팩)캠페인 설명
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "campContent", null,Locale.getDefault())+"</div>");
					break;
				//재평가 여부
				case 4:
					column.setWidth("80");
					//FIXME: (언어팩)재평가 여부
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "reEval", null,Locale.getDefault())+"</div>");
					break;
				//평가 대상
				case 5:
					column.setWidth("160");
					//FIXME: (언어팩)평가 대상
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "evalTarget", null,Locale.getDefault())+"</div>");
					break;
				//채점 방식
				case 6:
					column.setWidth("120");
					column.setHidden("1");
					//FIXME: (언어팩)채점 방식
					column.setValue(messageSource.getMessage("evaluation.campaign.grid.evalTargetSet", null,Locale.getDefault()));
					break;
				//생성 날짜
				case 7:
					column.setWidth("100");
					//FIXME: (언어팩)생성 날짜
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "createDate", null,Locale.getDefault())+"</div>");
					break;
				//최종 수정 날짜
				case 8:
					column.setWidth("0");
					column.setSort("int");
					//FIXME: (언어팩)최종 수정 날짜
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "updatedDate", null,Locale.getDefault())+"</div>");
					break;
				//남은 평가 기간
				case 9:
					column.setWidth("100");
					column.setSort("int");
					//FIXME: (언어팩) 남은 평가 기간
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "remnantDuration", null,Locale.getDefault())+"</div>");
					break;
				//수정
				case 10:
					column.setWidth("80");
					column.setType("link");
					column.setSort("na");
					//FIXME: (언어팩)수정
//					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "modification", null,Locale.getDefault())+"</div>");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.detail", null,Locale.getDefault())+"</div>");
					break;
				//캠페인코드
				case 11:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME: (언어팩)캠페인 코드
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "campTitle", null,Locale.getDefault())+"</div>");
					break;
				//r_ecamp_term
				case 12:
					column.setWidth("0");
					column.setHidden("1");
					//r_ecamp_term
					column.setValue("<div style=\"text-align:center;\">r_ecamp_term</div>");
					break;
				//평가 시작 날짜
				case 13:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME: (언어팩) 평가 시작 날짜
					column.setValue("<div style=\"text-align:center;\">평가 시작 날짜</div>");
					break;
				//평가 종료 날짜
				case 14:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME: (언어팩) 평가 종료 날짜
					column.setValue("<div style=\"text-align:center;\">평가 종료 날짜</div>");
					break;
				//시트 코드
				case 15:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME: (언어팩) 시트 코드
					column.setValue("<div style=\"text-align:center;\">시트 코드</div>");
					break;
				//r_per_eval_count 평가횟수
				case 16:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME: (언어팩) 평가 횟수
					column.setValue("<div style=\"text-align:center;\">평가 횟수</div>");
					break;
				//r_feedback
				case 17:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME:(언어팩)피드백
					column.setValue("<div style=\"text-align:center;\">피드백</div>");
					break;
				//시트 이름
				case 18:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME:(언어팩)시트 이름
					column.setValue("<div style=\"text-align:center;\">시트 이름</div>");
					break;
				//r_bg_code
				case 19:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rbgcode</div>");
					break;
				//r_bg_name
				case 20:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rbgname</div>");
					break;
				//r_mg_code
				case 21:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rmgcode</div>");
					break;
				//r_mg_name
				case 22:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rmgname</div>");
					break;
				//r_sg_code
				case 23:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rsgcode</div>");
					break;
				//r_sg_name
				case 24:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rsgcode</div>");
					break;
				//r_bpart_code
				case 25:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rbpartcode</div>");
					break;
				//r_mpart_code
				case 26:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rmpartcode</div>");
					break;
				//r_spart_code
				case 27:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rspartcode</div>");
					break;
				//r_eval_degree
				case 28:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">revaldegree</div>");
					break;
				//상담원 알림
				case 29:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME: (언어팩) 상담원 알림
					column.setValue("<div style=\"text-align:center;\">상담원 알림</div>");
					break;
				//이의제기
				case 30:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME: (언어팩) 이의제기
					column.setValue("<div style=\"text-align:center;\">이의제기</div>");
					break;
				//평가 분배	코드
				case 31:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">평가 분배 코드</div>");
					break;
				//평가 대상자 코드
				case 32:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">평가 대상자 코드</div>");
					break;
				//평가 차수..
				case 33:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">평가차수</div>");
					break;
				//평가 대상자 yn
				case 34:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">평가차수</div>");
					break;
				//평가 분배 yn
				case 35:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">평가차수</div>");
					break;
				//피드백 종료기간
				case 36:
					column.setHidden("1");
//					column.setWidth("50");
					column.setValue("<div style=\"text-align:center;\">종료기간</div>");
					break;
				}

				head.getColumnElement().add(column);
				column = null;
			}
			dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
			headCall.setParamElement(new ArrayList<String>());
			headCall.setCommand("attachHeader");
			xmls.setHeadElement(head);

			Map<String, Object> evaluationResultInfo = new HashMap<String, Object>();
			//검색 타입
			if(request.getParameter("searchType") != null
					&& !request.getParameter("searchType").isEmpty()){
				evaluationResultInfo.put("searchType", request.getParameter("searchType"));
			}
			//검색 단어
			if(request.getParameter("searchWord") != null
					&& !request.getParameter("searchWord").isEmpty()){
				evaluationResultInfo.put("searchWord",request.getParameter("searchWord"));
			}
			//캠페인 코드
			if(request.getParameter("rEcampCode") != null
					&& !request.getParameter("rEcampCode").isEmpty()){
				evaluationResultInfo.put("rEcampCode", request.getParameter("rEcampCode"));
			}
			//캠페인 이름
			if(request.getParameter("rEcampName") != null
					&& !request.getParameter("rEcampName").isEmpty()){
				evaluationResultInfo.put("rEcampName", request.getParameter("rEcampName"));
			}
			//캠페인 설명
			if(request.getParameter("rEcampContent") != null
					&& !request.getParameter("rEcampContent").isEmpty()){
				evaluationResultInfo.put("rEcampContent", request.getParameter("rEcampContent"));
			}
			//평가 시작 날짜
			if(evaluationResultInfo.get("sEvalDate") != null
					&& !StringUtil.toString(evaluationResultInfo.get("sEvalDate"), "").isEmpty()){
				evaluationResultInfo.put("sEvalDate", evaluationResultInfo.get("sEvalDate"));
			}
			//평가 종료 날짜
			if(evaluationResultInfo.get("eEvalDate") != null
					&& !StringUtil.toString(evaluationResultInfo.get("eEvalDate"), "").isEmpty()){
				evaluationResultInfo.put("eEvalDate", evaluationResultInfo.get("eEvalDate"));
			}
			//r_ecamp_term
			if(evaluationResultInfo.get("rEcampTerm") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rEcampTerm"), "").isEmpty()){
				evaluationResultInfo.put("rEcampTerm", evaluationResultInfo.get("rEcampTerm"));
			}
			//시트 코드
			if(evaluationResultInfo.get("rSheetCode") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rSheetCode"), "").isEmpty()){
				evaluationResultInfo.put("rSheetCode", evaluationResultInfo.get("rSheetCode"));
			}
			//평가 횟수
			if(evaluationResultInfo.get("rPerEvalCount") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rPerEvalCount"), "").isEmpty()){
				evaluationResultInfo.put("rPerEvalCount", evaluationResultInfo.get("rPerEvalCount"));
			}
			//FIXME: 피드백
			if(evaluationResultInfo.get("rFeedback") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rFeedback"), "").isEmpty()){
				evaluationResultInfo.put("rFeedback", evaluationResultInfo.get("rFeedback"));
			}
			//FIXME : 시트 이름
			if(evaluationResultInfo.get("rSheetName") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rSheetName"), "").isEmpty()){
				evaluationResultInfo.put("rSheetName", evaluationResultInfo.get("rSheetName"));
			}
			//대분류 코드
			if(evaluationResultInfo.get("rBpartCode") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rBpartCode"), "").isEmpty()){
				evaluationResultInfo.put("rBpartCode", evaluationResultInfo.get("rBpartCode"));
			}
			//중분류 코드
			if(evaluationResultInfo.get("rMpartCode") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rMpartCode"), "").isEmpty()){
				evaluationResultInfo.put("rMpartCode", evaluationResultInfo.get("rMpartCode"));
			}
			//소분류 코드
			if(evaluationResultInfo.get("rSpartCode") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rSpartCode"), "").isEmpty()){
				evaluationResultInfo.put("rSpartCode", evaluationResultInfo.get("rSpartCode"));
			}
			//r_eval_degree
			if(evaluationResultInfo.get("rEvalDegree") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rEvalDegree"), "").isEmpty()){
				evaluationResultInfo.put("rEvalDegree", evaluationResultInfo.get("rEvalDegree"));
			}
			//캠페인 select
			List<Map<String, Object>> selectResult = evaluationResultInfoService.selectCampaign(evaluationResultInfo);
			//List<Map<String, Object>> selectRule = evaluationResultInfoService.selectRule(evaluationResultInfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i=0; i< selectResult.size();i++){


			Map<String, Object> item = selectResult.get(i);


			dhtmlXGridRow rowItem = new dhtmlXGridRow();
			rowItem.setId(String.valueOf(i+1));
			rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

			//체크
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);

			//순번
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue(String.valueOf(i + 1));
			rowItem.getCellElements().add(cellInfo);

			//캠페인 제목
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("rEcampName"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("rEcampName"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//캠페인 설명
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("rEcampContent"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("rEcampContent"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//재평가 여부
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("revaluation"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("revaluation"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//평가 대상
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);

			//채점 방식
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("scoringSystem"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("scoringSystem"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//생성날짜
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("mDate"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("mDate"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//최종 수정 날짜
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("uDate"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("uDate"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//남은 평가 기간
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("eEvalDate"), "").isEmpty() || StringUtil.isNull(item.get("eEvalDate").toString())){
				cellInfo.setValue("");
			} else{

				String date = StringUtil.toString(item.get("eEvalDate"), "");
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String today = dateFormat.format(new Date());

				Date endDate = dateFormat.parse(date);
				Date todayDate = dateFormat.parse(today);

				long remnantDuration = (endDate.getTime() - todayDate.getTime()) / (24*60*60*1000);
				cellInfo.setValue(remnantDuration+"");/*"일"*/

			}
			rowItem.getCellElements().add(cellInfo);

			//수정
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("<button class=\"ui_main_btn_flat updateCampaign\" id="+(i+1)+" data-target=\"show\">"+messageSource.getMessage("evaluation.management.sheet.detail", null,Locale.getDefault())+"</button>");
			rowItem.getCellElements().add(cellInfo);

			//캠페인 코드
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("rEcampCode"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("rEcampCode"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//r_ecamp_term
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("rEcampTerm"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("rEcampTerm"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//평가 시작 날짜
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("sEvalDate"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("sEvalDate"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//평가 종료 날짜
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("eEvalDate"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("eEvalDate"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//시트 코드
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("rSheetCode"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("rSheetCode"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//r_per_eval_count 평가횟수
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_feedback
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//시트 이름
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_bg_code
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_bg_name
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_mg_code
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_mg_name
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_sg_code
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_sg_name
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_bpart_code
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_mpart_code
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_spart_code
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_eval_degree
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//상담원 알림
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("notification"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("notification"), ""));
			}
			rowItem.getCellElements().add(cellInfo);
			//재평가 여부
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("objection"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("objection"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//평가자 코드
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("evaluator"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("evaluator"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//평가 대상 코드
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("group"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("group"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//평가 차수
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("degree"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("degree"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//평가 대상 yn
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("scoringSystem"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("scoringSystem"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//평가 분배 yn
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("assignment"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("assignment"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//피드백 종료 날짜
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("feedbackDate"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("feedbackDate"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			xmls.getRowElements().add(rowItem);
			rowItem = null;
			}

		}else {
			xmls = null;
			}
		return xmls;
	}


	//캠페인 만들기  그리드
	@RequestMapping(value = "/gridCampaignMagician.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml gridCampaignMagician(HttpServletRequest request, HttpServletResponse response){

	CookieSetToLang cls = new CookieSetToLang();
	cls.langSetFunc(request, response);
	
	LoginVO userInfo = SessionManager.getUserInfo(request);
	dhtmlXGridXml xmls = null;

		if(userInfo != null){
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j=0;j<16;j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("0");
				column.setHidden("0");

				switch(j){
				case 0:
					column.setWidth("50");
					column.setType("ch");
					column.setValue(messageSource.getMessage("evaluation.management.sheet.check", null,Locale.getDefault()));
					break;

				//순번
				case 1:
					column.setWidth("50");
					//FIXME:(언어팩)순번
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("evaluation.management.sheet.no", null,Locale.getDefault()) + "</div>");
					break;
				//평가지명
				case 2:
					column.setWidth("*");
					//FIXME:(언어팩)평가지명
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("evaluation.management.sheet.sheetname", null,Locale.getDefault()) + "</div>");
					break;
				//평가 설명
				case 3:
					column.setWidth("200");
					//FIXME: (언어팩)평가 설명
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("evaluation.management.sheet.sheetdesc", null,Locale.getDefault()) + "</div>");
					break;
				//평가 단계
				case 4:
					column.setWidth("150");
					//FIXME: (언어팩)평가 단계
					column.setValue("<div style=\"text-align:center;\">" + messageSource.getMessage("evaluation.management.sheet.stepofeval", null,Locale.getDefault()) + "</div>");
					break;
				//평가 문항
				case 5:
					column.setWidth("80");
					//FIXME: (언어팩)평가 문항
					column.setValue("<div style=\"text-align:center;\">평가 문항</div>");
					break;
				//점수
				case 6:
					column.setWidth("80");
					//FIXME: (언어팩)점수
					column.setValue("<div style=\"text-align:center;\">점수</div>");
					break;
				case 7:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME: (언어팩)평가 생성자
					column.setValue("<div style=\"text-align:center;\">평가 생성자</div>");
					break;
				case 8:
					column.setWidth("0");
					column.setHidden("1");
					//sheet mdate
					column.setValue("<div style=\"text-align:center;\">rsheetmdate</div>");
					break;
				case 9:
					column.setWidth("0");
					column.setHidden("1");
					//sheet wdate
					column.setValue("<div style=\"text-align:center;\">rsheetwdate</div>");
					break;
				case 10:
					column.setWidth("0");
					column.setHidden("1");
					//sheet using
					column.setValue("<div style=\"text-align:center;\">rsheetusing</div>");
					break;
				case 11:
					column.setWidth("0");
					column.setHidden("1");
					//bpartcode
					column.setValue("<div style=\"text-align:center;\">bpartcode</div>");
					break;
				case 12:
					column.setWidth("0");
					column.setHidden("1");
					//mpartcode
					column.setValue("<div style=\"text-align:center;\">mpartcode</div>");
					break;
				case 13:
					column.setWidth("0");
					column.setHidden("1");
					//spartcode
					column.setValue("<div style=\"text-align:center;\">spartcode</div>");
					break;
				case 14:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">시트코드</div>");
				}
				head.getColumnElement().add(column);
				column = null;
			}
			dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
			headCall.setParamElement(new ArrayList<String>());
			headCall.setCommand("attachHeader");
			xmls.setHeadElement(head);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sheetCode", request.getParameter("sheetCode"));

			List<Map<String,Object>> selectEcampSheet = evaluationResultInfoService.selectEcampSheet(map);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			if(selectEcampSheet.size()==0){

				int i=0;
				dhtmlXGridRow rowItem = new dhtmlXGridRow();
				rowItem.setId(String.valueOf(i+1));
				rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

				//체크
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);
				//순번
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(String.valueOf(i + 1));
				rowItem.getCellElements().add(cellInfo);

				//순번
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);
				//평가지명
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);
				//평가 설명
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);
				//평가 단계
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);
				//평가 문항
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);
				//점수
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);

				//평가 생성자
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);
				//시트생성날짜
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);
				//rsheetwdate
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);
				//sheet using
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);
				//bpartcode
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);
				//mpartcode
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);
				//spartcode
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);
				//시트코드
				cellInfo = new dhtmlXGridRowCell();
				rowItem.getCellElements().add(cellInfo);

				xmls.getRowElements().add(rowItem);
				rowItem = null;
			}else{
				for(int i=0;i<selectEcampSheet.size();i++){

					Map<String, Object> item = selectEcampSheet.get(i);

					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					//체크
					cellInfo = new dhtmlXGridRowCell();
					rowItem.getCellElements().add(cellInfo);

					//순번
					cellInfo = new dhtmlXGridRowCell();
					cellInfo.setValue(String.valueOf(i + 1));
					rowItem.getCellElements().add(cellInfo);

					//시트 제목
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.toString(item.get("sheetName"), "").isEmpty()){
						cellInfo.setValue("");
					} else{
						cellInfo.setValue(StringUtil.toString(item.get("sheetName"), ""));
					}
					rowItem.getCellElements().add(cellInfo);

					//시트 설명
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.toString(item.get("sheetContent"), "").isEmpty()){
						cellInfo.setValue("");
					} else{
						cellInfo.setValue(StringUtil.toString(item.get("sheetContent"), ""));
					}
					rowItem.getCellElements().add(cellInfo);

					//시트 단계
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.toString(item.get("sheetDepth"), "").isEmpty()){
						cellInfo.setValue("");
					} else{
						cellInfo.setValue(StringUtil.toString(item.get("sheetDepth"), ""));
					}
					rowItem.getCellElements().add(cellInfo);

					//평가 문항
					cellInfo = new dhtmlXGridRowCell();
					rowItem.getCellElements().add(cellInfo);

					//점수
					cellInfo = new dhtmlXGridRowCell();
					rowItem.getCellElements().add(cellInfo);

					//평가 생성자
					cellInfo = new dhtmlXGridRowCell();
					rowItem.getCellElements().add(cellInfo);
					//시트생성날짜
					cellInfo = new dhtmlXGridRowCell();
					rowItem.getCellElements().add(cellInfo);
					//rsheetwdate
					cellInfo = new dhtmlXGridRowCell();
					rowItem.getCellElements().add(cellInfo);
					//sheet using
					cellInfo = new dhtmlXGridRowCell();
					rowItem.getCellElements().add(cellInfo);
					//bpartcode
					cellInfo = new dhtmlXGridRowCell();
					rowItem.getCellElements().add(cellInfo);
					//mpartcode
					cellInfo = new dhtmlXGridRowCell();
					rowItem.getCellElements().add(cellInfo);
					//spartcode
					cellInfo = new dhtmlXGridRowCell();
					rowItem.getCellElements().add(cellInfo);
					//시트코드
					cellInfo = new dhtmlXGridRowCell();
					if(StringUtil.toString(item.get("sheetCode"), "").isEmpty()){
						cellInfo.setValue("");
					} else{
						cellInfo.setValue(StringUtil.toString(item.get("sheetCode"), ""));
					}
					rowItem.getCellElements().add(cellInfo);

					xmls.getRowElements().add(rowItem);
					rowItem = null;
				}
			}

		}else {
			xmls = null;
			}
		return xmls;
	}

	//캠페인 만들기 그룹 그리드
	@RequestMapping(value = "/gridCampaignViewGroup.xml", method=RequestMethod.GET, produces="application/xml")
		public @ResponseBody dhtmlXGridXml gridCampaignViewGroup(HttpServletRequest request, HttpServletResponse response){
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);

		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();

		if(userInfo != null){
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j=0;j<5;j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("0");
				column.setHidden("0");

				switch(j){
				//체크
				case 0:
					column.setWidth("50");
					column.setType("ch");
					//FIXME:(언어팩)체크
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "check", null,Locale.getDefault())+"</div>");
					break;
				//그룹명
				case 1:
					column.setWidth("*");
					//FIXME:(언어팩)그룹명
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "gruopName", null,Locale.getDefault())+"</div>");
					break;
				//그룹원 수
				case 2:
					column.setWidth("100");
					//FIXME: (언어팩)그룹원 수
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "groupMemberNum", null,Locale.getDefault())+"</div>");
					break;
				case 3:
					column.setWidth("0");
					//FIXME: (언어팩)그룹코드
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\"></div>");
					break;
				case 4:
					column.setWidth("0");
					//FIXME: (언어팩)그룹코드
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\"></div>");
					break;
			}
				head.getColumnElement().add(column);
				column = null;
			}
			dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
			headCall.setParamElement(new ArrayList<String>());
			headCall.setCommand("attachHeader");
			xmls.setHeadElement(head);

			if (StringUtil.isNull(request.getParameter("init"), true)) {

				String selectGroup = "";
				String selectName = "";

				Map<String, Object> evaluationResultInfo = new HashMap<String, Object>();
				Map<String, Object> map = new HashMap<String, Object>();

					if(!StringUtil.isNull(request.getParameter("selectGroup"), true)){
//						sheetinfo.setcFormContents03(request.getParameter("formContents03"));
						selectGroup = request.getParameter("selectGroup");
					}
					if(!StringUtil.isNull(request.getParameter("selectName"), true)){
//						sheetinfo.setcFormContents03(request.getParameter("formContents03"));
						selectName = request.getParameter("selectName");
						map.put("selectName",selectName);

					}

				List<Map<String, Object>> selectResult = null;

				if(selectGroup.equals("sGroup"))	{selectResult = evaluationResultInfoService.selectsGroup(evaluationResultInfo); }
				else if(selectGroup.equals("mGroup"))	{selectResult = evaluationResultInfoService.selectmGroup(evaluationResultInfo);}
				else if(selectGroup.equals("all"))	{selectResult = evaluationResultInfoService.selectallGroup(evaluationResultInfo);}
				else if(selectGroup.equals("person"))	{selectResult = evaluationResultInfoService.selectPersonGroup(map);}
				else if(selectGroup.equals("affilicate"))	{selectResult = evaluationResultInfoService.selectAffilicate(map);}
				else if(selectGroup.equals("skill"))	{selectResult = evaluationResultInfoService.selectSkill(map);}

				else{
					selectResult = evaluationResultInfoService.selectallGroup(evaluationResultInfo);
				}
//				List<SheetInfo> selectResult = EvaluationService.selectGroupName(sheetinfo);
//				System.out.println(selectResult.size());
				//if(selectResult.size()>0){

				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;

				for(int i=0; i< selectResult.size();i++){

					Map<String, Object> item = selectResult.get(i);
					String data ="";
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					//체크
					cellInfo = new dhtmlXGridRowCell();
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					//그룹명
					if(StringUtil.toString(item.get("iname"), "").isEmpty()){
						cellInfo.setValue("");
					} else{
						cellInfo.setValue(StringUtil.toString(item.get("iname"), ""));
					}

					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					//그룹 수
					if(StringUtil.toString(item.get("cnt"), "").isEmpty()){
						cellInfo.setValue("");
					} else{
						cellInfo.setValue(StringUtil.toString(item.get("cnt"), ""));
					}
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					//그룹 코드
					if(StringUtil.toString(item.get("code"), "").isEmpty()){
						cellInfo.setValue("");
					} else{
						cellInfo.setValue(StringUtil.toString(item.get("code"), ""));
					}

					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					//그룹 코드
					if(StringUtil.toString(item.get("mcode"), "").isEmpty()){
						cellInfo.setValue("");
					} else{
						cellInfo.setValue(StringUtil.toString(item.get("mcode"), ""));
					}

					rowItem.getCellElements().add(cellInfo);

					xmls.getRowElements().add(rowItem);
					rowItem = null;
				}
			}
		}else {
			xmls = null;
		}
		return xmls;
	}


	//캠페인 만들기 그룹 그리드
	@RequestMapping(value = "/gridCampaignViewAssign.xml", method=RequestMethod.GET, produces="application/xml")
		public @ResponseBody dhtmlXGridXml gridCampaignViewAssign(HttpServletRequest request, HttpServletResponse response){

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();

		if(userInfo != null){
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j=0;j<4;j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("0");
				column.setHidden("0");

				switch(j){
				case 0:
					column.setWidth("50");
					column.setType("ch");
					//FIXME:(언어팩)체크
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "check", null,Locale.getDefault())+"</div>");
					break;
				//그룹명
				case 1:
					column.setWidth("*");
					//FIXME:(언어팩)평가자 id
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.result.placeholder.evaluatorId", null,Locale.getDefault())+"</div>");
					break;
				case 2:
					column.setWidth("150");
					//FIXME: (언어팩)평가자 명
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.result.placeholder.evaluator", null,Locale.getDefault())+"</div>");
					break;
				//그룹원 수
				case 3:
					column.setWidth("150");
					//FIXME: (언어팩)평가 횟수
					column.setType("ed");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.management.sheet.evaluationCount", null,Locale.getDefault())+"</div>");
					break;

			}
				head.getColumnElement().add(column);
				column = null;
			}
			dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
			headCall.setParamElement(new ArrayList<String>());
			headCall.setCommand("attachHeader");
			xmls.setHeadElement(head);

			if (StringUtil.isNull(request.getParameter("init"), true)) {

				Map<String, Object> evaluationResultInfo = new HashMap<String, Object>();
				List<Map<String, Object>> selectResult = null;
				Map<String, Object> map = new HashMap<String, Object>();

				selectResult = evaluationResultInfoService.selectEvaluator(evaluationResultInfo);

				xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
				dhtmlXGridRowCell cellInfo = null;

				for(int i=0; i< selectResult.size();i++){

					Map<String, Object> item = selectResult.get(i);
					String data ="";
					dhtmlXGridRow rowItem = new dhtmlXGridRow();
					rowItem.setId(String.valueOf(i+1));
					rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

					cellInfo = new dhtmlXGridRowCell();
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					//평가자 id
					if(StringUtil.toString(item.get("userId"), "").isEmpty()){
						cellInfo.setValue("");
					} else{
						cellInfo.setValue(StringUtil.toString(item.get("userId"), ""));
					}

					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					//평가자 명
					if(StringUtil.toString(item.get("userName"), "").isEmpty()){
						cellInfo.setValue("");
					} else{
						cellInfo.setValue(StringUtil.toString(item.get("userName"), ""));
					}
					rowItem.getCellElements().add(cellInfo);

					cellInfo = new dhtmlXGridRowCell();
					//평가 횟수
					if(StringUtil.toString(item.get("evalCount"), "").isEmpty()){
						cellInfo.setValue("");
					} else{
						cellInfo.setValue(StringUtil.toString(item.get("evalCount"), ""));
					}

					rowItem.getCellElements().add(cellInfo);

					xmls.getRowElements().add(rowItem);
					rowItem = null;
				}
			}
		}else {
			xmls = null;
		}
		return xmls;
	}

	//캠페인 처음 시작 -> 지난 캠페인
	@RequestMapping(value = "/evalLastCampaign.xml", method=RequestMethod.GET, produces="application/xml")
		public @ResponseBody dhtmlXGridXml evalLastCampaign(HttpServletRequest request,	HttpServletResponse response){
		
		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null){
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j=0;j<37;j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("0");
				column.setHidden("0");

				switch(j){
				//체크
				case 0:
					/*column.setWidth("40");
					column.setType("ch");
					//FIXME:(언어팩)체크
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "check", null,Locale.getDefault())+"</div>");
					*/
					column.setWidth("30");
					column.setType("ch");
					column.setSort("na");
					// FIXME:(언어팩)체크
					column.setValue("<div id='allcheck' style=\"cursor: pointer; top:2px;\"><img src='"+request.getContextPath()+"/resources/common/recsee/images/project/dhxgrid_web/item_chk0.gif'/></div>");
					break;
				//순번
				case 1:
					column.setWidth("40");
					column.setSort("int");
					//FIXME:(언어팩)순번
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "num", null,Locale.getDefault())+"</div>");
					break;
				//캠페인 제목
				case 2:
					column.setWidth("*");
					//FIXME: (언어팩)캠페인 제목
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "campTitle", null,Locale.getDefault())+"</div>");
					break;
				//캠페인 설명
				case 3:
					column.setWidth("*");
					//FIXME:(언어팩)캠페인 설명
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "campContent", null,Locale.getDefault())+"</div>");
					break;
				//재평가 여부
				case 4:
					column.setWidth("80");
					//FIXME: (언어팩)재평가 여부
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "reEval", null,Locale.getDefault())+"</div>");
					break;
				//평가 대상
				case 5:
					column.setWidth("160");
					//FIXME: (언어팩)평가 대상
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "evalTarget", null,Locale.getDefault())+"</div>");
					break;
				//채점 방식
				case 6:
					column.setWidth("120");
					column.setHidden("1");
					//FIXME: (언어팩)채점 방식
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.campaign.grid.evalTargetSet", null,Locale.getDefault())+"</div>");
					break;
				//생성 날짜
				case 7:
					column.setWidth("180");
					column.setSort("int");
					//FIXME: (언어팩)생성 날짜
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "createDate", null,Locale.getDefault())+"</div>");
					break;
				//최종 수정 날짜
				case 8:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME: (언어팩)최종 수정 날짜
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "updatedDate", null,Locale.getDefault())+"</div>");
					break;
				//남은 평가 기간
				case 9:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME: (언어팩) 남은 평가 기간
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "remnantDuration", null,Locale.getDefault())+"</div>");
					break;
				//수정
				case 10:
					column.setWidth("80");
					column.setType("link");
					//FIXME: (언어팩)수정
//					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "modification", null,Locale.getDefault())+"</div>");
					column.setValue(messageSource.getMessage("evaluation.management.sheet.detail", null,Locale.getDefault()));
					break;
				//캠페인코드
				case 11:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME: (언어팩)캠페인 코드
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage(titleBaseName + "campTitle", null,Locale.getDefault())+"</div>");
					break;
				//r_ecamp_term
				case 12:
					column.setWidth("0");
					column.setHidden("1");
					//r_ecamp_term
					column.setValue("<div style=\"text-align:center;\">r_ecamp_term</div>");
					break;
				//평가 시작 날짜
				case 13:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME: (언어팩) 평가 시작 날짜
					column.setValue("<div style=\"text-align:center;\">평가 시작 날짜</div>");
					break;
				//평가 종료 날짜
				case 14:
					column.setWidth("180");
//					column.setHidden("1");
					//FIXME: (언어팩) 평가 종료 날짜
					column.setValue("<div style=\"text-align:center;\">"+messageSource.getMessage("evaluation.selectPeriod.evalEndDate", null,Locale.getDefault())+"</div>");
					break;
				//시트 코드
				case 15:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME: (언어팩) 시트 코드
					column.setValue("<div style=\"text-align:center;\">시트 코드</div>");
					break;
				//r_per_eval_count 평가횟수
				case 16:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME: (언어팩) 평가 횟수
					column.setValue("<div style=\"text-align:center;\">평가 횟수</div>");
					break;
				//r_feedback
				case 17:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME:(언어팩)피드백
					column.setValue("<div style=\"text-align:center;\">피드백</div>");
					break;
				//시트 이름
				case 18:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME:(언어팩)시트 이름
					column.setValue("<div style=\"text-align:center;\">시트 이름</div>");
					break;
				//r_bg_code
				case 19:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rbgcode</div>");
					break;
				//r_bg_name
				case 20:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rbgname</div>");
					break;
				//r_mg_code
				case 21:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rmgcode</div>");
					break;
				//r_mg_name
				case 22:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rmgname</div>");
					break;
				//r_sg_code
				case 23:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rsgcode</div>");
					break;
				//r_sg_name
				case 24:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rsgcode</div>");
					break;
				//r_bpart_code
				case 25:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rbpartcode</div>");
					break;
				//r_mpart_code
				case 26:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rmpartcode</div>");
					break;
				//r_spart_code
				case 27:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">rspartcode</div>");
					break;
				//r_eval_degree
				case 28:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">revaldegree</div>");
					break;
				//상담원 알림
				case 29:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME: (언어팩) 상담원 알림
					column.setValue("<div style=\"text-align:center;\">상담원 알림</div>");
					break;
				//이의제기
				case 30:
					column.setWidth("0");
					column.setHidden("1");
					//FIXME: (언어팩) 이의제기
					column.setValue("<div style=\"text-align:center;\">이의제기</div>");
					break;
				//평가 분배	코드
				case 31:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">평가 분배 코드</div>");
					break;
				//평가 대상자 코드
				case 32:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">평가 대상자 코드</div>");
					break;
				//평가 차수..
				case 33:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">평가차수</div>");
					break;
				//평가 대상자 yn
				case 34:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">평가차수</div>");
					break;
				//평가 분배 yn
				case 35:
					column.setWidth("0");
					column.setHidden("1");
					column.setValue("<div style=\"text-align:center;\">평가차수</div>");
					break;
				//피드백 종료기간
				case 36:
					column.setHidden("1");
//					column.setWidth("50");
					column.setValue("<div style=\"text-align:center;\">종료기간</div>");
					break;

				}
				head.getColumnElement().add(column);
				column = null;
			}
			dhtmlXGridHeadCall headCall = new dhtmlXGridHeadCall();
			headCall.setParamElement(new ArrayList<String>());
			headCall.setCommand("attachHeader");
			xmls.setHeadElement(head);

			Map<String, Object> evaluationResultInfo = new HashMap<String, Object>();
			//검색 타입
			if(request.getParameter("searchType") != null
					&& !request.getParameter("searchType").isEmpty()){
				evaluationResultInfo.put("searchType", request.getParameter("searchType"));
			}
			//검색 단어
			if(request.getParameter("searchWord") != null
					&& !request.getParameter("searchWord").isEmpty()){
				evaluationResultInfo.put("searchWord",request.getParameter("searchWord"));
			}
			//캠페인 코드
			if(request.getParameter("rEcampCode") != null
					&& !request.getParameter("rEcampCode").isEmpty()){
				evaluationResultInfo.put("rEcampCode", request.getParameter("rEcampCode"));
			}
			//캠페인 이름
			if(request.getParameter("rEcampName") != null
					&& !request.getParameter("rEcampName").isEmpty()){
				evaluationResultInfo.put("rEcampName", request.getParameter("rEcampName"));
			}
			//캠페인 설명
			if(request.getParameter("rEcampContent") != null
					&& !request.getParameter("rEcampContent").isEmpty()){
				evaluationResultInfo.put("rEcampContent", request.getParameter("rEcampContent"));
			}
			//평가 시작 날짜
			if(evaluationResultInfo.get("sEvalDate") != null
					&& !StringUtil.toString(evaluationResultInfo.get("sEvalDate"), "").isEmpty()){
				evaluationResultInfo.put("sEvalDate", evaluationResultInfo.get("sEvalDate"));
			}
			//평가 종료 날짜
			if(evaluationResultInfo.get("eEvalDate") != null
					&& !StringUtil.toString(evaluationResultInfo.get("eEvalDate"), "").isEmpty()){
				evaluationResultInfo.put("eEvalDate", evaluationResultInfo.get("eEvalDate"));
			}
			//r_ecamp_term
			if(evaluationResultInfo.get("rEcampTerm") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rEcampTerm"), "").isEmpty()){
				evaluationResultInfo.put("rEcampTerm", evaluationResultInfo.get("rEcampTerm"));
			}
			//시트 코드
			if(evaluationResultInfo.get("rSheetCode") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rSheetCode"), "").isEmpty()){
				evaluationResultInfo.put("rSheetCode", evaluationResultInfo.get("rSheetCode"));
			}
			//평가 횟수
			if(evaluationResultInfo.get("rPerEvalCount") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rPerEvalCount"), "").isEmpty()){
				evaluationResultInfo.put("rPerEvalCount", evaluationResultInfo.get("rPerEvalCount"));
			}
			//FIXME: 피드백
			if(evaluationResultInfo.get("rFeedback") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rFeedback"), "").isEmpty()){
				evaluationResultInfo.put("rFeedback", evaluationResultInfo.get("rFeedback"));
			}
			//FIXME : 시트 이름
			if(evaluationResultInfo.get("rSheetName") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rSheetName"), "").isEmpty()){
				evaluationResultInfo.put("rSheetName", evaluationResultInfo.get("rSheetName"));
			}
			//대분류 코드
			if(evaluationResultInfo.get("rBpartCode") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rBpartCode"), "").isEmpty()){
				evaluationResultInfo.put("rBpartCode", evaluationResultInfo.get("rBpartCode"));
			}
			//중분류 코드
			if(evaluationResultInfo.get("rMpartCode") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rMpartCode"), "").isEmpty()){
				evaluationResultInfo.put("rMpartCode", evaluationResultInfo.get("rMpartCode"));
			}
			//소분류 코드
			if(evaluationResultInfo.get("rSpartCode") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rSpartCode"), "").isEmpty()){
				evaluationResultInfo.put("rSpartCode", evaluationResultInfo.get("rSpartCode"));
			}
			//r_eval_degree
			if(evaluationResultInfo.get("rEvalDegree") != null
					&& !StringUtil.toString(evaluationResultInfo.get("rEvalDegree"), "").isEmpty()){
				evaluationResultInfo.put("rEvalDegree", evaluationResultInfo.get("rEvalDegree"));
			}
			//캠페인 select
			List<Map<String, Object>> selectResult = evaluationResultInfoService.selectLastCampaign(evaluationResultInfo);
			//List<Map<String, Object>> selectRule = evaluationResultInfoService.selectRule(evaluationResultInfo);

			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i=0; i< selectResult.size();i++){


			Map<String, Object> item = selectResult.get(i);


			dhtmlXGridRow rowItem = new dhtmlXGridRow();
			rowItem.setId(String.valueOf(i+1));
			rowItem.setCellElements(new ArrayList<dhtmlXGridRowCell>());

			//체크
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);

			//순번
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue(String.valueOf(i + 1));
			rowItem.getCellElements().add(cellInfo);

			//캠페인 제목
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("rEcampName"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("rEcampName"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//캠페인 설명
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("rEcampContent"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("rEcampContent"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//재평가 여부
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("revaluation"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("revaluation"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//평가 대상
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);

			//채점 방식
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("scoringSystem"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("scoringSystem"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//생성날짜
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("mDate"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("mDate"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//최종 수정 날짜
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("uDate"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("uDate"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//남은 평가 기간
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);

			//수정
			/*cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("<button class=\"ui_main_btn_flat updateCampaign\" id="+(i+1)+" data-target=\"uc\">수정</button>");
			rowItem.getCellElements().add(cellInfo);*/
			cellInfo = new dhtmlXGridRowCell();
			cellInfo.setValue("<button class=\"ui_main_btn_flat updateLastCampaign\" id="+(i+1)+" data-target=\"show\">" + messageSource.getMessage("evaluation.management.sheet.detail", null,Locale.getDefault()) + "</button>");
			rowItem.getCellElements().add(cellInfo);

			//캠페인 코드
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("rEcampCode"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("rEcampCode"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//r_ecamp_term
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("rEcampTerm"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("rEcampTerm"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//평가 시작 날짜
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("sEvalDate"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("sEvalDate"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//평가 종료 날짜
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("eEvalDate"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("eEvalDate"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//시트 코드
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("rSheetCode"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("rSheetCode"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//r_per_eval_count 평가횟수
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_feedback
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//시트 이름
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_bg_code
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_bg_name
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_mg_code
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_mg_name
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_sg_code
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_sg_name
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_bpart_code
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_mpart_code
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_spart_code
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//r_eval_degree
			cellInfo = new dhtmlXGridRowCell();
			rowItem.getCellElements().add(cellInfo);
			//상담원 알림
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("notification"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("notification"), ""));
			}
			rowItem.getCellElements().add(cellInfo);
			//재평가 여부
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("objection"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("objection"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//평가자 코드
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("evaluator"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("evaluator"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//평가 대상 코드
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("group"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("group"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//평가 차수
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("degree"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("degree"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//평가 대상 yn
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("scoringSystem"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("scoringSystem"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//평가 분배 yn
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("assignment"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("assignment"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			//피드백 종료기간
			cellInfo = new dhtmlXGridRowCell();
			if(StringUtil.toString(item.get("feedbackDate"), "").isEmpty()){
				cellInfo.setValue("");
			} else{
				cellInfo.setValue(StringUtil.toString(item.get("feedbackDate"), ""));
			}
			rowItem.getCellElements().add(cellInfo);

			xmls.getRowElements().add(rowItem);
			rowItem = null;
			}


		}else {
			xmls = null;
			}
		return xmls;
	}

}
