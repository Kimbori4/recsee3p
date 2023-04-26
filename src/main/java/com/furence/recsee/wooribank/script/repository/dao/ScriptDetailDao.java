package com.furence.recsee.wooribank.script.repository.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.furence.recsee.wooribank.script.param.request.ScriptEditParam;
import com.furence.recsee.wooribank.script.repository.entity.ScriptDetail;

@Repository
public interface ScriptDetailDao {
	
	/**
	 * 스크립트 디테일 정보
	 * @param searchDto
	 * @return
	 */
	public List<ScriptDetail> selectScriptStepDetailList(ScriptEditParam.Detail searConditionDTO);
	
	/**
	 * 스크립트 디테일미리보기 정보
	 * @param searchDto
	 * @return
	 */
	public List<ScriptDetail> selectScriptStepDetailPreviewList(ScriptEditParam.Detail searConditionDTO);
	
	
	/**
	 * 중복여부 확인
	 * @param editDto
	 * @return
	 */
	List<Integer> selectCountForAlreadyRegisteredScriptStepDetail(ScriptEditParam.Detail editDto);
	
	/**
	 * 스크립트 스텝 추가/수정/삭제
	 * @param editDto
	 * @return
	 */
	int insertScriptStepDetailEditData(ScriptEditParam.Detail editDto);
	
	/**
	 * 디비에 임시 저장된 스크립트 스텝 수정건에 대한 수정
	 * @param editDto
	 * @return
	 */
	int updateScriptStepDetailEditData(ScriptEditParam.Detail editDto);
	
	
	/**
	 * 실제 저장된 데이터가 아닌 편집 트랜잭션 상 추가된 데이터에 대한 삭제처리용
	 * @param editDto
	 * @return
	 */
	int deleteScriptStepDetailEditData(ScriptEditParam.Detail editDto);
}
