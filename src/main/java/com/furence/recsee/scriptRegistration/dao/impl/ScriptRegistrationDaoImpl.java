
package com.furence.recsee.scriptRegistration.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.scriptRegistration.dao.ScriptRegistrationDao;
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

@Repository("scriptRegistrationDao")
public class ScriptRegistrationDaoImpl implements ScriptRegistrationDao {

	@Autowired
	private SqlSession sqlSession;

	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	// product
	@Override
	public List<ScriptRegistrationInfo> selectProductList(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectProductList(scriptRegistrationInfo);
	}

	// product count
	@Override
	public Integer selectProductListCount(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectProductListCount(scriptRegistrationInfo);
	}

	// tree
	@Override
	public List<ScriptRegistrationInfo> selectScriptStepList(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectScriptStepList(scriptRegistrationInfo);
	}
	
	@Override
	public List<ScriptStepDetailVO> selectScriptStepDetailList(ScriptStepDetailDTO scriptStepDetailDTO) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectScriptStepDetailList(scriptStepDetailDTO);
	}

	// searchScript
	@Override
	public List<ScriptRegistrationInfo> searchScript(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.searchScript(scriptRegistrationInfo);
	}

	@Override
	public Integer updateScript(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.updateScript(scriptRegistrationInfo);
	}

	// script UPdate
	@Override
	public Integer updateScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.updateScriptDetail(scriptRegistrationInfo);
	}

	// common script UPdate
	@Override
	public Integer updateCommonScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.updateCommonScriptDetail(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> selectScriptStepHistoryList(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectScriptStepHistoryList(scriptRegistrationInfo);
	}

	@Override
	public Integer selectCountScriptHistroy(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectCountScriptHistroy(scriptRegistrationInfo);
	}

	@Override
	public Integer insertScriptStepHistory(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.insertScriptStepHistory(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> selectScriptStepHistoryTree(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectScriptStepHistoryTree(scriptRegistrationInfo);
	}

	@Override
	public Integer insertScriptDetailHistory(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.insertScriptDetailHistory(scriptRegistrationInfo);
	}

	@Override
	public Integer scriptDetailHistoryCount(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.scriptDetailHistoryCount(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> scriptDetailHistoryList(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.scriptDetailHistoryList(scriptRegistrationInfo);
	}

	@Override
	public Integer updateScriptStepHistory(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.updateScriptStepHistory(scriptRegistrationInfo);

	}

	@Override
	public List<ScriptRegistrationInfo> scriptDetailList(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.scriptDetailList(scriptRegistrationInfo);
	}

	// delete UPdate
	@Override
	public Integer deleteScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.deleteScriptDetail(scriptRegistrationInfo);
	}

	// common delete UPdate
	@Override
	public Integer deleteCommonScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.deleteCommonScriptDetail(scriptRegistrationInfo);
	}

	@Override
	public Integer addScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.addScriptDetail(scriptRegistrationInfo);
	}

	@Override
	public Integer addCommonScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.addCommonScriptDetail(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> searchCommonScriptDetail(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.searchCommonScriptDetail(scriptRegistrationInfo);
	}

	@Override
	public Integer deleteCommonScript(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.deleteCommonScript(scriptRegistrationInfo);
	}

	@Override
	public Integer insertCommonScript(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.insertCommonScript(scriptRegistrationInfo);
	}

	@Override
	public Integer updateCommonScript(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.updateCommonScript(scriptRegistrationInfo);
	}

	// select tree script
	@Override
	public List<ScriptRegistrationInfo> selectTreeScript(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectTreeScript(scriptRegistrationInfo);
	}

	// add tree script
	@Override
	public Integer addTreeScript(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.addTreeScript(scriptRegistrationInfo);
	}

	// delete tree script
	@Override
	public Integer deleteTreeScript(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.deleteTreeScript(scriptRegistrationInfo);
	}

	// edit tree script
	@Override
	public Integer editTreeScript(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.editTreeScript(scriptRegistrationInfo);

	}

	// 스크립트 TTS HISTORY 저장
	@Override
	public Integer insertScriptTTSHistory(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.insertScriptTTSHistory(scriptRegistrationInfo);
	}

	@Override
	public Integer insertScriptTTS(HashMap<String, Object> hashMap) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.insertScriptTTS(hashMap);
	}

	@Override
	public Integer selectProductListGroup(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectProductListGroup(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> selectScriptDetailToHistory(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectScriptDetailToHistory(scriptRegistrationInfo);

	}

	@Override
	public List<scriptProductValueInfo> selectProductValue(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectProductValue(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> selectGRProductList(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectGRProductList(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> selectRecState(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectRecState(scriptRegistrationInfo);
	}

	@Override
	public Integer insertRecParam(HashMap param) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.insertRecParam(param);
	}

	// script step테이블 stepOrder 알기 위함
	@Override
	public List<ScriptRegistrationInfo> selectScriptDetailPK(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectScriptDetailPK(scriptRegistrationInfo);
	}

	// script_value에서 TTS가 Y인 값 가져오기
	@Override
	public List<ScriptRegistrationInfo> selectValueTTS(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectValueTTS(scriptRegistrationInfo);
	}

	// 상품푸가

	@Override
	public Integer addproducList(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.addproducList(scriptRegistrationInfo);
	}

	@Override
	public String selectDetailFilePath(Integer rScriptStepFk) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectDetailFilePath(rScriptStepFk);
	}

	@Override
	public List<scriptProductValueInfo> selectValueTTS2(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectValueTTS2(scriptRegistrationInfo);
	}

	@Override
	public List<Integer> selectOneScriptStepPk(Integer pk) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectOneScriptStepPk(pk);
	}

	@Override
	public List<ScriptRegistrationInfo> searchScriptDblClick(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.searchScriptDblClick(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> selectTree_Script(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectTree_Script(scriptRegistrationInfo);
	}

	@Override
	public List<scriptProductValueInfo> selectValueFromScriptStepPk(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectValueFromScriptStepPk(scriptRegistrationInfo);
	}

	@Override
	public ScriptRegistrationInfo selectProductListPkfromProductCode(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectProductListPkfromProductCode(scriptRegistrationInfo);
	}

	@Override
	public List<scriptProductValueInfo> selectProductValue2(String productCode) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectProductValue2(productCode);
	}

	@Override
	public ScriptRegistrationInfo selectProductListOne(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectProductListOne(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> selectProductListGroupList(String productCode) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectProductListGroupList(productCode);
	}

	@Override
	public List<ScriptRegistrationInfo> selectProductValueJoinProductList(ScriptRegistrationInfo info) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectProductValueJoinProductList(info);
	}

	@Override
	public List<ScriptRegistrationInfo> selectRsProductReserveFromProductCode(
			ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectRsProductReserveFromProductCode(scriptRegistrationInfo);
	}

	@Override
	public int insertRsProductReserve(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.insertRsProductReserve(scriptRegistrationInfo);
	}

	@Override
	public int updateProdcutReservceDate(ScriptRegistrationInfo originalData) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.updateProdcutReservceDate(originalData);

	}

	@Override
	public int updateProductValueNameAndVal(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.updateProductValueNameAndVal(scriptRegistrationInfo);
	}

	@Override
	public List<ScriptRegistrationInfo> selectProductChangeList(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectProductChangeList(scriptRegistrationInfo);
	}

	@Override
	public int updateProductReserveRsUseYn(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.updateProductReserveRsUseYn(scriptRegistrationInfo);
	}

	
	@Override
	public List<String> selectScriptCommonCode(List<String> categories) {
		// TODO Auto-generated method stub
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectScriptCommonCode(categories);
	}

	//공용스크립트 관리 가변값 가져오기
	@Override
	public List<ScriptRegistrationInfo> selectValueComTTS(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectValueComTTS(scriptRegistrationInfo);
	}

	@Override
	public List<ProductListVo> adminSelectProductList(ScriptRegistrationInfo info) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.adminSelectProductList(info);
	}

	@Override
	public List<ScriptStepVo> adminSelectScriptStep(String tKey) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.adminSelectScriptStep(tKey);
	}

	@Override
	public List<AdminScriptStepDetailInfo> adminSelectScriptStepDetail(String tKey) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.adminSelectScriptStepDetail(tKey);
	}

	@Override
	public int adminUpdateScriptDetail(RequestAdminScriptUpdateInfo info) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.adminUpdateScriptDetail(info);
	}

	@Override
	public int insertSinkTable(RequestSinkTableInfo sinkInfo) throws Exception {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.insertSinkTable(sinkInfo);
	}

	@Override
	public int deleteProductListToPk(String pk) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.deleteProductListToPk(pk);
	}
	
	@Override
	public List<ScriptRegistrationInfo> selectTaState(ScriptRegistrationInfo scriptRegistrationInfo) {
		ScriptRegistrationDao scriptRegistrationmapper = (ScriptRegistrationDao) sqlSession
				.getMapper(ScriptRegistrationDao.class);
		return scriptRegistrationmapper.selectTaState(scriptRegistrationInfo);
	}
}
