
package com.furence.recsee.scriptRegistration.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.util.JsonUtil;
import com.furence.recsee.scriptRegistration.dao.ScriptRegistrationDao;
import com.furence.recsee.scriptRegistration.model.AdminScriptStepDetailInfo;
import com.furence.recsee.scriptRegistration.model.ScriptRegistrationInfo;
import com.furence.recsee.scriptRegistration.model.ScriptStepDetailDTO;
import com.furence.recsee.scriptRegistration.model.ScriptStepDetailVO;
import com.furence.recsee.scriptRegistration.model.scriptProductValueInfo;
import com.furence.recsee.scriptRegistration.service.ScriptRegistrationService;
import com.furence.recsee.wooribank.facerecording.model.ProductListVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepVo;
import com.furence.recsee.wooribank.facerecording.request.RequestAdminScriptUpdateInfo;
import com.furence.recsee.wooribank.facerecording.request.RequestSinkTableInfo;

@Service("scriptRegistrationService")
public class ScriptRegistrationServiceImpl implements ScriptRegistrationService {

	private static final Logger logger = LoggerFactory.getLogger(ScriptRegistrationServiceImpl.class);

	
	@Autowired
	ScriptRegistrationDao scriptRegistrationmapper;

	// product
	@Override
	public List<ScriptRegistrationInfo> selectProductList(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectProductList(scriptRegistrationInfo);
	}

	// product
	@Override
	public Integer selectProductListCount(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectProductListCount(scriptRegistrationInfo);
	}

	// tree
	@Override
	public List<ScriptRegistrationInfo> selectScriptStepList(ScriptRegistrationInfo scriptRegistrationInfo) {

		return scriptRegistrationmapper.selectScriptStepList(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptStepDetailVO> selectScriptStepDetailList(ScriptStepDetailDTO scriptStepDetailDTO) {
		return scriptRegistrationmapper.selectScriptStepDetailList(scriptStepDetailDTO);
	}

	// searchScript
	@Override
	public List<ScriptRegistrationInfo> searchScript(ScriptRegistrationInfo scriptRegistrationInfo) {

		return scriptRegistrationmapper.searchScript(scriptRegistrationInfo);
	}

	@Override
	public Integer updateScript(ScriptRegistrationInfo scriptRegistrationInfo) {

		return scriptRegistrationmapper.updateScript(scriptRegistrationInfo);
	}

	// script UPdate
	@Override
	public Integer updateScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.updateScriptDetail(scriptRegistrationInfo);
	}

	// script common UPdate
	@Override
	public Integer updateCommonScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.updateCommonScriptDetail(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> selectScriptStepHistoryList(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectScriptStepHistoryList(scriptRegistrationInfo);
	}

	@Override
	public Integer selectCountScriptHistroy(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectCountScriptHistroy(scriptRegistrationInfo);
	}

	@Override
	public Integer insertScriptStepHistory(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.insertScriptStepHistory(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> selectScriptStepHistoryTree(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectScriptStepHistoryTree(scriptRegistrationInfo);
	}

	@Override
	public Integer insertScriptDetailHistory(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.insertScriptDetailHistory(scriptRegistrationInfo);
	}

	@Override
	public Integer scriptDetailHistoryCount(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.scriptDetailHistoryCount(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> scriptDetailHistoryList(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.scriptDetailHistoryList(scriptRegistrationInfo);
	}

	@Override
	public Integer updateScriptStepHistory(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.updateScriptStepHistory(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> scriptDetailList(ScriptRegistrationInfo scriptRegistrationInfo) {

		return scriptRegistrationmapper.scriptDetailList(scriptRegistrationInfo);
	}

	// script delete
	@Override
	public Integer deleteScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.deleteScriptDetail(scriptRegistrationInfo);
	}

	// script common delete
	@Override
	public Integer deleteCommonScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.deleteCommonScriptDetail(scriptRegistrationInfo);
	}

	// script add
	@Override
	public Integer addScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.addScriptDetail(scriptRegistrationInfo);
	}

	// script common add
//	@Override
//	public Integer addCommonScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo) {
//		return scriptRegistrationmapper.addCommonScriptDetail(scriptRegistrationInfo); 
//	}

	// script common search
	@Override
	public List<ScriptRegistrationInfo> searchCommonScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.searchCommonScriptDetail(scriptRegistrationInfo);
	}

	// script common delete
	@Override
	public Integer deleteCommonScript(ScriptRegistrationInfo scriptRegistrationInfo) {
		// TODO Auto-generated method stub
		return scriptRegistrationmapper.deleteCommonScript(scriptRegistrationInfo);
	}

	// insert common delete
	@Override
	public Integer insertCommonScript(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.insertCommonScript(scriptRegistrationInfo);
	}

	// update common delete
	@Override
	public Integer updateCommonScript(ScriptRegistrationInfo scriptRegistrationInfo) {
		// TODO Auto-generated method stub
		return scriptRegistrationmapper.updateCommonScript(scriptRegistrationInfo);
	}

	// select tree script
	@Override
	public List<ScriptRegistrationInfo> selectTreeScript(ScriptRegistrationInfo scriptRegistrationInfo) {

		return scriptRegistrationmapper.selectTreeScript(scriptRegistrationInfo);
	}

	// add tree script
	@Override
	public Integer addTreeScript(ScriptRegistrationInfo scriptRegistrationInfo) {

		return scriptRegistrationmapper.addTreeScript(scriptRegistrationInfo);
	}

	// delete tree script
	@Override
	public Integer deleteTreeScript(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.deleteTreeScript(scriptRegistrationInfo);
	}

	// edit tree script
	@Override
	public Integer editTreeScript(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.editTreeScript(scriptRegistrationInfo);
	}

	// 스크립트 TTS HISTORY 저장
	@Override
	public Integer insertScriptTTSHistory(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.insertScriptTTSHistory(scriptRegistrationInfo);
	}

	@Override
	public Integer insertScriptTTS(HashMap<String, Object> hashMap) {
		return scriptRegistrationmapper.insertScriptTTS(hashMap);
	}

	@Override
	public Integer selectProductListGroup(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectProductListGroup(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> selectScriptDetailToHistory(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectScriptDetailToHistory(scriptRegistrationInfo);
	}

	@Override
	public List<scriptProductValueInfo> selectProductValue(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectProductValue(scriptRegistrationInfo);

	}

	@Override
	public List<ScriptRegistrationInfo> selectGRProductList(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectGRProductList(scriptRegistrationInfo);
	}
	// script step테이블 stepOrder 알기 위함ㄴ

	@Override
	public List<ScriptRegistrationInfo> selectScriptDetailPK(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectScriptDetailPK(scriptRegistrationInfo);
	}

	// script_value에서 TTS가 Y인 값 가져오기
	@Override
	public List<ScriptRegistrationInfo> selectValueTTS(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectValueTTS(scriptRegistrationInfo);
	}

	@Override
	public String selectDetailFilePath(Integer rScriptStepFk) {
		return scriptRegistrationmapper.selectDetailFilePath(rScriptStepFk);
	}

	@Override
	public List<ScriptRegistrationInfo> selectRecState(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectRecState(scriptRegistrationInfo);
	}

	@Override
	public Integer insertRecParam(HashMap param) {
		return scriptRegistrationmapper.insertRecParam(param);
	}

	@Override
	public List<scriptProductValueInfo> selectValueTTS2(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectValueTTS2(scriptRegistrationInfo);
	}

	// 상품추가
	@Override
	public Integer addproducList(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.addproducList(scriptRegistrationInfo);
	}

	// 디테일 pk 한건
	@Override
	public List<Integer> selectOneScriptStepPk(Integer pk) {
		return scriptRegistrationmapper.selectOneScriptStepPk(pk);
	}

	@Override
	public List<ScriptRegistrationInfo> searchScriptDblClick(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.searchScriptDblClick(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> selectTree_Script(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectTree_Script(scriptRegistrationInfo);

	}

	@Override
	public List<scriptProductValueInfo> selectValueFromScriptStepPk(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectValueFromScriptStepPk(scriptRegistrationInfo);
	}

	@Override
	public ScriptRegistrationInfo selectProductListPkfromProductCode(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectProductListPkfromProductCode(scriptRegistrationInfo);
	}

	@Override
	public List<scriptProductValueInfo> selectProductValue2(String productCode) {
		return scriptRegistrationmapper.selectProductValue2(productCode);
	}

	@Override
	public ScriptRegistrationInfo selectProductListOne(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectProductListOne(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> selectProductListGroupList(String productCode) {
		return scriptRegistrationmapper.selectProductListGroupList(productCode);
	}

	@Override
	public List<ScriptRegistrationInfo> selectProductValueJoinProductList(ScriptRegistrationInfo info) {
		return scriptRegistrationmapper.selectProductValueJoinProductList(info);
	}

	// 스크립트 세부내용 불러올떄 각 상품코드에 맞는 가변값 불러오기

	@Override
	public List<ScriptRegistrationInfo> selectProductChangeList(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectProductChangeList(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> selectRsProductReserveFromProductCode(
			ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectRsProductReserveFromProductCode(scriptRegistrationInfo);
	}

	@Override
	public int insertRsProductReserve(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.insertRsProductReserve(scriptRegistrationInfo);
	}

	@Override
	public int updateProdcutReservceDate(ScriptRegistrationInfo originalData) {
		return scriptRegistrationmapper.updateProdcutReservceDate(originalData);
	}

	@Override
	public int updateProductValueNameAndVal(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.updateProductValueNameAndVal(scriptRegistrationInfo);
	}

	@Override
	public int updateProductReserveRsUseYn(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.updateProductReserveRsUseYn(scriptRegistrationInfo);
	}

	@Override
	public Map<String, JSONArray > selectScriptCommonCode(List<String> categories) {
		
	
		try {
			
			List<String> codeList =  scriptRegistrationmapper.selectScriptCommonCode(categories);
			
			if (!codeList.isEmpty()) {
			
				Map<String , JSONArray > codeMap= new HashMap<>();
				
				JSONParser parser = new JSONParser();				
				Iterator<String> iter = codeList.iterator();
				while(iter.hasNext()) {
					String jsonStr = iter.next();
					JSONObject jsonObj = (JSONObject)parser.parse(jsonStr);
					
					String category = (String)jsonObj.get("category");
					JSONArray items = (JSONArray)jsonObj.get("items");
					logger.info("{}:{}", category ,items );
					
					JsonUtil.removeNullFrom(items);
					
					codeMap.put(category, items);
				}
				
				return codeMap;
			}
			
		} catch ( Exception e) {
			logger.error("error", e);			
		}
		return null;
	}

	@Override
	public List<ScriptRegistrationInfo> selectValueComTTS(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectValueComTTS(scriptRegistrationInfo);
	}

	@Override
	public List<ProductListVo> adminSelectProductList(ScriptRegistrationInfo info) {
		
		List<ProductListVo> list = Optional.of(scriptRegistrationmapper.adminSelectProductList(info)).orElse(Collections.emptyList());
		return list;
	}

	@Override
	public List<ScriptStepVo> adminSelectScriptStep(String tKey) {
		List<ScriptStepVo> list = Optional.of(scriptRegistrationmapper.adminSelectScriptStep(tKey)).orElse(Collections.emptyList());
		return list;
	}

	@Override
	public List<AdminScriptStepDetailInfo> adminSelectScriptStepDetail(String tKey) {
		List<AdminScriptStepDetailInfo> list = Optional.of(scriptRegistrationmapper.adminSelectScriptStepDetail(tKey)).orElse(Collections.emptyList());
		return list;
	}

	@Override
	public RequestAdminScriptUpdateInfo adminScriptUpdateValidCheck(RequestAdminScriptUpdateInfo info) {
		
		return info;
	}

	@Override
	public int adminUpdateScriptDetail(RequestAdminScriptUpdateInfo info) {
		
		int result = scriptRegistrationmapper.adminUpdateScriptDetail(info);
		return result;
	}

	@Override
	public int insertSinkTable(RequestSinkTableInfo sinkInfo) throws Exception {

		int result = scriptRegistrationmapper.insertSinkTable(sinkInfo);
		
		return 0;
	}

	@Override
	public int deleteProductListToPk(String pk) {
		int result = scriptRegistrationmapper.deleteProductListToPk(pk);
		return 0;
	}

	@Override
	public List<ScriptRegistrationInfo> selectTaState(ScriptRegistrationInfo scriptRegistrationInfo) {
		return scriptRegistrationmapper.selectTaState(scriptRegistrationInfo);
	}
}
