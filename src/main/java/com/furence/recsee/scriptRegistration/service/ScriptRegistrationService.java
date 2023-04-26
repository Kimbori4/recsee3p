
  package com.furence.recsee.scriptRegistration.service;
  
  import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;

import com.furence.recsee.scriptRegistration.model.AdminScriptStepDetailInfo;
import com.furence.recsee.scriptRegistration.model.ScriptRegistrationInfo;
import com.furence.recsee.scriptRegistration.model.ScriptStepDetailDTO;
import com.furence.recsee.scriptRegistration.model.ScriptStepDetailVO;
import com.furence.recsee.scriptRegistration.model.scriptProductValueInfo;
import com.furence.recsee.wooribank.facerecording.model.ProductListVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepVo;
import com.furence.recsee.wooribank.facerecording.request.RequestAdminScriptUpdateInfo;
import com.furence.recsee.wooribank.facerecording.request.RequestSinkTableInfo;
  	
  public interface ScriptRegistrationService {
  
  //product
  List<ScriptRegistrationInfo> selectProductList(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //product
  Integer selectProductListCount(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //tree
  List<ScriptRegistrationInfo> selectScriptStepList(ScriptRegistrationInfo scriptRegistrationInfo);
  
  // 스크립트 디테일 리스트 가져오기
  List<ScriptStepDetailVO> selectScriptStepDetailList(ScriptStepDetailDTO scriptStepDetailDTO);
   
  //searchScript
  List<ScriptRegistrationInfo> searchScript (ScriptRegistrationInfo scriptRegistrationInfo);
    
  Integer updateScript(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //script UPdate
  Integer updateScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //common script UPdate
  Integer updateCommonScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //스크립트 히스토리 넣을 데이터 조회 
  List<ScriptRegistrationInfo> selectScriptStepHistoryList(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //스크립트 히스토리 데이터 조회 
  Integer selectCountScriptHistroy(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //스크립트 히스토리 저장 
  Integer insertScriptStepHistory(ScriptRegistrationInfo scriptRegistrationInfo);

  //스크립트 히스토리트리 데이터 조회 
  List<ScriptRegistrationInfo> selectScriptStepHistoryTree(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //스크립트 디테일 히스토리 데이터 저장 
  Integer insertScriptDetailHistory(ScriptRegistrationInfo scriptRegistrationInfo);
  ////스크립트 디테일 히스토리 카운트 조회 
  Integer scriptDetailHistoryCount(ScriptRegistrationInfo scriptRegistrationInfo);

  //스크립트 디테일 히스토리 데이터 조회 
  List<ScriptRegistrationInfo> scriptDetailHistoryList(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //스크립트 스텝 히스토리 업데이트 
  Integer updateScriptStepHistory(ScriptRegistrationInfo scriptRegistrationInfo);

  //detail
  List<ScriptRegistrationInfo> scriptDetailList(ScriptRegistrationInfo scriptRegistrationInfo);

  // delete script
  Integer deleteScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //delete common script 
  Integer deleteCommonScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //add script 
  Integer addScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo); 
    
  //select common script
  List<ScriptRegistrationInfo> searchCommonScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //delete common script
  Integer deleteCommonScript(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //insert common script
  Integer insertCommonScript(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //update common script
  Integer updateCommonScript(ScriptRegistrationInfo scriptRegistrationInfo);

  //select tree script
  List<ScriptRegistrationInfo> selectTreeScript(ScriptRegistrationInfo scriptRegistrationInfo);

  //add tree script
  Integer addTreeScript(ScriptRegistrationInfo scriptRegistrationInfo);
	
  //delete tree script
  Integer deleteTreeScript(ScriptRegistrationInfo scriptRegistrationInfo);
 
  //edit tree script
  Integer editTreeScript(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //스크립트 TTS HISTORY 저장
  Integer insertScriptTTSHistory(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //insert Script TTS
  Integer insertScriptTTS(HashMap<String, Object> hashMap);
 
  //그룹서치
  Integer selectProductListGroup(ScriptRegistrationInfo scriptRegistrationInfo);
  
  //ProductListGroup select
  List<ScriptRegistrationInfo> selectGRProductList(ScriptRegistrationInfo scriptRegistrationInfo);
	
  List<ScriptRegistrationInfo> selectScriptDetailToHistory(ScriptRegistrationInfo scriptRegistrationInfo);
	
  List<scriptProductValueInfo> selectProductValue(ScriptRegistrationInfo scriptRegistrationInfo);
	
  //script step테이블 stepOrder 알기 위함
  List<ScriptRegistrationInfo> selectScriptDetailPK(ScriptRegistrationInfo scriptRegistrationInfo);
	
  //script_value에서 TTS가 Y인 값 가져오기
  List<ScriptRegistrationInfo> selectValueTTS(ScriptRegistrationInfo scriptRegistrationInfo);
	
  //상품추가
  Integer addproducList(ScriptRegistrationInfo scriptRegistrationInfo);
	
	
  String selectDetailFilePath(Integer getrScriptStepFk);
	
  //녹취 상태 확인(api)
  List<ScriptRegistrationInfo> selectRecState(ScriptRegistrationInfo scriptRegistrationInfo);
	  
  Integer insertRecParam(HashMap<String, Object> hashMap);
	
  //스크립트 가변조회
  List<scriptProductValueInfo> selectValueTTS2(ScriptRegistrationInfo scriptRegistrationInfo);
	
  //디테일 pk 순차 pk asc 조회
  List<Integer> selectOneScriptStepPk(Integer pk);
	
  //검색창 더블클릭 표출
  List<ScriptRegistrationInfo> searchScriptDblClick(ScriptRegistrationInfo scriptRegistrationInfo);
	
  //selscst tree _cnrk
  List<ScriptRegistrationInfo> selectTree_Script(ScriptRegistrationInfo scriptRegistrationInfo);
	
  List<scriptProductValueInfo> selectValueFromScriptStepPk(ScriptRegistrationInfo scriptRegistrationInfo);
	
  ScriptRegistrationInfo selectProductListPkfromProductCode(ScriptRegistrationInfo scriptRegistrationInfo);
	
  //장진호(value select)
  List<scriptProductValueInfo> selectProductValue2(String productCode);
	
  ScriptRegistrationInfo selectProductListOne(ScriptRegistrationInfo scriptRegistrationInfo);
	
  List<ScriptRegistrationInfo> selectProductListGroupList(String productCode);
	
  //가변값조회
  List<ScriptRegistrationInfo> selectProductValueJoinProductList(ScriptRegistrationInfo info);
	
  //가변값예약테이블 조회
  List<ScriptRegistrationInfo> selectRsProductReserveFromProductCode(ScriptRegistrationInfo scriptRegistrationInfo);
	
  //가변예약추가
  int insertRsProductReserve(ScriptRegistrationInfo scriptRegistrationInfo);
	
  //예약일자 업데이트
  int updateProdcutReservceDate(ScriptRegistrationInfo originalData);
	
  //가변값 업데이트
  int updateProductValueNameAndVal(ScriptRegistrationInfo scriptRegistrationInfo);
	  
  //스크립트 세부내용 불러올떄 각 상품코드에 맞는 가변값 불러오기
  List<ScriptRegistrationInfo> selectProductChangeList(ScriptRegistrationInfo scriptRegistrationInfo);
	
  //가변값 예약 테이블 rs_use_yn => N으로 변경
  int updateProductReserveRsUseYn(ScriptRegistrationInfo scriptRegistrationInfo);

  Map<String, JSONArray> selectScriptCommonCode(List<String> categories);
  
  //공용문구에서 사용할 수 있는 가변값 가져오기
  List<ScriptRegistrationInfo> selectValueComTTS(ScriptRegistrationInfo scriptRegistrationInfo);

  //관리자 상품조회
  List<ProductListVo> adminSelectProductList(ScriptRegistrationInfo info);
  //관리자 상품스탭조회
  List<ScriptStepVo> adminSelectScriptStep(String tKey);

  //관리자 상품스탭디테일조회
  List<AdminScriptStepDetailInfo> adminSelectScriptStepDetail(String tKey);

  RequestAdminScriptUpdateInfo adminScriptUpdateValidCheck(RequestAdminScriptUpdateInfo info);
  
  //상품업데이트 (관리자)
  int adminUpdateScriptDetail(RequestAdminScriptUpdateInfo info);

  
  int insertSinkTable(RequestSinkTableInfo sinkInfo) throws Exception;

  int deleteProductListToPk(String pk);

  //ta 상태 확인(api)
  List<ScriptRegistrationInfo> selectTaState(ScriptRegistrationInfo scriptRegistrationInfo);
}
 