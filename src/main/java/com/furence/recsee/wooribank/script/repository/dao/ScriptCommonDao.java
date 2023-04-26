package com.furence.recsee.wooribank.script.repository.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.furence.recsee.wooribank.script.param.request.ScriptApproveParam;
import com.furence.recsee.wooribank.script.param.request.ScriptEditParam;

@Repository
public interface ScriptCommonDao {
	
		
	/**
	 * 공통스크립트 pk에 대한 결재대기건 확인(수정,삭제의 경우)	
	 * @param scriptCommonPk
	 * @return
	 * @throws Exception
	 */
	int selectCountForAlreadyRegisteredScriptCommon(String commonScriptPk) ;
	
	/**
	 * 공통스크립트 결재상신 요청
	 * @param editDto
	 * @return
	 * @throws Exception
	 */
	int insertApprovalDataForScriptCommon(ScriptEditParam.Common editDto);
	
	/**
	 * 공통스크립트 변경 결재상신건에 대한  취소/반려/결재 
	 * @param approveDto
	 * @return
	 * @throws Exception
	 */
	int updateApproveStatusForScriptCommon(ScriptApproveParam approveDto) ;
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	int updateScriptCommonApplyImmediately(Map<String,Object> map);
	
	/**
	 * 공통스크립트 pk가 스크립트디테일로 쓰이는지 확인(삭제 상신 전)
	 * @param scriptCommonPk
	 * @return
	 * @throws Exception
	 */
	int selectCountForUsedAsDetail(String commonScriptPk);
}
