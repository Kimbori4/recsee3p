package com.furence.recsee.wooribank.facerecording.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RetryRecReasonVo {
	
	private int rsRetryRecHistoryPk;
	
	private String rDate;
	
	private String rTime;
	
	private String rUserId;
	
	private String rCallKeyAp;
	
	private String rRetryReason;
	
	private String rRetryReasonDetail;
	
	

}
