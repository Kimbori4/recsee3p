package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.SttServerInfoDao;
import com.furence.recsee.admin.model.SttServerInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("sttServerInfoDao")
public class SttServerInfoDaoImpl implements SttServerInfoDao {

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
	public List<SttServerInfo> selectSttServerInfo(SttServerInfo sttServerInfo) {
		SttServerInfoDao sttServerInfoMapper = sqlSession.getMapper(SttServerInfoDao.class);

		return sttServerInfoMapper.selectSttServerInfo(sttServerInfo);
	}

	@Override
	public Integer insertSttServerInfo(SttServerInfo sttServerInfo) {
		SttServerInfoDao sttServerInfoMapper = sqlSession.getMapper(SttServerInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSttServerInfo").getBoundSql(sttServerInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return sttServerInfoMapper.insertSttServerInfo(sttServerInfo);
	}

	@Override
	public Integer updateSttServerInfo(SttServerInfo sttServerInfo) {
		SttServerInfoDao sttServerInfoMapper = sqlSession.getMapper(SttServerInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateSttServerInfo").getBoundSql(sttServerInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return sttServerInfoMapper.updateSttServerInfo(sttServerInfo);
	}

	@Override
	public Integer deleteSttServerInfo(SttServerInfo sttServerInfo) {
		SttServerInfoDao sttServerInfoMapper = sqlSession.getMapper(SttServerInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteSttServerInfo").getBoundSql(sttServerInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return sttServerInfoMapper.deleteSttServerInfo(sttServerInfo);
	}

}
