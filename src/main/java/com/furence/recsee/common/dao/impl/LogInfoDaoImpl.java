package com.furence.recsee.common.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.dao.LogInfoDao;
import com.furence.recsee.common.model.LogInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("logInfoDao")
public class LogInfoDaoImpl implements LogInfoDao {
	private static final Logger logger = LoggerFactory.getLogger(LogInfoDaoImpl.class);
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
	public List<LogInfo> selectLogInfo(LogInfo logInfo) {
		LogInfoDao logInfoMapper = sqlSession.getMapper(LogInfoDao.class);
		return logInfoMapper.selectLogInfo(logInfo);
	}

	@Override
	public List<LogInfo> selectSystemLogInfo(LogInfo logInfo) {
		LogInfoDao logInfoMapper = sqlSession.getMapper(LogInfoDao.class);
		return logInfoMapper.selectSystemLogInfo(logInfo);
	}

	@Override
	public Integer totalLogInfo(LogInfo logInfo) {
		LogInfoDao logInfoMapper = sqlSession.getMapper(LogInfoDao.class);
		return logInfoMapper.totalLogInfo(logInfo);
	}

	@Override
	public Integer insertLogInfo(LogInfo logInfo) {
		LogInfoDao logInfoMapper = sqlSession.getMapper(LogInfoDao.class);
		try {
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertLogInfo").getBoundSql(logInfo);
		synchronizationUtil.SynchronizationInsert(query);
		}catch(Exception e) {
			logger.error("error",e);
		}
		return logInfoMapper.insertLogInfo(logInfo);
	}

	@Override
	public Integer deleteLogInfo(LogInfo logInfo) {
		LogInfoDao logInfoMapper = sqlSession.getMapper(LogInfoDao.class);
		try {
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteLogInfo").getBoundSql(logInfo);
		synchronizationUtil.SynchronizationInsert(query);
		}catch(Exception e) {
			logger.error("error",e);
		}
		return logInfoMapper.deleteLogInfo(logInfo);
	}
	
}
