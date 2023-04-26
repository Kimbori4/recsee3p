package com.furence.recsee.common.model;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class Log {
	
	private	String rLogDate;
	private	String rLogTime;
	private String rLogCode;
	private String rLogName;
	private String rLogContents;
	private String rLogDetailCode;
	private String rLogLevel;
	private String rLogIp;
	private String rLogServerIp;
	private String rLogUserId;
	private String rLogEtc;
	
	private String sDate;
	private String eDate;
	private String sTime;
	private String eTime;

	private String topCount;
	private String limitUse;

	private Integer count;
	private Integer posStart;
	
	private String orderBy;
	private String direction;	
	
	public String getrLogDate() {
		String tmpDate = null;
		if(rLogDate != null)	tmpDate = String.format("%s-%s-%s", rLogDate.substring(0, 4), rLogDate.substring(4,  6), rLogDate.substring(6, 8));

		return tmpDate;
	}
	public void setrLogDate(String rLogDate) {
		this.rLogDate = rLogDate;
	}
	public String getrLogTime() {
		String tmpTime = null;
		if(rLogTime != null) tmpTime = String.format("%s:%s:%s", rLogTime.substring(0, 2), rLogTime.substring(2,  4), rLogTime.substring(4, 6));

		return tmpTime;
	}
	public void setrLogTime(String rLogTime) {
		this.rLogTime = rLogTime;
	}
	public String getrLogIp() {
		return rLogIp;
	}
	public void setrLogIp(String rLogIp) {
		this.rLogIp = rLogIp;
	}
	public String getrLogServerIp() {
		return rLogServerIp;
	}
	public void setrLogServerIp(String rLogServerIp) {
		this.rLogServerIp = rLogServerIp;
	}
	public String getrLogUserId() {
		return rLogUserId;
	}
	public void setrLogUserId(String rLogUserId) {
		this.rLogUserId = rLogUserId;
	}
	
	public String getrLogEtc() {
		return rLogEtc;
	}
	public void setrLogEtc(String rLogEtc) {
		this.rLogEtc = rLogEtc;
	}
	public String getsDate() {
		return sDate;
	}
	public void setsDate(String sDate) {
		this.sDate = sDate;
	}
	public String geteDate() {
		return eDate;
	}
	public void seteDate(String eDate) {
		this.eDate = eDate;
	}
	public String getsTime() {
		return sTime;
	}
	public void setsTime(String sTime) {
		this.sTime = sTime;
	}
	public String geteTime() {
		return eTime;
	}
	public void seteTime(String eTime) {
		this.eTime = eTime;
	}
	public String getTopCount() {
		return topCount;
	}
	public void setTopCount(String topCount) {
		this.topCount = topCount;
	}
	public String getLimitUse() {
		return limitUse;
	}
	public void setLimitUse(String limitUse) {
		this.limitUse = limitUse;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getPosStart() {
		return posStart;
	}
	public void setPosStart(Integer posStart) {
		this.posStart = posStart;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getrLogCode() {
		return rLogCode;
	}
	public void setrLogCode(String rLogCode) {
		this.rLogCode = rLogCode;
	}
	public String getrLogDetailCode() {
		return rLogDetailCode;
	}
	public void setrLogDetailCode(String rLogDetailCode) {
		this.rLogDetailCode = rLogDetailCode;
	}
	public String getrLogLevel() {
		return rLogLevel;
	}
	public void setrLogLevel(String rLogLevel) {
		this.rLogLevel = rLogLevel;
	}
	public String getrLogName() {
		return rLogName;
	}
	public void setrLogName(String rLogName) {
		this.rLogName = rLogName;
	}
	public String getrLogContents() {
		return rLogContents;
	}
	public void setrLogContents(String rLogContents) {
		this.rLogContents = rLogContents;
	}
	@Override
	public String toString() {
		return "Log [rLogDate=" + rLogDate + ", rLogTime=" + rLogTime + ", rLogCode=" + rLogCode + ", rLogName="
				+ rLogName + ", rLogContents=" + rLogContents + ", rLogDetailCode=" + rLogDetailCode + ", rLogLevel="
				+ rLogLevel + ", rLogIp=" + rLogIp + ", rLogServerIp=" + rLogServerIp + ", rLogUserId=" + rLogUserId
				+ ", rLogEtc=" + rLogEtc + ", sDate=" + sDate + ", eDate=" + eDate + ", sTime=" + sTime + ", eTime="
				+ eTime + ", topCount=" + topCount + ", limitUse=" + limitUse + ", count=" + count + ", posStart="
				+ posStart + ", orderBy=" + orderBy + ", direction=" + direction + ", getrLogDate()=" + getrLogDate()
				+ ", getrLogTime()=" + getrLogTime() + ", getrLogIp()=" + getrLogIp() + ", getrLogServerIp()="
				+ getrLogServerIp() + ", getrLogUserId()=" + getrLogUserId() + ", getrLogEtc()=" + getrLogEtc()
				+ ", getsDate()=" + getsDate() + ", geteDate()=" + geteDate() + ", getsTime()=" + getsTime()
				+ ", geteTime()=" + geteTime() + ", getTopCount()=" + getTopCount() + ", getLimitUse()=" + getLimitUse()
				+ ", getCount()=" + getCount() + ", getPosStart()=" + getPosStart() + ", getOrderBy()=" + getOrderBy()
				+ ", getDirection()=" + getDirection() + ", getrLogCode()=" + getrLogCode() + ", getrLogDetailCode()="
				+ getrLogDetailCode() + ", getrLogLevel()=" + getrLogLevel() + ", getrLogName()=" + getrLogName()
				+ ", getrLogContents()=" + getrLogContents() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}	
		
	public String toLogString(MessageSource messageSource) {
		return 	messageSource.getMessage("log.Log.Log",null,Locale.getDefault())+" [ "  
				+ (rLogDate!= 		null ? messageSource.getMessage("log.Log.rLogDate",null,Locale.getDefault())+"=" 		+ rLogDate + " " : "")
				+ (rLogTime!= 		null ? messageSource.getMessage("log.Log.rLogTime",null,Locale.getDefault())+"=" 		+ rLogTime + " " : "") 
				+ (rLogCode!= 		null ? messageSource.getMessage("log.Log.rLogCode",null,Locale.getDefault())+"=" 		+ rLogCode + " " : "") 
				+ (rLogName!= 		null ? messageSource.getMessage("log.Log.rLogName",null,Locale.getDefault())+"=" 		+ rLogName + " " : "") 
				+ (rLogContents!= 	null ? messageSource.getMessage("log.Log.rLogContents",null,Locale.getDefault())+"=" 	+ rLogContents + " " : "") 
				+ (rLogDetailCode!= null ? messageSource.getMessage("log.Log.rLogDetailCode",null,Locale.getDefault())+"=" 	+ rLogDetailCode + " " : "") 
				+ (rLogLevel!= 		null ? messageSource.getMessage("log.Log.rLogLevel",null,Locale.getDefault())+"=" 		+ rLogLevel + " " : "") 
				+ (rLogIp!= 		null ? messageSource.getMessage("log.Log.rLogIp",null,Locale.getDefault())+"=" 			+ rLogIp + " " : "") 
				+ (rLogServerIp!= 	null ? messageSource.getMessage("log.Log.rLogServerIp",null,Locale.getDefault())+"=" 	+ rLogServerIp + " " : "") 
				+ (rLogUserId!= 	null ? messageSource.getMessage("log.Log.rLogUserId",null,Locale.getDefault())+"=" 		+ rLogUserId + " " : "") 
				+ (rLogEtc!= 		null ? messageSource.getMessage("log.Log.rLogEtc",null,Locale.getDefault())+"="			+ rLogEtc + " " : "") 
					
				+ (sDate!= 			null ? messageSource.getMessage("log.Log.sDate",null,Locale.getDefault())+"=" 			+ sDate + " " : "") 
				+ (eDate!= 			null ? messageSource.getMessage("log.Log.eDate",null,Locale.getDefault())+"=" 			+ eDate + " " : "") 
				+ (sTime!= 			null ? messageSource.getMessage("log.Log.sTime",null,Locale.getDefault())+"=" 			+ sTime + " " : "") 
				+ (eTime!= 			null ? messageSource.getMessage("log.Log.eTime",null,Locale.getDefault())+"=" 			+ eTime + " " : "") 
				+ "]";																						
		
	}
}
