package com.furence.recsee.admin.service;

import java.util.List;

import com.furence.recsee.admin.model.DBInfo;
import com.furence.recsee.admin.model.DBSQLInfo;

public interface DBSQLInfoService {
	Integer insertDBSQLInfo(DBSQLInfo dbSQLInfo);
	List<DBSQLInfo> selectDBSQLInfo(DBSQLInfo dbSQLInfo);
	List<DBSQLInfo> selectDBSQLInfo2(DBSQLInfo dbSQLInfo);
	DBSQLInfo selectOneDBSQLInfo(DBSQLInfo dbSQLInfo);
}
