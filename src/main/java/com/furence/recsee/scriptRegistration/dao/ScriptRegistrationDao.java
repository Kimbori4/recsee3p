
package com.furence.recsee.scriptRegistration.dao;

import java.util.HashMap;
import java.util.List;

import com.furence.recsee.scriptRegistration.model.ScriptStepDetailDTO;
import com.furence.recsee.scriptRegistration.model.ScriptStepDetailVO;
import com.furence.recsee.scriptRegistration.model.AdminScriptStepDetailInfo;
import com.furence.recsee.scriptRegistration.model.ScriptCommonCodeVO;
import com.furence.recsee.scriptRegistration.model.ScriptRegistrationInfo;
import com.furence.recsee.scriptRegistration.model.ScriptStepDetailVO;
import com.furence.recsee.scriptRegistration.model.scriptProductValueInfo;
import com.furence.recsee.wooribank.facerecording.model.ProductListVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepVo;
import com.furence.recsee.wooribank.facerecording.request.RequestAdminScriptUpdateInfo;
import com.furence.recsee.wooribank.facerecording.request.RequestSinkTableInfo;

public interface ScriptRegistrationDao {

	// product
	List<ScriptRegistrationInfo> selectProductList(ScriptRegistrationInfo scriptRegistrationInfo);
	
	// product count
	Integer selectProductListCount(ScriptRegistrationInfo scriptRegistrationInfo);

	// tree
	List<ScriptRegistrationInfo> selectScriptStepList(ScriptRegistrationInfo scriptRegistrationInfo);
	
	// tree
	List<ScriptStepDetailVO> selectScriptStepDetailList(ScriptStepDetailDTO scriptStepDetailDTO);
	
	// searchScript
	List<ScriptRegistrationInfo> searchScript(ScriptRegistrationInfo scriptRegistrationInfo);

	Integer updateScript(ScriptRegistrationInfo scriptRegistrationInfo);

	// script UPdate
	Integer updateScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo);

	// common script UPdate
	Integer updateCommonScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo);

	// detail
	List<ScriptRegistrationInfo> scriptDetailList(ScriptRegistrationInfo scriptRegistrationInfo);

	// delete script
	Integer deleteScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo);

	// 스크립트 히스토리 넣을 데이터 조회
	List<ScriptRegistrationInfo> selectScriptStepHistoryList(ScriptRegistrationInfo scriptRegistrationInfo);

	// 스크립트 히스토리 데이터 조회
	Integer selectCountScriptHistroy(ScriptRegistrationInfo scriptRegistrationInfo);

	// 스크립트 히스토리 저장
	Integer insertScriptStepHistory(ScriptRegistrationInfo scriptRegistrationInfo);

	// 스크립트 히스토리트리 데이터 조회
	List<ScriptRegistrationInfo> selectScriptStepHistoryTree(ScriptRegistrationInfo scriptRegistrationInfo);

	// 스크립트 디테일 히스토리 데이터 저장
	Integer insertScriptDetailHistory(ScriptRegistrationInfo scriptRegistrationInfo);

	//// 스크립트 디테일 히스토리 카운트 조회
	Integer scriptDetailHistoryCount(ScriptRegistrationInfo scriptRegistrationInfo);

	// common delete script
	Integer deleteCommonScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo);

	// add script
	Integer addScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo);

	// common script add
	Integer addCommonScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo);

	// common script search
	List<ScriptRegistrationInfo> searchCommonScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo);

	// common script delete
	Integer deleteCommonScript(ScriptRegistrationInfo scriptRegistrationInfo);

	// common script insert
	Integer insertCommonScript(ScriptRegistrationInfo scriptRegistrationInfo);

	// common script update
	Integer updateCommonScript(ScriptRegistrationInfo scriptRegistrationInfo);

	// select tree script
	List<ScriptRegistrationInfo> selectTreeScript(ScriptRegistrationInfo scriptRegistrationInfo);

	// add tree script
	Integer addTreeScript(ScriptRegistrationInfo scriptRegistrationInfo);

	// delete tree script
	Integer deleteTreeScript(ScriptRegistrationInfo scriptRegistrationInfo);

	// edit tree script
	Integer editTreeScript(ScriptRegistrationInfo scriptRegistrationInfo);

	// 스크립트 디테일 히스토리 데이터 조회
	List<ScriptRegistrationInfo> scriptDetailHistoryList(ScriptRegistrationInfo scriptRegistrationInfo);

	// 스크립트 스텝 히스토리 업데이트
	Integer updateScriptStepHistory(ScriptRegistrationInfo scriptRegistrationInfo);

	// 스크립트 TTS HISTORY 저장
	Integer insertScriptTTSHistory(ScriptRegistrationInfo scriptRegistrationInfo);

	Integer insertScriptTTS(HashMap<String, Object> hashMap);

	Integer selectProductListGroup(ScriptRegistrationInfo scriptRegistrationInfo);

	List<ScriptRegistrationInfo> selectScriptDetailToHistory(ScriptRegistrationInfo scriptRegistrationInfo);

	List<scriptProductValueInfo> selectProductValue(ScriptRegistrationInfo scriptRegistrationInfo);

	// ProductListGroup select
	List<ScriptRegistrationInfo> selectGRProductList(ScriptRegistrationInfo scriptRegistrationInfo);

	// script step테이블 stepOrder 알기 위함
	List<ScriptRegistrationInfo> selectScriptDetailPK(ScriptRegistrationInfo scriptRegistrationInfo);

	// script_value에서 TTS가 Y인 값 가져오기
	List<ScriptRegistrationInfo> selectValueTTS(ScriptRegistrationInfo scriptRegistrationInfo);

	// 상품추가
	Integer addproducList(ScriptRegistrationInfo scriptRegistrationInfo);

	String selectDetailFilePath(Integer rScriptStepFk);

	List<ScriptRegistrationInfo> selectRecState(ScriptRegistrationInfo scriptRegistrationInfo);

	Integer insertRecParam(HashMap param);

	List<scriptProductValueInfo> selectValueTTS2(ScriptRegistrationInfo scriptRegistrationInfo);

	List<Integer> selectOneScriptStepPk(Integer pk);

	List<ScriptRegistrationInfo> searchScriptDblClick(ScriptRegistrationInfo scriptRegistrationInfo);

	List<scriptProductValueInfo> selectValueFromScriptStepPk(ScriptRegistrationInfo scriptRegistrationInfo);

	List<ScriptRegistrationInfo> selectTree_Script(ScriptRegistrationInfo scriptRegistrationInfo);

	ScriptRegistrationInfo selectProductListPkfromProductCode(ScriptRegistrationInfo scriptRegistrationInfo);

	List<scriptProductValueInfo> selectProductValue2(String productCode);

	ScriptRegistrationInfo selectProductListOne(ScriptRegistrationInfo scriptRegistrationInfo);

	List<ScriptRegistrationInfo> selectProductListGroupList(String productCode);

	// 가변값조회
	List<ScriptRegistrationInfo> selectProductValueJoinProductList(ScriptRegistrationInfo info);

	// 가변값예약 테이블 조회
	List<ScriptRegistrationInfo> selectRsProductReserveFromProductCode(ScriptRegistrationInfo scriptRegistrationInfo);

	// 예약 테이블 추가
	int insertRsProductReserve(ScriptRegistrationInfo scriptRegistrationInfo);

	// 예약일자 업데이트
	int updateProdcutReservceDate(ScriptRegistrationInfo originalData);

	// 가변값 업데이트
	int updateProductValueNameAndVal(ScriptRegistrationInfo scriptRegistrationInfo);

	List<ScriptRegistrationInfo> selectProductChangeList(ScriptRegistrationInfo scriptRegistrationInfo);

	// 가변값 예약테이블 rs_use_yn => N
	int updateProductReserveRsUseYn(ScriptRegistrationInfo scriptRegistrationInfo);
	
	// 공통코드 조회
	List<String> selectScriptCommonCode(List<String> categories);
	
	//공용문구 가변값
	List<ScriptRegistrationInfo> selectValueComTTS(ScriptRegistrationInfo scriptRegistrationInfo);

	List<ProductListVo> adminSelectProductList(ScriptRegistrationInfo info);
	
	List<ScriptStepVo> adminSelectScriptStep(String tKey);

	List<AdminScriptStepDetailInfo> adminSelectScriptStepDetail(String tKey);

	int adminUpdateScriptDetail(RequestAdminScriptUpdateInfo info);

	int insertSinkTable(RequestSinkTableInfo sinkInfo) throws Exception;

	int deleteProductListToPk(String pk);
	
	List<ScriptRegistrationInfo> selectTaState(ScriptRegistrationInfo scriptRegistrationInfo);
}