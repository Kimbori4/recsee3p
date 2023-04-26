package com.furence.recsee.admin.model;

public class DBSQLInfo {
	private String dbSQLName;
	private String dbSQLDescription;
	private String dbSQLContent;
	private String dbSQLType;
	private String dbName;
	private String dbSQLColumn;
	
	public String getDbSQLColumn() {
		return dbSQLColumn;
	}
	public void setDbSQLColumn(String dbSQLColumn) {
		this.dbSQLColumn = dbSQLColumn;
	}
	public String getDbSQLType() {
		return dbSQLType;
	}
	public void setDbSQLType(String dbSQLType) {
		this.dbSQLType = dbSQLType;
	}
	public String getDbSQLName() {
		return dbSQLName;
	}
	public String getDbSQLDescription() {
		return dbSQLDescription;
	}
	public String getDbSQLContent() {
		return dbSQLContent;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbSQLName(String dbSQLName) {
		this.dbSQLName = dbSQLName;
	}
	public void setDbSQLDescription(String dbSQLDescription) {
		this.dbSQLDescription = dbSQLDescription;
	}
	public void setDbSQLContent(String dbSQLContent) {
		this.dbSQLContent = dbSQLContent;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
}
