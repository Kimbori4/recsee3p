package com.furence.recsee.admin.model;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class JobManage {
	
	private String jobName;
	private String dbName;
	private String sqlName;
	
	public String getJobName() {
		return jobName;
	}
	public String getDbName() {
		return dbName;
	}
	public String getSqlName() {
		return sqlName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}
	
	@Override
	public String toString() {
		return "JobManage [jobName=" + jobName + ", dbName=" + dbName
				+ ", sqlName=" + sqlName + "]";
	}
	public String toLogString(MessageSource messageSource) {
		
		return 	messageSource.getMessage("log.JobManage.JobManage",null,Locale.getDefault())+" [ "  
				+ (jobName != 		null ? " "+messageSource.getMessage("log.JobManage.jobName",null,Locale.getDefault())+"=" 			+jobName+" "		: "")
				+ (dbName != 		null ? " "+messageSource.getMessage("log.JobManage.dbName",null,Locale.getDefault())+"=" 			+dbName+" " 		: "")
				+ (sqlName != 		null ? " "+messageSource.getMessage("log.JobManage.sqlName",null,Locale.getDefault())+"="	 		+sqlName+" " 		: "")
				+ "]";																								
	}
	
}
