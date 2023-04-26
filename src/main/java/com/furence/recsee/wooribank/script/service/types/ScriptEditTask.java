package com.furence.recsee.wooribank.script.service.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.furence.recsee.wooribank.script.constants.EditTypes;
import com.furence.recsee.wooribank.script.param.response.ResultBase;
import com.furence.recsee.wooribank.script.param.response.ResultCode;

@Service
public abstract class ScriptEditTask<T1, T2> {
	
	private static final Logger logger = LoggerFactory.getLogger(ScriptEditTask.class);
	public enum NextTask {
		Main,
		After,
		AfterOther,
		Stop;
	}
	
	/**
	 * task 실행 전처리 작업
	 * @param type
	 * @param edit
	 * @return
	 * @throws Exception
	 */
	abstract protected NextTask preTask(EditTypes type, T1 edit) throws Exception;
	
	/**
	 * 업데이트 처리
	 * @param type
	 * @param edit
	 * @return
	 */
	abstract protected ResultCode preTaskAfter(EditTypes type, T1 edit) throws Exception;
	
	
	/**  
	 * 삭제처리
	 * @param type
	 * @param edit
	 * @return
	 */
	abstract protected ResultCode preTaskAfterOther(EditTypes type, T1 edit) throws Exception;
	
	
	/**
	 * 메인 작업
	 * @param type
	 * @param edit
	 * @return
	 */
	abstract protected ResultCode mainTask(EditTypes type, T1 edit) throws Exception ;
	
	/**
	 * 메인 후 처리 작업
	 */
	abstract protected void afterTask(EditTypes type, T1 edit , ResultCode resultCode) throws Exception;
	
	
	/**
	 * 에러 처리
	 */
	abstract protected ResultCode exceptionTask(EditTypes type, T1 edit ) throws Exception;
	
	
	/**
	 * 편집데이터 퍼사드
	 * @param type
	 * @param edit
	 * @return
	 */
	public ResultBase runTask(EditTypes type, T1 edit) {
		
		try {
			
			ResultCode resultCode = ResultCode.SYSTEM_ERROR; 
			
			switch( preTask(type, edit) ) {
			case After:
				resultCode = preTaskAfter(type, edit);
				break;
			case AfterOther:
				resultCode = preTaskAfterOther(type, edit);
				break;
			case Main:
				resultCode = mainTask(type, edit);
				break;
			case Stop:
				return ResultBase.from(exceptionTask(type, edit));
			}	
			
			if( resultCode == ResultCode.SUCCESS) {
				afterTask(type, edit , resultCode);
			}
			
			return ResultBase.from(resultCode);
			
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		return ResultBase.from(ResultCode.SYSTEM_ERROR);
	}
}
