package com.furence.recsee.wooribank.facerecording.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@Builder
@ToString
public class BkProductAndScriptStepDetail {
	
	private String productCode;
	
	private List<ScriptStepDetailVo> detailList; 

}
