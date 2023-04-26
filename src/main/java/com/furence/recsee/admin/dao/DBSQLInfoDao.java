package com.furence.recsee.admin.dao;

import java.util.List;

import com.furence.recsee.admin.model.DBSQLInfo;

public interface DBSQLInfoDao {
	Integer insertDBSQLInfo(DBSQLInfo dbSQLInfo);
	List<DBSQLInfo> selectDBSQLInfo(DBSQLInfo dbSQLInfo);
	List<DBSQLInfo> selectDBSQLInfo2(DBSQLInfo dbSQLInfo);
	DBSQLInfo selectOneDBSQLInfo(DBSQLInfo dbSQLInfo);
}
