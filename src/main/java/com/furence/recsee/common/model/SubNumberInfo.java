package com.furence.recsee.common.model;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class SubNumberInfo {
	
	private String telNo;
	private String nickName;
	private String use;
	private String idx;

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	@Override
	public String toString() {
		return "SubNumberInfo [telNo=" + telNo + ", nickName=" + nickName
				+ ", use=" + use + ", idx=" + idx + "]";
	}
	
	public String toLogString(MessageSource messageSource) { 
		
		return 	messageSource.getMessage("log.SubNumberInfo.SubNumberInfo",null,Locale.getDefault())+" [ "   
				+ (telNo != 				null ? " "+messageSource.getMessage("log.SubNumberInfo.telNo",null,Locale.getDefault())+"=" 		+telNo+" "		: "")
				+ (nickName != 				null ? " "+messageSource.getMessage("log.SubNumberInfo.nickName",null,Locale.getDefault())+"=" 		+nickName+" " 	: "") 
				+ (use != 				null ? " "+messageSource.getMessage("log.SubNumberInfo.use",null,Locale.getDefault())+"=" 				+use+" " 		: "")
				+ (idx != 				null ? " "+messageSource.getMessage("log.SubNumberInfo.idx",null,Locale.getDefault())+"=" 				+idx+" " 		: "")
				+ "]";		
	}
}
