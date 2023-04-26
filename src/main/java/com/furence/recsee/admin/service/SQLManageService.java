package com.furence.recsee.admin.service;

import java.util.List;

import com.furence.recsee.admin.model.SQLManage;

public interface SQLManageService {
	List<SQLManage> selectSQLManage(SQLManage sqlManage);
	SQLManage selectOneSQLManage(SQLManage sqlManage);
	Integer insertSQLManage(SQLManage sqlManage);
	Integer updateSQLManage(SQLManage sqlManage);
	Integer deleteSQLManage(SQLManage sqlManage);
}
