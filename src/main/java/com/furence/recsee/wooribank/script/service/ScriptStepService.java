package com.furence.recsee.wooribank.script.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.main.model.dhtmlXGridXml;
import com.furence.recsee.wooribank.script.constants.EditTypes;
import com.furence.recsee.wooribank.script.param.request.ScriptEditParam;
import com.furence.recsee.wooribank.script.param.request.ScriptEditParam.Step;
import com.furence.recsee.wooribank.script.param.response.ResultCode;
import com.furence.recsee.wooribank.script.repository.dao.ScriptStepDao;
import com.furence.recsee.wooribank.script.repository.entity.ScriptStep;
import com.furence.recsee.wooribank.script.service.types.ScriptEditTask;
import com.furence.recsee.wooribank.script.util.DhtmlGridDataProvider;

@Service("stepService")
public class ScriptStepService extends ScriptEditTask<ScriptEditParam.Step, ScriptStep>{
	
	private static final Logger logger = LoggerFactory.getLogger(ScriptStepService.class);

	@Autowired
	private ScriptStepDao dao;
		
	/**
	 * 중복 등록 체크 및 신규입력에 대한 수정건에 대해 타입변경 처리
	 */
	@Override
	protected NextTask preTask(EditTypes type, Step edit) throws Exception {
				
		List<Integer> counts = this.dao.selectCountForAlreadyRegisteredScriptStep(edit);
		
		// 카운트쿼리이므로 리스트는 무조건 1
		boolean isExists = counts.get(0).intValue() > 0;
		boolean isNew = isExists && edit.getScriptStepPk().startsWith("N");
		
		// 임시 정보 의 업데이트를 위해 임시키 별도 저장
		edit.setScriptStepTempPk(edit.getScriptStepPk());
		
		// 원본 데이터에 처리할 추가/수정/삭제 타입
		switch (type) { 
		
		// 신규 데이터 입력 
		case Create:
			// 신규입력건에 대해 중복건이 존재하지 않으면 바로 저장							
			String newPk = "N"+System.currentTimeMillis();				
			edit.setScriptStepPk(newPk);
			edit.setScriptStepTempPk(edit.getScriptStepPk());	
			break;
			
		// 스크립트 삭제
		case Delete:
			// 신규 기등록건에 대한 삭제는  sql delete
			if( isNew ) return NextTask.AfterOther;
			break;
		// 스크립트 수정
		case Update:
			// 신규 기등록건에 대한 수정은  sql update
			if( isNew ) { 
				edit.setEditType(EditTypes.Create);
				return NextTask.After;			
			}
			break;
		} 
		
		return NextTask.Main;
	}
	
	/**
	 * db 업데이트 처리(등록건에 대한 update 전달시)
	 */
	@Override
	protected ResultCode preTaskAfter(EditTypes type, Step edit) {
		logger.info("doUpdate ["+type.name()+"] data:{}", edit.toString() );
		int updateResult = this.dao.updateScriptStepEditData(edit);
		return updateResult > 0 ? ResultCode.SUCCESS : ResultCode.NOT_FOUND_TRANSACTION;
	}
	
	/**
	 * db delete (신규 건에 대한 delete 타입 전달시)
	 */
	@Override
	protected ResultCode preTaskAfterOther(EditTypes type, Step edit) {
		logger.info("doDelete ["+type.name()+"] data:{}", edit.toString() );
		int updateResult = this.dao.deleteScriptStepEditData(edit);
		return updateResult > 0 ? ResultCode.SUCCESS : ResultCode.NOT_FOUND_TRANSACTION;
	}
	
	/**
	 * db insert (스크립트 신규/수정/삭제 실행 데이터 저장)
	 */
	@Override
	protected ResultCode mainTask(EditTypes type, Step edit) {
		logger.info("doInsert ["+type.name()+"] data:{}", edit.toString() );
		int updateResult = this.dao.insertScriptStepEditData(edit);
		return updateResult > 0 ? ResultCode.SUCCESS : ResultCode.NOT_FOUND_TRANSACTION;
	}
	
	/**
	 * 후처리
	 */
	@Override
	protected void afterTask(EditTypes type, Step edit, ResultCode resultCode) {
		logger.info("afterTask ["+type.name()+"] data:{}", edit.toString() );
	}
	
	/**
	 * 예외처리
	 */
	@Override
	protected ResultCode exceptionTask(EditTypes type, Step edit) throws Exception {
		
		return ResultCode.UNDEFINED_ERROR;
	}
	
	
	/**
	 * 조회조건에 해당하는 상품정보를 dhtmlx 용  xml 형태로 반환
	 * @param searchDTO - 조회조건
	 * @return
	 */
	public dhtmlXGridXml getDhtmlxRowOfScriptStepList(Step edit, String[] args) throws Exception{
		
		List<ScriptStep> result = getListOfScriptStep(edit);	
		List<ScriptStep> tree = getScriptStepTree(result,"0");
		dhtmlXGridXml xmls = DhtmlGridDataProvider.ScriptStepGrid.getRows(tree, args);
		return xmls;
	}
	
	/**
	 * 스텝 상-하위간 1차원 리스트를, 하위항목을 list로 포함하는 계층형 구조로 변환
	 * @param srcList
	 * @param key
	 * @return
	 */
	public List<ScriptStep> getScriptStepTree(List<ScriptStep> srcList, String key) {
		
		List<ScriptStep> list = srcList.stream()
				.filter(s -> s.getStepParentPk().equals(key))
				.collect(Collectors.toList());
		
		list.forEach(l -> {
			l.setSubStepList(getScriptStepTree(srcList, l.getStepPk()));
		});
		
		return list;
	}
	
	/**
	 * 조건에 맞는 스크립트 스텝 리스트 가져오기
	 * @param condition
	 * @return
	 */
	public List<ScriptStep> getListOfScriptStep(Step edit) {
		List<ScriptStep> list = this.dao.selectScriptStepList(edit);
		return list;
	}
}
