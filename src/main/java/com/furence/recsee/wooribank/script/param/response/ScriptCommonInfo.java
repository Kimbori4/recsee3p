package com.furence.recsee.wooribank.script.param.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ScriptCommonInfo {
		
	/**
	 * 공통스크립트 pk
	 */
	
	@JsonProperty("rsScriptCommonPk")
	private String commonPk;
	
	/**
	 * 수정타입
	 */	
	@JsonProperty("rsScriptCommonEditType")
	private String editType;
	
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
	@JsonProperty("rsScriptCommonRealtimeTTS")
	private String commonRealtimeTts;

}
