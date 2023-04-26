package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.PublicIpInfoDao;
import com.furence.recsee.admin.dao.SQLManageDao;
import com.furence.recsee.admin.model.PublicIpInfo;
import com.furence.recsee.admin.model.SQLManage;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("publicIpInfoDao")
public class PublicIpInfoDaoImpl implements PublicIpInfoDao {
	
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
	public List<PublicIpInfo> selectPublicIpInfo(PublicIpInfo publicIpInfo) {
		PublicIpInfoDao publicIpInfoMapper = sqlSession.getMapper(PublicIpInfoDao.class);
		return publicIpInfoMapper.selectPublicIpInfo(publicIpInfo);
	}

	@Override
	public PublicIpInfo selectOnePublicIpInfo(PublicIpInfo publicIpInfo) {
		PublicIpInfoDao publicIpInfoMapper = sqlSession.getMapper(PublicIpInfoDao.class);
		return publicIpInfoMapper.selectOnePublicIpInfo(publicIpInfo);
	}

	@Override
	public Integer insertPublicIpInfo(PublicIpInfo publicIpInfo) {
		PublicIpInfoDao publicIpInfoMapper = sqlSession.getMapper(PublicIpInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertPublicIpInfo").getBoundSql(publicIpInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return publicIpInfoMapper.insertPublicIpInfo(publicIpInfo);
	}

	@Override
	public Integer updatePublicIpInfo(PublicIpInfo publicIpInfo) {
		PublicIpInfoDao publicIpInfoMapper = sqlSession.getMapper(PublicIpInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updatePublicIpInfo").getBoundSql(publicIpInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return publicIpInfoMapper.updatePublicIpInfo(publicIpInfo);
	}

	@Override
	public Integer deletePublicIpInfo(PublicIpInfo publicIpInfo) {
		PublicIpInfoDao publicIpInfoMapper = sqlSession.getMapper(PublicIpInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deletePublicIpInfo").getBoundSql(publicIpInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return publicIpInfoMapper.deletePublicIpInfo(publicIpInfo);
	}
	
}
