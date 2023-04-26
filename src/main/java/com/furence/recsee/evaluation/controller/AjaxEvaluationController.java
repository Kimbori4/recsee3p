package com.furence.recsee.evaluation.controller;




import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.junit.internal.matchers.SubstringMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.util.ExcelView;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.evaluation.model.SheetInfo;
import com.furence.recsee.evaluation.service.EvaluationService;


@Controller
public class AjaxEvaluationController {
	private static final Logger logger = LoggerFactory.getLogger(AjaxEvaluationController.class);
	@Autowired
	private EvaluationService evaluationService;
	
	@Autowired
	private MessageSource messageSource;

	//평가 생성 페이지 삭제
	@RequestMapping(value="/deleteSheet.do", method={RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO deleteSheet(HttpServletRequest request){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){

				String checked=request.getParameter("sheetCode");
				String checkedRow = "";
				String useCampList = "";

				String[] checkedData = checked.split(",");
				int Count = 0;
				for(String data:checkedData){
//					System.out.println("data  ::: "+data);
					List<SheetInfo> checkUse = evaluationService.selectEvalUseCheck(data);
					if(checkUse.size() == 0) {
						Count++;
						checkedRow += "'" + data + "'" + ",";
					}else {
						useCampList +=  data+",";
					}
				}
				checkedRow = checkedRow.replaceFirst(",$", "");
				sheetInfo.setSheetCode(checkedRow);
				Integer deleteSheetResult = 0;
				if(Count>0) {
					deleteSheetResult = evaluationService.deleteSheetInfo(sheetInfo);
					evaluationService.deleteSheetBgCode(sheetInfo);
					evaluationService.deleteSheetMgCode(sheetInfo);
					evaluationService.deleteSheetSgCode(sheetInfo);
					evaluationService.deleteSheetICode(sheetInfo);

					if(deleteSheetResult > 0 && Count != checkedData.length){
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
						jRes.addAttribute("useList",useCampList);
					}else if(deleteSheetResult > 0) {
						jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					}else{
						jRes.setResult("NOTHING");
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
					}
				}else {
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.addAttribute("useList",useCampList);
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

	//@mars: 평가지 만들기 그리드 selectbox 불러오기
	@RequestMapping(value="/selectInfo.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO selectInfo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				// 받아온 데이터 셋팅
					jRes.addAttribute("bgList", evaluationService.selectBgInfo(sheetInfo));
					jRes.addAttribute("mgList", evaluationService.selectMgInfo(sheetInfo));
					jRes.addAttribute("sgList", evaluationService.selectSgInfo(sheetInfo));
					jRes.addAttribute("igList", evaluationService.selectIgInfo(sheetInfo));

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
	
	//@mars: 평가지 만들기 그리드 selectbox 불러오기
	@RequestMapping(value="/selectSheetMemo.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO selectSheetMemo(HttpServletRequest request) {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				
				String sheetCode = request.getParameter("sheetCode");
				
				sheetInfo.setSheetCode(sheetCode);
				
				List<SheetInfo> sheetinfoResult = evaluationService.selectSheetMemo(sheetInfo);
			
				int ResultCount = sheetinfoResult.size();
				
				jRes.addAttribute("sheetinfoResult",sheetinfoResult);
				jRes.addAttribute("ResultCount",ResultCount);
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			}else {
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

	//평가 생성 페이지 삽입
	@RequestMapping(value="/insertSheet.do", method={RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO insertSheet(HttpServletRequest request){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){

				// 받아온 데이터 셋팅
				//시트코드
				if (request.getParameter("sheetCode") != null && !request.getParameter("sheetCode").isEmpty()) {
					sheetInfo.setSheetCode(request.getParameter("sheetCode"));
				}
				//시트 이름
				if (request.getParameter("sheetName") != null && !request.getParameter("sheetName").isEmpty()) {
					sheetInfo.setSheetName(request.getParameter("sheetName"));
				}
				if (request.getParameter("sheetContent") != null && !request.getParameter("sheetContent").isEmpty()) {
					sheetInfo.setSheetContent(request.getParameter("sheetContent"));
				}
				//시트 생성자
				if (request.getParameter("sheetCreator") != null && !request.getParameter("sheetCreator").isEmpty()) {
					sheetInfo.setSheetCreator(request.getParameter("sheetCreator"));
				}
				//시트 단계
				if (request.getParameter("sheetDepth") != null && !request.getParameter("sheetDepth").isEmpty()) {
					sheetInfo.setSheetDepth(Integer.parseInt(request.getParameter("sheetDepth")));
				}
				//대분류 코드
				if (request.getParameter("sBgcode") != null && !request.getParameter("sBgcode").isEmpty()){
					sheetInfo.setsBgcode(request.getParameter("sBgcode"));
				}
				//중분류 코드
				if (request.getParameter("sMgcode") != null && !request.getParameter("sMgcode").isEmpty()){
					sheetInfo.setsMgcode(request.getParameter("sMgcode"));
				}
				//소분류 코드
				if (request.getParameter("sSgcode") != null && !request.getParameter("sSgcode").isEmpty()){
					sheetInfo.setsSgcode(request.getParameter("sSgcode"));
				}

				if (request.getParameter("rowNum") != null && !request.getParameter("rowNum").isEmpty()){
					sheetInfo.setRowNum(request.getParameter("rowNum"));
				}
				List<SheetInfo> insertResult = evaluationService.insertSheetInfo(sheetInfo);
				if(insertResult.size() > 0){
					String sheetCode = null;
					if(!StringUtil.isNull(insertResult.get(0).getSheetCode())) {
						sheetCode = insertResult.get(0).getSheetCode();
					}
					jRes.setSuccess(AJaxResVO.SUCCESS_Y);
					jRes.addAttribute("sheetCode", sheetCode);
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

	//평가지 만들기  대/중/소/세부 삽입
	@RequestMapping(value="/insertCate.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO insertCate(HttpServletRequest request){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();
		try{
			if(userInfo != null){
				String sBgcode = "", sMgcode = "", sSgcode = "", Icode = "";
				Integer sheetDepth = 4;
				//시트 단계
				if (request.getParameter("sheetDepth") != null && !request.getParameter("sheetDepth").isEmpty()) {
					sheetDepth = Integer.parseInt(request.getParameter("sheetDepth"));
				}

				//받아온 row 값
				String rowNum = "";
				int row = 1;
				if (request.getParameter("rowNum") != null && !request.getParameter("rowNum").isEmpty()) {
					sheetInfo.setRowNum(request.getParameter("rowNum"));
					rowNum = request.getParameter("rowNum");
					row = Integer.parseInt(rowNum);
				}

				//시트코드
				if (request.getParameter("sheetCode") != null && !request.getParameter("sheetCode").isEmpty()) {
					sheetInfo.setSheetCode(request.getParameter("sheetCode"));
				}
				if (request.getParameter("sBgcode") != null && !request.getParameter("sBgcode").isEmpty()) {
					sheetInfo.setsBgcode(request.getParameter("sBgcode"));
					sBgcode = request.getParameter("sBgcode");
				}
				if (request.getParameter("sMgcode") != null && !request.getParameter("sMgcode").isEmpty()) {
					sheetInfo.setsMgcode(request.getParameter("sMgcode"));
					sMgcode = request.getParameter("sMgcode");
				}
				if (request.getParameter("sSgcode") != null && !request.getParameter("sSgcode").isEmpty()) {
					sheetInfo.setsSgcode(request.getParameter("sSgcode"));
					sSgcode = request.getParameter("sSgcode");
				}
				if (request.getParameter("Icode") != null && !request.getParameter("Icode").isEmpty()) {
					Icode = request.getParameter("Icode");
				}

				Integer insertResultBg=0;
				Integer insertResultMg=0;
				Integer insertResultSg=0;
				Integer insertResultIg=0;
				String[] checkedDataBg = null;
				String[] checkedDataMg = null;
				String[] checkedDataSg = null;
				String[] checkedDataIg = Icode.split(",");

				switch(sheetDepth) {
				case 1:
					for(int i=0; i<row; i++){
						sheetInfo.setIcode(checkedDataIg[i].toString());

						insertResultIg = evaluationService.upInsertIgCate(sheetInfo);
					}
					break;
				case 2:
					checkedDataBg = sBgcode.split(",");

					for(int i=0; i<row; i++){
						sheetInfo.setsBgcode(checkedDataBg[i].toString());
						sheetInfo.setIcode(checkedDataIg[i].toString());

						insertResultBg = evaluationService.upInsertBgCate(sheetInfo);
						insertResultIg = evaluationService.upInsertIgCate(sheetInfo);
					}
					break;
				case 3:
					checkedDataBg = sBgcode.split(",");
					checkedDataMg = sMgcode.split(",");

					for(int i=0; i<row; i++){
						sheetInfo.setsBgcode(checkedDataBg[i].toString());
						sheetInfo.setsMgcode(checkedDataMg[i].toString());
						sheetInfo.setIcode(checkedDataIg[i].toString());

						insertResultBg = evaluationService.upInsertBgCate(sheetInfo);
						insertResultMg = evaluationService.upInsertMgCate(sheetInfo);
						insertResultIg = evaluationService.upInsertIgCate(sheetInfo);
					}
					break;
				case 4:
					checkedDataBg = sBgcode.split(",");
					checkedDataMg = sMgcode.split(",");
					checkedDataSg = sSgcode.split(",");

					for(int i=0; i<row; i++){
//						System.out.println("열의 수는 >>>>> "+row+"   i >>>> "+ i + "    bgData"+checkedDataBg[i].toString());
						sheetInfo.setsBgcode(checkedDataBg[i].toString());
						sheetInfo.setsMgcode(checkedDataMg[i].toString());
						sheetInfo.setsSgcode(checkedDataSg[i].toString());
						sheetInfo.setIcode(checkedDataIg[i].toString());

						insertResultBg = evaluationService.upInsertBgCate(sheetInfo);
						insertResultMg = evaluationService.upInsertMgCate(sheetInfo);
						insertResultSg = evaluationService.upInsertSgCate(sheetInfo);
						insertResultIg = evaluationService.upInsertIgCate(sheetInfo);
					}
					break;
				}

				if(insertResultIg>0){
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

	public void depthEvalBMSIinsert(Integer depth, String itemCode, String sbgCode, String smgCode, String ssgCode) {

	}
	
	//평가지 만들기  대/중/소/세부 업데이트
	@RequestMapping(value="/updateSheetItem.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO updateSheetItem(HttpServletRequest request){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅
				String sheetCode = request.getParameter("sheetCode");
				int ItmeLength = Integer.parseInt(request.getParameter("igListLiength"));
				
				String sheetDepth =  request.getParameter("sheetDepth");
				String igListAfterData = request.getParameter("igListAfterData");
				String bgListAfterData = request.getParameter("bgListAfterData");
				String mgListAfterData = request.getParameter("mgListAfterData");
				String sgListAfterData = request.getParameter("sgListAfterData");
		
				Integer updateResultBg=0;
				Integer updateResultMg=0;
				Integer updateResultSg=0;
				Integer updateResultIg=0;

				sheetInfo.setSheetCode(sheetCode);
				
				for(int i=0;i<ItmeLength;i++){
					if(sheetDepth.equals("4")) {
						sheetInfo.setsBgcode(ItmeLength == 1 ? bgListAfterData : bgListAfterData.split(",")[i]);
						sheetInfo.setsMgcode(ItmeLength == 1 ? mgListAfterData : mgListAfterData.split(",")[i]);
						sheetInfo.setsSgcode(ItmeLength == 1 ? sgListAfterData : sgListAfterData.split(",")[i]);
						sheetInfo.setIcode(ItmeLength == 1 ? igListAfterData : igListAfterData.split(",")[i]);
						sheetInfo.setBgoffset(String.valueOf(i));
						
						updateResultBg = evaluationService.updateBgCate(sheetInfo);
						updateResultMg = evaluationService.updateMgCate(sheetInfo);
						updateResultSg = evaluationService.updateSgCate(sheetInfo);
						updateResultIg = evaluationService.updateItemCate(sheetInfo);
					}else if(sheetDepth.equals("3")) {
						sheetInfo.setsBgcode(ItmeLength == 1 ? bgListAfterData : bgListAfterData.split(",")[i]);
						sheetInfo.setsMgcode(ItmeLength == 1 ? mgListAfterData : mgListAfterData.split(",")[i]);
						sheetInfo.setIcode(ItmeLength == 1 ? igListAfterData : igListAfterData.split(",")[i]);
						sheetInfo.setBgoffset(String.valueOf(i));
						
						updateResultBg = evaluationService.updateBgCate(sheetInfo);
						updateResultMg = evaluationService.updateMgCate(sheetInfo);
						updateResultIg = evaluationService.updateItemCate(sheetInfo);
					
					}else if(sheetDepth.equals("2")) {
						sheetInfo.setsBgcode(ItmeLength == 1 ? bgListAfterData : bgListAfterData.split(",")[i]);
						sheetInfo.setIcode(ItmeLength == 1 ? igListAfterData : igListAfterData.split(",")[i]);
						sheetInfo.setBgoffset(String.valueOf(i));
						
						updateResultBg = evaluationService.updateBgCate(sheetInfo);
						updateResultIg = evaluationService.updateItemCate(sheetInfo);
					
					}else if(sheetDepth.equals("1")) {
						sheetInfo.setIcode(ItmeLength == 1 ? igListAfterData : igListAfterData.split(",")[i]);
						sheetInfo.setBgoffset(String.valueOf(i));
						
						updateResultIg = evaluationService.updateItemCate(sheetInfo);
					
					}
				}
				if(updateResultIg>0){
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

	//수정 시 대/중/소/세부 삽입
	@RequestMapping(value="/upInsertCate.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO upInsertCate(HttpServletRequest request){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅
				String sBgcode = request.getParameter("sBgcode");
				String sMgcode = request.getParameter("sMgcode");
				String sSgcode = request.getParameter("sSgcode");
				String Icode = request.getParameter("Icode");
				String depth = request.getParameter("depth");
				//받아온 row 값
				String rowNum = request.getParameter("rowNum");
				int row = Integer.parseInt(rowNum);

				String[] checkedDataIg = Icode.split(",");
				String[] checkedDataBg = sBgcode.split(",");
				String[] checkedDataMg = sMgcode.split(",");
				String[] checkedDataSg = sSgcode.split(",");

				Integer insertResultBg=0;
				Integer insertResultMg=0;
				Integer insertResultSg=0;
				Integer insertResultIg=0;

				sheetInfo.setSheetCode(request.getParameter("sheetCode"));
				for(int i=0;i<row;i++){
					if(depth.equals("4")){
						sheetInfo.setsBgcode(checkedDataBg[i]);
						sheetInfo.setsMgcode(checkedDataMg[i]);
						sheetInfo.setsSgcode(checkedDataSg[i]);
						sheetInfo.setIcode(checkedDataIg[i]);

						insertResultBg = evaluationService.upInsertBgCate(sheetInfo);
						insertResultMg = evaluationService.upInsertMgCate(sheetInfo);
						insertResultSg = evaluationService.upInsertSgCate(sheetInfo);
						insertResultIg = evaluationService.upInsertIgCate(sheetInfo);
					}else if(depth.equals("3")){

						sheetInfo.setsMgcode(checkedDataMg[i]);
						sheetInfo.setsSgcode(checkedDataSg[i]);
						sheetInfo.setIcode(checkedDataIg[i]);

						insertResultMg = evaluationService.upInsertMgCate(sheetInfo);
						insertResultSg = evaluationService.upInsertSgCate(sheetInfo);
						insertResultIg = evaluationService.upInsertIgCate(sheetInfo);
					}else if(depth.equals("2")){

						sheetInfo.setsSgcode(checkedDataSg[i]);
						sheetInfo.setIcode(checkedDataIg[i]);

						insertResultSg = evaluationService.upInsertSgCate(sheetInfo);
						insertResultIg = evaluationService.upInsertIgCate(sheetInfo);
					}else{

						sheetInfo.setIcode(checkedDataIg[i]);

						insertResultIg = evaluationService.upInsertIgCate(sheetInfo);
					}

				}


				if(insertResultBg > 0 && insertResultMg >0 && insertResultSg>0 && insertResultIg>0){
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



	//평가지 만들기  대/중/소/세부 업데이트
	@RequestMapping(value="/updateCate.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO updateCate(HttpServletRequest request){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅
				String sheetCode = request.getParameter("sheetCode");
				sheetInfo.setSheetCode(sheetCode);
				String sheetName = request.getParameter("sheetName");
				String sheetContent = request.getParameter("sheetContent");
				sheetInfo.setSheetName(sheetName);
				sheetInfo.setSheetContent(sheetContent);

				String categoryBCode = request.getParameter("categoryBCode");
				String categoryMCode = request.getParameter("categoryMCode");
				String categorySCode = request.getParameter("categorySCode");
				String categoryICode = request.getParameter("categoryICode");
				String sBgcode = request.getParameter("sBgcode");
				String sMgcode = request.getParameter("sMgcode");
				String sSgcode = request.getParameter("sSgcode");
				String Icode = request.getParameter("Icode");
				//받아온 row 값

				Integer updateResultBg=0;
				Integer updateResultMg=0;
				Integer updateResultSg=0;
				Integer updateResultIg=0;

				sheetInfo.setsBgcode(sBgcode);
				sheetInfo.setsMgcode(sMgcode);
				sheetInfo.setsSgcode(sSgcode);
				sheetInfo.setIcode(Icode);

				sheetInfo.setCategoryBCode(categoryBCode);
				sheetInfo.setCategoryMCode(categoryMCode);
				sheetInfo.setCategorySCode(categorySCode);
				sheetInfo.setCategoryICode(categoryICode);

				updateResultBg = evaluationService.updateBgCate(sheetInfo);
				updateResultMg = evaluationService.updateMgCate(sheetInfo);
				updateResultSg = evaluationService.updateSgCate(sheetInfo);
				updateResultIg = evaluationService.updateItemCate(sheetInfo);



				if(updateResultBg > 0 && updateResultMg >0 && updateResultSg>0 && updateResultIg>0){
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

	//평가지 만들기  대/중/소/세부 업데이트
	@RequestMapping(value="/updateSheet.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO updateSheet(HttpServletRequest request){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅
				String sheetCode = request.getParameter("sheetCode");
				sheetInfo.setSheetCode(sheetCode);
				String sheetName = request.getParameter("sheetName");
				String sheetContent = request.getParameter("sheetContent");
				String rowNum = request.getParameter("rowNum");
				sheetInfo.setRowNum(rowNum);

				sheetInfo.setSheetName(sheetName);
				sheetInfo.setSheetContent(sheetContent);
				//받아온 row 값

				Integer updateResultSheet = evaluationService.updateSheetInfo(sheetInfo);

				if(updateResultSheet>0){
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



	//캠페인 화면 수정 버튼 눌렀을 때 팝업창에 해당 조건을 뿌려주는 ajax
	@RequestMapping(value="/upSelectSheet.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO upSelectSheet(HttpServletRequest request){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅
				if (request.getParameter("sheetCode") != null && !request.getParameter("sheetCode").isEmpty()) {
					sheetInfo.setSheetCode(request.getParameter("sheetCode"));
				}
				if (request.getParameter("sheetName") != null && !request.getParameter("sheetName").isEmpty()) {
					sheetInfo.setSheetName(request.getParameter("sheetName"));
				}
				if (request.getParameter("sheetContent") != null && !request.getParameter("sheetContent").isEmpty()) {
					sheetInfo.setSheetContent(request.getParameter("sheetContent"));
				}
				if (request.getParameter("sheetDepth") != null && !request.getParameter("sheetDepth").isEmpty()) {
					sheetInfo.setSheetDepth(Integer.parseInt(request.getParameter("sheetDepth")));
				}

				jRes.setSuccess(AJaxResVO.SUCCESS_Y);

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

	//평가 생성 페이지 삭제
	@RequestMapping(value="/deleteCate.do", method={RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO deleteCate(HttpServletRequest request){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();

		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){

				String sheetCode = request.getParameter("sheetCode");
				sheetInfo.setSheetCode(sheetCode);

				String checked = request.getParameter("categoryBCode");
				String checkedRow ="";

				String[] checkedData = checked.split(",");
				for(String data:checkedData){
					checkedRow += "'" + data + "'" + ",";
				}
				checkedRow = checkedRow.replaceFirst(",$", "");
				sheetInfo.setCategoryBCode(checkedRow);

				Integer deleteBgResult = evaluationService.deleteRegBCode(sheetInfo);
				Integer deleteMgResult = evaluationService.deleteRegMCode(sheetInfo);
				Integer deleteSgResult = evaluationService.deleteRegSCode(sheetInfo);
				Integer deleteIgResult = evaluationService.deleteRegICode(sheetInfo);

				if(deleteBgResult > 0 && deleteMgResult>0 && deleteSgResult>0 && deleteIgResult>0){
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

	@RequestMapping(value="/upSelectEvaluation.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO upSelectEvaluation(HttpServletRequest request){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅
				if (request.getParameter("sheetCode") != null && !request.getParameter("sheetCode").isEmpty()) {
					sheetInfo.seteCampCode(request.getParameter("campCode"));
					sheetInfo.setSheetCode(request.getParameter("sheetCode"));
//					List<SheetInfo> selectedFeedback = evaluationService.selectFeedback(sheetInfo);
//					jRes.addAttribute("selectedFeedback",selectedFeedback);
					jRes.addAttribute("userInfo",userInfo);
				} else {
					jRes.setResult("ajax로 넘겨받은 데이터가 없습니다");
					jRes.setSuccess(AJaxResVO.SUCCESS_N);
				}
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);

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

	@RequestMapping(value="/changeItemScore.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO changeItemScore(HttpServletRequest request){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅
				if (request.getParameter("itemCode") != null && !request.getParameter("itemCode").isEmpty()) {
					String itemCode = request.getParameter("itemCode");
					sheetInfo.setIcode(itemCode);
					List<SheetInfo> tempList = evaluationService.upSelectIgInfo(sheetInfo);
					sheetInfo.setChangeItemScore(Integer.parseInt(tempList.get(0).getMark()));

				}

				jRes.addAttribute("changeItemScore", sheetInfo.getChangeItemScore());
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);

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

	@RequestMapping(value="/subItemSearch.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO subItemSearch(HttpServletRequest request){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();
		try{
			if(userInfo != null){
				//받아온 데이터 셋팅
				if (request.getParameter("itemCode") != null && !request.getParameter("itemCode").isEmpty())
					sheetInfo.setIcode(request.getParameter("itemCode"));

				List<SheetInfo> subItemList = evaluationService.selectSubItemInfo(sheetInfo);
				if(subItemList.size()>0) {
					for(int i=0; i<subItemList.size(); i++) {
//						System.out.println("subitem code == "+subItemList.get(i).getsItemCode()); //sub item code
//						System.out.println("subitem mark == "+subItemList.get(i).getsItemMark()); //sub item code
//						System.out.println("subitem content == "+subItemList.get(i).getsItemContent()); //sub item code
					}
				}
				jRes.addAttribute("subItemList", subItemList);
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);


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

	@RequestMapping(value="/selectFeedback.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO selectFeedback(HttpServletRequest request){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅
				sheetInfo.seteCampCode(request.getParameter("campCode"));
				List<SheetInfo> selectedFeedback = evaluationService.selectFeedback(sheetInfo);
				jRes.addAttribute("selectedFeedback",selectedFeedback);
				jRes.addAttribute("userInfo",userInfo);

				jRes.setSuccess(AJaxResVO.SUCCESS_Y);

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

	@RequestMapping(value="/selectEvalStatus.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO selectEvalStatus(HttpServletRequest request){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅
				sheetInfo.setRecFileName(request.getParameter("fileName"));
				jRes.addAttribute("selectedEvalResult",evaluationService.selectEvalStatus(sheetInfo));

				jRes.setSuccess(AJaxResVO.SUCCESS_Y);

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

	//소그룹 명으로 캠페인 찾기
	@RequestMapping(value="/a.do", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody AJaxResVO a(HttpServletRequest request){
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();

		try{
			if(userInfo != null){
				//받아온 데이터 셋팅
				sheetInfo.setRecFileName(request.getParameter("fileName"));
				jRes.addAttribute("selectedEvalResult",evaluationService.selectEvalStatus(sheetInfo));

				jRes.setSuccess(AJaxResVO.SUCCESS_Y);

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

	@RequestMapping(value = "/evalSheetPage")
	public ModelAndView evalSheetPage(HttpServletRequest request, Model model) {
		ModelAndView result = new ModelAndView();

		if(!StringUtil.isNull(request.getParameter("listenUrl"))) {
			model.addAttribute("listenUrl",request.getParameter("listenUrl"));
		}
		if(!StringUtil.isNull(request.getParameter("rRecId"))) {
			model.addAttribute("rRecId",request.getParameter("rRecId"));
		}
		if(!StringUtil.isNull(request.getParameter("rRecName"))) {
			try {
				String rRecName = URLDecoder.decode(request.getParameter("rRecName"),"UTF-8");
				model.addAttribute("rRecName",rRecName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				logger.error("error",e);
			}
		}
		if(!StringUtil.isNull(request.getParameter("rRecDate"))) {
			model.addAttribute("rRecDate",request.getParameter("rRecDate"));
		}
		if(!StringUtil.isNull(request.getParameter("rRecTime"))) {
			model.addAttribute("rRecTime",request.getParameter("rRecTime"));
		}
		if(!StringUtil.isNull(request.getParameter("ip"))) {
			model.addAttribute("ip",request.getParameter("ip"));
		}
		if(!StringUtil.isNull(request.getParameter("port"))) {
			model.addAttribute("port",request.getParameter("port"));
		}
		if(!StringUtil.isNull(request.getParameter("firstEvalYN"))) {
			model.addAttribute("firstEvalYN",request.getParameter("firstEvalYN"));
		}
		if(!StringUtil.isNull(request.getParameter("cclist"))) {
			try {
				String cclist = URLDecoder.decode(request.getParameter("cclist"),"UTF-8");
				model.addAttribute("cclist",cclist);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				logger.error("error",e);
			}
		}
		if(!StringUtil.isNull(request.getParameter("cnlist"))) {
			try {
				String cnlist = URLDecoder.decode(request.getParameter("cnlist"),"UTF-8");
				model.addAttribute("cnlist",cnlist);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				logger.error("error",e);
			}
		}if(!StringUtil.isNull(request.getParameter("evaluator"))) {
			model.addAttribute("evaluator",request.getParameter("evaluator"));
		}
		if(!StringUtil.isNull(request.getParameter("evalatorId"))) {
			model.addAttribute("evalatorId",request.getParameter("evalatorId"));
		}
		if(!StringUtil.isNull(request.getParameter("evalatorName"))) {
			try {
				String evalatorName = URLDecoder.decode(request.getParameter("evalatorName"),"UTF-8");
				model.addAttribute("evalatorName",evalatorName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				logger.error("error",e);
			}
		}
		if(!StringUtil.isNull(request.getParameter("evalCode"))) {
			model.addAttribute("evalCode",request.getParameter("evalCode"));
		}
		if(!StringUtil.isNull(request.getParameter("campCode"))) {
			model.addAttribute("campCode",request.getParameter("campCode"));
		}
		if(!StringUtil.isNull(request.getParameter("userLevel"))) {
			model.addAttribute("levelUser",request.getParameter("userLevel"));
		}
		if(!StringUtil.isNull(request.getParameter("custName"))) {
			model.addAttribute("custName",request.getParameter("custName"));
		}
		if(!StringUtil.isNull(request.getParameter("custPhone"))) {
			model.addAttribute("custPhone",request.getParameter("custPhone"));
		}
		if(!StringUtil.isNull(request.getParameter("userType"))) {
			model.addAttribute("userType",request.getParameter("userType"));
		}
		if(!StringUtil.isNull(request.getParameter("mgCode"))) {
			model.addAttribute("mgCode",request.getParameter("mgCode"));
		}
		if(!StringUtil.isNull(request.getParameter("sgCode"))) {
			model.addAttribute("sgCode",request.getParameter("sgCode"));
		}
		if(!StringUtil.isNull(request.getParameter("bgCode"))) {
			model.addAttribute("bgCode",request.getParameter("bgCode"));
		}
		if(!StringUtil.isNull(request.getParameter("feedbackModifyYn"))) {
			model.addAttribute("feedbackModifyYn",request.getParameter("feedbackModifyYn"));
		}

		String evalThema = (String)request.getSession().getAttribute("evalThema");//평가 테마값 받기
		if(evalThema.equals("master")) {
			result.setViewName("/evaluation_master/evaluation_sheet");
		}else {
			result.setViewName("/evaluation/evaluation_sheet");
		}
//		result.setViewName("/evaluation/evaluation_sheet");
		return result;
	}

	@RequestMapping(value="/excelDownloadSheet.do")
	public void excelDownloadSheet(HttpServletRequest request, Map<String,Object> ModelMap, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();
//		System.out.println("excel download start!!!");
		int excelIndex = 0;
		if(userInfo != null) {
			if(!StringUtil.isNull(request.getParameter("sheetCode"))) {
				sheetInfo.setSheetCode(request.getParameter("sheetCode"));
			}
			List<String[]> contents = new ArrayList<String[]>();
			String[] row = null;

			List<SheetInfo> selectResult = evaluationService.excelDownSheetInfo(sheetInfo);

			if(selectResult.size()>0) {
				excelIndex = selectResult.size()+1; //엑셀 row 수
				Integer evalDepth = selectResult.get(0).getSheetDepth(); // 몇단계짜리 평가지인지 체크
//				System.out.println("DEPTH ?????? " + evalDepth);
				int arraySize = evalDepth + 2; // 배열크기 처리용 변수   >>1dep = 3  //2dep = 4  //3dep = 5  //4dep = 6
				row = new String[arraySize];
				String titleName=""; // 타이틀처리용 변수

				for(int z=0; z<arraySize; z++) {
					switch(evalDepth) {
					case 1:
						switch(z) {
						case 0:	titleName = messageSource.getMessage("evaluation.management.sheet.item", null,Locale.getDefault()); break; //상세내용
						case 1: titleName = "항목"; break;
						case 2: titleName = messageSource.getMessage("evaluation.selectPeriod.evalScore", null,Locale.getDefault()); break; //배점
						}
						row[z] = titleName;
						break;
					case 2:
						switch(z) {
						case 0: titleName = messageSource.getMessage("evaluation.management.sheet.Small", null,Locale.getDefault()); break; //소분류
						case 1:	titleName = messageSource.getMessage("evaluation.management.sheet.item", null,Locale.getDefault()); break; //상세내용
						case 2: titleName = "항목"; break;
						case 3: titleName = messageSource.getMessage("evaluation.selectPeriod.evalScore", null,Locale.getDefault()); break; //배점
						}
						row[z] = titleName;
						break;
					case 3:
						switch(z) {
						case 0: titleName = messageSource.getMessage("evaluation.management.sheet.Middle", null,Locale.getDefault()); break; //중분류
						case 1: titleName = messageSource.getMessage("evaluation.management.sheet.Small", null,Locale.getDefault()); break; //소분류
						case 2:	titleName = messageSource.getMessage("evaluation.management.sheet.item", null,Locale.getDefault()); break; //상세내용
						case 3: titleName = "항목"; break;
						case 4: titleName = messageSource.getMessage("evaluation.selectPeriod.evalScore", null,Locale.getDefault()); break; //배점
						}
						row[z] = titleName;
						break;
					case 4:
						switch(z) {
						case 0: titleName = messageSource.getMessage("evaluation.management.sheet.big", null,Locale.getDefault()); break; //대분류
						case 1: titleName = messageSource.getMessage("evaluation.management.sheet.Middle", null,Locale.getDefault()); break; //중분류
						case 2: titleName = messageSource.getMessage("evaluation.management.sheet.Small", null,Locale.getDefault()); break; //소분류
						case 3:	titleName = messageSource.getMessage("evaluation.management.sheet.item", null,Locale.getDefault()); break; //상세내용
						case 4: titleName = "항목"; break;
						case 5: titleName = messageSource.getMessage("evaluation.selectPeriod.evalScore", null,Locale.getDefault()); break; //배점
						}
						row[z] = titleName;
						break;
					}
				}
				contents.add(row);

				String col = "";
				for(int i=0; i<selectResult.size(); i++) {
					SheetInfo sheetItem = selectResult.get(i);
					switch(evalDepth) { //평가지 단계에 따라 타는곳이 달라짐 1dep, 2dep, 3dep, 4dep.. 평가지
					case 1:
						row = new String[arraySize];
						for(int z=0; z<arraySize; z++) {
							if(i != 0) {
								SheetInfo sheetBeforeItem = selectResult.get(i-1);
								switch(z) {
								case 0:
									if(sheetItem.getIcode().equals(sheetBeforeItem.getIcode())) { col = "";}
									else { col = sheetItem.getItemCode(); }
									break;
								case 1:
									col = sheetItem.getsItemContent();
									break;
								case 2:
									col = sheetItem.getSitemMark();
									break;
								}
							}else {
								switch(z) {
								case 0: col = sheetItem.getItemCode(); break;
								case 1: col = sheetItem.getsItemContent(); break;
								case 2: col = sheetItem.getSitemMark(); break;
								}
							}
							row[z] = col;
						}
						contents.add(row);
						break;
					case 2:
						row = new String[arraySize];
						for(int z=0; z<arraySize; z++) {
							if(i != 0) {
								SheetInfo sheetBeforeItem = selectResult.get(i-1);
								switch(z) {
								case 0:
									if(sheetItem.getCategorySCode().equals(sheetBeforeItem.getCategorySCode())) { col = "";}
									else { col = sheetItem.getsSgName(); }
									break;
								case 1:
									if(sheetItem.getIcode().equals(sheetBeforeItem.getIcode())) { col = "";}
									else { col = sheetItem.getItemCode(); }
									break;
								case 2:
									col = sheetItem.getsItemContent();
									break;
								case 3:
									col = sheetItem.getSitemMark();
									break;
								}
							}else {
								switch(z) {
								case 0: col = sheetItem.getsSgName(); break;
								case 1: col = sheetItem.getItemCode(); break;
								case 2: col = sheetItem.getsItemContent(); break;
								case 3: col = sheetItem.getSitemMark(); break;
								}
							}
							row[z] = col;
						}
						contents.add(row);
						break;
					case 3:
						row = new String[arraySize];
						for(int z=0; z<arraySize; z++) {
							if(i != 0) {
								SheetInfo sheetBeforeItem = selectResult.get(i-1);
								switch(z) {
								case 0:
									if(sheetItem.getCategoryMCode().equals(sheetBeforeItem.getCategoryMCode())) { col = "";}
									else { col = sheetItem.getsMgName(); }
									break;
								case 1:
									if(sheetItem.getCategorySCode().equals(sheetBeforeItem.getCategorySCode())) { col = "";}
									else { col = sheetItem.getsSgName(); }
									break;
								case 2:
									if(sheetItem.getIcode().equals(sheetBeforeItem.getIcode())) { col = "";}
									else { col = sheetItem.getItemCode(); }
									break;
								case 3:
									col = sheetItem.getsItemContent();
									break;
								case 4:
									col = sheetItem.getSitemMark();
									break;
								}
							}else {
								switch(z) {
								case 0: col = sheetItem.getsMgName(); break;
								case 1: col = sheetItem.getsSgName(); break;
								case 2: col = sheetItem.getItemCode(); break;
								case 3: col = sheetItem.getsItemContent(); break;
								case 4: col = sheetItem.getSitemMark(); break;
								}
							}
							row[z] = col;
						}
						contents.add(row);
						break;
					case 4:
						row = new String[arraySize];
						for(int z=0; z<arraySize; z++) {
							if(i != 0) {
								SheetInfo sheetBeforeItem = selectResult.get(i-1);
								switch(z) {
								case 0:
									if(sheetItem.getCategoryBCode().equals(sheetBeforeItem.getCategoryBCode())) { col = "";}
									else { col = sheetItem.getsBgName(); }
									break;
								case 1:
									if(sheetItem.getCategoryMCode().equals(sheetBeforeItem.getCategoryMCode())) { col = "";}
									else { col = sheetItem.getsMgName(); }
									break;
								case 2:
									if(sheetItem.getCategorySCode().equals(sheetBeforeItem.getCategorySCode())) { col = "";}
									else { col = sheetItem.getsSgName(); }
									break;
								case 3:
									if(sheetItem.getIcode().equals(sheetBeforeItem.getIcode())) { col = "";}
									else { col = sheetItem.getItemCode(); }
									break;
								case 4:
									col = sheetItem.getsItemContent();
									break;
								case 5:
									col = sheetItem.getSitemMark();
									break;
								}
							}else {
								switch(z) {
								case 0: col = sheetItem.getsBgName(); break;
								case 1: col = sheetItem.getsMgName(); break;
								case 2: col = sheetItem.getsSgName(); break;
								case 3: col = sheetItem.getItemCode(); break;
								case 4: col = sheetItem.getsItemContent(); break;
								case 5: col = sheetItem.getSitemMark(); break;
								}
							}
//							System.out.println(z+"  :  "+col);
							row[z] = col;
						}
						contents.add(row);
						break;
					}

				}

				ModelMap.put("excelList", contents);
				ModelMap.put("target", request.getParameter("fileName"));
			}else {

			}
		}else {

		}
//		System.out.println("excel create ::::::::::: ");
		String realPath = request.getSession().getServletContext().getRealPath("/eval");
		ExcelView.createEvalXlsx(ModelMap, realPath, response);

	}



	@RequestMapping(value="/excelDownloadSheetResult.do")
	public void excelDownloadSheetResult(HttpServletRequest request, Map<String,Object> ModelMap, HttpServletResponse response, Locale local) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();
//		System.out.println("excel download start!!!");
		int excelIndex = 0;
		int memoCnt = 0;
		if(userInfo != null) {
			if(!StringUtil.isNull(request.getParameter("evalCode"))) {
				sheetInfo.setEvalCode(request.getParameter("evalCode"));
			}
			
			if(!StringUtil.isNull(request.getParameter("sheetCode"))) {
				sheetInfo.setSheetCode(request.getParameter("sheetCode"));
			}
			
			if(!StringUtil.isNull(request.getParameter("memoCnt"))) {
				memoCnt = Integer.parseInt(request.getParameter("memoCnt"));
			}
			
			List<String[]> contents = new ArrayList<String[]>();
			String[] row = null;
			
			sheetInfo.setTotalScoreString(messageSource.getMessage("evaluation.management.sheet.totalScoreString", null,Locale.getDefault()));
			sheetInfo.setEvaluatorOp(messageSource.getMessage("evaluation.management.sheet.evaluatorOp", null,Locale.getDefault()));
			sheetInfo.setCounselorOp(messageSource.getMessage("evaluation.management.sheet.counselorOp", null,Locale.getDefault()));
				
			List<SheetInfo> selectResult = evaluationService.excelDownSheetInfoResult(sheetInfo);
			
			
			List<SheetInfo> selectSheetMemoResult = evaluationService.selectSheetMemo(sheetInfo);			
			List<SheetInfo> selectMemoResult = evaluationService.selectResultMemo(sheetInfo);		
						
			int memoLength = selectMemoResult.size();
			String regItemCode = "";
			int memoInddex = 0;

			if(selectResult.size()>0) {
				excelIndex = selectResult.size()+1; //엑셀 row 수
				Integer evalDepth = selectResult.get(0).getSheetDepth(); // 몇단계짜리 평가지인지 체크
//				System.out.println("DEPTH ?????? " + evalDepth);
				int arraySize = evalDepth + memoCnt + 3-1; //+ memoLength; // 배열크기 처리용 변수   >>1dep = 4  //2dep = 5  //3dep = 6  //4dep = 7
				row = new String[arraySize];
				String titleName="";
				//타이틀 정의
				for(int z=0; z<arraySize; z++) {
					switch(evalDepth) {
					case 1:
						switch(z) {
						case 0:	titleName = messageSource.getMessage("evaluation.management.sheet.details", null,Locale.getDefault()); break;//상세내용
						case 1: titleName = messageSource.getMessage("evaluation.management.sheet.itemS", null,Locale.getDefault()); break;//항목
						//case 2: titleName = messageSource.getMessage("evaluation.selectPeriod.evalScore", null,Locale.getDefault()); break;//배점
						case 2:	titleName = messageSource.getMessage("evaluation.management.sheet.score2", null,Locale.getDefault()); break;//점수
						case 3:	titleName = selectSheetMemoResult.get(0).getMemoContent(); break;// 메모1
						case 4:	titleName = selectSheetMemoResult.get(1).getMemoContent(); break;// 메모2
						}
						row[z] = titleName;
						break;
					case 2:
						switch(z) {
						case 0: titleName = messageSource.getMessage("evaluation.management.sheet.big", null,Locale.getDefault()); break;//대분류
						case 1:	titleName = messageSource.getMessage("evaluation.management.sheet.details", null,Locale.getDefault()); break;//상세내용
						case 2: titleName = messageSource.getMessage("evaluation.management.sheet.itemS", null,Locale.getDefault()); break;//항목
						//case 3: titleName = messageSource.getMessage("evaluation.selectPeriod.evalScore", null,Locale.getDefault()); break;//배점
						case 3:	titleName = messageSource.getMessage("evaluation.management.sheet.score2", null,Locale.getDefault()); break;//점수
						case 4:	titleName = selectSheetMemoResult.get(0).getMemoContent(); break;// 메모1
						case 5:	titleName = selectSheetMemoResult.get(1).getMemoContent(); break;// 메모2
						}
						row[z] = titleName;
						break;
					case 3:
						switch(z) {
						case 0: titleName = messageSource.getMessage("evaluation.management.sheet.big", null,Locale.getDefault()); break;//대분류
						case 1: titleName = messageSource.getMessage("evaluation.management.sheet.Middle", null,Locale.getDefault()); break;//중분류
						case 2:	titleName = messageSource.getMessage("evaluation.management.sheet.details", null,Locale.getDefault()); break;//상세내용
						case 3: titleName = messageSource.getMessage("evaluation.management.sheet.itemS", null,Locale.getDefault()); break;//항목
						//case 4: titleName = messageSource.getMessage("evaluation.selectPeriod.evalScore", null,Locale.getDefault()); break;//배점
						case 4:	titleName = messageSource.getMessage("evaluation.management.sheet.score2", null,Locale.getDefault()); break;//점수
						case 5:	titleName = selectSheetMemoResult.get(0).getMemoContent(); break;// 메모1
						case 6:	titleName = selectSheetMemoResult.get(1).getMemoContent(); break;// 메모2
						}
						row[z] = titleName;
						break;
					case 4:
						switch(z) {
						case 0: titleName = messageSource.getMessage("evaluation.management.sheet.big", null,Locale.getDefault()); break;//대분류
						case 1: titleName = messageSource.getMessage("evaluation.management.sheet.Middle", null,Locale.getDefault()); break;//중분류
						case 2: titleName = messageSource.getMessage("evaluation.management.sheet.Small", null,Locale.getDefault()); break;//소분류
						case 3:	titleName = messageSource.getMessage("evaluation.management.sheet.details", null,Locale.getDefault()); break;//상세내용
						case 4: titleName = messageSource.getMessage("evaluation.management.sheet.itemS", null,Locale.getDefault()); break;//항목
						//case 5: titleName = messageSource.getMessage("evaluation.selectPeriod.evalScore", null,Locale.getDefault()); break;//배점
						case 5:	titleName = messageSource.getMessage("evaluation.management.sheet.score2", null,Locale.getDefault()); break;//점수
						case 6:	titleName = selectSheetMemoResult.get(0).getMemoContent(); break;// 메모1
						case 7:	titleName = selectSheetMemoResult.get(1).getMemoContent(); break;// 메모2
						}
						row[z] = titleName;
						break;
					}
				}
				contents.add(row);

				String col = "";
				for(int i=0; i<selectResult.size(); i++) {
					SheetInfo sheetItem = selectResult.get(i);

					if(regItemCode.trim().length() == 0 ){			
						regItemCode = sheetItem.getCategoryICode();
					}
					
					if(sheetItem.getCategoryICode() != null) {
						if(!regItemCode.equals(sheetItem.getCategoryICode())) {
							regItemCode = sheetItem.getCategoryICode();
							memoInddex++;
						}
					}
					
					if(memoInddex > 0 && sheetItem.getCategoryICode() == null) {
						memoInddex++;
					}
						
					
					switch(evalDepth) { //평가지 단계에 따라 타는곳이 달라짐 1dep, 2dep, 3dep, 4dep.. 평가지
					case 1:
						row = new String[arraySize];
						for(int z=0; z<arraySize; z++) {
							if(i != 0) {
								SheetInfo sheetBeforeItem = selectResult.get(i-1);
								switch(z) {
								case 0:
									if(StringUtil.isNull(sheetItem.getIcode())){
										if(StringUtil.isNull(sheetItem.getSheetName()) || sheetItem.getSheetName().equals("")){ sheetItem.setSheetName("-"); }
										col = sheetItem.getSheetName();
									}
									else if(sheetItem.getIcode().equals(sheetBeforeItem.getIcode())) { col = ""; }
									else { col = sheetItem.getItemCode(); }
									break;
								case 1:
									if(StringUtil.isNull(sheetItem.getsItemContent())){ col = sheetItem.getSheetName(); }
									else{ col = sheetItem.getsItemContent()+" : "+sheetItem.getSitemMark(); }
									break;
								/*case 2:
									if(StringUtil.isNull(sheetItem.getSitemMark())){ col = sheetItem.getSheetName(); }
									else{ col = sheetItem.getSitemMark(); }
									break;*/
								case 2:
									if(StringUtil.isNull(sheetItem.getCategoryICode()) || StringUtil.isNull(sheetBeforeItem.getCategoryICode())){
										col = sheetItem.getSheetName(); 
									}
									else{
										//String code = sheetItem.getCategoryBCode().toString() + sheetItem.getCategoryMCode().toString() + sheetItem.getCategorySCode().toString() + sheetItem.getCategoryICode().toString();
										//String beforeCode = sheetBeforeItem.getCategoryBCode().toString() + sheetBeforeItem.getCategoryMCode().toString() + sheetBeforeItem.getCategorySCode().toString() + sheetBeforeItem.getCategoryICode().toString();

										//if(code.equals(beforeCode)) {
										//	col = "";
										//}else {
											col = sheetItem.getMark();
										//}
									}
									break;
								case 3:
									
									if(memoCnt == 1) {
										if( memoInddex < memoLength) {
											if(!StringUtil.isNull(selectMemoResult.get(i-1).getMemoMark())) {
												col = selectMemoResult.get(i-1).getMemoMark();
											}else {
												col = "";	
											}
										}else {
											col = "";
										}
									}else if(memoCnt == 2) {
										if( memoInddex < memoLength/2) {
												col = selectMemoResult.get(memoInddex==0?0:memoInddex*2).getMemoMark();		
										}else {
											col = "";
										}
									}
									break;
								case 4:
									if( memoInddex < memoLength/2) {
										col = selectMemoResult.get(memoInddex==0?1:memoInddex*2+1).getMemoMark();			
									}else {
										col = "";
									}
									break;
								}
							}else {
								switch(z) {
								case 0: col = sheetItem.getItemCode(); break;
								case 1: col = sheetItem.getsItemContent()+" : "+sheetItem.getSitemMark(); break;
								//case 2: col = sheetItem.getSitemMark(); break;
								case 2:
									if(StringUtil.isNull(sheetItem.getMark())){ col = sheetItem.getSheetName();}
									else{col = sheetItem.getMark();}
									break;
								case 3: col = selectMemoResult.get(0).getMemoMark(); break;
								case 4: col = selectMemoResult.get(1).getMemoMark(); break;
								}
							}
							row[z] = col;
						}
						contents.add(row);
						break;
					case 2:
						row = new String[arraySize];
						for(int z=0; z<arraySize; z++) {
							if(i != 0) {
								SheetInfo sheetBeforeItem = selectResult.get(i-1);
								switch(z) {
								case 0:
									if(sheetItem.getCategoryBCode().equals("2147483646")){ col = (sheetItem.getSheetCode()).substring(1); }
									else if(sheetItem.getCategoryBCode().equals(sheetBeforeItem.getCategoryBCode())) { col = ""; }
									else { col = sheetItem.getsBgName(); }
									break;
								case 1:
									if(StringUtil.isNull(sheetItem.getIcode())){
										if(StringUtil.isNull(sheetItem.getSheetName()) || sheetItem.getSheetName().equals("") ){ sheetItem.setSheetName("-"); }
										col = sheetItem.getSheetName();
									}
									else if(sheetItem.getIcode().equals(sheetBeforeItem.getIcode())) { col = ""; }
									else { col = sheetItem.getItemCode(); }
									break;
								case 2:
									if(StringUtil.isNull(sheetItem.getsItemContent())){ col = sheetItem.getSheetName(); }
									else{ col = sheetItem.getsItemContent()+" : "+sheetItem.getSitemMark(); }
									break;
								/*case 3:
									if(StringUtil.isNull(sheetItem.getSitemMark())){ col = sheetItem.getSheetName(); }
									else{ col = sheetItem.getSitemMark(); }
									break;*/
								case 3:
									if(sheetItem.getCategoryBCode().equals("2147483646")){ col = sheetItem.getSheetName(); }
									else{
										String code = sheetItem.getCategoryBCode().toString() + sheetItem.getCategoryICode().toString();
										String beforeCode = sheetBeforeItem.getCategoryBCode().toString() + sheetBeforeItem.getCategoryICode().toString();

										if(code.equals(beforeCode)) {
											col = "";
										}else {
											col = sheetItem.getMark();
										}
									}
									break;
								case 4:
									
									if(memoCnt == 1) {
										if( memoInddex < memoLength) {
											if(!StringUtil.isNull(selectMemoResult.get(memoInddex).getMemoMark())) {
												col = selectMemoResult.get(memoInddex).getMemoMark();
											}else {
												col = "";	
											}
										}else {
											col = "";
										}
									}else if(memoCnt == 2) {
										if( memoInddex < memoLength/2) {
												col = selectMemoResult.get(memoInddex==0?0:memoInddex*2).getMemoMark();		
										}else {
											col = "";
										}
									}
									break;
								case 5:
									if( memoInddex < memoLength/2) {
										col = selectMemoResult.get(memoInddex==0?1:memoInddex*2+1).getMemoMark();			
									}else {
										col = "";
									}
									break;
								}
							}else {
								switch(z) {
								case 0: col = sheetItem.getsBgName(); break;
								case 1: col = sheetItem.getItemCode(); break;
								case 2: col = sheetItem.getsItemContent()+" : "+sheetItem.getSitemMark(); break;
								//case 3: col = sheetItem.getSitemMark(); break;
								case 3:
									if(StringUtil.isNull(sheetItem.getMark())){ col = sheetItem.getSheetName();}
									else{col = sheetItem.getMark();}
									break;
								case 4: col = selectMemoResult.get(0).getMemoMark(); break;
								case 5: col = selectMemoResult.get(1).getMemoMark(); break;
								}
							}
							row[z] = col;
						}
						contents.add(row);
						break;
					case 3: //대 중 세부
						row = new String[arraySize];
						for(int z=0; z<arraySize; z++) {
							if(i != 0) {
								SheetInfo sheetBeforeItem = selectResult.get(i-1);
								switch(z) {
								case 0:
									if(sheetItem.getCategoryBCode().equals("2147483646")){ col = (sheetItem.getSheetCode()).substring(1); }
									else if(sheetItem.getCategoryBCode().equals(sheetBeforeItem.getCategoryBCode())) { col = ""; }
									else { col = sheetItem.getsBgName(); }
									break;
								case 1:
									if(StringUtil.isNull(sheetItem.getCategoryMCode())){
										if(StringUtil.isNull(sheetItem.getSheetName()) || sheetItem.getSheetName().equals("") ){ sheetItem.setSheetName("-"); }
										col = sheetItem.getSheetName();
									}else if(sheetItem.getCategoryMCode().equals(sheetBeforeItem.getCategoryMCode())) { col = "";}
									else { col = sheetItem.getsMgName();}
									break;
								case 2:
									if(StringUtil.isNull(sheetItem.getIcode())){ col = sheetItem.getSheetName(); }
									else if(sheetItem.getIcode().equals(sheetBeforeItem.getIcode())) { col = ""; }
									else { col = sheetItem.getItemCode(); }
									break;
								case 3:
									if(StringUtil.isNull(sheetItem.getsItemContent())){ col = sheetItem.getSheetName(); }
									else{ col = sheetItem.getsItemContent()+" : "+sheetItem.getSitemMark(); }
									break;
								/*case 4:
									if(StringUtil.isNull(sheetItem.getSitemMark())){ col = sheetItem.getSheetName(); }
									else{ col = sheetItem.getSitemMark(); }
									break;*/
								case 4:
									if(StringUtil.isNull(sheetItem.getCategoryMCode()) || StringUtil.isNull(sheetBeforeItem.getCategoryMCode())){ col = sheetItem.getSheetName(); }
									else{
										String code = sheetItem.getCategoryBCode().toString() + sheetItem.getCategoryMCode().toString() + sheetItem.getCategoryICode().toString();
										String beforeCode = sheetBeforeItem.getCategoryBCode().toString() + sheetBeforeItem.getCategoryMCode().toString() + sheetBeforeItem.getCategoryICode().toString();

										if(code.equals(beforeCode)) {
											col = "";
										}else {
											col = sheetItem.getMark();
										}
									}
									break;
								case 5:
									
									if(memoCnt == 1) {
										if( memoInddex < memoLength) {
											if(!StringUtil.isNull(selectMemoResult.get(i-1).getMemoMark())) {
												col = selectMemoResult.get(i-1).getMemoMark();
											}else {
												col = "";	
											}
										}else {
											col = "";
										}
									}else if(memoCnt == 2) {
										if( memoInddex < memoLength/2) {
												col = selectMemoResult.get(memoInddex==0?0:memoInddex*2).getMemoMark();		
										}else {
											col = "";
										}
									}
									break;
								case 6:
									if( memoInddex < memoLength/2) {
										col = selectMemoResult.get(memoInddex==0?1:memoInddex*2+1).getMemoMark();			
									}else {
										col = "";
									}
									break;
								}
							}else {
								switch(z) {
								case 0: col = sheetItem.getsBgName(); break;
								case 1: col = sheetItem.getsMgName(); break;
								case 2: col = sheetItem.getItemCode(); break;
								case 3: col = sheetItem.getsItemContent()+ " : "+sheetItem.getSitemMark(); break;
								//case 4: col = sheetItem.getSitemMark(); break;
								case 4:
									if(StringUtil.isNull(sheetItem.getMark())){ col = sheetItem.getSheetName();}
									else{col = sheetItem.getMark();}
									break;
								case 5: col = selectMemoResult.get(0).getMemoMark(); break;
								case 6: col = selectMemoResult.get(1).getMemoMark(); break;
								}
							}
							row[z] = col;
						}
						contents.add(row);
						break;
					case 4:
						row = new String[arraySize];
						for(int z=0; z<arraySize; z++) {
							if(i != 0) {
								SheetInfo sheetBeforeItem = selectResult.get(i-1);
								switch(z) {
								case 0:
									if(sheetItem.getCategoryBCode().equals("2147483646")){ col = (sheetItem.getSheetCode()).substring(1); }
									else if(sheetItem.getCategoryBCode().equals(sheetBeforeItem.getCategoryBCode())) { col = ""; }
									else { col = sheetItem.getsBgName(); }
									break;
								case 1:
									if(StringUtil.isNull(sheetItem.getCategoryMCode())){
										if(StringUtil.isNull(sheetItem.getSheetName()) || sheetItem.getSheetName().equals("") ){ sheetItem.setSheetName("-"); }
										col = sheetItem.getSheetName();
									}else if(sheetItem.getCategoryMCode().equals(sheetBeforeItem.getCategoryMCode())) { col = ""; }
									else { col = sheetItem.getsMgName(); }
									break;
								case 2:
									if(StringUtil.isNull(sheetItem.getCategorySCode())){ col = sheetItem.getSheetName(); }
									else if(sheetItem.getCategorySCode().equals(sheetBeforeItem.getCategorySCode())) { col = ""; }
									else { col = sheetItem.getsSgName(); }
									break;
								case 3:
									if(StringUtil.isNull(sheetItem.getIcode())){ col = sheetItem.getSheetName(); }
									else if(sheetItem.getIcode().equals(sheetBeforeItem.getIcode())) { col = ""; }
									else { col = sheetItem.getItemCode(); }
									break;
								case 4:
									if(StringUtil.isNull(sheetItem.getsItemContent())){ col = sheetItem.getSheetName(); }
									else{ col = sheetItem.getsItemContent()+" : "+sheetItem.getSitemMark(); }
									break;
								/*case 5:
									if(StringUtil.isNull(sheetItem.getSitemMark())){ col = sheetItem.getSheetName(); }
									else{ col = sheetItem.getSitemMark(); }
									break;*/
								case 5:
									if(StringUtil.isNull(sheetItem.getCategoryMCode()) || StringUtil.isNull(sheetBeforeItem.getCategoryMCode())){ col = sheetItem.getSheetName(); }
									else{
										String code = sheetItem.getCategoryBCode().toString() + sheetItem.getCategoryMCode().toString() + sheetItem.getCategorySCode().toString() + sheetItem.getCategoryICode().toString();
										String beforeCode = sheetBeforeItem.getCategoryBCode().toString() + sheetBeforeItem.getCategoryMCode().toString() + sheetBeforeItem.getCategorySCode().toString() + sheetBeforeItem.getCategoryICode().toString();

										if(code.equals(beforeCode)) {
											col = "";
										}else {
											col = sheetItem.getMark();
										}
									}
									break;
								case 6:
									
									if(memoCnt == 1) {
										if( memoInddex < memoLength) {
											if(!StringUtil.isNull(selectMemoResult.get(i-1).getMemoMark())) {
												col = selectMemoResult.get(i-1).getMemoMark();
											}else {
												col = "";	
											}
										}else {
											col = "";
										}
									}else if(memoCnt == 2) {
										if( memoInddex < memoLength/2) {
												col = selectMemoResult.get(memoInddex==0?0:memoInddex*2).getMemoMark();		
										}else {
											col = "";
										}
									}
									break;
								case 7:
									if( memoInddex < memoLength/2) {
										col = selectMemoResult.get(memoInddex==0?1:memoInddex*2+1).getMemoMark();			
									}else {
										col = "";
									}
									break;
								}
							}else {
								switch(z) {
								case 0: col = sheetItem.getsBgName(); break;
								case 1: col = sheetItem.getsMgName(); break;
								case 2: col = sheetItem.getsSgName(); break;
								case 3: col = sheetItem.getItemCode(); break;
								case 4: col = sheetItem.getsItemContent()+" : "+sheetItem.getSitemMark(); break;
								//case 5: col = sheetItem.getSitemMark(); break;
								case 5:
									if(StringUtil.isNull(sheetItem.getMark())){ col = sheetItem.getSheetName();}
									else{col = sheetItem.getMark();}
									break;
								case 6: col = selectMemoResult.get(0).getMemoMark(); break;
								case 7: col = selectMemoResult.get(1).getMemoMark(); break;
								}
							}
							row[z] = col;
						}
						contents.add(row);
						break;
					}

				}
				ModelMap.put("excelList", contents);
				ModelMap.put("evalDepth", evalDepth);
				ModelMap.put("target", (request.getParameter("fileName")));
			}else {

			}
		}else {

		}
		String realPath = request.getSession().getServletContext().getRealPath("/eval");
		ExcelView.createEvalResultXlsx(ModelMap, realPath, response);
	}
	
	
	/*20200305 김다빈추가*/
	@RequestMapping(value="/excelDownloadSheetAllResult.do")
	public void excelDownloadSheetAllResult(HttpServletRequest request, Map<String,Object> ModelMap, HttpServletResponse response, Locale local) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		LoginVO userInfo = SessionManager.getUserInfo(request);
		AJaxResVO jRes = new AJaxResVO();
		SheetInfo sheetInfo = new SheetInfo();
//		System.out.println("excel download start!!!");
		if(userInfo != null) {
			int rowsNum = 0;
			if(!StringUtil.isNull(request.getParameter("rowsNum"))) {
				rowsNum = Integer.parseInt(request.getParameter("rowsNum"));
			}
			

			String[] arrEvalCode = new String[rowsNum];
			String sheetCode = "";
			String fileName = "All_evaluation_result";
			//String[] arrFileName = new String[rowsNum];
			
			if(!StringUtil.isNull(request.getParameter("evalCode"))) {
				arrEvalCode = request.getParameter("evalCode").split("::");
			}
			if(!StringUtil.isNull(request.getParameter("sheetCode"))) {
				sheetCode = request.getParameter("sheetCode");
			}
			
			sheetInfo.setSheetCode(sheetCode);
			List<SheetInfo> selectItemNameList = evaluationService.selectItemNameList(sheetInfo);
			List<SheetInfo> selectSectItemList = evaluationService.selectSectItemList(sheetInfo);
			
			String sectItem = "";
			for (int i = 0 ; i < selectSectItemList.size(); i++ ) {
				sectItem += ",["+selectSectItemList.get(i).getSectItem()+"]";
			}
			sectItem = sectItem.substring(1);
			
			List<String[]> contents = new ArrayList<String[]>();
			String[] row = null;
			
			int itemNameSize = selectItemNameList.size();
			int arraySize = 17;
			int tempArrSize = 0;
			row = new String[arraySize+itemNameSize+1];
			String titleName="";
			//타이틀 정의
			for(int z=0; z<arraySize; z++) {
				switch(z) {
				case 0:	titleName = "no."; break;//순번
				case 1:	titleName = messageSource.getMessage("evaluation.result.placeholder.agentId", null,Locale.getDefault()); break;//user id
				case 2:	titleName = messageSource.getMessage("evaluation.result.placeholder.agentName", null,Locale.getDefault()); break;//user name
				case 3: titleName = messageSource.getMessage("evaluation.management.sheet.sheetname", null,Locale.getDefault()); break;//평가지명
				case 4:	titleName = messageSource.getMessage("evaluation.result.placeholder.evalDateStart", null,Locale.getDefault()); break;//평가날짜
				case 5:	titleName = messageSource.getMessage("evaluation.result.placeholder.recDateStart", null,Locale.getDefault()); break;//녹취날짜
				case 6:	titleName = messageSource.getMessage("views.search.grid.head.R_CALL_TTIME", null,Locale.getDefault()); break;//통화 시간
				case 7:	titleName = messageSource.getMessage("evaluation.result.placeholder.callNumber", null,Locale.getDefault()); break;//고객번호
				case 8: titleName = messageSource.getMessage("evaluation.js.alert.msg2", null,Locale.getDefault()); break;//내선번호
				case 9: titleName = messageSource.getMessage("evaluation.result.title.first.evaluator", null,Locale.getDefault()); break;//첫번째 평가자
				case 10: titleName = messageSource.getMessage("evaluation.result.title.first.evaluatorOp", null,Locale.getDefault()); break;//첫번째 평가자 의견
				case 11: titleName = messageSource.getMessage("evaluation.result.title.second.evaluator", null,Locale.getDefault()); break;//두번째 평가자
				case 12: titleName = messageSource.getMessage("evaluation.result.title.second.evaluatorOp", null,Locale.getDefault()); break;//두번째 평가자 의견
				case 13: titleName = messageSource.getMessage("evaluation.result.title.agentOp", null,Locale.getDefault()); break;//상담원 의견
				case 14: titleName = messageSource.getMessage("evaluation.result.placeholder.agreeStatus", null,Locale.getDefault()); break;//상태 동의
				case 15: titleName = messageSource.getMessage("evaluation.result.placeholder.rejectCount", null,Locale.getDefault()); break;//
				case 16: titleName = messageSource.getMessage("evaluation.result.placeholder.rejectDate", null,Locale.getDefault()); break;//		

				}
				row[tempArrSize] = titleName;
				tempArrSize++;
			}
			
			String pivotData = "";
			for (int z=0; z<itemNameSize; z++) {
				String itemName = selectItemNameList.get(z).getItemName();	
				row[tempArrSize] = itemName;
				tempArrSize++;
			}
			row[tempArrSize] = "Total"; // 총점
			contents.add(row);
			
			tempArrSize = 0; // 타이틀에서 사용 완료, 데이터 넣을때 재사용하기 위해 초기화
			
			for (int e = 0; e < arrEvalCode.length; e++) { 
				
				
				sheetInfo.setEvalCode(arrEvalCode[e]);
				sheetInfo.setSheetCode(sheetCode);
				sheetInfo.setSectItem(sectItem);
				
				sheetInfo.setTotalScoreString(messageSource.getMessage("evaluation.management.sheet.totalScoreString", null,Locale.getDefault()));
				sheetInfo.setEvaluatorOp(messageSource.getMessage("evaluation.management.sheet.evaluatorOp", null,Locale.getDefault()));
				sheetInfo.setCounselorOp(messageSource.getMessage("evaluation.management.sheet.counselorOp", null,Locale.getDefault()));
					
				List<SheetInfo> selectResult = evaluationService.excelDownSheetAllInfoResult(sheetInfo);
				List<SheetInfo> selectItemMarkList = evaluationService.selectItemMarkList(sheetInfo);
				List<SheetInfo> selectItemTotalAvgList = evaluationService.selectItemTotalAvgList(sheetInfo);
				
				if(selectResult.size()>0 && selectItemMarkList.size() > 0 && selectItemTotalAvgList.size() > 0) {
					String col = "";
					String userId = "";
					String userName = "";
					String total = "0";
					for(int i=0; i<selectResult.size(); i++) {
						row = new String[arraySize+itemNameSize+1];
						SheetInfo sheetItem = selectResult.get(i);
							if(i != 0) {
								for(int z=0; z<arraySize; z++) {
									SheetInfo sheetBeforeItem = selectResult.get(i-1);
									switch(z) {
									case 0:	// 순번
										col = (e+1) + "";
										if (StringUtil.isNull(sheetItem.getSheetCode()) || sheetItem.getSheetCode().equals("")) { col = ""; }
										break;
									case 1:	// user id
										if(StringUtil.isNull(sheetItem.getRecId()) || sheetItem.getRecId().equals("")){ col = "-"; }
										else { col = sheetItem.getRecId(); }
										break;
									case 2:	// user name
										if(StringUtil.isNull(sheetItem.getRecName()) || sheetItem.getRecName().equals("")){ col = "-"; }
										else { col = sheetItem.getRecName(); }
										break;
									case 3:	// 평가지명
										if(StringUtil.isNull(sheetItem.getSheetName()) || sheetItem.getSheetName().equals("") || sheetItem.getSheetName().equals(sheetBeforeItem.getSheetName())) { col = "-"; }
										else { col = sheetItem.getSheetName(); }
										break;
									case 4:	// 평가 날짜
										if(StringUtil.isNull(sheetItem.getEvalDate()) || sheetItem.getEvalDate().equals("") || sheetItem.getEvalDate().equals(sheetBeforeItem.getEvalDate())){ col = "-"; }
										else { col = sheetItem.getEvalDate(); }
										break;
									case 5:	// 녹취 날짜
										if(StringUtil.isNull(sheetItem.getRecDate()) || sheetItem.getRecDate().equals("") || sheetItem.getRecDate().equals(sheetBeforeItem.getRecDate())){ col = "-"; }
										else { col = sheetItem.getRecDate(); }
										break;
									case 6:	// 통화 시간
										if(StringUtil.isNull(sheetItem.getCallingTime()) || sheetItem.getCallingTime().equals("") || sheetItem.getCallingTime().equals(sheetBeforeItem.getCallingTime())){ col = "-"; }
										else { col = sheetItem.getCallingTime(); }
										break;
									case 7:	// 고객 번호
										if(StringUtil.isNull(sheetItem.getCustPhone()) || sheetItem.getCustPhone().equals("") || sheetItem.getCustPhone().equals(sheetBeforeItem.getCustPhone())){ col = "-"; }
										else { col = sheetItem.getCustPhone(); }
										break;
									case 8:	// 내선번호
										if(StringUtil.isNull(sheetItem.getCustPhone()) || sheetItem.getCustPhone().equals("") || sheetItem.getCustPhone().equals(sheetBeforeItem.getCustPhone())){ col = "-"; }
										else { col = sheetItem.getCustPhone(); }
										break;
										
									case 9:	// 첫번째 평가자
										if(sheetItem.getEvalatorName() == null || sheetItem.getEvalatorName().equals("") || sheetItem.getEvalatorName().equals(sheetBeforeItem.getEvalatorName())){ col = "-"; }
										else { col = sheetItem.getEvalatorName(); }
										break;
									case 10:	// 첫번째 평가자 의견
										if(sheetItem.getEvalatorFeedback() == null || sheetItem.getEvalatorFeedback().equals("") || sheetItem.getEvalatorFeedback().equals(sheetBeforeItem.getEvalatorFeedback())){ col = "-"; }
										else { col = sheetItem.getEvalatorFeedback(); }
										break;
									case 11:	// 두번째 평가자
										if(sheetItem.getEvalatorId2nd() == null || sheetItem.getEvalatorId2nd().equals("") || sheetItem.getEvalatorId2nd().equals(sheetBeforeItem.getEvalatorId2nd())){ col = "-"; }
										else { col = sheetItem.getEvalatorId2nd(); }
										break;
									case 12:	// 두번째 평가자 의견
										if(sheetItem.getEvalatorFeedback2nd() == null || sheetItem.getEvalatorFeedback2nd().equals("") || sheetItem.getEvalatorFeedback2nd().equals(sheetBeforeItem.getEvalatorFeedback2nd())){ col = "-"; }
										else { col = sheetItem.getEvalatorFeedback2nd(); }
										break;
									case 13:	// 상담원 의견
										if(sheetItem.getAgentFeedback() == null || sheetItem.getAgentFeedback().equals("") || sheetItem.getAgentFeedback().equals(sheetBeforeItem.getAgentFeedback())){ col = "-"; }
										else { col = sheetItem.getAgentFeedback(); }
										break;
										
										
									case 14: 
										if(sheetItem.getRejectStatus() == null || sheetItem.getRejectStatus().equals("") || sheetItem.getRejectStatus().equals(sheetBeforeItem.getRejectStatus())){ col = "-"; }
										else { col = sheetItem.getRejectStatus(); }
										break;
									case 15:
										if(sheetItem.getRejectCount() == null || sheetItem.getRejectCount().equals("") || sheetItem.getRejectCount().equals(sheetBeforeItem.getRejectCount())){ col = "-"; }
										else { col = sheetItem.getRejectCount(); }
										break;
									case 16:
										if(sheetItem.getRejectDate() == null || sheetItem.getRejectDate().equals("") || sheetItem.getRejectDate().equals(sheetBeforeItem.getRejectDate())){ col = "-"; }
										else { col = sheetItem.getRejectDate(); }
										break;
									}
									row[tempArrSize] = col;
									tempArrSize++;
								}
								if (sheetItem.getEvalTotalScore() != null || !sheetItem.getEvalTotalScore().equals("") ) { total = sheetItem.getEvalTotalScore(); }
								row[tempArrSize] = total; // 총점
							}else if (i == 0) {
								for(int z=0; z<arraySize; z++) {
									switch(z) {
										case 0: col = (e+1) + ""; break; // 순번
										case 1: col = sheetItem.getRecId(); userId=col; break; // user id
										case 2: if (sheetItem.getRecName() == null || sheetItem.getRecName().equals("")) { col = "-"; } else { col = sheetItem.getRecName(); userName=col; } break; // user name
										case 3: col = sheetItem.getSheetName(); break; //평가지명
										case 4:	col = sheetItem.getEvalDate(); break; // 평가 날짜
										case 5:	col = sheetItem.getRecDate(); break; // 녹취 날짜
										case 6:	col = sheetItem.getCallingTime(); break; // 통화 시간
										case 7:	if (sheetItem.getCustPhone() == null || sheetItem.getCustPhone().equals("") ) { col = "-"; } else { col = sheetItem.getCustPhone(); } break;  // 고객 번호
										case 8:	if (sheetItem.getExtNum() == null || sheetItem.getExtNum().equals("")) { col = "-"; } else { col = sheetItem.getExtNum(); } break;  // 내선 번호
										
	
										case 9:	// 첫번째 평가자
											if(sheetItem.getEvalatorName() == null || sheetItem.getEvalatorName().equals("")){ col = "-"; }
											else { col = sheetItem.getEvalatorName(); }
											break;
										case 10:	// 첫번째 평가자 의견
											if(sheetItem.getEvalatorFeedback() == null || sheetItem.getEvalatorFeedback().equals("")){ col = "-"; }
											else { col = sheetItem.getEvalatorFeedback(); }
											break;
										case 11:	// 두번째 평가자
											if(sheetItem.getEvalatorId2nd() == null || sheetItem.getEvalatorId2nd().equals("")){ col = "-"; }
											else { col = sheetItem.getEvalatorId2nd(); }
											break;
										case 12:	// 두번째 평가자 의견
											if(sheetItem.getEvalatorFeedback2nd() == null || sheetItem.getEvalatorFeedback2nd().equals("")){ col = "-"; }
											else { col = sheetItem.getEvalatorFeedback2nd(); }
											break;
										case 13:	// 상담원 의견
											if(sheetItem.getAgentFeedback() == null || sheetItem.getAgentFeedback().equals("")){ col = "-"; }
											else { col = sheetItem.getAgentFeedback(); }
											break;
										
										case 14: if (sheetItem.getRejectStatus() == null || sheetItem.getRejectStatus().equals("") ) { col = "-"; } else { col = sheetItem.getRejectStatus(); } break; // 상태 동의
										case 15: if (sheetItem.getRejectCount() == null || sheetItem.getRejectCount().equals("") ) { col = "-"; } else { col = sheetItem.getRejectCount(); } break; // 거부 횟수
										case 16: if (sheetItem.getRejectDate() == null || sheetItem.getRejectDate().equals("") ) { col = "-"; } else { col = sheetItem.getRejectDate(); } break;  // 거부 날짜
									}
									row[tempArrSize] = col;
									tempArrSize++;
								}
								String score = "-";
								for (int j=0; j<selectItemMarkList.size(); j++) {
									String markName = selectItemMarkList.get(j).getItemName();
									if (!score.equals("-")) break;
									for (int n=0; n<itemNameSize;n++) {
										String itemName = selectItemNameList.get(n).getItemName();
										if(markName.equals(itemName)) {
											score = selectItemMarkList.get(j).getMark();
											break;
										}
									}
									row[tempArrSize] = score;
									tempArrSize++;
									score="-";
								}
								if (sheetItem.getEvalTotalScore() != null ||
										!sheetItem.getEvalTotalScore().equals("") ) { total = sheetItem.getEvalTotalScore(); }
								row[tempArrSize] = total; // 총점
							} 
							contents.add(row);	
							tempArrSize = 0;
						}	
					
					
						row = new String[arraySize+itemNameSize+1];
						for (int temp = 0; temp <arraySize; temp++) {
							switch(temp) {
								case 0:	col = (e+1) + ""; break;							
								case 1: col = userId; break; // user id
								case 2: col = userName; break; // user id
								case 4: col = "Total"; break; // user id
								default : col = ""; break;
							}
							row[temp] = col;
						}						
						int tempNum = 0;
						for (int temp = arraySize; temp <arraySize+itemNameSize; temp++) {
							col = selectItemTotalAvgList.get(tempNum).getTotal();
							row[temp] = col;
							tempNum++;
						}
						row[arraySize+itemNameSize] = total;
						contents.add(row);	
						

						row = new String[arraySize+itemNameSize+1];
						for (int temp = 0; temp <arraySize; temp++) {
							switch(temp) {
								case 0:	col = (e+1) + ""; break;
								case 1: col = userId; break; // user id
								case 2: col = userName; break; // user id
								case 4: col = "AVG"; break; // user id
								default : col = ""; break;
							}
							row[temp] = col;
						}
						tempNum = 0;
						for (int temp = arraySize; temp <arraySize+itemNameSize; temp++) {
							col = selectItemTotalAvgList.get(tempNum).getAvg();
							row[temp] = col;
							tempNum++;
						}
						row[arraySize+itemNameSize] = total;
						contents.add(row);	
					}			
			}
			ModelMap.put("excelList", contents);
			ModelMap.put("resultSize", (arraySize+itemNameSize+1));
			ModelMap.put("target", fileName);					
		}else {

		}
		String realPath = request.getSession().getServletContext().getRealPath("/eval");
		ExcelView.createEvalResultAllXlsx(ModelMap, realPath, response);
	}

}
