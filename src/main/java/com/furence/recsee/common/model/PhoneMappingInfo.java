package com.furence.recsee.common.model;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class PhoneMappingInfo {
	
	private String custPhone;
	private String custNickName;
	private String useNickName;
	private String custPhoneKey;
	private String procType;
	private String procPosition;

	public String getCustPhone() {
		return custPhone;
	}

	public void setCustPhone(String custPhone) {
		this.custPhone = custPhone;
	}

	public String getCustNickName() {
		return custNickName;
	}

	public void setCustNickName(String custNickName) {
		this.custNickName = custNickName;
	}

	public String getUseNickName() {
		return useNickName;
	}

	public void setUseNickName(String useNickName) {
		this.useNickName = useNickName;
	}

	public String getCustPhoneKey() {
		return custPhoneKey;
	}

	public void setCustPhoneKey(String custPhoneKey) {
		this.custPhoneKey = custPhoneKey;
	}

	public String getProcType() {
		return procType;
	}

	public void setProcType(String procType) {
		this.procType = procType;
	}

	public String getProcPosition() {
		return procPosition;
	}

	public void setProcPosition(String procPosition) {
		this.procPosition = procPosition;
	}

	@Override
	public String toString() {
		return "PhoneMappingInfo [custPhone=" + custPhone + ", custNickName=" + custNickName
				+ ", useNickName=" + useNickName + ", custPhoneKey=" + custPhoneKey
				+ ", procType=" + procType + ", procPosition=" + procPosition + "]";
	}
	
	public String toLogString(MessageSource messageSource) { 
		
		return 	messageSource.getMessage("log.PhoneMappingInfo.PhoneMappingInfo",null,Locale.getDefault())+" [ "   
				+ (custPhone != 				null ? " "+messageSource.getMessage("log.PhoneMappingInfo.custPhone",null,Locale.getDefault())+"=" 					+custPhone+" "			: "")
				+ (custNickName != 				null ? " "+messageSource.getMessage("log.PhoneMappingInfo.custNickName",null,Locale.getDefault())+"=" 				+custNickName+" " 		: "") 
				+ (useNickName != 				null ? " "+messageSource.getMessage("log.PhoneMappingInfo.useNickName",null,Locale.getDefault())+"=" 				+useNickName+" " 		: "")
				+ (custPhoneKey != 				null ? " "+messageSource.getMessage("log.PhoneMappingInfo.custPhoneKey",null,Locale.getDefault())+"=" 				+custPhoneKey+" " 		: "")
				+ (procType != 					null ? " "+messageSource.getMessage("log.PhoneMappingInfo.procType",null,Locale.getDefault())+"=" 					+procType+" " 			: "")
				+ (procPosition != 				null ? " "+messageSource.getMessage("log.PhoneMappingInfo.procPosition",null,Locale.getDefault())+"=" 				+procPosition+" " 		: "")
				+ "]";		
	}
}
