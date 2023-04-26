package com.furence.recsee.wooribank.script.param.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furence.recsee.wooribank.script.constants.Const;
import com.furence.recsee.wooribank.script.constants.EditTypes;
import com.furence.recsee.wooribank.script.param.request.base.TransactionDtoType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScriptEditBase implements TransactionDtoType{

	/**
	 * 편집 트랜잭션 아이디
	 */
	@JsonProperty("transactionId")	
	private String transactionId;
	
	
	/**
	 * 수정타입 (C,U,D)
	 */	
	private EditTypes editType;
	
		
	/**
	 * 수정자
	 */
	private String editUser;
	
	/**
	 * 적용일 타입
	 */
	private Const.Apply applyType;
	
	/**
	 * 적용 예약일
	 */
	private String applyDate;
	
	/**
	 * 승인상태
	 */
	@JsonProperty("rsApprovementStatus")
	private Const.Approve approveStatus;
	
	/**
	 * 승인일
	 */
	private String approveDate;
	
	/**
	 * 승인자
	 */
	private String approveUser;
	
	@Override
	public String getTransactionId() {
		return transactionId;
	}

}
