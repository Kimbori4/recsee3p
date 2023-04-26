package com.furence.recsee.admin.dao.impl;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.PwHistoryDao;
import com.furence.recsee.admin.model.PwHistory;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("pwHistoryDao")
public class PwHistoryDaoImpl implements PwHistoryDao {
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
	public Integer selectPwHistory(PwHistory pwHistory){
		PwHistoryDao rPwHistoryMapper = sqlSession.getMapper(PwHistoryDao.class);
		return rPwHistoryMapper.selectPwHistory(pwHistory);
	}
	
	@Override
	public Integer insertPwHistory(PwHistory pwHistory){
		PwHistoryDao rPwHistoryMapper = sqlSession.getMapper(PwHistoryDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertPwHistory").getBoundSql(pwHistory);
		synchronizationUtil.SynchronizationInsert(query);
		
		return rPwHistoryMapper.insertPwHistory(pwHistory);
	}
}
