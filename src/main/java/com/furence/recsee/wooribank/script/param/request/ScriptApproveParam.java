package com.furence.recsee.wooribank.script.param.request;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furence.recsee.wooribank.script.constants.Const;
import com.furence.recsee.wooribank.script.constants.Const.Apply;
import com.furence.recsee.wooribank.script.constants.Const.Approve;
import com.furence.recsee.wooribank.script.param.request.base.Paging;
import com.furence.recsee.wooribank.script.param.request.base.TransactionDtoType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScriptApproveParam implements TransactionDtoType{
	
	/**
	 *  스크립트 수정테이블 pk
	 */
	@JsonProperty("rsScriptEditId")
	private String scriptEditId;
	
	/**
	 * 공통 스크립트 pk
	 */
	@JsonProperty("rsScriptCommonPk")
	private String commonScriptPk;
	
	/**
	 * 해당 스크립트가 속한 상품의 고유키
	 */
	@JsonProperty("rsProductPk")
	private String productPk;
	
	/**
	 * 상신건에 대해 처리할 상태(취소,반려,결재)
	 */
	@JsonProperty("rsApproveStatus")
	private Const.Approve approveStatus;
	
	/**
	 * 상품,공용 구분
	 */
	@JsonProperty("rsScriptType")
	private Const.ScriptKind scriptType;
	
	/**
	 * 적용타입
	 */
	@JsonProperty("rsApplyType")
	private Const.Apply applyType;
	
	/**
	 * 적용예정일
	 */
	@JsonProperty("rsApplyDate")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String applyDate;
	
	/**
	 * 상신유저
	 */
	@JsonIgnore
	private String approveUser;
	
	@Override
	public String getTransactionId() {		
		return this.scriptEditId;
	}
	
	public boolean isApplyImmediately() {
		return this.approveStatus == Approve.APPROVE 
				&& this.applyType == Apply.IMMEDIATELY;
	}
	
	private int procedureCode;
	
	private String procedureMessage;
	
	@Getter
	@Setter
	@ToString(callSuper = true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Search extends Paging{
		
		/**
		 * 스크립트 타입
		 */
		@JsonProperty("scriptType")
		private String scriptType;
		
		/**
		 * 검색어
		 */
		@JsonProperty("keyword")
		private String keyword;
		
		/**
		 * 상신 목록 혹은 결재대기목록(권한에 따라 구분)
		 */
		@JsonProperty("approverYN")
		private String approverYN;
		
		/**
		 * 상신 목록 혹은 결재대기목록(권한에 따라 구분)
		 */
		private String editUser;
		
		/**
		 * 상품부서
		 */
		private String productPart = "";
		
		/**
		 * 조회 시작일
		 */
		@JsonProperty("startDate")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private String startDate;
		
		/**
		 * 조회 종료일
		 */
		@JsonProperty("endDate")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private String endDate;
		
		/**
		 * 결재상태
		 */
		@JsonProperty("approveStatus")
		private String approveStatus;
		
		public String getApproveStatusCode() {
			if(this.approveStatus == null) return null;
			return Const.Approve.create(this.approveStatus).getValue();
		}
		
	}
}