package com.furence.recsee.common.model;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;

public class MMenuAccessInfo {

	private String programCode;
	private String levelCode;
	private String programSrc;
	private String programName;
	private String accessLevel;
	private String readYn;
	private String writeYn;
	private String modiYn;
	private String delYn;
	private String listenYn;
	private String downloadYn;
	private String excelYn;
	
	private String maskingYn;
	private String prereciptYn;
	private String reciptYn;
	private String approveYn;
	private String downloadApprove;
	private String encYn;
	private String uploadYn;
	private String bestcallYn;

	private String feedbackModifyYn;

	private String levelName;
	private String getLevelName;
	
	
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public String getGetLevelName() {
		return getLevelName;
	}
	public void setGetLevelName(String getLevelName) {
		this.getLevelName = getLevelName;
	}
	
	public String getFeedbackModifyYn() {
		return feedbackModifyYn;
	}
	public void setFeedbackModifyYn(String feedbackModifyYn) {
		this.feedbackModifyYn = feedbackModifyYn;
	}
	private String programTop;
	private String programPath;
	private Integer displayLevel;
	private Integer topPriority;
	

	public String getBestcallYn() {
		return bestcallYn;
	}
	public void setBestcallYn(String bestcallYn) {
		this.bestcallYn = bestcallYn;
	}
	public String getUploadYn() {
		return uploadYn;
	}
	public void setUploadYn(String uploadYn) {
		this.uploadYn = uploadYn;
	}
	public String getProgramCode() {
		return programCode;
	}
	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public String getProgramSrc() {
		return programSrc;
	}
	public void setProgramSrc(String programSrc) {
		this.programSrc = programSrc;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getAccessLevel() {
		return accessLevel;
	}
	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}
	public String getReadYn() {
		return readYn;
	}
	public void setReadYn(String readYn) {
		this.readYn = readYn;
	}
	public String getModiYn() {
		return modiYn;
	}
	public void setModiYn(String modiYn) {
		this.modiYn = modiYn;
	}
	public String getDelYn() {
		return delYn;
	}
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}
	public String getListenYn() {
		return listenYn;
	}
	public void setListenYn(String listenYn) {
		this.listenYn = listenYn;
	}
	public String getDownloadYn() {
		return downloadYn;
	}
	public void setDownloadYn(String downloadYn) {
		this.downloadYn = downloadYn;
	}
	public String getExcelYn() {
		return excelYn;
	}
	public String getWriteYn() {
		return writeYn;
	}
	public void setWriteYn(String writeYn) {
		this.writeYn = writeYn;
	}
	public void setExcelYn(String excelYn) {
		this.excelYn = excelYn;
	}
	public void setDisplayLevel(Integer displayLevel) {
		this.displayLevel = displayLevel;
	}
	public void setTopPriority(Integer topPriority) {
		this.topPriority = topPriority;
	}
	public String getProgramTop() {
		return programTop;
	}
	public void setProgramTop(String programTop) {
		this.programTop = programTop;
	}
	public String getProgramPath() {
		return programPath;
	}
	public void setProgramPath(String programPath) {
		this.programPath = programPath;
	}
	public int getDisplayLevel() {
		return displayLevel;
	}
	public void setDisplayLevel(int displayLevel) {
		this.displayLevel = displayLevel;
	}
	public int getTopPriority() {
		return topPriority;
	}
	public void setTopPriority(int topPriority) {
		this.topPriority = topPriority;
	}
	public String getMaskingYn() {
		return maskingYn;
	}
	public void setMaskingYn(String maskingYn) {
		this.maskingYn = maskingYn;
	}
	
	public String getPrereciptYn() {
		return prereciptYn;
	}
	public void setPrereciptYn(String prereciptYn) {
		this.prereciptYn = prereciptYn;
	}
	public String getReciptYn() {
		return reciptYn;
	}
	public void setReciptYn(String reciptYn) {
		this.reciptYn = reciptYn;
	}
	public String getApproveYn() {
		return approveYn;
	}
	public void setApproveYn(String approveYn) {
		this.approveYn = approveYn;
	}
	
	public String getDownloadApprove() {
		return downloadApprove;
	}
	public void setDownloadApprove(String downloadApprove) {
		this.downloadApprove = downloadApprove;
	}
	public String getEncYn() {
		return encYn;
	}
	public void setEncYn(String encYn) {
		this.encYn = encYn;
	}

	
	public MMenuAccessInfo getNowAccessRow(List<MMenuAccessInfo> menuAccessInfoList, String nowMenu) {
		MMenuAccessInfo nowAccessInfo = new MMenuAccessInfo();
		if(menuAccessInfoList!=null || menuAccessInfoList.size()>0) {
			Integer getMenuAccessTotal = menuAccessInfoList.size();
			for(int i = 0; i < getMenuAccessTotal; i++) {
				MMenuAccessInfo accessInfo = menuAccessInfoList.get(i);
				if(nowMenu.equals(accessInfo.getProgramSrc())) {
					nowAccessInfo = accessInfo;
					break;
				}
			}			
		}else {
			nowAccessInfo = null;
		}		 
		return nowAccessInfo;
	}
	
	public String toString(MessageSource messageSource) {
		return messageSource.getMessage("log.MMenuAccessInfo.MMenuAccessInfo",null,Locale.getDefault())
			+ " ["
			+ (programCode != null	? messageSource.getMessage("log.MMenuAccessInfo.programCode",null,Locale.getDefault())+"=" 		+ (programName!=null? programName+"("+programCode+")":programCode) : "" ) 
			+ (levelCode != null 	? ", "+messageSource.getMessage("log.MMenuAccessInfo.levelCode",null,Locale.getDefault())+"="	+ (levelName!=null? levelName+"("+levelCode+")":levelCode) : "")
			+ (accessLevel != null 	? ", "+messageSource.getMessage("log.MMenuAccessInfo.accessLevel",null,Locale.getDefault())+"="	+ accessLevel : "")
			+ (readYn != null 		? ", "+messageSource.getMessage("log.MMenuAccessInfo.readYn",null,Locale.getDefault())+"="		+ readYn : "")
			+ (modiYn != null 		? ", "+messageSource.getMessage("log.MMenuAccessInfo.modiYn",null,Locale.getDefault())+"="		+ modiYn : "")
			+ (delYn != null  		? ", "+messageSource.getMessage("log.MMenuAccessInfo.delYn",null,Locale.getDefault())+"=" 		+ delYn : "")
			+ (listenYn != null 	? ", "+messageSource.getMessage("log.MMenuAccessInfo.listenYn",null,Locale.getDefault())+"=" 	+ listenYn : "")
			+ (downloadYn != null 	? ", "+messageSource.getMessage("log.MMenuAccessInfo.downloadYn",null,Locale.getDefault())+"=" 	+ downloadYn : "")
			+ (excelYn != null 		? ", "+messageSource.getMessage("log.MMenuAccessInfo.excelYn",null,Locale.getDefault())+"=" 	+ excelYn : "")
			+ (maskingYn != null	? ", "+messageSource.getMessage("log.MMenuAccessInfo.maskingYn",null,Locale.getDefault())+"="  	+ maskingYn : "")
			+ (prereciptYn != null 	? ", "+messageSource.getMessage("log.MMenuAccessInfo.prereciptYn",null,Locale.getDefault())+"="	+ prereciptYn : "")
			+ (reciptYn != null 	? ", "+messageSource.getMessage("log.MMenuAccessInfo.reciptYn",null,Locale.getDefault())+"=" 	+ reciptYn : "")
			+ (approveYn != null 	? ", "+messageSource.getMessage("log.MMenuAccessInfo.approveYn",null,Locale.getDefault())+"="	+ approveYn  : "")
			+ (encYn != null 	 	? ", "+messageSource.getMessage("log.MMenuAccessInfo.encYn",null,Locale.getDefault())+"=" 		+ encYn : "")
			+ (downloadApprove != null 	? ", "+messageSource.getMessage("log.MMenuAccessInfo.downloadApprove",null,Locale.getDefault())+"="	+ downloadApprove : "")
			+ (feedbackModifyYn != null ? ", "+messageSource.getMessage("log.MMenuAccessInfo.feedbackModifyYn",null,Locale.getDefault())+"=" + feedbackModifyYn : "")
			+ (programTop != null 	? ", "+messageSource.getMessage("log.MMenuAccessInfo.programTop",null,Locale.getDefault())+"=" 	+ programTop : "")
			+ (programPath != null 	? ", "+messageSource.getMessage("log.MMenuAccessInfo.programPath",null,Locale.getDefault())+"=" + programPath : "")
			+ (displayLevel != null ? ", "+messageSource.getMessage("log.MMenuAccessInfo.displayLevel",null,Locale.getDefault())+"="+ displayLevel : "")
			+ (topPriority != null 	? ", "+messageSource.getMessage("log.MMenuAccessInfo.topPriority",null,Locale.getDefault())+"=" + topPriority : "")
			+ "]";
	}
}
