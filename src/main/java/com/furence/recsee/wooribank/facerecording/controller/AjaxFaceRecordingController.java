package com.furence.recsee.wooribank.facerecording.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.LogInfoService;
import com.furence.recsee.common.util.DateUtil;
import com.furence.recsee.common.util.DirectoryUtil;
import com.furence.recsee.common.util.FileCopyUtil;
import com.furence.recsee.common.util.OsUtil;
import com.furence.recsee.common.util.SessionManager;
import com.furence.recsee.common.util.StringUtil;
import com.furence.recsee.common.util.TTSUtil;
import com.furence.recsee.main.model.RetryRecInfo;
import com.furence.recsee.main.model.SearchListInfo;
import com.furence.recsee.recording.dao.SttDao;
import com.furence.recsee.scriptRegistration.model.ScriptHistoryCreateParam;
import com.furence.recsee.scriptRegistration.model.scriptProductValueInfo;
import com.furence.recsee.wooribank.facerecording.dto.ScriptProductValueEltDto;
import com.furence.recsee.wooribank.facerecording.model.BkScriptParamVo;
import com.furence.recsee.wooribank.facerecording.model.FaceRecMoreProductCodeDto;
import com.furence.recsee.wooribank.facerecording.model.FaceRecProductInfo;
import com.furence.recsee.wooribank.facerecording.model.FaceRecScriptInfo;
import com.furence.recsee.wooribank.facerecording.model.FaceRecordingInfo;
import com.furence.recsee.wooribank.facerecording.model.ProductListDto;
import com.furence.recsee.wooribank.facerecording.model.ProductListSearchVo;
import com.furence.recsee.wooribank.facerecording.model.ProductListVo;
import com.furence.recsee.wooribank.facerecording.model.ProductPrdRisk;
import com.furence.recsee.wooribank.facerecording.model.RecParamHistoryVo;
import com.furence.recsee.wooribank.facerecording.model.RectryReasonVo;
import com.furence.recsee.wooribank.facerecording.model.RequestRetryRecReason;
import com.furence.recsee.wooribank.facerecording.model.RuserCode;
import com.furence.recsee.wooribank.facerecording.model.ScriptRegistrationInfo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepDto;
import com.furence.recsee.wooribank.facerecording.request.RequestScriptStepKeyParam;
import com.furence.recsee.wooribank.facerecording.response.ResponseScriptStepMoreProductPk;
import com.furence.recsee.wooribank.facerecording.service.FaceRecordingService;

@Controller
public class AjaxFaceRecordingController {

	private static final Logger logger = LoggerFactory.getLogger(AjaxFaceRecordingController.class);
	
	@Autowired
	private LogInfoService logInfoService;

	@Autowired
	private FaceRecordingService faceRecordingService;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Autowired
	private FileCopyUtil copyUtil;

	@Value("#{ttsProperties['path.windowHistoryPath']}")
	private String windowHistoryPath;

	@Value("#{ttsProperties['path.linuxHistoryPath']}")
	private String linuxHistoryPath;

	@Value("#{ttsProperties['path.windowRealTimeTTSPath']}")
	private String windowRealTTSPath;

	@Value("#{ttsProperties['path.linuxRealTimeTTSPath']}")
	private String linuxRealTTSPath;

	@Value("#{ttsProperties['server.ip']}")
	private String serverIp;

	@Value("#{ttsProperties['server.http']}")
	private String HTTP;

	@RequestMapping(value = "/getProductList.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO getProductList(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			FaceRecProductInfo faceRecProductInfo = new FaceRecProductInfo();
			List<FaceRecProductInfo> product = faceRecordingService.selectFaceRecProductList(faceRecProductInfo);

			if (product != null && product.size() > 0) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("product", product);
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
		} else {
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");

			logInfoService.writeLog(request, "Etc - Logout");
		}

		return jRes;
	}

	// rs_recfile에서 청취하는내용 불러오기
	@RequestMapping(value = "/selectRsRecFileRecCallKey.do", method = { RequestMethod.POST })
	public @ResponseBody AJaxResVO selectRsRecFileRecCallKey(HttpServletRequest request) {
		AJaxResVO jRes = new AJaxResVO();
		FaceRecordingInfo faceRecordingInfo = new FaceRecordingInfo();

		if (!StringUtil.isNull(request.getParameter("recCallKey"), true)) {
			faceRecordingInfo.setrRecKey(request.getParameter("recCallKey"));

			FaceRecordingInfo scriptDetailList = faceRecordingService.selectRsRecFileRecCallKey(faceRecordingInfo);

			if (scriptDetailList != null) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("111111222222", scriptDetailList);
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}

		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "no param");
		}
		logger.info(jRes.getResult());
		logger.info(jRes.getResData().toString());

		return jRes;

	}

	@RequestMapping(value = "/getScriptList.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO getScriptList(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			FaceRecScriptInfo faceRecScriptInfo = new FaceRecScriptInfo();

			if (!StringUtil.isNull(request.getParameter("selectScript"), true))
				faceRecScriptInfo.setSearchData(request.getParameter("selectScript"));
			if (!StringUtil.isNull(request.getParameter("selectCode"), true))
				faceRecScriptInfo.setScriptCode(request.getParameter("selectCode"));

			// 선택한 상품만 나오게
			faceRecScriptInfo.setProductCode(request.getParameter("productCode"));

			List<FaceRecScriptInfo> script = faceRecordingService.selectFaceRecScriptList(faceRecScriptInfo);
			if (script != null && script.size() > 0) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
				jRes.addAttribute("script", script);
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "no result");
			}
		} else {
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");

			logInfoService.writeLog(request, "Etc - Logout");
		}

		return jRes;
	}


	/*
	 * ============================================== Map to JSONObject
	 * ==============================================
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject convertMapToJson(Map<String, String> map) {
		JSONObject json = new JSONObject();

		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			json.put(key, value);
		}

		return json;
	}

	@RequestMapping(value = "/insertFaceRecProductInfo.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO insertFaceRecProductInfo(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		if (userInfo != null) {
			FaceRecProductInfo faceRecProductInfo = new FaceRecProductInfo();

			Date date = new Date();
			String dateStr = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
			faceRecProductInfo.setProductCode("A" + dateStr);

			if (!StringUtil.isNull(request.getParameter("productName"), true)) {
				faceRecProductInfo.setProductName(request.getParameter("productName"));
			}

			if (!StringUtil.isNull(request.getParameter("productType"), true))
				faceRecProductInfo.setProductType(request.getParameter("productType"));

			faceRecProductInfo.setProductUpdateUser(userInfo.getUserId());

			Integer result = faceRecordingService.insertFaceRecProductInfo(faceRecProductInfo);
			if (result > 0) {
				jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
			}
		} else {
			jRes.setResult("0");
			jRes.addAttribute("msg", "login fail");
		}

		return jRes;
	}

	public static String getOnlyDigit(String str) {

		StringBuffer sb = new StringBuffer();

		if (str != null && str.length() != 0) {
			Pattern p = Pattern.compile("^[ㄱ-ㅎ가-힣0-9]*$");

			Matcher m = p.matcher(str);

			while (m.find()) {

				sb.append(m.group());

			}

		}

		return sb.toString();

	}

	@RequestMapping(value = "/insertRetryRecReason.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO insertRetryRecReason(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		RetryRecInfo retryRecInfo = new RetryRecInfo();
		if (!StringUtil.isNull(request.getParameter("userId"), true)) {
			retryRecInfo.setUserId(request.getParameter("userId"));
		}
		if (!StringUtil.isNull(request.getParameter("callKeyAp"), true)) {
			retryRecInfo.setCallKeyAp(request.getParameter("callKeyAp"));
		}
		if (!StringUtil.isNull(request.getParameter("retryReason"), true)) {
			retryRecInfo.setRetryReason(request.getParameter("retryReason"));
		}
		if (!StringUtil.isNull(request.getParameter("retryReasonDetail"), true)) {
			retryRecInfo.setRetryReasonDetail(request.getParameter("retryReasonDetail"));
		} else {
			retryRecInfo.setRetryReasonDetail(request.getParameter(""));
		}

		Integer result = faceRecordingService.insertRetryRecReason(retryRecInfo);
		if (result > 0) {
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}

		return jRes;
	}
	@RequestMapping(value = "/insertAllRetryRecReason.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO insertAllRetryRecReason(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		
		
		RequestRetryRecReason reqInfo = new RequestRetryRecReason();
		if (!StringUtil.isNull(request.getParameter("userId"), true)) {
			reqInfo.setUserId(request.getParameter("userId"));
		}
		if (!StringUtil.isNull(request.getParameter("callKey"), true)) {
			reqInfo.setCallKey(request.getParameter("callKey"));
		}
		if (!StringUtil.isNull(request.getParameter("userId"), true)) {
			reqInfo.setUserId(request.getParameter("userId"));
		}
		if (!StringUtil.isNull(request.getParameter("callKeyApArr"), true)) {
			reqInfo.setCallKeyAp(request.getParameter("callKeyApArr"));
		}
		if (!StringUtil.isNull(request.getParameter("retryReason"), true)) {
			reqInfo.setRetryReason(request.getParameter("retryReason"));
		}
		if (!StringUtil.isNull(request.getParameter("retryReasonDetail"), true)) {
			reqInfo.setRetryReasonDetail(request.getParameter("retryReasonDetail"));
		} else {
			reqInfo.setRetryReasonDetail(request.getParameter(""));
		}
		
		reqInfo.MakescriptCallKeyArr();
		List<RectryReasonVo> reasonList = null;
		try {
			reasonList = faceRecordingService.selectSavedRetryReson(reqInfo);
			if(reasonList != null) {
				reasonList.forEach(vo->{
					logger.info(vo.toString());
					List<String> filterList = reqInfo.getScriptCallKeyArr().stream().filter(i->{
						if(vo.getRCallKeyAp().equals(i)) {
							return false;
						}
						return true;
					}).collect(Collectors.toList());
					filterList = Optional.of(filterList).orElse(Collections.EMPTY_LIST);
					if(!filterList.isEmpty()) {
						reqInfo.setScriptCallKeyArr(filterList);
					}
				});
			}
		}catch (Exception e) {
			logger.error("error", e);
		}
		
		
		int result = 0;
		if(reqInfo != null) {
			result = faceRecordingService.insertAllRetryRecReason(reqInfo);
		}
		
		if (result > 0) {
			try {
				faceRecordingService.clearRectryReasonToRecParamHistory(reqInfo.getCallKey());
			} catch (Exception e) {
				logger.error("[ {} ] : RecParamHistory Clear Failed",reqInfo.getCallKey());
			}
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		} else {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		
		return jRes;
	}
	@RequestMapping(value = "/grepRecParamHistoryJson.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO grepRecParamHistoryJson(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		jRes.setSuccess(AJaxResVO.SUCCESS_N);
		

		String callKey = null;
		if (!StringUtil.isNull(request.getParameter("callKey"), true)) {
			callKey = request.getParameter("callKey");
		}
		if(callKey == null) {
			return jRes;
		}
		RecParamHistoryVo paramVo = null;
		try {
			paramVo = faceRecordingService.grepRecParamHistory(callKey);
		}catch (Exception e) {
			return jRes;
		}
		if(paramVo != null) {
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("historyParam",paramVo);
		}
		
		return jRes;
	}
	@ResponseBody
	@RequestMapping(value = "/faceRecInsertParameter.do", method = { RequestMethod.POST, RequestMethod.GET })
	public AJaxResVO faceRecInsertParameter(HttpServletRequest request, @RequestParam Map<String, String> params)
			throws IOException {
		String callKey = null;
		AJaxResVO jRes = new AJaxResVO();
		if (request.getParameter("params") != null && !"".equals(params.get("params"))
				&& request.getParameter("callKey") != null && !"".equals(request.getParameter("callKey"))) {
			callKey = faceRecordingService.insertRecParam(request);
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}
		return jRes;
	}

	
	
	// product
	@ResponseBody
	@RequestMapping(value = "/faceRecSelectProductList.do", method = { RequestMethod.POST, RequestMethod.GET })
	public AJaxResVO faceRecSelectProductList(HttpServletRequest request, @RequestParam Map<String, String> params)
			throws IOException {

		logger.info("=========================/faceRecSelectProductList.do================================");
		AJaxResVO jRes = new AJaxResVO();
		BkScriptParamVo bkVo = null;
		String prd_risk = null;
		String callKey = null;
		boolean isaRedTextFlag = false;
		List<ScriptStepDto> stepDtoList = null;
 		HashMap<String, String> parameter = faceRecordingService
				.getRequestParamterChangeHashMap((String) request.getParameter("params"));
		FaceRecMoreProductCodeDto moreProductCodedto = null;

		jRes.setSuccess(AJaxResVO.SUCCESS_N);
		
		if(parameter.containsKey("RCD_KEY")) {
			callKey = parameter.get("RCD_KEY");
		}else {
			jRes.addAttribute("msg", "callKey is Null");
			return jRes;
		}
		/**
		 * 펀드(다계좌 체크)
		 */
		try {
			moreProductCodedto = faceRecordingService.setFaceRecMoreProductCode(parameter, callKey);
		} catch (ArrayIndexOutOfBoundsException e) {
			jRes.addAttribute("msg", e.getMessage());
			return jRes;
		}

		ScriptHistoryCreateParam param = faceRecordingService.setScriptHistoryCreateParam(parameter, callKey);

		// ISA일경우 빨간글씨로 바꾸기 위한 체크
		if (parameter.containsKey("SYS_DIS") && parameter.containsKey("BIZ_DIS")) {
			isaRedTextFlag = faceRecordingService.setIsaRedTextFlag(parameter, callKey);
			logger.info("isRedTextFlag : "+true);
		}
		
		// ELF 상품체크 js -> java
		boolean elfFlag = false;
		try {
			elfFlag = faceRecordingService.setElfFlag(parameter,callKey);
		} catch (Exception e2) {
			elfFlag = false;
		}
		
		//검색 클래스
		ProductListSearchVo searchVo = faceRecordingService.setProductListSearchVo(parameter,isaRedTextFlag);
		
		//양수도일시 rsUseYn체크안하는 플레그
		if(moreProductCodedto.getMoreProductType() == 2) {
			String type = Optional.ofNullable(parameter.get("BIZ_DIS")).orElse("");
			if(type.equals("2")) {
				searchVo.setTrFlag(false);
				jRes.addAttribute("trFlag", "Y");
			}
		}
		
		// 방카 파라미터 정렬후 객체 저장
		if (param.getrProductType().equals("4")) {
			if(parameter.containsKey("RCD_DSCD_NO")) {
				if( !parameter.get("RCD_DSCD_NO").equals("01") ) {
					EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
					etcConfigInfo.setGroupKey("FACE_RECORDING");
					List<EtcConfigInfo> result = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
					result = Optional.of(result).orElse(Collections.EMPTY_LIST);
					if(!result.isEmpty()) {
						//방카 실시간파라미터 vo 생성
						bkVo = faceRecordingService.setBkVoParam(parameter);
						searchVo.setrSearchWord(bkVo.getPrdCdNo());
						
						logger.info("[" + callKey + "] select Product type is : " + param.getrProductType());
			
						if (parameter.containsKey("PRD_CD_NO")) {
							param.addScriptDetailList(String.valueOf(parameter.get("PRD_CD_NO")));
						} else {
							jRes.addAttribute("msg", "방카 보종코드 실종");
							logger.error("["+callKey+"] 방카 보종코드 실종");
							return jRes;
						}
					}else {
						jRes.addAttribute("msg", "ETC config 정보누락");
						logger.error("["+callKey+"] ETC config 정보누락");
						return jRes;
					}
				}
			}else {
				jRes.addAttribute("msg", "방카 구분코드 실종");
				logger.error("["+callKey+"] 방카 구분코드 실종");
				return jRes;
			}
		}


		// searchVo에 ISA나 FND를 넣어주고 테이블에 해당하는 컬럼을넣어주면 null체크를해서 검색할수 있지???
 		List<ProductListVo> selectProductList = faceRecordingService.selectProductList(searchVo, callKey);
 		logger.info("["+callKey+"] productSearch to String : "+searchVo.toString());
		selectProductList = Optional.ofNullable(selectProductList).orElse(Collections.emptyList());

		//검색 상품 체크
		if (selectProductList.isEmpty() || selectProductList.size() != moreProductCodedto.getProductCodeList().size()) {
			jRes.addAttribute("msg", "상품정보를 찾을 수 없습니다. 상품타입"
					+ searchVo.getrProductType()+"상품코드 :" +searchVo.getrSearchWord()  +" sys : "+searchVo.getSysType());
			logger.error("상품정보를 찾을 수 없습니다. 상품타입"
					+ searchVo.getrProductType()+"상품코드 :" +searchVo.getrSearchWord()  +" sys : "+searchVo.getSysType());
			return jRes;
		}
		
		//다계좌 상품코드리스트 체크
		if (moreProductCodedto.getMoreProductType() == 1) {
			if (moreProductCodedto.getProductCodeList().size() != moreProductCodedto.getRegAmArr().length) {
				jRes.addAttribute("msg", "다계좌 상품코드중 없는 검색되지 않는 상품코드 발견");
				logger.error("다계좌 상품코드중 없는 검색되지 않는 상품코드 발견");
				return jRes;
			}
		}

		// 스크립트 히스토리에 콜키의 값이 존재하는지 확인
		Integer scriptHistoryCount = faceRecordingService.selectCountScriptHistroy(callKey);

		if (scriptHistoryCount > 0 || scriptHistoryCount==null) {
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("selectProductList", selectProductList);
			// 상품위험등급 표기를 위함
			try {
				if(moreProductCodedto.getMoreProductType() > 0) {
					jRes.addAttribute("riskInfo",faceRecordingService.moreFindPrdRisk(selectProductList,parameter));
				}else {
					jRes.addAttribute("riskInfo",faceRecordingService.findPrdRisk(selectProductList,parameter));
				}
				ProductListVo productListVo = selectProductList.get(0);
				if(productListVo.getrProductAttributes() != null && productListVo.getrProductType().equals("1")) {
					jRes.addAttribute("eltProduct", "Y");
				}
				
				
			} catch (Exception e) {
				logger.error("error",e);
			}
			// 양수도,다계좌일시 상품명 변경
			if (moreProductCodedto.getMoreProductType() > 0) {
				selectProductList.get(0).setrProductName(moreProductCodedto.getProductName());
			}
			return jRes;
		}
		
//		 * 양수도 , 다계좌 , 일반상품 체크후 step리스트 강제로 생성
		if (moreProductCodedto.getMoreProductType() > 0) {
			try {
				if(moreProductCodedto.getMoreProductType() > 0) {
					jRes.addAttribute("riskInfo",faceRecordingService.moreFindPrdRisk(selectProductList,parameter));
				}
			} catch (Exception e) {}
			logger.info("["+callKey+"] 양수도 or 다계좌 상품");
			for (ProductListVo vo : selectProductList) {
				logger.info(vo + "insert!!");
				moreProductCodedto.addProductList(vo);
			}
			try {
				if (moreProductCodedto.getMoreProductType() == 1) {
					logger.info("["+callKey+"] 다계좌상품");
					stepDtoList = faceRecordingService.selectMoreProductStep(moreProductCodedto);
					stepDtoList = faceRecordingService.modifyMoreProductStep(stepDtoList, moreProductCodedto);
					int result = faceRecordingService.insertMordeProductStep(stepDtoList, callKey);
				}
	
				if (moreProductCodedto.getMoreProductType() == 2) {
					String moreProductType = Optional.ofNullable(parameter.get("BIZ_DIS")).orElse("");
					if(moreProductType.equals("1")) {
						throw new Exception("신탁상품은 양수도 녹취를 진행할 수 없습니다.");
					}
					logger.info("["+callKey+"] 양수도상품");
					stepDtoList = faceRecordingService.selectMoreProductStep(moreProductCodedto);
					stepDtoList = faceRecordingService.modifyMoreDeathProductStep(stepDtoList, moreProductCodedto, callKey);
					int result = faceRecordingService.insertMordeProductStep(stepDtoList, callKey);
				}
			}catch (Exception e) {
				logger.error("e : "+e.getMessage());
				logger.error("e : "+"같은 그룹 상품이거나 같은 상품입니다.");
				return jRes;
			}
			
			stepDtoList = Optional.of(stepDtoList).orElse(Collections.EMPTY_LIST);
			if(stepDtoList.isEmpty()) {
				jRes.addAttribute("msg", "다계좌 스탭 생성오류");
				logger.error("["+callKey+"] 다계좌 스탭 생성오류");
				return jRes;
			}
		}

		for (int i = 0; i < selectProductList.size(); i++) {

			ProductListVo product = selectProductList.get(i);
			
			ProductListDto dto = new ProductListDto();
			ProductListSearchVo sVo = new ProductListSearchVo();

			try {
				sVo.copyProductListVo(product);
				dto.setProductListDto(product);
				
				faceRecordingService.elfAndEltCheck(dto,product);
				faceRecordingService.upInvestProductCheck(dto,product,parameter);
				dto.setrElfFlag(elfFlag);
			}catch (Exception e) {
				jRes.addAttribute("msg", e.getMessage());
				logger.error("["+callKey+"] e :"+e.getMessage());
				return jRes;
			}
			
			//그룹상품 PK 재지정
			try {
				Integer selectProductListGroup = 0;
				if(!searchVo.getTrFlag()) {
					selectProductListGroup = faceRecordingService.selectProductListGroupTrCase(product);
				}else {
					selectProductListGroup = faceRecordingService.selectProductListGroup(product);
				}
				if (selectProductListGroup != null) {
					dto.setrProductGroupPk(String.valueOf(selectProductListGroup));
					logger.info("["+callKey+"] group pk : "+selectProductListGroup);
				} else {
					dto.setrProductGroupPk(product.getrProductListPk());
				}
			}catch (Exception e) {
				dto.setrProductGroupPk(product.getrProductListPk());
			}
			
			jRes.addAttribute("productInfo",dto);
			
			// call Key 저장
			if(callKey != null) {
				dto.setCallKey(callKey);
			}else {
				jRes.addAttribute("msg", "콜키 누락");
				logger.error("콜키 누락");
				return jRes;
			}

			if (dto.getrProductType().equals("4")) {
				sVo.setrSearchWord(searchVo.getrSearchWord());
				dto.copyParamIfCaseList(param.getrScriptDetailList());
				if (parameter.containsKey("PRD_CD_NO")) {
					dto.setrProductBojungCode(String.valueOf(parameter.get("PRD_CD_NO")));
				}
			}

			//ELT & ELS 개인법인 대리인 조건
			try {
				JSONObject jsonObj = faceRecordingService.makeEltJsonParamCase(parameter,callKey);
				if(jsonObj !=null) {
					dto.setrScriptStepDetailCaseAttributes(jsonObj.toJSONString());
				}
			}catch (Exception e) {
				faceRecordingService.deleteScriptStepHistoryFromCallKey(callKey);
				logger.error("["+callKey+"] error : "+e.getMessage());
				jRes.addAttribute("msg", "error :"+e.getMessage());
				return jRes;
			}

			
			List<scriptProductValueInfo> valueList = faceRecordingService.selectProductValue2(sVo);
			//위험등급 구분
			ProductPrdRisk riskInfo = null;
			if(moreProductCodedto.getMoreProductType() == 0) {
				try {
					riskInfo = faceRecordingService.setPrdRiskFromValueList(valueList,parameter);
				} catch (Exception e1) {
					faceRecordingService.deleteScriptStepHistoryFromCallKey(callKey);
					jRes.addAttribute("msg", "PRD_RISK_GD 누락 상품명 : " + dto.getrProductName());
					return jRes;
				}
			}
			
			if(riskInfo != null) {
				jRes.addAttribute("riskInfo" , riskInfo);
			}
			
			//REMUN_RT_TYPE replace
			if(product.getrProductType().equals("2") || product.getrProductType().equals("5")) {
				try {
					valueList = faceRecordingService.replaceRemunRtTypeValue(valueList,callKey);
				}catch (Exception e) {
					faceRecordingService.deleteScriptStepHistoryFromCallKey(callKey);
					logger.error("[ {} ] remun change Error" ,callKey);
					jRes.addAttribute("msg", "remun error");
					return jRes;
				}
			}
			
			//개설일자구분
			try {
				String ad047 = faceRecordingService.setAd047FromValueList(valueList);
				if (ad047 != null)
					dto.addScriptDetailList(ad047);
			}catch (Exception e) {
				faceRecordingService.deleteScriptStepHistoryFromCallKey(callKey);
				jRes.addAttribute("msg", "AD047 가변값 누락 상품명 : " + dto.getrProductName());
				return jRes;
			}
			//원화조건 추가로직
			faceRecordingService.setCucdIfCase(valueList,dto);
			
			dto = faceRecordingService.setProductListDtoCreateParam(dto, parameter, callKey);

			// ELT / ELS / ELF 상품 구분 (구) & (신)
//			if (product.getrProductAttributes() != null) {
			if (dto.getrElfAndEltFlagValue() > 0) {
				jRes.addAttribute("eltProduct", "Y");
				ObjectMapper mapper = new ObjectMapper();
				Map<String, String> readValue = mapper.readValue(product.getrProductAttributes(), Map.class);
				dto.setrProductAttributes(product.getrProductAttributes());
				try {
					faceRecordingService.setEltRequestParameter(readValue, parameter, dto);
					faceRecordingService.checkEltKorea(readValue,dto);
					faceRecordingService.checkAgtpPrdRcm(parameter,dto);
				} catch (Exception e) {
					jRes.addAttribute("msg", "AD047 가변값 누락 상품명 : " + dto.getrProductName());
					logger.error("["+callKey+"] 방카 구분코드 실종 AD047 가변값 누락 상품명");
					return jRes;
				}
			}
				
			// ELF (조건 , 적합성)
			try {
				if(dto.getrElfAndEltFlagValue() == 2) {
					faceRecordingService.setelfCase(parameter,dto,callKey);
					faceRecordingService.checkAgtpPrdRcm(parameter,dto);
				}
			} catch (Exception e) {
				jRes.addAttribute("msg", "e :"+e.getMessage());
				logger.error("["+callKey+"] {}",e.getMessage());
				return jRes;
			}

			// EISA 조건 체크	(ISA_OP_TYPE)  && ISA가 아닐때 즉시매수 셋팅
			int isaOpType=0;
			try {
				isaOpType = faceRecordingService.checkIsaIfDetailCase(parameter, searchVo, dto);
			} catch (NotFoundException e) {
				jRes.addAttribute("msg", e.getMessage());
				logger.error("["+callKey+"] e : "+ e.getMessage());
				return jRes;
			}

			//CP 클래스 확인 및 CP일시 조건추가
			boolean cpClass = faceRecordingService.checkCpClassFromProductCode(sVo);
			if (cpClass) {
				dto = faceRecordingService.setCpClassParam(dto);
				logger.info("[" + callKey + "] is CpClass");
			}

			//다계좌 X & 양수도 X = 일반상품 일시 StepHistory Insert
			if(moreProductCodedto != null) {
				if (moreProductCodedto.getMoreProductType() == 0) {
					Integer insertScriptHistory = faceRecordingService.insertScriptStepHistory(dto);
				}
				//비정규 ELT 상품시 특정구간 체크후 수동녹취
				try {
					faceRecordingService.checkNonElfStepList(dto);
				}catch (Exception e) {
					faceRecordingService.deleteScriptStepHistoryFromCallKey(callKey);
					jRes.addAttribute("msg", "비정규형 ELT상품 기준가격평가 구간 누락");
					logger.error("["+callKey+"] e : 비정규형 ELT상품 기준가격평가 구간 누락,  상품명 : " + dto.getrProductName());
					return jRes;
				}
			}


			//step_detail_List
			List<ScriptStepDetailVo> detailList = null;
			try {
				//(ELS & ELS ) || 일반상품일시 분기처리
				if (dto.getrElfAndEltFlagValue() > 0) {
					detailList = dto.getrElfAndEltFlagValue() == 1 ?  
							faceRecordingService.selectEltScriptDetailToHistory(dto) : faceRecordingService.selectScriptDetailToHistory2(dto);
				} else {
					detailList = faceRecordingService.selectScriptDetailToHistory2(dto);
				}
			} catch (Exception e) {
				faceRecordingService.deleteScriptStepHistoryFromCallKey(callKey);
				jRes.addAttribute("msg", e.getMessage() + "상품명 : " + dto.getrProductName());
				logger.error("["+callKey+"] e : "+e.getMessage() + "상품명 : " + dto.getrProductName());
				return jRes;
			}

			//detailList 사이즈 체크
			detailList = Optional.of(detailList).orElse(Collections.EMPTY_LIST);
			if(detailList.isEmpty()) {
				jRes.addAttribute("msg", "상품디테일을 찾을 수 없습니다.");
				logger.error("["+callKey+"] e : " +"상품디테일을 찾을 수 없습니다.");
				return jRes;
			}
			
			if(dto.getrElfAndEltFlagValue() == 2) {
				try {
					detailList = faceRecordingService.setLizardDetailRsUseYn(detailList,product);
				}catch (Exception e) {
					logger.error("error",e);
				}
			}
			
			//ISA_OP_TYPE 에 관한 문구를 수정
			if(isaOpType > 0) {
				try {
					faceRecordingService.updateIsaOpTypeScript(detailList,isaOpType,callKey);
				} catch (Exception e) {
					faceRecordingService.deleteScriptStepHistoryFromCallKey(callKey);
					jRes.addAttribute("msg", e.getMessage() + "상품명 : " + dto.getrProductName());
					logger.error("["+callKey+"] e : "+e.getMessage() + "상품명 : " + dto.getrProductName());
					return jRes;
				}
			}
			
			
			//TISA + fund 붉은글씨 추가
			if (isaRedTextFlag) {
				EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
				etcConfigInfo.setGroupKey("SCRIPT");
				etcConfigInfo.setConfigKey("REDTEXT");
				List<EtcConfigInfo> result = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
				result = Optional.of(result).orElse(Collections.EMPTY_LIST);
				if(result.isEmpty()) {
					jRes.addAttribute("msg", "ETC config redText is null");
					logger.error("["+callKey+"] e : ETC config redText is null");
					return jRes;
				}
				String redText = null;

				if (result.size() > 0) {
					redText = "" + result.get(0).getConfigValue();
				}
				detailList = faceRecordingService.addScriptDetailRedText(detailList, redText, callKey);
			}

			//방카일시 스크립트 반복표출
			if (bkVo != null) {
				try {
					detailList = faceRecordingService.addBkScriptToDetailList(bkVo, detailList);
				} catch (Exception e) {
					faceRecordingService.deleteScriptStepHistoryFromCallKey(callKey);
					jRes.addAttribute("msg", e.getMessage());
					logger.error("["+callKey+"] e : "+e.getMessage());
					e.getStackTrace();
					return jRes;
				}
			}
			
			//고도화 투자성향분석일시 조건별 rs_use_yn 변경
			if( dto.isUpInvestProductYn() ) {
				try {
					detailList = faceRecordingService.modifyUpInvestProductDetailList(detailList,parameter);
				}catch (Exception e) {
					logger.error(e.getMessage());
					
				}
			}

			// 다계좌일시 상품 5,6 구간 반복표출
			if (moreProductCodedto.getMoreProductType() == 1) {
				faceRecordingService.setValueListToProductName(valueList, moreProductCodedto.getProductList());
				try {
					faceRecordingService.setMoreProductScriptDetail(stepDtoList, detailList, moreProductCodedto);
				} catch (CloneNotSupportedException e) {
					faceRecordingService.deleteScriptStepHistoryFromCallKey(callKey);
					logger.error("["+callKey+"] e : "+e.getMessage());
					jRes.addAttribute("msg", "상품검색 결과가 없습니다.");
					return jRes;
				} catch (NullPointerException e) {
					faceRecordingService.deleteScriptStepHistoryFromCallKey(callKey);
					logger.error("["+callKey+"] e : "+e.getMessage());
					jRes.addAttribute("msg", "상품검색 결과가 없습니다.");
					return jRes;
				}
			}

			// ELT & ELS || 일반 상품 분기별 가변값 format 처리
			List<ScriptProductValueEltDto> eltDto = null;
//			if (dto.getrProductAttributes() != null || elfFlag) {
			if( dto.getrElfAndEltFlagValue() > 0 ) {
				try {
					//ELF = parseValue 추가적으로 한번더해준다.
					if(dto.getrElfAndEltFlagValue() == 2) {
						valueList = TTSUtil.parseValue(valueList);
					}
					eltDto = faceRecordingService.modifyEltValueList(valueList, callKey, parameter);
				} catch (Exception e) {
					faceRecordingService.deleteScriptStepHistoryFromCallKey(callKey);
					logger.error("["+callKey+"] e : "+e.getMessage());
					return jRes;
				}
			}else {
				valueList = TTSUtil.parseValue(valueList);
			}
			
			//가변값 -> 텍스트 치환
			boolean resultValueListFromParam = faceRecordingService.setValueListFromParam(valueList, parameter,
					callKey);
			if (resultValueListFromParam) {
				logger.info("[" + callKey + "] value change Success");
			} else {
				logger.info("[" + callKey + "] value size = [0] change Failed");
			}
			
			
			//양수도 전용가입금액 치환
			if (moreProductCodedto.getMoreProductType() == 2) {
				String[] regAmArr = moreProductCodedto.getRegAmArr();
				String regAm = regAmArr[i];
				valueList.forEach(value -> {
					if (value.getRsProductValueCode().equals("REG_AM")) {
						value.setRsProductValueVal(regAm);
					}else if (value.getRsProductValueCode().equals("PRD_NM")) {
						value.setRsProductValueVal(product.getrProductName());
					}
				});
			}

			// (ELT & ELS & ELF) 가변값치환 및 일반상품가변값 치환
			// (ELT & ELS & ELF) 
//			if (dto.getrProductAttributes() != null || elfFlag) {
			if( dto.getrElfAndEltFlagValue() > 0 ) {
				// ELT & ELS 가변값 치환
				try {
					List<Integer> textIdxList = faceRecordingService.setDetailTextToVariableElt(detailList, eltDto);
				}catch (Exception e) {
					faceRecordingService.deleteScriptStepHistoryFromCallKey(callKey);
					jRes.addAttribute("msg", e.getMessage());
					logger.error("["+callKey+"] e : "+e.getMessage());
					return jRes;
				}
				
				try {
					// (ELT , ELS) 적합성 체크
					faceRecordingService.modifyEltComReptScript(detailList,parameter,callKey);
				}catch (Exception e) {
					logger.info(e.getMessage());
					faceRecordingService.deleteScriptStepHistoryFromCallKey(callKey);
					logger.error("["+callKey+"] e : "+e.getMessage());
					jRes.addAttribute("msg", "적합성파라미터 유실");
					return jRes;
					
				}
			} else {
				// 일반상품
				if (!detailList.isEmpty()) {
					valueList.forEach(value -> {
						if (value.getRsProductValueCode().equals("PRD_NM")) {
							value.setRsProductValueVal(product.getrProductName());
						}
					});
					List<Integer> textIdxList = faceRecordingService.setDetailTextToVariable(detailList, valueList);
				}
			}

			//step_detail_history 저장
			try {
				if(! searchVo.getTrFlag() && product.getrUseYn().equals("N")) {
					for (ScriptStepDetailVo vo : detailList) {
						vo.setrScriptDetailRealtimeTTS("Y");
					}
				}
				Integer history = faceRecordingService.insertScriptDetailHistory(detailList, callKey);
			} catch (Exception e) {
				logger.error("e :"+e.getMessage());
				jRes.addAttribute("msg", e.getMessage());
				
			}
			
			// step_history 에서 안에 detail없는것들은 use_yn = 'N'처리
//			if(moreProductCodedto.getMoreProductType() == 0 ) {
//				try {
//					faceRecordingService.updateStepSizeZeroUseYn(callKey);
//				}catch (Exception e) {
//					logger.info(e.getMessage());
//					faceRecordingService.deleteScriptStepHistoryFromCallKey(callKey);
//					logger.error("["+callKey+"] e : "+e.getMessage());
//					jRes.addAttribute("msg", "step update 실패");
//					return jRes;
//				}
//			}
			
		} // for...end

		try {
			faceRecordingService.updateStepSizeZeroUseYn(callKey);
		}catch (Exception e) {
			logger.info(e.getMessage());
			faceRecordingService.deleteScriptStepHistoryFromCallKey(callKey);
			logger.error("["+callKey+"] e : "+e.getMessage());
			jRes.addAttribute("msg", "step update 실패");
			return jRes;
		}
		
		// 양수도,다계좌일시 상품명 변경(2)
		if (moreProductCodedto.getMoreProductType() > 0) {
			selectProductList.get(0).setrProductName(moreProductCodedto.getProductName());
		}
		jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		jRes.addAttribute("selectProductList", selectProductList);
		return jRes;
	}

	// product
	@RequestMapping(value = "/faceRecSelectOfferProduct.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO faceRecSelectOfferProduct(HttpServletRequest request,
			@RequestParam Map<String, String> params) throws IOException {
		logger.info("=========================/faceRecSelectOfferProduct.do================================");
		AJaxResVO jRes = new AJaxResVO();
		ProductPrdRisk prd_risk = null;
		String callKey = null;
		boolean offerProductFlag = false;
		int pType = 0;

		List<scriptProductValueInfo> offerProductValueList = null;

		HashMap parameter = faceRecordingService
				.getRequestParamterChangeHashMap((String) request.getParameter("params"));

		if (request.getParameter("params") != null && !"".equals(params.get("params"))
				&& request.getParameter("callKey") != null && !"".equals(request.getParameter("callKey"))) {
			callKey = faceRecordingService.insertRecParam(request);
		}
		/**
		 * 상품 타입 저장
		 */
		if (parameter.containsKey("BIZ_DIS")) {
			String type = "" + parameter.get("BIZ_DIS");
			pType = Integer.parseInt(type);
		}

		/**
		 * 권유녹취 (현재는 방카) 에 대한 가변값 리스트를 가져온다.
		 */
		if (parameter.containsKey("RCD_DSCD_NO")) {
			if (("" + parameter.get("RCD_DSCD_NO")).equals("02")) {
				offerProductFlag = true;
			}
			if (("" + parameter.get("RCD_DSCD_NO")).equals("02") && ("" + parameter.get("BIZ_DIS")).equals("4")) {
				logger.info("===============권유녹취 대상상품==================");
				offerProductValueList = faceRecordingService.setOfferBkProductValueList(parameter, callKey);
			}
		}
		if (offerProductValueList == null || offerProductValueList.size() == 0) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "상품검색 결과가 없습니다.");
			return jRes;
		}

		ScriptHistoryCreateParam param = faceRecordingService.setScriptHistoryCreateParam(parameter, callKey);
		ProductListSearchVo searchVo = faceRecordingService.setProductListSearchVo(parameter,false);

		/**
		 * etcConfig에서 저장한 상품타입별 권유녹취 상품코드를 가져온다
		 */
		if (offerProductFlag) {
			EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SCRIPT");
			etcConfigInfo.setConfigKey("OFFER");
			List<EtcConfigInfo> result = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			if (result.size() > 0) {
				String bizDis = "" + parameter.get("BIZ_DIS");
				int parseInt = Integer.parseInt(bizDis) - 1;
				EtcConfigInfo etcConfigInfo2 = result.get(0);
				String configValue = etcConfigInfo2.getConfigValue();
				String[] split = configValue.split(",");
				searchVo.setrSearchWord(split[parseInt]);
			}

		}
		try {
			faceRecordingService.checkOfferDetailScript(pType, parameter);
		} catch (NotFoundException e) {
			logger.info(e.getMessage());
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", e.getMessage());
			return jRes;
		}

		// searchVo에 ISA나 FND를 넣어주고 테이블에 해당하는 컬럼을넣어주면 null체크를해서 검색할수 있지???
		List<ProductListVo> selectProductList = faceRecordingService.selectProductList(searchVo, callKey);

		// 스크립트 히스토리에 콜키의 값이 존재하는지 확인
		Integer scriptHistoryCount = faceRecordingService.selectCountScriptHistroy(callKey);

		if (scriptHistoryCount > 0) {
			logger.info("카운트가 있다.");
		} else {

			if (selectProductList != null && selectProductList.size() > 0) {
				for (int i = 0; i < selectProductList.size(); i++) {
					ProductListVo product = selectProductList.get(i);
					ProductListDto dto = new ProductListDto();

					dto.setProductListDto(product);

					logger.info("[" + callKey + "] select pk : " + product.getrProductListPk());

//						Integer selectProductListGroup = faceRecordingService.selectProductListGroup(Integer.parseInt(product.getrProductListPk()));
					Integer selectProductListGroup = faceRecordingService.selectProductListGroup(product);
					if (selectProductListGroup != null) {
						param.setrScriptStepFk(String.valueOf(selectProductListGroup));
						dto.setrProductGroupPk(String.valueOf(selectProductListGroup));
					} else {
						// 에러처리
					}
					// new data(2)
					param.setrScriptCallKey(callKey);
					dto.setCallKey(callKey);

					//대리인구분
					if (parameter.containsKey("AGNPE_NM")) {
						JSONObject jsonObj = new JSONObject();
						String agnpeNm = String.valueOf(parameter.get("AGNPE_NM"));
						if (agnpeNm.trim().equals("없음")) {
							jsonObj.put("isDeputy", false);
						} else {
							jsonObj.put("isDeputy", true);
						}
						dto.setrScriptStepDetailCaseAttributes(jsonObj.toJSONString());
					}
					
					//개설일자구분
					try {
						String ad047 = faceRecordingService.setAd047FromValueList(offerProductValueList);
						if (ad047 != null)
							dto.addScriptDetailList(ad047);
					}catch (Exception e) {
						faceRecordingService.deleteScriptStepHistoryFromCallKey(callKey);
						jRes.addAttribute("msg", "AD047 가변값 누락 상품명 : " + dto.getrProductName());
						return jRes;
					}

					// 해당 상품이 CP클래스인지 확인하는 조건문
					dto = faceRecordingService.setProductListDtoCreateParam(dto, parameter, callKey);

					//일반상품  , (ELS & ELT) 구분 상품스탭History insert
					Integer insertScriptHistory = faceRecordingService.insertScriptStepHistory(dto);
					if(insertScriptHistory < 1) {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", "상품 스탭 추가 실패");
						return jRes;
					}
					
					//디테일 검색
					List<ScriptStepDetailVo> detailList = faceRecordingService.selectScriptDetailToHistory2(dto);
					if (detailList.size() == 0 || detailList == null) {
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", "상품검색 결과가 없습니다.");
						return jRes;
					}
					try {
						detailList = faceRecordingService.makeOfferDetailScript(detailList, pType, parameter);
					} catch (NotFoundException e) {
						logger.info(e.getMessage());
						jRes.setSuccess(AJaxResVO.SUCCESS_N);
						jRes.addAttribute("msg", e.getMessage());
						return jRes;
					}
					
					// 넘어온 파라미터를 가변값리스트에 적용
					boolean resultValueListFromParam = faceRecordingService.setValueListFromParam(offerProductValueList,
							parameter, callKey);
					if (resultValueListFromParam) {
						logger.info("[" + callKey + "] value change Success");
					} else {
						logger.info("[" + callKey + "] value size = [0] change Failed");
					}

					// 방카 라인추가
					if (!detailList.isEmpty()) {
						List<Integer> textIdxList = faceRecordingService.setDetailTextToVariable(detailList,
								offerProductValueList);
					}
					try {
						Integer history = faceRecordingService.insertScriptDetailHistory(detailList, callKey);
					} catch (Exception e) {
						logger.error("e :"+e.getMessage());
						jRes.addAttribute("msg", e.getMessage());
					}
					
				} // for...end

			} else {
				// 실패시
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "상품검색 결과가 없습니다.");
			}

		}

		jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		jRes.addAttribute("selectProductList", selectProductList);
		return jRes;
	}

	/**
	 * @author 장진호
	 * @deprecated 대면녹취 상품이 없을시 해당 메소드를 통해 수동녹취화면을 표출
	 * 
	 */
	@RequestMapping(value = "/faceManualRecordingProductSelect.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO faceManualRecordingProductSelect(HttpServletRequest request,
			@RequestParam Map<String, String> params) throws Exception {
		AJaxResVO jRes = new AJaxResVO();
		String callKey = "";
		ProductListSearchVo searchVo = new ProductListSearchVo();

		searchVo.setrSearchType("03");

		/**
		 * 넘겨온 파라미터 저장
		 */
		HashMap<String,String> parameter = faceRecordingService
				.getRequestParamterChangeHashMap((String) request.getParameter("params"));
		FaceRecMoreProductCodeDto moreProductCodedto = null;

		
		
		/**
		 * 파라미터 콜키체크
		 */
		if (request.getParameter("params") != null && !"".equals(params.get("params"))
				&& request.getParameter("callKey") != null && !"".equals(request.getParameter("callKey"))) {
			callKey = faceRecordingService.insertRecParam(request);
		}

		if(parameter.containsKey("PRD_CD")) {
			searchVo.setrSearchWord(parameter.get("PRD_CD"));
		}
		
		if (parameter.containsKey("BIZ_DIS")) {
			searchVo.setrProductType("" + parameter.get("BIZ_DIS"));
		}
		
		if(parameter.containsKey("SYS_DIS")) {
			searchVo.setSysType(parameter.get("SYS_DIS"));
		}
		List<scriptProductValueInfo> valueList = new ArrayList<scriptProductValueInfo>();
		try {
			valueList = faceRecordingService.selectProductValue2(searchVo);
		}catch (Exception e) {
			logger.error("error",e);
		}
		valueList = Optional.of(valueList).orElse(Collections.EMPTY_LIST);
		
		ProductPrdRisk riskInfo = null;
		try {
			riskInfo = faceRecordingService.setPrdRiskFromValueList(valueList,parameter);
		} catch (Exception e1) {
			return jRes;
		}
		
		
		if(riskInfo != null) {
			jRes.addAttribute("riskInfo" , riskInfo);
		}
		
		/**
		 * [펀드 , 방카 , 퇴직연금] 수동녹취 , 신탁 수동녹취 구분
		 */
		EtcConfigInfo etcInfo = new EtcConfigInfo();
		etcInfo.setGroupKey("SCRIPT");
		etcInfo.setConfigKey("MANUAL");
		List<EtcConfigInfo> result = etcConfigInfoService.selectEtcConfigInfo(etcInfo);
		if (result.size() > 0) {
			searchVo.setrSearchWord(result.get(0).getConfigValue());
		}else {
			logger.info("수동녹취의 상품에 대한 스탭리스트의 조회결과가 없습니다.");
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "수동녹취의 상품에 대한 스탭리스트의 조회결과가 없습니다.");
			return jRes;
		}

		if (Integer.parseInt(searchVo.getrProductType()) > 1) {
			searchVo.setrProductType("3");
		} else {
			searchVo.setrProductType("1");
		}

		searchVo.setSysType(null);
		
		List<ProductListVo> selectProductList = faceRecordingService.selectProductList(searchVo, callKey);

		if (selectProductList == null && selectProductList.size() == 0) {
			logger.info("수동녹취 상품이 존재하지 않습니다.");
		}

		Integer scriptHistoryCount = faceRecordingService.selectCountScriptHistroy(callKey);

		if (scriptHistoryCount > 0) {
			logger.info("카운트가 있다.");
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("selectProductList", selectProductList);
			return jRes;
		}

		for (int i = 0; i < selectProductList.size(); i++) {
			ProductListVo productVo = selectProductList.get(i);
			ProductListDto dto = new ProductListDto();

			dto.setProductListDto(productVo);
			dto.setCallKey(callKey);

			Integer selectProductListGroup = faceRecordingService.selectProductListGroup(productVo);
			if (selectProductListGroup != null) {
				dto.setrProductGroupPk(String.valueOf(selectProductListGroup));
			} else {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "상품정보를 찾을 수 없습니다.");
				return jRes;
			}

			Integer insertScriptHistory = faceRecordingService.insertScriptStepHistory(dto);
			if (insertScriptHistory == 0 || insertScriptHistory == null) {
				logger.info("수동녹취의 상품에 대한 스탭리스트의 조회결과가 없습니다.");
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "수동녹취의 상품에 대한 스탭리스트의 조회결과가 없습니다.");
				break;
			}

			/**
			 * 상품별 스크립트 내용 조회
			 */
			List<ScriptStepDetailVo> detailList = null;
			try {
				detailList = faceRecordingService.selectScriptDetailToHistory2(dto);
			} catch (NullPointerException e) {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", e.getMessage() + "상품명 : " + dto.getrProductName());
				return jRes;
			}

			Integer history = faceRecordingService.insertScriptDetailHistory(detailList, callKey);

			if (history == null || history == 0) {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "수동녹취의 상품에 대한 스탭디테일 조회결과가 없습니다.");
				break;
			}

		}

		if (jRes.getSuccess().equals("Y")) {
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("selectProductList", selectProductList);
		}

		return jRes;
	}

	@RequestMapping(value = "/selectScriptDetailHistoryList.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO selectScriptDetailHistoryList(HttpServletRequest request,
			@RequestParam Map<String, String> params) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
		HashMap<String, String> hash = new HashMap<>();
		hash.put("params", request.getParameter("params"));
		HashMap<String,String> parameter = mapper.readValue(hash.get("params"), HashMap.class);
		String productCode = "";
		// fasle 재녹취(X) true 재녹취(O)
		boolean rectryFlag = false;
		String clickType = "";
		boolean moreProductFlag = false;
		com.furence.recsee.wooribank.facerecording.model.ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

		// 재녹취 플레그
		if (parameter.containsKey("retryType")) {
			rectryFlag = true;
		}
		scriptRegistrationInfo.setRectryFlag(rectryFlag);
		if (parameter.containsKey("PRD_CD")) {
			productCode = "" + parameter.get("PRD_CD");
			scriptRegistrationInfo.setrProductCode(request.getParameter("productCode"));
		}
		if (parameter.containsKey("PSN_YN")) {
			scriptRegistrationInfo.setrScriptDetailIfCase("" + parameter.get("PSN_YN"));
			logger.info("PSNYN : " + "" + parameter.get("PSN_YN"));
		}
		if (parameter.containsKey("MORE_PRODUCT")) {
			if (((String) parameter.get("MORE_PRODUCT")).equals("Y")) {
				moreProductFlag = true;
			} else {
				moreProductFlag = false;
			}
			logger.info("MORE_PRODUCT : " + "" + parameter.get("MORE_PRODUCT"));
		}
		// 추가끝
		// StepFk
		if (!StringUtil.isNull(request.getParameter("rScriptStepFk"), true))
			scriptRegistrationInfo.setrScriptStepFk(Integer.parseInt(request.getParameter("rScriptStepFk")));
		
		boolean nextStepMoreProductCheck = true;
		if (!StringUtil.isNull(request.getParameter("moreProductFlag"), true))
			nextStepMoreProductCheck = Boolean.parseBoolean(request.getParameter("moreProductFlag"));

		if (!StringUtil.isNull(request.getParameter("type"), true))
			clickType = "" + request.getParameter("type");

		if (!StringUtil.isNull(request.getParameter("callKey"), true))
			scriptRegistrationInfo.setrScriptCallKey(request.getParameter("callKey"));

		if(parameter.containsKey("BIZ_DIS")) {
			scriptRegistrationInfo.setrProductType(parameter.get("BIZ_DIS"));
		}
		
		if(parameter.containsKey("SYS_DIS")) {
			scriptRegistrationInfo.setrSysDisType(parameter.get("SYS_DIS"));
		}
		boolean eltFlag = false;
		if(parameter.containsKey("eltProduct")) {
			eltFlag = String.valueOf(parameter.get("eltProduct")).equals("Y") ? true : false;
		}
		
		scriptRegistrationInfo.setrScriptDetailComKind("Y");
		scriptRegistrationInfo.setrUseYn("Y");

		if (clickType.equals("F")) {
			Integer pk = scriptRegistrationInfo.getrScriptStepFk();
			List<Integer> pkList = faceRecordingService.selectOneScriptStepPk(pk);
			scriptRegistrationInfo.setrScriptStepFk(pkList.get(0));

		}
		ProductListVo product = null;

		// 다계좌
		try {
			if (moreProductFlag) {
				product = faceRecordingService.selectProductListPkfromProductCode2(scriptRegistrationInfo);
			} else {
				product = faceRecordingService.selectProductListPkfromProductCode(scriptRegistrationInfo);
				if(product == null) {
					scriptRegistrationInfo.setrSysDisType(null);
					product =  faceRecordingService.selectProductListPkfromProductCode(scriptRegistrationInfo);
				}
				
			}
		}catch (Exception e) {
			logger.error("["+parameter.get("RCD_KEY")+"] "+e.getMessage());
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("error", e.getMessage());
			return jRes;
		}

		List<ScriptRegistrationInfo> scriptDetailHistoryList = faceRecordingService
				.scriptDetailHistoryList(scriptRegistrationInfo);
		
		//다음스탭이 다계좌 상품제목일때 다계좌전용문구를 강제로 추가
		
		if (scriptDetailHistoryList.size() > 0) {
			if( !nextStepMoreProductCheck ) {
				scriptDetailHistoryList = faceRecordingService.addMoreProductDetailScript(scriptDetailHistoryList);
			}
			for (ScriptRegistrationInfo data : scriptDetailHistoryList) {
				String type = data.getrScriptDetailType();
				if (type.equals("T") || type.equals("R")) {
					try {
						data.setrScriptDetailText(data.getrScriptDetailText().replaceAll("\n","/n"));
					}catch (Exception e) {
						logger.error("text replace Error");
					}
					List<String> ttsTextList = TTSUtil.ttsTextList(data.getrScriptDetailText());
					try {
						data.setrScriptDetailText(data.getrScriptDetailText().replaceAll("/n","\n"));
					}catch (Exception e) {
						logger.error("text replace Error");
					}
					for (String textStr : ttsTextList) {
						try {
							textStr = textStr.replaceAll("/n","\n");
						}catch (Exception e) {
							logger.error("text replace Error");
						}
						data.getrScriptDetailTextList().add(textStr);
					}
				} else {
					data.getrScriptDetailTextList().add(data.getrScriptDetailText());
				}
			}
			// 가변 치환
			if (!clickType.equals("N") && !clickType.equals("F")) {

				String path = "";
				String os = OsUtil.getInstance().OsCheck();
				if (os.contains("win")) {
					path = this.windowRealTTSPath;
					System.out.println("path : " + path);
					DirectoryUtil.mkdirDirectory(new File(path));
				} else {
					path = this.linuxRealTTSPath;
					System.out.println("path : " + path);
					DirectoryUtil.mkdirDirectory(new File(path));
				}

				String callKey = scriptRegistrationInfo.getrScriptCallKey();
				path += callKey;
				path = TTSUtil.getrValidPath(path);
				logger.info("path+callKey : " + path);
				if (new File(path).exists()) {
				} else {
					DirectoryUtil.mkdirDirectory(new File(path));
				}
				String dateFormat = DateUtil.toString(new Date());
				// 실시간 TTS 유무 체크
				for (ScriptRegistrationInfo data : scriptDetailHistoryList) {
					Integer pk2 = data.getrScriptDetailPk();
					logger.info("detail pk : " + pk2);
					String type = data.getrScriptDetailType();
					logger.info("data type : " + type);
					if (type.equals("T") || type.equals("R") || type.equals("Z")) {
						// rScriptDetailRealtimeTTS
						String realtimeTTS = data.getrScriptDetailRealtimeTTS();
						if (realtimeTTS == null) {
							realtimeTTS = data.getRsScriptCommonRealTimeTts();
						}
						logger.info("realtimeTTS : " + realtimeTTS);
						if (realtimeTTS.equals("Y")) {
							String text = data.getrScriptDetailText();
							logger.info("realTimeTTS text : " + text);
							// 파라미터값 넣어줘야할 로직이 필요한데?
							StringBuffer buff = new StringBuffer();

							// 일단없으니 그냥 실행
							List<String> ttsTextList = TTSUtil.ttsTextList(text);
							// TTS파일생성
							for (int i = 1; i <= ttsTextList.size(); i++) {
								String fileName = dateFormat + "_" + callKey + "_" + pk2 + "_" + i;
								String string = ttsTextList.get(i - 1);
								string = TTSUtil.specialTextParse(string);
								string = TTSUtil.dotChangeUtil(string);
								string = TTSUtil.textOneChange(string);
								try {
									if(eltFlag) string = string.replaceAll("-", ",");

								}catch (Exception e) {
								}
								try {
									TTSUtil.createTTS2(string, fileName, path + "/");
								} catch (Exception e) {
									jRes.setSuccess(AJaxResVO.SUCCESS_N);
									jRes.addAttribute("error", e.getMessage());
									return jRes;
									// TTS에러 -> 알림창띄우게 해주기
								}
								logger.info("[ " + path + " ]" + fileName + " CREATE");
								fileName += ".wav";
								String resultPath = path + "/" + fileName;
								if (i == ttsTextList.size()) {
									buff.append(resultPath);
								} else {
									buff.append(resultPath + ",");
								}
								// path+=fileName;
							}
							data.setrScriptFilePath(buff.toString());
							// properties
							data.setrScriptTtsServerIp(serverIp);
							// 여긴 was VIP
						} else {
							Integer pk = data.getrScriptDetailPk();
							try {
								if (!productCode.equals(product.getrProductCode())) {
									StringBuffer buf = new StringBuffer();
									String filePath = data.getrScriptFilePath();
									try {
										String[] split = filePath.split(",");
										for (int i = 1; i <= split.length; i++) {
											String result = "";
											if (moreProductFlag) {
												result = split[i - 1] + "." + product.getrProductCode() + ".wav";
											} else {
												result = split[i - 1] + "." + productCode + ".wav";
											}
											if (i == split.length) {
												buf.append(result);
											} else {
												buf.append(result + ",");
											}
											data.setrScriptFilePath(buf.toString());
										}
									} catch (NullPointerException e) {
										scriptDetailHistoryList = null;
									}
								}
							}catch (Exception e) {
								logger.error("error", e);
							}
						}
					}
				}
			}
		} else {
		}
		if (scriptDetailHistoryList != null) {
			// 성공시
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
			jRes.addAttribute("scriptDetailHistoryList", scriptDetailHistoryList);
		} else {
			// 실패시
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "tts file path error");
		}
		return jRes;
	}

	@RequestMapping(value = "/updateScriptStepHistory.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO updateScriptStepHistory(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
//		  if (userInfo != null) {
		ScriptRegistrationInfo scriptRegistrationInfo = new ScriptRegistrationInfo();

		if (!StringUtil.isNull(request.getParameter("rScriptStepPk"), true)) {
			scriptRegistrationInfo.setrScriptStepPk(request.getParameter("rScriptStepPk"));
		}
		if (!StringUtil.isNull(request.getParameter("rScriptRecState"), true)) {
			scriptRegistrationInfo.setrScriptRecState(request.getParameter("rScriptRecState"));
		}
		if (!StringUtil.isNull(request.getParameter("scriptStepCallKey"), true)) {
			scriptRegistrationInfo.setrSccriptStepCallKey(request.getParameter("scriptStepCallKey"));
		}

		if (!StringUtil.isNull(request.getParameter("clearRecYn"), true)) {
			scriptRegistrationInfo.setClearRecYn(request.getParameter("clearRecYn"));
		}

		if (!StringUtil.isNull(request.getParameter("scriptCallKey"), true)) {
			scriptRegistrationInfo.setrScriptCallKey(request.getParameter("scriptCallKey"));
		}

		Integer result = faceRecordingService.updateScriptStepHistory(scriptRegistrationInfo);

		if (result > 0) {
			// 성공시
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		} else {
			// 실패시
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "no result");
		}
//		  } else { jRes.setResult("0");
//			  jRes.addAttribute("msg", "login fail");
//			  logInfoService.writeLog(request, "Etc - Logout"); 
//		  }

		return jRes;

	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addAllRectryParamHistory.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO addAllRectryParamHistory(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		LoginVO userInfo = SessionManager.getUserInfo(request);
//		  if (userInfo != null) {
		HashMap<String, String> hash = new  HashMap<String, String>();
		JSONObject jObj = new JSONObject();
		
		if (!StringUtil.isNull(request.getParameter("userId"), true)) 
			jObj.put("userId", request.getParameter("userId"));
		if (!StringUtil.isNull(request.getParameter("retryReason"), true)) 
			jObj.put("retryReason", request.getParameter("retryReason"));
		if (!StringUtil.isNull(request.getParameter("retryReasonDetail"), true)) 
			jObj.put("retryReasonDetail", request.getParameter("retryReasonDetail"));
		if (!StringUtil.isNull(request.getParameter("callKey"), true))
			hash.put("callKey", request.getParameter("callKey"));
		
		if(jObj.size() > 0) {
			hash.put("jsonData", jObj.toJSONString());
		}
		
		int result = 0;
		try {
			result = faceRecordingService.updateRecParamHistoryAllRecData(hash);
		} catch (Exception e) {
			logger.error("[ {} ] recParamHistory Update Error" ,request.getParameter("callKey") );
		}
		if (result > 0) {
			// 성공시
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		} else {
			// 실패시
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "no result");
		}

		return jRes;
	}
	
	@RequestMapping(value = "/selectParamHistory/{callKey}", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO selectParamHistoryFromCallKey(HttpServletRequest request ,@PathVariable(value = "callKey") String callKey ) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		RecParamHistoryVo info = null;
		try {
			info = faceRecordingService.selectParamHistoryFromCallKey(callKey);
			if(info!=null) {
				jRes.addAttribute("paramHistory",info);
			}
		}catch (Exception e) {
			logger.error("error",e);
		}
		return jRes;
	}
	@RequestMapping(value = "/clearStepRec/{callKey}", method = { RequestMethod.PUT})
	public @ResponseBody AJaxResVO clearStepRec(HttpServletRequest request ,@PathVariable(value = "callKey") String callKey ) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		jRes.setSuccess(jRes.SUCCESS_N);
		int result = 0;
		try {
			result = faceRecordingService.clearStepRec(callKey);
			if(result > 0) {
				jRes.setSuccess(jRes.SUCCESS_Y);
			}
		}catch (Exception e) {
		}
		return jRes;
	}
	@RequestMapping(value = "/getDeptData/{oprNo}", method = { RequestMethod.GET})
	public @ResponseBody AJaxResVO getDeptData(HttpServletRequest request ,@PathVariable(value = "oprNo") String oprNo ) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		jRes.setSuccess(jRes.SUCCESS_N);
		List<RuserCode> userCode = null;
		try {
			userCode = faceRecordingService.getDeptData(oprNo);
			if(!userCode.isEmpty()) {
				if(userCode.size() == 1) {
					jRes.addAttribute("userInfo",userCode.get(0));
					jRes.setSuccess(jRes.SUCCESS_Y);
				}else {
					jRes.setSuccess(jRes.SUCCESS_N);
				}
			}
		}catch (Exception e) {
		}
		return jRes;
	}
	
	@RequestMapping(value = "/checkEnableMoreProductStep.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO checkEnableMoreProductStep(HttpServletRequest request,@RequestBody RequestScriptStepKeyParam param) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		jRes.setSuccess(AJaxResVO.SUCCESS_N);
		
		List<String> paramList = Optional.of(param.getGridIdsArr()).orElse(Collections.emptyList());
		if(paramList.isEmpty()) {
			return jRes;
		}
		
		ResponseScriptStepMoreProductPk resMoreProductPk = null;
		try {
			resMoreProductPk =  faceRecordingService.selectScriptStepMoreProductPk(param);
		}catch (Exception e) {
			logger.error("e : {}",e.getMessage());
		}
		if(resMoreProductPk == null) {
			return jRes;
		}
			
		jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		jRes.addAttribute("result",resMoreProductPk);
		return jRes;
	}
	private String getPrdRiskGdNm(scriptProductValueInfo variableData) {
		String name = null;
		if (variableData.getRsProductValueVal().contains("1")) {
			name = "매우높은위험";
		}
		if (variableData.getRsProductValueVal().contains("2")) {
			name = "높은 위험";
		}
		if (variableData.getRsProductValueVal().contains("3")) {
			name = "다소 높은 위험";
		}
		if (variableData.getRsProductValueVal().contains("4")) {
			name = "보통위험";
		}
		if (variableData.getRsProductValueVal().contains("5")) {
			name = "낮은위험";
		}
		return name;
	}
	@Autowired
	private SqlSession sqlSession;
	@RequestMapping(value="/sendTaResult.do",method=RequestMethod.POST,produces="application/json; charset=utf8")
	@ResponseBody
	public AJaxResVO sendTaResult(HttpServletRequest hsr) {
		AJaxResVO jRes = new AJaxResVO();
		jRes.setSuccess(AJaxResVO.SUCCESS_N);
		SttDao sttdao = sqlSession.getMapper(SttDao.class);
		String callId = hsr.getParameter("callId");
		String taResult = hsr.getParameter("taResult");
		String reason = hsr.getParameter("reason");
		
		taResult = Optional.ofNullable(taResult).orElse("F");
		
		reason = Optional.ofNullable(reason).orElse("");
		
		try {
			sttdao.sendTaResult(callId,taResult,reason);
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		}catch (Exception e) {
			
		}
		return jRes;
	}
	
	@RequestMapping(value = "/insertEtc.do", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody AJaxResVO insertEtcConfig(HttpServletRequest request) throws IOException {
		AJaxResVO jRes = new AJaxResVO();
		SearchListInfo searchListInfo = new SearchListInfo();
//		
//		"groupKey" : gk,
//		"configKey" : ck,
//		"configValue" : cv
		String groupKey = null;
		groupKey = Optional.ofNullable(request.getParameter("groupKey")).orElse("");
		String configKey = null;
		configKey = Optional.ofNullable(request.getParameter("configKey")).orElse("");
		String configValue = null;
		configValue = Optional.ofNullable(request.getParameter("configValue")).orElse("");

		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey(groupKey);
		etcConfigInfo.setConfigKey(configKey);
		etcConfigInfo.setConfigValue(configValue);
		etcConfigInfo.setDesc("client-version");
		
		Integer insertEtcConfigInfo = etcConfigInfoService.insertEtcConfigInfo(etcConfigInfo);
		
		
		if(insertEtcConfigInfo > 0 ) {
			jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		} else if(insertEtcConfigInfo == 0) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
		}
		
		return jRes;
	}
}
