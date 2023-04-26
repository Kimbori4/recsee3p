package com.furence.recsee.wooribank.script.repository.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.furence.recsee.wooribank.script.param.request.ScriptApproveParam;
import com.furence.recsee.wooribank.script.param.response.MergedApprove;
import com.furence.recsee.wooribank.script.param.response.ScriptCommonInfo;
import com.furence.recsee.wooribank.script.repository.entity.Product;
import com.furence.recsee.wooribank.script.repository.entity.ScriptApprove;

@Repository
public interface ScriptEditApproveDao {
	
	/**
	 * 스크립트 수정 결재요청건 조회
	 * @param approve
	 * @return
	 */
	List<MergedApprove> selectApproveList(ScriptApproveParam.Search search);
	
	
	/**
	 * 스크립트 수정 결재요청건 전체 카운트
	 * @param approve
	 * @return
	 */
	Integer selectApproveListCount(ScriptApproveParam.Search search);
	
	
	/**
	 * 스크립트 상세 조회
	 * @param scriptEditId
	 * @return
	 */
	String selectProductScriptInfoBeforeApply(String scriptEditId);
	
	/**
	 * 스크립트 수정 결재요청건 상세 조회
	 * @param scriptEditId
	 * @return
	 */
	String selectProductScriptInfoAfterApply(String scriptEditId);
	
	/**
	 * 공용 스크립트 현재 데이터 조회
	 * @param scriptEditId
	 * @return
	 */
	ScriptCommonInfo selectCommonScriptInfoBeforeApply(int scriptEditId);
	
	
	/**
	 * 공용 스크립트 수정내용 미리보기 조회회
	 * @param scriptEditId
	 * @return
	 */
	ScriptCommonInfo selectCommonScriptInfoAfterApply(int scriptEditId);
	
	/**
	 * 상품 스크립트 수정 상신건 결재/반려 처리 
	 * @param scriptEditId
	 * @return
	 */
	int updateApproveStatusForProduct(Map<String, Object> map) throws Exception;
	
	/**
	 * 공통 스크립트 수정 상신건 결재/반려 처리 
	 * @param scriptEditId
	 * @return
	 */
	int updateApproveStatusForCommon(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	int updateScriptCommonApplyImmediately(Map<String, Object> map) throws Exception ;
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	int updateScriptApplyImmediately(Map<String, Object> map) throws Exception;
	
	
	/**
	 * rs_script_edit_id 리스트에 해당하는 리스트 가져오기
	 * @param list
	 * @return
	 */
	List<ScriptApprove> selectScriptApproveList(List<String> list);
	
	/**
	 * 상품별 스냅샷 저장 프로시저 실행
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int insertProductScriptSnapshot(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 스크립트 편집건에 대한 대상상품 pk 리스트 가져오기
	 * @param productPk
	 * @return
	 */
	List<Product> selectProductListForSnapshot(String scriptEditId);
	
	
	/**
	 * 상품별 스냅샷 저장 프로시저2 실행
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int insertProductScriptSnapshot2(Map<String, Object> map) ;
	
	/**
	 * 당일 반영 예약된 스크립트 정보 조회 ( 스냅샷 저장용 )
	 * @return
	 */
	List<ScriptApprove> selectReservedApproveList(String reservedDate);
	

	/**
	 * 트랜젝션 ID 참조해 비정규 ELT상품체크
	 * @return
	 */
	String selectNonRefEltCheck(String tId);


	void updateNonRefEltAttr(String tId); 
}
