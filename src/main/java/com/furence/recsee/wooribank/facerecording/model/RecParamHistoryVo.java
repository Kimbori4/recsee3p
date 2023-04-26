package com.furence.recsee.wooribank.facerecording.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecParamHistoryVo {
	
	private String rRecKey;
	
	private String rRecDate;
	
	private String rRecTime;
	
	private String rRecParam;
	
	private String rRecRetryJson;
	
	private String callId;
	
	private String taResult;

}
