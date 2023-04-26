package com.furence.recsee.wooribank.script.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.wooribank.script.constants.Const;
import com.furence.recsee.wooribank.script.param.request.MakeTTSParam;
import com.furence.recsee.wooribank.script.param.request.ScriptApproveParam;
import com.furence.recsee.wooribank.script.param.response.DataComparable;
import com.furence.recsee.wooribank.script.param.response.DataComparable.Side;
import com.furence.recsee.wooribank.script.param.response.MergedApprove;
import com.furence.recsee.wooribank.script.param.response.ResultCode;
import com.furence.recsee.wooribank.script.param.response.ScriptCommonInfo;
import com.furence.recsee.wooribank.script.param.response.ScriptInfo;
import com.furence.recsee.wooribank.script.repository.dao.ScriptEditApproveDao;
import com.furence.recsee.wooribank.script.repository.entity.Product;
import com.furence.recsee.wooribank.script.repository.entity.ScriptApprove;
import com.furence.recsee.wooribank.script.util.DhtmlGridDataProvider;

@EnableAsync
@Service("approveService")
public class ScriptEditApproveService {
	
	private static final Logger logger = LoggerFactory.getLogger(ScriptEditApproveService.class);
		
	private ScriptEditApproveDao approveDao;
	
	private ScriptTtsService ttsService;
	
	private AsyncTaskService asyncService;
		
	@Autowired
	public ScriptEditApproveService(ScriptEditApproveDao approveDao, 
			ScriptTtsService ttsService ,
			AsyncTaskService asyncService) {
		this.approveDao = approveDao;
		this.ttsService = ttsService;
		this.asyncService = asyncService;
	}
	
	/**
	 * 상품스크립트 수정 결재요청건 조회
	 * @param paging
	 * @return
	 */
	public List<MergedApprove> getListOfApproveList(ScriptApproveParam.Search search){
		return this.approveDao.selectApproveList(search);
	}
	
	
	/**
	 * 상품스크립트 수정 결재요청건 전체 건수 조회
	 * @return
	 */
	public int getCountOfApproveList(ScriptApproveParam.Search search){
		return this.approveDao.selectApproveListCount(search);
	}
	
	public dhtmlXGridXml getDhtmlxHeaderOfApprovalList(ScriptApproveParam.Search search, String args[]) {
		
		boolean isApprover = Optional.ofNullable(search.getApproverYN())
				.orElse("").equals("Y");

		DhtmlGridDataProvider gridProvider = isApprover
				? DhtmlGridDataProvider.ScriptApprovalGrid 
				: DhtmlGridDataProvider.ScriptApprovalResultGrid;
		return gridProvider.getHeaders( args ); 
	}
	
	public dhtmlXGridXml getDhtmlxRowOfApprovalList(ScriptApproveParam.Search search) {
		
		try {
			boolean isApprover = Optional.ofNullable(search.getApproverYN()).orElse("").equals("Y");
			
			List<MergedApprove> result = this.getListOfApproveList(search);
			int totalCount = this.getCountOfApproveList(search);
			
			DhtmlGridDataProvider gridProvider = isApprover
					? DhtmlGridDataProvider.ScriptApprovalGrid 
					: DhtmlGridDataProvider.ScriptApprovalResultGrid;
			
			String [] args = new String[2];
			args[0] = Boolean.toString(isApprover);
			args[1] = search.getApproveStatus();
			
			dhtmlXGridXml xmls = gridProvider.getRows(result, args );
			xmls.setTotal_count( totalCount + "" );
			xmls.setPos(search.getOffset()+"");
			
			return xmls;
		} catch(Exception e) {
			logger.error("error",e);
			return new dhtmlXGridXml();
		}
	}
	
	/**
	 * 스크립트 적용 전,후 비교데이터 조회	
	 * @param transactionId
	 * @param scriptKind
	 * @return
	 */
	public DataComparable<?> getComparablePreviewScript(String transactionId, Const.ScriptKind scriptKind){
		
		switch (scriptKind) {
		case Common:
			return getPreviewOfCommonScript(transactionId);
		case Product:
			return getPreviewOfCProductScript(transactionId);		
		}
		return null;
	}
		
	/**
	 * 결재 승인/반려 처리
	 * @param approve
	 * @return
	 */	
	public ResultCode completeApprove(ScriptApproveParam approve) {
		
		try {
			switch(approve.getScriptType()) {
			case Common:
				return completeScriptCommonEditArrove(approve);
			case Product:
				return completeScriptEditArrove(approve);
			default:
				return ResultCode.INVALID_PARAMETER;
			}
		} catch (Exception e) {
			logger.error("error",e);
			ResultCode result = ResultCode.SYSTEM_ERROR;
			result.setMessage(e.getMessage());
			return result;
		}
	}
	
	
	
	/**
	 * 공용 스크립트 결재 완료(승인/반려/취소)
	 * @param approve
	 * @return
	 */
	private ResultCode completeScriptCommonEditArrove(ScriptApproveParam approve) throws Exception {
		
		Map<String, Object> param = convertObject2Map(approve);
		int commonEditId = Integer.parseInt(approve.getTransactionId());
		param.put("scriptCommonEditId", commonEditId);
		
		this.approveDao.updateApproveStatusForCommon(param);
		logger.info("updateApproveStatusForCommon:", param);
		
		if(approve.isApplyImmediately()) {
			applyScriptCommonImmediately(param);
		}
		
		return param.get("code").toString().equals("1") ? ResultCode.SUCCESS : ResultCode.UNDEFINED_ERROR ;
	}
	
	/**
	 * 스크립트 수정데이터 반영
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private void applyScriptCommonImmediately(Map<String, Object> param) throws Exception{
		this.approveDao.updateScriptCommonApplyImmediately(param);
		logger.info("updateScriptCommonApplyImmediately:", param);
	} 
	
	
	/**
	 * 상품 스크립트 결재 완료(승인/반려/취소)
	 * @param approve
	 * @return
	 */
	private ResultCode completeScriptEditArrove(ScriptApproveParam approve) throws Exception{
		return approve.isApplyImmediately() 
				? executeApplyScriptImmediatelyTask(approve) 
				: completeApproveSatusForProduct(approve);
	}
	
	/**
	 * 결재건 승인처리
	 * @param approve
	 * @return
	 * @throws Exception
	 */
	private ResultCode completeApproveSatusForProduct(ScriptApproveParam approve) throws Exception{

		Map<String, Object> param = convertObject2Map(approve);
		this.approveDao.updateApproveStatusForProduct(param);
		logger.info("updateApproveStatusForProduct:", param);	
		return param.get("code").toString().equals("1") ? ResultCode.SUCCESS : ResultCode.UNDEFINED_ERROR ; 
	}
	
	
	/**
	 * Aysnc 작업 실행
	 * @param approve
	 * @throws Exception
	 */
	private ResultCode executeApplyScriptImmediatelyTask(ScriptApproveParam approve) throws Exception {
		
		boolean isAlive = this.ttsService.healthCheckTTSServer() ;
		
		if( isAlive ) {
			this.asyncService.execute(() -> {
				try {
					applyScriptImmediately(approve);
				} catch (Exception e) {
					logger.error("error",e);
				}
			});		
		}		
		return isAlive ? ResultCode.SUCCESS : ResultCode.CONNECT_FAIL_TO_TTS_SERVER ;		
	}	
	
	/**
	 * 새로운 스크립트 편집 결재건 즉시 적용
	 * @param kind
	 * @param param
	 * @return
	 */	
	private void applyScriptImmediately(ScriptApproveParam approve) throws Exception {
		
		if(false == createScriptSnapShot(approve)) return;
		if( completeApproveSatusForProduct(approve) != ResultCode.SUCCESS ) return;
		if(false == updateScriptEditData(approve)) return;
		
		// TTS 생성 요청 (async)
		requestMakeTTS(approve.getApproveUser(), approve.getScriptEditId());
	}	
	
	/**
	 *  product에 대한 TTS 즉시 생성 요청	
	 * @param productPk
	 */	
	private void requestMakeTTS(String userId, String editId) throws Exception {
		
		List<String> productKeyList = getListOfProductPk(editId);		
		if(productKeyList.isEmpty()) {
			logger.error("Not found rs_product_list_pk of editId:{} ",productKeyList);
			return;
		}
		
		requestMakeTTS(mekeTTSParam(userId, productKeyList));
	}
	
	/**
	 * TTS 실시간 생성 패킷 전송
	 * @param param
	 */
	private void requestMakeTTS(MakeTTSParam param) {		
		try {
			this.ttsService.requestMakeTTS( param);
		} catch (Exception e) {
			logger.error("error",e);
		}		
	}
	
	/**
	 * 상품별  스크립트 스냅샷 저장
	 * @param approve
	 * @return
	 * @throws Exception
	 */
	private boolean createScriptSnapShot(ScriptApproveParam approve) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();		
		param.put("code", 0);
		param.put("message", "");
		
		List<Product> productList= this.approveDao.selectProductListForSnapshot(approve.getScriptEditId());
		
		productList.forEach( product -> {
			param.put("productPk", Integer.parseInt(product.getProductPk()));
			param.put("scriptEditId", approve.getScriptEditId());
			this.approveDao.insertProductScriptSnapshot2(param);
			logger.info("insertProductScriptSnapshot2: {}", param);
		});
		
		return true;
	}
	
	/**
	 * 상품 스크립트 편집데이터 적용
	 * @param approve
	 * @return
	 * @throws Exception
	 */
	private boolean updateScriptEditData(ScriptApproveParam approve) throws Exception {
		Map<String, Object> param = convertObject2Map(approve);
		// 업데이트 적용
		this.approveDao.updateScriptApplyImmediately(param);
		logger.info("UpdateScriptApplyImmediately Code:"+ param.get("code") + " Message:"+ param.get("message"));
		return (int)param.get("code") == 1;
	}
	
	/**
	 * 편집건에 대한 상품 id
	 * @param editId
	 * @return
	 */
	private List<String> getListOfProductPk(String editId) {
		List<ScriptApprove> list = this.approveDao.selectScriptApproveList(List.of(editId));
		return Optional.ofNullable(list)
				.orElse(Collections.emptyList())
				.stream()
				.map(e->e.getProductPk())
				.collect(Collectors.toList());
	}
	
	/**
	 * TTS 즉시반영 파라미터 생성
	 * @param userId
	 * @param productKeyList
	 * @return
	 */
	private MakeTTSParam mekeTTSParam (String userId, List<String> productKeyList) {
		
		return MakeTTSParam.builder()
				.userId(userId)
				.productPkList(productKeyList)
				.build();
	}
	
	
	/**
	 * 상품 스크립트 비교데이터 조회
	 * @param transactionId
	 * @return
	 */
	private DataComparable<ScriptInfo> getPreviewOfCProductScript(String transactionId) {
		
		return DataComparable.<ScriptInfo>builder()
				.before(getPreviewOfCProductScript(Side.Before, transactionId))
				.after(getPreviewOfCProductScript(Side.After, transactionId))
				.build();
	}
	
	/**
	 * 공용 스크립트 비교데이터 조회
	 * @param transactionId
	 * @return
	 */
	private DataComparable<ScriptCommonInfo> getPreviewOfCommonScript(String transactionId) {
		
		return DataComparable.<ScriptCommonInfo>builder()
				.before(getPreviewOfCommonScript(Side.Before, transactionId))
				.after(getPreviewOfCommonScript(Side.After, transactionId))
				.build();
	}

	/**
	 * 상품 스크립트 적용 전/후 비교데이터 조회
	 * @param side
	 * @param transactionId
	 * @return
	 */
	private ScriptInfo getPreviewOfCProductScript(DataComparable.Side side, String transactionId) {
		
		ScriptInfo info = null;
		
		try {
			String jsonStr = side == Side.Before 
					? this.approveDao.selectProductScriptInfoBeforeApply(transactionId)
					: this.approveDao.selectProductScriptInfoAfterApply(transactionId);
			info = ScriptInfo.from(jsonStr);
		} catch (Exception e) {
			logger.error("error",e);
		}
		
		return info;
	}
	
	/**
	 * 공용 스크립트 적용 전/후 비교데이터 조회
	 * @param side
	 * @param transactionId
	 * @return
	 */
	private ScriptCommonInfo getPreviewOfCommonScript(DataComparable.Side side, String transactionId) {
		
		ScriptCommonInfo info = null;
		
		try {			
			int iTransactionId = Integer.parseInt(transactionId);
			info = side == Side.Before 
					? this.approveDao.selectCommonScriptInfoBeforeApply(iTransactionId)
					: this.approveDao.selectCommonScriptInfoAfterApply(iTransactionId);
		} catch (Exception e) {
			logger.error("error",e);
		}
		
		return info;
	}
	
	private Map<String, Object> convertObject2Map(ScriptApproveParam approve) {
		Map<String, Object> param = new HashMap<>();
		
		param.put("scriptEditId", approve.getTransactionId());
		param.put("approveUser", approve.getApproveUser());
		param.put("approveStatus", approve.getApproveStatus().getValue());
		param.put("applyDate", approve.getApplyDate());
		param.put("applyType", approve.getApplyType() == null ? null : approve.getApplyType().getValue() );
		param.put("code", 0);
		param.put("message", "");
		
		return param;
	}

	public String selectNonRefEltCheck(String tId) throws Exception{
		String flag = this.approveDao.selectNonRefEltCheck(tId);
		return Optional.ofNullable(flag).orElse("N");
	}

	public void updateNonRefEltAttr(String tId) throws Exception {
		this.approveDao.updateNonRefEltAttr(tId);
	}
}
