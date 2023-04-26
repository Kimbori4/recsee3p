package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.ScreenInfoDao;
import com.furence.recsee.admin.model.ScreenInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("screenInfoDao")
public class ScreenInfoDaoImpl implements ScreenInfoDao {
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
	public List<ScreenInfo> selectUserInfo(ScreenInfo screenInfo) {
		ScreenInfoDao userInfoMapper = sqlSession.getMapper(ScreenInfoDao.class);
		return userInfoMapper.selectUserInfo(screenInfo);
	}

	@Override
	public List<ScreenInfo> selectScreenUserInfo(ScreenInfo screenInfo) {
		ScreenInfoDao screenUserInfoMapper = sqlSession.getMapper(ScreenInfoDao.class);
		return screenUserInfoMapper.selectScreenUserInfo(screenInfo);
	}

	@Override
	public Integer updateScreenUser(ScreenInfo screenInfo) {
		ScreenInfoDao screenUserInfoMapper = sqlSession.getMapper(ScreenInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateScreenUser").getBoundSql(screenInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return screenUserInfoMapper.updateScreenUser(screenInfo);
	}


}
