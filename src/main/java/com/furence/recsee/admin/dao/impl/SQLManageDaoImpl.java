package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.SQLManageDao;
import com.furence.recsee.admin.model.SQLManage;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

public class SQLManageDaoImpl implements SQLManageDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public List<SQLManage> selectSQLManage(SQLManage sqlManage) {
		SQLManageDao sqlManageMapper = sqlSession.getMapper(SQLManageDao.class);
		return sqlManageMapper.selectSQLManage(sqlManage);
	}
	
	@Override
	public SQLManage selectOneSQLManage(SQLManage sqlManage) {
		SQLManageDao sqlManageMapper = sqlSession.getMapper(SQLManageDao.class);
		return sqlManageMapper.selectOneSQLManage(sqlManage);
	}
	
	@Override
	public Integer insertSQLManage(SQLManage sqlManage) {
		SQLManageDao sqlManageMapper = sqlSession.getMapper(SQLManageDao.class);
	
		return sqlManageMapper.insertSQLManage(sqlManage);
	}

	@Override
	public Integer updateSQLManage(SQLManage sqlManage) {
		SQLManageDao sqlManageMapper = sqlSession.getMapper(SQLManageDao.class);
		
		return sqlManageMapper.updateSQLManage(sqlManage);
	}

	@Override
	public Integer deleteSQLManage(SQLManage sqlManage) {
		SQLManageDao sqlManageMapper = sqlSession.getMapper(SQLManageDao.class);
		
		return sqlManageMapper.deleteSQLManage(sqlManage);
	}

}
