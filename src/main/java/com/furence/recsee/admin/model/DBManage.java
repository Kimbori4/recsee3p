package com.furence.recsee.admin.model;

import java.util.Locale;


import org.springframework.context.MessageSource;

public class DBManage {
	
	private String dbName;
	private String dbServer;
	private String url;
	private String id;
	private String pw;
	private String timeout;
	
	public String getDbName() {
		return dbName;
	}
	public String getDbServer() {
		return dbServer;
	}
	public String getUrl() {
		return url;
	}
	public String getId() {
		return id;
	}
	public String getPw() {
		return pw;
	}
	public String getTimeout() {
		return timeout;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public void setDbServer(String dbServer) {
		this.dbServer = dbServer;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	
	@Override
	public String toString() {
		return "DBManage [dbName=" + dbName + ", dbServer=" + dbServer + ", url=" + url
				+ ", id=" + id + ", pw=" + pw + ", timeout=" + timeout + "]";
	}
	public String toLogString(MessageSource messageSource) {
		
		return 	messageSource.getMessage("log.DBManage.DBManage",null,Locale.getDefault())+" [ "  
				+ (dbName != 		null ? " "+messageSource.getMessage("log.DBManage.dbName",null,Locale.getDefault())+"=" 			+dbName+" "			: "")
				+ (dbServer != 		null ? " "+messageSource.getMessage("log.DBManage.dbServer",null,Locale.getDefault())+"=" 			+dbServer+" " 		: "") 
				+ (url != 			null ? " "+messageSource.getMessage("log.DBManage.url",null,Locale.getDefault())+"=" 				+url+" " 			: "")
				+ (id != 			null ? " "+messageSource.getMessage("log.DBManage.id",null,Locale.getDefault())+"=" 				+id+" " 			: "")
				+ (pw !=			null ? " "+messageSource.getMessage("log.DBManage.pw",null,Locale.getDefault())+"="					+pw+ " " 			: "")
				+ (timeout !=		null ? " "+messageSource.getMessage("log.DBManage.timeout",null,Locale.getDefault())+"="			+timeout+ " " 		: "")
				+ "]";																								
	}
	
}
