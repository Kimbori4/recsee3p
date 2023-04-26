package com.furence.recsee.wooribank.script.repository.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.furence.recsee.wooribank.script.param.request.ScriptEditParam;

@Repository
public interface ScriptEditTransactionDao {
	
	/**
	 * 트랜잭션ID 로 조회 트랜잭션 상태,중복 검사
	 * @param transactionId
	 * @return
	 */
	List<Integer> selectCountForOpenTransaction(String transactionId);
	
	/**
	 * 상품 PK로 조회 트랜잭션 상태,중복 검사
	 * @param proudctPk
	 * @return
	 */
	int selectApproveRequestCountByProduct(String proudctPk);
	
	
	
	/**
	 * 스크립트 편집 트랜잭션 시작
	 * @param dto
	 * @return
	 */
	int insertScriptEditBeginInfo(ScriptEditParam.Transaction dto);
	
	/**
	 * 스크립트 편집 트랜잭션 종료
	 * @param dto
	 * @return
	 */
	int updateScriptEditEndInfo(ScriptEditParam.Transaction dto);
	
	/**
	 * 해당 트랜잭션 정보를 삭제
	 * @param dto
	 * @return
	 */
	int deleteScriptEditInfo(ScriptEditParam.Transaction dto);
	
	/**
	 * 트랜잭션에 해당하는 스크립트 스텝 수정 데이터들을 삭제
	 * @param dto
	 * @return
	 */
	int deleteScriptStepEditList(ScriptEditParam.Transaction dto);
	
	/**
	 * 트랜잭션에 해당하는 스크립트 스텝디테일 수정 데이터를 삭제
	 * @param dto
	 * @return
	 */
	int deleteScriptStepDetailEditList(ScriptEditParam.Transaction dto);
	
	/**
	 * 스크립트 수정 즉시 적용
	 * @param map
	 * @return
	 */
	int updateScriptApplyImmediately(Map<String,Object> map);
	
	
	/**
	 * 기등록 스크립트 복사 적용 
	 * @param map
	 * @return
	 */
	int insertScriptFromRegistered(Map<String,Object> map);
	
	
	/**
	 * 기등록 스크립트 복사 결재요청
	 * @param map
	 * @return
	 */
	int insertScriptEditDataFromRegistered(Map<String,Object> map);

	/**
	 * old Transaction ID 상태 체크 : 결재의뢰 or 적용예정
	 * @param transactionId
	 * @return
	 */
	int selectApproveRequestCountByTransactionId(String transactionId);
	
	/**
	 * 재상신 시작 (1): edit_info insert
	 * @param dto
	 * @return
	 */
	int insertRestartEditInfo(ScriptEditParam.Transaction dto);
	
	/**
	 * 재상신 시작 (2): 스텝 편집정보 insert
	 * @param dto
	 * @return
	 */
	int insertRestartEditStep(ScriptEditParam.Transaction dto);
	
	/**
	 * 재상신 시작 (3): 디테일 편집정보 insert
	 * @param dto
	 * @return
	 */
	int insertRestartEditDetail(ScriptEditParam.Transaction dto);
	
}
