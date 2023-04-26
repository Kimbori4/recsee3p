package com.furence.recsee.wooribank.script.repository.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.furence.recsee.wooribank.script.constants.Const;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@ToString
public class ScriptApprove  implements ScriptEntity{
	
	/**
	 * 시퀀스
	 */
	@JsonIgnore
	private int seq;
	
	/**
	 * 편집 아이디 
	 */
	@JsonProperty("transactionId")
	private String scriptEditId;
	
	/**
	 * 스크립트 매핑 상품 pk
	 */
	@JsonProperty("rsProductPk")
	private String productPk;
	
	/**
	 * 현재 스크립트 버전
	 */
	@JsonProperty("rsScriptVersion")
	private int	scriptVersion;
	
	/*
	 * 편집자(상신요청자)
	 */
	@JsonProperty("rsEditUser")
	private String editUser;
	
	/*
	 * 완료여부(미완료건은 상신목록에서 제외)
	 */
	@JsonProperty("rsCompletedYN")
	private Const.Bool completedYN;
	
	/**
	 * 편집 시작일시
	 */
	@JsonIgnore
	private String editStartDate;
	
	/**
	 * 편집 완료일시
	 */
	@JsonProperty("rsEditDate")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String editEndDate;
	
	/**
	 * 적용타입
	 */
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("rsApplyType")
	private String applyType;
	
	/**
	 * 적용일(예약일)
	 */
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("rsApplyDate")
	private String applyDate;
	
	/**
	 * 적용여부
	 */
	
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("rsApplyYN")
	private Const.Bool applyYN;
	
	/**
	 * 상신상태
	 */
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("rsApplyStatus")
	private Const.Approve approveStatus;
	
	/**
	 * 상신등록일
	 */
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("rsRegistDate")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String registDate;
	
	/**
	 * 결재일
	 */
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("rsApproveDate")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String approveDate;
	
	/**
	 * 결재자
	 */
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("rsApproveUser")
	private String approveUser;

	
}
