package com.furence.recsee.wooribank.facerecording.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.security.acls.model.NotFoundException;

import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.main.model.RetryRecInfo;
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
import com.furence.recsee.wooribank.facerecording.model.RecParamHistroyInfo;
import com.furence.recsee.wooribank.facerecording.model.RectryReasonVo;
import com.furence.recsee.wooribank.facerecording.model.RequestRetryRecReason;
import com.furence.recsee.wooribank.facerecording.model.RetryRecReasonVo;
import com.furence.recsee.wooribank.facerecording.model.RuserCode;
import com.furence.recsee.wooribank.facerecording.model.ScriptRegistrationInfo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepDto;
import com.furence.recsee.wooribank.facerecording.request.RequestScriptStepKeyParam;
import com.furence.recsee.wooribank.facerecording.response.ResponseScriptStepMoreProductPk;

public interface FaceRecordingService {
	List<FaceRecordingInfo> selectFaceRecordingInfo(FaceRecordingInfo faceRecordingInfo);
	List<FaceRecProductInfo> selectFaceRecProductList(FaceRecProductInfo faceRecProductInfo);
	List<FaceRecScriptInfo> selectFaceRecScriptList(FaceRecScriptInfo faceRecScriptInfo);

	Integer insertFaceRecScriptInfo(FaceRecScriptInfo faceRecScriptInfo);
	Integer updateFaceRecScriptInfo(FaceRecScriptInfo faceRecScriptInfo);
	
	Integer insertFaceRecInfo(FaceRecordingInfo faceRecordingInfo);
	Integer insertFaceRecProductInfo(FaceRecProductInfo faceRecProductInfo);
	//recfile 콜키로 조회 
	FaceRecordingInfo selectRsRecFileRecCallKey(FaceRecordingInfo faceRecordingInfo);
	Integer insertRetryRecReason(RetryRecInfo retryRecInfo);
	//파라미터 추가
	String insertRecParam(HttpServletRequest request);
	//상품리스트 조회
	List<ProductListVo> selectProductList(ProductListSearchVo searchVo,String callKeyr);
	//상품 그룹 pk조회
	Integer selectProductListGroup(ProductListVo productVo);
	
	//history 테이블 카운트 조회
	Integer selectCountScriptHistroy(String callKey);
	//가변값조회
	List<scriptProductValueInfo> selectProductValue2(ProductListSearchVo searchVo);
	//CP클래스 확인조회
	boolean checkCpClassFromProductCode(ProductListSearchVo searchVo);
	//step 히스토리 추가
	Integer insertScriptStepHistory(ProductListDto dto);
	//step_detail 을 history와 엮어 select
	List<ScriptStepDetailVo> selectScriptDetailToHistory2(ProductListDto dto) throws NullPointerException;
	
	
	//step detail history insert
	Integer insertScriptDetailHistory(List<ScriptStepDetailVo> detailList,String callKey) throws Exception;
	//TTS history insert
	Integer insertScriptTTSHistory2(ScriptHistoryCreateParam param);
	//step pk one 조회
	List<Integer> selectOneScriptStepPk(Integer pk);
	
	ProductListVo selectProductListPkfromProductCode(ScriptRegistrationInfo scriptRegistrationInfo);
	
	List<ScriptRegistrationInfo> scriptDetailHistoryList(ScriptRegistrationInfo scriptRegistrationInfo);
	
	Integer updateScriptStepHistory(ScriptRegistrationInfo scriptRegistrationInfo);
	
	List<ScriptRegistrationInfo> selectScriptStepHistoryTree(ScriptRegistrationInfo scriptRegistrationInfo);
	//요청으로 넘어온 파라미터 HashMap으로 바꾸어줌
	HashMap<String, String> getRequestParamterChangeHashMap(String parameter) throws IOException;
	
	
	ScriptHistoryCreateParam setScriptHistoryCreateParam(HashMap parameter,String callKey);
	
	ProductListSearchVo setProductListSearchVo(HashMap parameter, boolean isaRedTextFlag);
	
	ProductPrdRisk setPrdRiskFromValueList(List<scriptProductValueInfo> valueList, HashMap<String, String> parameter) throws Exception;
	
	String setAd047FromValueList(List<scriptProductValueInfo> valueList) throws Exception;
	
	ProductListDto setCpClassParam(ProductListDto dto);
	
	boolean setValueListFromParam(List<scriptProductValueInfo> valueList, HashMap parameter, String callKey);
	
	List<Integer> setDetailTextToVariable(List<ScriptStepDetailVo> detailList, List<scriptProductValueInfo> valueList);
	
	BkScriptParamVo setBkVoParam(HashMap parameter);
	
	
	List<ScriptStepDetailVo> addBkScriptToDetailList(BkScriptParamVo bkVo, List<ScriptStepDetailVo> detailList) throws Exception;
	
	boolean setIsaRedTextFlag(HashMap parameter, String callKey);
	boolean setAClassFlag(HashMap parameter, String callKey);
	
	List<ScriptStepDetailVo> addScriptDetailRedText(List<ScriptStepDetailVo> detailList,String redText ,String callKey);
	void setBkDetailIfCaseDetail(ScriptHistoryCreateParam param, List<EtcConfigInfo> result);
	
	FaceRecMoreProductCodeDto setFaceRecMoreProductCode(HashMap parameter, String callKey);
	
	ProductListDto setProductListDtoCreateParam(ProductListDto dto, HashMap parameter, String callKey);
	
	List<ScriptStepDto> selectMoreProductStep(FaceRecMoreProductCodeDto dto) throws Exception;
	
	List<ScriptStepDto> modifyMoreProductStep(List<ScriptStepDto> stepDtoList, FaceRecMoreProductCodeDto moreProductCodedto) throws Exception;
	
	int insertMordeProductStep(List<ScriptStepDto> stepDtoList, String callKey);
	
	ProductListVo selectProductListPkfromProductCode2(ScriptRegistrationInfo scriptRegistrationInfo);
	
	List<String> setOfferProductValueList(HashMap parameter, String callKey);
	
	List<scriptProductValueInfo> setOfferBkProductValueList(HashMap parameter, String callKey);
	
	List<ScriptStepDetailVo> makeOfferDetailScript(List<ScriptStepDetailVo> detailList, int pType, HashMap parameter);
	
	void setValueListToProductName(List<scriptProductValueInfo> valueList, List<ProductListVo> productList);
	
	List<ScriptStepDto> modifyMoreDeathProductStep(List<ScriptStepDto> stepDtoList,
			FaceRecMoreProductCodeDto moreProductCodedto, String callKey);
	
	void setMoreProductScriptDetail(List<ScriptStepDto> stepDtoList, List<ScriptStepDetailVo> detailList, FaceRecMoreProductCodeDto moreProductCodedto) throws CloneNotSupportedException;
	
	void checkOfferDetailScript(int pType, HashMap parameter);
	
	void checkRealTimeParameter(HashMap parameter, String callKey) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException;
	
	int checkIsaIfDetailCase(HashMap parameter, ProductListSearchVo searchVo, ProductListDto dto) throws NotFoundException;
	
	void deleteScriptStepHistoryFromCallKey(String callKey);
	
	void setEltRequestParameter(Map<String, String> readValue, HashMap<String, String> parameter, ProductListDto dto);
	
	List<ScriptStepDetailVo> selectEltScriptDetailToHistory(ProductListDto dto) throws Exception;
	
	List<ScriptProductValueEltDto> modifyEltValueList(List<scriptProductValueInfo> valueList, String callKey,
			HashMap<String, String> parameter) throws Exception;
	
	List<Integer> setDetailTextToVariableElt(List<ScriptStepDetailVo> detailList,
			List<ScriptProductValueEltDto> eltDto) throws Exception;
	
	void modifyEltComReptScript(List<ScriptStepDetailVo> detailList, HashMap<String, String> parameter, String callKey);
	
	//ELT 원화외화 체크
	void checkEltKorea(Map<String, String> readValue, ProductListDto dto);
	//ELT 적합성유형체크
	void checkAgtpPrdRcm(HashMap<String, String> parameter, ProductListDto dto);
	
	ProductPrdRisk findPrdRisk(List<ProductListVo> selectProductList, HashMap<String, String> parameter) throws Exception;
	
	
	void selectShtProductManyBizDIs(ScriptHistoryCreateParam param, String callKey, HashMap<String, String> parameter);
	
	void updateIsaOpTypeScript(List<ScriptStepDetailVo> detailList, int isaOpType, String callKey) throws Exception;
	
	JSONObject makeEltJsonParamCase(HashMap<String, String> parameter, String callKey);
	
	int insertAllRetryRecReason(RequestRetryRecReason recReason);
	
	void orderByMoreProductList(FaceRecMoreProductCodeDto moreProductCodedto, List<ProductListVo> selectProductList);
	
	void setCucdIfCase(List<scriptProductValueInfo> valueList, ProductListDto dto);
	
	int updateRecParamHistoryAllRecData(HashMap<String, String> hash) throws Exception;
	
	RecParamHistoryVo grepRecParamHistory(String callKey) throws Exception;
	
	ProductPrdRisk moreFindPrdRisk(List<ProductListVo> selectProductList, HashMap<String, String> parameter) throws Exception;
	
	void eltheckCustType(HashMap<String, String> parameter,JSONObject jsonObj) throws Exception;
	
	List<RectryReasonVo> selectSavedRetryReson(RequestRetryRecReason reqInfo) throws Exception;
	
	void clearRectryReasonToRecParamHistory(String callKey) throws Exception;
	
	List<scriptProductValueInfo> replaceRemunRtTypeValue(List<scriptProductValueInfo> valueList, String callKey) throws Exception;
	
	boolean setElfFlag(HashMap<String, String> parameter, String callKey) throws Exception;
	
	void setelfCase(HashMap<String, String> parameter, ProductListDto dto, String callKey) throws Exception;
	
	RecParamHistoryVo selectParamHistoryFromCallKey(String callKey) throws Exception;
	
	//ELF and ELT 구분체크
	void elfAndEltCheck(ProductListDto dto, ProductListVo product) throws Exception;
	
	ResponseScriptStepMoreProductPk selectScriptStepMoreProductPk(RequestScriptStepKeyParam param);
	
	List<ScriptStepDetailVo> setLizardDetailRsUseYn(List<ScriptStepDetailVo> detailList, ProductListVo product) throws Exception;
	
	List<ScriptRegistrationInfo> addMoreProductDetailScript(List<ScriptRegistrationInfo> scriptDetailHistoryList);
	
	void upInvestProductCheck(ProductListDto dto, ProductListVo product, HashMap<String, String> parameter);
	
	List<ScriptStepDetailVo> modifyUpInvestProductDetailList(List<ScriptStepDetailVo> detailList,
			HashMap<String, String> parameter) throws Exception;
			
	void checkNonElfStepList(ProductListDto dto) throws Exception;
	
	int clearStepRec(String callKey) throws Exception;
	
	List<RuserCode> getDeptData(String oprNo);	
	
	Integer selectProductListGroupTrCase(ProductListVo product);
	
	void updateStepSizeZeroUseYn(String callKey) throws Exception;
	
	
	
}
