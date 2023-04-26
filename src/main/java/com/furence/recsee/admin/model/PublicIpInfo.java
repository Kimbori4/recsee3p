package com.furence.recsee.admin.model;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class PublicIpInfo {
	
	private String rPublicIp;

	private String rPublicIpOld;

	public String getrPublicIp() {
		return rPublicIp;
	}

	public String getrPublicIpOld() {
		return rPublicIpOld;
	}

	public void setrPublicIp(String rPublicIp) {
		this.rPublicIp = rPublicIp;
	}

	public void setrPublicIpOld(String rPublicIpOld) {
		this.rPublicIpOld = rPublicIpOld;
	}

	@Override
	public String toString() {
		return "PublicIpInfo [rPublicIp=" + rPublicIp + ", rPublicIpOld=" + rPublicIpOld + "]";
	}
	
	public String toLogString(MessageSource messageSource) { 
		
		return 	messageSource.getMessage("log.PublicIpInfo.PublicIpInfo",null,Locale.getDefault())+" [ "   
				+ (rPublicIp != 				null ? " "+messageSource.getMessage("log.PublicIpInfo.rPublicIp",null,Locale.getDefault())+"=" 		+rPublicIp+" "		: "")
				+ (rPublicIpOld != 			null ? " "+messageSource.getMessage("log.PublicIpInfo.rPublicIpOld",null,Locale.getDefault())+"=" 	+rPublicIpOld+" "		: "")
				+ "]";		
	}
}
