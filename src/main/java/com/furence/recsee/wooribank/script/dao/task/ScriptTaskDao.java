package com.furence.recsee.wooribank.script.dao.task;

import java.util.List;

import com.furence.recsee.wooribank.script.param.request.base.EditDtoType;
import com.furence.recsee.wooribank.script.repository.entity.ScriptEntity;

public interface ScriptTaskDao<T extends EditDtoType, T2 extends ScriptEntity> {

	/**
	 * 조회
	 * @param searConditionDTO
	 * @return
	 */
	public List<T2> selectScriptEntity(T eto);
	
	/**
	 * 중복여부 확인
	 * @param editDto
	 * @return
	 */
	List<Integer> selectCountScriptEntity(T eto);
	
	/**
	 * 스크립트 추가/수정/삭제 편집 데이터 저장
	 * @param editDto
	 * @return
	 */
	int insertScriptEditData(T eto);
	
	/**
	 * 디비에 임시 저장된 스크립트 수정건에 대한 수정
	 * @param editDto
	 * @return
	 */
	int updateScriptEditData(T eto);
	
	/**
	 * 실제 저장된 데이터가 아닌 편집 트랜잭션 상 추가된 데이터에 대한 삭제처리용
	 * @param editDto
	 * @return
	 */
	int deleteScriptEditData(T eto);
}
