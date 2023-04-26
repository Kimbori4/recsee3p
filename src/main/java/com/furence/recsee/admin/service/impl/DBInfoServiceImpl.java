package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.DBInfoDao;
import com.furence.recsee.admin.model.DBInfo;
import com.furence.recsee.admin.service.DBInfoService;

@Service("dbInfoService")
public class DBInfoServiceImpl implements DBInfoService {
	
	@Autowired
	DBInfoDao dbInfoMapper;

	@Override
	public List<DBInfo> selectDBInfo(DBInfo dbInfo) {
		return dbInfoMapper.selectDBInfo(dbInfo);
	}
	
	@Override
	public List<DBInfo> selectOneDBInfo(DBInfo dbInfo) {
		return dbInfoMapper.selectOneDBInfo(dbInfo);
	}

	@Override
	public Integer insertDBInfo(DBInfo dbInfo) {
		return dbInfoMapper.insertDBInfo(dbInfo);
	}

	@Override
	public Integer updateDBInfo(DBInfo dbInfo) {
		return dbInfoMapper.updateDBInfo(dbInfo);
	}

	@Override
	public Integer deleteDBInfo(DBInfo dbInfo) {
		return dbInfoMapper.deleteDBInfo(dbInfo);
		
	}
	
}
