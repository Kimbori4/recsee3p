package com.furence.recsee.evaluation.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;
import com.furence.recsee.evaluation.dao.EvaluationResultInfoDao;
import com.furence.recsee.evaluation.model.EvaluationResultInfo;
import com.furence.recsee.evaluation.model.SheetInfo;

@Repository("evaluationResultInfoDao")
public class EvaluationResultInfoDaoImpl implements EvaluationResultInfoDao {
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private SynchronizationService synchronizationService;

	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public List<EvaluationResultInfo> selectEvaluationResult(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectEvaluationResult(evaluationResultInfo);
	}

	@Override
	public Integer totalEvaluationResult(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.totalEvaluationResult(evaluationResultInfo);
	}

	@Override
	public List<EvaluationResultInfo> selectEvaluationResult2(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectEvaluationResult2(evaluationResultInfo);
	}

	@Override
	public Integer totalEvaluationResult2(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.totalEvaluationResult2(evaluationResultInfo);
	}

	@Override
	public List<SheetInfo> selectEvaluationSheet(SheetInfo sheetInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectEvaluationSheet(sheetInfo);
	}
	@Override
	public Integer updateResult(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateResult").getBoundSql(evaluationResultInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.updateResult(evaluationResultInfo);
	}
	
	@Override
	public Integer updateResultAgendSearchFlag(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateResultAgendSearchFlag").getBoundSql(evaluationResultInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.updateResultAgendSearchFlag(evaluationResultInfo);
	}
	
	@Override
	public Integer insertResult(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertResult").getBoundSql(evaluationResultInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertResult(evaluationResultInfo);
	}
	@Override
	public Integer updateResultEvalator(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateResultEvalator").getBoundSql(evaluationResultInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.updateResultEvalator(evaluationResultInfo);
	}
	@Override
	public List<EvaluationResultInfo> selectCheckEvalList(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectCheckEvalList(evaluationResultInfo);
	}
	@Override
	public Integer insertEvalItemResult(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertEvalItemResult").getBoundSql(evaluationResultInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertEvalItemResult(evaluationResultInfo);
	}
	// 수정
	@Override
	public Integer insertEvalReportResult(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertEvalReportResult").getBoundSql(evaluationResultInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertEvalReportResult(evaluationResultInfo);
	}
	@Override
	public Integer deleteEvalItemResult(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteEvalItemResult").getBoundSql(evaluationResultInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.deleteEvalItemResult(evaluationResultInfo);
	}
	// 수정
	@Override
	public Integer deleteEvalReportResult(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteEvalReportResult").getBoundSql(evaluationResultInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.deleteEvalReportResult(evaluationResultInfo);
	}
	@Override
	public Integer updateRejectStatus(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateRejectStatus").getBoundSql(evaluationResultInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.updateRejectStatus(evaluationResultInfo);
	}
	@Override
	public Integer updateEvaluatorAssign(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateEvaluatorAssign").getBoundSql(evaluationResultInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.updateEvaluatorAssign(evaluationResultInfo);
	}
	@Override
	public Integer deleteEvalResult(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteEvalResult").getBoundSql(evaluationResultInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.deleteEvalResult(evaluationResultInfo);
	}
	@Override
	public List<Map<String, Object>> selectCampaign(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectCampaign(map);
	}
	@Override
	public List<Map<String, Object>> selectAllCampaign(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectAllCampaign(map);
	}
	@Override
	public List<Map<String, Object>> selectLastCampaign(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectLastCampaign(map);
	}
	@Override
	public List<Map<String, Object>> selectEvaluator(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectEvaluator(map);
	}
	@Override
	public Integer insertCampaign(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertCampaign").getBoundSql(map);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertCampaign(map);
	}

	@Override
	public Integer insertRule(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertRule").getBoundSql(map);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertRule(map);
	}

	@Override
	public Integer insertObjection(Map<String, Object> map) {
				
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertRule").getBoundSql(map);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertObjection(map);
	}

	@Override
	public List<Map<String, Object>> selectRule(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectRule(map);
	}

	@Override
	public Integer deleteCampaign(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteCampaign").getBoundSql(map);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.deleteCampaign(map);
	}

	@Override
	public Integer deleteRule(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteRule").getBoundSql(map);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.deleteRule(map);
	}
	@Override
	public Integer deleteGroup(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteGroup").getBoundSql(map);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.deleteGroup(map);
	}
	@Override
	public Integer deleteAssign(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteAssign").getBoundSql(map);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.deleteAssign(map);
	}
	@Override
	public Integer updateCampaign(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateCampaign").getBoundSql(map);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.updateCampaign(map);
	}

	@Override
	public Integer updateRule(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateRule").getBoundSql(map);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.updateRule(map);
	}
	@Override
	public Map<String, Object> selectEcampVisible(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectEcampVisible(map);
	}
	@Override
	public Integer updateEcampVisible(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateEcampVisible").getBoundSql(map);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.updateEcampVisible(map);
	}
	@Override
	public Integer insertEcampVisible(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertEcampVisible").getBoundSql(map);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertEcampVisible(map);
	}
	@Override
	public Integer deleteEcampVisible(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteEcampVisible").getBoundSql(map);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.deleteEcampVisible(map);
	}
	@Override
	public List<Map<String, Object>> upSelectCampaign(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.upSelectCampaign(map);
	}
	@Override
	public List<Map<String, Object>> selectDeleteCampaign(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectDeleteCampaign(map);
	}

	@Override
	public List<Map<String, Object>> upSelectRule(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.upSelectRule(map);
	}
	@Override
	public List<Map<String, Object>> selectCampaignInfo(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectCampaignInfo(map);
	}
	@Override
	public List<Map<String, Object>> selectSgName(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectSgName(map);
	}
	@Override
	public List<Map<String, Object>> selectMgName(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectMgName(map);
	}
	@Override
	public List<Map<String, Object>> selectEvalDegree(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectEvalDegree(map);
	}
	@Override
	public List<Map<String, Object>> evaluationData(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.evaluationData(map);
	}
	@Override
	public List<Map<String, Object>> evaluationData2(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.evaluationData2(map);
	}
	@Override
	public List<Map<String, Object>> evaluationData3(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.evaluationData3(map);
	}
	@Override
	public List<Map<String, Object>> evaluationDataJob(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.evaluationDataJob(map);
	}
	@Override
	public List<Map<String, Object>> evaluationDataPart(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.evaluationDataPart(map);
	}
	@Override
	public List<Map<String, Object>> evaluationBrazilGroup(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.evaluationBrazilGroup(map);
	}
	@Override
	public List<Map<String, Object>> evaluationBrazilCategory(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.evaluationBrazilCategory(map);
	}
	@Override
	public Integer insertEcampSheet(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertEcampSheet").getBoundSql(map);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertEcampSheet(map);
	}

	@Override
	public List<Map<String, Object>> selectEcampSheet(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectEcampSheet(map);
	}
	@Override
	public List<Map<String, Object>> selectPersonGroup(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectPersonGroup(map);
	}
	@Override
	public List<Map<String, Object>> selectAffilicate(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectAffilicate(map);
	}

	@Override
	public List<Map<String, Object>> selectSkill(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectSkill(map);
	}

	@Override
	public Integer updateEcampSheet(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateEcampSheet").getBoundSql(map);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.updateEcampSheet(map);
	}

	@Override
	public Integer deleteEcampSheet(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteEcampSheet").getBoundSql(map);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.deleteEcampSheet(map);
	}

	@Override
	public Integer deleteTotalSheet(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteTotalSheet").getBoundSql(map);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.deleteTotalSheet(map);
	}

	@Override
	public List<Map<String, Object>> selectsGroup(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectsGroup(map);
	}
	@Override
	public List<Map<String, Object>> selectmGroup(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectmGroup(map);
	}
	@Override
	public List<Map<String, Object>> selectallGroup(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectallGroup(map);
	}

	@Override
	public List<Map<String, Object>> selectallPerson(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectallPerson(map);
	}
	@Override
	public Integer insertSgGroup(SheetInfo sheetinfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSgGroup").getBoundSql(sheetinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertSgGroup(sheetinfo);
	}

	@Override
	public Integer insertMgGroup(SheetInfo sheetinfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertMgGroup").getBoundSql(sheetinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertMgGroup(sheetinfo);
	}

	@Override
	public Integer insertBgGroup(SheetInfo sheetinfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertBgGroup").getBoundSql(sheetinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertBgGroup(sheetinfo);
	}
	@Override
	public Integer insertMgGroup2(SheetInfo sheetinfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertMgGroup2").getBoundSql(sheetinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertMgGroup2(sheetinfo);
	}
	@Override
	public Integer insertSgGroup2(SheetInfo sheetinfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSgGroup2").getBoundSql(sheetinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertSgGroup2(sheetinfo);
	}
	@Override
	public Integer insertPeopleGroup(SheetInfo sheetinfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertPeopleGroup").getBoundSql(sheetinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertPeopleGroup(sheetinfo);
	}
	@Override
	public Integer insertPeopleGroup2(SheetInfo sheetinfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertPeopleGroup2").getBoundSql(sheetinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertPeopleGroup2(sheetinfo);
	}
	@Override
	public Integer insertAssign(SheetInfo sheetinfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertAssign").getBoundSql(sheetinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertAssign(sheetinfo);
	}
	@Override
	public Integer insertAssign2(SheetInfo sheetinfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertAssign2").getBoundSql(sheetinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertAssign2(sheetinfo);
	}
	@Override
	public Integer insertSkillGroup(SheetInfo sheetinfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSkillGroup").getBoundSql(sheetinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertSkillGroup(sheetinfo);
	}
	@Override
	public Integer insertSkillGroup2(SheetInfo sheetinfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSkillGroup2").getBoundSql(sheetinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertSkillGroup2(sheetinfo);
	}
	@Override
	public Integer insertAffilicateGroup(SheetInfo sheetinfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertAffilicateGroup").getBoundSql(sheetinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertAffilicateGroup(sheetinfo);
	}
	@Override
	public Integer insertAffilicateGroup2(SheetInfo sheetinfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);;
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertAffilicateGroup2").getBoundSql(sheetinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return evaluationResultMapper.insertAffilicateGroup(sheetinfo);
	}
	@Override
	public List<EvaluationResultInfo> selectSearchEvalSheet(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectSearchEvalSheet(evaluationResultInfo);
	}
	@Override
	public List<EvaluationResultInfo> selectAssignCount(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectAssignCount(evaluationResultInfo);
	}
	@Override
	public List<EvaluationResultInfo> selectEvalScore(EvaluationResultInfo evaluationResultInfo) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectEvalScore(evaluationResultInfo);
	}	
	@Override
	public List<Map<String, Object>> selectlistenUrlEvalData(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectlistenUrlEvalData(map);
	}
	@Override
	public List<Map<String, Object>> selectEvaluatorList(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectlistenUrlEvalData(map);
	}
	@Override
	public List<Map<String, Object>> selectEvalListStatics(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectEvalListStatics(map);
	}
	@Override
	public List<Map<String, Object>> selectEvalTeamStatics(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectEvalTeamStatics(map);
	}
	@Override
	public List<Map<String, Object>> selectUseEvaluator(Map<String, Object> map) {
		EvaluationResultInfoDao evaluationResultMapper = sqlSession.getMapper(EvaluationResultInfoDao.class);
		return evaluationResultMapper.selectUseEvaluator(map);
	}	
}
