package com.furence.recsee.wooribank.script.param.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ProductScriptCompare {
	
	/**
	 * 현재 스크립트 정보
	 */
	@JsonInclude(Include.ALWAYS)
	private ScriptInfo before;
	
	/**
	 * 적용예정인 스크립트 정보
	 */
	@JsonInclude(Include.ALWAYS)
	private ScriptInfo after;

}
