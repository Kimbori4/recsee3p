package com.furence.recsee.common.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.dao.SystemInfoDao;
import com.furence.recsee.common.model.SystemInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("systemInfoDao")
public class SystemInfoDaoImpl implements SystemInfoDao {

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
	public List<SystemInfo> selectSystemInfo(SystemInfo systemInfo) {
		SystemInfoDao systemInfoMapper = sqlSession.getMapper(SystemInfoDao.class);

		return systemInfoMapper.selectSystemInfo(systemInfo);
	}

	@Override
	public Integer insertSystemInfo(SystemInfo systemInfo) {
		SystemInfoDao systemInfoMapper = sqlSession.getMapper(SystemInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSystemInfo").getBoundSql(systemInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return systemInfoMapper.insertSystemInfo(systemInfo);
	}

	@Override
	public Integer updateSystemInfo(SystemInfo systemInfo) {
		SystemInfoDao systemInfoMapper = sqlSession.getMapper(SystemInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateSystemInfo").getBoundSql(systemInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return systemInfoMapper.updateSystemInfo(systemInfo);
	}

	@Override
	public Integer deleteSystemInfo(SystemInfo systemInfo) {
		SystemInfoDao systemInfoMapper = sqlSession.getMapper(SystemInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteSystemInfo").getBoundSql(systemInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return systemInfoMapper.deleteSystemInfo(systemInfo);
	}

	@Override
	public List<SystemInfo> selectLicenceInfo(SystemInfo systemInfo) {
		SystemInfoDao systemInfoMapper = sqlSession.getMapper(SystemInfoDao.class);

		return systemInfoMapper.selectLicenceInfo(systemInfo);
	}

	@Override
	public Integer insertLicenceInfo(SystemInfo systemInfo) {
		SystemInfoDao systemInfoMapper = sqlSession.getMapper(SystemInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertLicenceInfo").getBoundSql(systemInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return systemInfoMapper.insertLicenceInfo(systemInfo);
	}

	@Override
	public Integer updateLicenceInfo(SystemInfo systemInfo) {
		SystemInfoDao systemInfoMapper = sqlSession.getMapper(SystemInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateLicenceInfo").getBoundSql(systemInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return systemInfoMapper.updateLicenceInfo(systemInfo);
	}

	@Override
	public Integer deleteLicenceInfo(SystemInfo systemInfo) {
		SystemInfoDao systemInfoMapper = sqlSession.getMapper(SystemInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteLicenceInfo").getBoundSql(systemInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return systemInfoMapper.deleteLicenceInfo(systemInfo);
	}

	@Override
	public Integer insertSysDeleteInfo(SystemInfo systemInfo) {
		SystemInfoDao systemInfoMapper = sqlSession.getMapper(SystemInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSysDeleteInfo").getBoundSql(systemInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return systemInfoMapper.insertSysDeleteInfo(systemInfo);
	}

	@Override
	public Integer deleteSysDeleteInfo(SystemInfo systemInfo) {
		SystemInfoDao systemInfoMapper = sqlSession.getMapper(SystemInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteSysDeleteInfo").getBoundSql(systemInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return systemInfoMapper.deleteSysDeleteInfo(systemInfo);
	}
}
