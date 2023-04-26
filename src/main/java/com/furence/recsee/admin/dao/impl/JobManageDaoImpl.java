package com.furence.recsee.admin.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.JobManageDao;
import com.furence.recsee.admin.model.JobManage;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("jobManageDao")
public class JobManageDaoImpl implements JobManageDao {
	
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
	public List<JobManage> selectJobManage(JobManage jobManage) {
		JobManageDao jobManageMapper = sqlSession.getMapper(JobManageDao.class);
		return jobManageMapper.selectJobManage(jobManage);
	}
	
	@Override
	public JobManage selectOneJobManage(JobManage jobManage) {
		JobManageDao jobManageMapper = sqlSession.getMapper(JobManageDao.class);
		return jobManageMapper.selectOneJobManage(jobManage);
	}
	
	@Override
	public Integer insertJobManage(JobManage jobManage) {
		JobManageDao jobManageMapper = sqlSession.getMapper(JobManageDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertJobManage").getBoundSql(jobManage);
		synchronizationUtil.SynchronizationInsert(query);
		
		return jobManageMapper.insertJobManage(jobManage);
	}

	@Override
	public Integer updateJobManage(JobManage jobManage) {
		JobManageDao jobManageMapper = sqlSession.getMapper(JobManageDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateJobManage").getBoundSql(jobManage);
		synchronizationUtil.SynchronizationInsert(query);
		
		return jobManageMapper.updateJobManage(jobManage);
	}

	@Override
	public Integer deleteJobManage(JobManage jobManage) {
		JobManageDao jobManageMapper = sqlSession.getMapper(JobManageDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteJobManage").getBoundSql(jobManage);
		synchronizationUtil.SynchronizationInsert(query);
		
		return jobManageMapper.deleteJobManage(jobManage);
	}

}
