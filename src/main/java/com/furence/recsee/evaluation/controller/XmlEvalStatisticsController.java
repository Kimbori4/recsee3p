package com.furence.recsee.evaluation.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
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
public class XmlEvalStatisticsController {
	private static final Logger logger = LoggerFactory.getLogger(XmlEvalStatisticsController.class);
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


	@RequestMapping(value = "/gridEvaluationStatistics.xml", method=RequestMethod.GET, produces="application/xml")
	public @ResponseBody dhtmlXGridXml gridEvaluationStatistics(HttpServletRequest request, HttpServletResponse response){

		CookieSetToLang cls = new CookieSetToLang();
		cls.langSetFunc(request, response);
		
		LoginVO userInfo = SessionManager.getUserInfo(request);
		dhtmlXGridXml xmls = null;

		if(userInfo != null){
			xmls = new dhtmlXGridXml();
			xmls.setHeadElement(new dhtmlXGridHead());

			dhtmlXGridHead head = new dhtmlXGridHead();
			head.setColumnElement(new ArrayList<dhtmlXGridHeadColumn>());

			for(int j=0;j<13;j++){
				dhtmlXGridHeadColumn column = new dhtmlXGridHeadColumn();

				column.setType("ro");
				column.setSort("str");
				column.setAlign("center");
				column.setFiltering("0");
				column.setEditable("0");
				column.setCache("0");
				column.setHidden("0");

				switch(j){
					case 0: //순번
						column.setId("order");
						column.setHidden("1");
						//column.setWidth("40");
						//FIXME:(언어팩)순번
						column.setValue("<div style=\"text-align:center;\">순번</div>");
						break;
					case 1: //대상자
						column.setId("rRecName");
						column.setWidth("100");
						column.setValue("대상자");						
						break;
					case 2: //구분
						column.setId("rUserType");
						column.setWidth("80");
						column.setValue("구분");
						break;
					case 3: // 파트
						column.setId("rSpartCode");
						column.setWidth("150");
						column.setValue("파트");
						break;
					case 4: //담당자
						column.setId("");
						column.setWidth("0");
						column.setHidden("1");
						column.setValue("담당자");
						break;
					case 5: //Level
						column.setId("Level");
						column.setWidth("0");
						column.setHidden("1");
						column.setValue("Level");
						break;
					case 6: //평가점수
						column.setId("rEvalTotalScore");
						column.setWidth("100");
						column.setValue("평가점수");
						break;
					case 7: //귀책감점
						column.setId("");
						column.setWidth("0");
						column.setHidden("1");
						column.setValue("귀책감점");
						break;
					case 8: //최종총점
						column.setId("");
						column.setWidth("0");
						column.setHidden("1");
						column.setValue("최종총점");
						break;
					case 9: //당월
						column.setId("nowMon");
						column.setWidth("100");
						column.setValue("당월");
						break;
					case 10: //전월
						column.setId("beforeMon");
						column.setWidth("100");
						column.setValue("전월");
						break;
					case 11: //GAP
						column.setId("GAP");
						column.setWidth("100");
						column.setValue("GAP");
						break;
					case 12: //공백용
						column.setId("blank");
						column.setWidth("*");
						column.setValue("");
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
			evaluationResultInfo.setrUserName((String) param.get("userId"));

			// 평가 결과 조회하기
			List<EvaluationResultInfo> selectResult = evaluationResultInfoService.selectEvaluationResult(evaluationResultInfo);
			xmls.setRowElements(new ArrayList<dhtmlXGridRow>());
			dhtmlXGridRowCell cellInfo = null;

			for(int i=0; i< selectResult.size();i++) {
				EvaluationResultInfo item = selectResult.get(i);

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

				// 평가결과 코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrEvalCode(), ""));
				rowItem.getCellElements().add(cellInfo);

				// 평가지 코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrSheetCode(), ""));
				rowItem.getCellElements().add(cellInfo);

				// 캠페인 코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrEcampCode(), ""));
				rowItem.getCellElements().add(cellInfo);

				//평가 날짜
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrEvalDate(), ""));
				rowItem.getCellElements().add(cellInfo);

				//평가 시간
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrEvalTime(), ""));
				rowItem.getCellElements().add(cellInfo);

				//캠페인명
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrEcampName(), ""));
				rowItem.getCellElements().add(cellInfo);

				//평가상태코드
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrEvalStatus(), ""));
				rowItem.getCellElements().add(cellInfo);

				//평가상태
				cellInfo = new dhtmlXGridRowCell();
				{
					String status = StringUtil.toString(item.getrEvalStatus(), "X");
					String value;
					try {
						value = messageSource.getMessage("evaluation.result.option.status." + status, null, Locale.getDefault());
					} catch(NoSuchMessageException e) {
						value = status;
						logger.error("error",e);
					}
					cellInfo.setValue(value);
				}
				rowItem.getCellElements().add(cellInfo);

				//보기
				// 버튼 달기 및 이벤트 연결시키기
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue("<button class=\"ui_btn_white ui_main_btn_flat icon_btn_view_white showEval\" id=" + (i + 1)
						+ " onclick='showEval(\"" + (i+1) + "\")'  style=\"background-color: rgb(96, 179, 220); border-color: rgb(96, 179, 220); color: rgb(255, 255, 255); cursor: pointer; transition: all 0.3s ease 0s;\">"
						+ "</button>"
					);

				rowItem.getCellElements().add(cellInfo);

				//평가자ID
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrEvalatorId(), ""));
				rowItem.getCellElements().add(cellInfo);

				//평가자
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrEvalatorName(), ""));
				rowItem.getCellElements().add(cellInfo);

				//평가자 피드백
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrEvalatorFeedback(), ""));
				rowItem.getCellElements().add(cellInfo);

				//상담원 피드백
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrAgentFeedback(), ""));
				rowItem.getCellElements().add(cellInfo);

				//상담원ID
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrRecId(), ""));
				rowItem.getCellElements().add(cellInfo);

				//상담원명
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrRecName(), ""));
				rowItem.getCellElements().add(cellInfo);

				//고객번호
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrCustPhone1(), ""));
				rowItem.getCellElements().add(cellInfo);

				//녹취 날짜
				cellInfo = new dhtmlXGridRowCell();
				String date = item.getrRecDate();
				SimpleDateFormat origin = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd");

				try{
					Date originDate = origin.parse(date);
					String newDate = newformat.format(originDate);

					cellInfo.setValue(newDate);
				}catch(Exception e){
					logger.error("error",e);
				}
				rowItem.getCellElements().add(cellInfo);

				//녹취 시간
				cellInfo = new dhtmlXGridRowCell();
				String time = item.getrRecTime();
				SimpleDateFormat originTime = new SimpleDateFormat("hhmmss");
				SimpleDateFormat newTime = new SimpleDateFormat("hh:mm:ss");

				try{
					Date time1 = originTime.parse(time);
					String time2 = newTime.format(time1);
					cellInfo.setValue(time2);
				}catch(Exception e){
					logger.error("error",e);
				}
				rowItem.getCellElements().add(cellInfo);

				//R_EVAL_TOTAL_MARK
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrEvalTotalMark(), ""));
				rowItem.getCellElements().add(cellInfo);

				//R_REC_FILENAME
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrRecFilename(), ""));
				rowItem.getCellElements().add(cellInfo);

				//R_EVAL_TOTAL_SCORE
				cellInfo = new dhtmlXGridRowCell();
				cellInfo.setValue(StringUtil.toString(item.getrEvalTotalScore() + "점", ""));
				rowItem.getCellElements().add(cellInfo);

				//상담원 이의제기 여부
				cellInfo = new dhtmlXGridRowCell();
				String feedbackYn = "X";
				if(!StringUtil.isNull(item.getrAgentFeedback())){
					feedbackYn = "O";
				}
				cellInfo.setValue(feedbackYn);
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
