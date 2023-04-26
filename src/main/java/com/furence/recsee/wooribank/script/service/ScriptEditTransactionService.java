package com.furence.recsee.wooribank.script.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.wooribank.script.constants.Const.Approve;
import com.furence.recsee.wooribank.script.param.request.ProductParam;
import com.furence.recsee.wooribank.script.param.request.ScriptEditParam;
import com.furence.recsee.wooribank.script.param.response.ResultCode;
import com.furence.recsee.wooribank.script.repository.dao.ScriptEditTransactionDao;

@Service("transactionService")
public class ScriptEditTransactionService {
	
	private static final Logger logger = LoggerFactory.getLogger(ScriptEditTransactionService.class);
	
	private ScriptEditTransactionDao transactionDao;
	
	@Autowired
	public ScriptEditTransactionService(ScriptEditTransactionDao transactionDao) {
		this.transactionDao = transactionDao;
	}
	
	
	/**
	 * 특정 상품 스크립트에 대한 수정 트랜잭션 시작 가능여부 확인
	 * @param productPk
	 * @return
	 */
	private boolean isExistApprovalRequestOfProduct(String productPk) {
		
		int count = this.transactionDao.selectApproveRequestCountByProduct(productPk);
		return !(count == 0);
	}
	
	
	/**
	 * 진행된 수정건에 대해 상신등록 가능 여부 확인
	 * @param transactionId
	 * @return
	 */
	private boolean isEnableRequestApprove(String transactionId) {	
		
		List<Integer> list = this.transactionDao.selectCountForOpenTransaction(transactionId);
		list = Optional.ofNullable(list).orElse(Collections.emptyList());
		if( list.isEmpty() || list.size() < 3 ) return false;
		
		boolean isOpen = list.get(0) == 1;
		boolean isRegistered = list.get(1) + list.get(2) > 0;
		
		return isOpen && isRegistered;
	}
	
	/**
	 * 재상신 하려는 old transaction ID가 결재의뢰 혹은 적용예정 상태인지 확인
	 * @param transactionId
	 * @return
	 */
	private boolean isTransactionIng(String transactionId) {
		int count = this.transactionDao.selectApproveRequestCountByTransactionId(transactionId);
		return !(count == 0);
	}

	/**
	 * 새로운 트랜잭션 아이디를 생성 반환
	 * @param transactionDto
	 * @return
	 * @throws Exception
	 */
	public ResultCode transactionBegin(ScriptEditParam.Transaction dto) throws Exception {
		
		// 이미 등록된 건이 있는지 확인
		if( this.isExistApprovalRequestOfProduct(dto.getProductPk()) ) {
			return ResultCode.ALREADY_REQUESTED;
		}
		
		// 없으면 uuid 생성후 입력
		String uuid = UUID.randomUUID().toString();
		dto.setTransactionId(uuid);
		
		int rowCount = 0;
		
		// 신규의뢰
		if(StringUtils.isEmpty(dto.getOldTransactionId())) {
			rowCount = this.transactionDao.insertScriptEditBeginInfo(dto);
		}else { // 재의뢰
			
			// old transaction id가 결재의뢰 또는 완료 상태
			if ( true == this.isTransactionIng(dto.getOldTransactionId())) {
				return ResultCode.NOT_REJECTED;
			}
			
			// old transaction id가 반려 또는 취소 상태 - new transaction id에 old id의 스텝 및 디테일 추가
			rowCount = this.transactionDao.insertRestartEditInfo(dto);
			if (rowCount > 0) {
				int stepCount = this.transactionDao.insertRestartEditStep(dto);
				int detailCount = this.transactionDao.insertRestartEditDetail(dto);
				rowCount = Math.max(stepCount, detailCount);
			}else {
				return ResultCode.SYSTEM_ERROR;
			}
		}
		logger.info("rowCount:" + rowCount);
		return rowCount > 0 ? ResultCode.SUCCESS : ResultCode.SYSTEM_ERROR;
	}
	
	/**
	 * 스크립트 편집 트랜잭션 완료/취소
	 * @param transactionDto
	 * @return
	 */
	public ResultCode transactionEnd(ScriptEditParam.Transaction dto) {
		
		ResultCode resultCode = ResultCode.SYSTEM_ERROR;
		
		switch(dto.getEndType()) {
		case COMMIT:
			if( this.isExistApprovalRequestOfProduct(dto.getProductPk()) ) {
				return ResultCode.ALREADY_REQUESTED;
			}
			
			// commit할 트랜잭션내에 스크립트 수정된 건이 존재하는지 확인
			if( false == this.isEnableRequestApprove(dto.getTransactionId())) {
				return ResultCode.NO_EDITED_SCRIPT;
			}
			
			dto.setApproveStatus(Approve.REQUEST);
			// 전달받은 파라미터를 통해 트랜잭션을 완료 처리함.	
			int rowCount = this.transactionDao.updateScriptEditEndInfo(dto);
			
			if (rowCount > 0) {
				resultCode = ResultCode.SUCCESS;				
			}
			break;
		case ROLLBACK:
			// 취소인 경우에는 삭제 
			// 디테일 삭제
			this.transactionDao.deleteScriptStepDetailEditList(dto);
			// 스텝 삭제
			this.transactionDao.deleteScriptStepEditList(dto);
			// 트랜잭션 삭제
			this.transactionDao.deleteScriptEditInfo(dto);
			
			resultCode = ResultCode.SUCCESS;	
			break; 
		}
		
		
		return resultCode;
	}
	
	/**
	 * 기등록 스크립트를 같은 상품타입의 스크립트 미등록 상품에 적용 
	 * @param copy
	 * @return
	 */
	public ResultCode copyScript(ProductParam.Copy copy) {
		
		Map<String, Object> param = new HashMap<>();
		
		param.put("srcProductPk", Integer.parseInt(copy.getSrcProductPk()));
		param.put("destProductPkList", copy.getTargetList());
		param.put("code", 0);
		param.put("message", "");
		
		logger.info("Before execue:{}", param);
		
		this.transactionDao.insertScriptFromRegistered(param);
		
		logger.info("After execue:{}", param);
		
		return param.get("code").toString().equals("0")  ? ResultCode.SUCCESS : ResultCode.UNDEFINED_ERROR ;
	}
	
	
	private int registCopyScriptApprove(ProductParam.CopyApprove copy ) {
		
		Map<String, Object> param = new HashMap<>();
		
		param.put("editUser", copy.getApproveUser());
		param.put("srcProductPk", Integer.parseInt(copy.getSrcProductPk()));
		param.put("applyType", copy.getApplyType());
		param.put("applyDate", copy.getApplyDate());
		param.put("code", 0);
		param.put("message", "");
		
		int succCount = 0;		
		
		for( Integer destPk : copy.getTargetList() ) {
			try {
				String uuid = UUID.randomUUID().toString();
				param.put("transactionId", uuid);
				param.put("destProductPk", destPk);	
				logger.info("Before execue:{}", param);			
				this.transactionDao.insertScriptEditDataFromRegistered(param);			
				logger.info("After execue:{}", param);
				succCount = succCount + ((int)param.get("code") < 0 ? 0 : 1);
			} catch (Exception e) {
				logger.error("error",e);
			}
		}
		
		return succCount;
	}
	
	/**
	 * 기등록 스크립트를 같은 상품타입의 스크립트 미등록 상품에 적용 
	 * @param copy
	 * @return
	 */
	public ResultCode copyScriptForApprove(ProductParam.CopyApprove copy) {
		
		int successCount = this.registCopyScriptApprove(copy);
		
		return copy.getTargetList().size() ==  successCount 
				? ResultCode.SUCCESS : ResultCode.UNDEFINED_ERROR ;
	}
}
