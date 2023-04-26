package com.furence.recsee.admin.dao;

import java.util.List;

import com.furence.recsee.admin.model.SQLManage;

public interface SQLManageDao {
	List<SQLManage> selectSQLManage(SQLManage sqlManage);
	SQLManage selectOneSQLManage(SQLManage sqlManage);
	Integer insertSQLManage(SQLManage sqlManage);
	Integer updateSQLManage(SQLManage sqlManage);
	Integer deleteSQLManage(SQLManage sqlManage);
}
