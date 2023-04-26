package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.QueueInfoDao;
import com.furence.recsee.admin.model.QueueInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("queueInfoDao")
public class QueueInfoDaoImpl implements QueueInfoDao {
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
	public List<QueueInfo> selectQueueInfo(QueueInfo queueInfo) {
		QueueInfoDao queueInfoMapper = sqlSession.getMapper(QueueInfoDao.class);
		return queueInfoMapper.selectQueueInfo(queueInfo);
	}

	@Override
	public Integer insertQueueInfo(QueueInfo queueInfo) {
		QueueInfoDao queueInfoMapper = sqlSession.getMapper(QueueInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertQueueInfo").getBoundSql(queueInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return queueInfoMapper.insertQueueInfo(queueInfo);
	}

	@Override
	public Integer updateQueueInfo(QueueInfo queueInfo) {
		QueueInfoDao queueInfoMapper = sqlSession.getMapper(QueueInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateQueueInfo").getBoundSql(queueInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return queueInfoMapper.updateQueueInfo(queueInfo);
	}

	@Override
	public Integer deleteQueueInfo(QueueInfo queueInfo) {
		QueueInfoDao queueInfoMapper = sqlSession.getMapper(QueueInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteQueueInfo").getBoundSql(queueInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return queueInfoMapper.deleteQueueInfo(queueInfo);
	}

}
