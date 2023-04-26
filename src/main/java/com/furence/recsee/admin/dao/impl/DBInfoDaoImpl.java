package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.DBInfoDao;
import com.furence.recsee.admin.dao.DBSQLInfoDao;
import com.furence.recsee.admin.model.DBInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

public class DBInfoDaoImpl implements DBInfoDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public List<DBInfo> selectDBInfo(DBInfo dbInfo) {
		DBInfoDao dbInfoMapper = sqlSession.getMapper(DBInfoDao.class);
		return dbInfoMapper.selectDBInfo(dbInfo);
	}
	
	@Override
	public List<DBInfo> selectOneDBInfo(DBInfo dbInfo) {
		DBInfoDao dbInfoMapper = sqlSession.getMapper(DBInfoDao.class);
		return dbInfoMapper.selectOneDBInfo(dbInfo);
	}

	@Override
	public Integer insertDBInfo(DBInfo dbInfo) {
		DBInfoDao dbInfoMapper = sqlSession.getMapper(DBInfoDao.class);
				
		return dbInfoMapper.insertDBInfo(dbInfo);
	}

	@Override
	public Integer updateDBInfo(DBInfo dbInfo) {
		DBInfoDao dbInfoMapper = sqlSession.getMapper(DBInfoDao.class);
		
		return dbInfoMapper.updateDBInfo(dbInfo);
	}

	@Override
	public Integer deleteDBInfo(DBInfo dbInfo) {
		DBInfoDao dbInfoMapper = sqlSession.getMapper(DBInfoDao.class);
		
		return dbInfoMapper.deleteDBInfo(dbInfo);
	}

}
