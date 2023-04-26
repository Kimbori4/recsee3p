package com.furence.recsee.common.model;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class SystemInfo {
	
	private String sysId;
	private String sysName;
	private String sysIp;
	private String pbxId;
	private String storagePath;
	private String channelUpdateFlag;
	private String channelUpdateReqTime;
	private String channelUpdateProcTime;
	private String sysLicence;
	private String licUse;
	private String licNouse;
	private String licEnc;
	private String switchSysYn;
	private String switchSysCode;
	private String switchSysName;
	private String switchChNum;

	private String sysDeleteYN;
	private String sysDeleteSize;
	private String sysDeletePath;
	
	
	public String getSwitchSysName() {
		return switchSysName;
	}
	public void setSwitchSysName(String switchSysName) {
		this.switchSysName = switchSysName;
	}
	
	public String getSysDeleteYN() {
		return sysDeleteYN;
	}
	public void setSysDeleteYN(String sysDeleteYN) {
		this.sysDeleteYN = sysDeleteYN;
	}
	public String getSysDeleteSize() {
		return sysDeleteSize;
	}
	public void setSysDeleteSize(String sysDeleteSize) {
		this.sysDeleteSize = sysDeleteSize;
	}
	public String getSysDeletePath() {
		return sysDeletePath;
	}
	public void setSysDeletePath(String sysDeletePath) {
		this.sysDeletePath = sysDeletePath;
	}
	public String getSwitchChNum() {
		return switchChNum;
	}
	public void setSwitchChNum(String switchChNum) {
		this.switchChNum = switchChNum;
	}
	public String getLicEnc() {
		return licEnc;
	}
	public void setLicEnc(String licEnc) {
		this.licEnc = licEnc;
	}
	public String getLicUse() {
		return licUse;
	}
	public void setLicUse(String licUse) {
		this.licUse = licUse;
	}
	public String getLicNouse() {
		return licNouse;
	}
	public void setLicNouse(String licNouse) {
		this.licNouse = licNouse;
	}
	public String getSysLicence() {
		return sysLicence;
	}
	public void setSysLicence(String sysLicence) {
		this.sysLicence = sysLicence;
	}
	public String getSysId() {
		return sysId;
	}
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getSysIp() {
		return sysIp;
	}
	public void setSysIp(String sysIp) {
		this.sysIp = sysIp;
	}

	public String getPbxId() {
		return pbxId;
	}
	public void setPbxId(String pbxId) {
		this.pbxId = pbxId;
	}

	public String getStoragePath() {
		return storagePath;
	}
	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}
	public String getChannelUpdateFlag() {
		return channelUpdateFlag;
	}
	public void setChannelUpdateFlag(String channelUpdateFlag) {
		this.channelUpdateFlag = channelUpdateFlag;
	}
	public String getChannelUpdateReqTime() {
		return channelUpdateReqTime;
	}
	public void setChannelUpdateReqTime(String channelUpdateReqTime) {
		this.channelUpdateReqTime = channelUpdateReqTime;
	}
	public String getChannelUpdateProcTime() {
		return channelUpdateProcTime;
	}
	public void setChannelUpdateProcTime(String channelUpdateProcTime) {
		this.channelUpdateProcTime = channelUpdateProcTime;
	}
	public String getSwitchSysYn() {
		return switchSysYn;
	}
	public void setSwitchSysYn(String switchSysYn) {
		this.switchSysYn = switchSysYn;
	}
	public String getSwitchSysCode() {
		return switchSysCode;
	}
	public void setSwitchSysCode(String switchSysCode) {
		this.switchSysCode = switchSysCode;
	}
	
	@Override
	public String toString() {
		return "SystemInfo [sysId=" + sysId + ", sysName=" + sysName
				+ ", sysIp=" + sysIp + ", pbxId=" + pbxId + ", storagePath="
				+ storagePath + ", channelUpdateFlag=" + channelUpdateFlag
				+ ", channelUpdateReqTime=" + channelUpdateReqTime
				+ ", channelUpdateProcTime=" + channelUpdateProcTime + "]";
	}
	
	public String toLogString(MessageSource messageSource) { 
		
		return 	messageSource.getMessage("log.SystemInfo.SystemInfo",null,Locale.getDefault())+" [ "   
				+ (sysId != 				null ? " "+messageSource.getMessage("log.SystemInfo.sysId",null,Locale.getDefault())+"=" 			+sysId+" "		: "")
				+ (sysName != 				null ? " "+messageSource.getMessage("log.SystemInfo.sysName",null,Locale.getDefault())+"=" 			+sysName+" " 	: "") 
				+ (sysIp != 				null ? " "+messageSource.getMessage("log.SystemInfo.sysIp",null,Locale.getDefault())+"=" 			+sysIp+" " 		: "")
				+ (pbxId != 				null ? " "+messageSource.getMessage("log.SystemInfo.pbxId",null,Locale.getDefault())+"=" 			+pbxId+" " 		: "")
				+ (storagePath != 			null ? " "+messageSource.getMessage("log.SystemInfo.storagePath",null,Locale.getDefault())+"=" 		+storagePath+" ": "") 	
				+ "]";		
	}
}
