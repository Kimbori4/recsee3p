package com.furence.recsee.common.model;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class MAccessLevelInfo {
	
	private String levelCode;
	private String levelName;
	private String levelNote;
	private Integer lockCount;
	private Integer accountDate;
	private String maxLevelCode;
	private Integer dupCheck;
	
	//허용 범위
	private Integer allowableCount;	

	public Integer getAllowableCount() {
		return allowableCount;
	}
	public void setAllowableCount(Integer allowableCount) {
		this.allowableCount = allowableCount;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public String getLevelNote() {
		return levelNote;
	}
	public void setLevelNote(String levelNote) {
		this.levelNote = levelNote;
	}
	public Integer getLockCount() {
		return lockCount;
	}
	public void setLockCount(Integer lockCount) {
		this.lockCount = lockCount;
	}
	public Integer getAccountDate() {
		return accountDate;
	}
	public void setAccountDate(Integer accountDate) {
		this.accountDate = accountDate;
	}
	public String getMaxLevelCode() {
		return maxLevelCode;
	}
	public void setMaxLevelCode(String maxLevelCode) {
		this.maxLevelCode = maxLevelCode;
	}
	public Integer getDupCheck() {
		return dupCheck;
	}
	public void setDupCheck(Integer dupCheck) {
		this.dupCheck = dupCheck;
	}
	public String toString(MessageSource messageSource) {
		return messageSource.getMessage("log.MAccessLevelInfo.MAccessLevelInfo",null,Locale.getDefault())
				+ " ["
				+      messageSource.getMessage("log.MAccessLevelInfo.levelCode",null,Locale.getDefault()) + "=" + levelCode
				+ ", "+messageSource.getMessage("log.MAccessLevelInfo.levelName",null,Locale.getDefault())+"="+ levelName/*
				 + ", levelNote=" + levelNote + ", lockCount="
				+ lockCount + ", accountDate=" + accountDate
				+ ", maxLevelCode=" + maxLevelCode + ", dupCheck=" + dupCheck*/
				+ "]";
	}
}
