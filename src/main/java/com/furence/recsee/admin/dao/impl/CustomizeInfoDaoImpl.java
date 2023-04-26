package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.CustomizeInfoDao;
import com.furence.recsee.admin.model.CustomizeCopyListInfo;
import com.furence.recsee.admin.model.CustomizeItemInfo;
import com.furence.recsee.admin.model.CustomizeListInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("customizeInfoDao")
public class CustomizeInfoDaoImpl implements CustomizeInfoDao {
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
	public List<CustomizeListInfo> selectCustomizeListInfo(CustomizeListInfo customizeListInfo) {
		CustomizeInfoDao customizeMapper = sqlSession.getMapper(CustomizeInfoDao.class);
		return customizeMapper.selectCustomizeListInfo(customizeListInfo);
	}

	@Override
	public Integer insertCustomizeListInfo(CustomizeListInfo customizeListInfo) {
		CustomizeInfoDao customizeMapper = sqlSession.getMapper(CustomizeInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertCustomizeListInfo").getBoundSql(customizeListInfo);
		synchronizationUtil.SynchronizationInsert(query);
				
		return customizeMapper.insertCustomizeListInfo(customizeListInfo);
	}

	@Override
	public Integer updateCustomizeListInfo(CustomizeListInfo customizeListInfo) {
		CustomizeInfoDao customizeMapper = sqlSession.getMapper(CustomizeInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateCustomizeListInfo").getBoundSql(customizeListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return customizeMapper.updateCustomizeListInfo(customizeListInfo);
	}

	@Override
	public Integer deleteCustomizeListInfo(CustomizeListInfo customizeListInfo) {
		CustomizeInfoDao customizeMapper = sqlSession.getMapper(CustomizeInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteCustomizeListInfo").getBoundSql(customizeListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return customizeMapper.deleteCustomizeListInfo(customizeListInfo);
	}

	@Override
	public List<CustomizeItemInfo> selectCustomizeItemInfo(CustomizeItemInfo customizeItemInfo) {
		CustomizeInfoDao customizeMapper = sqlSession.getMapper(CustomizeInfoDao.class);
		return customizeMapper.selectCustomizeItemInfo(customizeItemInfo);
	}

	@Override
	public Integer insertCustomizeItemInfo(CustomizeItemInfo customizeItemInfo) {
		CustomizeInfoDao customizeMapper = sqlSession.getMapper(CustomizeInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertCustomizeItemInfo").getBoundSql(customizeItemInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return customizeMapper.insertCustomizeItemInfo(customizeItemInfo);
	}

	@Override
	public Integer updateCustomizeItemInfo(CustomizeItemInfo customizeItemInfo) {
		CustomizeInfoDao customizeMapper = sqlSession.getMapper(CustomizeInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateCustomizeItemInfo").getBoundSql(customizeItemInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return customizeMapper.updateCustomizeItemInfo(customizeItemInfo);
	}

	@Override
	public Integer deleteCustomizeItemInfo(CustomizeItemInfo customizeItemInfo) {
		CustomizeInfoDao customizeMapper = sqlSession.getMapper(CustomizeInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteCustomizeItemInfo").getBoundSql(customizeItemInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return customizeMapper.deleteCustomizeItemInfo(customizeItemInfo);
	}

	@Override
	public List<CustomizeCopyListInfo> selectCustomizeCopyListInfo(CustomizeCopyListInfo customizeListInfo) {
		CustomizeInfoDao customizeMapper = sqlSession.getMapper(CustomizeInfoDao.class);
		return customizeMapper.selectCustomizeCopyListInfo(customizeListInfo);
	}
	@Override
	public Integer insertCustomizeCopyListInfo(CustomizeCopyListInfo customizeListInfo) {
		CustomizeInfoDao customizeMapper = sqlSession.getMapper(CustomizeInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertCustomizeCopyListInfo").getBoundSql(customizeListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return customizeMapper.insertCustomizeCopyListInfo(customizeListInfo);
	}

	@Override
	public Integer updateCustomizeCopyListInfo(CustomizeCopyListInfo customizeListInfo) {
		CustomizeInfoDao customizeMapper = sqlSession.getMapper(CustomizeInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateCustomizeCopyListInfo").getBoundSql(customizeListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return customizeMapper.updateCustomizeCopyListInfo(customizeListInfo);
	}

	@Override
	public Integer deleteCustomizeCopyListInfo(CustomizeCopyListInfo customizeListInfo) {
		CustomizeInfoDao customizeMapper = sqlSession.getMapper(CustomizeInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteCustomizeCopyListInfo").getBoundSql(customizeListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return customizeMapper.deleteCustomizeCopyListInfo(customizeListInfo);
	}

}
