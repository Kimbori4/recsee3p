package com.furence.recsee.common.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.dao.OrganizationInfoDao;
import com.furence.recsee.common.model.OrganizationInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("organizationInfoDao")
public class OrganizationInfoDaoImpl implements OrganizationInfoDao{

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
	public List<OrganizationInfo> selectOrganizationBgInfo(
			OrganizationInfo organizationInfo) {
		OrganizationInfoDao organizationInfoMapper = sqlSession.getMapper(OrganizationInfoDao.class);
		return organizationInfoMapper.selectOrganizationBgInfo(organizationInfo);
	}

	@Override
	public List<OrganizationInfo> selectOrganizationMgInfo(
			OrganizationInfo organizationInfo) {
		OrganizationInfoDao organizationInfoMapper = sqlSession.getMapper(OrganizationInfoDao.class);
		return organizationInfoMapper.selectOrganizationMgInfo(organizationInfo);
	}

	@Override
	public List<OrganizationInfo> selectOrganizationSgInfo(
			OrganizationInfo organizationInfo) {
		OrganizationInfoDao organizationInfoMapper = sqlSession.getMapper(OrganizationInfoDao.class);
		return organizationInfoMapper.selectOrganizationSgInfo(organizationInfo);
	}

	@Override
	public HashMap<String, String> selectOrganizationLastInfo(OrganizationInfo organizationInfo) {
		OrganizationInfoDao organizationInfoMapper = sqlSession.getMapper(OrganizationInfoDao.class);
		return organizationInfoMapper.selectOrganizationLastInfo(organizationInfo);
	}

	@Override
	public Integer insertOrganizationInfo(OrganizationInfo organizationInfo) {
		OrganizationInfoDao organizationInfoMapper = sqlSession.getMapper(OrganizationInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertOrganizationInfo").getBoundSql(organizationInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return organizationInfoMapper.insertOrganizationInfo(organizationInfo);
	}

	@Override
	public Integer updateOrganizationInfo(OrganizationInfo organizationInfo) {
		OrganizationInfoDao organizationInfoMapper = sqlSession.getMapper(OrganizationInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateOrganizationInfo").getBoundSql(organizationInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return organizationInfoMapper.updateOrganizationInfo(organizationInfo);
	}

	@Override
	public Integer deleteOrganizationInfo(OrganizationInfo organizationInfo) {
		OrganizationInfoDao organizationInfoMapper = sqlSession.getMapper(OrganizationInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteOrganizationInfo").getBoundSql(organizationInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return organizationInfoMapper.deleteOrganizationInfo(organizationInfo);
	}

	@Override
	public List<OrganizationInfo> sameNameOrganizationInfo(OrganizationInfo organizationInfo) {
		OrganizationInfoDao organizationInfoMapper = sqlSession.getMapper(OrganizationInfoDao.class);
		return organizationInfoMapper.sameNameOrganizationInfo(organizationInfo);
	}

	@Override
	public Integer updateGroupCodeInfo(OrganizationInfo organizationInfo) {
		OrganizationInfoDao organizationInfoMapper = sqlSession.getMapper(OrganizationInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateGroupCodeInfo").getBoundSql(organizationInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return organizationInfoMapper.updateGroupCodeInfo(organizationInfo);
	}

	@Override
	public List<OrganizationInfo> selectOraganiztionAll(OrganizationInfo organizationInfo) {
		OrganizationInfoDao organizationInfoMapper = sqlSession.getMapper(OrganizationInfoDao.class);
		return organizationInfoMapper.selectOraganiztionAll(organizationInfo);
	}

	@Override
	public Integer updateUseBg(OrganizationInfo bgcode) {
		OrganizationInfoDao organizationInfoMapper = sqlSession.getMapper(OrganizationInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateUseBg").getBoundSql(bgcode);
		synchronizationUtil.SynchronizationInsert(query);
		
		return organizationInfoMapper.updateUseBg(bgcode);
	}

	@Override
	public List<OrganizationInfo> selectCallCenterMgInfo(OrganizationInfo organizationInfo) {
		OrganizationInfoDao organizationInfoMapper = sqlSession.getMapper(OrganizationInfoDao.class);
		return organizationInfoMapper.selectCallCenterMgInfo(organizationInfo);
	}
	
	@Override
	public List<OrganizationInfo> selectOrganizationCBgInfo(OrganizationInfo organizationInfo) {
		OrganizationInfoDao organizationInfoMapper = sqlSession.getMapper(OrganizationInfoDao.class);
		return organizationInfoMapper.selectOrganizationCBgInfo(organizationInfo);
	}
	
	@Override
	public List<OrganizationInfo> selectOrganizationCMgInfo(OrganizationInfo organizationInfo) {
		OrganizationInfoDao organizationInfoMapper = sqlSession.getMapper(OrganizationInfoDao.class);
		return organizationInfoMapper.selectOrganizationCMgInfo(organizationInfo);
	}
	
	@Override
	public List<OrganizationInfo> selectOrganizationCSgInfo(OrganizationInfo organizationInfo) {
		OrganizationInfoDao organizationInfoMapper = sqlSession.getMapper(OrganizationInfoDao.class);
		return organizationInfoMapper.selectOrganizationCSgInfo(organizationInfo);
	}
}
