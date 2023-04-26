package com.furence.recsee.admin.model;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class ExecuteManage {
	
	private String executeName;
	private String jobName;
	private String rSchedulerSelect;
	private String rSchedulerWeek;
	private String rSchedulerDay;
	private String rSchedulerHour;
	private String rExecuteFlag;
	private String rExecuteDate;
	private String rExecuteTime;
	private String rCompleteDate;
	private String rCompleteTime;
	private String rErrorMessage;
	
	public String getExecuteName() {
		return executeName;
	}
	public void setExecuteName(String executeName) {
		this.executeName = executeName;
	}
	public String getJobName() {
		return jobName;
	}
	public String getrSchedulerSelect() {
		return rSchedulerSelect;
	}
	public String getrSchedulerWeek() {
		return rSchedulerWeek;
	}
	public String getrSchedulerDay() {
		return rSchedulerDay;
	}
	public String getrSchedulerHour() {
		return rSchedulerHour;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public void setrSchedulerSelect(String rSchedulerSelect) {
		this.rSchedulerSelect = rSchedulerSelect;
	}
	public void setrSchedulerWeek(String rSchedulerWeek) {
		this.rSchedulerWeek = rSchedulerWeek;
	}
	public void setrSchedulerDay(String rSchedulerDay) {
		this.rSchedulerDay = rSchedulerDay;
	}
	public void setrSchedulerHour(String rSchedulerHour) {
		this.rSchedulerHour = rSchedulerHour;
	}
	public String getrExecuteFlag() {
		return rExecuteFlag;
	}
	public String getrExecuteDate() {
		return rExecuteDate;
	}
	public void setrExecuteFlag(String rExecuteFlag) {
		this.rExecuteFlag = rExecuteFlag;
	}
	public void setrExecuteDate(String rExecuteDate) {
		this.rExecuteDate = rExecuteDate;
	}
	public String getrExecuteTime() {
		return rExecuteTime;
	}
	public String getrCompleteDate() {
		return rCompleteDate;
	}
	public String getrCompleteTime() {
		return rCompleteTime;
	}
	public void setrExecuteTime(String rExecuteTime) {
		this.rExecuteTime = rExecuteTime;
	}
	public void setrCompleteDate(String rCompleteDate) {
		this.rCompleteDate = rCompleteDate;
	}
	public void setrCompleteTime(String rCompleteTime) {
		this.rCompleteTime = rCompleteTime;
	}
	public String getrErrorMessage() {
		return rErrorMessage;
	}
	public void setrErrorMessage(String rErrorMessage) {
		this.rErrorMessage = rErrorMessage;
	}
	
	@Override
	public String toString() {
		return "ExecuteManage [executeName=" + executeName + ", jobName=" + jobName
				+ ", rSchedulerSelect=" + rSchedulerSelect + ", rSchedulerWeek=" + rSchedulerWeek
				+ ", rSchedulerDay=" + rSchedulerDay + ", rSchedulerHour=" + rSchedulerHour
				+ ", rExecuteFlag=" + rExecuteFlag + ", rExecuteDate=" + rExecuteDate
				+ ", rExecuteTime=" + rExecuteTime + ", rCompleteDate=" + rCompleteDate
				+ ", rCompleteTime=" + rCompleteTime + ", rErrorMessage=" + rErrorMessage + "]";
	}
	public String toLogString(MessageSource messageSource) {
		
		return 	messageSource.getMessage("log.ExecuteManage.ExecuteManage",null,Locale.getDefault())+" [ "  
				+ (executeName != 			null ? " "+messageSource.getMessage("log.ExecuteManage.executeName",null,Locale.getDefault())+"=" 				+executeName+" "		: "")
				+ (jobName != 				null ? " "+messageSource.getMessage("log.ExecuteManage.jobName",null,Locale.getDefault())+"=" 					+jobName+" "			: "")
				+ (rSchedulerSelect != 		null ? " "+messageSource.getMessage("log.ExecuteManage.rSchedulerSelect",null,Locale.getDefault())+"=" 			+rSchedulerSelect+" " 	: "")
				+ (rSchedulerWeek != 		null ? " "+messageSource.getMessage("log.ExecuteManage.rSchedulerWeek",null,Locale.getDefault())+"="	 		+rSchedulerWeek+" " 	: "")
				+ (rSchedulerDay != 		null ? " "+messageSource.getMessage("log.ExecuteManage.rSchedulerDay",null,Locale.getDefault())+"="	 			+rSchedulerDay+" " 		: "")
				+ (rSchedulerHour != 		null ? " "+messageSource.getMessage("log.ExecuteManage.rSchedulerHour",null,Locale.getDefault())+"="	 		+rSchedulerHour+" " 	: "")
				+ (rExecuteFlag != 			null ? " "+messageSource.getMessage("log.ExecuteManage.rExecuteFlag",null,Locale.getDefault())+"="	 			+rExecuteFlag+" " 		: "")
				+ (rExecuteDate != 			null ? " "+messageSource.getMessage("log.ExecuteManage.rExecuteDate",null,Locale.getDefault())+"="				+rExecuteDate+" " 		: "")
				+ (rExecuteTime != 			null ? " "+messageSource.getMessage("log.ExecuteManage.rExecuteTime",null,Locale.getDefault())+"="				+rExecuteTime+" " 		: "")
				+ (rCompleteDate != 		null ? " "+messageSource.getMessage("log.ExecuteManage.rCompleteDate",null,Locale.getDefault())+"="				+rCompleteDate+" " 		: "")
				+ (rCompleteTime != 		null ? " "+messageSource.getMessage("log.ExecuteManage.rCompleteTime",null,Locale.getDefault())+"="				+rCompleteTime+" " 		: "")
				+ (rErrorMessage != 		null ? " "+messageSource.getMessage("log.ExecuteManage.rErrorMessage",null,Locale.getDefault())+"="				+rErrorMessage+" " 		: "")
				+ "]";																								
	}
	
}
