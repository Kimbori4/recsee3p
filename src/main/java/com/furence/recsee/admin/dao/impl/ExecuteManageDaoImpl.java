package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.ExecuteManageDao;
import com.furence.recsee.admin.dao.JobManageDao;
import com.furence.recsee.admin.model.ExecuteManage;
import com.furence.recsee.admin.model.JobManage;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("executeManageDao")
public class ExecuteManageDaoImpl implements ExecuteManageDao {
	
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
	public List<ExecuteManage> selectExecuteManage(ExecuteManage executeManage) {
		ExecuteManageDao executeManageMapper = sqlSession.getMapper(ExecuteManageDao.class);
		return executeManageMapper.selectExecuteManage(executeManage);
	}
	
	@Override
	public ExecuteManage selectOneExecuteManage(ExecuteManage executeManage) {
		ExecuteManageDao executeManageMapper = sqlSession.getMapper(ExecuteManageDao.class);
		return executeManageMapper.selectOneExecuteManage(executeManage);
	}
	
	@Override
	public Integer insertExecuteManage(ExecuteManage executeManage) {
		ExecuteManageDao executeManageMapper = sqlSession.getMapper(ExecuteManageDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertExecuteManage").getBoundSql(executeManage);
		synchronizationUtil.SynchronizationInsert(query);
		
		return executeManageMapper.insertExecuteManage(executeManage);
	}

	@Override
	public Integer updateExecuteManage(ExecuteManage executeManage) {
		ExecuteManageDao executeManageMapper = sqlSession.getMapper(ExecuteManageDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateExecuteManage").getBoundSql(executeManage);
		synchronizationUtil.SynchronizationInsert(query);
		
		return executeManageMapper.updateExecuteManage(executeManage);
	}
	
	@Override
	public Integer updateFlagExecuteManage(ExecuteManage executeManage) {
		ExecuteManageDao executeManageMapper = sqlSession.getMapper(ExecuteManageDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateFlagExecuteManage").getBoundSql(executeManage);
		synchronizationUtil.SynchronizationInsert(query);
		
		return executeManageMapper.updateFlagExecuteManage(executeManage);
	}
	
	@Override
	public Integer updateStatusExecuteManage(ExecuteManage executeManage) {
		ExecuteManageDao executeManageMapper = sqlSession.getMapper(ExecuteManageDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateStatusExecuteManage").getBoundSql(executeManage);
		synchronizationUtil.SynchronizationInsert(query);
		
		return executeManageMapper.updateStatusExecuteManage(executeManage);
	}

	@Override
	public Integer deleteExecuteManage(ExecuteManage executeManage) {
		ExecuteManageDao executeManageMapper = sqlSession.getMapper(ExecuteManageDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteExecuteManage").getBoundSql(executeManage);
		synchronizationUtil.SynchronizationInsert(query);
		
		return executeManageMapper.deleteExecuteManage(executeManage);
	}
	
	@Override
	public Integer deleteContainExecuteManage(ExecuteManage executeManage) {
		ExecuteManageDao executeManageMapper = sqlSession.getMapper(ExecuteManageDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteContainExecuteManage").getBoundSql(executeManage);
		synchronizationUtil.SynchronizationInsert(query);
		
		return executeManageMapper.deleteContainExecuteManage(executeManage);
	}

}
