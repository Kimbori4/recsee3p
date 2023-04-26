package com.furence.recsee.wooribank.script.repository.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductScriptVariable {
	
	/**
	 * 상품 가변값 PK
	 */
	@JsonProperty("rsProductValuePk")
	private String productValuePk;

	/**
	 * 상품타입
	 */
	@JsonProperty("rsProductType")
	private String productType;

	/**
	 * 상품코드
	 */
	@JsonProperty("rsProductCode")
	private String productCode;

	/**
	 * 가변값 표시이름
	 */	
	@JsonProperty("rsProductValueName")
	private String variableName;

	/**
	 * 가변값 코드
	 */	
	@JsonProperty("rsProductValueCode")
	private String variableCode;

	/**
	 * 가변값 값(실제 TTS 변환할 때 쓰이는 값)
	 */
	@JsonProperty("rsProductValueVal")
	private String variableValue;

	/**
	 * 상품 가변값 PK
	 */
	@JsonProperty("rsProductValueRealtimeTTS")
	private String resalTimeTTS;

	/**
	 * sysdis - 해당상품의 sysdis type
	 */
	@JsonProperty("rsSysdisType")
	private String sysdisType;

	/**
	 * 최종 업데이트 시각
	 */
	@JsonProperty("rsUpdateTime")
	private LocalDateTime updateTime;
	
}
