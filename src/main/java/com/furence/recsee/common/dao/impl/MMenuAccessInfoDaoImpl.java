package com.furence.recsee.common.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.dao.MMenuAccessInfoDao;
import com.furence.recsee.common.model.MMenuAccessInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("MMenuAccessInfoDao")
public class MMenuAccessInfoDaoImpl implements MMenuAccessInfoDao {

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
	public List<MMenuAccessInfo> selectMenuAccessInfo(MMenuAccessInfo menuAccessInfo) {

		MMenuAccessInfoDao menuAccessInfoMapper = sqlSession.getMapper(MMenuAccessInfoDao.class);

		return menuAccessInfoMapper.selectMenuAccessInfo(menuAccessInfo);
	}

	@Override
	public List<MMenuAccessInfo> checkAccessInfo(MMenuAccessInfo menuAccessInfo) {
		MMenuAccessInfoDao menuAccessInfoMapper = sqlSession.getMapper(MMenuAccessInfoDao.class);

		return menuAccessInfoMapper.checkAccessInfo(menuAccessInfo);
	}

	@Override
	public List<MMenuAccessInfo> selectSubMenuAccessInfo(MMenuAccessInfo menuAccessInfo) {

		MMenuAccessInfoDao menuAccessInfoMapper = sqlSession.getMapper(MMenuAccessInfoDao.class);

		return menuAccessInfoMapper.selectSubMenuAccessInfo(menuAccessInfo);
	}

	@Override
	public Integer updateMenuAccessInfo(MMenuAccessInfo menuAccessInfo) {
		MMenuAccessInfoDao menuAccessInfoMapper = sqlSession.getMapper(MMenuAccessInfoDao.class);
	
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateMenuAccessInfo").getBoundSql(menuAccessInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return menuAccessInfoMapper.updateMenuAccessInfo(menuAccessInfo);
	}
	
	@Override
	public Integer updatecAccessInfo(MMenuAccessInfo menuAccessInfo) {
		MMenuAccessInfoDao menuAccessInfoMapper = sqlSession.getMapper(MMenuAccessInfoDao.class);
	
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updatecAccessInfo").getBoundSql(menuAccessInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return menuAccessInfoMapper.updatecAccessInfo(menuAccessInfo);
	}

	@Override
	public Integer deleteMenuAccessInfo(MMenuAccessInfo menuAccessInfo) {
		MMenuAccessInfoDao menuAccessInfoMapper = sqlSession.getMapper(MMenuAccessInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteMenuAccessInfo").getBoundSql(menuAccessInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return menuAccessInfoMapper.deleteMenuAccessInfo(menuAccessInfo);
	}

	@Override
	public Integer insertMenuAccessInfo(MMenuAccessInfo menuAccessInfo) {
		MMenuAccessInfoDao menuAccessInfoMapper = sqlSession.getMapper(MMenuAccessInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertMenuAccessInfo").getBoundSql(menuAccessInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return menuAccessInfoMapper.insertMenuAccessInfo(menuAccessInfo);
	}
	
	@Override
	public Integer accessLevelEmpty(MMenuAccessInfo menuAccessInfo) {
		MMenuAccessInfoDao menuAccessInfoMapper = sqlSession.getMapper(MMenuAccessInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("accessLevelEmpty").getBoundSql(menuAccessInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return menuAccessInfoMapper.accessLevelEmpty(menuAccessInfo);
	}

	@Override
	public List<MMenuAccessInfo> selectHideMenuAccessInfo() {
		MMenuAccessInfoDao menuAccessInfoMapper = sqlSession.getMapper(MMenuAccessInfoDao.class);

		return menuAccessInfoMapper.selectHideMenuAccessInfo();
	}
}
