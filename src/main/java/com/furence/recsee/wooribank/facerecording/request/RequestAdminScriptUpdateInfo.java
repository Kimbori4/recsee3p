package com.furence.recsee.wooribank.facerecording.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestAdminScriptUpdateInfo {
	
	private String detailPk;
	
	private RequestScriptDetailInfo info;

}
