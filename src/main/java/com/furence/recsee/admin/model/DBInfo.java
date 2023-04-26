package com.furence.recsee.admin.model;

public class DBInfo {
	private String dbName;
	private String dbDriver;
	private String dbUrl;
	private String dbUser;
	private String dbPassword;
	
	public String getDbName() {
		return dbName;
	}
	public String getDbDriver() {
		return dbDriver;
	}
	public String getDbUrl() {
		return dbUrl;
	}
	public String getDbUser() {
		return dbUser;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	
	@Override
	public String toString() {
		return "DBInfo [dbName=" + dbName + ", dbDriver=" + dbDriver + ", dbUrl=" + dbUrl
				+ ", dbUser=" + dbUser + ", dbPassword=" + dbPassword+ "]";
	}
	public String toLogString() {
		
		return 	"DB 정보[ "  
				+ (dbName != 		null ? " DB이름=" 			+dbName+" "		: "")
				+ (dbDriver != 		null ? " 드라이버=" 		+dbDriver+" " 		: "") 
				+ (dbUrl != 		null ? " URL=" 			+dbUrl+" " 		: "")
				+ (dbUser != 		null ? " 사용자=" 			+dbUser+" " 			: "")
				+ (dbPassword !=	null ? " 비밀번호="		+dbPassword+ " " 		: "")
				+ "]";																								
	}
	
}
