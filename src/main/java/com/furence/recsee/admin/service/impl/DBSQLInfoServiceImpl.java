package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.DBInfoDao;
import com.furence.recsee.admin.dao.DBSQLInfoDao;
import com.furence.recsee.admin.model.DBInfo;
import com.furence.recsee.admin.model.DBSQLInfo;
import com.furence.recsee.admin.service.DBInfoService;
import com.furence.recsee.admin.service.DBSQLInfoService;

@Service("dbSQLInfoService")
public class DBSQLInfoServiceImpl implements DBSQLInfoService {
	
	@Autowired
	DBSQLInfoDao dbSQLInfoMapper;

	@Override
	public Integer insertDBSQLInfo(DBSQLInfo dbSQLInfo) {
		return dbSQLInfoMapper.insertDBSQLInfo(dbSQLInfo);
	}

	@Override
	public List<DBSQLInfo> selectDBSQLInfo(DBSQLInfo dbSQLInfo) {
		return dbSQLInfoMapper.selectDBSQLInfo(dbSQLInfo);
	}
	
	@Override
	public List<DBSQLInfo> selectDBSQLInfo2(DBSQLInfo dbSQLInfo) {
		return dbSQLInfoMapper.selectDBSQLInfo2(dbSQLInfo);
	}
	
	@Override
	public DBSQLInfo selectOneDBSQLInfo(DBSQLInfo dbSQLInfo) {
		return dbSQLInfoMapper.selectOneDBSQLInfo(dbSQLInfo);
	}
	
}
