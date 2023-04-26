package com.furence.recsee.wooribank.script.repository.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ExcelScriptStepVo {
	
	private int rScriptStepPk;
	
	private int rScriptStepFk;
	
	private int rScriptStepParent;
	
	private String rScriptStepName;
	
	private int rScriptStepOrder;
	
	private String rScriptStepType;
	
	private String rUseYn;

		

}
