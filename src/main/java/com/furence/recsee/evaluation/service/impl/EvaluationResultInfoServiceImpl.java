package com.furence.recsee.evaluation.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.evaluation.dao.EvaluationResultInfoDao;
import com.furence.recsee.evaluation.model.EvaluationResultInfo;
import com.furence.recsee.evaluation.model.SheetInfo;
import com.furence.recsee.evaluation.service.EvaluationResultInfoService;

@Service("evaluationResultInfoService")
public class EvaluationResultInfoServiceImpl implements EvaluationResultInfoService {

	@Autowired
	EvaluationResultInfoDao evaluationResultMapper;

	@Override
	public Integer totalEvaluationResult(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.totalEvaluationResult(evaluationResultInfo);
	}
	@Override
	public List<EvaluationResultInfo> selectSearchEvalSheet(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.selectSearchEvalSheet(evaluationResultInfo);
	}
	@Override
	public List<EvaluationResultInfo> selectAssignCount(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.selectAssignCount(evaluationResultInfo);
	}
	@Override
	public List<EvaluationResultInfo> selectEvalScore(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.selectEvalScore(evaluationResultInfo);
	}
	@Override
	public List<EvaluationResultInfo> selectEvaluationResult(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.selectEvaluationResult(evaluationResultInfo);
	}
	@Override
	public List<EvaluationResultInfo> selectEvaluationResult2(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.selectEvaluationResult2(evaluationResultInfo);
	}
	@Override
	public Integer totalEvaluationResult2(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.totalEvaluationResult2(evaluationResultInfo);
	}
	// 수정
	@Override
	public Integer insertEvalReportResult(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.insertEvalReportResult(evaluationResultInfo);
	}
	// 수정
	@Override
	public Integer deleteEvalReportResult(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.deleteEvalReportResult(evaluationResultInfo);
	}
	@Override
	public List<SheetInfo> selectEvaluationSheet(SheetInfo sheetInfo) {
		return evaluationResultMapper.selectEvaluationSheet(sheetInfo);
	}
	@Override
	public Integer insertResult(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.insertResult(evaluationResultInfo);
	}
	@Override
	public Integer updateResultEvalator(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.updateResultEvalator(evaluationResultInfo);
	}
	@Override
	public List<EvaluationResultInfo> selectCheckEvalList(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.selectCheckEvalList(evaluationResultInfo);
	}
	@Override
	public Integer insertEvalItemResult(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.insertEvalItemResult(evaluationResultInfo);
	}
	@Override
	public Integer updateRejectStatus(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.updateRejectStatus(evaluationResultInfo);
	}
	@Override
	public Integer deleteEvalItemResult(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.deleteEvalItemResult(evaluationResultInfo);
	}
	@Override
	public Integer updateEvaluatorAssign(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.updateEvaluatorAssign(evaluationResultInfo);
	}
	@Override
	public Integer deleteEvalResult(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.deleteEvalResult(evaluationResultInfo);
	}
	@Override
	public Integer updateResult(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.updateResult(evaluationResultInfo);
	}
	@Override
	public Integer updateResultAgendSearchFlag(EvaluationResultInfo evaluationResultInfo) {
		return evaluationResultMapper.updateResultAgendSearchFlag(evaluationResultInfo);
	}
	
	@Override
	public List<Map<String, Object>> selectCampaign(Map<String, Object> map) {
		return evaluationResultMapper.selectCampaign(map);
	}
	@Override
	public List<Map<String, Object>> selectAllCampaign(Map<String, Object> map) {
		return evaluationResultMapper.selectAllCampaign(map);
	}
	@Override
	public List<Map<String, Object>> selectLastCampaign(Map<String, Object> map) {
		return evaluationResultMapper.selectLastCampaign(map);
	}
	@Override
	public List<Map<String, Object>> selectEvaluator(Map<String, Object> map) {
		return evaluationResultMapper.selectEvaluator(map);
	}
	@Override
	public Integer insertCampaign(Map<String, Object> map) {
		return evaluationResultMapper.insertCampaign(map);
	}
	@Override
	public Integer insertRule(Map<String, Object> map) {
		return evaluationResultMapper.insertRule(map);
	}
	@Override
	public Integer insertObjection(Map<String, Object> map) {
		return evaluationResultMapper.insertObjection(map);
	}
	@Override
	public List<Map<String, Object>> selectRule(Map<String, Object> map) {
		return evaluationResultMapper.selectRule(map);
	}
	@Override
	public Integer deleteCampaign(Map<String, Object> map) {
		return evaluationResultMapper.deleteCampaign(map);
	}
	@Override
	public Integer deleteRule(Map<String, Object> map) {
		return evaluationResultMapper.deleteRule(map);
	}
	@Override
	public Integer deleteGroup(Map<String, Object> map) {
		return evaluationResultMapper.deleteGroup(map);
	}
	@Override
	public Integer deleteAssign(Map<String, Object> map) {
		return evaluationResultMapper.deleteAssign(map);
	}
	@Override
	public Integer updateCampaign(Map<String, Object> map) {
		return evaluationResultMapper.updateCampaign(map);
	}
	@Override
	public Integer updateRule(Map<String, Object> map) {
		return evaluationResultMapper.updateRule(map);
	}
	@Override
	public Map<String, Object> selectEcampVisible(Map<String, Object> map) {
		return evaluationResultMapper.selectEcampVisible(map);
	}
	@Override
	public Integer updateEcampVisible(Map<String, Object> map) {
		return evaluationResultMapper.updateEcampVisible(map);
	}
	@Override
	public Integer insertEcampVisible(Map<String, Object> map) {
		return evaluationResultMapper.insertEcampVisible(map);
	}
	@Override
	public Integer deleteEcampVisible(Map<String, Object> map) {
		return evaluationResultMapper.deleteEcampVisible(map);
	}
	@Override
	public List<Map<String, Object>> upSelectCampaign(Map<String, Object> map) {
		return evaluationResultMapper.upSelectCampaign(map);
	}
	@Override
	public List<Map<String, Object>> selectDeleteCampaign(Map<String, Object> map) {
		return evaluationResultMapper.selectDeleteCampaign(map);
	}
	@Override
	public List<Map<String, Object>> upSelectRule(Map<String, Object> map) {
		return evaluationResultMapper.upSelectRule(map);
	}
	@Override
	public List<Map<String, Object>> selectCampaignInfo(Map<String, Object> map) {
		return evaluationResultMapper.selectCampaignInfo(map);
	}
	@Override
	public List<Map<String, Object>> selectSgName(Map<String, Object> map) {
		return evaluationResultMapper.selectSgName(map);
	}
	@Override
	public List<Map<String, Object>> selectMgName(Map<String, Object> map) {
		return evaluationResultMapper.selectMgName(map);
	}
	@Override
	public List<Map<String, Object>> selectEvalDegree(Map<String, Object> map) {
		return evaluationResultMapper.selectEvalDegree(map);
	}
	@Override
	public List<Map<String, Object>> evaluationData(Map<String, Object> map) {
		return evaluationResultMapper.evaluationData(map);
	}
	@Override
	public List<Map<String, Object>> evaluationData2(Map<String, Object> map) {
		return evaluationResultMapper.evaluationData2(map);
	}
	@Override
	public List<Map<String, Object>> evaluationData3(Map<String, Object> map) {
		return evaluationResultMapper.evaluationData3(map);
	}
	@Override
	public List<Map<String, Object>> evaluationDataJob(Map<String, Object> map) {
		return evaluationResultMapper.evaluationDataJob(map);
	}
	@Override
	public List<Map<String, Object>> evaluationDataPart(Map<String, Object> map) {
		return evaluationResultMapper.evaluationDataPart(map);
	}
	@Override
	public List<Map<String, Object>> evaluationBrazilGroup(Map<String, Object> map) {
		return evaluationResultMapper.evaluationBrazilGroup(map);
	}
	@Override
	public List<Map<String, Object>> evaluationBrazilCategory(Map<String, Object> map) {
		return evaluationResultMapper.evaluationBrazilCategory(map);
	}
	@Override
	public Integer insertEcampSheet(Map<String, Object> map) {
		return evaluationResultMapper.insertEcampSheet(map);
	}
	@Override
	public List<Map<String, Object>> selectEcampSheet(Map<String, Object> map) {
		return evaluationResultMapper.selectEcampSheet(map);
	}
	@Override
	public List<Map<String, Object>> selectPersonGroup(Map<String, Object> map) {
		return evaluationResultMapper.selectPersonGroup(map);
	}
	@Override
	public List<Map<String, Object>> selectAffilicate(Map<String, Object> map) {
		return evaluationResultMapper.selectAffilicate(map);
	}
	@Override
	public List<Map<String, Object>> selectSkill(Map<String, Object> map) {
		return evaluationResultMapper.selectSkill(map);
	}
	@Override
	public Integer updateEcampSheet(Map<String, Object> map) {
		return evaluationResultMapper.updateEcampSheet(map);
	}
	@Override
	public Integer deleteEcampSheet(Map<String, Object> map) {
		return evaluationResultMapper.deleteEcampSheet(map);
	}
	@Override
	public Integer deleteTotalSheet(Map<String, Object> map) {
		return evaluationResultMapper.deleteTotalSheet(map);
	}
	@Override
	public List<Map<String, Object>> selectsGroup(Map<String, Object> map) {
		return evaluationResultMapper.selectsGroup(map);
	}
	@Override
	public List<Map<String, Object>> selectmGroup(Map<String, Object> map) {
		return evaluationResultMapper.selectmGroup(map);
	}
	@Override
	public List<Map<String, Object>> selectallGroup(Map<String, Object> map) {
		return evaluationResultMapper.selectallGroup(map);
	}
	@Override
	public List<Map<String, Object>> selectallPerson(Map<String, Object> map) {
		return evaluationResultMapper.selectallPerson(map);
	}
	@Override
	public Integer insertBgGroup(SheetInfo sheetinfo) {
		return evaluationResultMapper.insertBgGroup(sheetinfo);
	}
	@Override
	public Integer insertMgGroup(SheetInfo sheetinfo) {
		return evaluationResultMapper.insertMgGroup(sheetinfo);
	}
	@Override
	public Integer insertSgGroup(SheetInfo sheetinfo) {
		return evaluationResultMapper.insertSgGroup(sheetinfo);
	}
	@Override
	public Integer insertMgGroup2(SheetInfo sheetinfo) {
		return evaluationResultMapper.insertMgGroup2(sheetinfo);
	}
	@Override
	public Integer insertSgGroup2(SheetInfo sheetinfo) {
		return evaluationResultMapper.insertSgGroup2(sheetinfo);
	}
	@Override
	public Integer insertPeopleGroup(SheetInfo sheetinfo) {
		return evaluationResultMapper.insertPeopleGroup(sheetinfo);
	}
	@Override
	public Integer insertPeopleGroup2(SheetInfo sheetinfo) {
		return evaluationResultMapper.insertPeopleGroup2(sheetinfo);
	}

	@Override
	public Integer insertAssign(SheetInfo sheetinfo) {
		return evaluationResultMapper.insertAssign(sheetinfo);
	}
	@Override
	public Integer insertAssign2(SheetInfo sheetinfo) {
		return evaluationResultMapper.insertAssign2(sheetinfo);
	}
	@Override
	public Integer insertSkillGroup(SheetInfo sheetinfo) {
		return evaluationResultMapper.insertSkillGroup(sheetinfo);
	}
	@Override
	public Integer insertSkillGroup2(SheetInfo sheetinfo) {
		return evaluationResultMapper.insertSkillGroup2(sheetinfo);
	}
	@Override
	public Integer insertAffilicateGroup(SheetInfo sheetinfo) {
		return evaluationResultMapper.insertAffilicateGroup(sheetinfo);
	}
	@Override
	public Integer insertAffilicateGroup2(SheetInfo sheetinfo) {
		return evaluationResultMapper.insertAffilicateGroup2(sheetinfo);
	}
	@Override
	public List<Map<String, Object>> selectlistenUrlEvalData(Map<String, Object> map) {
		return evaluationResultMapper.selectlistenUrlEvalData(map);
	}
	@Override
	public List<Map<String, Object>> selectEvaluatorList(Map<String, Object> map) {
		return evaluationResultMapper.selectEvaluatorList(map);
	}
	@Override
	public List<Map<String, Object>> selectEvalListStatics(Map<String, Object> map) {
		return evaluationResultMapper.selectEvalListStatics(map);
	}
	@Override
	public List<Map<String, Object>> selectEvalTeamStatics(Map<String, Object> map) {
		return evaluationResultMapper.selectEvalTeamStatics(map);
	}
	@Override
	public List<Map<String, Object>> selectUseEvaluator(Map<String, Object> map) {
		return evaluationResultMapper.selectUseEvaluator(map);
	}
}
