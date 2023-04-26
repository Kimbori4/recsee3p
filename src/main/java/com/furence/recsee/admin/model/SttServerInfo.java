package com.furence.recsee.admin.model;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class SttServerInfo {
	
	private String rSysCode;
	private String rSysName;
	private String rSysIp;
	
	public String getrSysCode() {
		return rSysCode;
	}

	public String getrSysName() {
		return rSysName;
	}

	public String getrSysIp() {
		return rSysIp;
	}

	public void setrSysCode(String rSysCode) {
		this.rSysCode = rSysCode;
	}

	public void setrSysName(String rSysName) {
		this.rSysName = rSysName;
	}

	public void setrSysIp(String rSysIp) {
		this.rSysIp = rSysIp;
	}

	@Override
	public String toString() {
		return "SttServerInfo [sysCode=" + rSysCode + ", sysName=" + rSysName
				+ ", sysIp=" + rSysIp + "]";
	}
	
	public String toLogString(MessageSource messageSource) { 
		
		return 	messageSource.getMessage("log.SttServerInfo.SttServerInfo",null,Locale.getDefault())+" [ "   
				+ (rSysCode != 				null ? " "+messageSource.getMessage("log.SttServerInfo.sysCode",null,Locale.getDefault())+"=" 			+rSysCode+" "		: "")
				+ (rSysName != 				null ? " "+messageSource.getMessage("log.SttServerInfo.sysName",null,Locale.getDefault())+"=" 			+rSysName+" " 	: "") 
				+ (rSysIp != 				null ? " "+messageSource.getMessage("log.SttServerInfo.sysIp",null,Locale.getDefault())+"=" 			+rSysIp+" " 		: "")
				+ "]";		
	}
}
