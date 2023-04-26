package com.furence.recsee.wooribank.script.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.wooribank.script.annotation.Translate;
import com.furence.recsee.wooribank.script.constants.EditTypes;
import com.furence.recsee.wooribank.script.param.request.ScriptEditParam;
import com.furence.recsee.wooribank.script.param.request.ScriptEditParam.Detail;
import com.furence.recsee.wooribank.script.param.response.ResultCode;
import com.furence.recsee.wooribank.script.repository.dao.ScriptDetailDao;
import com.furence.recsee.wooribank.script.repository.entity.ScriptDetail;
import com.furence.recsee.wooribank.script.service.types.ScriptEditTask;

@Service("detailService")
public class ScriptDetailService extends ScriptEditTask<ScriptEditParam.Detail, ScriptDetail> {

	private static final Logger logger = LoggerFactory.getLogger(ScriptDetailService.class);
	
	@Autowired
	private ScriptDetailDao dao;
	
	
	/**
	 * 중복 등록 체크 및 신규입력에 대한 수정건에 대해 타입변경 처리
	 */
	@Override
	protected NextTask preTask(EditTypes type, Detail edit) throws Exception {
				
		List<Integer> counts = this.dao.selectCountForAlreadyRegisteredScriptStepDetail(edit);
		
		// 카운트쿼리이므로 리스트는 무조건 1
		boolean isExists = counts.get(0).intValue() > 0;
		boolean isNew = isExists && edit.getScriptStepDetailPk().startsWith("N");
		
		// 임시 정보 의 업데이트를 위해 임시키 별도 저장
		edit.setScriptStepDetailTempPk(edit.getScriptStepDetailPk());
		
		// 원본 데이터에 처리할 추가/수정/삭제 타입
		switch (type) { 
		
		// 신규 데이터 입력 
		case Create:
			// 신규입력건에 대해 중복건이 존재하지 않으면 바로 저장						
			String newPk = "N"+System.currentTimeMillis();				
			edit.setScriptStepDetailPk(newPk);
			edit.setScriptStepDetailTempPk(edit.getScriptStepDetailPk());
			break;
			
		// 스크립트 삭제
		case Delete:
			// 신규 기등록건에 대한 삭제는  sql delete
			if( isNew ) return NextTask.AfterOther;
			break;
		// 스크립트 수정
		case Update:
			// 신규 기등록건에 대한 수정은  sql update
			if( isExists ) {				
				if(isNew)edit.setEditType(EditTypes.Create);
				return NextTask.After;			
			}
			break;
		} 
		
		return NextTask.Main;

	};
		
	@Override
	protected ResultCode preTaskAfter(EditTypes type, Detail edit) throws Exception {
		logger.info("doUpdate ["+type.name()+"] data:{}", edit.toString() );
		int updateResult = this.dao.updateScriptStepDetailEditData(edit);
		return updateResult > 0 ? ResultCode.SUCCESS : ResultCode.NOT_FOUND_TRANSACTION;
	}
	
	@Override
	protected ResultCode preTaskAfterOther(EditTypes type, Detail edit) throws Exception {
		logger.info("doDelete ["+type.name()+"] data:{}", edit.toString() );
		int updateResult = this.dao.deleteScriptStepDetailEditData(edit);
		return updateResult > 0 ? ResultCode.SUCCESS : ResultCode.NOT_FOUND_TRANSACTION;
	}
	
	@Override
	protected ResultCode mainTask(EditTypes type, @Translate Detail edit) throws Exception {
		logger.info("doInsert ["+type.name()+"] data:{}", edit.toString() );
		
		int updateResult = this.dao.insertScriptStepDetailEditData(edit);
		return updateResult > 0 ? ResultCode.SUCCESS : ResultCode.NOT_FOUND_TRANSACTION;
	}
	
	@Override
	protected void afterTask(EditTypes type, Detail edit, ResultCode resultCode) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected ResultCode exceptionTask(EditTypes type, Detail edit) throws Exception {
		return ResultCode.UNDEFINED_ERROR;
	}
	
	/**
	 * 스텝에 해당하는 스크립트 디테일 쿼리 
	 * @param edit
	 * @return
	 */
	public List<ScriptDetail> getListOfScriptDetail(Detail edit) {
		return this.dao.selectScriptStepDetailList(edit);
	}
	
	/**
	 * 스크립트 디테일의 수정사항 반영한 미리보기 
	 * @param edit
	 * @return
	 */
	public List<ScriptDetail> getListOfScriptDetailPreview(Detail edit) {
		return this.dao.selectScriptStepDetailPreviewList(edit);
	}
	
}
