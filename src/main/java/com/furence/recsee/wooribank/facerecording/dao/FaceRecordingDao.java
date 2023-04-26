package com.furence.recsee.wooribank.facerecording.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.furence.recsee.main.model.RetryRecInfo;
import com.furence.recsee.scriptRegistration.model.ScriptHistoryCreateParam;
import com.furence.recsee.wooribank.facerecording.model.ScriptRegistrationInfo;
import com.furence.recsee.scriptRegistration.model.scriptProductValueInfo;
import com.furence.recsee.wooribank.facerecording.model.BkScriptParamVo;
import com.furence.recsee.wooribank.facerecording.model.FaceRecMoreProductCodeDto;
import com.furence.recsee.wooribank.facerecording.model.FaceRecProductInfo;
import com.furence.recsee.wooribank.facerecording.model.FaceRecScriptInfo;
import com.furence.recsee.wooribank.facerecording.model.FaceRecordingInfo;
import com.furence.recsee.wooribank.facerecording.model.ProductCharacterDetailVo;
import com.furence.recsee.wooribank.facerecording.model.ProductFundDetailInfo;
import com.furence.recsee.wooribank.facerecording.model.ProductListDto;
import com.furence.recsee.wooribank.facerecording.model.ProductListSearchVo;
import com.furence.recsee.wooribank.facerecording.model.ProductListVo;
import com.furence.recsee.wooribank.facerecording.model.RecParamHistoryVo;
import com.furence.recsee.wooribank.facerecording.model.RecParamHistroyInfo;
import com.furence.recsee.wooribank.facerecording.model.RectryReasonVo;
import com.furence.recsee.wooribank.facerecording.model.RequestRetryRecReason;
import com.furence.recsee.wooribank.facerecording.model.RetryRecReasonVo;
import com.furence.recsee.wooribank.facerecording.model.RuserCode;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepDto;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepListAndCallKey;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepVo;
import com.furence.recsee.wooribank.facerecording.request.RequestScriptStepKeyParam;
@Repository
@Qualifier("faceRecordingDao")
public interface FaceRecordingDao {
	
	List<FaceRecordingInfo> selectFaceRecordingInfo(FaceRecordingInfo faceRecordingInfo);

	List<FaceRecProductInfo> selectFaceRecProductList(FaceRecProductInfo faceRecProductInfo);

	List<FaceRecScriptInfo> selectFaceRecScriptList(FaceRecScriptInfo faceRecScriptInfo);

	Integer insertFaceRecScriptInfo(FaceRecScriptInfo faceRecScriptInfo);

	Integer updateFaceRecScriptInfo(FaceRecScriptInfo faceRecScriptInfo);

	Integer insertFaceRecInfo(FaceRecordingInfo faceRecordingInfo);
	
	Integer insertFaceRecProductInfo(FaceRecProductInfo faceRecProductInfo);

	FaceRecordingInfo selectRsRecFileRecCallKey(FaceRecordingInfo faceRecordingInfo);

	Integer insertRetryRecReason(RetryRecInfo retryRecInfo);

	void insertRecParam(HashMap<String,String> temp);

	List<ProductListVo> selectProductList(ProductListSearchVo searchVo);

	Integer selectProductListGroup(ProductListVo vo);

	Integer selectCountScriptHistroy(String callKey);

	List<scriptProductValueInfo> selectProductValue2(ProductListSearchVo searchVo);

	List<Integer> checkCpClassFromProductCode(ProductListSearchVo searchVo);

	Integer insertScriptStepHistory(ProductListDto dto);

	List<ScriptStepDetailVo> selectScriptDetailToHistory2(ProductListDto dto);

	Integer insertScriptDetailHistory(ScriptStepDetailVo data);

	Integer insertScriptTTSHistory2(ScriptHistoryCreateParam param);

	List<Integer> selectOneScriptStepPk(Integer pk);

	ProductListVo selectProductListPkfromProductCode(ScriptRegistrationInfo scriptRegistrationInfo);

	List<ScriptRegistrationInfo> scriptDetailHistoryList(ScriptRegistrationInfo scriptRegistrationInfo);

	Integer updateScriptStepHistory(ScriptRegistrationInfo scriptRegistrationInfo);

	List<ScriptRegistrationInfo> selectScriptStepHistoryTree(ScriptRegistrationInfo scriptRegistrationInfo);

	 List<ProductFundDetailInfo> selectBkDetailScript(BkScriptParamVo bkVo);

	List<ScriptStepDto> selectMoreProductStep(FaceRecMoreProductCodeDto dto);

	int insertMordeProductStep(List<ScriptStepDto> stepDtoList);

	ProductListVo selectProductListPkfromProductCode2(ScriptRegistrationInfo scriptRegistrationInfo);

	List<scriptProductValueInfo> selectBkProductValueList(HashMap parameter);

	List<ProductCharacterDetailVo> selectOfferDetailScript(HashMap<String, Object> hash);

	List<ScriptStepVo> selectScriptStepHistoryWhereRedTextStep(String callKey);

	void deleteScriptStepHistoryFromCallKey(String callKey);

	List<ScriptStepDetailVo> selectEltScriptDetailToHistory(ProductListDto dto);

	List<ScriptStepVo> selectScriptStepElt(ProductListDto dto);

	int insertScriptStepHistoryElt(ScriptStepListAndCallKey sslAndCallKey);

	List<ProductListVo> selectProductListFromGroupCode(ScriptHistoryCreateParam param);

	int insertAllRetryRecReason(RequestRetryRecReason recReason);

	int updateRecParamHistoryAllRecData(HashMap<String, String> hash);

	RecParamHistoryVo grepRecParamHistory(String callKey);

	List<RectryReasonVo> selectSavedRetryReson(RequestRetryRecReason reqInfo) throws Exception;

	int clearRectryReasonToRecParamHistory(String callKey) throws Exception;

	RecParamHistoryVo selectParamHistoryFromCallKey(String callKey) throws Exception;

	List<Integer> selectScriptStepMoreProductPk(List<String> list);
	
	Integer selectProductListGroupTrCase(ProductListVo product);

	
	int clearStepRec(String callKey);

	List<RuserCode> getDeptData(String oprNo);

	void updateStepSizeZeroUseYn(String callKey);
}
