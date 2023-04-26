package com.furence.recsee.admin.service;

import java.util.List;

import com.furence.recsee.admin.model.DBInfo;
import com.furence.recsee.admin.model.DBSQLInfo;

public interface DBInfoService {
	List<DBInfo> selectDBInfo(DBInfo dbInfo);
	List<DBInfo> selectOneDBInfo(DBInfo dbInfo);
	Integer insertDBInfo(DBInfo dbInfo);
	Integer updateDBInfo(DBInfo dbInfo);
	Integer deleteDBInfo(DBInfo dbInfo);
}
