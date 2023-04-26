package com.furence.recsee.wooribank.script.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.wooribank.script.constants.EditTypes;
import com.furence.recsee.wooribank.script.param.request.ScriptApproveParam;
import com.furence.recsee.wooribank.script.param.request.ScriptEditParam;
import com.furence.recsee.wooribank.script.param.request.ScriptEditParam.Common;
import com.furence.recsee.wooribank.script.param.response.ResultCode;
import com.furence.recsee.wooribank.script.repository.dao.ScriptCommonDao;
import com.furence.recsee.wooribank.script.repository.entity.ScriptStep;
import com.furence.recsee.wooribank.script.service.types.ScriptEditTask;


@Service("commonService")
public class ScriptCommonService extends ScriptEditTask<ScriptEditParam.Common, ScriptStep> {
	private static final Logger logger = LoggerFactory.getLogger(ScriptCommonService.class);
	private ScriptCommonDao commonDao;
	
	@Autowired                                                                                     
	public ScriptCommonService(ScriptCommonDao commonDao) {
		this.commonDao = commonDao;
	}
	
	
	@Override
	protected NextTask preTask(EditTypes type, Common edit) throws Exception {
		
		// 원본 데이터에 처리할 추가/수정/삭제 타입
		switch (type) { 
		
		// 신규 데이터 입력 
		case Create:			
			break;
		case Delete:
			// 상품스크립트 포함 여부
			boolean isUsing = checkUsingInProductScript(edit.getCommonScriptPk());
			// 포함되어 있으면 stop
			if(isUsing) return NextTask.AfterOther;
			// 포함되어 있지 않으면 다음 진행 = 상신 건 있는지 체크
		// 스크립트 수정/삭제
		case Update:	
			boolean isExists = isExistApprovalRequest(edit.getCommonScriptPk());			
			if(isExists) return NextTask.Stop;
			break;
		} 
		
		return NextTask.Main;
	}

	@Override
	protected ResultCode preTaskAfter(EditTypes type, Common edit) throws Exception {
		return ResultCode.SUCCESS;
	}
	
	@Override
	protected ResultCode preTaskAfterOther(EditTypes type, Common edit) throws Exception {
		return ResultCode.USING_IN_PRODUCT_SCRIPT;
	}
	
	@Override
	protected void afterTask(EditTypes type, Common edit, ResultCode resultCode) throws Exception {
		
	}
	
	@Override
	protected ResultCode exceptionTask(EditTypes type, Common edit) throws Exception {
		return ResultCode.ALREADY_REQUESTED;
	}
	
	@Override
	protected ResultCode mainTask(EditTypes type, Common edit) throws Exception {
		
		ResultCode result = ResultCode.SYSTEM_ERROR;
		try {
			// 상신건 등록
		
			this.commonDao.insertApprovalDataForScriptCommon(edit);
			String commEditId = edit.getCommonScriptEditId();
			
			if ( commEditId != null ) {
				result = ResultCode.SUCCESS;			
			}
		} catch (Exception e) {
			logger.error("error",e);
		}
		return result;
	}
	
	
	/**
	 * 해당 공용문구에 대해 현재 결재대기중인 수정,삭제 상신건이 있는지 확인
	 * @param commonScriptPk
	 * @return
	 */
	public boolean isExistApprovalRequest(String commonScriptPk) {
		
		int count = this.commonDao.selectCountForAlreadyRegisteredScriptCommon(commonScriptPk);
		return count > 0;
	}
	
	
	/**
	 * 스크립트 편집 결재 상신건 - 결재/반려/취소
	 * @param dto
	 * @return
	 */
	public ResultCode approveEdittedScript(ScriptApproveParam dto) {
		
		ResultCode result = ResultCode.SYSTEM_ERROR;
		
		try {
			
			int rowCount = commonDao.updateApproveStatusForScriptCommon(dto);
			result = rowCount > 0 ? ResultCode.SUCCESS : ResultCode.NO_AFFECTED;	
			
		} catch(Exception e) {
			logger.error("error",e);
		}
		
		return result;
	}
	
	
	/**
	 * 해당 공용문구가 현재 상품스크립트 디테일로 사용되고 있는지 확인
	 * @param commonScriptPk
	 * @return
	 */
	private boolean checkUsingInProductScript(String commonScriptPk) {
		
		int count = this.commonDao.selectCountForUsedAsDetail(commonScriptPk);
		return count > 0;
	}
}
