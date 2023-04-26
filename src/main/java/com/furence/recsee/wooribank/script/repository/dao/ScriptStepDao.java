package com.furence.recsee.wooribank.script.repository.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.furence.recsee.wooribank.script.param.request.ScriptEditParam;
import com.furence.recsee.wooribank.script.repository.entity.ScriptStep;

@Repository
public interface ScriptStepDao {

	/**
	 * 스텝 정보 조회
	 * @param searConditionDTO
	 * @return
	 */
	public List<ScriptStep> selectScriptStepList(ScriptEditParam.Step searchDto);
	
	/**
	 * 중복여부 확인
	 * @param editDto
	 * @return
	 */
	List<Integer> selectCountForAlreadyRegisteredScriptStep(ScriptEditParam.Step editDto);
	
	/**
	 * 스크립트 스텝 추가/수정/삭제
	 * @param editDto
	 * @return
	 */
	int insertScriptStepEditData(ScriptEditParam.Step editDto);
	
	/**
	 * 디비에 임시 저장된 스크립트 스텝 수정건에 대한 수정
	 * @param editDto
	 * @return
	 */
	int updateScriptStepEditData(ScriptEditParam.Step editDto);
	
	/**
	 * 실제 저장된 데이터가 아닌 편집 트랜잭션 상 추가된 데이터에 대한 삭제처리용
	 * @param editDto
	 * @return
	 */
	int deleteScriptStepEditData(ScriptEditParam.Step editDto);
}
