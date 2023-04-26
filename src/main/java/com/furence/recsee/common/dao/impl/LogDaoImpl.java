package com.furence.recsee.common.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.dao.LogDao;
import com.furence.recsee.common.model.Log;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("logDao")
public class LogDaoImpl implements LogDao {
	private static final Logger logger = LoggerFactory.getLogger(LogDaoImpl.class);
	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private SynchronizationService synchronizationService;
	
	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public List<Log> selectLog(Log log) {
		LogDao logMapper = sqlSession.getMapper(LogDao.class);
		return logMapper.selectLog(log);
	}
	
	@Override
	public List<Log> selectLogName(Log log){
		LogDao logMapper = sqlSession.getMapper(LogDao.class);
		return logMapper.selectLogName(log);
	}
	
	@Override
	public List<Log> selectLogContents(Log log){
		LogDao logMapper = sqlSession.getMapper(LogDao.class);
		return logMapper.selectLogContents(log);
	}

	@Override
	public Integer totalLog(Log log) {
		LogDao logMapper = sqlSession.getMapper(LogDao.class);
		return logMapper.totalLog(log);
	}

	@Override
	public Integer insertLog(Log log) {
		LogDao logMapper = sqlSession.getMapper(LogDao.class);
		
		try {
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertLog").getBoundSql(log);
		synchronizationUtil.SynchronizationInsert(query);
		}catch(Exception e) {
			logger.error("error",e);
		}
		return logMapper.insertLog(log);
	}

	@Override
	public Integer deleteLog(Log log) {
		LogDao logMapper = sqlSession.getMapper(LogDao.class);
		
		try {
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteLog").getBoundSql(log);
		synchronizationUtil.SynchronizationInsert(query);
		}catch(Exception e) {
			logger.error("error",e);
		}
		return logMapper.deleteLog(log);
	}
}
