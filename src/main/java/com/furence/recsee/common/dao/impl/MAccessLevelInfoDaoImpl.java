package com.furence.recsee.common.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.dao.MAccessLevelInfoDao;
import com.furence.recsee.common.model.MAccessLevelInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("MAccessLevelInfoDao")
public class MAccessLevelInfoDaoImpl implements MAccessLevelInfoDao {

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
	public List<MAccessLevelInfo> selectAccessLevelInfo(MAccessLevelInfo accessLevelInfo) {
		// TODO Auto-generated method stub
		MAccessLevelInfoDao accessLevelInfoMapper = sqlSession.getMapper(MAccessLevelInfoDao.class);

		return accessLevelInfoMapper.selectAccessLevelInfo(accessLevelInfo);
	}

	@Override
	public Integer insertAccessLevelInfo(MAccessLevelInfo accessLevelInfO) {
		// TODO Auto-generated method stub
		MAccessLevelInfoDao accessLevelInfoMapper = sqlSession.getMapper(MAccessLevelInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertAccessLevelInfo").getBoundSql(accessLevelInfO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return accessLevelInfoMapper.insertAccessLevelInfo(accessLevelInfO);
	}

	@Override
	public Integer updateAccessLevelInfo(MAccessLevelInfo accessLevelInfo) {
		// TODO Auto-generated method stub
		MAccessLevelInfoDao accessLevelInfoMapper = sqlSession.getMapper(MAccessLevelInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateAccessLevelInfo").getBoundSql(accessLevelInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return accessLevelInfoMapper.updateAccessLevelInfo(accessLevelInfo);
	}

	@Override
	public Integer deleteAccessLevelInfo(MAccessLevelInfo accessLevelInfo) {
		// TODO Auto-generated method stub
		MAccessLevelInfoDao accessLevelInfoMapper = sqlSession.getMapper(MAccessLevelInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteAccessLevelInfo").getBoundSql(accessLevelInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return accessLevelInfoMapper.deleteAccessLevelInfo(accessLevelInfo);
	}

	@Override
	public MAccessLevelInfo checkAccessLevelInfo(MAccessLevelInfo accessLevelInfo) {
		MAccessLevelInfoDao accessLevelInfoMapper = sqlSession.getMapper(MAccessLevelInfoDao.class);

		return accessLevelInfoMapper.checkAccessLevelInfo(accessLevelInfo);
	}
	
	
	
	
	@Override
	public List<MAccessLevelInfo> selectAllowableInfo(MAccessLevelInfo accessLevelInfo) {
		// TODO Auto-generated method stub
		MAccessLevelInfoDao accessLevelInfoMapper = sqlSession.getMapper(MAccessLevelInfoDao.class);

		return accessLevelInfoMapper.selectAllowableInfo(accessLevelInfo);
	}

	@Override
	public Integer insertAllowableInfo(MAccessLevelInfo accessLevelInfO) {
		// TODO Auto-generated method stub
		MAccessLevelInfoDao accessLevelInfoMapper = sqlSession.getMapper(MAccessLevelInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertAllowableInfo").getBoundSql(accessLevelInfO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return accessLevelInfoMapper.insertAllowableInfo(accessLevelInfO);
	}

	@Override
	public Integer updateAllowableInfo(MAccessLevelInfo accessLevelInfo) {
		// TODO Auto-generated method stub
		MAccessLevelInfoDao accessLevelInfoMapper = sqlSession.getMapper(MAccessLevelInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateAllowableInfo").getBoundSql(accessLevelInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return accessLevelInfoMapper.updateAllowableInfo(accessLevelInfo);
	}

	@Override
	public Integer deleteAllowableInfo(MAccessLevelInfo accessLevelInfo) {
		// TODO Auto-generated method stub
		MAccessLevelInfoDao accessLevelInfoMapper = sqlSession.getMapper(MAccessLevelInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteAllowableInfo").getBoundSql(accessLevelInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return accessLevelInfoMapper.deleteAllowableInfo(accessLevelInfo);
	}
	
	@Override
	public MAccessLevelInfo checkAllowableInfo(MAccessLevelInfo accessLevelInfo) {
		MAccessLevelInfoDao accessLevelInfoMapper = sqlSession.getMapper(MAccessLevelInfoDao.class);

		return accessLevelInfoMapper.checkAllowableInfo(accessLevelInfo);
	}

}
