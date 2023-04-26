package com.furence.recsee.evaluation.controller;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hyperic.sigar.SigarException;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.evaluation.model.SheetInfo;
import com.furence.recsee.evaluation.service.EvaluationResultInfoService;
import com.furence.recsee.evaluation.service.EvaluationService;

@Controller
public class AjaxCampaignController {
	private static final Logger logger = LoggerFactory.getLogger(AjaxCampaignController.class);
	@Autowired
	private EvaluationResultInfoService evaluationResultInfoService;
	@Autowired
	private EvaluationService evaluationService;

	//캠페인 삽입
	@RequestMapping(value="/insertCampaign.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO insertCampaign(HttpServletRequest request, @RequestParam Map<String, Object> map){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅
				//캠페인 코드
				if (StringUtil.toString(map.get("rEcampCode"), "").isEmpty()) {
					map.put("rEcampCode", map.get("rEcampCode"));
				}
				//캠페인 제목
				if (StringUtil.toString(map.get("rEcampName"), "").isEmpty()) {
					map.put("rEcampName", map.get("rEcampName"));
				}
				//캠페인 내용
				if (StringUtil.toString(map.get("rEcampContent"), "").isEmpty()) {
					map.put("rEcampContent", map.get("rEcampContent"));
				}
				//평가 기간
				if (StringUtil.toString(map.get("rEcampTerm"), "").isEmpty()){
					map.put("rEcampTerm", map.get("rEcampTerm"));
				}
				//평가 시작 날짜
				if (StringUtil.toString(map.get("sDate"), "").isEmpty()){
					map.put("sDate", map.get("sDate"));
				}
				//평가 종료 날짜
				if (StringUtil.toString(map.get("eDate"), "").isEmpty()){
					map.put("eDate", map.get("eDate"));
				}
				//평가 그룹
				if (StringUtil.toString(map.get("evalGroup"), "").isEmpty()){
					map.put("evalGroup", map.get("evalGroup"));
				}
				if (StringUtil.toString(map.get("evalDegree"), "").isEmpty()){
					map.put("evalDegree", map.get("evalDegree"));
				}
				if (StringUtil.toString(map.get("sheetCode"), "").isEmpty()){
					map.put("sheetCode", map.get("sheetCode"));
				}
				if (StringUtil.toString(map.get("sheetName"), "").isEmpty()){
					map.put("sheetName", map.get("sheetName"));
				}
				if (StringUtil.toString(map.get("evalAssign"), "").isEmpty()){
					map.put("evalAssign", map.get("evalAssign"));
				}
				if (StringUtil.toString(map.get("rFeedbackYn"), "").isEmpty()){
					map.put("rFeedbackYn", map.get("rFeedbackYn"));
				}
				if (StringUtil.toString(map.get("rFeedbackDate"), "").isEmpty()){
					map.put("rFeedbackDate", map.get("rFeedbackDate"));
				}

				Integer insertResult = evaluationResultInfoService.insertCampaign(map);
								
				if(insertResult > 0 ){
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				}else{
					jRes.setResult("NOTHING");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			}else{
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}catch (Exception e){
			logger.error("error",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}


	//룰 삽입
	@RequestMapping(value="/insertRule.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO insertRule(HttpServletRequest request, @RequestParam Map<String, Object> map){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅
				//캠페인 제목
				if (StringUtil.toString(map.get("rEcampCode"), "").isEmpty()) {
					map.put("rEcampCode", map.get("rEcampCode"));
				}
				//룰 코드
				if (StringUtil.toString(map.get("rRuleCode"), "").isEmpty()) {
					map.put("rRuleCode", map.get("rRuleCode"));
				}
				//룰 값 코드
				if (StringUtil.toString(map.get("rRuleValCode"), "").isEmpty()){
					map.put("rRuleValCode", map.get("rRuleValCode"));
				}

				Integer insertResultRule = evaluationResultInfoService.insertRule(map);

				if(insertResultRule > 0){
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				}else{
					jRes.setResult("NOTHING");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			}else{
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}catch (Exception e){
			logger.error("error",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}

	//시트 추가
	@RequestMapping(value="/insertEcampSheet.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO insertEcampSheet(HttpServletRequest request, @RequestParam Map<String, Object> map){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅

				//캠페인 코드
				if (StringUtil.toString(map.get("rEcampCode"), "").isEmpty()) {
					map.put("rEcampCode", map.get("rEcampCode"));
				}
				//시트 코드
				if (StringUtil.toString(map.get("sheetCode"), "").isEmpty()) {
					map.put("sheetCode", map.get("sheetCode"));
				}

				Integer insertResultRule = evaluationResultInfoService.insertEcampSheet(map);

				if(insertResultRule > 0){
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				}else{
					jRes.setResult("NOTHING");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			}else{
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}catch (Exception e){
			logger.error("error",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}

	//평가 캠페인 만들기에서 처음 생성시 캠페인 코드 자동 업데이트 해주는 ajax
	@RequestMapping(value="/updateEcampSheet.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO updateEcampSheet(HttpServletRequest request, @RequestParam Map<String, Object> map){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅

				//캠페인 코드
				if (StringUtil.toString(map.get("rEcampCode"), "").isEmpty()) {
					map.put("rEcampCode", map.get("rEcampCode"));
				}

				Integer updateResult = evaluationResultInfoService.updateEcampSheet(map);

				if(updateResult > 0){
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				}else{
					jRes.setResult("NOTHING");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			}else{
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}catch (Exception e){
			logger.error("error",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}


	//캠페인 삭제
	@RequestMapping(value="/deleteCampaign.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO deleteCampaign(HttpServletRequest request, @RequestParam Map<String, Object> map){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try{
				//체크된 캠페인 코드를 가져온다
				String checked=StringUtil.toString(map.get("rEcampCode"));
				String checkedRow = "";
				String[] checkedData;
				int Count = 0;
				{
					//체크된 캠페인코드를 ','로 자른다
					checkedData = checked.split(",");

					for(String data: checkedData) {
						map.put("ecampCode", data);
						List<Map<String, Object>> selectDelete = evaluationResultInfoService.selectDeleteCampaign(map);
						if(selectDelete.size() == 0){
							Count++;
							checkedRow += "'" + data + "'" + ",";
						}

					}
					// 마지막 쉼표 지우기
					checkedRow = checkedRow.replaceFirst(",$", "");
					map.clear();
					map.put("rEcampCode", checkedRow);
				}
//				map.put("rEcampCode", checkedRow);
				/*for(int i = 0; i < checkedData.length ; i ++){
					map.put("ecampCode", checkedData[i]);
				}*/
//				List<Map<String, Object>> selectDelete = evaluationResultInfoService.selectDeleteCampaign(map);

				if(Count > 0){

					Integer deleteResult = evaluationResultInfoService.deleteCampaign(map);
					Integer deleteResultRule = evaluationResultInfoService.deleteRule(map);
					Integer deleteResultSheet = evaluationResultInfoService.deleteTotalSheet(map);
					Integer deleteGroupInfo = evaluationResultInfoService.deleteGroup(map);
					Integer deleteAssign = evaluationResultInfoService.deleteAssign(map);

					if(deleteResult > 0  && checkedData.length != Count){
						jRes.setResult("1");
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					}else if(deleteResult > 0 && checkedData.length == Count){
						jRes.setResult("2");
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					}
				}else{
					jRes.setResult("InRESULT");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}



		}catch (Exception e){
			logger.error("error",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}


	private void setResult(String string) {
		// TODO Auto-generated method stub

	}


	//평가 캠페인 만들기 내 시트 삭제
	@RequestMapping(value="/deleteEcampSheet.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO deleteEcampSheet(HttpServletRequest request, @RequestParam Map<String, Object> map){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try{
			if(userInfo != null){
				//체크된 캠페인 코드를 가져온다
				String checked=StringUtil.toString(map.get("sheetCode"));
				String checkedRow = "";
				{
					//체크된 캠페인코드를 ','로 자른다
					String[] checkedData = checked.split(",");
					for(String data: checkedData) {
						checkedRow += "'" + data + "'" + ",";
					}
					// 마지막 쉼표 지우기
					checkedRow = checkedRow.replaceFirst(",$", "");
				}
				map.put("sheetCode", checkedRow);

				Integer deleteResult = evaluationResultInfoService.deleteEcampSheet(map);

				if(deleteResult > 0 ){
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				}else{
					jRes.setResult("NOTHING");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			}else{
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}catch (Exception e){
			logger.error("error",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}


	//캠페인 만들기 페이지의 selectbox 평가지 생성 select
	@RequestMapping(value="/selectSheetInfo.do", method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody AJaxResVO selectSheetInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, SigarException {

		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){

				if (request.getParameter("sheetName") != null
						&& !request.getParameter("sheetName").isEmpty()) {
					sheetInfo.setSheetName(request.getParameter("sheetName"));
				}
				if (request.getParameter("sheetCode") != null
						&& !request.getParameter("sheetCode").isEmpty()) {
					sheetInfo.setSheetCode(request.getParameter("sheetCode"));
				}
				if (request.getParameter("sheetCreator") != null
						&& !request.getParameter("sheetCreator").isEmpty()) {
					sheetInfo.setSheetName(request.getParameter("sheetCreator"));
				}
				if (request.getParameter("sheetDepth") != null
						&& !request.getParameter("sheetDepth").isEmpty()) {
					sheetInfo.setSheetName(request.getParameter("sheetDepth"));
				}
				if (request.getParameter("sheetMdate") != null
						&& !request.getParameter("sheetMdate").isEmpty()) {
					sheetInfo.setSheetName(request.getParameter("sheetMdate"));
				}
				if (request.getParameter("sheetWdate") != null
						&& !request.getParameter("sheetWdate").isEmpty()) {
					sheetInfo.setSheetName(request.getParameter("sheetWdate"));
				}
				if (request.getParameter("sheetUsingYn") != null
						&& !request.getParameter("sheetUsingYn").isEmpty()) {
					sheetInfo.setSheetName(request.getParameter("sheetUsingYn"));
				}
				if (request.getParameter("bPartCode") != null
						&& !request.getParameter("bPartCode").isEmpty()) {
					sheetInfo.setSheetName(request.getParameter("bPartCode"));
				}
				if (request.getParameter("mPartCode") != null
						&& !request.getParameter("mPartCode").isEmpty()) {
					sheetInfo.setSheetName(request.getParameter("mPartCode"));
				}
				if (request.getParameter("sPartCode") != null
						&& !request.getParameter("sPartCode").isEmpty()) {
					sheetInfo.setSheetName(request.getParameter("sPartCode"));
				}

				List<SheetInfo> selectResult = evaluationService.selectSheetList(sheetInfo);
				List<SheetInfo> selectboxResult = evaluationService.selectboxSheet(sheetInfo);

				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("sheetList", selectResult);
				jRes.addAttribute("selectResult", selectboxResult);
				jRes.addAttribute("selectboxResultsize",selectboxResult.size());
			}
		}

		catch (Exception e){
			logger.error("error",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}


	//캠페인 화면 수정 버튼 눌렀을 때 팝업창에 해당 조건을 뿌려주는 ajax
	@RequestMapping(value="/upSelectCampaign.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO upSelectCampaign(HttpServletRequest request, @RequestParam Map<String, Object> map){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅
				//캠페인 코드
				if (StringUtil.toString(map.get("rEcampCode"), "").isEmpty()) {
					map.put("rEcampCode", map.get("rEcampCode"));
				}
				//캠페인 제목
				if (StringUtil.toString(map.get("rEcampName"), "").isEmpty()) {
					map.put("rEcampName", map.get("rEcampName"));
				}
				//캠페인 내용
				if (StringUtil.toString(map.get("rEcampContent"), "").isEmpty()) {
					map.put("rEcampContent", map.get("rEcampContent"));
				}
				//평가 기간
				if (StringUtil.toString(map.get("rEcampTerm"), "").isEmpty()){
					map.put("rEcampTerm", map.get("rEcampTerm"));
				}
				//평가 시작 날짜
				if (StringUtil.toString(map.get("sEvalDate"), "").isEmpty()){
					map.put("sEvalDate", map.get("sEvalDate"));
				}
				//평가 종료 날짜
				if (StringUtil.toString(map.get("eEvalDate"), "").isEmpty()){
					map.put("eEvalDate", map.get("eEvalDate"));
				}
				if(StringUtil.toString(map.get("rRuleCode"), "").isEmpty()){
					map.put("rRuleCode", map.get("rRuleCode"));
				}
				if(StringUtil.toString(map.get("rRuleValCode"), "").isEmpty()){
					map.put("rRuleValCode", map.get("rRuleValCode"));
				}

				List<Map<String, Object>> selectResult = evaluationResultInfoService.upSelectCampaign(map);
				List<Map<String, Object>> selectResultRule = evaluationResultInfoService.upSelectRule(map);

				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("upSelectCampaign", selectResult);
				jRes.addAttribute("upSelectRule", selectResultRule);

			}else{
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}catch (Exception e){
			logger.error("error",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}

	//캠페인 update해주는 ajax
	@RequestMapping(value="/updateCampaign.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO updateCampaign(HttpServletRequest request, @RequestParam Map<String, Object> map){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅
				//캠페인 코드
				if (StringUtil.toString(map.get("rEcampCode"), "").isEmpty()) {
					map.put("rEcampCode", map.get("rEcampCode"));
				}
				//캠페인 제목
				if (StringUtil.toString(map.get("rEcampName"), "").isEmpty()) {
					map.put("rEcampName", map.get("rEcampName"));
				}
				//캠페인 내용
				if (StringUtil.toString(map.get("rEcampContent"), "").isEmpty()) {
					map.put("rEcampContent", map.get("rEcampContent"));
				}
				//평가 기간
				if (StringUtil.toString(map.get("rEcampTerm"), "").isEmpty()){
					map.put("rEcampTerm", map.get("rEcampTerm"));
				}
				//평가 시작 날짜
				if (StringUtil.toString(map.get("sDate"), "").isEmpty()){
					map.put("sDate", map.get("sDate"));
				}
				//평가 종료 날짜
				if (StringUtil.toString(map.get("eDate"), "").isEmpty()){
					map.put("eDate", map.get("eDate"));
				}
				if (StringUtil.toString(map.get("evalDegree"), "").isEmpty()){
					map.put("evalDegree", map.get("evalDegree"));
				}
				if (StringUtil.toString(map.get("evalGroup"), "").isEmpty()){
					map.put("evalGroup", map.get("evalGroup"));
				}
				if(StringUtil.toString(map.get("evalAssign"), "").isEmpty()){
					map.put("evalAssign", map.get("evalAssign"));
				}
				if(StringUtil.toString(map.get("sheetCode"), "").isEmpty()){
					map.put("sheetCode", map.get("sheetCode"));
				}
				Integer selectResult = evaluationResultInfoService.updateCampaign(map);

				if(selectResult>0){
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				}else{
					jRes.setResult("NOTHING");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			}else{
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}catch (Exception e){
			logger.error("error",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}

	@RequestMapping(value="/campaignGroup.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO campaignGroup(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				// 받아온 데이터 셋팅
				String select = StringUtil.toString(request.getParameter("select"), "");
				String checked = StringUtil.toString(request.getParameter("checked"), "");
				String checked2 = StringUtil.toString(request.getParameter("checked2"),"");
				String checked3 = StringUtil.toString(request.getParameter("checked3"),"");
				String groupType = StringUtil.toString(request.getParameter("groupType"),"");
				String[] checked3Data;
				String[] checked2Data;
				String[] checkedData;

				if(!(groupType.trim().length() ==0 && groupType == null))	sheetInfo.setGroupType(groupType);

				/*if(!select.equals("sGroup") && !select.equals("mGroup") && !select.equals("all")) {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("NOT_VALID_CODE_TYPE");
				}*/
				if(checked.trim().isEmpty()) {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("NO_DATA");
				}

				String checkedRow = "";
				{
					checkedData = checked.split(",");
					for(String data: checkedData) {
						checkedRow += "'" + data + "'" + ",";
					}
					// 마지막 쉼표 지우기
					checkedRow = checkedRow.replaceFirst(",$", "");
				}

				//소분류의 중뷴ㄿ...
				if(checked2.trim().isEmpty()) {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("NO_DATA");
				}

				String checked2Row = "";
				{
					checked2Data = checked2.split(",");
					for(String data: checked2Data) {
						checked2Row += "'" + data + "'" + ",";
					}
					// 마지막 쉼표 지우기
					checked2Row = checked2Row.replaceFirst(",$", "");
				}

				String checked3Row = "";
				{
					checked3Data = checked3.split(",");
					for(String data: checked3Data) {
						checked3Row += "'" + data + "'" + ",";
					}
					// 마지막 쉼표 지우기
					checked3Row = checked3Row.replaceFirst(",$", "");
				}

				Integer insertGroup = -1;
				Integer insertGroup2 = 0;
				if(select.equals("all")) {
					sheetInfo.setsBgcode(checkedRow);
					insertGroup = evaluationResultInfoService.insertBgGroup(sheetInfo);
				} else if(select.equals("mGroup")) {
					if(checkedData.length != 0){
						for(int i =0; i<checkedData.length; i++){
							if(i==0){
								sheetInfo.setsMgcode(checkedData[0]);
								insertGroup = evaluationResultInfoService.insertMgGroup(sheetInfo);

							}else{
								sheetInfo.setsMgcode(checkedData[i]);
								insertGroup2 = evaluationResultInfoService.insertMgGroup2(sheetInfo);
							}
						}
					}
				} else if(select.equals("sGroup")) {
					if(checked2Data.length !=0){
						for(int i = 0; i<checked2Data.length; i++){
							if(i == 0){
								sheetInfo.setsSgcode(checkedData[0]);
								sheetInfo.setsMgcode(checked2Data[0]);
								insertGroup = evaluationResultInfoService.insertSgGroup(sheetInfo);
							}else{
								sheetInfo.setsSgcode(checkedData[i]);
								sheetInfo.setsMgcode(checked2Data[i]);
								insertGroup2 = evaluationResultInfoService.insertSgGroup2(sheetInfo);
							}

						}
					}

				}else if(select.equals("people")) {
					for(int i = 0; i<checkedData.length; i++){
						if(i == 0){
							sheetInfo.setsSgcode(checkedData[0]);
							insertGroup = evaluationResultInfoService.insertPeopleGroup(sheetInfo);
						}else{
							sheetInfo.setsSgcode(checkedData[i]);
							insertGroup2 = evaluationResultInfoService.insertPeopleGroup2(sheetInfo);
						}

					}
				}else if(select.equals("assign")) {
					for(int i = 0; i<checkedData.length; i++){
						if(i == 0){
							sheetInfo.setEvalatorId(checkedData[0]);
							sheetInfo.setEvalatorName(checked2Data[0]);
							sheetInfo.setEvaluatorCount(checked3Data[0]);
							insertGroup = evaluationResultInfoService.insertAssign(sheetInfo);
						}else{
							sheetInfo.setEvalatorId(checkedData[i]);
							sheetInfo.setEvalatorName(checked2Data[i]);
							sheetInfo.setEvaluatorCount(checked3Data[i]);
							insertGroup2 = evaluationResultInfoService.insertAssign2(sheetInfo);
						}

					}
				}else if(select.equals("skill")){
					for(int i = 0; i<checkedData.length; i++){
						if(i == 0){
							sheetInfo.setsSgcode(checkedData[0]);
							insertGroup = evaluationResultInfoService.insertSkillGroup(sheetInfo);
						}else{
							sheetInfo.setsSgcode(checkedData[i]);
							insertGroup2 = evaluationResultInfoService.insertSkillGroup2(sheetInfo);
						}

					}
				}else if(select.equals("affilicate")){
					for(int i = 0; i<checkedData.length; i++){
						if(i == 0){
							sheetInfo.setsSgcode(checkedData[0]);
							insertGroup = evaluationResultInfoService.insertAffilicateGroup(sheetInfo);
						}else{
							sheetInfo.setsSgcode(checkedData[i]);
							insertGroup2 = evaluationResultInfoService.insertAffilicateGroup2(sheetInfo);
						}

					}
				}

				if(insertGroup > 0){

					jRes.addAttribute("selectGroupSeq", sheetInfo.getIndexNum());
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);

				}else{
					jRes.setResult("NOTHING");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					logger.info("fail : nothing");
				}
			}else{
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				logger.info("fail : loginFail");
			}
		}catch (Exception e){
			logger.error("error",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			logger.info("fail : error");
		}
		return jRes;
	}

	// rule update해주는 ajax
	@RequestMapping(value="/updateRule.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO updateRule(HttpServletRequest request, @RequestParam Map<String, Object> map){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅
				//캠페인 코드
				if (StringUtil.toString(map.get("rEcampCode"), "").isEmpty()) {
					map.put("rEcampCode", map.get("rEcampCode"));
				}
				if(StringUtil.toString(map.get("rRuleCode"), "").isEmpty()){
					map.put("rRuleCode", map.get("rRuleCode"));
				}
				if(StringUtil.toString(map.get("rRuleValCode"), "").isEmpty()){
					map.put("rRuleValCode", map.get("rRuleValCode"));
				}

				Integer selectResult = evaluationResultInfoService.updateRule(map);

				if( selectResult>0){
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				}else{
					jRes.setResult("NOTHING");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			}else{
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}catch (Exception e){
			logger.error("error",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
	
	// rule update해주는 ajax
	@RequestMapping(value="/UpdateCampainVisible.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO UpdateCampainVisible(HttpServletRequest request, @RequestParam Map<String, Object> map){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try{
			if(userInfo != null){
				//캠페인 코드
				
				String flag = (String)map.get("visibleCmd");
				
				map.put("campainCode", map.get("visibleCampainCode"));
				map.put("campainVisibleFlag", map.get("visibleFlag"));
				
				Integer selectResult = 0;
				if("insert".equals(flag)) {
					selectResult = evaluationResultInfoService.insertEcampVisible(map);
				}
				else if("update".equals(flag)) {
					selectResult = evaluationResultInfoService.updateEcampVisible(map);
				}
				else if("delete".equals(flag)) {
					selectResult = evaluationResultInfoService.deleteEcampVisible(map);
				}

				if( selectResult>0){
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				}else{
					jRes.setResult("NOTHING");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			}else{
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}catch (Exception e){
			logger.error("error",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
	
	@RequestMapping(value="/UpdateCampainVisibleReturn.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO UpdateCampainVisibleReturn(HttpServletRequest request, @RequestParam Map<String, Object> map){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		try{
			if(userInfo != null){
				//캠페인 코드
				
				map.put("campainCode", map.get("visibleCampainCode"));

				Map<String,Object> selectResult = evaluationResultInfoService.selectEcampVisible(map);
			
				if(!selectResult.isEmpty()){
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.addAttribute("visibleYN", selectResult.get("r_visible_yn"));
					
				}else{
					jRes.setResult("NOTHING");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
			}else{
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		}catch (Exception e){
			logger.error("error",e);
			jRes.setResult("ERROR");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		return jRes;
	}
}

