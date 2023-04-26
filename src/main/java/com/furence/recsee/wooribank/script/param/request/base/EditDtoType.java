package com.furence.recsee.wooribank.script.param.request.base;

import com.furence.recsee.wooribank.script.constants.EditTypes;

public interface EditDtoType {
		
	/**
	 * 스크립트 편집 타입
	 * @return
	 */
	EditTypes getEditType();
	
	/**
	 * 스크립트 데이터 키(공통,스텝,디테일)
	 * @return
	 */
	String getScriptKey();
}
