package com.furence.recsee.wooribank.script.service.types;

import com.furence.recsee.wooribank.script.param.request.base.TransactionDtoType;
import com.furence.recsee.wooribank.script.param.response.ResultBase;

public interface ApprovalService<Tran extends TransactionDtoType> {
	
	/**
	 * 결재 상신처리
	 * @param tran
	 * @return
	 */
	ResultBase approveEdittedScript(Tran tran);
}
