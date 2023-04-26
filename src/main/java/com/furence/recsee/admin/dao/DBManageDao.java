package com.furence.recsee.admin.dao;

import java.util.List;

import com.furence.recsee.admin.model.DBInfo;
import com.furence.recsee.admin.model.DBManage;
import com.furence.recsee.admin.model.DBSQLInfo;

public interface DBManageDao {
	List<DBManage> selectDBManage(DBManage dbManage);
	DBManage selectOneDBManage(DBManage dbManage);
	Integer insertDBManage(DBManage dbManage);
	Integer updateDBManage(DBManage dbManage);
	Integer deleteDBManage(DBManage dbManage);
}
