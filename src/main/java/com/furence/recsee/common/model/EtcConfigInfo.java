package com.furence.recsee.common.model;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class EtcConfigInfo {
	
	private String groupKey;
	private String configKey;
	
	private String mGroupKey;
	private String mConfigKey;
	
	private String configValue;
	private String configOption;
	private String desc;

	public String getConfigOption() {
		return configOption;
	}
	public void setConfigOption(String configOption) {
		this.configOption = configOption;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getGroupKey() {
		return groupKey;
	}
	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	public String getConfigKey() {
		return configKey;
	}
	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigValue() {
		return configValue;
	}
	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}
	public String getmGroupKey() {
		return mGroupKey;
	}
	public void setmGroupKey(String mGroupKey) {
		this.mGroupKey = mGroupKey;
	}
	public String getmConfigKey() {
		return mConfigKey;
	}
	public void setmConfigKey(String mConfigKey) {
		this.mConfigKey = mConfigKey;
	}
	
	public String toLogString(MessageSource messageSource) {
		return 	messageSource.getMessage("log.EtcConfigInfo.EtcConfigInfo",null,Locale.getDefault())+" [ "  
				+ (groupKey != 			null ? " "+messageSource.getMessage("log.EtcConfigInfo.groupKey",null,Locale.getDefault())+"=" 				+groupKey+" " 		: "")
				+ (mGroupKey != 		null ? " "+messageSource.getMessage("log.EtcConfigInfo.mGroupKey",null,Locale.getDefault())+"=" 			+mGroupKey+" " 		: "")
				+ (configKey != 		null ? " "+messageSource.getMessage("log.EtcConfigInfo.configKey",null,Locale.getDefault())+"="		 			+configKey+" " 		: "")
				+ (mConfigKey != 		null ? " "+messageSource.getMessage("log.EtcConfigInfo.mConfigKey",null,Locale.getDefault())+"="			+mConfigKey+" " 		: "")
				+ (configValue != 		null ? " "+messageSource.getMessage("log.EtcConfigInfo.configValue",null,Locale.getDefault())+"=" 				+configValue+" " 	: "") 
				+ " ]";
	}
}
