package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.DBManageDao;
import com.furence.recsee.admin.model.DBManage;
import com.furence.recsee.admin.service.DBManageService;

@Service("dbManageService")
public class DBManageServiceImpl implements DBManageService {
	
	@Autowired
	DBManageDao dbManageMapper;

	@Override
	public List<DBManage> selectDBManage(DBManage dbManage) {
		return dbManageMapper.selectDBManage(dbManage);
	}
	
	@Override
	public DBManage selectOneDBManage(DBManage dbManage) {
		return dbManageMapper.selectOneDBManage(dbManage);
	}

	@Override
	public Integer insertDBManage(DBManage dbManage) {
		return dbManageMapper.insertDBManage(dbManage);
	}

	@Override
	public Integer updateDBManage(DBManage dbManage) {
		return dbManageMapper.updateDBManage(dbManage);
	}

	@Override
	public Integer deleteDBManage(DBManage dbManage) {
		return dbManageMapper.deleteDBManage(dbManage);
		
	}
	
}
