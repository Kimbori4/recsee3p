package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.SQLManageDao;
import com.furence.recsee.admin.model.SQLManage;
import com.furence.recsee.admin.service.SQLManageService;

@Service("sqlManageService")
public class SQLManageServiceImpl implements SQLManageService {
	
	@Autowired
	SQLManageDao sqlManageMapper;

	@Override
	public List<SQLManage> selectSQLManage(SQLManage sqlManage) {
		return sqlManageMapper.selectSQLManage(sqlManage);
	}
	
	@Override
	public SQLManage selectOneSQLManage(SQLManage sqlManage) {
		return sqlManageMapper.selectOneSQLManage(sqlManage);
	}

	@Override
	public Integer insertSQLManage(SQLManage sqlManage) {
		return sqlManageMapper.insertSQLManage(sqlManage);
	}

	@Override
	public Integer updateSQLManage(SQLManage sqlManage) {
		return sqlManageMapper.updateSQLManage(sqlManage);
	}

	@Override
	public Integer deleteSQLManage(SQLManage sqlManage) {
		return sqlManageMapper.deleteSQLManage(sqlManage);
		
	}
	
}
