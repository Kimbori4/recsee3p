package com.furence.recsee.evaluation.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.evaluation.model.SheetInfo;
import com.furence.recsee.evaluation.service.EvaluationService;

@Controller
public class AjaxQuestionController {
	private static final Logger logger = LoggerFactory.getLogger(AjaxQuestionController.class);
	private static String replaceLast(String string, String toReplace, String replacement) {

		   int pos = string.lastIndexOf(toReplace);

		   if (pos > -1) {
			   return string.substring(0, pos)+ replacement + string.substring(pos +   toReplace.length(), string.length());
		   } else {
			   return string;
		   }
	}

	@Autowired
	private EvaluationService evaluationService;

	//@mars: 대분류 항목 삽입
	@RequestMapping(value="/insertBgInfo.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO insertBgInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){


				// 받아온 데이터 셋팅
				//대분류 항몽
				if (request.getParameter("itemCode") != null && !request.getParameter("itemCode").isEmpty()) {
					String itemCode = request.getParameter("itemCode");
					itemCode = itemCode.replaceAll("'", "''");
					sheetInfo.setItemCode(itemCode);
				}
				//대분류 내용
				if (request.getParameter("itemContent") != null && !request.getParameter("itemContent").isEmpty()) {
					sheetInfo.setItemContent(request.getParameter("itemContent"));

				}

				//대분류 코드
				if (request.getParameter("bPartCode") != null && !request.getParameter("bPartCode").isEmpty()) {
					sheetInfo.setbPartCode(request.getParameter("bPartCode"));

				}
				//중분류 코드
				if (request.getParameter("mPartCode") != null && !request.getParameter("mPartCode").isEmpty()) {
					sheetInfo.setmPartCode(request.getParameter("mPartCode"));

				}
				//소분류 코드
				if (request.getParameter("sPartCode") != null && !request.getParameter("sPartCode").isEmpty()) {
					sheetInfo.setsPartCode(request.getParameter("sPartCode"));

				}
				//점수
				if (request.getParameter("mark") != null && !request.getParameter("mark").isEmpty()) {
					sheetInfo.setMark("0");

				}
				Integer insertResult= evaluationService.insertBgInfo(sheetInfo);

				if(insertResult > 0){

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

	//@mars: 중분류 항목 삽입
	@RequestMapping(value="/insertMgInfo.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO insertMgInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){


				// 받아온 데이터 셋팅
				//중분류 항목
				if (request.getParameter("itemCode") != null && !request.getParameter("itemCode").isEmpty()) {
					String itemCode = request.getParameter("itemCode");
					itemCode = itemCode.replaceAll("'", "''");
					sheetInfo.setItemCode(itemCode);
				}
				//중분류 내용
				if (request.getParameter("itemContent") != null && !request.getParameter("itemContent").isEmpty()) {
					sheetInfo.setItemContent(request.getParameter("itemContent"));

				}

				//대분류 코드
				if (request.getParameter("bPartCode") != null && !request.getParameter("bPartCode").isEmpty()) {
					sheetInfo.setbPartCode(request.getParameter("bPartCode"));

				}
				//중분류 코드
				if (request.getParameter("mPartCode") != null && !request.getParameter("mPartCode").isEmpty()) {
					sheetInfo.setmPartCode(request.getParameter("mPartCode"));

				}
				//소분류 코드
				if (request.getParameter("sPartCode") != null && !request.getParameter("sPartCode").isEmpty()) {
					sheetInfo.setsPartCode(request.getParameter("sPartCode"));

				}
				//점수
				if (request.getParameter("mark") != null && !request.getParameter("mark").isEmpty()) {
					sheetInfo.setMark("0");

				}

				Integer insertResult = evaluationService.insertMgInfo(sheetInfo);

				if(insertResult > 0){

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

	//@mars: 소분류 항목 삽입
	@RequestMapping(value="/insertSgInfo.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO insertSgInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){


				// 받아온 데이터 셋팅
				//소분류 항몽
				if (request.getParameter("itemCode") != null && !request.getParameter("itemCode").isEmpty()) {
					String itemCode = request.getParameter("itemCode");
					itemCode = itemCode.replaceAll("'", "''");
					sheetInfo.setItemCode(itemCode);

				}
				//소분류 내용
				if (request.getParameter("itemContent") != null && !request.getParameter("itemContent").isEmpty()) {
					sheetInfo.setItemContent(request.getParameter("itemContent"));

				}

				//대분류 코드
				if (request.getParameter("bPartCode") != null && !request.getParameter("bPartCode").isEmpty()) {
					sheetInfo.setbPartCode(request.getParameter("bPartCode"));

				}
				//중분류 코드
				if (request.getParameter("mPartCode") != null && !request.getParameter("mPartCode").isEmpty()) {
					sheetInfo.setmPartCode(request.getParameter("mPartCode"));

				}
				//소분류 코드
				if (request.getParameter("sPartCode") != null && !request.getParameter("sPartCode").isEmpty()) {
					sheetInfo.setsPartCode(request.getParameter("sPartCode"));

				}
				/*//점수
				if (request.getParameter("mark") != null && !request.getParameter("mark").isEmpty()) {
					sheetInfo.setMark(Integer.parseInt(request.getParameter("itemMark")));

				}*/

				Integer insertResult = evaluationService.insertSgInfo(sheetInfo);

				if(insertResult > 0){

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

	//@mars: 세부사항 항목 삽입
	@RequestMapping(value="/insertIgInfo.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO insertIgInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){


				// 받아온 데이터 셋팅 (그리드에 대중소 코드는 다 들어가 있으니까 가져온다 일단.
				//세부항목 제목
				if (request.getParameter("itemCode") != null && !request.getParameter("itemCode").isEmpty()) {
					String itemCode = request.getParameter("itemCode");
					itemCode = itemCode.replaceAll("'", "''");
					sheetInfo.setItemCode(itemCode);
				}
				//세부항목내용
				if (request.getParameter("itemContent") != null && !request.getParameter("itemContent").isEmpty()) {
					sheetInfo.setItemContent(request.getParameter("itemContent"));
				}
				//세부항목 대분류 코드
				if (request.getParameter("bPartCode") != null && !request.getParameter("bPartCode").isEmpty()) {
					sheetInfo.setbPartCode(request.getParameter("bPartCode"));

				}
				//세부항목 중분류 코드
				if (request.getParameter("mPartCode") != null && !request.getParameter("mPartCode").isEmpty()) {
					sheetInfo.setmPartCode(request.getParameter("mPartCode"));

				}
				//세부항목 소분류 코드
				if (request.getParameter("sPartCode") != null && !request.getParameter("sPartCode").isEmpty()) {
					sheetInfo.setsPartCode(request.getParameter("sPartCode"));
				}

				//점수
				if (!StringUtil.isNull(request.getParameter("mark"), true)) {
					sheetInfo.setMark((request.getParameter("mark")));
				}else {
					sheetInfo.setMark("0");
				}

				Integer insertResult = evaluationService.insertIgInfo(sheetInfo);

				if(insertResult > 0){
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


	//@mars: 대분류 항목 삭제
	@RequestMapping(value="/deleteCodeInfo.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO deleteBgInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				// 받아온 데이터 셋팅
				String target = StringUtil.toString(request.getParameter("target"), "");
				String checked = StringUtil.toString(request.getParameter("checked"), "");

				if(!target.equals("bg") && !target.equals("mg") && !target.equals("sg") && !target.equals("ig")) {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("NOT_VALID_CODE_TYPE");
				}
				if(checked.trim().isEmpty()) {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("NO_DELETE_DATA");
				}

				int Count = 0;
				String[] checkedData;
				String checkedRow = "";
				{
					List<SheetInfo> selectSheetUse = null ;
					checkedData = checked.split(",");
					for(String data: checkedData) {
						if(target.equals("bg")){
							sheetInfo.setsBgcode(data);
							selectSheetUse = evaluationService.selectSheetUseBg(sheetInfo);

							if(selectSheetUse.size() == 0){
								Count++;
								checkedRow += "'" + data + "'" + ",";
							}
						}else if(target.equals("mg")){
							sheetInfo.setsMgcode(data);
							selectSheetUse = evaluationService.selectSheetUseMg(sheetInfo);

							if(selectSheetUse.size() == 0){
								Count++;
								checkedRow += "'" + data + "'" + ",";
							}
						}else if(target.equals("sg")){
							sheetInfo.setsSgcode(data);
							selectSheetUse = evaluationService.selectSheetUseSg(sheetInfo);

							if(selectSheetUse.size() == 0){
								Count++;
								checkedRow += "'" + data + "'" + ",";
							}
						}else if(target.equals("ig")){
							sheetInfo.setIcode(data);
							selectSheetUse = evaluationService.selectSheetUseIg(sheetInfo);

							if(selectSheetUse.size() == 0){
								Count++;
								checkedRow += "'" + data + "'" + ",";
							}
						}
//						checkedRow += "'" + data + "'" + ",";
					}
					// 마지막 쉼표 지우기
					checkedRow = checkedRow.replaceFirst(",$", "");
				}

				Integer deleteResult = -1;

				if(Count > 0){
					if(target.equals("bg")) {
						sheetInfo.setsBgcode(checkedRow);
						deleteResult = evaluationService.deleteBgInfo(sheetInfo);

					} else if(target.equals("mg")) {
						sheetInfo.setsMgcode(checkedRow);
						deleteResult = evaluationService.deleteMgInfo(sheetInfo);

					} else if(target.equals("sg")) {
						sheetInfo.setsSgcode(checkedRow);
						deleteResult = evaluationService.deleteSgInfo(sheetInfo);

					} else if(target.equals("ig")) {
						sheetInfo.setIcode(checkedRow);
						deleteResult = evaluationService.deleteIgInfo(sheetInfo);
						evaluationService.deleteSubInfo(sheetInfo);
						evaluationService.deleteSubMemoInfo(sheetInfo);						
					}

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


	//@mars: 수정하기 전 select
	@RequestMapping(value="/selectCodeInfo.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO selectCodeInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				// 받아온 데이터 셋팅

				String target = StringUtil.toString(request.getParameter("target"), "");
				String bmsCode = StringUtil.toString(request.getParameter("bmsCode"), "");

				if(!target.equals("ub") && !target.equals("um") && !target.equals("us") && !target.equals("ui")) {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("NOT_VALID_CODE_TYPE");
				}

				if(target.equals("ub")) {
					sheetInfo.setsBgcode(bmsCode);
					jRes.addAttribute("sBgName", evaluationService.upSelectBgInfo(sheetInfo).get(0).getsBgName());
					jRes.addAttribute("sBgContent", evaluationService.upSelectBgInfo(sheetInfo).get(0).getsBgContent());
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else if(target.equals("um")) {
					sheetInfo.setsMgcode(bmsCode);
					jRes.addAttribute("sMgName", evaluationService.upSelectMgInfo(sheetInfo).get(0).getsMgName());
					jRes.addAttribute("sMgContent", evaluationService.upSelectMgInfo(sheetInfo).get(0).getsMgContent());
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else if(target.equals("us")) {
					sheetInfo.setsSgcode(bmsCode);
					jRes.addAttribute("sSgName", evaluationService.upSelectSgInfo(sheetInfo).get(0).getsSgName());
					jRes.addAttribute("sSgContent", evaluationService.upSelectSgInfo(sheetInfo).get(0).getsSgContent());
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				} else if(target.equals("ui")) {
					sheetInfo.setIcode(bmsCode);
					jRes.addAttribute("itemName", evaluationService.upSelectIgInfo(sheetInfo).get(0).getItemCode());

					String subItemContent = "";
					String subItemMark = "";
					String subMemo = "";

					List<SheetInfo> upSelectSubIgInfo = evaluationService.upSelectSubIgInfo(sheetInfo);
					List<SheetInfo> upSelectSubMemoIgInfo = evaluationService.upSelectSubIgMemoInfo(sheetInfo);
					for(int i = 0; i<upSelectSubIgInfo.size(); i++) {
//						jRes.addAttribute("subItemCode", evaluationService.upSelectSubIgInfo(sheetInfo).get(i).getsItemCode());
						subItemContent += evaluationService.upSelectSubIgInfo(sheetInfo).get(i).getsItemContent() + ",";
						subItemMark += evaluationService.upSelectSubIgInfo(sheetInfo).get(i).getSitemMark() + ",";
					}
					
					for(int i = 0; i<upSelectSubMemoIgInfo.size(); i++) {
//						jRes.addAttribute("subItemCode", evaluationService.upSelectSubIgInfo(sheetInfo).get(i).getsItemCode());
						subMemo += evaluationService.upSelectSubIgMemoInfo(sheetInfo).get(i).getsItemContent() + ",";
					}
					
					jRes.addAttribute("subItemContent", subItemContent);
					jRes.addAttribute("subItemMark", subItemMark);
					jRes.addAttribute("upSelectSubIgInfoSize",upSelectSubIgInfo.size());
					jRes.addAttribute("subMemo",subMemo);
					jRes.addAttribute("upSelectSubMemoIgInfoSize",upSelectSubMemoIgInfo.size());
					/* jRes.addAttribute("itemContents", evaluationService.upSelectIgInfo(sheetInfo).get(0).getItemContent()); */

					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
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


	//@mars: 업데이트하기
	@RequestMapping(value="/updateCode.do", method = RequestMethod.POST)
	public @ResponseBody  AJaxResVO updateCode(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();


		try{
			if(userInfo != null){
				// 받아온 데이터 셋팅
				String target = "";
				if (request.getParameter("itemName") != null && !request.getParameter("itemName").isEmpty()) {
					sheetInfo.setItemName(request.getParameter("itemName"));
				}

				if (request.getParameter("itemContent") != null && !request.getParameter("itemContent").isEmpty()) {
					sheetInfo.setItemContent(request.getParameter("itemContent"));
				}

				if (request.getParameter("Icode") != null && !request.getParameter("Icode").isEmpty()) {
					String icode = "'" + request.getParameter("Icode") + "'";
					sheetInfo.setIcode(icode);
				}

				if (request.getParameter("mark") != null && !request.getParameter("mark").isEmpty()) {
					sheetInfo.setMark((request.getParameter("mark")));
				}

				if (request.getParameter("target") != null && !request.getParameter("target").isEmpty()) {
					target = request.getParameter("target");
				}

//				String bmsCode = StringUtil.toString(request.getParameter("bmsCode"), "");

				if(!target.equals("ub") && !target.equals("um") && !target.equals("us") && !target.equals("ui")) {
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
					jRes.setResult("NOT_VALID_CODE_TYPE");
				}

				Integer result = 0;
				List<SheetInfo> selectSheetUse = null ;
				if(target.equals("ub")) {
					sheetInfo.setsBgcode(sheetInfo.getIcode());
					//평가지에서 쓰고 있냐,,
					selectSheetUse = evaluationService.selectSheetUseBg(sheetInfo);
					if(selectSheetUse.size() == 0 && selectSheetUse != null){
						result = evaluationService.updateBgInfo(sheetInfo);
					}
				} else if(target.equals("um")) {
					sheetInfo.setsMgcode(sheetInfo.getIcode());

					selectSheetUse = evaluationService.selectSheetUseMg(sheetInfo);
					if(selectSheetUse.size() == 0 && selectSheetUse != null){
						result = evaluationService.updateMgInfo(sheetInfo);
					}
				} else if(target.equals("us")) {
					sheetInfo.setsSgcode(sheetInfo.getIcode());

					selectSheetUse = evaluationService.selectSheetUseSg(sheetInfo);
					if(selectSheetUse.size() == 0 && selectSheetUse != null){
						result = evaluationService.updateSgInfo(sheetInfo);
					}
				} else if(target.equals("ui")) {

					selectSheetUse = evaluationService.selectSheetUseIg(sheetInfo);
					if(selectSheetUse.size()== 0 && selectSheetUse != null){
						result = evaluationService.updateIgInfo(sheetInfo);
					}
				}

				if(result > 0){
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				}else{
					if(selectSheetUse.size() > 0){
						jRes.setResult("useSheet");
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
					}else{
						jRes.setResult("NOTHING");
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
					}
				}
			}else{
				jRes.setResult("LOGINFAIL");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
				/*Integer result=-1;
				if(target.equals("bg")) {
					sheetInfo.setsBgcode(request.getParameter("itemCode"));
					result = evaluationService.updateBgInfo(sheetInfo);
				} else if(target.equals("mg")) {
					sheetInfo.setsMgcode(request.getParameter("itemCode"));
					result = evaluationService.updateMgInfo(sheetInfo);
				} else if(target.equals("sg")) {
					sheetInfo.setsSgcode(request.getParameter("itemCode"));
					result = evaluationService.updateSgInfo(sheetInfo);
				} else if(target.equals("ui")) {
					sheetInfo.setIcode(bmsCode);
					result = evaluationService.updateIgInfo(sheetInfo);
				}*/

				/*if(result > 0){
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
		}*/


			}catch (Exception e){
				logger.error("error",e);
				jRes.setResult("ERROR");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		return jRes;
	}


	@RequestMapping(value="/insertSubInfo.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO insertSubInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){

				String subItemMark = request.getParameter("subItemMark");
				String subItemName = request.getParameter("subItemName");
				subItemName = subItemName.replaceFirst("'", "");
				subItemName = replaceLast(subItemName, "'", "");

				String[] subItemMarkArray = subItemMark.split(",");
				String[] subItemNameArray = subItemName.split("','");

				for(int i = 0; i<subItemNameArray.length ; i++){
					subItemNameArray[i] = subItemNameArray[i].replaceAll("'", "''");
				}

				String rowNum = request.getParameter("rowNum");
				int row = Integer.parseInt(rowNum);

				Integer insertsubInfo =0;

				for(int i=0;i<row;i++){
					sheetInfo.setCategorySubICode(subItemNameArray[i]);
					sheetInfo.setCategorySubIMark(subItemMarkArray[i]);

					insertsubInfo = evaluationService.insertsubInfo(sheetInfo);
				}

				if(insertsubInfo > 0){
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

	@RequestMapping(value="/updateSubInfo.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO updateSubInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){

				String subItemMark = request.getParameter("subItemMark");
				String subItemName = request.getParameter("subItemName");
				subItemName = subItemName.replaceAll("'", "''");
				String rowNum = request.getParameter("rowNum");
					
				int row = Integer.parseInt(rowNum);
				String Icode = request.getParameter("Icode");
				sheetInfo.setIcode("'" + Icode + "'");
				Integer updateSubInfo =0;

				String[] subItemMarkArray = subItemMark.split(",");
				String[] subItemNameArray = subItemName.split(",");
				Integer deleteSubInfo = evaluationService.deleteSubInfo(sheetInfo);

				for(int i=0;i<row;i++){
					sheetInfo.setCategorySubICode(subItemNameArray[i]);
					sheetInfo.setCategorySubIMark(subItemMarkArray[i]);

					updateSubInfo = evaluationService.updateSubInfo(sheetInfo);
				}
				

				
				String memochk = request.getParameter("memochk");
				String memoInsertChk = "0";
				
				int memorow = Integer.parseInt(memochk);
				
				if(memorow > 0) {
					String subItemMemo = request.getParameter("subItemMemo");
					subItemMemo = subItemMemo.replaceAll("'", "''");
					String[] subItemMemoArray = subItemMemo.split(",");
					
					evaluationService.deleteSubMemoInfo(sheetInfo);
					
					for(int i=0;i<memorow;i++) {
						sheetInfo.setCategorySubICode(subItemMemoArray[i]);
						
						updateSubInfo = evaluationService.updateSubMemoInfo(sheetInfo);
						
						if(updateSubInfo > 0) {
							memoInsertChk = "1";
						}else {
							memoInsertChk = "2";
						}
					}
					
				}

				if(updateSubInfo > 0 || memoInsertChk.equals("2")){
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
	@RequestMapping(value="/updateItemMark.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO updateItemMark(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){

				String totalMark = (request.getParameter("totalMark"));
				sheetInfo.setMark(totalMark);
				if(request.getParameter("Icode").trim().length() == 0 || request.getParameter("Icode") == null) {
					sheetInfo.setItemCode(null);
				}else {
					sheetInfo.setItemCode(request.getParameter("Icode"));
				}

				Integer updateItemMark = 0;

				updateItemMark = evaluationService.updateItemMark(sheetInfo);

				if(updateItemMark > 0){
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
	
	@RequestMapping(value="/insertInputSubInfo.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO insertInputSubInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				
				Integer insertsubInfo = 0;
				String answerType = request.getParameter("answerType");	// 수정 값은 1 또는 2
				String itemContent = request.getParameter("itemContent");
				String maxMark = request.getParameter("maxMark");
				
				// 수정
				if (answerType != null && answerType.equals("2")) {	// 답변형일경우
					itemContent = "답변";
					maxMark = "0";
				}
				
				sheetInfo.setCategorySubICode(itemContent);
				sheetInfo.setCategorySubIMark(maxMark);		
				sheetInfo.setCategorySubIType(answerType);	// 수정	
				
				insertsubInfo = evaluationService.insertInputsubInfo(sheetInfo);
				if(insertsubInfo > 0){
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
	
	@RequestMapping(value="/updateInputItemMark.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO updateInputItemMark(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				
				String maxMark = request.getParameter("maxMark");
				
				if(StringUtil.isNull(maxMark) || maxMark.length() < 1) maxMark = "0";
					
				
				sheetInfo.setMark(maxMark);	
				
				if(request.getParameter("itemContent").trim().length() == 0 || request.getParameter("itemContent") == null) {
					sheetInfo.setItemCode(null);
				}else {
					sheetInfo.setItemCode(request.getParameter("Icode"));
				}
				
				Integer updateItemMark = 0;

				updateItemMark = evaluationService.updateItemMark(sheetInfo);

				if(updateItemMark > 0){
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
	
	@RequestMapping(value="/insertMemoSubInfo.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO insertMemoSubInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){

				String subItemMemo = request.getParameter("subItemMemo");
				subItemMemo = subItemMemo.replaceFirst("'", "");
				subItemMemo = replaceLast(subItemMemo, "'", "");

				String[] subItemMemoArray = subItemMemo.split("','");

				for(int i = 0; i<subItemMemoArray.length ; i++){
					subItemMemoArray[i] = subItemMemoArray[i].replaceAll("'", "''");
				}

				String rowNum = request.getParameter("rowNum");
				String sheetCode = request.getParameter("sheetCode");
				int row = Integer.parseInt(rowNum);

				Integer insertsubInfo =0;

				for(int i=0;i<row;i++){
					sheetInfo.setCategorySubICode(subItemMemoArray[i]);
					sheetInfo.setSheetCode(sheetCode);
					insertsubInfo = evaluationService.insertSheetMemoInfo(sheetInfo);
				}

				if(insertsubInfo > 0){
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
	
	@RequestMapping(value="/updateMemoSubInfo.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody  AJaxResVO updateMemoSubInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){

				String subItemMemo = request.getParameter("subItemMemo");
				subItemMemo = subItemMemo.replaceFirst("'", "");
				subItemMemo = replaceLast(subItemMemo, "'", "");

				String[] subItemMemoArray = subItemMemo.split("','");

				for(int i = 0; i<subItemMemoArray.length ; i++){
					subItemMemoArray[i] = subItemMemoArray[i].replaceAll("'", "''");
				}

				String rowNum = request.getParameter("rowNum");
				String sheetCode = request.getParameter("sheetCode");
				int row = Integer.parseInt(rowNum);

				Integer insertsubInfo =0;

				for(int i=0;i<row;i++){
					sheetInfo.setCategorySubICode(subItemMemoArray[i]);
					sheetInfo.setSheetCode(sheetCode);
					sheetInfo.setBgoffset(String.valueOf(i));
					insertsubInfo = evaluationService.updateSheetMemoInfo(sheetInfo);
				}

				if(insertsubInfo > 0){
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
}
