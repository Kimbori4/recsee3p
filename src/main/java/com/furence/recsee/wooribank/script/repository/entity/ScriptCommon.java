package com.furence.recsee.wooribank.script.repository.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScriptCommon implements ScriptEntity{
	/**
	 * 공통스크립트 pk
	 */
	@JsonProperty("rsScriptCommonPk")
	private String commonPk;
	
	/**
	 * 공통스크립트 타입
	 */
	@JsonProperty("rsScriptCommonType")
	private String commonType;
	
	/**
	 * 공통스크립트 타입 이름
	 */
	@JsonProperty("rsScriptCommonTypeName")
	private String commonTypeName;

	/**
	 * 공통스크립트 이름
	 */
	@JsonProperty("rsScriptCommonName")
	private String commonName;

	/**
	 * 공통 스크립트 설명
	 */
	@JsonProperty("rsScriptCommonDesc")
	private String commonDesc;

	/**
	 * 공통스크립트 내용
	 */
	@JsonProperty("rsScriptCommonText")
	private String commonText;

	/**
	 * 실시간 TTS 여부
	 */
	@JsonProperty("rsScriptCommonRealtimeTts")
	private String commonRealtimeTts;

	/**
	 * 최초 생성일
	 */
    @JsonProperty("rsScriptCommonCreateDate")
    private String commonCreateDate;

	/**
	 * 최초 생성자
	 */
	@JsonProperty("rsScriptCommonCreateUser")
	private String commonCreateUser;

	/**
	 * 최근 수정일자
	 */
	@JsonProperty("rsScriptCommonUpdateDate")
	private String commonUpdateDate;

	/**
	 * 최근 수정자
	 */
	@JsonProperty("rsScriptCommonUpdateUser")
	private String commonUpdateUser;

	/**
	 * 결재여부
	 */
	@JsonProperty("rsScriptCommonConfirm")
	private String commonConfirm;

	/**
	 * 결재일자
	 */
	@JsonProperty("rsScriptCommonConfirmDate")
	private String commonConfirmDate;

	/**
	 * 결재자
	 */
	@JsonProperty("rsScriptCommonConfirmUser")
	private String commonConfirmUser;

	/**
	 * 예약일
	 */
	@JsonProperty("rsScriptCommonReservDate")
	private String commonReservDate;

	/**
	 * 사용여부
	 */
	@JsonProperty("rsUseYn")
	private String useYn;

	/**
	 * 스크립트 버전
	 */
	@JsonProperty("rsScriptVersion")
	private String scriptVersion;

}
