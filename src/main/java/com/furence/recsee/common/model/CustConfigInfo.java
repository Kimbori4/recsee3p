package com.furence.recsee.common.model;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class CustConfigInfo {
	
	private String rCustCode;
	private String rConfigKey;
	private String rConfigValue;
	private String desc;
	
	public String getrCustCode() {
		return rCustCode;
	}
	public String getrConfigKey() {
		return rConfigKey;
	}
	public String getrConfigValue() {
		return rConfigValue;
	}
	public String getDesc() {
		return desc;
	}
	public void setrCustCode(String rCustCode) {
		this.rCustCode = rCustCode;
	}
	public void setrConfigKey(String rConfigKey) {
		this.rConfigKey = rConfigKey;
	}
	public void setrConfigValue(String rConfigValue) {
		this.rConfigValue = rConfigValue;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String toLogString(MessageSource messageSource) {
		return 	messageSource.getMessage("log.CustConfigInfo.CustConfigInfo",null,Locale.getDefault())+" [ "  
				+ (rCustCode != 		null ? " "+messageSource.getMessage("log.CustConfigInfo.rCustCode",null,Locale.getDefault())+"=" 					+rCustCode+" " 		: "")
				+ (rConfigKey != 		null ? " "+messageSource.getMessage("log.CustConfigInfo.rConfigKey",null,Locale.getDefault())+"="		 			+rConfigKey+" " 	: "")
				+ (rConfigValue != 		null ? " "+messageSource.getMessage("log.CustConfigInfo.rConfigValue",null,Locale.getDefault())+"=" 				+rConfigValue+" " 	: "") 
				+ (desc != 				null ? " "+messageSource.getMessage("log.CustConfigInfo.desc",null,Locale.getDefault())+"=" 						+desc+" " 			: "") 
				+ " ]";
	}
}
