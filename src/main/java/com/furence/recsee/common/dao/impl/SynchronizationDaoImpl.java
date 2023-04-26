package com.furence.recsee.common.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.furence.recsee.common.dao.SynchronizationDao;
import com.furence.recsee.common.model.SynchronizationVO;
import com.furence.recsee.common.service.SynchronizationService;

public class SynchronizationDaoImpl implements SynchronizationService  {

	@Autowired
	private SqlSession sqlSession;
	
	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	@Override
	public Integer insertSynchronizationInfo(SynchronizationVO synchronizationVO) {
		SynchronizationDao systemInfoMapper = sqlSession.getMapper(SynchronizationDao.class);

		return systemInfoMapper.insertSynchronizationInfo(synchronizationVO);
	}
}
