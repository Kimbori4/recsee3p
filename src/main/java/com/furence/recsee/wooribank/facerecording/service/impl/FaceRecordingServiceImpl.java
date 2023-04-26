package com.furence.recsee.wooribank.facerecording.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.util.TTSUtil;
import com.furence.recsee.main.model.RetryRecInfo;
import com.furence.recsee.scriptRegistration.model.ScriptHistoryCreateParam;
import com.furence.recsee.scriptRegistration.model.scriptProductValueInfo;
import com.furence.recsee.wooribank.facerecording.dao.FaceRecordingDao;
import com.furence.recsee.wooribank.facerecording.dto.ScriptProductValueEltDto;
import com.furence.recsee.wooribank.facerecording.model.BkScriptParamVo;
import com.furence.recsee.wooribank.facerecording.model.FaceRecMoreProductCodeDto;
import com.furence.recsee.wooribank.facerecording.model.FaceRecProductInfo;
import com.furence.recsee.wooribank.facerecording.model.FaceRecScriptInfo;
import com.furence.recsee.wooribank.facerecording.model.FaceRecordingInfo;
import com.furence.recsee.wooribank.facerecording.model.ParamKeyDefine;
import com.furence.recsee.wooribank.facerecording.model.ProductCharacterDetailVo;
import com.furence.recsee.wooribank.facerecording.model.ProductFundDetailInfo;
import com.furence.recsee.wooribank.facerecording.model.ProductListDto;
import com.furence.recsee.wooribank.facerecording.model.ProductListSearchVo;
import com.furence.recsee.wooribank.facerecording.model.ProductListVo;
import com.furence.recsee.wooribank.facerecording.model.ProductPrdRisk;
import com.furence.recsee.wooribank.facerecording.model.RecParamHistoryVo;
import com.furence.recsee.wooribank.facerecording.model.RecParamHistroyInfo;
import com.furence.recsee.wooribank.facerecording.model.RectryReasonVo;
import com.furence.recsee.wooribank.facerecording.model.RequestRetryRecReason;
import com.furence.recsee.wooribank.facerecording.model.RetryRecReasonVo;
import com.furence.recsee.wooribank.facerecording.model.RuserCode;
import com.furence.recsee.wooribank.facerecording.model.ScriptRegistrationInfo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepDto;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepListAndCallKey;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepVo;
import com.furence.recsee.wooribank.facerecording.request.RequestScriptStepKeyParam;
import com.furence.recsee.wooribank.facerecording.response.ResponseScriptStepMoreProductPk;
import com.furence.recsee.wooribank.facerecording.service.FaceRecordingService;
import com.furence.recsee.wooribank.facerecording.util.EltValueUtil;
import com.furence.recsee.wooribank.facerecording.util.IsaOpTypeMaker;
import com.furence.recsee.wooribank.facerecording.util.RemunRtTypeChange;
import com.sun.tools.sjavac.Log;
import com.furence.recsee.wooribank.script.repository.dao.ExcelUploadDao;

@Service("faceRecordingService")
@Qualifier("faceRecordingService")
public class FaceRecordingServiceImpl implements FaceRecordingService {
	private static final Logger logger = LoggerFactory.getLogger(FaceRecordingServiceImpl.class);

	@Autowired
	FaceRecordingDao faceRecordingMapper;
	
	@Autowired
	private ExcelUploadDao uploadMapper;
	
	
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;

	@Override
	public List<FaceRecordingInfo> selectFaceRecordingInfo(FaceRecordingInfo faceRecordingInfo) {
		return faceRecordingMapper.selectFaceRecordingInfo(faceRecordingInfo);
	}

	@Override
	public List<FaceRecProductInfo> selectFaceRecProductList(FaceRecProductInfo faceRecProductInfo) {
		return faceRecordingMapper.selectFaceRecProductList(faceRecProductInfo);
	}

	@Override
	public List<FaceRecScriptInfo> selectFaceRecScriptList(FaceRecScriptInfo faceRecScriptInfo) {
		return faceRecordingMapper.selectFaceRecScriptList(faceRecScriptInfo);
	}

	@Override
	public Integer insertFaceRecScriptInfo(FaceRecScriptInfo faceRecScriptInfo) {
		return faceRecordingMapper.insertFaceRecScriptInfo(faceRecScriptInfo);
	}

	@Override
	public Integer updateFaceRecScriptInfo(FaceRecScriptInfo faceRecScriptInfo) {
		return faceRecordingMapper.updateFaceRecScriptInfo(faceRecScriptInfo);
	}

	@Override
	public Integer insertFaceRecInfo(FaceRecordingInfo faceRecordingInfo) {
		return faceRecordingMapper.insertFaceRecInfo(faceRecordingInfo);
	}

	@Override
	public Integer insertFaceRecProductInfo(FaceRecProductInfo faceRecProductInfo) {
		return faceRecordingMapper.insertFaceRecProductInfo(faceRecProductInfo);
	}

	@Override
	public FaceRecordingInfo selectRsRecFileRecCallKey(FaceRecordingInfo faceRecordingInfo) {
		return faceRecordingMapper.selectRsRecFileRecCallKey(faceRecordingInfo);
	}

	@Override
	public Integer insertRetryRecReason(RetryRecInfo retryRecInfo) {
		return faceRecordingMapper.insertRetryRecReason(retryRecInfo);
	}

	
	
	//
	@Override
	public String insertRecParam(HttpServletRequest request) {
		String callKey = null;
		HashMap<String,String> temp = new HashMap<String, String>();
		temp.put("params", (String)request.getParameter("params"));
		temp.put("callKey", (String)request.getParameter("callKey"));
		callKey = request.getParameter("callKey");
		faceRecordingMapper.insertRecParam(temp);
		return callKey;
		
	}

	@Override
	public List<ProductListVo> selectProductList(ProductListSearchVo searchVo,String callKey) {
		List<ProductListVo> result =faceRecordingMapper.selectProductList(searchVo);		
		if(result.size()>0) {
			logger.info("["+callKey+"] select Size > 0 ");
		}
		return result;
	}

	@Override
	public Integer selectProductListGroup(ProductListVo vo) {
		return faceRecordingMapper.selectProductListGroup(vo);
	}

	@Override
	public Integer selectCountScriptHistroy(String callKey) {
		return faceRecordingMapper.selectCountScriptHistroy(callKey);
	}

	@Override
	public List<scriptProductValueInfo> selectProductValue2(ProductListSearchVo searchVo) {
//		throw new NullArgumentException("");
		return faceRecordingMapper.selectProductValue2(searchVo);
	}

	/**
	 * 일반상품  , (ELS & ELT) 구분 상품스탭History insert
	 * */
	@Override
	public Integer insertScriptStepHistory(ProductListDto dto) {
		int result = 0;
//		if(null == dto.getrProductAttributes()) {
		//일반상품
//		if(null == dto.getrProductAttributes() && ! dto.isrElfFlag() ) {
		if(dto.getrElfAndEltFlagValue() == 0) {
			result = faceRecordingMapper.insertScriptStepHistory(dto);
		}else {
		//ELT & ELS
			List<ScriptStepVo> voList = faceRecordingMapper.selectScriptStepElt(dto);
			if(voList.isEmpty() || voList == null) {
				throw new NotFoundException("ELT 상품 스탭을 찾을수 없습니다.");
			}
			//ELT ELF분기
			for (ScriptStepVo scriptStepVo : voList) {
				switch (dto.getrElfAndEltFlagValue()) {
				case 1:	//ELT
					//원화
					if(scriptStepVo.getrScriptStepParent()!=0 && scriptStepVo.getrScriptStepOrder() == 3) {
						if(dto.isKorean()) scriptStepVo.setrUseYn("N");
					}
					//적합성 보고서 유형
					if(scriptStepVo.getrScriptStepParent() == 0 && scriptStepVo.getrScriptStepOrder() == 4) {
						if(!dto.isAgtp()) scriptStepVo.setrUseYn("N");
					}
					
					break;
				case 2:	//ELF
					if(scriptStepVo.getrScriptStepParent() == 0 && scriptStepVo.getrScriptStepOrder() == 6) {
						if(!dto.isAgtp()) scriptStepVo.setrUseYn("N");
					}
					break;
				}
			}
			
			ScriptStepListAndCallKey sslAndCallKey = new ScriptStepListAndCallKey(voList, dto.getCallKey());
			result = faceRecordingMapper.insertScriptStepHistoryElt(sslAndCallKey);
			
		}
		
		
		return result;
	}

	@Override
	public List<ScriptStepDetailVo> selectScriptDetailToHistory2(ProductListDto dto) throws NullPointerException {
		List<ScriptStepDetailVo> selectScriptDetailToHistory2 = faceRecordingMapper.selectScriptDetailToHistory2(dto);
		if(selectScriptDetailToHistory2 == null || selectScriptDetailToHistory2.size() == 0) {
			throw new NullPointerException("상품내부의 스크립트를 찾을 수 없습니다.");
		}
		return selectScriptDetailToHistory2;
	}

	@Override
	public Integer insertScriptDetailHistory(List<ScriptStepDetailVo> detailList,String callKey) throws Exception {
		if(detailList.size()>0) {
			for (ScriptStepDetailVo data : detailList) {
				data.setrScriptCallKey(callKey);
				Integer history = faceRecordingMapper.insertScriptDetailHistory(data);
				if(history > 0) {
					logger.info("["+callKey+"]"+" insertDetailHistory PK : "+ data.getrScriptDetailPk());
				}else {
					logger.info("["+callKey+"]"+" failedDetailHistory PK : "+ data.getrScriptDetailPk());						
				}
			}
		}else {
			return 0;
		}
		return 1;
	}

	@Override
	public Integer insertScriptTTSHistory2(ScriptHistoryCreateParam param) {
		return faceRecordingMapper.insertScriptTTSHistory2(param);
	}

	@Override
	public List<Integer> selectOneScriptStepPk(Integer pk) {
		return faceRecordingMapper.selectOneScriptStepPk(pk);
	}

	@Override
	public ProductListVo selectProductListPkfromProductCode(ScriptRegistrationInfo scriptRegistrationInfo) {
		return faceRecordingMapper.selectProductListPkfromProductCode(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> scriptDetailHistoryList(ScriptRegistrationInfo scriptRegistrationInfo) {
		return faceRecordingMapper.scriptDetailHistoryList(scriptRegistrationInfo);
	}

	@Override
	public Integer updateScriptStepHistory(ScriptRegistrationInfo scriptRegistrationInfo) {
		return faceRecordingMapper.updateScriptStepHistory(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> selectScriptStepHistoryTree(ScriptRegistrationInfo scriptRegistrationInfo) {
		return faceRecordingMapper.selectScriptStepHistoryTree(scriptRegistrationInfo);
	}

	@Override
	public HashMap<String, String> getRequestParamterChangeHashMap(String parameter) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> param = null;  
		HashMap<String, String> hash = new HashMap<>();
		hash.put("params", parameter);
		param = mapper.readValue(hash.get("params"), HashMap.class);
				
		return param;
	}

	
	
	@Override
	public ScriptHistoryCreateParam setScriptHistoryCreateParam(HashMap parameter,String callKey) {
		ScriptHistoryCreateParam param = new ScriptHistoryCreateParam();
		if (parameter.containsKey("BIZ_DIS")) {
			logger.info("["+callKey+"] BIZ_DIS : "+parameter.get("BIZ_DIS"));
			param.setrProductType((String)parameter.get("BIZ_DIS"));
		}
		if (parameter.containsKey("CUCD")) {
			String cucd = "" + parameter.get("CUCD");
//			-------------------------------------
			if(cucd.equals("KRW"))
				param.addScriptDetailList("KRW");
			if(cucd.equals("USD"))
				param.addScriptDetailList("USD");
//			-------------------------------------			
			logger.info("["+callKey+"] CUCD : "+cucd);
		}
		if (parameter.containsKey("PRD_CD")) {
			String pCode = "" + parameter.get("PRD_CD");
			param.addScriptDetailList(pCode);
			param.setrProductCode(pCode);
			
			logger.info("["+callKey+"] PRD_CD : "+pCode);
		}
		if (parameter.containsKey("PSN_YN")) {
//			-------------------------------------			
			if((""+parameter.get("PSN_YN")).equals("Y")) 
				param.addScriptDetailList("개인");
			if((""+parameter.get("PSN_YN")).equals("N")) 
				param.addScriptDetailList("법인");				
//			-------------------------------------			
			logger.info("["+callKey+"] PSN_YN : "+parameter.get("PSN_YN"));
		}
		return param;
	}

	@Override
	public ProductListSearchVo setProductListSearchVo(HashMap parameter,boolean isaRedTextFlag) {
		ProductListSearchVo vo = new ProductListSearchVo();
		if (parameter.containsKey("PRD_CD")) {
			String pCode = "" + parameter.get("PRD_CD");
			vo.setrSearchWord(pCode);
		}
		
		if (parameter.containsKey("BIZ_DIS")) {
			vo.setrProductType(""+parameter.get("BIZ_DIS"));
		}
		
		//기본겁색 옵션
		vo.setrSearchType("03");
		
		if(parameter.containsKey("SYS_DIS")) {
			if(!isaRedTextFlag) {
				vo.setSysType(""+parameter.get("SYS_DIS"));
			}else {
				vo.setSysType("WMS");
			}
		}
		return vo;
	}

	@Override
	public ProductPrdRisk setPrdRiskFromValueList(List<scriptProductValueInfo> valueList, HashMap<String, String> parameter) throws Exception {
		
		for (scriptProductValueInfo variableData : valueList) {
			logger.info(variableData.getRsProductValueCode());
			if(variableData.getRsProductValueCode().equals("PRD_RISK_GD")) {
				return ProductPrdRisk.builder().productRiskNumer(variableData.getRsProductValueVal())
												.productRiskName(this.getPrdRiskGdNm(variableData.getRsProductValueVal()))
												.build();
			}
		}
		
		if(parameter.containsKey("PRD_RISK_GD")) {
			return ProductPrdRisk.builder().productRiskNumer(parameter.get("PRD_RISK_GD"))
										   .productRiskName(this.getPrdRiskGdNm(parameter.get("PRD_RISK_GD")))
										   .build();
		}
		return null;
	}
	
	private String getPrdRiskGdNm(String value) {
		String name = null;
		if(value.contains("1")) {
			name = "매우높은위험";
		}
		if(value.contains("2")) {
			name = "높은 위험";
		}
		if(value.contains("3")) {
			name = "다소 높은 위험";
		}
		if(value.contains("4")) {
			name = "보통 위험";
		}
		if(value.contains("5")) {
			name = "낮은 위험";
		}	
		if(value.contains("6")) {
			name = "매우 낮은 위험";
		}	
		return name;
	}

	@Override
	public String setAd047FromValueList(List<scriptProductValueInfo> valueList) throws Exception {
		String ad047 = null;
		for (scriptProductValueInfo variableData : valueList) {
			if("AD047".equals(variableData.getRsProductValueCode())) {
//				-----------------------------------------------------			
				if(TTSUtil.ad047Check(variableData.getRsProductValueVal()).equals("1"))
					ad047 = "3년 초과";	
				if(TTSUtil.ad047Check(variableData.getRsProductValueVal()).equals("0"))
					ad047 = "3년 이내";
//				-----------------------------------------------------			
				logger.info("AD047 : "+ad047);
			}
		}
		return ad047;
	}
	@Override
	public boolean checkCpClassFromProductCode(ProductListSearchVo searchVo) {
		boolean cpClassFlag = false;
		List<Integer> cpClassList = faceRecordingMapper.checkCpClassFromProductCode(searchVo);
		if(cpClassList.size()>0) {			
			int result = cpClassList.get(0);
			if(result==1) {
				cpClassFlag = true;
			}else {
				cpClassFlag = false;
			}
		}
		return cpClassFlag;		
	}

	@Override
	public ProductListDto setCpClassParam(ProductListDto dto) {
		dto.setCpClassFlag(1);
		for (int i = 0; i < dto.getrScriptDetailList().size(); i++) {
			String condition = dto.getrScriptDetailList().get(i);
			if(condition.equals("개인") || condition.equals("법인")) {
				dto.getrScriptDetailList().remove(i);
			}
		}
		return dto;
	}

	@Override
	public boolean setValueListFromParam(List<scriptProductValueInfo> valueList,HashMap parameter,String callKey) {
		if(valueList.size()>0) {
			for (scriptProductValueInfo variableData : valueList) {
				if(variableData.getRsProductValueCode().contains("PRD_RISK")) {
					continue;
				}
				boolean containsKey = parameter.containsKey(variableData.getRsProductValueCode());
				if (containsKey) {
					String value = (String) parameter.get("" + variableData.getRsProductValueCode());
					logger.info("["+callKey+"]"+"change value Data : "+ variableData.getRsProductValueCode());
					variableData.setRsProductValueVal(value);
				}
	
			}
			return true;
		}else {
			return false;
		}
	}

	@Override
	public List<Integer> setDetailTextToVariable(List<ScriptStepDetailVo> detailList,
			List<scriptProductValueInfo> valueList) {
		
		List<Integer> list = new ArrayList<Integer>();
		
		for (int i = 0; i < detailList.size(); i++) {
			ScriptStepDetailVo info = detailList.get(i);
			
			String text = info.getrScriptDetailText();
			
			try {
				text = text.replace("\n", "/n");
			}catch (Exception e) {
			}
			
			List<String> ttsTextList = null; 
			try {
				ttsTextList = TTSUtil.ttsTextList(text);
			}catch(Exception e) {
				throw new NullPointerException("text is null");
			}
			String resultText = "";
			for (String string : ttsTextList) {
				try {
					string = string.replaceAll("/n", "\n");
				}catch (Exception e) {
				}
				resultText += TTSUtil.randomRemove(string, valueList);
			}
			list.add(ttsTextList.size());
			detailList.get(i).setrScriptDetailText(resultText);
		}
		return list;
	}

	@Override
	public BkScriptParamVo setBkVoParam(HashMap parameter) {
		
		BkScriptParamVo vo = new  BkScriptParamVo();
		Field[] fields = vo.getClass().getDeclaredFields();
		
		for(int i=0; i< fields.length; i++) {
			String fieldName = fields[i].getName().toUpperCase();
			Set keySet = parameter.keySet();
			for (Object key : keySet) {
				try {
					String keyStr = String.valueOf(key).replaceAll("_", "");
					if(keyStr.equals(fieldName)) {
						Field field = vo.getClass().getDeclaredField(fields[i].getName());
						field.setAccessible(true);
						field.set(vo, parameter.get(key));
						logger.info("[key] : "+fieldName +"   [value] : "+parameter.get(key) );
					}
				}catch(Exception e) {
					logger.info(e.getMessage());
					logger.error("error", e);
					return null;
				}
			}
		}

		return vo;
	}

	@Override
	public List<ScriptStepDetailVo> addBkScriptToDetailList(BkScriptParamVo bkVo,
			List<ScriptStepDetailVo> detailList) throws Exception {
		
		//예외처리 ( 펀드 특징코드 )
		List<ProductFundDetailInfo> fundInfo  = faceRecordingMapper.selectBkDetailScript(bkVo);
		fundInfo = Optional.of(fundInfo).orElse(Collections.EMPTY_LIST);
		if(fundInfo.isEmpty()) {
			throw new NullPointerException("fundInfo is Size 0");
		}
		
		Queue<String> voQueue = new LinkedList<String>();

		List<String> fndList = Arrays.asList(bkVo.getFndCdList().split("\\|"));
		fndList = Optional.of(fndList).orElse(Collections.EMPTY_LIST);
		if(fndList.isEmpty() || fndList.size() != fundInfo.size()) {
			throw new NullPointerException("펀드특징코드 개수 : "+fndList.size() +" , 찾은 펀드 특징 코드 : "+fundInfo.size());
		}
		
		fndList.forEach(i-> voQueue.add(i));
		
		
		 //펀드 특징코드 삽입 구간
		StringBuffer buf = new StringBuffer();
		while(!voQueue.isEmpty()) {
			String fndCode = voQueue.poll();
			for (int i=0; i<fundInfo.size();i++) {
				ProductFundDetailInfo data = fundInfo.get(i);
				if(!fndCode.equals(data.getrProductFundDetailCode())) {
					continue;
				}
				String fundText = data.getrProductFundDetailText();
				String fundName = data.getrProductFundDetailName();
				String insuranceName = data.getrProductInsuranceName();
				
				buf.append(insuranceName+"의 "+fundName+"은(는) "+fundText);
				if(!voQueue.isEmpty()) {
					buf.append("\n\n");
				}
			}
			
		}
		
		
		
		String fundRateInfo = bkVo.getFndNmRateInfo();
		StringBuffer buf2 = new StringBuffer();
		
		if(fundRateInfo == null) {
			throw new NullPointerException("fund Rate is Null");
		}else {
			fundRateInfo+=" 입니다.";
		}
		buf2.append(fundRateInfo);

		this.setBkDetailListIfCase(detailList,buf,buf2);
		
		return detailList;
	}

	private void setBkDetailListIfCase(List<ScriptStepDetailVo> detailList,StringBuffer fndBuf,StringBuffer rateBuf) throws Exception {
		for (ScriptStepDetailVo detail : detailList) {
			if(detail.getrScriptStepDetailCaseAttributes() == null) {
				continue;
			}
			Map<String,Boolean> caseMapper = new ObjectMapper().readValue(detail.getrScriptStepDetailCaseAttributes(), Map.class);
			if(caseMapper.containsKey("isFundRate")){
				detail.setrScriptDetailText(rateBuf.toString());
			}
			if(caseMapper.containsKey("isFundDetail")){
				detail.setrScriptDetailText(fndBuf.toString());
			}
			/*
			if(detail.getrScriptDetailIfCase().equals("Y")) {
				if(detail.getrScriptDetailIfCaseDetail().equals("FND")) {
					detail.setrScriptDetailText(fndBuf.toString());
				}
				if(detail.getrScriptDetailIfCaseDetail().equals("RATE")) {
					detail.setrScriptDetailText(rateBuf.toString());
				}
			}
			 * */
		}
	}

	@Override
	public boolean setIsaRedTextFlag(HashMap parameter, String callKey) {
		String sysDis = ((String)parameter.get("SYS_DIS")).toUpperCase();
		String bisDIs = ((String)parameter.get("BIZ_DIS")).toUpperCase();
		
		logger.info("[ "+callKey+" ] SYS_DIS : "+sysDis);
		logger.info("[ "+callKey+" ] BIZ_DIS : "+bisDIs);
		
		boolean isaFlag = sysDis.equals("TISA");
		boolean fndFlag = bisDIs.equals("2");
		
		
		
		if(isaFlag && fndFlag) {
			logger.info("[ "+callKey+" ] setIsaRedTextFlag : true");
			return true;
		}
		logger.info("[ "+callKey+" ] setIsaRedTextFlag : false");
		return false;
	}

	@Override
	public boolean setAClassFlag(HashMap parameter, java.lang.String callKey) {
//		String bizDis = null;
//		String prdNm = null;
//		
//		if(parameter.containsKey("PRD_NM") && parameter.containsKey("BIZ_DIS")) {
//			bizDis = ((String)parameter.get("BIZ_DIS")).toUpperCase();
//			prdNm = ((String)parameter.get("PRD_NM")).toUpperCase().replace(" ",);
//		}else {
//			return false;
//		}
//		
//		int prdNmSize = prdNm.length();
//		
//		if(prdNm.contains("A")) {
//			return false;
//		}
//		
//		if(bizDis.equals("2") && prdNm.)
//		
//		
		return false;
	}

	@Override
	public List<ScriptStepDetailVo> addScriptDetailRedText(List<ScriptStepDetailVo> detailList, String redText , String callKey) {
		List<ScriptStepDetailVo> list = new ArrayList<ScriptStepDetailVo>();
		List<ScriptStepVo> stepVoList = faceRecordingMapper.selectScriptStepHistoryWhereRedTextStep(callKey);
		
		int i = 0;
		try {
			Iterator<ScriptStepDetailVo> iterator = detailList.iterator();
			boolean flag= false;
			while(iterator.hasNext()) {
				ScriptStepDetailVo next = iterator.next();
				String text = next.getrScriptDetailText().replace(" ", "");
				if(stepVoList.size() !=0 && stepVoList != null) {
					if(stepVoList.get(0).getrScriptStepPk() == Integer.parseInt(next.getrScriptStepFk())) {
						ScriptStepDetailVo vo = (ScriptStepDetailVo) next.clone();
						vo.setrScriptDetailPk(999999);
						vo.setrScriptDetailText(redText);
						vo.setrScriptDetailRealtimeTTS("Y");
						vo.setrScriptDetailType("T");
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("isTextRed", true);
						vo.setrScriptStepDetailCaseAttributes(jsonObject.toJSONString());
						vo.setrScriptDetailConfirmOrder(""+(Integer.parseInt(next.getrScriptDetailConfirmOrder())+20));
						if(!flag) {
							list.add(next);
							list.add(vo);
							flag = true;
						}else {
							list.add(next);
						}
						logger.info("[ "+callKey+" ] is A Class : true");
						continue;
					}else {
						list.add(next);
					}
				}
			}
		}catch(Exception e) {
			logger.error("error", e);
			return detailList;
		}
		return list;
	}

	@Override
	public void setBkDetailIfCaseDetail(ScriptHistoryCreateParam param, List<EtcConfigInfo> result) {
		for (EtcConfigInfo info : result) {
			param.addScriptDetailList(info.getConfigValue());
		}		
	}

	@Override
	public FaceRecMoreProductCodeDto setFaceRecMoreProductCode(HashMap parameter, java.lang.String callKey) {
		boolean prdCdFlag = false;
		FaceRecMoreProductCodeDto dto = new FaceRecMoreProductCodeDto();
		
		boolean moreFlag = false;
		boolean moreDeathFlag = false;
		
		
		if(parameter.containsKey("PRD_CD")) {
			prdCdFlag = true;
		}else {
			return dto;			//size = 0 check 꼭할것
		}
		String prdCd = (String)parameter.get("PRD_CD");
		dto.setPrdCd(prdCd);
		
		String[] splitPrdCd = this.splitPrdCd(prdCd);
		for (String code : splitPrdCd) {
			dto.addProductCodeList(code);
		}
		if(parameter.containsKey("REG_AM")) {
			String regAm = (String)parameter.get("REG_AM");
			String[] split = regAm.split("\\|");
			dto.setRegAmArr(split);
		}
		
		
		List<String> collect = dto.getProductCodeList().stream().distinct().collect(Collectors.toList());
		if(collect.size() != dto.getProductCodeList().size()) {
			throw new ArrayIndexOutOfBoundsException("같은 상품의 코드가 들어갔습니다.");
		}
		
		
		dto.setCallKey(callKey);
		/**
		 * 상품 타입이 펀드일 경우 + 다계좌 인지 판단
		 * */
		
		if(dto.getProductCodeList().size() > 1 && ((String)parameter.get("BIZ_DIS")).equals("2") ) {
			moreFlag = true;
		}
		if(parameter.containsKey("TR_AC_YN")) {
			if(((String)parameter.get("TR_AC_YN")).equals("Y")) {
				moreDeathFlag = true;
			}
		}
		
		if(moreFlag || moreDeathFlag) {
			if(!moreFlag && moreDeathFlag) { dto.setMoreProductType(2); dto.setProductName("양수도");}
			if(moreFlag && moreDeathFlag) { dto.setMoreProductType(2); dto.setProductName("양수도");}
			if(moreFlag && !moreDeathFlag) { dto.setMoreProductType(1); dto.setProductName("다계좌 신규 ("+splitPrdCd.length+"건)"); }
		}else {
			dto.setMoreProductType(0);
		}
		/**
		 * 다계좌 , 양수도 상품코드 제한 ☞ 수동녹취 전환
		 * */
		if(dto.getMoreProductType() == 2) {
			if(splitPrdCd.length > 10) {
				logger.info("양수도이지만 상품이 10개 넘어 수동녹취로 넘어갈예정");
				throw new ArrayIndexOutOfBoundsException("양수도의 검색상품 최대치(10)을 초과하였습니다.");
			}
		}
		if(dto.getMoreProductType() == 1) {
			if(splitPrdCd.length > 6) {
				logger.info("다계좌는 6개까지만가능하므로 수동녹취 예정");
				throw new ArrayIndexOutOfBoundsException("다계좌펀드의 검색상품 최대치(6)을 초과하였습니다.");
			}
		}
		
		return dto;
	}
	
	private String[] splitPrdCd(String prdCd) {
		String[] split = prdCd.split("\\|");
		if(split.length == 1) {
			return new String[]{prdCd};
		}
		return split;
	}

	@Override
	public ProductListDto setProductListDtoCreateParam(ProductListDto dto, HashMap parameter,
			java.lang.String callKey) {
		if (parameter.containsKey("CUCD")) {
			String cucd = "" + parameter.get("CUCD");
//			-------------------------------------
			if(cucd.equals("KRW"))
				dto.addScriptDetailList("KRW");
			if(cucd.equals("USD"))
				dto.addScriptDetailList("USD");
//			-------------------------------------			
			logger.info("["+callKey+"] CUCD : "+cucd);
		}
		
		dto.addScriptDetailList(dto.getrProductCode());
			
		if (parameter.containsKey("PSN_YN")) {
//			-------------------------------------			
			if((""+parameter.get("PSN_YN")).equals("Y")) 
				dto.addScriptDetailList("개인");
			if((""+parameter.get("PSN_YN")).equals("N")) 
				dto.addScriptDetailList("법인");				
//			-------------------------------------			
			logger.info("["+callKey+"] PSN_YN : "+parameter.get("PSN_YN"));
		}
		return dto;
	}

	@Override
	public List<ScriptStepDto> selectMoreProductStep(FaceRecMoreProductCodeDto dto) throws Exception {
		String productCode = dto.getPrdCd();
		List<ScriptStepDto> stepDto = faceRecordingMapper.selectMoreProductStep(dto);
		return stepDto;
	}

	@Override
	public List<ScriptStepDto> modifyMoreProductStep(List<ScriptStepDto> stepDtoList, FaceRecMoreProductCodeDto moreProductCodedto) throws Exception {
		if(!stepDtoList.isEmpty() && stepDtoList == null) {
			return null;
		}
		
		int idx = 2;
		
		String prdCd = moreProductCodedto.getPrdCd();
		String[] prdSplit = prdCd.split("\\|");
		
		List<ScriptStepDto> popStepDtoList = new ArrayList<ScriptStepDto>();
		HashMap<String, List<ScriptStepDto>> productScripStepListHash = new HashMap<String, List<ScriptStepDto>>();
		//첫,끝 추출
		ScriptStepDto first = stepDtoList.get(0);
		ScriptStepDto end = stepDtoList.get(stepDtoList.size()-1);
		
		
		List<ScriptStepDto> stepFive = stepDtoList.stream().filter(step -> {
			String[] odArr = step.gettOrder().split(",");
			
			int od = Integer.parseInt(odArr[0]);
			if(od == 5) {
				return true;
			}
			if(step.getDepth() > 1) {
				if(od == 5 && Integer.parseInt(odArr[1]) > 0) {
					return true;
				}
			}
			return false;
		}).filter(step -> {
			String getrProductCode = step.getrProductCode();
			if(first.getrProductCode().equals(getrProductCode)) {
				return true;
			}
			return false;
		}).collect(Collectors.toList());
		
		
		popStepDtoList.add(first);
		
		//순회 추출
		for (int r = 0; r < prdSplit.length; r++) {
			String productCode = prdSplit[r];
			List<ScriptStepDto> list = new ArrayList<ScriptStepDto>();
			
			List<ScriptStepDto> collect = stepDtoList.stream().filter(step -> {
				String[] odArr = step.gettOrder().split(",");
				if(!step.getrProductCode().equals(productCode)) {
					return false;
				}
				int od = Integer.parseInt(odArr[0]);
				if(od == 1 || od == 6 || od == 5) {
					return false;
				}
				if(step.getDepth() > 1) {
					if(od == 5 && Integer.parseInt(odArr[1]) > 0) {
						return false;
					}
				}
				return true;
			}).collect(Collectors.toList());
//			
//			for (int i = 0; i < stepDtoList.size(); i++) {
//				ScriptStepDto scriptStepDto = stepDtoList.get(i);
//				if(scriptStepDto.gettOrder().equals("1") || scriptStepDto.gettOrder().equals("6") || scriptStepDto.gettOrder().equals("5")) {
//					continue;
//				}
//				if(scriptStepDto.getrProductCode().equals(productCode)) {
//					list.add(scriptStepDto);
//					System.out.println(scriptStepDto.getrProductCode() + "---> [ "+scriptStepDto.getrScriptStepName() + " ]  is in list");
//				}
//			}
			productScripStepListHash.put(productCode, collect);
		}
			
		for (int i = 0; i < prdSplit.length; i++) {
			String productCode = prdSplit[i];
			List<ScriptStepDto> list = productScripStepListHash.get(productCode);
			list = this.modifyMoreScriptStepList(productCode, list, idx);
			popStepDtoList.addAll(list);
			idx++;
		}
		
		stepFive.forEach(step -> {
			if(prdSplit.length>3) {
				if(step.getDepth() == 1) {
					int od = Integer.parseInt(step.gettOrder());
					step.settOrder(""+(od+(prdSplit.length - 3)));
				}
			}
		});
		
		
		if(prdSplit.length>3) {
			int oder = Integer.parseInt(end.gettOrder());
			int resultOrder = oder+(prdSplit.length - 3);
			end.settOrder(""+resultOrder);
		}
		popStepDtoList.addAll(stepFive);
		popStepDtoList.add(end);
		for (ScriptStepDto scriptStepDto : popStepDtoList) {
			System.out.println(scriptStepDto);
		}		
		return popStepDtoList;
	}
	
	private List<ScriptStepDto>  modifyMoreScriptStepList(String productCode, List<ScriptStepDto> dtoList , int order) {
		ScriptStepDto dto = dtoList.get(0);
		ScriptStepDto dtoFirst = new ScriptStepDto();
		dtoFirst.setMoreProductStepFirst(dto.getrProductCode(), dto.getCallKey(), dto.getrProductName(), order);
		dtoList.add(0, dtoFirst);
		logger.info("create more Step first : "+dtoFirst.toString());
		for (int i = 0; i < dtoList.size(); i++) {
			ScriptStepDto stepdto = dtoList.get(i);
			if(stepdto.getrScriptStepParent() == 0) {
				stepdto.setrScriptStepParent(dtoFirst.gettKey());
			}
		}
			
		return dtoList;
	}

	@Override
	public int insertMordeProductStep(List<ScriptStepDto> stepDtoList, String callKey) {
		int result = faceRecordingMapper.insertMordeProductStep(stepDtoList);
		return result;
	}

	@Override
	public ProductListVo selectProductListPkfromProductCode2(ScriptRegistrationInfo scriptRegistrationInfo) {
		return faceRecordingMapper.selectProductListPkfromProductCode2(scriptRegistrationInfo);
	}

	@Override
	public List<java.lang.String> setOfferProductValueList(HashMap parameter, java.lang.String callKey) {
		List<String> list = new ArrayList<String>();
		if(parameter.containsKey("RECO_AFCO_CD_NO_1")) {
			list.add((String)parameter.get("RECO_AFCO_CD_NO_1"));
		}
		if(parameter.containsKey("RECO_AFCO_CD_NO_2")) {
			list.add((String)parameter.get("RECO_AFCO_CD_NO_2"));
		}
		if(parameter.containsKey("RECO_AFCO_CD_NO_3")) {
			list.add((String)parameter.get("RECO_AFCO_CD_NO_3"));
		}
		if(parameter.containsKey("RECO_AFCO_NM_1")) {
			list.add((String)parameter.get("RECO_AFCO_NM_1"));
		}
		if(parameter.containsKey("RECO_AFCO_NM_2")) {
			list.add((String)parameter.get("RECO_AFCO_NM_2"));
		}
		if(parameter.containsKey("RECO_AFCO_NM_3")) {
			list.add((String)parameter.get("RECO_AFCO_NM_3"));
		}
		if(parameter.containsKey("RECO_RPRS_PDCD_1")) {
			list.add((String)parameter.get("RECO_RPRS_PDCD_1"));
		}
		if(parameter.containsKey("RECO_RPRS_PDCD_2")) {
			list.add((String)parameter.get("RECO_RPRS_PDCD_2"));
		}
		if(parameter.containsKey("RECO_RPRS_PDCD_3")) {
			list.add((String)parameter.get("RECO_RPRS_PDCD_3"));
		}
		

		
		
		
		return null;
	}

	@Override
	public List<scriptProductValueInfo> setOfferBkProductValueList(HashMap parameter, java.lang.String callKey) {
		
		List<scriptProductValueInfo> valueInfoList = faceRecordingMapper.selectBkProductValueList(parameter);
		
		if(valueInfoList.size() == 0 || valueInfoList == null) {
			return null;
		}
		return valueInfoList;
	}

	@Override
	public List<ScriptStepDetailVo> makeOfferDetailScript(List<ScriptStepDetailVo> detailList, int pType,HashMap parameter) {
		switch (pType) {
		case 1:
			
			break;
			
		case 2:
			
			break;
			
		case 3:
			
			break;
			
		case 4:
			logger.info("방카 권유녹취 detail제작중.....");
			detailList = this.makeOfferDetailScriptBk(detailList,parameter,pType);
			break;
			
		case 5:
			
			break;
		}
		
		return detailList;
	}

	private List<ScriptStepDetailVo> makeOfferDetailScriptBk(List<ScriptStepDetailVo> detailList, HashMap parameter,int pType) {
		List<String> productCodeList = new ArrayList<String>();
		List<String> subCodeList = new ArrayList<String>();
		Queue<String> voQueue = new LinkedList<String>();
		
		Set keySet = parameter.keySet();
		Iterator iterator = keySet.iterator();
		while(iterator.hasNext()) {
			String next = ""+iterator.next();
			if(next.contains("RECO_RPRS_PDCD")) {
				String nextStr = String.valueOf(parameter.get(next));
				//map null체크
				if(nextStr != null) {
					//map length체크
					if(nextStr.trim().length()>0) {
						productCodeList.add(""+parameter.get(next));
						voQueue.add(""+parameter.get(next));
					}
				}else {
					throw new NotFoundException("권유상품 Null값");
				}
			}
			if(next.contains("RECO_AFCO_CD_NO")) {
				subCodeList.add(""+parameter.get(next));
			}
		}
		
		productCodeList = Optional.of(productCodeList).orElse(Collections.EMPTY_LIST);
		if(productCodeList.isEmpty()) {
			throw new NotFoundException("권유 상품번호 파라미터에서 누락");
		}
		
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("codeList", productCodeList);
		hash.put("subCodeList", subCodeList);
		hash.put("type", pType);
		
		List<ProductCharacterDetailVo> voList = faceRecordingMapper.selectOfferDetailScript(hash);
		voList = Optional.of(voList).orElse(Collections.emptyList());
		if(voList.isEmpty() || voList.size() != voQueue.size()) {
			throw new NotFoundException("권유 상품을 찾을수 없습니다.");
		}
		int od = 1;
		while (!voQueue.isEmpty()) {
			String codeName = voQueue.poll();
			for (int i = 0; i < detailList.size(); i++) {
				ScriptStepDetailVo scriptStepDetailVo = detailList.get(i);
				int order = Integer.parseInt(scriptStepDetailVo.getrScriptDetailConfirmOrder());
				int order2 = 0;
				if(i < detailList.size()-1) {
					ScriptStepDetailVo scriptStepDetailVoNext = detailList.get(i+1);
					order2 = Integer.parseInt(scriptStepDetailVoNext.getrScriptDetailConfirmOrder());
				}else {
					break;
				}
				if(order2 > 0) {
					if((order2 - order) > 3 ) {
						logger.info("찾앗따 요놈");
						List<ScriptStepDetailVo> addVoList = new ArrayList<ScriptStepDetailVo>();
						
						for (int j = 0; j < voList.size(); j++) {
							ProductCharacterDetailVo vo = voList.get(j);
							if(vo.getrProductCode().equals(codeName)) {
								try {
									ScriptStepDetailVo copyVo =(ScriptStepDetailVo)scriptStepDetailVo.clone();
									copyVo.setrScriptDetailConfirmOrder(""+(Integer.parseInt(copyVo.getrScriptDetailConfirmOrder())+(od)));
									copyVo.setrScriptDetailPk(1000000+(od));
									copyVo.setrScriptDetailText(TTSUtil.StringToNumberLine(od)+", "+vo.getrProductInsuranceName()+"의 "+vo.getrProductName()+" 입니다."+"\n"+vo.getrProductDetailText());
									addVoList.add(copyVo);
									od++;
								} catch (CloneNotSupportedException e) {
									logger.error("error",e);
								}
							}else {
								continue;
							}
						}
						detailList.addAll(i, addVoList);
						break;
					}
				}else {
					break;
				}
				
				
			}
		}
		
		
		
		/*
		 * 
		for (int i = 0; i < detailList.size(); i++) {
			ScriptStepDetailVo scriptStepDetailVo = detailList.get(i);
			int order = Integer.parseInt(scriptStepDetailVo.getrScriptDetailConfirmOrder());
			int order2 = 0;
			if(i < detailList.size()-1) {
				ScriptStepDetailVo scriptStepDetailVoNext = detailList.get(i+1);
				order2 = Integer.parseInt(scriptStepDetailVoNext.getrScriptDetailConfirmOrder());
			}else {
				break;
			}
			
			if(order2 > 0) {
				if((order2 - order) > 3 ) {
					System.out.println("찾앗따 요놈");
					List<ScriptStepDetailVo> addVoList = new ArrayList<ScriptStepDetailVo>();
					for (int j = 0; j < voList.size(); j++) {
						ProductCharacterDetailVo vo = voList.get(j);
						try {
							ScriptStepDetailVo copyVo =(ScriptStepDetailVo)scriptStepDetailVo.clone();
							copyVo.setrScriptDetailConfirmOrder(""+(Integer.parseInt(copyVo.getrScriptDetailConfirmOrder())+(j+1)));
							copyVo.setrScriptDetailPk(1000000+(j+1));
							copyVo.setrScriptDetailText(TTSUtil.StringToNumberLine(j+1)+", "+vo.getrProductInsuranceName()+"의 "+vo.getrProductName()+" 입니다."+"\n"+vo.getrProductDetailText());
							addVoList.add(copyVo);
						} catch (CloneNotSupportedException e) {
						}
					}
					detailList.addAll(i, addVoList);
					break;
				}
			}else {
				break;
			}
		}
		 * */
		return detailList;
	}

	@Override
	public void setValueListToProductName(List<scriptProductValueInfo> valueList, List<ProductListVo> productList) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < productList.size(); i++) {
			if(i == productList.size()-1) {
				buf.append(productList.get(i).getrProductName());
				break;
			}
			buf.append(productList.get(i).getrProductName()+"\n");
		}
		
		List<scriptProductValueInfo> filterList = valueList.stream().filter(value -> {
			boolean flag = value.getRsProductValueCode().equals("PRD_NM");
			if(flag) { logger.info(value.getRsProductValueCode() + " filter success");}
			return flag;
		}).collect(Collectors.toList());
		
		filterList.forEach(i -> i.setRsProductValueVal(buf.toString()));
	}

	@Override
	public List<ScriptStepDto> modifyMoreDeathProductStep(List<ScriptStepDto> stepDtoList,
			FaceRecMoreProductCodeDto moreProductCodedto,String callKey) {
		
		List<ProductListVo> productList = moreProductCodedto.getProductList();
		
		List<ScriptStepDto> newScriptStepList = new ArrayList<ScriptStepDto>();
		
		for (int i = 0; i < productList.size(); i++) {
			ProductListVo productListVo = productList.get(i);
			List<ScriptStepDto> collect = stepDtoList.stream().filter(step -> {
				String code = productListVo.getrProductCode();
				if(step.getrProductCode().equals(code)) {
					logger.info("code : "+code);
					return true;
				}else { return false; }
			}).collect(Collectors.toList());

			ScriptStepDto dto = new ScriptStepDto();
			dto.setMoreProductStepFirst(productListVo.getrProductCode(),callKey , productListVo.getrProductName(), i);
			
			collect.stream().forEach(step -> {
				if(step.getDepth() == 1 && step.getrScriptStepParent() == 0) {
					step.setrScriptStepParent(dto.gettKey());
				}
			});
			collect.add(0, dto);
			newScriptStepList.addAll(collect);
		}
		return newScriptStepList;
	}

	@Override
	public void setMoreProductScriptDetail(
			List<ScriptStepDto> stepDtoList,
			List<com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo> detailList,
			 FaceRecMoreProductCodeDto moreProductCodedto) throws CloneNotSupportedException {
		
		
		
		List<ProductListVo> productList = moreProductCodedto.getProductList();
		
		List<String> orderList = Arrays.asList(moreProductCodedto.getPrdCd().split("\\|"));
		
		orderList = Optional.of(orderList).orElse(Collections.EMPTY_LIST);
		if(orderList.isEmpty()) {
			throw new NotFoundException("다계좌 상품코드를 나눌수 없음");
		}
		List<ProductListVo> voList = new ArrayList<ProductListVo>();
		for(int i=0; i<orderList.size();i++) {
			String realCode = orderList.get(i);
			for (ProductListVo vo : productList) {
				String productCode = vo.getrProductCode();
				if(realCode.equals(productCode)) {
					voList.add(vo);
				}else {
					continue;
				}
			}
		}
		
		productList = voList;
		
		
		Optional<ScriptStepDto> findFirst = stepDtoList.stream().filter(step -> {
			String[] split = step.gettOrder().split(",");
			if(step.getDepth() == 2) {
				if(split[0].equals("5") && split[1].equals("1")) {
					return true;
				}
			}
			return false;
		}).findFirst();
		
		stepDtoList.forEach(System.out::println);
		
		int productListSize = productList.size();
		
		Optional<ScriptStepDto> stepSix = stepDtoList.stream().filter(step -> {
			String[] split = step.gettOrder().split(",");
			if(step.getDepth() == 1 && step.getrScriptStepParent() == 0 ) {
				if(productListSize>=4) {
					if(Integer.parseInt(split[0]) == 6 + (productListSize-3)) {
						return true;
					}
				}else {
					if(Integer.parseInt(split[0]) == 6) {
						return true;
					}
				}
			}
			return false;
		}).findFirst();
		
		
		if(findFirst.isPresent()) {
			ScriptStepDetailVo copyVo;
			boolean plusFlag = false;
			for (int j = 0; j < detailList.size(); j++) {
				ScriptStepDetailVo detailVo = detailList.get(j);
				if(Integer.parseInt(detailVo.getrScriptStepFk()) == findFirst.get().gettKey()) {
					if(plusFlag) {
						detailVo.setrScriptDetailConfirmOrder(""+(j+2));
						break;
					}else {
						detailVo.setrScriptDetailConfirmOrder(""+(j+2));
						copyVo = (com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo) detailVo.clone();
						copyVo.moreProductFiveStepEntity("지금부터 「투자자에게 설명해야 할 공통 설명사항」에 대해 설명드리겠습니다. 이는 고객님께서 가입하시는 상품에 공통적으로 적용되는 내용입니다.");
						detailList.add(j, copyVo);
						plusFlag = true;
					}
				}
			}
		}
		
		if(stepSix.isPresent()) {
			ScriptStepDetailVo copyS = null;
			ScriptStepDetailVo copyG = null;
			
			List<ScriptStepDetailVo> searchIdxList = new ArrayList<ScriptStepDetailVo>();
			for (int j = 0; j < detailList.size(); j++) {
				ScriptStepDetailVo detailVo = detailList.get(j);
				System.out.println("stepSix : " + stepSix.get().gettKey());
				if(Integer.parseInt(detailVo.getrScriptStepFk()) == stepSix.get().gettKey()) {
					if(detailVo.getrScriptDetailText().contains("{PRD_NM}") && detailVo.getrScriptDetailType().equals("S")) {
						searchIdxList.add(detailVo); 
						copyS = (com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo) detailVo.clone();
						System.out.println("복사 S : "+copyS.toString());
						
					}
					try {
						if(detailVo.getrScriptDetailType().equals("G") && detailList.get(j-1).getrScriptDetailText().contains("{PRD_NM}") && detailList.get(j-1).getrScriptDetailType().equals("S")) {
							searchIdxList.add(detailVo);
							copyG = (com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo) detailVo.clone();
							System.out.println("복사 G: "+copyG.toString());
						} 
					}catch(Exception e) {
						System.out.println("첫구간이라서 넘긴다");
					}
				}
			}
			if(searchIdxList.size() < 1) {
				return;
			}
			for(ScriptStepDetailVo vo : detailList) {
				if(Integer.parseInt(copyG.getrScriptStepFk()) == Integer.parseInt(vo.getrScriptStepFk())) {
					if(Integer.parseInt(vo.getrScriptDetailConfirmOrder()) > Integer.parseInt(copyG.getrScriptDetailConfirmOrder())) {
						int parseInt = Integer.parseInt(vo.getrScriptDetailConfirmOrder());
						int size = productList.size();
						int result = parseInt+(2*size);
						vo.setrScriptDetailConfirmOrder(""+result);
						
					}
				}
			}
//			for (int j = 0; j < searchIdxList.size(); j++) {
//				ScriptStepDetailVo detailVo = detailList.get(j);
//				int parseInt = Integer.parseInt(detailVo.getrScriptDetailConfirmOrder());
//				int size = productList.size();
//				int result = parseInt+(2*size);
//				System.out.println("삭제할 인덱스 : "+result);
//				detailVo.setrScriptDetailConfirmOrder(""+result);
//			}
			
			searchIdxList.forEach(System.out::println);
			Integer start = Integer.parseInt(searchIdxList.get(0).getrScriptDetailConfirmOrder());
			int startOrder = Integer.parseInt(copyS.getrScriptDetailConfirmOrder())-1;
							
			List<ScriptStepDetailVo> newEntityList = new ArrayList<ScriptStepDetailVo>();
			int dum = 99999980;
			for (int j = 0; j < productList.size(); j++) {
				
				int temp = start;
				String makeProductCntName = this.makeProductCntName(j+1);
				for(int k = 0; k < 2; k++ ) {
					String[] regAmArr = moreProductCodedto.getRegAmArr();
					if(regAmArr == null) {
						throw new NullPointerException("가입금액이 다계좌형식에 맞지 않습니다.");
					}
					startOrder++;
					dum++;
					if(k % 2 ==0) {
						ScriptStepDetailVo voS = (com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo) copyS.clone();
						voS.moreProductSixStepEntityS(dum,"고객님께서 가입하신 상품은 "+makeProductCntName+", "+productList.get(j).getrProductName()+"로서, 가입금액은 "+regAmArr[j]+"입니다. 맞습니까?", ""+startOrder);
						System.out.println("====================");
						System.out.println(voS.toString());
						System.out.println("====================");
						newEntityList.add(voS);
					}else {
						ScriptStepDetailVo voG = (com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo) copyG.clone();
						voG.moreProductSixStepEntityG(dum,"네 → 진행\r\n" + 
								"아니오 → 운용자산명 및 가입금액 재확인 또는 상품판매 중단", ""+startOrder);
						System.out.println("====================");
						System.out.println(voG.toString());
						System.out.println("====================");
						newEntityList.add(voG);
					}
				}
			}
			searchIdxList.forEach(i-> {
				System.out.println(i.toString());
				boolean remove = detailList.remove(i);
				System.out.println("remove check : " +remove);
			
				});
			
			
			detailList.addAll(start, newEntityList);
			System.out.println("======================================");
			newEntityList.forEach(System.out::println);
			System.out.println("======================================");
			
		}
		
//		detailList.forEach(System.out::println);
		detailList.stream().sorted(Comparator.comparing(ScriptStepDetailVo::getrScriptStepFk)).sorted(Comparator.comparing(ScriptStepDetailVo::getrScriptDetailConfirmOrder)).forEach(System.out::println);
		
	}
	
	
	private String makeProductCntName(int cnt) {
		String result = "";
		switch (cnt) {
		case 1:
			result = "첫번째";
			break;
		case 2:
			result = "두번째";
			break;
		case 3:
			result = "세번째";
			break;
		case 4:
			result = "네번째";
			break;
		case 5:
			result = "다섯번째";
			break;
		case 6:
			result = "여섯번째";
			break;
		}
		return result;
	}

	@Override
	public void checkOfferDetailScript(int pType, HashMap parameter) {
		List<String> productCodeList = new ArrayList<String>();
		List<String> subCodeList = new ArrayList<String>();
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("codeList", productCodeList);
		hash.put("subCodeList", subCodeList);
		hash.put("type", pType);
		
		Set keySet = parameter.keySet();
		Iterator iterator = keySet.iterator();
		while(iterator.hasNext()) {
			String next = ""+iterator.next();
			if(next.contains("RECO_RPRS_PDCD")) {
				if(String.valueOf(parameter.get(next)).trim().length() == 0) {
					continue;
				}
				productCodeList.add(""+parameter.get(next));
			}
			if(next.contains("RECO_AFCO_CD_NO")) {
				subCodeList.add(""+parameter.get(next));
			}
		}
		
		if(productCodeList.size() < 2) {
			throw new NotFoundException("권유녹취 상품이 한가지만 들어왔읍니다. (최소 2가지 이상)");
		}
		
		List<ProductCharacterDetailVo> voList = faceRecordingMapper.selectOfferDetailScript(hash);
		
		if(voList.size() != productCodeList.size()) {
			throw new NotFoundException("상품코드를 찾을 수 없습니다");
		}
		
		logger.info("상품결과검색완료!");
		
	}

	@Override
	public void checkRealTimeParameter(HashMap parameter, String callKey) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		int bizDis = 0;
		String sysDis = "";
		ParamKeyDefine paramClass = null;
		if(parameter.containsKey("BIZ_DIS")) {
			bizDis = Integer.parseInt(""+parameter.get("BIZ_DIS"));
		}
		if(parameter.containsKey("SYS_DIS")) {
			sysDis = String.valueOf(parameter.get("SYS_DIS"));
		}
		
		
		if(bizDis > 0) {
			if(bizDis ==ParamKeyDefine.shintak.type) {
				paramClass = new ParamKeyDefine.shintak();
			}
			if(bizDis ==ParamKeyDefine.Fund.type) {
				paramClass = new ParamKeyDefine.Fund();
			}
			if(bizDis ==ParamKeyDefine.Bk.type) {
				paramClass = new ParamKeyDefine.Bk();
			}
			if(bizDis ==ParamKeyDefine.sht.type) {
				paramClass = new ParamKeyDefine.sht();
			}
			if(sysDis.contains("ISA")) {
				paramClass = new ParamKeyDefine.Isa();
			}
		}
		
		
		
		Field[] fields = paramClass.getClass().getFields();
		
		for(int i=0; i< fields.length; i++) {
			boolean check = false;
			String fieldName = fields[i].getName().toUpperCase();
			Set keySet = parameter.keySet();
			for (Object key : keySet) {
				String keyStr = String.valueOf(key).toUpperCase();
				if(!fieldName.equals("TYPE")) {
					if(keyStr.equals(fieldName)) {
						check = true;
						System.out.println("[ "+fieldName + "] PARAM IS OK");
					}
				}else {
					check = true;
				}
			}
			if(!check) {
				throw new NoSuchFieldException("필수 파라미터가 없다.");
			}
		}
	}

	@Override
	public int checkIsaIfDetailCase(HashMap parameter, ProductListSearchVo searchVo, ProductListDto dto)
			throws NotFoundException {
		int result = 0;
		
		
		if(searchVo.getSysType() != null) {
			if(searchVo.getrProductType().equals("1") || searchVo.getrProductType().equals("2")) {
				if(searchVo.getSysType().equals("TISA") || parameter.get("SYS_DIS").equals("TISA")) {
					if(parameter.containsKey("ISA_OP_TYPE")){
						int isaType= Integer.parseInt(String.valueOf(parameter.get("ISA_OP_TYPE")));
						result = isaType;
						if(isaType == 2 || isaType == 3) {
							if(!parameter.containsKey("ISA_OP_TYPE")){
								throw new NotFoundException("ISA 운용비율 누락");
							}
						}
						if(isaType == 1) {
							dto.addScriptDetailList("즉시매수");
						}else if(isaType == 2) {
							dto.addScriptDetailList("예약매수");
						}else if(isaType == 3) {
							dto.addScriptDetailList("보유상품");
						}
					}else {
						throw new NotFoundException("ISA운용지시구분 값 누락");
					}
				}else {
					// ISA가 아닐때 기본 즉시매수 세팅
					dto.addScriptDetailList("즉시매수");
				}
			}
		}else {
			logger.info("ISA 구분이 아닌 상품");
		}
		
		return result;
		
	}

	@Override
	public void deleteScriptStepHistoryFromCallKey(String callKey) {
		faceRecordingMapper.deleteScriptStepHistoryFromCallKey(callKey);
	}

	@Override
	public void setEltRequestParameter(Map<String, String> readValue, HashMap<String, String> parameter,
			ProductListDto dto) {
		boolean psnFlag = false;
		boolean comReptTypeFlag = false;
		boolean agentFlag = false;

		JSONObject jObj = new JSONObject();
		
		Set<String> keySet = parameter.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String next = iterator.next();
			//개인법인
			if(next.equals("PSN_YN")) {
				psnFlag = true;
				if(parameter.get("PSN_YN").equals("Y")) {
					jObj.put("isPersonal", true);
				}else {
					jObj.put("isPersonal", false);
				}
			}
			//적합성보고 번호
			if(next.equals("AGTP_PRD_RCM_RSN") || next.equals("COM_REPT_TYPE")) {
				comReptTypeFlag = true;
				if(next.equals("COM_REPT_TYPE")) {
					jObj.put("investType", Integer.parseInt(parameter.get("COM_REPT_TYPE")));
//					dto.setrEltCase(Integer.parseInt(parameter.get("COM_REPT_TYPE")));
				}
				if(next.equals("AGTP_PRD_RCM_RSN")) {
					jObj.put("investType", Integer.parseInt(parameter.get("AGTP_PRD_RCM_RSN")));
//					dto.setrEltCase(Integer.parseInt(parameter.get("AGTP_PRD_RCM_RSN")));
				}
			}
			//대리인명
			if(next.equals("AGNPE_NM")) {
				agentFlag = true;
				if(parameter.get("AGNPE_NM").equals("없음") || parameter.get("AGNPE_NM").trim().length() == 0) {
					jObj.put("isAgent", false);
				}else {
					jObj.put("isAgent", true);
				}
			}
			
		}
		
		if(readValue.containsKey("isIsa")) {
			jObj.put("isIsa", readValue.get("isIsa"));
		}
		
		if(!psnFlag || !comReptTypeFlag || !agentFlag) {
			throw new NotFoundException("params has not detailCase");
		}
		//cust_type 체크
		this.eltheckCustType(parameter, jObj);
		
		dto.setrProductAttributesExt(jObj.toJSONString());
	}

	@Override
	public List<ScriptStepDetailVo> selectEltScriptDetailToHistory(ProductListDto dto) throws Exception {
		List<ScriptStepDetailVo> detailList = null;
		detailList = faceRecordingMapper.selectEltScriptDetailToHistory(dto);
		Optional<List<ScriptStepDetailVo>> of = Optional.of(detailList);
		if(of.isEmpty()) {
			throw new NotFoundException("step Detail has not data");
		}
		return detailList;
	}

	@Override
	public List<ScriptProductValueEltDto> modifyEltValueList(List<scriptProductValueInfo> valueList, String callKey,
			HashMap<String, String> parameter) throws Exception {
		
		List<ScriptProductValueEltDto> dtoList = new ArrayList<ScriptProductValueEltDto>();
		
		//이구간에서 공통가변값을 셋팅하는작업을 해야한다.
		List<scriptProductValueInfo> common = valueList.stream().filter(i->{
			if(i.getRsProductCode().contains("COMM")) {
				return true;
			}
			return false;
		}).collect(Collectors.toList());
		
		common.forEach(i->{
			if(parameter.containsKey(i.getRsProductValueCode())) {
				i.setRsProductValueVal(parameter.get(i.getRsProductValueCode()));
			}
			dtoList.add(new ScriptProductValueEltDto(i));
		});
		
		valueList.stream().filter(i->{
			if(i.getRsProductCode().contains("COMM")) {
				return false;
			}
			return true;
		}).forEach(i->{
			dtoList.add(new ScriptProductValueEltDto(i));
		});
		if(dtoList.isEmpty()) {
			return null;
		}
		
		
		
		
		for (ScriptProductValueEltDto eltDto : dtoList) {
			EltValueUtil.setEltProductValueValList(eltDto);
		}
		
		return dtoList;
	}

	@Override
	public List<Integer> setDetailTextToVariableElt(List<ScriptStepDetailVo> detailList,
			List<ScriptProductValueEltDto> eltDto) throws Exception {

		//빈값체크
		if(Optional.of(eltDto).orElse(Collections.EMPTY_LIST).isEmpty() || Optional.of(detailList).orElse(Collections.EMPTY_LIST).isEmpty()) {
			throw new NotFoundException("detail or eltDto is size 0");
		}
		
		for (ScriptStepDetailVo vo : detailList) {
			String text = vo.getrScriptDetailText();
			Integer eltType = vo.getrScriptDetailEltCase();
			text = EltValueUtil.changeEltValue(text, eltDto , eltType);
			if(text != null ) {
				vo.setrScriptDetailText(text);
			}
		}
		return null;
	}

	/**
	 * modifyEltComReptScript()
	 * ELS & ELT 적합성보고서유형 체크
	 * */
	@Override
	public void modifyEltComReptScript(List<ScriptStepDetailVo> detailList, HashMap<String, String> parameter,
			String callKey) {
		
		String sysDis = parameter.get("SYS_DIS");
		
		// true : ELS  /  false : ELT
//		boolean sysFlag = sysDis.contains("ISA") ? true : false;
		
		// ELS : AGTP_PRD_RCM_RSN 체크 불필요
		// ELT :  AGTP_PRD_RCM_RSN /  AGTP_PRD_RCM_ETC 체크
		
		
		// ELT 체크
			if(!parameter.containsKey("AGTP_PRD_RCM_RSN")) {
				throw new NotFoundException("AGTP_PRD_RCM_RSN not Fount Exception");
			}
		
		for(ScriptStepDetailVo vo :detailList) { 
			if(vo.getrProductAttributesExt() != null) {
				org.json.simple.parser.JSONParser parse = new org.json.simple.parser.JSONParser();
				JSONObject jObj = null;
				try {
					jObj = (JSONObject) parse.parse(vo.getrProductAttributesExt());
				} catch (org.json.simple.parser.ParseException e) {
					logger.info("json 파싱에러 [AGTP_PRD_RCM_ETC]");
				}
				
				if(jObj != null) {
					if(jObj.containsKey("investType")) {
						String v =	String.valueOf(jObj.get("investType"));
						if(v.equals("6")) {
							if(parameter.get("AGTP_PRD_RCM_ETC") == null) {
								throw new NotFoundException("AGTP_PRD_RCM_ETC : 유실");
							}
							vo.setrScriptDetailText(parameter.get("AGTP_PRD_RCM_ETC"));
						}
						vo.setrScriptDetailText(parameter.get("AGTP_PRD_RCM_ETC"));
					}
				}
				
			}
			
		}
		
		
		
		
	}

	/**
	 * ELT & ELS 원화단계 표출 유무 체크
	 * */
	@Override
	public void checkEltKorea(Map<String, String> readValue, ProductListDto dto) {
		if(!readValue.containsKey("isKorean")) {
			return;
		}
		if(readValue.get("isKorean") != null) {
			if(Boolean.parseBoolean(String.valueOf(readValue.get("isKorean")))) {
				dto.setKorean(true);
			}else {
				dto.setKorean(false);
			}
		}
	}

	
	/**
	 * 적합성보고서 유형 번호 체크
	 * */
	@Override
	public void checkAgtpPrdRcm(HashMap<String, String> parameter, ProductListDto dto) {
		String string = parameter.get("AGTP_PRD_RCM_RSN");
		if(Integer.parseInt(string) == 0) {
			dto.setAgtp(false);
		}else {
			dto.setAgtp(true);
		}
		
		
	}

	@Override
	public ProductPrdRisk findPrdRisk(List<ProductListVo> selectProductList, HashMap<String, String> parameter) throws Exception {
		ProductListVo productListVo = selectProductList.get(0);
		
		ProductListSearchVo sVo = new ProductListSearchVo();
		sVo.copyProductListVo(productListVo);
		
		List<scriptProductValueInfo> valueList = this.selectProductValue2(sVo);
		
		ProductPrdRisk riskInfo = this.setPrdRiskFromValueList(valueList,parameter);
		if(riskInfo == null) {
			throw new Exception("PRD_RISK_GD is null");
		}
		
		return riskInfo;
	}

	@Override
	public void selectShtProductManyBizDIs(ScriptHistoryCreateParam param, String callKey,
			HashMap<String, String> parameter) {
		
		List<ProductListVo> productVoList = faceRecordingMapper.selectProductListFromGroupCode(param);
		productVoList = Optional.of(productVoList).orElse(Collections.EMPTY_LIST); 
		
		if(!productVoList.isEmpty()) return;
		
		param.setrProductType(null);
		List<ProductListVo> reProductVoList = faceRecordingMapper.selectProductListFromGroupCode(param);
		reProductVoList = Optional.of(reProductVoList).orElse(Collections.EMPTY_LIST); 
		 
		if(reProductVoList.isEmpty()) {
			throw new NotFoundException("["+callKey+"] 퇴직연금이며 펀드및 신탁일때 상품을 찾을 수 없음.");
		}
		if(reProductVoList.size() > 1) {
			throw new NotFoundException("["+callKey+"] 퇴직연금이며 펀드및 신탁일때 중복코드가 있음을 발견");
		}
		
		if(parameter.containsKey("BIZ_DIS")) {
			parameter.replace("BIZ_DIS", reProductVoList.get(0).getrProductType());
		}else {
			throw new NotFoundException("["+callKey+"] BIZ_DIS is null");
		}
		
		if(parameter.containsKey("SYS_DIS")) {
			parameter.replace("SYS_DIS",reProductVoList.get(0).getrSysType());
		}else {
			throw new NotFoundException("["+callKey+"] SYS_DIS is null");
		}
	}

	@Override
	public void updateIsaOpTypeScript(List<ScriptStepDetailVo> detailList, int isaOpType, String callKey) throws Exception {
		
		for (ScriptStepDetailVo vo : detailList) {
			String text = vo.getrScriptDetailText();
			if(text == null) {
				throw new Exception(callKey+"text is null");
			}
			if(text.contains("REG_AM") && text.contains("PRD_NM")) {
				text = IsaOpTypeMaker.getInstance().IsaOpTypeMaker(isaOpType, text);
				if(text != null) {
					vo.setrScriptDetailText(text);
				}
			}
		}
		
		
	}

	@Override
	public JSONObject makeEltJsonParamCase(HashMap<String, String> parameter, String callKey) {
		JSONObject jsonObj = new JSONObject();
		
		if(!parameter.get("BIZ_DIS").equals("5")) {
			if (parameter.containsKey("AGNPE_NM")) {
				String agnpeNm = String.valueOf(parameter.get("AGNPE_NM"));
				if (agnpeNm.trim().equals("없음") || agnpeNm.trim().length() == 0 ) {
					jsonObj.put("isDeputy", false);
				} else {
					jsonObj.put("isDeputy", true);
				}
			}else {
				throw new NotFoundException("AGNPE_NM is Null");
			}
		}
		
		if(!parameter.get("SYS_DIS").equals("BK2") && !parameter.get("BIZ_DIS").equals("5") ) {
			if (parameter.containsKey("PSN_YN")) {
				String psnYn = String.valueOf(parameter.get("PSN_YN"));
				if (psnYn.trim().equals("Y")) {
					jsonObj.put("isPersonal", true);
				} else {
					jsonObj.put("isPersonal", false);
				}
			}else {
				throw new NotFoundException("PSN_YN is Null");
			}
		}
		return jsonObj;
	}

	@Override
	public int insertAllRetryRecReason(RequestRetryRecReason recReason) {
		int result = faceRecordingMapper.insertAllRetryRecReason(recReason);
		return result;
	}

	@Override
	public void orderByMoreProductList(FaceRecMoreProductCodeDto moreProductCodedto,
			List<ProductListVo> selectProductList) {
		List<String> orderList = Arrays.asList(moreProductCodedto.getPrdCd().split(","));
		
		orderList = Optional.of(orderList).orElse(Collections.EMPTY_LIST);
		if(orderList.isEmpty()) {
			throw new NotFoundException("다계좌 상품코드를 나눌수 없음");
		}
		List<ProductListVo> voList = new ArrayList<ProductListVo>();
		
		for(int i=0; i<orderList.size();i++) {
			String realCode = orderList.get(i);
			for (ProductListVo vo : selectProductList) {
				String productCode = vo.getrProductCode();
				if(realCode.equals(productCode)) {
					moreProductCodedto.addProductList(vo);
				}else {
					continue;
				}
			}
		}
		
		
	}

	//원화 조건 추가하는로직
	@Override
	public void setCucdIfCase(List<scriptProductValueInfo> valueList, ProductListDto dto) {
		valueList = Optional.of(valueList).orElse(Collections.EMPTY_LIST);
		valueList.forEach(i->{
			if(i.getRsProductValueCode().equals("CUCD")) {
				dto.addScriptDetailList(i.getRsProductValueVal());
			}
		});
		
	}

	@Override
	public int updateRecParamHistoryAllRecData(HashMap<String, String> hash) throws Exception {
		return faceRecordingMapper.updateRecParamHistoryAllRecData(hash);
	}

	@Override
	public RecParamHistoryVo grepRecParamHistory(String callKey) throws Exception {
		return faceRecordingMapper.grepRecParamHistory(callKey);
	}

	@Override
	public ProductPrdRisk moreFindPrdRisk(List<ProductListVo> selectProductList, HashMap<String, String> parameter) throws Exception {
		selectProductList = Optional.of(selectProductList).orElse(Collections.emptyList());
		
		//PRD_CD 이전에 null체크 끝난상태
		List<String> prdCdList = Arrays.asList((parameter.get("PRD_CD").split("\\|"))) ;
		prdCdList = Optional.of(prdCdList).orElse(Collections.emptyList());
		
		//정렬될상품리스트
		List<ProductListVo> sortedProductList = new ArrayList<ProductListVo>();
		
		for(int i=0; i<prdCdList.size();i++) {
			String realCode = prdCdList.get(i);
			for (ProductListVo vo : selectProductList) {
				String productCode = vo.getrProductCode();
				if(realCode.equals(productCode)) {
					sortedProductList.add(vo);
				}else {
					continue;
				}
			}
		}
		
		StringBuffer productRiskNumer = new StringBuffer();
		StringBuffer productRiskName = new StringBuffer();
		
		for (int i = 0 ; i < sortedProductList.size(); i++ ) {
			ProductListSearchVo sVo = new ProductListSearchVo();
			sVo.copyProductListVo(sortedProductList.get(i));
			List<scriptProductValueInfo> valueList = this.selectProductValue2(sVo);
			
			valueList = Optional.of(valueList).orElse(Collections.emptyList());
			if(valueList.isEmpty()) { continue;};
			
			ProductPrdRisk riskInfo = this.setPrdRiskFromValueList(valueList,parameter);
			
			if(riskInfo != null) {
				if(i == sortedProductList.size()-1) {
					productRiskName.append(riskInfo.getProductRiskName() == null ? "":riskInfo.getProductRiskName());
					productRiskNumer.append(riskInfo.getProductRiskNumer() == null ? "":riskInfo.getProductRiskNumer());
					continue;
				}
				productRiskName.append(riskInfo.getProductRiskName() == null ? "":riskInfo.getProductRiskName()+"|");
				productRiskNumer.append(riskInfo.getProductRiskNumer() == null ? "":riskInfo.getProductRiskNumer()+" / ");
			}
		}
		
		return ProductPrdRisk.builder()
						.productRiskName(productRiskName.toString())
						.productRiskNumer(productRiskNumer.toString())
						.build();
	}

	@Override
	public void eltheckCustType(HashMap<String, String> parameter, JSONObject jsonObj) {
		if(parameter.containsKey("CUST_TYPE")) {
			if(parameter.get("CUST_TYPE").equals("5")) {
				if(jsonObj.containsKey("isAgent")){
					jsonObj.replace("isAgent", false);
					logger.info(jsonObj.toJSONString());
				}
			}
		}
		
		
	}

	@Override
	public List<RectryReasonVo> selectSavedRetryReson(RequestRetryRecReason reqInfo) throws Exception {
		List<RectryReasonVo> recReasonList = faceRecordingMapper.selectSavedRetryReson(reqInfo);
		recReasonList = Optional.of(recReasonList).orElse(Collections.emptyList());
		if(recReasonList.isEmpty()) {
			throw new Exception("저장된 녹취리스트를 찾을 수 없음");
		}
		
		return recReasonList;
	}

	@Override
	public void clearRectryReasonToRecParamHistory(String callKey) throws Exception {
		int result = faceRecordingMapper.clearRectryReasonToRecParamHistory(callKey);
		
	}

	@Override
	public List<scriptProductValueInfo> replaceRemunRtTypeValue(List<scriptProductValueInfo> valueList,
			String callKey) throws Exception {
		//optional
		valueList = Optional.of(valueList).orElse(Collections.EMPTY_LIST);
		if(valueList.isEmpty()) {
			throw new Exception("valueList is Empty");
		}
		//foreach builder
		String type = null;
		
		RemunRtTypeChange remun = null;
		for (scriptProductValueInfo valueInfo : valueList) {
			if("REMUN_RT_TYPE".equals(valueInfo.getRsProductValueCode())) {
				type = valueInfo.getRsProductValueVal();
			}
		}
		
		if(type != null) {
			remun = RemunRtTypeChange.builder().type(type).build();
		}
		
		// 펀드가아닐때
		if(remun == null) {
			return valueList;
		}
		//데이터 셋팅
		remun.setTotPeeRtAndSumPeeRt(valueList);
		//value셋팅
		remun.createRemnunValue();
		//가변값리스트에적용
		
		valueList = remun.replaceRemunRtValue(valueList);
		valueList.forEach(i->{
			if(i.getRsProductValueCode().equals("REMUN_RT_TYPE")) {
				logger.info(i.toString());
			}
		});
		
		return valueList;
	}
	//ELF 상품 체크 
	@Override
	public boolean setElfFlag(HashMap<String, String> parameter, String callKey) throws Exception {
		String elfYn = parameter.get("ELF_YN");
		
		if(elfYn == null) {
			return false;
		}
		
		if(elfYn != null && elfYn.equals("Y")) {
			return true;
		}
		return false;
	}

	@Override
	public void setelfCase(HashMap<String, String> parameter, ProductListDto dto, String callKey) throws Exception {
		String next = parameter.get("AGTP_PRD_RCM_RSN");
		if(next == null) {
			throw new NullPointerException("AGTP_PRD_RCM_RSN is null");
		}else {
			dto.setrEltCase(Integer.parseInt(next));
		}
	}

	@Override
	public RecParamHistoryVo selectParamHistoryFromCallKey(String callKey) throws Exception {
		
		RecParamHistoryVo info = faceRecordingMapper.selectParamHistoryFromCallKey(callKey);
		if(info == null) {
			return null;
		}
		
		return info;
	}

	//ELT & ELF 인지 체크
	@Override
	public void elfAndEltCheck(ProductListDto dto, ProductListVo product) throws Exception{
		int type = Integer.parseInt(product.getrProductType());
		if(product.getrProductAttributes() != null) {
			switch (type) {
			case 1:
				dto.setrElfAndEltFlagValue(1);
				break;
			case 2:
				dto.setrElfAndEltFlagValue(2);
				break;
			default : 
				dto.setrElfAndEltFlagValue(0);
				break;
			}
		}else {
			dto.setrElfAndEltFlagValue(0);
		}
		
	}

	@Override
	public ResponseScriptStepMoreProductPk selectScriptStepMoreProductPk(RequestScriptStepKeyParam param) {
		List<Integer> moreProductPkList = faceRecordingMapper.selectScriptStepMoreProductPk(param.getGridIdsArr());
		
		moreProductPkList = Optional.of(moreProductPkList).orElse(Collections.emptyList());
		if(moreProductPkList.isEmpty()) {
			throw new NotFoundException("script Step Pk moreProduct is not Found");
		}
		
		moreProductPkList.forEach(i->{
			param.getGridIdsArr().remove(""+i);
		});
		
		return ResponseScriptStepMoreProductPk.builder().moreProductPkArr(param.getGridIdsArr()).build();
	}

	@Override
	public List<ScriptStepDetailVo> setLizardDetailRsUseYn(List<ScriptStepDetailVo> detailList, ProductListVo product)
			throws Exception {
		String attributes = product.getrProductAttributes();
		if(attributes == null) return detailList;
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(attributes);
		
		JSONObject jsonObj = (JSONObject) obj;
		
		String isLizard = Optional.of(String.valueOf(jsonObj.get("isLizard"))).orElse("false");
		String lizardTimes = Optional.of(String.valueOf(jsonObj.get("lizardTimes"))).orElse("0");
		
		if(isLizard.equals("false")) return detailList;
		
		detailList.forEach(i-> {
			JSONParser detailAttrParse = new JSONParser();
			Object detailParse = null;
			
			try {
				detailParse = detailAttrParse.parse(i.getrProductAttributes());
			}catch (Exception e) {
				logger.error("error",e);
			}
			
			if(detailParse != null) {
				JSONObject jsonobj2 = (JSONObject) detailParse;
				String detailTimes = String.valueOf(jsonobj2.get("lizardTimes"));
				
				if(detailTimes != null) {
					if(!detailTimes.equals("null")) {
						String useYn = detailTimes.equals(lizardTimes) ? "Y" : "N";
						i.setrUseYn(useYn);
					}
				}
			}
			
		});
		return detailList;
	}

	@Override
	public List<ScriptRegistrationInfo> addMoreProductDetailScript(
			List<ScriptRegistrationInfo> scriptDetailHistoryList) {
		
		int listSize = scriptDetailHistoryList.size();
		try {
			ScriptRegistrationInfo info = new ScriptRegistrationInfo();
			info.createMoreProductScriptDetailOne();
			scriptDetailHistoryList.add(info);
		}catch (Exception e) {
			logger.error("error",e);
		}
		
		return scriptDetailHistoryList;
	}

	@Override
	public void checkNonElfStepList(ProductListDto dto) throws Exception {
		//비정규ELT 플레그
		boolean flag = (dto.getrProductAttributes() != null && dto.getrProductType().equals("1") && dto.getrProductGroupYn().equals("N")) ? true : false;
		
		if(!flag) return;
		
		int checkCnt = uploadMapper.checkNonRelEltStepDetail(dto);
		
		if(checkCnt == 0) {
			throw new Exception();
		}
	}
		@Override
	public Integer selectProductListGroupTrCase(ProductListVo product) {
		return faceRecordingMapper.selectProductListGroupTrCase(product);
	}

	@Override
	public void upInvestProductCheck(ProductListDto dto, ProductListVo product, HashMap<String, String> parameter) {
		String upInvestYn = "N";
		upInvestYn =  Optional.ofNullable(parameter.get("UP_INVEST_YN")).orElse("N");
		
		if(upInvestYn.equals("Y")) {
			dto.setUpInvestProductYn(true);
		}else {
			dto.setUpInvestProductYn(false);
		}
		
	}

	@Override
	public List<ScriptStepDetailVo> modifyUpInvestProductDetailList(List<ScriptStepDetailVo> detailList,
			HashMap<String, String> parameter) throws Exception {
		
		String cusAnswer = Optional.of(parameter.get("CUS_ANSER")).orElseThrow(() -> new NotFoundException("CUS_ANSER is null"));
		
		List<String> answerArr = Optional.of(Arrays.asList(cusAnswer.split(":"))).orElse(Collections.emptyList());
		if(answerArr.isEmpty()) {
			throw new NotFoundException("CUS_ANSER is Not Correct");
		}
		
		List<String> answerNumberArr = Optional.of(Arrays.asList(answerArr.get(1).split(","))).orElse(Collections.emptyList());
		
		if(answerNumberArr.isEmpty()) {
			throw new NotFoundException("CUS_ANSER is Not Correct");
		}
		
		//quest : 문항 answer 
		Queue<JSONObject> hashQueue = new LinkedList<JSONObject>();
		for (int i = 0; i < answerNumberArr.size(); i++) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("answer", answerNumberArr.get(i));
			jsonObj.put("question", String.valueOf(i+1));
			hashQueue.add(jsonObj);
		}
		 
		while( !hashQueue.isEmpty() ) {
//			String jsonString = hashQueue.poll().toJSONString();
			JSONObject jObj = hashQueue.poll();
			String q = (String) jObj.get("question");
			String a = (String) jObj.get("answer");
			
			detailList.forEach(i->{
				String attr = Optional.ofNullable(i.getrScriptStepDetailCaseAttributes()).orElse("").replaceAll(" ", "");
				if(attr.length() > 0) {
					JSONParser parse = new JSONParser();
					JSONObject attObj = null;
					try {
						attObj = (JSONObject)parse.parse(attr);
					} catch (ParseException e) {
						throw new NotFoundException("고도화 상품 디테일 속성에러");
					}
					
					String aQ = (String) attObj.get("question");
					String aA = (String) attObj.get("answer");
					
					if(q.equals(aQ)) {
						i.setrUseYn(a.equals(aA) ? "Y" : "N");
					}
				}
			});
		}
		return detailList;
	}

	@Override
	public int clearStepRec(String callKey) throws Exception {
		int result = faceRecordingMapper.clearStepRec(callKey);
		return result;
	}

	@Override
	public List<RuserCode> getDeptData(String oprNo) {
		List<RuserCode> info = faceRecordingMapper.getDeptData(oprNo);
		
		info = Optional.of(info).orElse(Collections.EMPTY_LIST);
		
		return info;
	}

	@Override
	public void updateStepSizeZeroUseYn(String callKey) throws Exception {
		faceRecordingMapper.updateStepSizeZeroUseYn(callKey);
	}
	


	
}

	
