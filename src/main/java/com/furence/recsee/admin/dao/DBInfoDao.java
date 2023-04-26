package com.furence.recsee.admin.dao;

import java.util.List;

import com.furence.recsee.admin.model.DBInfo;
import com.furence.recsee.admin.model.DBSQLInfo;

public interface DBInfoDao {
	List<DBInfo> selectDBInfo(DBInfo dbInfo);
	List<DBInfo> selectOneDBInfo(DBInfo dbInfo);
	Integer insertDBInfo(DBInfo dbInfo);
	Integer updateDBInfo(DBInfo dbInfo);
	Integer deleteDBInfo(DBInfo dbInfo);
}
