package com.furence.recsee.evaluation.dao;

import java.util.List;
import java.util.Map;

import com.furence.recsee.evaluation.model.EvaluationResultInfo;
import com.furence.recsee.evaluation.model.SheetInfo;

public interface EvaluationResultInfoDao {
	// 평가된 리스트 조회
	List<EvaluationResultInfo> selectEvaluationResult(EvaluationResultInfo evaluationResultInfo);
	List<EvaluationResultInfo> selectEvaluationResult2(EvaluationResultInfo evaluationResultInfo);
	Integer totalEvaluationResult(EvaluationResultInfo evaluationResultInfo);
	Integer totalEvaluationResult2(EvaluationResultInfo evaluationResultInfo);
	// 시트 코드로 항목 조회
	List<SheetInfo> selectEvaluationSheet(SheetInfo sheetInfo);
	// 항목 선택(마크) 값 추가
	Integer insertResult(EvaluationResultInfo evaluationResultInfo);
	Integer updateResultEvalator(EvaluationResultInfo evaluationResultInfo);
	List<EvaluationResultInfo> selectCheckEvalList(EvaluationResultInfo evaluationResultInfo);
	// 항목 선택(마크) 코드별 값 추가
	Integer insertEvalItemResult(EvaluationResultInfo evaluationResultInfo);
	Integer deleteEvalItemResult(EvaluationResultInfo evaluationResultInfo);
	Integer updateRejectStatus(EvaluationResultInfo evaluationResultInfo);
	Integer updateEvaluatorAssign(EvaluationResultInfo evaluationResultInfo);
	Integer deleteEvalResult(EvaluationResultInfo evaluationResultInfo);
	Integer insertEvalReportResult(EvaluationResultInfo evaluationResultInfo); // 수정
	Integer deleteEvalReportResult(EvaluationResultInfo evaluationResultInfo);	// 수정
	// 항목 선택(마크) 값 업데이트
	Integer updateResult(EvaluationResultInfo evaluationResultInfo);

	Integer updateResultAgendSearchFlag(EvaluationResultInfo evaluationResultInfo);
	
	//캠페인 리스트 select
	List<Map<String, Object>> selectCampaign(Map<String, Object> map);
	List<Map<String, Object>> selectAllCampaign(Map<String, Object> map);
	List<Map<String, Object>> selectLastCampaign(Map<String, Object> map);
	List<Map<String, Object>> selectEvaluator(Map<String, Object> map);
	//캠페인 생성 -  캠페인  추가
	Integer insertCampaign(Map<String, Object> map);
	//룰 리스트 select
	List<Map<String, Object>> selectRule(Map<String, Object> map);
	//룰 추가
	Integer insertRule(Map<String, Object> map);
	Integer insertObjection(Map<String, Object> map);
	//캠페인 삭제
	Integer deleteCampaign(Map<String, Object> map);
	//룰 삭제
	Integer deleteRule(Map<String, Object> map);
	Integer deleteGroup(Map<String, Object> map);
	Integer deleteAssign(Map<String, Object> map);
	//캠페인 수정
	Integer updateCampaign(Map<String, Object> map);
	//룰 수정
	Integer updateRule(Map<String, Object> map);
	
	Map<String, Object> selectEcampVisible(Map<String, Object> map);
	//캠페인 활성화 업데이트
	Integer updateEcampVisible(Map<String, Object> map);
	//캠페인 활성화 생성
	Integer insertEcampVisible(Map<String, Object> map);
	//캠페인 활성화 삭제
	Integer deleteEcampVisible(Map<String, Object> map);
	//수정버튼 시 불러올 캠페인
	List<Map<String, Object>> upSelectCampaign(Map<String, Object> map);
	List<Map<String, Object>> selectDeleteCampaign(Map<String, Object> map);
	//수정 버튼 시 불러올 룰
	List<Map<String, Object>> upSelectRule(Map<String, Object> map);
	List<Map<String, Object>> selectCampaignInfo(Map<String, Object> map);
	List<Map<String, Object>> selectMgName(Map<String, Object> map);
	List<Map<String, Object>> selectSgName(Map<String, Object> map);
	List<Map<String, Object>> selectEvalDegree(Map<String, Object> map);
	List<Map<String, Object>> evaluationData(Map<String, Object> map);
	List<Map<String, Object>> evaluationData2(Map<String, Object> map);
	List<Map<String, Object>> evaluationData3(Map<String, Object> map);
	List<Map<String, Object>> evaluationDataJob(Map<String, Object> map);
	List<Map<String, Object>> evaluationDataPart(Map<String, Object> map);
	List<Map<String, Object>> evaluationBrazilGroup(Map<String, Object> map);
	List<Map<String, Object>> evaluationBrazilCategory(Map<String, Object> map);
	//평가 캠페인 만들기에서 추가 버튼 시 추가 되는 시트
	Integer insertEcampSheet(Map<String, Object> map);
	//추가 한 시트 평가 캠페인 만들기에 뿌리기
	List<Map<String, Object>> selectEcampSheet(Map<String, Object> map);
	List<Map<String, Object>> selectPersonGroup(Map<String, Object> map);
	List<Map<String, Object>> selectAffilicate(Map<String, Object> map);
	List<Map<String, Object>> selectSkill(Map<String, Object> map);
	//평가 캠페인 생성 시 시트의 캠페인 코드 업데이트
	Integer updateEcampSheet(Map<String, Object> map);
	//평가 캠페인 내 시트 삭제
	Integer deleteEcampSheet(Map<String, Object> map);
	//캠페인 지울 때 시트도 같이 삭제
	Integer deleteTotalSheet(Map<String, Object> map);
	List<Map<String, Object>> selectsGroup(Map<String, Object> map);
	List<Map<String, Object>> selectmGroup(Map<String, Object> map);
	List<Map<String, Object>> selectallGroup(Map<String, Object> map);
	List<Map<String, Object>> selectallPerson(Map<String, Object> map);
	Integer insertBgGroup(SheetInfo sheetinfo);
	Integer insertSgGroup(SheetInfo sheetinfo);
	Integer insertMgGroup(SheetInfo sheetinfo);
	Integer insertMgGroup2(SheetInfo sheetinfo);
	Integer insertSgGroup2(SheetInfo sheetinfo);
	Integer insertPeopleGroup(SheetInfo sheetinfo);
	Integer insertPeopleGroup2(SheetInfo sheetinfo);
	Integer insertAssign(SheetInfo sheetinfo);
	Integer insertAssign2(SheetInfo sheetinfo);
	Integer insertSkillGroup(SheetInfo sheetinfo);
	Integer insertSkillGroup2(SheetInfo sheetinfo);
	Integer insertAffilicateGroup(SheetInfo sheetinfo);
	Integer insertAffilicateGroup2(SheetInfo sheetinfo);

	List<EvaluationResultInfo> selectSearchEvalSheet(EvaluationResultInfo evaluationResultInfo);
	List<EvaluationResultInfo> selectAssignCount(EvaluationResultInfo evaluationResultInfo);
	List<EvaluationResultInfo> selectEvalScore(EvaluationResultInfo evaluationResultInfo);
	
	//listenUrl로 조회하기
	List<Map<String, Object>> selectlistenUrlEvalData(Map<String, Object> map);
	
	//평가결과 테이블에서 평가자들 가져오기
	List<Map<String, Object>> selectEvaluatorList(Map<String, Object> map);
	List<Map<String, Object>> selectEvalListStatics(Map<String, Object> map);
	List<Map<String, Object>> selectEvalTeamStatics(Map<String, Object> map);
	
	//평가자 조회해오기
	List<Map<String, Object>> selectUseEvaluator(Map<String, Object> map);
}
