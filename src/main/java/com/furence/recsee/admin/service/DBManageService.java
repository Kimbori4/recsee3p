package com.furence.recsee.admin.service;

import java.util.List;

import com.furence.recsee.admin.model.DBManage;

public interface DBManageService {
	List<DBManage> selectDBManage(DBManage dbManage);
	DBManage selectOneDBManage(DBManage dbManage);
	Integer insertDBManage(DBManage dbManage);
	Integer updateDBManage(DBManage dbManage);
	Integer deleteDBManage(DBManage dbManage);
}
