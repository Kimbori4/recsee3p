package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.DBInfoDao;
import com.furence.recsee.admin.dao.DBSQLInfoDao;
import com.furence.recsee.admin.model.DBInfo;
import com.furence.recsee.admin.model.DBSQLInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

public class DBSQLInfoDaoImpl implements DBSQLInfoDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public Integer insertDBSQLInfo(DBSQLInfo dbSQLInfo) {
		DBSQLInfoDao dbSQLInfoMapper = sqlSession.getMapper(DBSQLInfoDao.class);
			
		return dbSQLInfoMapper.insertDBSQLInfo(dbSQLInfo);
	}

	@Override
	public List<DBSQLInfo> selectDBSQLInfo(DBSQLInfo dbSQLInfo) {
		DBSQLInfoDao dbSQLInfoMapper = sqlSession.getMapper(DBSQLInfoDao.class);
		return dbSQLInfoMapper.selectDBSQLInfo(dbSQLInfo);
	}
	
	@Override
	public List<DBSQLInfo> selectDBSQLInfo2(DBSQLInfo dbSQLInfo) {
		DBSQLInfoDao dbSQLInfoMapper = sqlSession.getMapper(DBSQLInfoDao.class);
		return dbSQLInfoMapper.selectDBSQLInfo2(dbSQLInfo);
	}
	
	@Override
	public DBSQLInfo selectOneDBSQLInfo(DBSQLInfo dbSQLInfo) {
		DBSQLInfoDao dbSQLInfoMapper = sqlSession.getMapper(DBSQLInfoDao.class);
		return dbSQLInfoMapper.selectOneDBSQLInfo(dbSQLInfo);
	}
	
}
