package com.furence.recsee.admin.model;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class SQLManage {
	
	private String sqlName;
	private String sql;
	
	public String getSqlName() {
		return sqlName;
	}
	public String getSql() {
		return sql;
	}
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	@Override
	public String toString() {
		return "SQLManage [sqlName=" + sqlName + ", sql=" + sql + "]";
	}
	public String toLogString(MessageSource messageSource) {
		
		return 	messageSource.getMessage("log.SQLManage.SQLManage",null,Locale.getDefault())+" [ "  
				+ (sqlName != 		null ? " "+messageSource.getMessage("log.SQLManage.sqlName",null,Locale.getDefault())+"=" 			+sqlName+" "		: "")
				+ (sql != 			null ? " "+messageSource.getMessage("log.SQLManage.sql",null,Locale.getDefault())+"=" 				+sql+" " 		: "") 
				+ "]";																								
	}
	
}
